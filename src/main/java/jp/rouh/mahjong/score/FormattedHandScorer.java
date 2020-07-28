package jp.rouh.mahjong.score;

import jp.rouh.mahjong.hand.FormattedHand;

/**
 * 整形済み手牌点数計算機能インターフェース。
 *
 * <p>このインターフェースを実装したクラスは, 手牌及び勝利コンテキストから, 符と役を判定し,
 * 算出された符及び翻数から基本点を取得可能な得点オブジェクト{@link HandScore}を計算する機能を持ちます。
 * @see WinningContext
 * @see HandScore
 * @author Rouh
 * @version 1.0
 */
public interface FormattedHandScorer{

    /**
     * 整形済み手牌と勝利コンテキストを元に手牌の得点を算出します。
     * @param hand 整形済み手牌
     * @param context 勝利コンテキスト
     * @return 手牌の得点
     */
    HandScore calculate(FormattedHand hand, WinningContext context);
}
