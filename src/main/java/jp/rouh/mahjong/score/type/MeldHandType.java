package jp.rouh.mahjong.score.type;

import jp.rouh.mahjong.score.FormattedHand;
import jp.rouh.mahjong.score.HandComponent;
import jp.rouh.mahjong.score.Meld;
import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.util.OperableList;

import java.util.List;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

/**
 * 面子の並べ替え構成に依存する通常役のクラス。
 *
 * <p>面子の並べ替え構成に依存する通常役は以下の通り。
 * <ul><li>七対子</li>
 *     <li>対々和</li>
 *     <li>三暗刻</li>
 *     <li>平和</li>
 *     <li>混全帯么九</li>
 *     <li>純全帯么九</li>
 *     <li>一気通貫</li>
 *     <li>三色同順</li>
 *     <li>三色同刻</li>
 *     <li>一盃口</li>
 *     <li>二盃口</li></ul>
 *
 * @author Rouh
 * @version 1.0
 */
public enum MeldHandType implements HandType{
    SEVEN_PAIRS("七対子", 2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return hand.isSevenPairsHand();
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
    NO_POINT("平和",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed() && hand.isMeldHand()
                    && hand.getHead().getHeadBasicPoint(context.getSeatWind(), context.getRoundWind())
                    + hand.getMelds().stream().mapToInt(Meld::getMeldBasicPoint).sum()==0;
        }
    },
    HALF_TERMINAL_SETS("混全帯么九",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && hand.isMeldHand()
                    && hand.getComponents().stream().allMatch(HandComponent::isOrphan)
                    && hand.getComponents().stream().anyMatch(HandComponent::isHonor);
        }
    },
    CALLED_HALF_TERMINAL_SETS("混全帯么九",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && hand.isMeldHand()
                    && hand.getComponents().stream().allMatch(HandComponent::isOrphan)
                    && hand.getComponents().stream().anyMatch(HandComponent::isHonor);
        }
    },
    FULL_TERMINAL_SETS("純全帯么九",3){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && hand.isMeldHand() && hand.getMelds().stream().allMatch(Meld::isTerminal);
        }
    },
    CALLED_FULL_TERMINAL_SETS("純全帯么九",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && hand.isMeldHand() && hand.getMelds().stream().allMatch(Meld::isTerminal);
        }
    },
    FULL_STRAIGHTS("一気通貫",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->melds.stream().map(Meld::getTileType).distinct().count()==1
                            && melds.stream().map(Meld::getTilesSorted).flatMap(List::stream)
                            .mapToInt(Tile::suitNumber).distinct().count()==9);
        }
    },
    CALLED_FULL_STRAIGHTS("一気通貫",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->melds.stream().map(Meld::getTileType).distinct().count()==1
                            && melds.stream().map(Meld::getTilesSorted).flatMap(List::stream)
                            .mapToInt(Tile::suitNumber).distinct().count()==9);
        }
    },
    THREE_COLOR_STRAIGHTS("三色同順",2){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return context.isConcealed()
                    && hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->
                            melds.stream().allMatch(Meld::isStraight)
                                    && melds.stream().map(Meld::getTileType).distinct().count()==3
                                    && melds.stream().map(Meld::getFirst).mapToInt(Tile::suitNumber).distinct().count()==1);
        }
    },
    CALLED_THREE_COLOR_STRAIGHTS("三色同順",1){
        @Override
        boolean define(FormattedHand hand, HandFeature feature, WinningContext context){
            return !context.isConcealed()
                    && hand.isMeldHand() && new OperableList<>(hand.getMelds()).combinationSizeOf(3)
                    .stream().anyMatch(melds->
                            melds.stream().allMatch(Meld::isStraight)
                                    && melds.stream().map(Meld::getTileType).distinct().count()==3
                                    && melds.stream().map(Meld::getFirst).mapToInt(Tile::suitNumber).distinct().count()==1);
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
    };
    private final String name;
    private final int point;
    MeldHandType(String name, int point){
        this.name = name;
        this.point = point;
    }
    abstract boolean define(FormattedHand hand, HandFeature feature, WinningContext context);
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
