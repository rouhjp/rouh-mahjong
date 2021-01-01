package jp.rouh.mahjong.score;

/**
 * 点数区分
 */
public enum Limit{
    /** ナシ */
    EMPTY(""),

    /** 満貫 */
    JACKPOT("満貫"),

    /** 跳満 */
    GREATER_JACKPOT("跳満"),

    /** 倍満 */
    DOUBLE_JACKPOT("倍満"),

    /** 三倍満 */
    TRIPLE_JACKPOT("三倍満"),

    /** 数え役満 */
    COUNT_LIMIT("数え役満"),

    /** 役満 */
    HAND_LIMIT("役満"),

    /** 二倍役満 */
    DOUBLE_HAND_LIMIT("二倍役満"),

    /** 三倍役満 */
    TRIPLE_HAND_LIMIT("三倍役満"),

    /** 四倍役満 */
    QUADRUPLE_HAND_LIMIT("四倍役満"),

    /** 五倍役満 */
    QUINTUPLE_HAND_LIMIT("五倍役満"),

    /** 六倍役満 */
    SEXTUPLE_HAND_LIMIT("六倍役満"),

    /** 七倍役満 */
    SEPTUPLE_HAND_LIMIT("七倍役満"),

    /** 八倍役満 */
    OCTUPLE_HAND_LIMIT("八倍役満");
    private final String name;
    Limit(String name){
        this.name = name;
    }

    /**
     * 点数区分名を文字列で返します。
     * @return 点数区分名
     */
    public String getName(){
        return name;
    }

    public boolean isEmpty(){
        return this==Limit.EMPTY;
    }

    public int getBaseScore(){
        return 0;
    }

    static Limit valueOf(int point, int doubles){
        if(doubles>=13) return Limit.COUNT_LIMIT;
        if(doubles>=11) return Limit.TRIPLE_JACKPOT;
        if(doubles>=8) return Limit.DOUBLE_JACKPOT;
        if(doubles>=6) return Limit.GREATER_JACKPOT;
        if(doubles>=5) return Limit.JACKPOT;
        if(doubles>=4 && point>=40) return Limit.JACKPOT;
        if(doubles>=3 && point>=70) return Limit.JACKPOT;
        return Limit.EMPTY;
    }

    static Limit valueOf(int limitMultiplier){
        switch(limitMultiplier){
            case 1: return Limit.HAND_LIMIT;
            case 2: return Limit.DOUBLE_HAND_LIMIT;
            case 3: return Limit.TRIPLE_HAND_LIMIT;
            case 4: return Limit.QUADRUPLE_HAND_LIMIT;
            case 5: return Limit.QUINTUPLE_HAND_LIMIT;
            case 6: return Limit.SEXTUPLE_HAND_LIMIT;
            case 7: return Limit.SEPTUPLE_HAND_LIMIT;
            case 8: return Limit.OCTUPLE_HAND_LIMIT;
        }
        throw new IllegalArgumentException("");
    }
}
