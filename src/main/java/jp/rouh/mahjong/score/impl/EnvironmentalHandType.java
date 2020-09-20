package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningType;

/**
 * 手牌に依存しない通常役クラス。
 *
 * <p>手牌の形に一切影響を受けず, 和了時の状況によって成立・不成立が決定するクラス。
 * 該当する役は以下の通り。
 * <ul><li>立直</li>
 *     <li>両立直</li>
 *     <li>一発</li>
 *     <li>門前清自摸和</li>
 *     <li>海底摸月</li>
 *     <li>河底撈魚</li>
 *     <li>嶺上開花</li>
 *     <li>槍槓</li></ul>
 *
 * @author Rouh
 * @version 1.0
 */
public enum EnvironmentalHandType implements HandType{
    READY("立直", 1){
        @Override
        boolean define(WinningContext context){
            return context.isReady();
        }
    },
    DOUBLE_READY("両立直", 2){
        @Override
        boolean define(WinningContext context){
            return context.isFirstAroundReady();
        }
    },
    ONE_SHOT("一発", 1){
        @Override
        boolean define(WinningContext context){
            return context.isReadyAroundWin();
        }
    },
    SELF_PICK("門前清自摸和", 1){
        @Override
        boolean define(WinningContext context){
            return context.isConcealed() && context.getWinningType().isSelfDraw();
        }
    },
    LAST_TILE_DRAW("海底摸月", 1){
        @Override
        boolean define(WinningContext context){
            //嶺上開花とは複合しない。
            return context.isLastTileWin() && context.getWinningType()==WinningType.WALL_DRAW;
        }
    },
    LAST_TILE_GRAB("河底撈魚", 1){
        @Override
        boolean define(WinningContext context){
            return context.isLastTileWin() && context.getWinningType()==WinningType.RIVER_GRAB;
        }
    },
    QUAD_DRAW("嶺上開花", 1){
        @Override
        boolean define(WinningContext context){
            return context.getWinningType()==WinningType.QUAD_DRAW;
        }
    },
    QUAD_GRAB("槍槓", 1){
        @Override
        boolean define(WinningContext context){
            return context.getWinningType()==WinningType.QUAD_GRAB;
        }
    };
    private final String name;
    private final int point;
    EnvironmentalHandType(String name, int point){
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
        return false;
    }
    @Override
    public int getDoubles(){
        return point;
    }
}
