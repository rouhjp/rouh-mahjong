package jp.rouh.mahjong.table;

public enum ResultType{
    SETTLED("和了"),
    EXHAUSTION("荒廃平定"),
    NINE_TYPES("九種九牌"),
    FOUR_WINDS("四風連打"),
    FOUR_QUADS("四槓散了"),
    FOUR_REACHES("四家立直"),
    THREE_STEALS("三家和");
    private final String name;
    ResultType(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
