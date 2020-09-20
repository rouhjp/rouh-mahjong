package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.score.*;
import jp.rouh.mahjong.score.impl.TieredHandScoreCalculator;
import jp.rouh.mahjong.table.TableStrategyAdaptor;
import jp.rouh.mahjong.table.action.CallAction;
import jp.rouh.mahjong.table.action.CallPhaseContextContainer;
import jp.rouh.mahjong.table.action.TurnAction;
import jp.rouh.mahjong.table.action.TurnPhaseContextContainer;
import jp.rouh.mahjong.tile.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

class RoundPlayer extends TableStrategyAdaptor{
    private final HandScoreCalculator calculator = new TieredHandScoreCalculator();
    private final RoundParticipant participant;
    private final FieldRound round;
    private final Wind seatWind;
    private final List<Tile> discardPile = new ArrayList<>(24);
    private final List<Meld> openMelds = new ArrayList<>(4);
    private final List<Tile> handTiles = new ArrayList<>(13);
    private final Set<Tile> callShiftTiles = new HashSet<>(2);
    private Tile drawnTile;
    private int quadCount = 0;
    private boolean concealed = true;
    private ReadyCache readyCache = null;
    private HandCache handCache = null;
    private TurnStatus status = TurnStatus.AFTER_PASS;
    private static class ReadyCache{
        private final Set<Tile> quadTiles;
        private final int readyTurnCount;
        private ReadyCache(List<Tile> handTiles, int readyTurnCount){
            this.quadTiles = HandTiles.readyQuadTilesOf(handTiles);
            this.readyTurnCount = readyTurnCount;
        }
    }
    private static class HandCache{
        private final Set<Tile> winningTiles;
        private final boolean thirteenOrphansHandReady;
        private final boolean sacredDiscardCoolDown;
        private boolean aroundSacredDiscardCoolDown;
        private HandCache(List<Tile> handTiles, List<Tile> discardPile){
            this.winningTiles = HandTiles.winningTilesOf(handTiles);
            this.thirteenOrphansHandReady = HandTiles.isThirteenOrphansHandReady(handTiles);
            this.sacredDiscardCoolDown = winningTiles.stream().anyMatch(tile->
                    discardPile.stream().anyMatch(tile::equalsIgnoreRed));
        }
    }
    private enum TurnStatus{
        AFTER_PASS,
        AFTER_CALL,
        AFTER_QUAD
    }
    RoundPlayer(RoundParticipant participant, FieldRound round, Wind seatWind){
        super(participant);
        this.participant = participant;
        this.round = round;
        this.seatWind = seatWind;
    }
    public Wind getSeatWind(){
        return seatWind;
    }
    void draw(Tile tile){
        drawnTile = tile;
//        round.tileDrawn(seatWind, tile);
    }

    void makeAddQuad(Tile tile){
        for(var meld:openMelds){
            if(meld.isTriple() && meld.getFirst().equalsIgnoreRed(tile)){
                int index = openMelds.indexOf(meld);
                var quad = Meld.makeAddQuad(meld, tile);
                openMelds.remove(meld);
                openMelds.add(index, quad);
                return;
            }
        }
        throw new IllegalArgumentException("加槓に必要な刻子が見つかりません");
    }
    void makeSelfQuad(Tile tile){
        var targets = handTiles.stream()
                .filter(tile::equalsIgnoreRed)
                .collect(toList());
        if(targets.size()!=4){
            throw new IllegalArgumentException("暗槓に必要な構成牌が見つかりません");
        }
        targets.forEach(handTiles::remove);
        var quad = Meld.makeSelfQuad(targets);
        openMelds.add(quad);
    }
    void readyDiscard(Tile tile){


    }
    void discard(Tile tile){

        callShiftTiles.clear();
        handTiles.remove(tile);
        handTiles.add(drawnTile);
        drawnTile = null;



        status = TurnStatus.AFTER_PASS;
    }
    void notifyTurnFinished(){
//        if(round.getTurnCount()==readyTurnCount){
//            participant.applyScore(-1000);
//            //TODO:
//        }
    }

    void callStraight(Tile tile, List<Tile> base){
        concealed = false;
        base.forEach(handTiles::remove);
        openMelds.add(Meld.callStraight(base, tile));
        status = TurnStatus.AFTER_CALL;
        callShiftTiles.addAll(HandTiles.waitingTilesOf(base));

//        round.leftTiltMeldAdded();

    }
    void callTriple(Tile tile, List<Tile> base, Side side){
        concealed = false;
        base.forEach(handTiles::remove);
        openMelds.add(Meld.callTriple(base, tile, side));
        status = TurnStatus.AFTER_CALL;
        callShiftTiles.addAll(HandTiles.waitingTilesOf(base));
    }
    void callQuad(Tile tile, List<Tile> base, Side side){
        concealed = false;
        base.forEach(handTiles::remove);
        openMelds.add(Meld.callQuad(base, tile, side));
        status = TurnStatus.AFTER_QUAD;
    }

    private WinningContext getWinningContext(){
        return null;
    }
    private WinningHand getWinningHand(){
        return null;
    }

    // AS TURN PLAYER

