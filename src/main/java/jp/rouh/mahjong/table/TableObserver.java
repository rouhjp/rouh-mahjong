package jp.rouh.mahjong.table;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

/**
 * 麻雀のゲーム情況を通知するオブザーバ・インターフェース<br>
 * ユーザのGUIの更新, もしくはCPUプレイヤーが情況を把握するために用います。
 * @author Rouh
 * @version 1.4
 */
public interface TableObserver{

    /**
     * プレイヤー情報の更新を通知します。<br>
     * 座席の変更などでは, 各席に対応して4度このメソッドが呼ばれることになります。
     * @param side 観測者に対するプレイヤーの相対位置
     * @param board プレイヤー情報
     */
    void seatUpdated(Side side, PlayerBoard board);

    /**
     * 親プレイヤーのサイコロの結果を通知します<br>
     * @param side 観測者に対する親プレイヤーの相対位置
     * @param d1 1つ目のサイコロの目(1..6)
     * @param d2 2つ目のサイコロの目(1..6)
     */
    void diceRolled(Side side, int d1, int d2);

    /**
     * 局が開始し, 山牌が生成されたことを通知します。
     */
    void wallGenerated();

    /**
     * 山牌から牌がツモされたことを通知します。<br>
     * @param side 観測者に対するツモ箇所の相対位置
     * @param column 卓の中心から見たとき左から数えたツモ箇所の列(0..16)
     * @param floor ツモ箇所の段目(上の場合0, 下の場合1)
     */
    void wallTileTaken(Side side, int column, int floor);

    /**
     * ドラ表示牌がめくられたことを通知します。<br>
     * @param side 観測者に対するツモ箇所の相対位置
     * @param column 卓の中心から見たとき左から数えたツモ箇所の列(0..16)
     * @param floor ツモ箇所の段目(上の場合0, 下の場合1)
     * @param tile めくられた牌
     */
    void wallTileRevealed(Side side, int column, int floor, Tile tile);


    void riverTileAdded(Side side, Tile tile);

    void riverTileAddedAsReady(Side side, Tile tile);

    void meldAdded(Side side, Meld meld);

    void meldUpdated(Side side, int index, Meld meld);

}
