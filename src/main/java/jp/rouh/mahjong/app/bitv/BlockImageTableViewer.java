package jp.rouh.mahjong.app.bitv;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlockImageTableViewer extends JFrame /* implements TableStrategy */{
    private final BlockImageTable table = new BlockImageTable();
    private final ControlPanel controlPanel = new ControlPanel();
    public BlockImageTableViewer(){
        setTitle("麻雀");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(600, 730));
        pack();
        setLayout(null);
        table.setLocation(10, 10);
        controlPanel.setLocation(10, 600);
        add(table);
        add(controlPanel);
        controlPanel.updateHand(List.of("M1", "M2", "M3"));
        setVisible(true);
    }

/*

    @Override
    public void diceRolled(Side side, int d1, int d2){
        //TODO: implementation
    }

    @Override
    public void wallGenerated(){
        for (var d:Direction.values()){
            for (int i = 0; i<17; i++){
                for (int j = 0; j<2; j++){
                    table.putWallTile(d, i, j);
                }
            }
        }
    }

    @Override
    public void wallTileTaken(Side side, int column, int floor){
        table.removeWallTile(Direction.of(side), column, floor);
    }

    @Override
    public void wallTileRevealed(Side side, int column, int floor, Tile tile){
        table.removeWallTile(Direction.of(side), column, floor);
        table.putWallTileAsIndicator(Direction.of(side), column, floor, tile);
    }

    @Override
    public void riverTileAdded(Side side, Tile tile){
        table.putRiverTile(Direction.of(side), tile);
    }

    @Override
    public void riverTileAddedAsReady(Side side, Tile tile){
        table.putRiverTileAsReady(Direction.of(side), tile);
    }

    @Override
    public void meldAdded(Side side, Meld meld){
        var direction = Direction.of(side);
        var tileArray = meld.getTilesFormed().toArray(new Tile[0]);
        switch(meld.getSourceSide()){
            case SELF:
                table.putSelfQuadMeld(direction, tileArray);
                break;
            case LEFT:
                table.putLeftTiltMeld(direction, tileArray);
                break;
            case ACROSS:
                table.putMiddleTiltMeld(direction, tileArray);
                break;
            case RIGHT:
                table.putRightTiltMeld(direction, tileArray);
                break;
        }
    }

    @Override
    public void meldUpdated(Side side, int index, Meld meld){
        assert meld.isAddQuad();
        var additionalTile = meld.getTilesFormed().get(4);
        table.putAdditionalMeldTile(Direction.of(side), index, additionalTile);
    }
    @Override
    public void tileDrawn(Side side){

    }
    @Override
    public void tileDrawn(Side side, Tile tile){

    }
    @Override
    public void readyDeclared(Side side){

    }
    @Override
    public void roundStarted(Wind wind, int count, int streak, int bets){

    }


 */
}
