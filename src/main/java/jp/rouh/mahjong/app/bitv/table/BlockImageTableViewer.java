package jp.rouh.mahjong.app.bitv.table;

import jp.rouh.mahjong.table.Declaration;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * 麻雀卓を描画するパネルクラス。
 *
 * @author Rouh
 * @version 1.0
 */
public class BlockImageTableViewer extends RegisteredPane implements TableViewer{
    /** 麻雀牌の幅 */
    private static final int BLOCK_WIDTH = 20;

    /** 麻雀牌の高さ */
    private static final int BLOCK_HEIGHT = 30;

    /** 麻雀卓の幅 */
    private static final int TABLE_WIDTH = 580;

    /** 麻雀卓の高さ */
    private static final int TABLE_HEIGHT = 580;

    /** 面子と面子の間の幅 */
    private static final int MELD_MARGIN = BLOCK_WIDTH/2;

    /** 次の捨て牌位置 */
    private int[] nextRiverIndexes;

    /** リーチ宣言牌の捨て牌位置(初期値-1) */
    private int[] readyRiverIndexes;

    /** 次の副露面子位置 */
    private int[] nextMeldIndexes;

    /** 副露面子の合計幅 */
    private int[] totalMeldOffsets;

    /** 加槓牌の追加位置 */
    private int[][] additionalTileOffsets;

    /** 加槓牌の向き */
    private Direction[][] additionalTileRotation;

    /**
     * コンストラクタ
     */
    public BlockImageTableViewer(){
        setSize(TABLE_WIDTH, TABLE_HEIGHT);
        setBorder(new LineBorder(Color.BLACK));
        initializeVariables();
    }

    /**
     * 変数を初期化します。
     */
    private void initializeVariables(){
        readyRiverIndexes = new int[]{-1, -1, -1, -1};
        nextRiverIndexes = new int[4];
        nextMeldIndexes = new int[4];
        totalMeldOffsets = new int[4];
        additionalTileOffsets = new int[4][4];
        additionalTileRotation = new Direction[4][4];
    }

    /**
     * 牌を指定された場所に描画します。
     * @param label 牌ラベル
     * @param x x座標
     * @param y y座標
     * @param z 高さ
     * @param name 登録名
     */
    private void putBlock(BlockLabel label, int x, int y, int z, String name){
        label.setLocationCentered(x, y);
        addNamed(label, name);
        setLayer(label, z*TABLE_HEIGHT + y);
    }

    /**
     * テーブルの状態をリセットします。
     *
     * <p>ただしプレイヤー情報などは引き継がれます。
     */
    @Override
    public void clear(){
        for(var d:Direction.values()){
            removeByNameIfPresent(getNameOfReadyBone(d));
            for(int index = 0; index<nextRiverIndexes[d.ordinal()]; index++){
                removeByName(getNameOfRiverTile(d, index));
            }
            for(int index = 0; index<17; index++){
                for(int floor = 0; floor<2; floor++){
                    removeByNameIfPresent(getNameOfWallTile(d, index, floor));
                }
            }
            removeHandTiles(d);
            for(int meldIndex = 0; meldIndex<nextMeldIndexes[d.ordinal()]; meldIndex++){
                for(int tileIndex = 0; tileIndex<4; tileIndex++){
                    removeByNameIfPresent(getNameOfMeldTile(d, meldIndex, tileIndex));
                }
            }
        }
        initializeVariables();
    }

    @Override
    public void showRoundInformation(Wind w, int count, int streak){
        //TODO: implementation
    }

    @Override
    public void showDeclaration(Direction d, Declaration dec){
        var label = new TableLabel();
        label.setText(dec.getText());
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(new LineBorder(Color.BLACK, 2));
        label.setSize(new Dimension(80, 40));
        var point = TablePoints.ofPlayerMessage(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getNameOfPlayerMessage(d));
        setLayer(label, 2000);
//        sleep();
//        removeByName(getNameOfPlayerMessage(d));
    }

