package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;

public interface TurnPhaseContext{

    /**
     *
     */
    List<Tile> getTiles();

    /**
     * 九種九牌宣言が可能かどうか検査します。
     * @return true  九種九牌宣言可能な場合
     *         false 九種九牌宣言不可能な場合
     */
    boolean canDeclareDraw();

    /**
     * ツモ和了が可能かどうか検査します。
     * @return true  ツモ和了可能な場合
     *         false ツモ和了不可能な場合
     */
    boolean canDeclareWin();

    /**
     * リーチが可能かどうか検査します。
     * @return true  リーチ可能な場合
     *         false リーチ不可能な場合
     */
    boolean canDeclareReady();

    /**
     * カン(加槓/暗槓)が可能かどうか検査します。
     * @return true  カンが可能な場合
     *         false カンが不可能な場合
     */
    boolean canDeclareQuad();

    /**
     * 指定した牌を打牌可能かどうか検査します。
     * @param tile 検査対象牌
     * @return true  打牌が可能な場合
     *         false 打牌が不可能な場合
     */
    boolean canDiscard(Tile tile);

    /**
     * 指定した牌でカン(加槓/暗槓)が可能かどうか検査します。
     * @param tile 検査対象牌
     * @return true  カンが可能な場合
     *         false カンが不可能な場合
     */
    boolean canDeclareReady(Tile tile);

    /**
     * 指定した牌でカン(加槓/暗槓)が可能かどうか検査します。
     * @param tile 検査対象牌
     * @return true  カンが可能な場合
     *         false カンが不可能な場合
     */
    boolean canDeclareQuad(Tile tile);

    boolean canDeclareAddQuad(Tile tile);

    boolean canDeclareSelfQuad(Tile tile);
}
