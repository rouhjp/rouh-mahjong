package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 暗槓を表すクラス。
 *
 * <p>このクラスは面子{@link Meld}インターフェースの実装で,
 * {@link HandMeld}クラスのサブクラスです。
 *
 * @author Rouh
 * @version 1.0
 */
class SelfQuad extends HandMeld{
    public SelfQuad(List<Tile> tiles){
        super(tiles, true);
    }

    @Override
    public boolean isExposed(){
        return true;
    }
}
