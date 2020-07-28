package jp.rouh.mahjong.table;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

public class Player implements TableStrategy{
    private final String name;
    private final TableStrategy strategy;
    public Player(String name, TableStrategy strategy){
        this.name = name;
        this.strategy = strategy;
    }
    protected Player(Player orig){
        this.name = orig.name;
        this.strategy = orig.strategy;
    }
    public String getName(){
        return name;
    }

    @Override
    public TurnAction moveTurnPhase(List<TurnAction> choices){
        return strategy.moveTurnPhase(choices);
    }
    @Override
    public void wallGenerated(){

    }
    @Override
    public void wallTileTaken(Side side, int column, int floor){

    }
    @Override
    public void wallTileRevealed(Side side, int column, int floor, Tile tile){

    }
    @Override
    public void seatUpdated(Side side, PlayerBoard board){
        strategy.seatUpdated(side, board);
    }

    @Override
    public void diceRolled(Side side, int d1, int d2){
        strategy.diceRolled(side, d1, d2);
    }
    @Override
    public void riverTileAdded(Side side, Tile tile){
        strategy.riverTileAdded(side, tile);
    }
    @Override
    public void riverTileAddedAsReady(Side side, Tile tile){
        strategy.riverTileAddedAsReady(side, tile);
    }
    @Override
    public void meldAdded(Side side, Meld meld){
        strategy.meldAdded(side, meld);
    }
    @Override
    public void meldUpdated(Side side, int index, Meld meld){
        strategy.meldUpdated(side, index, meld);
    }
}
