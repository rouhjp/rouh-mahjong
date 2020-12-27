package jp.rouh.mahjong.table;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

/**
 * プレイヤーに麻雀卓上の変化を通知するインターフェース。
 *
 * <p>このインターフェースではビューが実装すべき項目を定義します。
 * @author Rouh
 * @version 1.0
 */
public interface TableObserver{

    /**
     * プレイヤーの情報が更新されたことを通知します。
     * @param side 観測者に対するプレイヤーの相対位置
     * @param wind プレイヤーの風
     * @param name プレイヤー名
     * @param score プレイヤーのスコア
     */
    void seatUpdated(Side side, Wind wind, String name, int score);

    void roundStarted(Wind wind, int count, int streak, int deposit);

    void roundSettled(String expression);

    /**
     * 親プレイヤーのサイコロの結果を通知します。
     * @param side 観測者に対する親プレイヤーの相対位置
     * @param dice1 1つ目のサイコロの目(1..6)
     * @param dice2 2つ目のサイコロの目(1..6)
     */
    void diceRolled(Side side, int dice1, int dice2);

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

    /**
     * ツモによって牌が手に1枚追加されたことを通知します。
     *
     * <p>この処理は自摸牌が見えない他家に対する処理です。
     * 自家のツモは変わりに{@link #tileDrawn(Tile)}メソッドが利用されます。
     * @param side 観測者に対する自摸者の相対位置
     */
    void tileDrawn(Side side);

    /**
     * ツモによって牌が自家の手に1枚追加されたことを通知します。
     * @param tile 自摸牌
     */
    void tileDrawn(Tile tile);

    /**
     * 配牌や副露によって手牌が変更されたことを通知します。
     *
     * <p>この処理は手牌が見えない他家に対する処理です。
     * 自家の手牌の変更は変わりに{@link #handUpdated(List)}メソッドが利用されます。
     * @param side 観測者に対する手牌が変更されたプレイヤーの相対位置
     * @param size 変更後の手牌の長さ
     */
    void handUpdated(Side side, int size);

    /**
     * 配牌や副露によって自家の手牌が変更されたことを通知します。
     * @param handTiles 手牌
     */
    void handUpdated(List<Tile> handTiles);

    /**
     * 手牌が倒されたことを通知します。
     * @param side 観測者に対する倒された手牌の相対位置
     * @param handTiles 手牌
     */
    void handRevealed(Side side, List<Tile> handTiles);

    /**
     * 手牌が倒されたことを通知します。
     * @param side 観測者に対する倒された手牌の相対位置
     * @param handTiles 手牌
     * @param drawnTile ツモ牌
     */
    void handRevealed(Side side, List<Tile> handTiles, Tile drawnTile);

    /**
     * 牌が捨てられたことを通知します。
     * @param side 観測者に対する打牌者の相対位置
     * @param tile 捨て牌
     */
    void tileDiscarded(Side side, Tile tile);

    /**
     * 牌が立直宣言牌として横向きに捨てられたことを通知します。
     * @param side 観測者に対する打牌者の相対位置
     * @param tile 捨て牌
     */
    void tileDiscardedAsReady(Side side, Tile tile);

    /**
     * 最後に捨てた捨て牌が副露によって河から取り除かれたことを通知します。
     * @param side 観測者に対する放銃者の相対位置
     */
    void riverTileTaken(Side side);

    /**
     * 面子(順子/刻子/大明槓)が追加されたことを通知します。
     * @param side 観測者に対する面子追加者の相対位置
     * @param tilt 副露元
     * @param tiles 構成牌
     */
    void tiltMeldAdded(Side side, Side tilt, List<Tile> tiles);

    /**
     * 暗槓が追加されたことを通知します。
     * @param side 観測者に対する暗槓追加者の相対位置
     * @param tiles 構成牌
     */
    void selfQuadAdded(Side side, List<Tile> tiles);

    /**
     * 加槓が発生したことを通知します。
     * @param side 観測者に対する加槓宣言者の相対位置
     * @param index 追加先の刻子の位置(0..3)
     * @param added 追加牌
     */
    void meldTileAdded(Side side, int index, Tile added);

    /**
     * リーチ棒が追加されたことを通知します。
     * @param side 観測者に対する立直者の相対位置
     */
    void readyBoneAdded(Side side);

    /**
     * プレイヤーが何らかの宣言を行ったことを通知します。
     * @param side 観測者に対する宣言者の相対位置
     * @param declaration 宣言
     */
    void declared(Side side, Declaration declaration);

}