    @Override
    public void updateRoundInfo(Wind w, int count){
        removeByNameIfPresent("round-info");
        var label = new TableLabel();
        var text = w.toString() + List.of("一", "二", "三", "四").get(count - 1) + "局";
        label.setText(text);
        var point = TablePoints.CENTER;
        label.setSize(new Dimension(40, 20));
        label.setLocationCentered(point.x, point.y - 10);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        addNamed(label, "round-info");
    }

    @Override
    public void updateStreakCount(int streak){
        removeByNameIfPresent("round-info-streak");
        var label = new TableLabel();
        label.setText(Integer.toString(streak));
        var point = TablePoints.CENTER;
        label.setSize(new Dimension(20, 20));
        label.setLocationCentered(point.x - 10, point.y + 10);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        addNamed(label, "round-info-streak");
    }

    @Override
    public void updateCarryOverCount(int carryOver){
        removeByNameIfPresent("round-info-carryover");
        var label = new TableLabel();
        label.setText(Integer.toString(carryOver));
        var point = TablePoints.CENTER;
        label.setSize(new Dimension(20, 20));
        label.setLocationCentered(point.x + 10, point.y + 10);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        addNamed(label, "round-info-carryover");
    }

    @Override
    public void updatePlayerWind(Direction d, Wind wind){
        removeByNameIfPresent(getNameOfPlayerWind(d));
        var label = TableLabels.ofPlayerWind(d, wind);
        var point = TablePoints.ofPlayerWind(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getNameOfPlayerWind(d));
    }

    @Override
    public void updatePlayerScore(Direction d, int score){
        removeByNameIfPresent(getNameOfPlayerScore(d));
        var label = TableLabels.ofPlayerScore(d, score);
        var point = TablePoints.ofPlayerScore(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getNameOfPlayerScore(d));
    }

    @Override
    public void updatePlayerName(Direction d, String name){
        removeByNameIfPresent(getNameOfPlayerName(d));
        var label = TableLabels.ofPlayerName(d, name);
        var point = TablePoints.ofPlayerName(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getNameOfPlayerName(d));
    }

    @Override
    public void putReadyBone(Direction d){
        var label = TableLabels.ofReadyBar(d);
        var point = TablePoints.ofReadyBar(d);
        label.setLocationCentered(point.x, point.y);
        addNamed(label, getNameOfReadyBone(d));
    }

    @Override
    public void appendRiverTile(Direction d, Tile tile){
        int riverIndex = nextRiverIndexes[d.ordinal()];
        int readyIndex = readyRiverIndexes[d.ordinal()];
        var point = readyIndex==-1?
                TablePoints.ofRiverBlock(d, riverIndex):
                TablePoints.ofRiverBlock(d, riverIndex, readyIndex);
        var block = BlockLabel.ofFaceUp(d.reversed(), tile);
        var name = getNameOfRiverTile(d, riverIndex);
        putBlock(block, point.x, point.y, 0, name);
        nextRiverIndexes[d.ordinal()]++;
    }

    @Override
    public void appendRiverTileRotated(Direction d, Tile tile){
        readyRiverIndexes[d.ordinal()] = nextRiverIndexes[d.ordinal()];
        appendRiverTile(d, tile);
    }

    @Override
    public void removeLastRiverTile(Direction d){
        assert nextRiverIndexes[d.ordinal()]>0;
        removeByName(getNameOfRiverTile(d, nextRiverIndexes[d.ordinal()] - 1));
    }

    private void putWallTile(Direction d, int index, int floor){
        var point = TablePoints.ofWallBlock(d, index, floor);
        var block  = BlockLabel.ofFaceDown(d);
        var name = getNameOfWallTile(d, index, floor);
        putBlock(block, point.x, point.y, floor, name);
    }

    private void putWallTileFaceUp(Direction d, int index, int floor, Tile tile){
        var point = TablePoints.ofWallBlock(d, index, floor);
        var block = BlockLabel.ofFaceUp(d, tile);
        var name = getNameOfWallTile(d, index, floor);
        putBlock(block, point.x, point.y, floor, name);
    }

