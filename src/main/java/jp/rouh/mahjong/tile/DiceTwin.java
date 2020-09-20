package jp.rouh.mahjong.tile;

public class DiceTwin{
    private final int dice1 = Dices.rollDice();
    private final int dice2 = Dices.rollDice();
    public int getDice1(){
        return dice1;
    }
    public int getDice2(){
        return dice2;
    }
    public int getDiceSum(){
        return dice1 + dice2;
    }
}
