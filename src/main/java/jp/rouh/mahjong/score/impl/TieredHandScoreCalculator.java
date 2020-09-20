package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.FormattedHand;
import jp.rouh.mahjong.score.Meld;
import jp.rouh.mahjong.score.*;

import java.util.ArrayList;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * 手牌点数計算機能の実装クラス。
 *
 * @see EnvironmentalHandType
 * @see EnvironmentalLimitHandType
 * @see OverallHandType
 * @see OverallLimitHandType
 * @see MeldHandType
 * @see HandFeature
 * @see HandScoreCalculator
 * @author Rouh
 * @version 1.0
 */
public class TieredHandScoreCalculator implements HandScoreCalculator{

    /**
     * {@inheritDoc}
     * <p>手牌の並べ替えに関わらない役の判定を先に実施し, 共通化することで計算の重複を抑えます。
     */
    @Override
    public HandScore calculate(WinningHand hand, WinningContext context){
        var handTypes = new ArrayList<HandType>();
        //役満の判定
        for(var handType: EnvironmentalLimitHandType.values()){
            if(handType.define(context)){
                handTypes.add(handType);
            }
        }
        var feature = new HandFeature(hand, context);
        for(var handType: OverallLimitHandType.values()){
            if(handType.define(feature, context)){
                handTypes.add(handType);
            }
        }
        if(!handTypes.isEmpty()){
            return HandScore.ofLimit(handTypes, context.isDealer());
        }
        //通常役の判定
        for(var handType: EnvironmentalHandType.values()){
            if(handType.define(context)){
                handTypes.add(handType);
            }
        }
        for(var handType: OverallHandType.values()){
            if(handType.define(feature, context)){
                handTypes.add(handType);
            }
        }
        //ドラの判定
        var prisedTileHandTypes = new ArrayList<HandType>();
        if(feature.getOpenPrisedTileCount()>0){
            prisedTileHandTypes.add(PrisedTileHandType.of(feature.getOpenPrisedTileCount()));
        }
        if(context.isReady() && feature.getHiddenPrisedTileCount()>0){
            prisedTileHandTypes.add(PrisedTileHandType.ofHidden(feature.getHiddenPrisedTileCount()));
        }
        if(feature.getRedPrisedTileCount()>0){
            prisedTileHandTypes.add(PrisedTileHandType.ofRedTile(feature.getRedPrisedTileCount()));
        }
        var handScores = new ArrayList<HandScore>();
        for(var formattedHand:hand.format()){
            var formattedHandTypes = new ArrayList<>(handTypes);
            for(var handType: MeldHandType.values()){
                if(handType.define(formattedHand, feature, context)){
                    formattedHandTypes.add(handType);
                }
            }
            if(!formattedHandTypes.isEmpty()){
                formattedHandTypes.addAll(prisedTileHandTypes);
            }
            var basicPoint = pointOf(formattedHand, context);
            var handScore = HandScore.of(basicPoint, formattedHandTypes, context.isDealer());
            handScores.add(handScore);
        }
        return handScores.stream().max(comparing(HandScore::getBaseScore)).orElseThrow();
    }

    /**
     * {@inheritDoc}
     * <p>役が一つでも確定した場合, 計算を切り上げて結果を返します。
     */
    @Override
    public boolean checkIfScorePresent(WinningHand hand, WinningContext context){
        if(Stream.of(EnvironmentalHandType.values()).anyMatch(type->type.define(context))) return true;
        if(Stream.of(EnvironmentalLimitHandType.values()).anyMatch(type->type.define(context))) return true;
        var feature = new HandFeature(hand, context);
        if(Stream.of(OverallHandType.values()).anyMatch(type->type.define(feature, context))) return true;
        if(Stream.of(OverallLimitHandType.values()).anyMatch(type->type.define(feature, context))) return true;
        return hand.format().parallelStream().anyMatch(formattedHand->
                Stream.of(MeldHandType.values()).anyMatch(type->type.define(formattedHand, feature, context)));
    }

    /**
     * 手牌の符を計算します。
     * @param hand 整形済み手牌
     * @param context 勝利コンテキスト
     * @return 20 国士無双形の場合(ダミー値)
     *         20 平和ツモの場合
     *         25 七対子の場合
     *         30 食い平和形の場合
     *         30..110 通常の計算に則った値
     */
    private int pointOf(FormattedHand hand, WinningContext context){
        //国士無双形 20符(計算には用いない)
        if(hand.isThirteenOrphansHand()) return 20;
        //七対子形 25符
        if(hand.isSevenPairsHand()) return 25;
        //待ち符
        int waitPoint = hand.getWait().getWaitBasicPoint();
        //雀頭符
        int headPoint = hand.getHead().getHeadBasicPoint(context.getSeatWind(), context.getRoundWind());
        //面子符
        int meldPoint = hand.getMelds().stream().mapToInt(Meld::getMeldBasicPoint).sum();
        int handPoint = waitPoint + headPoint + meldPoint;
        //ツモ符 (和了がツモの場合に付与する　ただし自摸八計算上、平和ツモは例外的に平和(20符)が優先される)
        boolean noPoint = context.isConcealed() && handPoint==0;
        int selfDrawPoint = context.getWinningType().isSelfDraw() && !noPoint? 2:0;
        //門前加符 (門前ロンの場合に付与)
        int concealedRiverGrabPoint = context.isConcealed() && !context.getWinningType().isSelfDraw()? 10:0;
        //平和加符 (食い平和形(ロンであれば本来20符)の場合に付与)
        int noPointCompensation = handPoint==0 && !context.isConcealed()? 10:0;
        //副底
        int leastPoint = 20;
        int totalPoint = leastPoint + handPoint + selfDrawPoint + concealedRiverGrabPoint + noPointCompensation;
        return (int)Math.ceil(totalPoint/10d)*10;
    }
}
