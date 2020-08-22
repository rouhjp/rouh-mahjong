package jp.rouh.mahjong.score;

/**
 * 点数計算に関するユーティリティクラス。
 *
 * @author Rouh
 * @version 1.0
 */
public final class Scores{
    private Scores(){
        throw new AssertionError("no instance");
    }

    /**
     * 役満手の点数表現を取得します。
     * @param score 点数
     * @param scoreClass 点数区分
     * @return 点数表現
     */
    public static String scoreExpressionOfEpic(int score, String scoreClass){
        return scoreClass + " " + score + "点";
    }

    /**
     * 通常手の点数表現を取得します。
     * @param basicPoint 符
     * @param typePoint 翻
     * @param score 点数
     * @param scoreClass 点数区分
     * @return 点数表現
     */
    public static String scoreExpressionOf(int basicPoint, int typePoint, int score, String scoreClass){
        return basicPoint + "符" + typePoint + "翻 " +
                (scoreClass.isEmpty()?"":scoreClass + " ") + score + "点";
    }

    /**
     * 基本点に対応する点数区分を返します。
     *
     * <p>点数区分として, 満貫, 跳満, 倍満, 三倍満, 役満
     * および二～九倍役満が定義されています。
     * 点数区分に満たない点数, もしくは不正な点数であれば
     * 空の文字列を返します。
     * @param baseScore 基本点
     * @return 点数区分
     */
    public static String scoreClassOf(int baseScore){
        if(baseScore>8000){
            if(baseScore%8000!=0) return "";
            int multiplier = baseScore/8000;
            if(multiplier>=10) return "";
            var suitChars = new String[]{
                    "", "", "二", "三", "四",
                    "五", "六", "七", "八", "九"
            };
            return suitChars[multiplier]+"倍役満";
        }
        if(baseScore==8000) return "役満";
        if(baseScore==6000) return "三倍満";
        if(baseScore==4000) return "倍満";
        if(baseScore==3000) return "跳満";
        if(baseScore==2000) return "満貫";
        return "";
    }

    /**
     * 与えられた数を100の位で切り上げます。
     * @param point 任意の数
     * @return 100の位で切り上げた数
     */
    public static int ceilToHundred(int point){
        return (int)Math.ceil(point/100d)*100;
    }

    /**
     * 与えられた数を10の位で切り上げます。
     *
     * <p>この関数は符の計算に用いられます。
     * @param point 任意の数
     * @return 10の位で切り上げた数
     */
    public static int ceilToTen(int point){
        return (int)Math.ceil(point/10d)*10;
    }

    /**
     * 役満の基本点を返します。
     * @param epicMultiplier 役満の倍数
     * @return 基本点
     */
    public static int baseScoreOfEpic(int epicMultiplier){
        return 8000*epicMultiplier;
    }

    /**
     * 指定された符と翻に対応する基本点を返します。
     *
     * <p>基本点は, 符*(2^(翻 + 2))で算出される値で,
     * 子なら4倍, 親なら6倍した点数が得点となります。
     * @param basicPoint 符
     * @param typePoint 翻
     * @return 基本点
     */
    public static int baseScoreOf(int basicPoint, int typePoint){
        if(typePoint>=13) return 8000;
        if(typePoint>=11) return 6000;
        if(typePoint>=8) return 4000;
        if(typePoint>=6) return 3000;
        if(typePoint>=5) return 2000;
        return Math.min(2000, basicPoint*(int)Math.pow(2, typePoint + 2));
    }

    /**
     * 指定された基本点に子であれば4, 親であれば6を掛け得点を算出します。
     * @param baseScore 基本点
     * @param dealer true  親の場合
     *               false 子の場合
     * @return 得点
     */
    public static int scoreOf(int baseScore, boolean dealer){
        return baseScore*(dealer?6:4);
    }
}
