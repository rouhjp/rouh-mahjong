package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 面子{@link Meld}インターフェースの実装クラス。
 * @author Rouh
 * @version 1.0
 */
class BaseMeld implements Meld{
    private final List<Tile> sorted;
    private final List<Tile> formed;
    private final Side source;
    private final boolean concealed;
    private final boolean added;
    BaseMeld(List<Tile> tiles, boolean concealed){
        this.sorted = tiles.stream()
                .sorted(Tile.comparator()).collect(toList());
        this.formed = sorted;
        this.source = Side.SELF;
        this.concealed = concealed;
        this.added = false;
    }

    BaseMeld(List<Tile> base, Tile called, Side source){
        var formed = new ArrayList<>(base);
        int index;
        switch(source){
            case LEFT: index = 0; break;
            case ACROSS: index = 1; break;
            case RIGHT: index = base.size(); break;
            default: throw new IllegalArgumentException();
        }
        formed.add(index, called);
        this.formed = formed;
        this.sorted = formed.stream()
                .sorted(Tile.comparator()).collect(toList());
        this.source = source;
        this.concealed = false;
        this.added = false;
    }

    BaseMeld(Meld triple, Tile added){
        var formed = new ArrayList<>(triple.getTilesFormed());
        formed.add(added);
        this.formed = formed;
        this.sorted = formed.stream()
                .sorted(Tile.comparator()).collect(toList());
        this.source = triple.getSourceSide();
        this.concealed = false;
        this.added = true;
    }

    @Override
    public List<Tile> getTilesFormed(){
        return formed;
    }

    @Override
    public List<Tile> getTilesSorted(){
        return sorted;
    }

    @Override
    public List<Tile> getTilesTruncated(){
        return getTilesSorted().subList(0, 3);
    }

    @Override
    public Side getSourceSide(){
        return source;
    }

    @Override
    public boolean isStraight(){
        return getTilesSorted().size()==3 && !getLast().equalsIgnoreRed(getLast());
    }

    @Override
    public boolean isTriple(){
        return getTilesSorted().size()==3 && getFirst().equalsIgnoreRed(getLast());
    }

    @Override
    public boolean isQuad(){
        return getTilesSorted().size()==4;
    }

    @Override
    public boolean isAddQuad(){
        return added;
    }

    @Override
    public boolean isSelfQuad(){
        return isQuad() && isConcealed();
    }

    @Override
    public boolean isConcealed(){
        return concealed;
    }

    @Override
    public int getMeldBasicPoint(){
        if(isStraight()) return 0;
        return 2*(isQuad()?4:1)*(isConcealed()?2:1)*(isTerminal()?2:1);
    }
}
