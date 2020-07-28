package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Dices;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

public class Round{
    private final Game game;
    private final Wind roundWind;
    private final int roundCount;
    private final int streakCount;
    private final List<RoundPlayer> players;
    private Wall wall;
    public Round(Game game, Wind roundWind, int roundCount, int streakCount){
        this.game = game;
        this.roundWind = roundWind;
        this.roundCount = roundCount;
        this.streakCount = streakCount;
        var dealer = game.getInitialDealerDirection()
                .shift(roundWind.ordinal() + roundCount - 1);
        this.players = List.of(
                new RoundPlayer(this, game.playerAt(dealer), Wind.EAST),
                new RoundPlayer(this, game.playerAt(dealer.shift(1)), Wind.SOUTH),
                new RoundPlayer(this, game.playerAt(dealer.shift(2)), Wind.WEST),
                new RoundPlayer(this, game.playerAt(dealer.shift(3)), Wind.NORTH));
    }
    public void init(){
        var d1 = Dices.rollDices();
        var d2 = Dices.rollDices();
        players.forEach(player->{
            var side = Wind.EAST.from(player.getSeatWind());
            player.diceRolled(side, d1, d2);
        });
        wall = new Wall(this, d1, d2);
        for(int i = 0; i<3; i++){
            for(var wind: Wind.values()){
                for(int j = 0; j<4; j++){
                    playerAt(wind).draw(wall.takeTile());
                }
            }
        }
        for(Wind wind: Wind.values()){
            playerAt(wind).draw(wall.takeTile());
        }
    }
    private void start(){
        Wind turn = Wind.EAST;
        while(wall.hasDrawableTile()){
            playerAt(turn).draw(wall.takeTile());
            var action = playerAt(turn).moveTurnPhase(null);
        }

    }
    public Wind getRoundWind(){
        return roundWind;
    }
    public int getRoundCount(){
        return roundCount;
    }
    public int getStreakCount(){
        return streakCount;
    }
    List<RoundPlayer> players(){
        return players;
    }
    RoundPlayer playerAt(Wind seatWind){
        return players.get(seatWind.ordinal());
    }
    RoundPlayer dealer(){
        return playerAt(Wind.EAST);
    }
}
