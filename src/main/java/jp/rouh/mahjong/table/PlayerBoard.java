package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Wind;

public class PlayerBoard{
    private final String name;
    private final Wind seatWind;
    private final int score;
    private final int rank;
    public PlayerBoard(String name, Wind seatWind, int score, int rank){
        this.name = name;
        this.seatWind = seatWind;
        this.score = score;
        this.rank = rank;
    }
    public String getName(){
        return name;
    }
    public Wind getSeatWind(){
        return seatWind;
    }
    public int getScore(){
        return score;
    }
    public int getRank(){
        return rank;
    }
    public static PlayerBoard of(GamePlayer gp, Wind wind){
        return new PlayerBoard(gp.getName(), wind,
                gp.getScore(), gp.getRank());
    }
    public static PlayerBoard of(RoundPlayer rp){
        return new PlayerBoard(rp.getName(), rp.getSeatWind(),
                rp.getScore(), rp.getRank());
    }

    @Override
    public String toString(){
        return "["+seatWind+"]"+name+" ("+score+")";
    }
}
