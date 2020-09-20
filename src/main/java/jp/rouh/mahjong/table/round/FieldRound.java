package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.table.TableSpectator;
import jp.rouh.mahjong.table.round.result.RoundResult;
import jp.rouh.mahjong.tile.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FieldRound implements Round{
    private final Wind roundWind;
    private final int roundCount;
    private final int streakCount;
    private final int depositCount;
    private Map<Wind, RoundPlayer> players = new HashMap<>();
    private Wall wall;
    private int turnCount = 0;
    private int quadCount = 0;
    public FieldRound(Wind roundWind, int roundCount, int streakCount, int depositCount){
        this.roundWind = roundWind;
        this.roundCount = roundCount;
        this.streakCount = streakCount;
        this.depositCount = depositCount;
    }
    @Override
    public void join(Wind seatWind, RoundParticipant participant){
        if(players.containsKey(seatWind)){
            throw new IllegalStateException();
        }
        var roundPlayer = new RoundPlayer(participant, this, seatWind);
        players.put(seatWind, roundPlayer);
    }
    @Override
    public void addSpectator(TableSpectator spectator){
        //TODO: implements later
    }

    private void initialize(){
        if(!Stream.of(Wind.values()).allMatch(players::containsKey)){
            throw new IllegalStateException("参加者が全員揃っていません");
        }
        var dices = new DiceTwin();
        wall = new IndexedWall(dices.getDiceSum());
        for(var wind:Wind.values()){
            for(int i = 0; i<4; i++){
                playerAt(wind).draw(wall.takeTile());
            }
        }
        for(var wind:Wind.values()){
            playerAt(wind).draw(wall.takeTile());
        }

    }
    @Override
    public RoundResult start(){
        initialize();
        var turnWind = Wind.EAST;
        while(true){
            var turnPlayer = playerAt(turnWind);
            var turnAction = turnPlayer.moveTurnPhase();
            if(turnAction.isNineTilesDrawDeclaration()){
                return null;
            }
            if(turnAction.isSelfDrawWinDeclaration()){
                return null;
            }
            if(turnAction.isSelfQuadDeclaration()){
                var quadTile = turnAction.getSelectedTile();
                turnPlayer.makeSelfQuad(quadTile);
                for(var callerWind:turnWind.others()){
                    var callAction = playerAt(callerWind).moveSelfQuadCallPhase(quadTile);
                    if(callAction.isRiverGrabWinDeclaration()){
                        return null;
                    }
                }
                turnPlayer.draw(wall.takeQuadTile());
                continue;
            }
            if(turnAction.isAddQuadDeclaration()){
                var quadTile = turnAction.getSelectedTile();
                turnPlayer.makeAddQuad(quadTile);
                for(var callerWind:turnWind.others()){
                    var callAction = playerAt(callerWind).moveSelfQuadCallPhase(quadTile);
                    if(callAction.isRiverGrabWinDeclaration()){
                        return null;
                    }
                }
                turnPlayer.draw(wall.takeQuadTile());
                continue;
            }
            var discardedTile = turnAction.getSelectedTile();
            if(turnAction.isReadyDeclaration()){
                turnPlayer.readyDiscard(discardedTile);
            }else{
                turnPlayer.discard(discardedTile);
            }
            for(var callerWind:turnWind.others()){
                var callAction = playerAt(callerWind).moveCallPhase(discardedTile, turnWind.from(callerWind));
                if(callAction.isRiverGrabWinDeclaration()){
                    return null;
                }
                if(callAction.isMeldCallDeclaration()){
                    var caller = playerAt(callerWind);
                    if(callAction.isStraightCallDeclaration()){
                        caller.callStraight(discardedTile, callAction.getSelectedTiles());
                    }
                    if(callAction.isTripleCallDeclaration()){
                        caller.callTriple(discardedTile, callAction.getSelectedTiles(), turnWind.from(callerWind));
                    }
                    if(callAction.isQuadCallDeclaration()){
                        caller.callQuad(discardedTile, callAction.getSelectedTiles(), turnWind.from(callerWind));
                    }
                    turnWind = callerWind;
                    continue;
                }
                // all pass
                if(!wall.hasDrawableTile()){
                    return null;
                }
                turnWind = turnWind.next();
                playerAt(turnWind).draw(wall.takeTile());
            }
        }
    }

    private RoundPlayer playerAt(Wind seatWind){
        return players.get(seatWind);
    }
    Wind getRoundWind(){
        return roundWind;
    }
    int getRoundCount(){
        return roundCount;
    }
    int getStreakCount(){
        return streakCount;
    }
    int getDepositCount(){
        return depositCount;
    }
    int getTurnCount(){
        return turnCount;
    }
    int getTotalQuadCount(){
        return quadCount;
    }
    int getRemainingTurnCount(){
        return wall.getDrawableTileCount();
    }
    boolean isFirstAround(){
        return getTurnCount()<4;
    }
    boolean isLastAround(){
        return getRemainingTurnCount()<4;
    }
    boolean isLastTurn(){
        return getRemainingTurnCount()==0;
    }
    List<Tile> getUpperPrisedTiles(){
        return wall.getUpperIndicators().stream()
                .map(Tile::indicates).collect(toList());
    }
    List<Tile> getLowerPrisedTiles(){
        return wall.getLowerIndicators().stream()
                .map(Tile::indicates).collect(toList());
    }
}
