package jp.rouh.mahjong.table;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 和了結果描画インターフェース。
 *
 *
 *
 */
public interface ScoreViewer{

    /**
     *
     * @param tiles 手牌
     * @param winningTile 和了牌
     */
    void showHand(List<Tile> tiles, Tile winningTile);


    void showTiltMeld(Side tilt, List<Tile> tiles);

    void showAddMeld(Side tilt, List<Tile> tiles, Tile addedTile);

    void showSelfQuad(List<Tile> tiles);

    /**
     *
     * @param types 役のリスト
     */
    void showHandType(List<HandType> types);

    /**
     * 表ドラ表示牌を描画します。
     * @param tiles 表ドラ
     */
    void showUpperIndicators(List<Tile> tiles);

    /**
     * 裏ドラ表示牌を描画します。
     * @param tiles 裏ドラ
     */
    void showLowerIndicators(List<Tile> tiles);
}
