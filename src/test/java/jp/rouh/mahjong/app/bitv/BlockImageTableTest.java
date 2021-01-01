package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.app.bitv.table.BlockImageTableViewer;
import jp.rouh.mahjong.app.bitv.table.Direction;
import jp.rouh.mahjong.table.Declaration;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * GUIの手動テストクラス
 */
public class BlockImageTableTest extends JFrame{
    private final BlockImageTableViewer table = new BlockImageTableViewer();
    private BlockImageTableTest(){
        add(table);
        setLayout(null);
        setTitle("TEST");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(600, 600));
        pack();
        test();
        add(table);
        setVisible(true);
    }

    public void test(){
        for(var d: Direction.values()){
            table.putReadyBone(d);
            table.updatePlayerName(d, "hellomynameismuu");
            table.updatePlayerScore(d, 100000);
            table.updatePlayerWind(d, Wind.values()[d.ordinal()]);
        }

        table.setLocation(10, 10);
        for (var d:Direction.values()) {
            for (int i = 0; i < 24; i++) {
                table.appendRiverTile(d, Tile.M5);
            }
        }
        table.putAllWallTiles();
//        table.showRoundInformation(Wind.EAST, 1, 0);
        table.updateRoundInfo(Wind.EAST, 1);
        table.updateStreakCount(0);
        table.updateCarryOverCount(3);

        table.removeLastRiverTile(Direction.RIGHT);

        table.appendSelfQuad(Direction.BOTTOM, List.of(Tile.M1, Tile.M1, Tile.M1, Tile.M1));
        table.appendMiddleTiltMeld(Direction.RIGHT, List.of(Tile.DR, Tile.DR, Tile.DR, Tile.DR));
        table.appendRightTiltMeld(Direction.RIGHT, List.of(Tile.DW, Tile.DW, Tile.DW));
        table.appendLeftTiltMeld(Direction.RIGHT, List.of(Tile.DG, Tile.DG, Tile.DG));
        table.addTileToMeld(Direction.RIGHT, 2, Tile.DG);

        table.appendMiddleTiltMeld(Direction.TOP, List.of(Tile.DR, Tile.DR, Tile.DR, Tile.DR));
        table.appendRightTiltMeld(Direction.TOP, List.of(Tile.DW, Tile.DW, Tile.DW));
        table.appendLeftTiltMeld(Direction.TOP, List.of(Tile.DG, Tile.DG, Tile.DG));
        table.addTileToMeld(Direction.TOP, 2, Tile.DG);

        table.appendMiddleTiltMeld(Direction.BOTTOM, List.of(Tile.DR, Tile.DR, Tile.DR, Tile.DR));
        table.appendRightTiltMeld(Direction.BOTTOM, List.of(Tile.DW, Tile.DW, Tile.DW));
        table.appendLeftTiltMeld(Direction.BOTTOM, List.of(Tile.DG, Tile.DG, Tile.DG));
        table.addTileToMeld(Direction.BOTTOM, 2, Tile.DG);


        var handTiles = List.of(Tile.M2, Tile.M2, Tile.M3, Tile.M3, Tile.M4, Tile.M4,
                Tile.M5, Tile.M5R, Tile.M6, Tile.M6, Tile.M7, Tile.M7, Tile.M8, Tile.M9);

        table.updateHandTiles(handTiles, true);
        table.updateHandTiles(Direction.RIGHT, 13, false);
        table.updateHandTiles(Direction.TOP, 13, false);
        table.updateHandTiles(Direction.LEFT, 13, false);

        table.showDeclaration(Direction.TOP, Declaration.PON);


    }

    public static void main(String[] args) {
        new BlockImageTableTest();
    }
}