    @Override
    public void putAllWallTiles(){
        for(var d:Direction.values()){
            for(int i = 0; i<17; i++){
                for(int j = 0; j<2; j++){
                    putWallTile(d, i, j);
                }
            }
        }
    }

    @Override
    public void revealWallTile(Direction d, int index, int floor, Tile tile){
        removeByName(getNameOfWallTile(d, index, floor));
        putWallTileFaceUp(d, index, floor, tile);
    }

    @Override
    public void removeWallTile(Direction d, int index, int floor){
        removeByName(getNameOfWallTile(d, index, floor));
    }

    private void appendHandTile(Tile t, int index, boolean margin){
        var d = Direction.BOTTOM;
        var point = TablePoints.ofHandBlock(d, index, margin);
        var block = BlockLabel.ofPlayerHand(t);
        putBlock(block, point.x, point.y, 0, getNameOfHandTile(d, index));
    }

    private void appendHandTile(Direction d, int index, boolean margin){
        assert d==Direction.BOTTOM;
        var point = TablePoints.ofHandBlock(d, index, margin);
        var block = BlockLabel.ofOpponentHand(d.isSideways()?d:d.reversed());
        putBlock(block, point.x, point.y, 0, getNameOfHandTile(d, index));
    }

    private void appendHandTileFaceUp(Direction d, int index, Tile t, boolean margin){
        var point = TablePoints.ofHandBlock(d, index, margin);
        var block = BlockLabel.ofFaceUp(d, t);
        putBlock(block, point.x, point.y, 0, getNameOfHandTile(d, index));
    }

    private void appendHandTileFaceDown(Direction d, int index){
        var point = TablePoints.ofHandBlock(d, index, false);
        var block = BlockLabel.ofFaceDown(d);
        putBlock(block, point.x, point.y, 0, getNameOfHandTile(d, index));
    }

    private void removeHandTiles(Direction d){
        for(int i = 0; i<14; i++){
            removeByNameIfPresent(getNameOfHandTile(d, i));
        }
    }

    @Override
    public void updateHandTiles(List<Tile> tiles, boolean isolation){
        if(tiles.size()>14) throw new IllegalArgumentException("illegal size: "+tiles);
        var d = Direction.BOTTOM;
        removeHandTiles(d);
        for(int i = 0, last = tiles.size() - 1; i<=last; i++){
            appendHandTile(tiles.get(i), i, isolation && i==last);
        }
    }

    @Override
    public void updateHandTiles(Direction d, int size, boolean isolation){
        if(d==Direction.BOTTOM) throw new IllegalArgumentException("illegal direction: "+d);
        if(size>14) throw new IllegalArgumentException("illegal size: "+size);
        removeHandTiles(d);
        for(int i = 0, last = size - 1; i<=last; i++){
            appendHandTile(d, i, isolation && i==last);
        }
    }

    @Override
    public void updateHandTilesFaceUp(Direction d, List<Tile> tiles, boolean isolation){
        if(tiles.size()>14) throw new IllegalArgumentException("illegal size: "+tiles);
        removeHandTiles(d);
        for(int i = 0, last = tiles.size() - 1; i<=last; i++){
            appendHandTileFaceUp(d, i, tiles.get(i), isolation && i==last);
        }
    }

