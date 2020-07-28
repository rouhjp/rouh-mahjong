package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

/**
 * 手牌中から作成した面子を表すクラス。
 *
 * <p>このクラスは面子{@link Meld}インターフェースの実装です。
 * このクラスは暗槓を含む副露のように公開された面子ではなく,
 * 和了時のプレイヤーの手牌中から並べ替え処理を経て構成された面子です。
 *
 * <p>手牌中から作成した面子クラスは基本的に暗面子です。
 * しかし, ロン牌によって成立した刻子は例外的に暗刻となるため,
 * この牌が暗面子かどうかコンストラクタで指定する必要があります。
 *
 * @author Rouh
 * @version 1.0
 */
class HandMeld implements Meld{
    private final List<Tile> tiles;
    private final boolean concealed;
    HandMeld(List<Tile> tiles, boolean concealed){
        this.tiles = tiles;
        this.concealed = concealed;
    }

    @Override
    public List<Tile> getTilesFormed(){
        return tiles;
    }
    @Override
    public List<Tile> getTilesSorted(){
        return tiles;
    }
    @Override
    public Side getSourceSide(){
        return Side.SELF;
    }
    @Override
    public boolean isAddQuad(){
        return false;
    }
    @Override
    public boolean isConcealed(){
        return concealed;
    }
    @Override
    public boolean isCalled(){
        return false;
    }
    @Override
    public boolean isExposed(){
        return false;
    }
}
