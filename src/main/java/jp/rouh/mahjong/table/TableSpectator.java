package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

public interface TableSpectator{

    void diceRolled(Wind wind, int dice1, int dice2);

    void seatUpdated(Wind wind, String name, int score);

    void tileDiscarded(Wind wind, Tile tile);

}
