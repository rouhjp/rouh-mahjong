package jp.rouh.mahjong.app;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.table.PlayerBoard;
import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.table.TurnAction;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

public class ConsoleViewer implements TableStrategy{
    @Override
    public TurnAction moveTurnPhase(List<TurnAction> choices){
        return null;
    }
    @Override
    public void seatUpdated(Side side, PlayerBoard board){
        System.out.println(side + ": " + board);
    }
    @Override
    public void diceRolled(Side side, int d1, int d2){
        System.out.println(side + " rolled dices: " + d1 + "," + d2);
    }
    @Override
    public void wallGenerated(){
        System.out.println("wall generated");
    }
    @Override
    public void wallTileTaken(Side side, int column, int floor){
        System.out.println("wall tile taken");
    }
    @Override
    public void wallTileRevealed(Side side, int column, int floor, Tile tile){
        System.out.println("wall tile revealed: "+tile);
    }

    @Override
    public void riverTileAdded(Side side, Tile tile){
        System.out.println(side + " discard: "+tile);
    }
    @Override
    public void riverTileAddedAsReady(Side side, Tile tile){
        System.out.println(side + " discard <REACH>: "+tile);
    }
    @Override
    public void meldAdded(Side side, Meld meld){
        System.out.println(side + " called: "+meld);
    }
    @Override
    public void meldUpdated(Side side, int index, Meld meld){
        System.out.println(side + " added: "+meld);
    }
}
