package jp.rouh.mahjong.score;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 手牌の得点を表す不変クラス。
 *
 * <p>手牌に対する得点として符数・翻数・得点区分をまとめた点数表現,
 * 役および親・子によって決定される得点, 役名のリストを保持します。
 *
 * <p>詰み符や供託, 各プレイヤー間の最終的な点数移動等の計算は,
 * このクラスでは扱いません。
 *
 * @see HandScorer
 * @author Rouh
 * @version 1.0
 */
public class HandScore{
    private final int score;
    private final String expression;
    private final List<String> handTypeNames;
    private HandScore(int score, String expression, List<String> handTypeNames){
        this.score = score;
        this.expression = expression;
        this.handTypeNames = List.copyOf(handTypeNames);
    }

    /**
     * 手牌の役をリスト形式で取得します。
     *
     * <p>役を格納する順番は実装に依存します。
     * @return 役のリスト
     */
    public List<String> getHandTypeNames(){
        return handTypeNames;
    }

    /**
     * 点数表現を取得します。
     *
     * <p>点数表現は, 符数 + 翻数 + 点数区分 + 点数 で構成されます。
     * このうち点数区分とは, 満貫や役満といった表現を指します。
     * 満貫未満の場合は, 点数区分は省略されます。
     * 役満の場合は, 符数および翻数は省略されます。
     * @return 点数表現
     */
    String getScoreExpression(){
        return expression;
    }

    /**
     * 得点を返します。
     *
     * <p>この値は, 基本点から子なら4倍, 親なら6倍した値を指します。
     * つまり, この値は詰み符や供託が無い場合, 和了者が得る点数と一致します。
     * @return 得点
     */
    public int getScore(){
        return score;
    }

    private static HandScore of(int basicPoint, int typePoint,
                                List<String> handTypeNames, boolean epic, boolean dealer){
        var baseScore = Scores.baseScoreOf(basicPoint, typePoint);
        var score = Scores.scoreOf(baseScore, dealer);
        var sb = new StringBuilder();
        if(!epic){
            sb.append(basicPoint);
            sb.append("符");
            sb.append(typePoint);
            sb.append("翻");
            sb.append(" ");
        }
        var scoreClass = Scores.scoreClassOf(baseScore);
        if(!scoreClass.isEmpty()){
            sb.append(scoreClass);
            sb.append(" ");
        }
        sb.append(score);
        sb.append("点");
        return new HandScore(score, sb.toString(), handTypeNames);
    }

    public static HandScore of(int basicPoint, int typePoint, List<String> handTypeNames, boolean dealer){
        return of(basicPoint, typePoint, handTypeNames, false, dealer);
    }

    public static HandScore ofEpic(int epicTypePoint, List<String> handTypeNames, boolean dealer){
        return of(0, epicTypePoint, handTypeNames, true, dealer);
    }

    public static HandScore of(int basicPoint, List<HandType> handTypes, boolean dealer){
        var typePoint = handTypes.stream().mapToInt(HandType::getTypePoint).sum();
        var handTypeNames = handTypes.stream().map(HandType::getName).collect(toList());
        return of(basicPoint, typePoint, handTypeNames, dealer);
    }

    public static HandScore ofEpic(List<HandType> handTypes, boolean dealer){
        var epicTypePoint = handTypes.stream().mapToInt(HandType::getTypePoint).sum();
        var handTypeNames = handTypes.stream().map(HandType::getName).collect(toList());
        return ofEpic(epicTypePoint, handTypeNames, dealer);
    }
}
