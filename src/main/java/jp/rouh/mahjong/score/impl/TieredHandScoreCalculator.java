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
    public HandScore calculate(WinningHand hand, WinningContext context) throws HandFormatException{
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
            var point = new StandardHandPointCalculator().calculate(formattedHand, context);
            var handScore = HandScore.of(point, formattedHandTypes, context.isDealer());
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
        try{
            return hand.format().parallelStream().anyMatch(formattedHand->
                    Stream.of(MeldHandType.values()).anyMatch(type->
                            type.define(formattedHand, feature, context)));
        }catch(HandFormatException e){
            return false;
        }
    }
}
