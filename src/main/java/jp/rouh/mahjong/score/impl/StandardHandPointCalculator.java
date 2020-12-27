package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.FormattedHand;
import jp.rouh.mahjong.score.FormattedHandPointCalculator;
import jp.rouh.mahjong.score.Meld;
import jp.rouh.mahjong.score.WinningContext;

public class StandardHandPointCalculator implements FormattedHandPointCalculator{

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
    @Override
    public int calculate(FormattedHand hand, WinningContext context){
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
