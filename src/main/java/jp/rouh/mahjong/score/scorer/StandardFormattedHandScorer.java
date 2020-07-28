package jp.rouh.mahjong.score.scorer;

import jp.rouh.mahjong.hand.FormattedHand;
import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.score.*;

import java.util.ArrayList;
import java.util.List;

public class StandardFormattedHandScorer extends ParallelHandScorer{
    @Override
    public HandScore calculate(FormattedHand hand, WinningContext context){
        var feature = new HandFeature(hand, context);
        var handTypes = new ArrayList<HandType>();
        //EPIC HAND TYPES
        for(var epicHandType: EpicHandType.values()){
            if(epicHandType.test(feature, context)){
                handTypes.add(epicHandType);
            }
        }
        if(!handTypes.isEmpty()){
            return HandScore.ofEpic(handTypes, context.isDealer());
        }
        //BASE HAND TYPES
        for(var handType: CommonHandType.values()){
            if(handType.test(hand, feature, context)){
                handTypes.add(handType);
            }
        }
        //PRISED TILES
        var openPrisedTileCount = feature.getOpenPrisedTileCount();
        if(openPrisedTileCount>0){
            handTypes.add(PrisedTile.of(openPrisedTileCount));
        }
        if(context.isReady()){
            var hiddenPrisedTileCount = feature.getHiddenPrisedTileCount();
            if(hiddenPrisedTileCount>0){
                handTypes.add(PrisedTile.ofHidden(hiddenPrisedTileCount));
            }
        }
        var redPrisedTileCount = feature.getRedPrisedTileCount();
        if(redPrisedTileCount>0){
            handTypes.add(PrisedTile.ofRedTile(redPrisedTileCount));
        }
        return HandScore.of(basicPointOf(hand, handTypes, context), handTypes, context.isDealer());
    }


    private int basicPointOf(FormattedHand hand, List<HandType> types, WinningContext context){
        //七対子形 固定25符
        if(types.contains(CommonHandType.SEVEN_PAIRS)){
            return 25;
        }
        //平和ツモ 固定20符
        if(types.contains(CommonHandType.NO_POINT)
                && context.getWinningType().isSelfDraw()){
            return 20;
        }
        //面子・雀頭・待ち符
        var handBasicPoint = 0;
        handBasicPoint += hand.getWait().getWaitBasicPoint();
        handBasicPoint += hand.getHead().getHeadBasicPoint(context.getSeatWind(), context.getRoundWind());
        handBasicPoint += hand.getMelds().stream().mapToInt(Meld::getMeldBasicPoint).sum();
        //食い平和形 固定30符
        if(handBasicPoint==0 && !context.isConcealed()){
            return 30;
        }
        var winningBasicPoint = 0;
        if(context.getWinningType().isSelfDraw()){
            //ツモ符
            winningBasicPoint += 2;
        }else if(context.isConcealed()){
            //門前加符
            winningBasicPoint += 10;
        }
        return Scores.ceilToTen(20 + handBasicPoint + winningBasicPoint);
    }

}
