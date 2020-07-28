package jp.rouh.mahjong.score.scorer;

import jp.rouh.mahjong.hand.FormattedHand;
import jp.rouh.mahjong.hand.HandComponent;
import jp.rouh.mahjong.hand.Meld;
import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningType;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.util.OperableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

public enum CommonHandType implements HandType{
    READY("立直", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isReady() && !context.isDoubleReady();
        }
    },

    DOUBLE_READY("両立直", 2){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isDoubleReady();
        }
    },

    READY_AROUND_WIN("一発", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isReadyAroundWin();
        }
    },

    ALL_SELF_DRAW("門前清自摸和", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed() && context.getWinningType().isSelfDraw();
        }
    },

    LAST_TILE_DRAW("海底摸月", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isLastTileWin() && context.getWinningType()==WinningType.WALL_DRAW;
        }
    },

    LAST_TILE_GRAB("河底撈魚", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isLastTileWin() && context.getWinningType()==WinningType.RIVER_GRAB;
        }
    },

    QUAD_DRAW("嶺上開花", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.getWinningType()==WinningType.QUAD_DRAW;
        }
    },

    QUAD_GRAB("槍槓", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.getWinningType()==WinningType.QUAD_GRAB;
        }
    },

    SMALL_THREE("小三元", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getDragonCount()==8;
        }
    },

    DRAGON_WHITE("翻牌　白",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getDragonWhiteCount()==3;
        }
    },

    DRAGON_GREEN("翻牌　發",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getDragonGreenCount()==3;
        }
    },

    DRAGON_RED("翻牌　中",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getDragonRedCount()==3;
        }
    },

    SEAT_WIND("自風牌",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getSeatWindCount()==3;
        }
    },

    ROUND_WIND("場風牌",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getRoundWindCount()==3;
        }
    },

    NO_ORPHANS("断么九", 1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getOrphanCount()==0;
        }
    },

    NO_POINT("平和",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed() && hand.isMeldHand()
                    && hand.getHead().getHeadBasicPoint(context.getSeatWind(), context.getRoundWind())
                    + hand.getMelds().stream().mapToInt(Meld::getMeldBasicPoint).sum()==0;
        }
    },

    DUAL_STRAIGHTS("一盃口",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed() && hand.isMeldHand()
                    && new OperableList<>(hand.getMelds()).combinationSizeOf(2).stream()
                    .filter(melds->melds.stream().allMatch(Meld::isStraight)
                            && melds.stream().map(Meld::getFirst).mapToInt(Tile::tileNumber).count()==1).count()==1;
        }
    },

    DOUBLE_DUAL_STRAIGHTS("二盃口",3){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed() && hand.isMeldHand()
                    && hand.getMelds().stream().allMatch(Meld::isStraight)
                    && hand.getMelds().stream().map(Meld::getFirst)
                    .collect(groupingBy(Tile::tileNumber))
                    .values().stream().allMatch(melds->melds.size()==2);
        }
    },

    SEVEN_PAIRS("七対子", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isSevenPairsHand();
        }
    },

    @Concealed
    HALF_SINGLE_COLOR("混一色", 2){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getOrphanCount()>0 && feature.getSuitTypeKind()==1;
        }
    },

    @Called
    HALF_SINGLE_COLOR_CALLED("混一色", 1){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return HALF_SINGLE_COLOR.define(hand, feature, context);
        }
    },

    @Concealed
    FULL_SINGLE_COLOR("清一色", 6){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return feature.getOrphanCount()==0 && feature.getSuitTypeKind()==1;
        }
    },

    @Called
    FULL_SINGLE_COLOR_CALLED("清一色", 5){
        @Override
        public boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return FULL_SINGLE_COLOR.define(hand, feature, context);
        }
    },

    @Concealed
    THREE_COLOR_STRAIGHTS("三色同順",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->
                            melds.stream().allMatch(Meld::isStraight)
                                    && melds.stream().map(Meld::getTileType).distinct().count()==3
                                    && melds.stream().map(Meld::getFirst).mapToInt(Tile::suitNumber).distinct().count()==1);
        }
    },

    @Called
    THREE_COLOR_STRAIGHTS_CALLED("三色同順",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return THREE_COLOR_STRAIGHTS.define(hand, feature, context);
        }
    },

    @Concealed
    FULL_STRAIGHTS("一気通貫",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->melds.stream().map(Meld::getTileType).distinct().count()==1
                            && melds.stream().map(Meld::getTilesSorted).flatMap(List::stream)
                            .mapToInt(Tile::suitNumber).distinct().count()==9);
        }
    },

    @Called
    FULL_STRAIGHTS_CALLEd("一気通貫",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand() && FULL_STRAIGHTS.define(hand, feature, context);
        }
    },

    @Concealed
    HALF_TERMINAL_SETS("混全帯么九",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand()
                    && hand.getComponents().stream().allMatch(HandComponent::isOrphan)
                    && hand.getComponents().stream().anyMatch(HandComponent::isHonor);
        }
    },

    @Called
    HALF_TERMINAL_SETS_CALLED("混全帯么九",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return HALF_TERMINAL_SETS.define(hand, feature, context);
        }
    },

    @Concealed
    FULL_TERMINAL_SETS("純全帯么九",3){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand() && hand.getMelds().stream().allMatch(Meld::isTerminal);
        }
    },

    @Called
    FULL_TERMINAL_SETS_CALLED("純全帯么九",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return FULL_TERMINAL_SETS.define(hand, feature, context);
        }
    },

    THREE_COLOR_TRIPLES("三色同刻", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand()
                    && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->
                            melds.stream().noneMatch(Meld::isStraight)
                                    && melds.stream().noneMatch(Meld::isHonor)
                                    && melds.stream().map(Meld::getTileType).distinct().count()==3
                                    && melds.stream().map(Meld::getFirst)
                                    .mapToInt(Tile::suitNumber).distinct().count()==1);
        }
    },

    ALL_TRIPLES("対々和", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand() && hand.getMelds().stream().noneMatch(Meld::isStraight);
        }
    },

    THREE_CONCEALED_TRIPLES("三暗刻", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isMeldHand()
                    && hand.getMelds().stream().filter(not(Meld::isConcealed))
                    .filter(not(Meld::isStraight)).count()==3;
        }
    },

    THREE_QUADS("三槓子", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.getQuadCount()==3;
        }
    },

    ;
    private static final Set<CommonHandType> CALLED;
    private static final Set<CommonHandType> CONCEALED;
    static{
        try{
            var calledSet = new HashSet<CommonHandType>();
            var concealedSet = new HashSet<CommonHandType>();
            for(var value:values()){
                var calledAnnotation = CommonHandType.class.getField(value.name()).getAnnotation(Called.class);
                var concealedAnnotation = CommonHandType.class.getField(value.name()).getAnnotation(Concealed.class);
                if(calledAnnotation!=null) calledSet.add(value);
                if(concealedAnnotation!=null) concealedSet.add(value);
            }
            CALLED = calledSet;
            CONCEALED = concealedSet;
        }catch(NoSuchFieldException e){
            throw new AssertionError(e);
        }
    }
    private final String typeName;
    private final int typePoint;
    CommonHandType(String name, int point){
        this.typeName = name;
        this.typePoint = point;
    }

    boolean test(FormattedHand hand, HandFeature feature, WinningContext context){
        if(CONCEALED.contains(this) && !context.isConcealed()) return false;
        if(CALLED.contains(this) && context.isConcealed()) return false;
        return define(hand, feature, context);
    }

    abstract boolean define(FormattedHand hand, HandFeature feature, WinningContext context);

    @Override
    public String getName(){
        return typeName;
    }

    @Override
    public boolean isEpic(){
        return false;
    }

    @Override
    public int getTypePoint(){
        return typePoint;
    }
}
