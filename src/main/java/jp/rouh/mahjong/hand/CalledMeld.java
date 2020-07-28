package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * 副露した面子を表すインターフェース。
 *
 * <p>このクラスは面子{@link Meld}インターフェースの実装です。
 * 暗槓はこのクラスではなく{@link SelfQuad}クラスにて表現します。
 * また加槓はこのクラスを拡張した{@link AddQuad}クラスにて表現します。
 *
 * @author Rouh
 * @version 1.0
 */
class CalledMeld extends HandMeld{
    private final Tile calledTile;
    private final Side source;
    CalledMeld(List<Tile> allTiles, Tile calledTile, Side source){
        super(allTiles, false);
        this.calledTile = calledTile;
        this.source= source;
    }

    Tile getCalledTile(){
        return calledTile;
    }

    private int formIndex(){
        switch(source){
            case LEFT: return 0;
            case ACROSS: return 1;
            case RIGHT: return getTilesSorted().size() - 1;
            default: throw new IllegalStateException();
        }
    }

    @Override
    public List<Tile> getTilesFormed(){
        var formed = new ArrayList<>(getTilesSorted());
        formed.remove(calledTile);
        formed.add(formIndex(), calledTile);
        return formed;
    }

    @Override
    public Side getSourceSide(){
        return source;
    }

    @Override
    public boolean isCalled(){
        return true;
    }

    @Override
    public boolean isExposed(){
        return true;
    }
}
