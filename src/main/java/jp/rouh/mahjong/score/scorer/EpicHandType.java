package jp.rouh.mahjong.score.scorer;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningType;

public enum EpicHandType implements HandType{
    BLESSING_OF_HEAVEN("天和",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isFirstAroundWin() && context.isDealer()
                    && context.getWinningType()==WinningType.WALL_DRAW;
        }
    },

    BLESSING_OF_EARTH("地和",1){
        @Override
        boolean define(HandFeature feature, WinningContext context){
            return context.isFirstAroundWin() && !context.isDealer()
                    && context.getWinningType()==WinningType.WALL_DRAW;
        }
    },

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
    EpicHandType(String name, int point){
        this.name = name;
        this.point = point;
    }

    boolean test(HandFeature feature, WinningContext context){
        return define(feature, context);
    }

    abstract boolean define(HandFeature feature, WinningContext context);

    @Override
    public String getName(){
        return null;
    }
    @Override
    public boolean isEpic(){
        return false;
    }
    @Override
    public int getTypePoint(){
        return 0;
    }
}
