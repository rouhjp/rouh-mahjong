package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningType;

/**
 * 手牌に依存しない役満クラス。
 *
 * <p>手牌の形に一切影響を受けず, 和了時の状況によって成立・不成立が決定するクラス。
 * 該当する役は以下の通り。
 * <ul><li>天和</li>
 *     <li>地和</li></ul>
 *
 * @author Rouh
 * @version 1.0
 */
public enum EnvironmentalLimitHandType implements HandType{
    BLESSING_OF_HEAVEN("天和",1){
        @Override
        boolean define(WinningContext context){
            return context.isFirstAroundWin() && context.isDealer()
                    && context.getWinningType()==WinningType.WALL_DRAW;
        }
    },
    BLESSING_OF_EARTH("地和",1){
        @Override
        boolean define(WinningContext context){
            return context.isFirstAroundWin() && !context.isDealer()
                    && context.getWinningType()==WinningType.WALL_DRAW;
        }
    };
    private final String name;
    private final int point;
    EnvironmentalLimitHandType(String name, int point){
        this.name = name;
        this.point = point;
    }

    /**
     * 役の成立条件を定義します。
     * @param context 勝利状況
     * @return true  役が成立する場合
     *         false 役が成立しない場合
     */
    abstract boolean define(WinningContext context);
    @Override
    public String getName(){
        return name;
    }
    @Override
    public boolean isLimit(){
        return true;
    }
    @Override
    public int getDoubles(){
        return point*13;
    }
}
