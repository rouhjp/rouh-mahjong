package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.tile.Tile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * 麻雀卓を描画するパネルクラス。
 * @author Rouh
 * @version 1.0
 */
public class BlockImageTable extends JLayeredPane{
    private static final int MELD_MARGIN = BlockLabel.WIDTH/2;
    static final int BLOCK_WIDTH = 20;
    static final int BLOCK_HEIGHT = 30;
    static final int TABLE_WIDTH = 580;
    static final int TABLE_HEIGHT = 580;
    private final Map<Direction, AreaInfo> areaInfo = new HashMap<>();

    /**
     * 画面方向ごとの各種内部情報を保持するクラス。
     */
    private static class AreaInfo{
        /** 捨て牌の牌ラベルの参照 */
        private final BlockLabel[] riverBlockReference = new BlockLabel[24];
        /** 山牌の牌ラベルの参照 */
        private final BlockLabel[][] wallBlockReferences = new BlockLabel[17][2];
        /** 加槓時の描画処理の登録先 */
        private final Map<Integer, Consumer<Tile>> addingActions = new HashMap<>();
        /** 副露置き場の右端からの累積距離 */
        private int totalMeldOffset = 0;
        /** 副露の数 */
        private int nextMeldIndex = 0;
        /** 捨て牌の数 */
        private int nextRiverIndex = 0;
        /** リーチ宣言牌の位置 */
        private int readyRiverIndex = -1;
    }

    BlockImageTable(){
        setSize(TABLE_WIDTH, TABLE_HEIGHT);
        setBorder(new LineBorder(Color.BLACK));
        for (var dir:Direction.values()){
            areaInfo.put(dir, new AreaInfo());
        }
    }

    /**
     * 牌ラベルを指定した位置に追加する。
     * @param label ラベル
     * @param x ラベルの座標(x) 0..width
     * @param y ラベルの座標(y) 0..height
     * @param z ラベルの高さ(z) 0..1
     */
    private void putBlock(BlockLabel label, int x, int y, int z){
        label.setLocationCentered(x, y);
        add(label);
        setLayer(label, z*TABLE_HEIGHT + y);
    }

    /**
     * 指定した位置に副露面子構成牌の牌ラベルを縦向きに描画します。
     * @param d 方向
     * @param offset 副露位置(指定方向から見た画面右端からの位置)
     * @param tile 牌
     */
    private void putMeldTile(Direction d, int offset, Tile tile){
        var point = BlockPoints.ofMeldBlock(d, offset, false, false);
        var block = BlockLabel.ofFaceUp(d.reversed(), tile);
        putBlock(block, point.x, point.y, 0);
    }

    /**
     * 指定した位置に伏せた牌ラベルを描画します。
     * @param d 方向
     * @param offset 副露位置(指定方向から見た画面右端からの位置)
     */
    private void putMeldTileFaceDown(Direction d, int offset){
        var point = BlockPoints.ofMeldBlock(d, offset, false, false);
        var block = BlockLabel.ofFaceDown(d.reversed());
        putBlock(block, point.x, point.y, 0);
    }

    /**
     * 指定した位置に副露面子構成牌の牌ラベルを横向き・下揃えに描画します。
     * @param d 方向
     * @param offset 副露位置(指定方向から見た画面右端からの位置)
     * @param tile 牌
     */
    private void putMeldTileRotated(Direction d, int offset, Tile tile, UnaryOperator<Direction> rotate){
        var point = BlockPoints.ofMeldBlock(d, offset, true, false);
        var block = BlockLabel.ofFaceUp(rotate.apply(d.reversed()), tile);
        putBlock(block, point.x, point.y, 0);
    }

    /**
     * 指定した位置に副露面子構成牌の牌ラベルを横向き・上揃えに描画します。
     * @param d 方向
     * @param offset 副露位置(指定方向から見た画面右端からの位置)
     * @param tile 牌
     * @param rotation 牌を倒す向き {@link Direction::turnLeft} または {@link Direction::turnRight}
     */
    private void putMeldTileAdded(Direction d, int offset, Tile tile, UnaryOperator<Direction> rotation){
        var point = BlockPoints.ofMeldBlock(d, offset, true, true);
        var block = BlockLabel.ofFaceUp(rotation.apply(d.reversed()), tile);
        putBlock(block, point.x, point.y, 0);
    }

    /**
     * 指定された方向の副露置き場に, 面子(明順子/明刻子/大明槓)を描画します。<br>
     * 面子が槓子でない場合, 予め加槓時の描画処理を内部に登録します。
     * @param d 方向
     * @param rotation 牌を倒す向き {@link Direction::turnLeft} または {@link Direction::turnRight}
     * @param tiltIndex 倒す牌の左からの位置(0..3)
     * @param tiles 牌(3..4)
     */
    private void putTiltMeld(Direction d, UnaryOperator<Direction> rotation, int tiltIndex, Tile...tiles){
        if (tiles.length!=3 && tiles.length!=4){
            throw new IllegalArgumentException("illegal length of meld tile");
        }
        for (int i = tiles.length - 1; i>=0; i--){
            if (i==tiltIndex){
                putMeldTileRotated(d, areaInfo.get(d).totalMeldOffset, tiles[i], rotation);
                if (tiles.length==3) {
                    var offset = areaInfo.get(d).totalMeldOffset; //deep copy
                    areaInfo.get(d).addingActions.put(areaInfo.get(d).nextMeldIndex,
                            tile -> putMeldTileAdded(d, offset, tile, rotation));
                }
                areaInfo.get(d).totalMeldOffset += BLOCK_HEIGHT;
            }else{
                putMeldTile(d, areaInfo.get(d).totalMeldOffset, tiles[i]);
                areaInfo.get(d).totalMeldOffset += BLOCK_WIDTH;
            }
        }
        areaInfo.get(d).nextMeldIndex++;
        areaInfo.get(d).totalMeldOffset += MELD_MARGIN;
    }

