package jp.rouh.mahjong.score;

import jp.rouh.mahjong.hand.FormattedHand;
import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.score.scorer.StandardHandScorer;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
public interface HandScorer{

    /**
     * 全てのパターンの整形済み手牌と勝利コンテキストを元に手牌の得点を算出します。
     *
     * <p>手牌の役または符の計算に複数の解釈が可能な場合は,
     * 高点法ルールに基づき最も点数が高く評価される解釈を採用します。
     * <p>役がつかない場合は役無しの{@link HandScore}オブジェクトを返します。
     * @see HandScore
     * @see WinningContext
     * @param hands 全てのパターンの整形済み手牌のセット
     * @param context 勝利コンテキスト
     * @return 手牌の得点
     */
    HandScore calculate(Set<FormattedHand> hands, WinningContext context);

    /**
     * 手牌と勝利コンテキストを元に手牌の得点を算出します。
     *
     * <p>与えられた手牌が整形不可能であった場合は{@code Optional.empty()}が返されます。
     * <p>手牌の役または符の計算に複数の解釈が可能な場合は,
     * 高点法ルールに基づき最も点数が高く評価される解釈を採用します。
     * <p>役がつかない場合は役無しの{@link HandScore}オブジェクトを返します。
     * @see HandScore
     * @see WinningContext
     * @see StandardHandScorer
     * @param handTiles 手牌中の牌
     * @param openMelds 副露した面子(暗槓を含む)
     * @param context 勝利コンテキスト
     * @return 手牌の得点
     */
    static Optional<HandScore> evaluate(List<Tile> handTiles, List<Meld> openMelds, WinningContext context){
        var defaultScorer = new StandardHandScorer();
        var winningTile = context.getWinningTile();
        var selfDraw = context.getWinningType().isSelfDraw();
        var formattedHands = FormattedHand.of(handTiles, openMelds, winningTile, selfDraw);
        if(formattedHands.isEmpty()) return Optional.empty();
        return Optional.of(defaultScorer.calculate(formattedHands, context));
    }
}
