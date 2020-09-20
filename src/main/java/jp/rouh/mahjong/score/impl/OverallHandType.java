package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;

/**
 * 面子の並べ替え構成に依存しない通常役のクラス。
 *
 * <p>面子の並べ替え構成に依存しない通常役は次の三種類に分類されます。
 * <ol><li><h2>統一役:面子構成に依存しない成立条件の役(七対子と複合可能)</h2>
 *         <ul><li>断么九</li>
 *             <li>混一色</li>
 *             <li>清一色</li>
 *             <li>混老頭</li></ul></li>
 *     <li><h2>複数刻子役:成立条件の必然で複数並べ替えパターンが発生し得ない役</h2>
 *         <ul><li>三槓子</li>
 *             <li>小三元</li></ul></li>
 *     <li><h2>単数刻子役:どの並べ替えパターンでも変化しない刻子が成立条件の役</h2>
 *         <ul><li>翻牌　白</li>
 *             <li>翻牌　發</li>
 *             <li>翻牌　中</li>
 *             <li>自風牌</li>
 *             <li>場風牌</li></ul></li></ol>
 *
 * @author Rouh
 * @version 1.0
 */
public enum OverallHandType implements HandType{
    NO_ORPHANS("断么九", 1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getOrphanCount()==0;
        }
    },
    HALF_SINGLE_COLOR("混一色", 2){
        @Override
        public boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && feature.getOrphanCount()>0 && feature.getSuitTypeKind()==1;
        }
    },
    CALLED_HALF_SINGLE_COLOR("混一色", 1){
        @Override
        public boolean define(HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && feature.getOrphanCount()>0 && feature.getSuitTypeKind()==1;
        }
    },
    FULL_SINGLE_COLOR("清一色", 6){
        @Override
        public boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && feature.getOrphanCount()==0 && feature.getSuitTypeKind()==1;
        }
    },
    CALLED_FULL_SINGLE_COLOR("清一色", 5){
        @Override
        public boolean define(HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && feature.getOrphanCount()==0 && feature.getSuitTypeKind()==1;
        }
    },
    THREE_QUADS("三槓子", 2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.getQuadCount()==3;
        }
    },
    SMALL_THREE("小三元", 2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getDragonCount()==8;
        }
    },
    HALF_TERMINALS("混老頭", 2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getHonorCount()>0
                    && feature.getOrphanCount()==14
                    && feature.getTileKind()<=7;
        }
    },
    DRAGON_WHITE("翻牌　白",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getDragonWhiteCount()==3;
        }
    },
    DRAGON_GREEN("翻牌　發",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getDragonGreenCount()==3;
        }
    },
    DRAGON_RED("翻牌　中",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getDragonRedCount()==3;
        }
    },
    SEAT_WIND("自風牌",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getSeatWindCount()==3;
        }
    },
    ROUND_WIND("場風牌",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getRoundWindCount()==3;
        }
    };
    private final String name;
    private final int point;
    OverallHandType(String name, int point){
        this.name = name;
        this.point = point;
    }

    /**
     * 役の成立条件を定義します。
     * @param feature 手牌の特徴量
     * @return true  役が成立する場合
     *         false 役が成立しない場合
     */
    abstract boolean define(HandFeature feature, WinningContext context);
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
