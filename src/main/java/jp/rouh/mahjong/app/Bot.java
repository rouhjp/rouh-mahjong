package jp.rouh.mahjong.app;

import jp.rouh.mahjong.table.Player;
import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.table.action.CallAction;
import jp.rouh.mahjong.table.action.CallPhaseContext;
import jp.rouh.mahjong.table.action.TurnAction;
import jp.rouh.mahjong.table.action.TurnPhaseContext;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

public class Bot extends Player{
    public Bot(String name){
        super(name, new TableStrategy(){
            @Override
            public TurnAction askTurnAction(TurnPhaseContext context){
                return TurnAction.ofDiscard(context.getTiles().get(context.getTiles().size() - 1));
            }
            @Override
            public CallAction askCallAction(CallPhaseContext context){
                return CallAction.ofPass();
            }
            @Override
            public void seatUpdated(Side side, Wind wind, String name, int score){

            }
            @Override
            public void diceRolled(Side side, int dice1, int dice2){

            }
            @Override
            public void roundStarted(Wind wind, int count, int streak, int deposit){

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
            public void tileDrawn(Side side){

            }
            @Override
            public void tileDrawn(Tile tile){

            }
            @Override
            public void handUpdated(Side side, int count){

            }
            @Override
            public void handUpdated(List<Tile> handTiles){

            }
            @Override
            public void tileDiscarded(Side side, Tile tile){

            }
            @Override
            public void tiltTileDiscarded(Side side, Tile tile){

            }
            @Override
            public void selfQuadAdded(Side side, List<Tile> tiles){

            }
            @Override
            public void leftTiltMeldAdded(Side side, List<Tile> tiles){

            }
            @Override
            public void middleTiltMeldAdded(Side side, List<Tile> tiles){

            }
            @Override
            public void rightTiltMeldAdded(Side side, List<Tile> tiles){

            }
            @Override
            public void meldTileAdded(Side side, int index, Tile tile){

            }
            @Override
            public void handUpdated(Side side, List<Tile> handTiles){

            }
        });
    }

}
