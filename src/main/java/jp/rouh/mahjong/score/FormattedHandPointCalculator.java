package jp.rouh.mahjong.score;

/**
 * 符計算機能インターフェース。
 * @author Rouh
 * @version 1.0
 */
@FunctionalInterface
public interface FormattedHandPointCalculator{

    /**
     * 符を計算します。
     *
     * <p>符は20～110の間の10の位で割り切れる整数で返されます。
     * @param hand 整形済み手牌
     * @param context 勝利コンテキスト
     * @return 符数
     */
    int calculate(FormattedHand hand, WinningContext context);

}
