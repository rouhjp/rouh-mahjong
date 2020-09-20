package jp.rouh.mahjong.score;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

/**
 * 役の判定に際して必要となる勝利時の情況を表すインターフェース。
 * @author Rouh
 * @version 1.0
 */
public interface WinningContext{

    /**
     * 局の場風を返します。
     * @return 場風
     */
    Wind getRoundWind();

    /**
     * 和了プレイヤーの自風を返します。
     * @return 自風
     */
    Wind getSeatWind();

    /**
     * 和了プレイヤーが親かどうか検査します。
     * @return true  和了プレイヤーが親の場合
     *         false 和了プレイヤーが子の場合
     */
    boolean isDealer();

    /**
     * 和了プレイヤーが門前かどうか検査します。
     * ロン和了であっても副露(暗槓を含まない)がなければ門前となります。
     * @return true  門前の場合
     *         false 門前でない場合
     */
    boolean isConcealed();

    /**
     * 和了プレイヤーが立直していたかどうか検査します。
     * @return true  立直の場合
     *         false 立直でない場合
     */
    boolean isReady();

    /**
     * 和了プレイヤーがダブル立直していたかどうか検査します。
     * この結果が{@code true}のときは必ず{@link #isReady()}は{@code true}となります。
     * @return true  ダブル立直の場合
     *         false ダブル立直でない場合
     */
    boolean isFirstAroundReady();

    /**
     * この和了が一発かどうか検査します。
     * この検査に適合する場合は必ず{@link #isReady()}も適合します。
     * @return true  一発の場合
     *         false 一発でない場合
     */
    boolean isReadyAroundWin();

    /**
     * この和了が鳴きを挟まない一巡目の和了であるか検査します。
     * @return true  鳴きを挟まない一巡目の場合
     *         false 鳴きを挟まない一巡目でない場合
     */
    boolean isFirstAroundWin();

    /**
     * この和了が海底または河底であるか検査します。
     * ルール上, 海底牌では槓は不可能(海底牌が移動が不可能)であるため,
     * この検査に適合する場合は{@link #getWinningType()}の値は{@code WinningType.QUAD_DRAW}以外になります。
     * @return true  海底の場合, 河底の場合
     *         false 海底でも河底でもない場合
     */
    boolean isLastTileWin();

    /**
     * 和了タイプを取得します。
     * @see WinningType
     * @return 和了タイプ
     */
    WinningType getWinningType();

    /**
     * 和了プレイヤーがカンを宣言した回数を取得します。
     * @return カン回数
     */
    int getQuadCount();

    /**
     * ドラのリストを取得します。
     * 裏ドラを含みません。裏ドラは{@link #getHiddenPrisedTiles}にて取得します。
     * ドラ表示牌ではなく, ドラ自体であることに注意が必要です。
     * @return ドラのリスト
     */
    List<Tile> getOpenPrisedTiles();

    /**
     * 裏ドラのリストを取得します。
     * ドラ表示牌ではなく, ドラ自体であることに注意が必要です。
     * @return 裏ドラのリスト
     */
    List<Tile> getHiddenPrisedTiles();
}