    @Override
    public void updateHandTilesFaceDown(Direction d, int size){
        if(size>14) throw new IllegalArgumentException("illegal size: "+size);
        removeHandTiles(d);
        for(int i = 0; i<size; i++){
            appendHandTileFaceDown(d, i);
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

    /**
     * 順子/刻子/大明槓の副露面子を追加します。
     * @param d 方向
     * @param tiles 牌(長さ 3..4)
     * @param tiltIndex 倒す牌の位置(0..tiles.size()-1)
     * @param rotateToLeft true  左向きに牌を倒します
     *                     false 右向きに牌を倒します
     */
    private void appendTiltMeld(Direction d, List<Tile> tiles, int tiltIndex, boolean rotateToLeft){
        assert tiles.size()==3 || tiles.size()==4;
        var rotation = rotateToLeft? d.turnLeft():d.turnRight();
        for(int index = tiles.size() - 1; index>=0; index--){
            var name = getNameOfMeldTile(d, nextMeldIndexes[d.ordinal()], index);
            if(index==tiltIndex){
                putMeldTileRotated(d, tiles.get(index), rotation, totalMeldOffsets[d.ordinal()], name);
                additionalTileOffsets[d.ordinal()][nextMeldIndexes[d.ordinal()]] = totalMeldOffsets[d.ordinal()];
                additionalTileRotation[d.ordinal()][nextMeldIndexes[d.ordinal()]] = rotation;
                totalMeldOffsets[d.ordinal()] += BLOCK_HEIGHT;
            }else{
                putMeldTile(d, tiles.get(index), totalMeldOffsets[d.ordinal()], name);
                totalMeldOffsets[d.ordinal()] += BLOCK_WIDTH;
            }
        }
        nextMeldIndexes[d.ordinal()]++;
        totalMeldOffsets[d.ordinal()] += MELD_MARGIN;
    }

    @Override
    public void appendLeftTiltMeld(Direction d, List<Tile> tiles){
        if(tiles.size()<3 || tiles.size()>4) throw new IllegalArgumentException("illegal size: "+tiles);
        appendTiltMeld(d, tiles, 0, true);
    }

    @Override
    public void appendMiddleTiltMeld(Direction d, List<Tile> tiles){
        if(tiles.size()<3 || tiles.size()>4) throw new IllegalArgumentException("illegal size: "+tiles);
        appendTiltMeld(d, tiles, 1, false);
    }

    @Override
    public void appendRightTiltMeld(Direction d, List<Tile> tiles){
        if(tiles.size()<3 || tiles.size()>4) throw new IllegalArgumentException("illegal size: "+tiles);
        appendTiltMeld(d, tiles, tiles.size() - 1, false);
    }

    @Override
    public void addTileToMeld(Direction d, int meldIndex, Tile t){
        int offset = additionalTileOffsets[d.ordinal()][meldIndex];
        var rotation = additionalTileRotation[d.ordinal()][meldIndex];
        var name = getNameOfMeldTile(d, meldIndex, 3);
        putMeldTileAdded(d, t, rotation, offset, name);
    }

    @Override
    public void appendSelfQuad(Direction d, List<Tile> tiles){
        if(tiles.size()>4) throw new IllegalArgumentException("illegal size: "+tiles);
        for(int index = 3; index>=0; index--){
            var name = getNameOfMeldTile(d, nextMeldIndexes[d.ordinal()], index);
            if(index==0 || index==3){
                putMeldTileFaceDown(d, totalMeldOffsets[d.ordinal()], name);
            }else{
                putMeldTile(d, tiles.get(index), totalMeldOffsets[d.ordinal()], name);
            }
            totalMeldOffsets[d.ordinal()] += BLOCK_WIDTH;
        }
        nextMeldIndexes[d.ordinal()]++;
        totalMeldOffsets[d.ordinal()] += MELD_MARGIN;
    }

    private static void sleep(){
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private static String getNameOfHandTile(Direction d, int index){
        return "hand("+d+"-"+index+")";
    }

    private static String getNameOfMeldTile(Direction d, int meldIndex, int tileIndex){
        return "meld("+d+"-"+meldIndex+")-"+tileIndex;
    }

    private static String getNameOfWallTile(Direction d, int column, int floor){
        return "wall-"+d.ordinal()*34+column*2+floor;
    }

    private static String getNameOfRiverTile(Direction d, int index){
        return "river("+d+")-"+index;
    }

    private static String getNameOfReadyBone(Direction d){
        return "bone("+d+")";
    }

    private static String getNameOfPlayerMessage(Direction d){
        return "message("+d+")";
    }

    private static String getNameOfPlayerWind(Direction d){
        return "wind("+d+")";
    }

    private static String getNameOfPlayerScore(Direction d){
        return "score("+d+")";
    }

    private static String getNameOfPlayerName(Direction d){
        return "name("+d+")";
    }
}
