package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Tile;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 四面子一雀頭形の手牌を表すインターフェース。
 * @author Rouh
 * @version 1.0
 */
class MeldHand implements FormattedHand{
    private final Head head;
    private final List<Meld> melds;
    private final Wait wait;
    MeldHand(Head head, List<Meld> handMelds, List<Meld> openMelds, Wait wait){
        this.head = head;
        this.melds = new ArrayList<>();
        this.melds.addAll(handMelds);
        this.melds.addAll(openMelds);
        this.wait = wait;
    }

    @Override
    public boolean isMeldHand(){
        return true;
    }

    @Override
    public boolean isSevenPairsHand(){
        return false;
    }

    @Override
    public boolean isThirteenOrphansHand(){
        return false;
    }

    @Override
    public Head getHead(){
        return head;
    }

    @Override
    public List<Meld> getMelds(){
        return melds;
    }

    @Override
    public List<HandComponent> getComponents(){
        var components = new ArrayList<HandComponent>(melds);
        components.add(head);
        return components;
    }

    @Override
    public List<Tile> getTilesSorted(){
        return getComponents().stream()
                .map(HandComponent::getTilesSorted)
                .flatMap(List::stream)
                .sorted(Tile.comparator())
                .collect(toList());
    }

    @Override
    public List<Tile> getTilesTruncated(){
        var meldTiles = getMelds().stream()
                .map(Meld::getTilesTruncated)
                .flatMap(List::stream)
                .collect(toList());
        meldTiles.addAll(head.getTilesSorted());
        meldTiles.sort(Tile.comparator());
        return meldTiles;
    }

    @Override
    public Wait getWait(){
        return wait;
    }
}
