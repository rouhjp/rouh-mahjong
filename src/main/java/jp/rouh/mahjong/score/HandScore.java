package jp.rouh.mahjong.score;

import java.util.List;

/**
 * 手牌の得点を表すインターフェース。
 *
 * <p>ある手牌に対する得点, 点数表現, 役を提供します。
 * 得点には詰み符や供託を含ません。
 * @see HandScorer
 * @author Rouh
 * @version 1.0
 */
public interface HandScore{

    /**
     * 手牌の役をリスト形式で取得します。
     *
     * <p>役の順序は手牌点数計算機能{@link HandScorer}の実装に依存します。
     * @return 役のリスト
     */
    List<HandType> getHandTypes();

    /**
     * 点数表現を取得します。
     *
     * <p>点数表現は, 符数 + 翻数 + 点数区分 + 点数 で構成されます。
     * このうち点数区分とは, 満貫や役満といった表現を指します。
     * 満貫未満の場合は, 点数区分は省略されます。
     * 役満の場合は, 符数および翻数は省略されます。
     * @return 点数表現
     */
    String getScoreExpression();

    /**
     * 得点を返します。
     *
     * <p>この値は, 基本点から子なら4倍, 親なら6倍した値を指します。
     * つまり, この値は詰み符や供託が無い場合, 和了者が得る点数と一致します。
     * @return 得点
     */
    int getScore();

    /**
     * 役無しかどうか検査します。
     * @return true  役無しの場合
     *         false 役がある場合
     */
    default boolean hasNoType(){
        return getHandTypes().isEmpty();
    }

    /**
     * 通常手の手牌得点オブジェクトを生成します。
     * @param basicPoint 符
     * @param handTypes 役
     * @param dealer 親かどうか
     * @return 手牌得点
     */
    static HandScore of(int basicPoint, List<HandType> handTypes, boolean dealer){
        int typePoint = handTypes.stream().mapToInt(HandType::getTypePoint).sum();
        int baseScore = Scores.baseScoreOf(basicPoint, typePoint);
        int score = Scores.scoreOf(baseScore, dealer);
        var scoreClass = Scores.scoreClassOf(baseScore);
        var expression = Scores.scoreExpressionOf(basicPoint, typePoint, score, scoreClass);
        return new HandScore(){
            @Override
            public List<HandType> getHandTypes(){
                return handTypes;
            }
            @Override
            public String getScoreExpression(){
                return expression;
            }
            @Override
            public int getScore(){
                return score;
            }
        };
    }

    /**
     * 役満手の手牌得点オブジェクトを生成します。
     * @param handTypes 役
     * @param dealer 親かどうか
     * @return 手牌得点
     */
    static HandScore ofEpic(List<HandType> handTypes, boolean dealer){
        int epicMultiplier = handTypes.stream().mapToInt(HandType::getTypePoint).sum();
        int baseScore = Scores.baseScoreOfEpic(epicMultiplier);
        int score = Scores.scoreOf(baseScore, dealer);
        var scoreClass = Scores.scoreClassOf(baseScore);
        var expression = Scores.scoreExpressionOfEpic(score, scoreClass);
        return new HandScore(){
            @Override
            public List<HandType> getHandTypes(){
                return handTypes;
            }
            @Override
            public String getScoreExpression(){
                return expression;
            }
            @Override
            public int getScore(){
                return score;
            }
        };
    }
}
