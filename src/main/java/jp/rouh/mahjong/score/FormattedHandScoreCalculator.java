package jp.rouh.mahjong.score;

import static java.util.Comparator.naturalOrder;

/**
 * 整形済み手牌点数計算機能インターフェース。
 *
 * <p>手牌点数計算機能の実装を簡易にするためのアダプタインターフェース。
 * @author Rouh
 * @version 1.0
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface FormattedHandScoreCalculator extends HandScoreCalculator{

    /**
     * 整形済み手牌および勝利コンテキストから点数を計算します。
     * @param hand 整形済み手牌
     * @param context 勝利コンテキスト
     * @return 得点
     */
    HandScore calculate(FormattedHand hand, WinningContext context);

    /**
     * {@inheritDoc}
     * <p>和了手牌を整形し、全ての整形済み手牌に対して
     * {@link #calculate(FormattedHand, WinningContext)}メソッドを呼び出し、
     * 最も点数の高い得点オブジェクトを返します。
     * @param hand 和了手牌
     * @param context 勝利コンテキスト
     * @return 得点
     */
    @Override
    default HandScore calculate(WinningHand hand, WinningContext context) throws HandFormatException{
        return hand.format().stream()
                .map(formattedHand->calculate(formattedHand, context))
                .max(naturalOrder())
                .orElseThrow();
    }
}
