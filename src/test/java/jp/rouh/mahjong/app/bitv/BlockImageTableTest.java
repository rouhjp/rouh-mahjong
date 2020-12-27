package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GUIの手動テストクラス
 */
public class BlockImageTableTest extends JFrame{
    private final BlockImageTable table = new BlockImageTable();
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
        for(var d:Direction.values()){
            table.putReadyBone(d);
            table.putPlayerNameArea(d, "hellomynameismuu");
            table.putPlayerScoreArea(d, 100000);
            table.putPlayerWind(d, Wind.values()[d.ordinal()]);
        }

        table.setLocation(10, 10);
        for (var d:Direction.values()) {
            for (int i = 0; i < 24; i++) {
                table.putRiverTile(d, Tile.M5);
            }
        }
        for (var d:Direction.values()){
            for (int i = 0; i<17; i++){
                for (int j = 0; j<2; j++){
                    table.putWallTile(d, i, j);
                }
            }
        }
        table.removeRiverTile(Direction.RIGHT);
        table.putMiddleTiltMeld(Direction.BOTTOM, Tile.DR, Tile.DR, Tile.DR, Tile.DR);
        table.putRightTiltMeld(Direction.BOTTOM, Tile.DW, Tile.DW, Tile.DW);
        table.putAddMeldTile(Direction.BOTTOM, 1, Tile.DW);
        table.putLeftTiltMeld(Direction.BOTTOM, Tile.DG, Tile.DG, Tile.DG);

        var handTiles = List.of(Tile.M2, Tile.M2, Tile.M3, Tile.M3, Tile.M4, Tile.M4,
                Tile.M5, Tile.M5R, Tile.M6, Tile.M6, Tile.M7, Tile.M7, Tile.M8, Tile.M9);

        table.putPlayerMessage(Direction.LEFT, "ポン");
        table.putPlayerMessage(Direction.RIGHT, "カン");
        table.putPlayerMessage(Direction.BOTTOM, "チー");
        table.putPlayerMessage(Direction.TOP, "ロン");

        //
//        table.putHandTiles(Direction.LEFT, handTiles);
//        table.putHandTiles(Direction.TOP, handTiles);
//        table.putHandTiles(Direction.RIGHT, handTiles);
    }

    public static void main(String[] args) {
        new BlockImageTableTest();
    }
}
