package jp.rouh.mahjong.score.type;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;

/**
 * 面子の並べ替え構成に依存しない役満のクラス。
 *
 * <p>面子の並べ替え構成に依存しない通常役は次の五種類に分類されます。
 * <ol><li><h2>統一役:面子構成に依存しない成立条件の役</h2>
 *         <ul><li>緑一色</li>
 *             <li>字一色</li></ul></li>
 *     <li><h2>特殊役:特殊な形のため並べ替えパターンが発生し得ない役</h2>
 *         <ul><li>国士無双</li></ul></li>
 *     <li><h2>複数刻子役:成立条件の必然で複数並べ替えパターンが発生し得ない役</h2>
 *         <ul><li>四槓子</li>
 *             <li>大三元</li>
 *             <li>小四喜</li>
 *             <li>大四喜</li>
 *             <li>清老頭</li></ul></li>
 *     <li><h2>複数順子役:成立条件の必然で複数並べ替えパターンが発生し得ない役</h2>
 *         <ul><li>九蓮宝燈</li></ul></li>
 *     <li><h2>面子役:高点法上別の解釈パターンより優先される役</h2>
 *         <ul><li>四暗刻</li></ul></li></ol>
 *
 * @author Rouh
 * @version 1.0
 */
public enum OverallLimitHandType implements HandType{
    THIRTEEN_ORPHANS("国士無双",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed() && feature.getTileKind()==13
                    && feature.getOrphanCount()==14 && feature.getWinningTileCount()==1;
        }
    },
    THIRTEEN_ORPHANS13("国士無双十三面",2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed() && feature.getTileKind()==13
                    && feature.getOrphanCount()==14 && feature.getWinningTileCount()==2;
        }
    },
    NINE_GATES("九蓮宝燈",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed() && context.getQuadCount()==0
                    && feature.getSuitTypeKind()==1 && feature.getTileKind()==9
                    && feature.getHonorCount()==0
                    && feature.getTerminalCount()==(feature.getLargestDuplicationCount()==4?7:6)
                    && feature.getWinningTileCount()%2==1;
        }
    },
    NINE_GATES9("純正九蓮宝燈", 2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed() && context.getQuadCount()==0
                    && feature.getSuitTypeKind()==1 && feature.getTileKind()==9
                    && feature.getHonorCount()==0
                    && feature.getTerminalCount()==(feature.getLargestDuplicationCount()==4?7:6)
                    && feature.getWinningTileCount()%2==0;
        }
    },
    FOUR_QUADS("四槓子",2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.getQuadCount()==4;
        }
    },
    BIG_THREE("大三元",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getDragonCount()==9;
        }
    },
    SMALL_WIND("小四喜",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getWindCount()==11;
        }
    },
    BIG_WIND("大四喜",2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getWindCount()==12;

        }
    },
    ALL_HONORS("字一色",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getHonorCount()==14;

        }
    },
    ALL_TERMINALS("清老頭",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getTerminalCount()==14;

        }
    },
    ALL_GREENS("緑一色",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return feature.getGreenTileCount()==14;
        }
    },
    FOUR_CONCEALED_TRIPLES("四暗刻",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && feature.getTileKind()==5
                    && feature.getLargestDuplicationCount()==3
                    && feature.getWinningTileCount()==3
                    && context.getWinningType().isSelfDraw();
        }
    },
    FOUR_CONCEALED_TRIPLES1("四暗刻単騎",2){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && feature.getTileKind()==5
                    && feature.getLargestDuplicationCount()==3
                    && feature.getWinningTileCount()==2;
        }
    };
    private final String name;
    private final int point;
    OverallLimitHandType(String name, int point){
        this.name = name;
        this.point = point;
    }
    abstract boolean define(HandFeature feature, WinningContext context);
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
