package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

@Deprecated
public abstract class TableSideAdaptor implements TableSpectator{
    private final TableStrategy strategy;
    TableSideAdaptor(TableStrategy strategy){
        this.strategy = strategy;
    }

    public abstract Wind getSubjectiveWind();

    @Override
    public void diceRolled(Wind wind, int dice1, int dice2){
        strategy.diceRolled(wind.from(getSubjectiveWind()), dice1, dice2);
    }
    @Override
    public void seatUpdated(Wind wind, String name, int score){
        strategy.seatUpdated(wind.from(getSubjectiveWind()), wind, name, score);
    }
    @Override
    public void tileDiscarded(Wind wind, Tile tile){
        strategy.tileDiscarded(wind.from(getSubjectiveWind()), tile);
    }
}