    /**
     * 指定した方向の副露置き場に上家からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putLeftTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction::turnLeft, 0, tiles);
    }

    /**
     * 指定した方向の副露置き場に対面からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putMiddleTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction::turnRight, 1, tiles);
    }

    /**
     * 指定した方向の副露置き場に下家からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putRightTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction::turnRight, tiles.length - 1, tiles);
    }

    /**
     * 指定した方向のn番目副露に加槓として牌を追加で描画します。
     * @param d 副露の方向
     * @param meldIndex n(0..3)
     * @param tile 追加する牌
     */
    void putAdditionalMeldTile(Direction d, int meldIndex, Tile tile){
        var actions = areaInfo.get(d).addingActions;
        if (!actions.containsKey(meldIndex)){
            throw new IllegalStateException("can't add tile to meld at "+meldIndex+", "+d);
        }
        actions.get(meldIndex).accept(tile);
        actions.remove(meldIndex);
    }

    /**
     * 指定した方向の副露置き場に暗槓を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(4)
     */
    void putSelfQuadMeld(Direction d, Tile...tiles){
        if (tiles.length!=4){
            throw new IllegalArgumentException("illegal length of self meld tiles");
        }
        for (int i = 3; i>=0; i--) {
            if (i == 0 || i == 3) {
                putMeldTileFaceDown(d, areaInfo.get(d).totalMeldOffset);
            } else {
                putMeldTile(d, areaInfo.get(d).totalMeldOffset, tiles[i]);
            }
            areaInfo.get(d).totalMeldOffset += BLOCK_WIDTH;
        }
        areaInfo.get(d).nextMeldIndex++;
        areaInfo.get(d).totalMeldOffset += MELD_MARGIN;
    }

    /**
     * 指定した山牌の座標に下向き状態の牌ラベルを描画します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     */
    void putWallTile(Direction d, int column, int floor){
        var point = BlockPoints.ofWallBlock(d, column, floor);
        var block = areaInfo.get(d).wallBlockReferences[column][floor] = BlockLabel.ofFaceDown(d);
        putBlock(block, point.x, point.y, floor);
    }

    /**
     * 指定した山牌の座標にドラ表示牌として, 上向き状態の牌ラベルを描画します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     * @param tile 牌
     */
    void putWallTileAsIndicator(Direction d, int column, int floor, Tile tile){
        var point = BlockPoints.ofWallBlock(d, column, floor);
        var block = areaInfo.get(d).wallBlockReferences[column][floor] = BlockLabel.ofFaceUp(d, tile);
        putBlock(block, point.x, point.y, floor);
    }

    /**
     * 指定した山牌の座標の牌ラベルを削除します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     */
    void removeWallTile(Direction d, int column, int floor){
        remove(areaInfo.get(d).wallBlockReferences[column][floor]);
        areaInfo.get(d).wallBlockReferences[column][floor] = null;
    }

    /**
     * 指定した捨て牌の座標に牌ラベルを描画します。
     * @param d 捨て牌の方向
     * @param tile 牌
     */
    void putRiverTile(Direction d, Tile tile){
        var info = areaInfo.get(d);
        int riverIndex = info.nextRiverIndex;
        var point = info.readyRiverIndex==-1?
                BlockPoints.ofRiverBlock(d, riverIndex):
                BlockPoints.ofRiverBlock(d, riverIndex, info.nextRiverIndex);
        var block = info.riverBlockReference[info.nextRiverIndex]
                = BlockLabel.ofFaceUp(d.reversed(), tile);
        putBlock(block, point.x, point.y, 0);
        info.nextRiverIndex++;
    }

    /**
     * 指定した捨て牌の座標に牌ラベルをリーチ宣言牌として, 右回転した状態で描画します。
     * @param d 捨て牌の方向
     * @param tile 牌
     */
    void putRiverTileAsReady(Direction d, Tile tile){
        var info = areaInfo.get(d);
        info.readyRiverIndex = info.nextRiverIndex;
        putRiverTile(d, tile);
    }

    /**
     * 指定した方向の捨て牌にある, 最後の捨て牌ラベルを削除します。
     * @param d 捨て牌の方向
     */
    void removeRiverTile(Direction d){
        var info = areaInfo.get(d);
        if (info.nextRiverIndex==0){
            throw new IllegalStateException("no river tile set yet");
        }
        int lastRiverIndex = info.nextRiverIndex - 1;
        remove(info.riverBlockReference[lastRiverIndex]);
        info.riverBlockReference[lastRiverIndex] = null;
    }
}
