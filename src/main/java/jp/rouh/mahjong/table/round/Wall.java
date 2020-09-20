package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 山を表すインターフェース。
 *
 * @author Rouh
 * @version 1.0
 */
public interface Wall{

    /**
     * 残りツモ可能枚数を取得します。
     * @return ツモ可能枚数
     */
    int getDrawableTileCount();

    /**
     * ツモ可能牌が存在するか検査します。
     * @return true  残りツモ可能枚数が1以上の場合
     *         false 残りツモ可能枚数が0の場合
     */
    boolean hasDrawableTile();

    /**
     * 山から牌を1枚ツモします。
     *
     * <p>この操作の結果として山のツモ可能牌は1枚減ります。
     * 山のツモ可能枚数が0の状態でこの操作を行った場合は,
     * {@link IllegalStateException}例外が発生します。
     * @throws IllegalStateException ツモ可能牌が0の場合
     * @return ツモ牌
     */
    Tile takeTile();

    /**
     * 山から牌を4枚ツモします。
     *
     * <p>この操作は基本的に{@link #takeTile()}メソッドを
     * 4回呼び出す操作と等価です。
     * <p>この操作の結果として山のツモ可能牌は4枚減ります。
     * この操作は配牌時のツモとして利用されることを想定します。
     * 山のツモ可能枚数が4未満の状態でこの操作を行った場合は,
     * {@link IllegalStateException}例外が発生します。
     * @throws IllegalStateException ツモ可能牌が0の場合
     * @return ツモ牌のリスト
     */
    default List<Tile> takeFourTiles(){
        return List.of(takeTile(), takeTile(), takeTile(), takeTile());
    }

    /**
     * 嶺上牌を1枚ツモします。
     *
     * <p>この操作の結果として嶺上牌は1枚減ります。
     * 嶺上牌は計4枚です。この操作が5回呼ばれた場合,
     * {@link IllegalStateException}例外が発生します。
     * @throws IllegalStateException 5枚目の嶺上牌をツモしようとした場合
     * @return ツモ牌
     */
    Tile takeQuadTile();

    /**
     * 新ドラを1枚即時追加します。
     * @throws IllegalStateException 6枚目のドラを追加しようとした場合
     */
    void revealIndicatorImmediately();

    /**
     * 新ドラがあれば, 追加します。
     *
     * <p>明槓はカン宣言時に即時新ドラ追加は実行されず,
     * 当該ターン終了時(打牌成立時/連続カンを含む副露成立時)に実行されます。
     * <p>このため, この操作はターン終了時に実行されることを想定し,
     * 明槓が当該ターンに発生していた場合,
     * {@link #revealIndicatorImmediately()}メソッドを呼び出し,
     * 即めくりでない新ドラを追加します。
     * <p>当該ターンに明槓が発生していたかどうかは,
     * {@link #takeQuadTile()}メソッドが呼び出された回数と
     * {@link #revealIndicatorImmediately()}メソッドが呼び出された回数の
     * 差分で検査します。
     * @throws IllegalStateException 6枚目のドラを追加しようとした場合
     */
    void revealIndicatorIfPresent();

    /**
     * 表ドラ表示牌をリスト形式で取得します。
     * @return 表ドラ表示牌のリスト
     */
    List<Tile> getUpperIndicators();

    /**
     * 裏ドラ表示牌をリスト形式で取得します。
     * @return 裏ドラ表示牌のリスト
     */
    List<Tile> getLowerIndicators();
}
