package jp.rouh.mahjong.score;

import jp.rouh.mahjong.hand.FormattedHand;

import java.util.Set;

import static java.util.Comparator.comparing;

/**
 * 手牌から導かれる全ての整形済み手牌パターンを並列的に評価して
 * 最も高い点数を算出する手牌点数計算機能クラス。
 *
 * <p>このクラスは, 整形済み手牌点数計算機能{@link FormattedHandScorer}の実装を
 * 手牌点数計算機能{@link HandScorer}へアダプトします。
 *
 * @see FormattedHandScorer
 * @see HandScorer
 * @author Rouh
 * @version 1.0
 */
public abstract class ParallelHandScorer implements HandScorer, FormattedHandScorer{

    /**
     * {@inheritDoc}
     * コンストラクタで渡された整形済み手牌点数計算オブジェクトを利用して
     * 与えられた整形済み手牌のセットに対応する点数を並列的に計算し,
     * 最も高い点数を返します。
     * @throws java.util.NoSuchElementException 最大の点数が算出できなかった場合
     */
    @Override
    public final HandScore calculate(Set<FormattedHand> hands, WinningContext context){
        return hands.stream().parallel()
                .map(hand->calculate(hand, context))
                .max(comparing(HandScore::getScore))
                .orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract HandScore calculate(FormattedHand hand, WinningContext context);
}
