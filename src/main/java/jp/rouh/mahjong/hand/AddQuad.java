package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 加槓を表すクラス。
 *
 * <p>このクラスは面子{@link Meld}インターフェースの実装で,
 * {@link CalledMeld}クラスのサブクラスです。
 *
 * @author Rouh
 * @version 1.0
 */
class AddQuad extends CalledMeld{
    private final Tile added;
    AddQuad(List<Tile> allTiles, Tile calledTile, Tile addedTile, Side source){
        super(allTiles, calledTile, source);
        this.added = addedTile;
    }

    @Override
    public List<Tile> getTilesFormed(){
        var formed = super.getTilesFormed();
        formed.add(added);
        return formed;
    }

    @Override
    public boolean isAddQuad(){
        return true;
    }
}
