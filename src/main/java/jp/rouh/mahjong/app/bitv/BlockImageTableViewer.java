package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.table.PlayerBoard;
import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.table.TurnAction;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlockImageTableViewer extends JFrame implements TableStrategy{
    private final BlockImageTable table = new BlockImageTable();
    public BlockImageTableViewer(){
        setTitle("麻雀");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(600, 600));
        pack();
        setLayout(null);
        table.setLocation(10, 10);
        add(table);
        setVisible(true);
    }

    @Override
    public TurnAction moveTurnPhase(List<TurnAction> choices){
        //TODO: implementation
        return null;
    }

    @Override
    public void seatUpdated(Side side, PlayerBoard board){
        //TODO: implementation
    }

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
        assert meld.isExposed();
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
}
