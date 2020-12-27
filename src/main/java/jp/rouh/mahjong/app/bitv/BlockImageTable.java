package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BlockImageTable extends RegisteredPane{
    private static final int BLOCK_WIDTH = 20;
    private static final int BLOCK_HEIGHT = 30;
    private static final int TABLE_WIDTH = 580;
    private static final int TABLE_HEIGHT = 580;
    private static final int MELD_MARGIN = BLOCK_WIDTH/2;
    private int readyRiverIndexes[] = {-1, -1, -1, -1};
    private int nextRiverIndexes[] = new int[4];
    private int nextMeldIndexes[] = new int[4];
    private int totalMeldOffsets[] = new int[4];
    private int addMeldOffsets[][] = new int[4][4];
    private Direction addMeldTilts[][] = new Direction[4][4];
    BlockImageTable(){
        setSize(TABLE_WIDTH, TABLE_HEIGHT);
        setBorder(new LineBorder(Color.BLACK));
    }

    private void putBlock(BlockLabel label, int x, int y, int z, String name){
        label.setLocationCentered(x, y);
        addNamed(label, name);
        setLayer(label, z*TABLE_HEIGHT + y);
    }

    private String getMeldTileId(Direction d, int meldIndex, int tileIndex){
        return "meld("+d+"-"+meldIndex+")-"+tileIndex;
    }

    private void clear(){
        nextMeldIndexes = new int[4];
        totalMeldOffsets = new int[4];
        addMeldOffsets = new int[4][4];
        addMeldTilts = new Direction[4][4];
        for(var d:Direction.values()){
            for(int i = 0;i<4;i++){
                for(int j = 0; j<4; j++){
                    removeByName(getMeldTileId(d, i, j));
                }
            }
            removeByName(getReadyBoneId(d));
        }
    }

    private void putMeldTile(Direction d, Tile tile, int offset, String name){
        var point = TablePoints.ofMeldBlock(d, offset, false, false);
        var block = BlockLabel.ofFaceUp(d.reversed(), tile);
        putBlock(block, point.x, point.y, 0, name);
    }

    private void putMeldTileFaceDown(Direction d, int offset, String name){
        var point = TablePoints.ofMeldBlock(d, offset, false, false);
        var block = BlockLabel.ofFaceDown(d.reversed());
        putBlock(block, point.x, point.y, 0, name);
    }

    private void putMeldTileRotated(Direction d, Tile tile, Direction rotated, int offset, String name){
        var point = TablePoints.ofMeldBlock(d, offset, true, false);
        var block = BlockLabel.ofFaceUp(rotated.reversed(), tile);
        putBlock(block, point.x, point.y, 0, name);
    }

    private void putMeldTileAdded(Direction d, Tile tile, Direction rotated, int offset, String name){
        var point = TablePoints.ofMeldBlock(d, offset, true, true);
        var block = BlockLabel.ofFaceUp(rotated.reversed(), tile);
        putBlock(block, point.x, point.y, 0, name);
    }

    private void putTiltMeld(Direction d, Direction tilt, Tile...tiles){
        assert tiles.length==3 || tiles.length==4;
        assert tilt==Direction.LEFT || tilt==Direction.RIGHT;
        int tiltIndex = tilt==Direction.LEFT? 0:tiles.length - 1;
        var rotated = tilt==Direction.LEFT? d.turnLeft():d.turnRight();
        for(int i = tiles.length - 1; i>=0; i--){
            String name = getMeldTileId(d, nextMeldIndexes[d.ordinal()], i);
            if(i==tiltIndex){
                putMeldTileRotated(d, tiles[i], rotated, totalMeldOffsets[d.ordinal()], name);
                addMeldOffsets[d.ordinal()][nextMeldIndexes[d.ordinal()]] = totalMeldOffsets[d.ordinal()];
                addMeldTilts[d.ordinal()][nextMeldIndexes[d.ordinal()]] = tilt;
                totalMeldOffsets[d.ordinal()] += BLOCK_HEIGHT;
            }else{
                putMeldTile(d, tiles[i], totalMeldOffsets[d.ordinal()], name);
                totalMeldOffsets[d.ordinal()] += BLOCK_WIDTH;
            }
        }
        nextMeldIndexes[d.ordinal()]++;
        totalMeldOffsets[d.ordinal()] += MELD_MARGIN;
    }

    /**
     * 指定した方向の副露置き場に上家からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putLeftTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction.LEFT, tiles);
    }

    /**
     * 指定した方向の副露置き場に対面からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putMiddleTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction.RIGHT, tiles);
    }

    /**
     * 指定した方向の副露置き場に下家からの副露を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(3..4)
     */
    void putRightTiltMeld(Direction d, Tile...tiles){
        putTiltMeld(d, Direction.RIGHT, tiles);
    }

    /**
     * 指定した方向の副露置き場に暗槓を描画します。
     * @param d 副露牌の方向
     * @param tiles 牌(4)
     */
    void putSelfQuadMeld(Direction d, Tile...tiles){
        assert tiles.length==4;
        for(int i = 3; i>=0; i--){
            String name = getMeldTileId(d, nextMeldIndexes[d.ordinal()], i);
            if(i==0 || i==3){
                putMeldTileFaceDown(d, totalMeldOffsets[d.ordinal()], name);
            }else{
                putMeldTile(d, tiles[i], totalMeldOffsets[d.ordinal()], name);
            }
            totalMeldOffsets[d.ordinal()] += MELD_MARGIN;
        }
        nextMeldIndexes[d.ordinal()]++;
        totalMeldOffsets[d.ordinal()] += MELD_MARGIN;
    }

    /**
     * 指定した方向のn番目副露に加槓として牌を追加で描画します。
     * @param d 副露の方向
     * @param meldIndex n(0..3)
     * @param tile 追加する牌
     */
    void putAddMeldTile(Direction d, int meldIndex, Tile tile){
        int offset = addMeldOffsets[d.ordinal()][meldIndex];
        var tilt = addMeldTilts[d.ordinal()][meldIndex];
        var rotated = tilt==Direction.LEFT? d.turnLeft():d.turnRight();
        var name = getMeldTileId(d, meldIndex, 3);
        putMeldTileAdded(d, tile, rotated, offset, name);
    }

    private String getWallTileId(Direction d, int column, int floor){
        return "wall-"+d.ordinal()*34+column*2+floor;
    }

    /**
     * 指定した山牌の座標に下向き状態の牌ラベルを描画します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     */
    void putWallTile(Direction d, int column, int floor){
        var point = TablePoints.ofWallBlock(d, column, floor);
        var block  = BlockLabel.ofFaceDown(d);
        var name = getWallTileId(d, column, floor);
        putBlock(block, point.x, point.y, floor, name);
    }

    /**
     * 指定した山牌の座標にドラ表示牌として, 上向き状態の牌ラベルを描画します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     * @param tile 牌
     */
    void putWallTileAsIndicator(Direction d, int column, int floor, Tile tile){
        var point = TablePoints.ofWallBlock(d, column, floor);
        var block = BlockLabel.ofFaceUp(d, tile);
        var name = getWallTileId(d, column, floor);
        putBlock(block, point.x, point.y, floor, name);
    }

    /**
     * 指定した山牌の座標の牌ラベルを削除します。
     * @param d 山牌の方向
     * @param column 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     */
    void removeWallTile(Direction d, int column, int floor){
        removeByName(getWallTileId(d, column, floor));
    }

    private String getRiverTileId(Direction d, int index){
        return "river("+d+")-"+index;
    }

    /**
     * 指定した捨て牌の座標に牌ラベルを描画します。
     * @param d 捨て牌の方向
     * @param tile 牌
     */
    void putRiverTile(Direction d, Tile tile){
        int riverIndex = nextRiverIndexes[d.ordinal()];
        int readyIndex = readyRiverIndexes[d.ordinal()];
        var point = readyIndex==-1?
                TablePoints.ofRiverBlock(d, riverIndex):
                TablePoints.ofRiverBlock(d, riverIndex, readyIndex);
        var block = BlockLabel.ofFaceUp(d.reversed(), tile);
        var name = getRiverTileId(d, riverIndex);
        putBlock(block, point.x, point.y, 0, name);
        nextRiverIndexes[d.ordinal()]++;
    }

    /**
     * 指定した捨て牌の座標に牌ラベルをリーチ宣言牌として, 右回転した状態で描画します。
     * @param d 捨て牌の方向
     * @param tile 牌
     */
    void putRiverTileAsReady(Direction d, Tile tile){
        readyRiverIndexes[d.ordinal()] = nextRiverIndexes[d.ordinal()];
        putRiverTile(d, tile);
    }

    /**
     * 指定した方向の捨て牌にある, 最後の捨て牌ラベルを削除します。
     * @param d 捨て牌の方向
     */
    void removeRiverTile(Direction d){
        assert nextRiverIndexes[d.ordinal()]>0;
        removeByName(getRiverTileId(d, nextRiverIndexes[d.ordinal()] - 1));
    }

    private String getReadyBoneId(Direction d){
        return "ready-"+d;
    }

    void putReadyBone(Direction d){
        var label = TableLabels.ofReadyBar(d);
        var point = TablePoints.ofReadyBar(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getReadyBoneId(d));
    }

    void putPlayerNameArea(Direction d, String name){
        var label = TableLabels.ofPlayerName(d, name);
        var point = TablePoints.ofPlayerName(d);
        label.setLocationCentered(point.x, point.y);
        add(label);
    }

    void putPlayerScoreArea(Direction d, int score){
        var label = TableLabels.ofPlayerScore(d, score);
        var point = TablePoints.ofPlayerScore(d);
        label.setLocationCentered(point.x, point.y);
        add(label);
    }

    void putPlayerWind(Direction d, Wind wind){
        var label = TableLabels.ofPlayerWind(d, wind);
        var point = TablePoints.ofPlayerWind(d);
        label.setLocationCentered(point.x, point.y);
        add(label);
    }

    void putPlayerMessage(Direction d, String msg){
        var label = new TableLabel();
        label.setText(msg);
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBackground(new Color(240, 230, 140));
        label.setOpaque(true);
        label.setBorder(new LineBorder(Color.BLACK, 2));
        label.setSize(new Dimension(80, 40));
        var point = TablePoints.ofPlayerMessage(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, "message-"+d);
        setLayer(label, 2000);
    }
}
