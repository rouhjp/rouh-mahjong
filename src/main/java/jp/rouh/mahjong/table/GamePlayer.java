package jp.rouh.mahjong.table;

import jp.rouh.mahjong.table.round.RoundParticipant;
import jp.rouh.mahjong.tile.DiceTwin;
import jp.rouh.mahjong.tile.Wind;

public class GamePlayer extends TableStrategyAdaptor implements RoundParticipant{
    private final Player player;
    private final GameMaster gameMaster;
    private final Wind orderWind;
//    private Wind tempSeatWind;
    private int score;
    GamePlayer(Player player, Wind orderWind, GameMaster gameMaster){
        super(player.getViewer());
        this.player = player;
        this.orderWind = orderWind;
//        this.tempSeatWind = orderWind;
        this.gameMaster = gameMaster;
    }

    public int rollDices(){
        var dices = new DiceTwin();
        //diceRolled(orderWind, dice1, dice2);
        return dices.getDiceSum();
    }

    @Override
    public int getScore(){
        return score;
    }

//    public void setTempSeatWind(Wind seatWind){
//        this.tempSeatWind = seatWind;
//    }

    @Override
    public Wind getOrderWind(){
        return orderWind;
    }

    @Override
    public void applyScore(int score){
        this.score += score;
    }

    // delegate from Player
    @Override
    public String getName(){
        return player.getName();
    }
}
