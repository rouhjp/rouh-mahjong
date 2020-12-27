package jp.rouh.mahjong.score;

import java.util.List;

/**
 * 得点を表すクラス。
 *
 * <p>ある手牌に対する得点, 点数表現, 役を提供します。
 * 得点には詰み符や供託を含ません。
 *
 * <p>例えば, 子の30符4翻の手は,
 * 基本点{@link #getBaseScore}が 30*2^(2 + 4) となり1920点,
 * 得点{@link #getScore}が 1920*4 を100の位で切り上げた7700点,
 * ロン時の放銃者の基本支払額が得点と同じ7700点,
 * ツモ時の親の基本支払額が 1920*2 を100の位で切り上げた3900点
 * ツモ時の子の基本支払額が 1920*1 を100の位で切り上げた2000点
 * となります。
 *
 *
 *
 * @see HandScoreCalculator
 * @author Rouh
 * @version 1.0
 */
public class HandScore implements Comparable<HandScore>{

    /** 符数 */
    private final int point;

    /** 翻数 */
    private final int doubles;

    /** 基本点 */
    private final int baseScore;

    /** 役 */
    private final List<HandType> handTypes;

    private final String limitExpression;
    private final boolean dealer;
    private final boolean handLimit;

    /**
     * 通常役の得点を生成します。
     * @param point 符
     * @param handTypes 役のリスト
     * @param dealer 親かどうか
     * @throws IllegalArgumentException 符が10の位で切り上げられていない場合
     *                                  符が20未満の場合
     */
    private HandScore(int point, List<HandType> handTypes, boolean dealer){
        if(point%10!=0 || point<20){
            throw new IllegalArgumentException("invalid point: "+point);
        }
        this.point = point;
        this.doubles = handTypes.stream().mapToInt(HandType::getDoubles).sum();
        this.baseScore = baseScoreOf(point, doubles);
        this.handTypes = List.copyOf(handTypes);
        this.limitExpression = getScoreLimitExpression(baseScore, false);
        this.dealer = dealer;
        this.handLimit = false;
    }

    /**
     * 役満の得点を生成します。
     * @param handTypes 役のリスト
     * @param dealer 親かどうか
     */
    private HandScore(List<HandType> handTypes, boolean dealer){
        this.point = 0;
        this.doubles = handTypes.stream().mapToInt(HandType::getDoubles).sum();
        this.baseScore = doubles/13*8000;
        this.handTypes = handTypes;
        this.limitExpression = getScoreLimitExpression(baseScore, true);
        this.dealer = dealer;
        this.handLimit = true;
    }

    /**
     * 手牌の役をリスト形式で取得します。
     *
     * <p>役の順序は手牌点数計算機能{@link HandScoreCalculator}の実装に依存します。
     * @return 役のリスト
     */
    public List<HandType> getHandTypes(){
        return handTypes;
    }

//    /**
//     * 手牌の符算出項目をリスト形式で取得します。
//     * @return 符算出項目のリスト
//     */
//    public List<HandPoint> getHandPoints(){
//        return handPoints;
//    }

    /**
     * 手牌の点数区分を取得します。
     *
     * <p>点数区分として, 満貫, 跳満, 倍満, 三倍満, 役満, n倍役満が定義されています。
     * <p>得点が満貫に満たない場合は空の文字列が返されます。
     * @return 点数区分
     */
    public String getLimitExpression(){
        return limitExpression;
    }

    /**
     * 翻数を返します。
     *
     * <p>役満の場合は役満の倍数に13を掛けた値が翻数として返されます。
     * @return 翻数
     */
    public int getDoubles(){
        return doubles;
    }

    /**
     * 符を返します。
     *
     * <p>符は10の位で切り上げられた値です。
     * @return 符
     */
    public int getPoint(){
        return point;
    }

