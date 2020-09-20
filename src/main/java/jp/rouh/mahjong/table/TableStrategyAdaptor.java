package jp.rouh.mahjong.table;

import jp.rouh.mahjong.table.action.CallAction;
import jp.rouh.mahjong.table.action.CallPhaseContext;
import jp.rouh.mahjong.table.action.TurnAction;
import jp.rouh.mahjong.table.action.TurnPhaseContext;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

/**
 * テーブル戦略を注入したオブジェクトに委譲するアダプタクラス。
 * @author Rouh
 * @version 1.0
 */
public abstract class TableStrategyAdaptor implements TableStrategy{
    private final TableStrategy strategy;
    protected TableStrategyAdaptor(TableStrategy strategy){
        this.strategy = strategy;
    }
    @Override
    public TurnAction askTurnAction(TurnPhaseContext context){
        return strategy.askTurnAction(context);
    }
    @Override
    public CallAction askCallAction(CallPhaseContext context){
        return strategy.askCallAction(context);
    }
    @Override
    public void seatUpdated(Side side, Wind wind, String name, int score){
        strategy.seatUpdated(side, wind, name, score);
    }
    @Override
    public void diceRolled(Side side, int dice1, int dice2){
        strategy.diceRolled(side, dice1, dice2);
    }
    @Override
    public void roundStarted(Wind wind, int count, int streak, int deposit){
        strategy.roundStarted(wind, count, streak, deposit);
    }
    @Override
    public void wallGenerated(){
        strategy.wallGenerated();
    }
    @Override
    public void wallTileTaken(Side side, int column, int floor){
        strategy.wallTileTaken(side, column, floor);
    }
    @Override
    public void wallTileRevealed(Side side, int column, int floor, Tile tile){
        strategy.wallTileRevealed(side, column, floor, tile);
    }
    @Override
    public void tileDrawn(Side side){
        strategy.tileDrawn(side);
    }
    @Override
    public void tileDrawn(Tile tile){
        strategy.tileDrawn(tile);
    }
    @Override
    public void handUpdated(Side side, int count){
        strategy.handUpdated(side, count);
    }
    @Override
    public void handUpdated(List<Tile> handTiles){
        strategy.handUpdated(handTiles);
    }
    @Override
    public void tileDiscarded(Side side, Tile tile){
        strategy.tileDiscarded(side, tile);
    }
    @Override
    public void tiltTileDiscarded(Side side, Tile tile){
        strategy.tiltTileDiscarded(side, tile);
    }
    @Override
    public void selfQuadAdded(Side side, List<Tile> tiles){
        strategy.selfQuadAdded(side, tiles);
    }
    @Override
    public void leftTiltMeldAdded(Side side, List<Tile> tiles){
        strategy.leftTiltMeldAdded(side, tiles);
    }
    @Override
    public void middleTiltMeldAdded(Side side, List<Tile> tiles){
        strategy.middleTiltMeldAdded(side, tiles);
    }
    @Override
    public void rightTiltMeldAdded(Side side, List<Tile> tiles){
        strategy.rightTiltMeldAdded(side, tiles);
    }
    @Override
    public void meldTileAdded(Side side, int index, Tile tile){
        strategy.meldTileAdded(side, index, tile);
    }
    @Override
    public void handUpdated(Side side, List<Tile> handTiles){
        strategy.handUpdated(side, handTiles);
    }
}
