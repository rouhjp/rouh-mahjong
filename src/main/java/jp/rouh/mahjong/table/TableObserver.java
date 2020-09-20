package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

/**
 *
 * @author Rouh
 * @version 1.0
 */
public interface TableObserver{

    void seatUpdated(Side side, Wind wind, String name, int score);

    /**
     * 親プレイヤーのサイコロの結果を通知します。
     * @param side 観測者に対する親プレイヤーの相対位置
     * @param dice1 1つ目のサイコロの目(1..6)
     * @param dice2 2つ目のサイコロの目(1..6)
     */
    void diceRolled(Side side, int dice1, int dice2);

    void roundStarted(Wind wind, int count, int streak, int deposit);

    /**
     * 局が開始し, 山牌が生成されたことを通知します。
     */
    void wallGenerated();

    /**
     * 山牌から牌がツモされたことを通知します。
     * @param side 観測者に対するツモ箇所の相対位置
     * @param column 卓の中心から見たとき左から数えたツモ箇所の列(0..16)
     * @param floor ツモ箇所の段目(上の場合0, 下の場合1)
     */
    void wallTileTaken(Side side, int column, int floor);

    /**
     * ドラ表示牌がめくられたことを通知します。
     * @param side 観測者に対するツモ箇所の相対位置
     * @param column 卓の中心から見たとき左から数えたツモ箇所の列(0..16)
     * @param floor ツモ箇所の段目(上の場合0, 下の場合1)
     * @param tile めくられた牌
     */
    void wallTileRevealed(Side side, int column, int floor, Tile tile);

    void tileDrawn(Side side);

    void tileDrawn(Tile tile);

    void handUpdated(Side side, int count);

    void handUpdated(List<Tile> handTiles);

    void tileDiscarded(Side side, Tile tile);

    void tiltTileDiscarded(Side side, Tile tile);

    void selfQuadAdded(Side side, List<Tile> tiles);

    void leftTiltMeldAdded(Side side, List<Tile> tiles);

    void middleTiltMeldAdded(Side side, List<Tile> tiles);

    void rightTiltMeldAdded(Side side, List<Tile> tiles);

    void meldTileAdded(Side side, int index, Tile tile);

    void handUpdated(Side side, List<Tile> handTiles);

    default void roundSettled(String expression){

    }

}
