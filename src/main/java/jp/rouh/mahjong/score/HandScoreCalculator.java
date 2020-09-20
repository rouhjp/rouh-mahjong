package jp.rouh.mahjong.score;

/**
 * 手牌点数計算機能インターフェース。
 *
 * <p>このインターフェースを実装したクラスは, 手牌及び勝利コンテキストから, 符と役を判定し,
 * 算出された符及び翻数から基本点を取得可能な手牌の得点{@link HandScore}を計算する機能を持ちます。
 *
 * @see WinningContext
 * @see HandScore
 * @author Rouh
 * @version 1.0
 */
public interface HandScoreCalculator{
    /**
     * 手牌と勝利コンテキストを元に手牌の得点を算出します。
     *
     * <p>手牌の役または符の計算に複数の解釈が可能な場合は,
     * 高点法ルールに基づき最も点数が高く評価される解釈を採用します。
     * <p>役がつかない場合は役無しの{@link HandScore}オブジェクトを返します。
     * @see HandScore
     * @see WinningContext
     * @param hand 和了手牌
     * @param context 勝利コンテキスト
     * @throws IllegalArgumentException 手牌が完成形でない場合
     * @return 手牌の得点
     */
    HandScore calculate(WinningHand hand, WinningContext context);

    /**
     * 与えられた手牌と勝利コンテキストで点数が発生するかどうか検査します。
     * @return true  点数が発生する場合
     *         false 点数が発生しない場合(0翻)
     *               手牌が未完成の場合
     */
    boolean checkIfScorePresent(WinningHand hand, WinningContext context);

}
