package jp.rouh.mahjong.table.impl;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.table.Player;
import jp.rouh.mahjong.table.PlayerBoard;
import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.table.TurnAction;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

public class Bot extends Player{
    public Bot(String name){
        super(name, new TableStrategy(){
            @Override
            public TurnAction moveTurnPhase(List<TurnAction> choices){
                return null;
            }
            @Override
            public void seatUpdated(Side side, PlayerBoard board){

            }
            @Override
            public void diceRolled(Side side, int d1, int d2){

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
            public void riverTileAdded(Side side, Tile tile){

            }
            @Override
            public void riverTileAddedAsReady(Side side, Tile tile){

            }
            @Override
            public void meldAdded(Side side, Meld meld){

            }
            @Override
            public void meldUpdated(Side side, int index, Meld meld){

            }
        });
    }
}