    TurnAction moveTurnPhase(){
        if(status==TurnStatus.AFTER_CALL)
            return askTurnAction(TurnPhaseContextContainer.of(handTiles, callShiftTiles));
        return askTurnAction(TurnPhaseContextContainer.of(handTiles,
                readyTiles(), addQuadTiles(), selfQuadTiles(),
                canDeclareSelfDrawWin(), canDeclareNineTilesDraw()));
    }

    private boolean canDeclareSelfDrawWin(){
        if(isReady()) return handCache.winningTiles.contains(drawnTile);
        return HandTiles.isCompleted(handTiles, drawnTile)
                && calculator.checkIfScorePresent(getWinningHand(), getWinningContext());
    }
    private boolean canDeclareNineTilesDraw(){
        if(!round.isFirstAround()) return false;
        return HandTiles.isNineTiles(handTiles, drawnTile);
    }
    private Set<Tile> readyTiles(){
        if(isReady()) return emptySet();
        if(!concealed) return emptySet();
        if(round.isLastAround()) return emptySet();
        if(participant.getScore()<1000) return emptySet();
        return HandTiles.readyTilesOf(handTiles, drawnTile);
    }
    private Set<Tile> addQuadTiles(){
        if(isReady()) return emptySet();
        if(round.isLastTurn()) return emptySet();
        if(round.getTotalQuadCount()==4) return emptySet();
        if(openMelds.size()==4) return emptySet();
        return HandTiles.addQuadTilesOf(handTiles, drawnTile, openMelds);
    }
    private Set<Tile> selfQuadTiles(){
        if(isReady()) return readyCache.quadTiles;
        if(round.isLastTurn()) return emptySet();
        if(round.getTotalQuadCount()==4) return emptySet();
        if(openMelds.size()==4) return emptySet();
        return HandTiles.selfQuadTilesOf(handTiles, drawnTile);
    }

    // AS CALL PLAYER

    CallAction moveCallPhase(Tile discarded, Side side){
        var win = canDeclareRiverGrabWin(discarded);
        var quadBase = List.copyOf(quadBases(discarded)).get(0);
        var tripleBases = tripleBases(discarded);
        var straightBases = straightBases(discarded, side);
        if(!win && quadBase.isEmpty() && tripleBases.isEmpty() && straightBases.isEmpty()){
            // pass automatically
            return CallAction.ofPass();
        }
        return askCallAction(CallPhaseContextContainer
                .of(handTiles, discarded, straightBases, tripleBases, quadBase, win));
    }

    CallAction moveAddQuadCallPhase(Tile addQuad){
        if(canAddQuadGrabWin(addQuad)){
            return askCallAction(CallPhaseContextContainer.ofWinning(handTiles, addQuad));
        }
        // pass automatically
        return CallAction.ofPass();
    }

    CallAction moveSelfQuadCallPhase(Tile selfQuad){
        if(canSelfQuadGrabWin(selfQuad)){
            return askCallAction(CallPhaseContextContainer.ofWinning(handTiles, selfQuad));
        }
        // pass automatically
        return CallAction.ofPass();
    }

    private boolean canDeclareRiverGrabWin(Tile discardedTile){
        //TODO cache check
        if(handCache.sacredDiscardCoolDown) return false;
        if(handCache.aroundSacredDiscardCoolDown) return false;
        if(!handCache.winningTiles.contains(discardedTile)) return false;
        return isReady() || HandTiles.isCompleted(handTiles, discardedTile)
                && calculator.checkIfScorePresent(getWinningHand(), getWinningContext());
    }
    private boolean canSelfQuadGrabWin(Tile selfQuadTile){
        if(handCache.thirteenOrphansHandReady) return false;
        return handCache.winningTiles.contains(selfQuadTile);
    }
    private boolean canAddQuadGrabWin(Tile addQuadTile){
        if(handCache.sacredDiscardCoolDown) return false;
        if(handCache.aroundSacredDiscardCoolDown) return false;
        return handCache.winningTiles.contains(addQuadTile);
    }
    private Set<List<Tile>> quadBases(Tile discardedTile){
        if(isReady()) return emptySet();
        if(round.isLastAround()) return emptySet();
        if(round.getTotalQuadCount()==4) return emptySet();
        if(openMelds.size()==4) return emptySet();
        return HandTiles.quadBasesOf(handTiles, discardedTile);
    }
    private Set<List<Tile>> tripleBases(Tile discardedTile){
        if(isReady()) return emptySet();
        if(round.isLastAround()) return emptySet();
        if(openMelds.size()==4) return emptySet();
        return HandTiles.tripleBasesOf(handTiles, discardedTile);
    }
    private Set<List<Tile>> straightBases(Tile discardedTile, Side side){
        if(side!=Side.LEFT) return emptySet();
        if(isReady()) return emptySet();
        if(round.isLastAround()) return emptySet();
        if(openMelds.size()==4) return emptySet();
        return HandTiles.straightBasesOf(handTiles, discardedTile);
    }

    // AS WINNING CONTEXT

    public boolean isConcealed(){
        return concealed;
    }
    public boolean isReady(){
        return readyCache!=null;
    }
    public boolean isFirstAroundReady(){
        return isReady() && readyCache.readyTurnCount<=4;
    }
    public boolean isReadyAround(){
        return isReady() && round.getTurnCount() - readyCache.readyTurnCount<=4;
    }
    public int getQuadCount(){
        return quadCount;
    }
}
