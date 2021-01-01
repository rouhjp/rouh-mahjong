package jp.rouh.mahjong.app.bitv.table;

import jp.rouh.mahjong.table.Declaration;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

/**
 * テーブルの描画機能インターフェース。
 * @author Rouh
 * @version 1.0
 */
public interface TableViewer{

    /**
     * テーブルの状態をリセットします。
     */
    void clear();

    /**
     * 局情報を表示します。
     */
    void showRoundInformation(Wind w, int count, int streak);

    /**
     * プレイヤーの宣言を表示します。
     * @param d 方向
     * @param dec 宣言
     */
    void showDeclaration(Direction d, Declaration dec);

    /**
     * 局情報を更新します。
     * @param w 場風
     * @param count 局数(1..4)
     */
    void updateRoundInfo(Wind w, int count);

    /**
     * 積み棒の数を更新します。
     * @param streak 積み棒
     */
    void updateStreakCount(int streak);

    /**
     * 供託の数を更新します。
     * @param carryOver 供託
     */
    void updateCarryOverCount(int carryOver);

    /**
     * プレイヤーの自風を表示します。
     * @param d 方向
     * @param wind 自風
     */
    void updatePlayerWind(Direction d, Wind wind);

    /**
     * プレイヤーの得点を表示します。
     * @param d 方向
     * @param score 得点
     */
    void updatePlayerScore(Direction d, int score);

    /**
     * プレイヤーの名前を表示します。
     * @param d 方向
     * @param name 名前
     */
    void updatePlayerName(Direction d, String name);

    /**
     * リーチ棒を表示します。
     * @param d 方向
     */
    void putReadyBone(Direction d);

    /**
     * 捨て牌を追加します。
     *
     * @param d 方向
     * @param t 牌
     */
    void appendRiverTile(Direction d, Tile t);

    /**
     * 捨て牌を横向きで追加します。
     * @param d 方向
     * @param t 牌
     */
    void appendRiverTileRotated(Direction d, Tile t);

    /**
     * 最後の捨て牌を取り除きます。
     * @param d 方向
     */
    void removeLastRiverTile(Direction d);

    /**
     * 134枚の山牌を表示します。
     */
    void putAllWallTiles();

    /**
     * 指定の場所にある山牌を取り除きます。
     * @param d 方向
     * @param index 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     */
    void removeWallTile(Direction d, int index, int floor);

    /**
     * 指定の場所にある山牌を表向きにします。
     * @param d 方向
     * @param index 左から数えた山牌の列(0..16)
     * @param floor 山牌の段目(0..1)
     * @param t 牌
     */
    void revealWallTile(Direction d, int index, int floor, Tile t);

    /**
     * 下方向のプレイヤーの手牌を更新します。
     * @param tiles 手牌
     * @param isolation 手牌の最後の牌に間隔を設けるかどうか
     */
    void updateHandTiles(List<Tile> tiles, boolean isolation);

    /**
     * プレイヤーの手牌を更新します。
     * @param d 方向(下方向を除く)
     * @param size 手牌の枚数
     * @param isolation 手牌の最後の牌に間隔を設けるかどうか
     * @throws IllegalArgumentException 方向に下方向を指定した場合
     */
    void updateHandTiles(Direction d, int size, boolean isolation);

    /**
     * 表向きで手牌を表示します。
     * @param d 方向
     * @param tiles 手牌
     * @param isolation 手牌の最後の牌に間隔を設けるかどうか
     */
    void updateHandTilesFaceUp(Direction d, List<Tile> tiles, boolean isolation);

    /**
     * 裏向きで手牌を表示します。
     * @param d 方向
     * @param size 手牌の枚数
     */
    void updateHandTilesFaceDown(Direction d, int size);

    void appendLeftTiltMeld(Direction d, List<Tile> tiles);

    void appendMiddleTiltMeld(Direction d, List<Tile> tiles);

    void appendRightTiltMeld(Direction d, List<Tile> tiles);

    void addTileToMeld(Direction d, int meldIndex, Tile t);

    void appendSelfQuad(Direction d, List<Tile> tiles);

}
