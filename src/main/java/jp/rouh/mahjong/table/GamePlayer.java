package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Dices;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Wind;

import java.util.stream.Collectors;

/**
 * ゲーム参加者を表すクラス<br>
 * このクラスは実質的に{@code Player }クラスのインナークラスです。<br>
 * @see Player
 * @see RoundPlayer
 * @author Rouh
 * @version 1.0 prototype
 */
public class GamePlayer extends Player{
    private final Game game;
    private final Wind directionWind;
    private int score = Game.DEFAULT_SCORE;
    public GamePlayer(Game game, Player parent, Wind directionWind){
        super(parent);
        this.game = game;
        this.directionWind = directionWind;
    }
    protected GamePlayer(GamePlayer orig){
        super(orig);
        this.game = orig.game;
        this.directionWind = orig.directionWind;
    }
    public Wind getDirectionWind(){
        return directionWind;
    }
    public Wind getInitialSeatWind(){
        return Wind.EAST.shift((4 +
                game.getInitialDealerDirection().ordinal() -
                directionWind.ordinal())%4);
    }
    public int getScore(){
        return score;
    }
    public int getRank(){
        return game.players().stream()
                .map(GamePlayer::getScore)
                .sorted()
                .collect(Collectors.toList())
                .indexOf(this.getScore());
    }
    public GamePlayer nextPlayer(){
        return playerAt(Side.RIGHT);
    }
    public GamePlayer playerAt(Side side){
        return game.playerAt(side.of(directionWind));
    }

    public Wind rollDicesToPickDirection(){
        var d1 = Dices.rollDices();
        var d2 = Dices.rollDices();
        game.players().forEach(opponent->
                opponent.diceRolled(directionWind.from(opponent.directionWind), d1, d2));
        var selectedDirection = Side.of(d1, d2).of(directionWind);
        game.players().forEach(eachPlayer->{
            var eachWind = eachPlayer.directionWind.from(selectedDirection).of(Wind.EAST);
            var eachBoard = eachPlayer.getPlayerBoard(eachWind);
            game.players().forEach(opponent->{
                var side = opponent.directionWind.from(eachPlayer.directionWind);
                opponent.seatUpdated(side, eachBoard);
            });
        });
        return Side.of(d1, d2).of(directionWind);
    }


    public PlayerBoard getPlayerBoard(Wind wind){
        return PlayerBoard.of(this, wind);
    }
}
