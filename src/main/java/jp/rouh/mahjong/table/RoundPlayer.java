package jp.rouh.mahjong.table;

import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.ArrayList;
import java.util.List;

public class RoundPlayer extends GamePlayer{
    private final Round round;
    private final Wind seatWind;
    private final List<Tile> handTiles;
    private final List<Meld> openMelds;

    RoundPlayer(Round round, GamePlayer parent, Wind wind){
        super(parent);
        this.round = round;
        this.seatWind = wind;
        this.handTiles = new ArrayList<>(14);
        this.openMelds = new ArrayList<>(4);
    }

    public Wind getSeatWind(){
        return seatWind;
    }

    void draw(Tile tile){

    }

    boolean canDeclareReady(){


        return false;
    }













}
