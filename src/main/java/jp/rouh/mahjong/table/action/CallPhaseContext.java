package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 他プレイヤー打牌時の入力を仲介するインターフェース。
 *
 */
public interface CallPhaseContext{

    List<Tile> getTiles();

    Tile getCallTile();

    /**
     * チーの入力が可能か検査します。
     * @return true  チーができる場合
     *         false チーができない場合
     */
    boolean canCallStraight();

    boolean canCallTriple();

    boolean canCallQuad();

    boolean canCallWin();

    /**
     * この牌がチー対象牌かどうか検査します。
     * @param tile 手牌中の牌
     * @return true  チー対象牌の場合
     *         false チー対象牌でない場合
     */
    boolean canCallStraight(Tile tile);

    boolean canCallTriple(Tile tile);

    boolean canCallQuad(Tile tile);


    boolean canCallStraight(Tile tile, Tile selected);

    boolean canCallTriple(Tile tile, Tile selected);
}