    /**
     * 点数表現を取得します。
     *
     * <p>点数表現は, 符数 + 翻数 + 点数区分 + 点数 で構成されます。
     * このうち点数区分とは, 満貫や役満といった表現を指します。
     * 得点が満貫に満たない場合は, 点数区分は省略されます。
     * 役満の場合は, 符数および翻数は省略されます。
     * @return 点数表現
     */
    public String getScoreExpression(){
        return (handLimit? "":point + "符 " + doubles + "翻 ")
                + (limitExpression.isEmpty()? "":limitExpression + " ")
                + getScore() + "点";
    }

    /**
     * 基本点を取得します。
     *
     * <p>基本点は, 符*(2^(翻 + 2))で算出される値です。
     * 符が10の位に切り上げられているため, 基本点は10の位までの計算精度を持ちます。
     * <p>基本点を子なら4倍, 親なら6倍して100の位で切り上げた値が得点となります。
     * @return 基本点
     */
    public int getBaseScore(){
        return baseScore;
    }

    /**
     * 得点を返します。
     *
     * <p>この値は, 基本点から子なら4倍, 親なら6倍して100の位で切り上げた値を指します。
     * <p>ただし, 実際の支払いにおいては, 子なら基本点, 親なら基本点の二倍の数値を
     * それぞれ100の位で切り上げた額を負担するため, 必ずしも支払い額の合計が得点と合致するとは
     * 限りません。積み棒が無い場合, 一人の放銃者が全額を負担する際の額は得点と一致します。
     * @return 得点
     */
    private int getScore(){
        return (int)Math.ceil(((dealer?6:4)*baseScore)/100d)*100;
    }

    @Override
    public int compareTo(HandScore o){
        return this.getBaseScore() - o.getBaseScore();
    }

    /**
     * 基本点を計算します。
     * @param point 符
     * @param doubles 翻
     * @return 基本点
     */
    private static int baseScoreOf(int point, int doubles){
        if(doubles>=13) return 8000;
        if(doubles>=11) return 6000;
        if(doubles>=8) return 4000;
        if(doubles>=6) return 3000;
        if(doubles>=5) return 2000;
        return Math.min(2000, point*(int)Math.pow(2, doubles + 2));
    }

    /**
     * 点数区分を文字列として返します。
     *
     * <p>点数区分として, 満貫, 跳満, 倍満, 三倍満, 役満, n倍役満が定義されています。
     * 点数区分に満たない点数, もしくは不正な点数であれば空の文字列を返します。
     * @return 点数区分
     */
    private static String getScoreLimitExpression(int baseScore, boolean handLimit){
        if(handLimit){
            int limitMultiplier = baseScore/8000;
            if(limitMultiplier==1) return "役満";
            return suitCharOf(limitMultiplier)+"倍役満";
        }
        if(baseScore==8000) return "数え役満";
        if(baseScore==6000) return "三倍満";
        if(baseScore==4000) return "倍満";
        if(baseScore==3000) return "跳満";
        if(baseScore==2000) return "満貫";
        return "";
    }

    /**
     * 1~10の整数に対応する漢字表記を取得します。
     * 1~10以外は{@link Integer#toString}の結果を返します。
     * @param number 整数
     * @return 漢字表記
     */
    private static String suitCharOf(int number){
        switch(number){
            case 1: return "一";
            case 2: return "二";
            case 3: return "三";
            case 4: return "四";
            case 5: return "五";
            case 6: return "六";
            case 7: return "七";
            case 8: return "八";
            case 9: return "九";
            case 10: return "十";
            default: return Integer.toString(number);
        }
    }

    /**
     * 通常手の手牌得点オブジェクトを生成します。
     * @param point 符
     * @param handTypes 役
     * @param dealer 親かどうか
     * @return 手牌得点
     */
    public static HandScore of(int point, List<HandType> handTypes, boolean dealer){
        return new HandScore(point, handTypes, dealer);
    }

    /**
     * 役満手の手牌得点オブジェクトを生成します。
     * @param limitHandTypes 役
     * @param dealer 親かどうか
     * @return 手牌得点
     */
    public static HandScore ofLimit(List<HandType> limitHandTypes, boolean dealer){
        return new HandScore(limitHandTypes, dealer);
    }

}
