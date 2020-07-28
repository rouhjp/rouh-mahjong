package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.HandFunctions;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;
import java.util.Optional;

/**
 * 特殊形の手牌を表すクラス。
 *
 * <p>このクラスは{@link FormattedHand}インターフェースの部分実装です。
 *
 * @author Rouh
 * @version 1.0
 */
abstract class IrregularHand implements FormattedHand{
    private final List<Tile> handTiles;
    IrregularHand(List<Tile> handTiles){
        this.handTiles = handTiles;
    }

    @Override
    public boolean isMeldHand(){
        return false;
    }

    @Override
    public abstract boolean isSevenPairsHand();

    @Override
    public abstract boolean isThirteenOrphansHand();

    @Override
    public Head getHead(){
        throw new UnsupportedOperationException("no head in irregular hand");
    }

    @Override
    public List<Meld> getMelds(){
        throw new UnsupportedOperationException("no melds in irregular hand");
    }

    @Override
    public List<Meld> getHandMelds(){
        throw new UnsupportedOperationException("no melds in irregular hand");
    }

    @Override
    public List<Meld> getOpenMelds(){
        throw new UnsupportedOperationException("no melds in irregular hand");
    }

    @Override
    public List<HandComponent> getComponents(){
        throw new UnsupportedOperationException("no components in irregular hand");
    }

    @Override
    public List<Tile> getTilesSorted(){
        return handTiles;
    }

    @Override
    public List<Tile> getTilesTruncated(){
        return handTiles;
    }

    @Override
    public Wait getWait(){
        return Wait.SINGLE_HEAD;
    }

    /**
     * 与えた手牌が七対子形であれば七対子形の整形済み手牌を取得します。
     * @param handTiles 手牌
     * @return 七対子形の整形済み手牌(オプショナル)
     */
    public static Optional<IrregularHand> tryGetSevenPairs(List<Tile> handTiles){
        return HandFunctions.isSevenPairsHand(handTiles)?
                Optional.of(ofSevenPairs(handTiles)):Optional.empty();
    }

    /**
     * 与えた手牌が国士無双形であれば国士無双形の整形済み手牌を取得します。
     * @param handTiles 手牌
     * @return 国士無双形の整形済み手牌(オプショナル)
     */
    public static Optional<IrregularHand> tryGetThirteenOrphans(List<Tile> handTiles){
        return HandFunctions.isThirteenOrphansHand(handTiles)?
                Optional.of(ofThirteenOrphans(handTiles)):Optional.empty();
    }

    private static IrregularHand ofSevenPairs(List<Tile> handTiles){
        return new IrregularHand(handTiles){
            @Override
            public boolean isSevenPairsHand(){
                return true;
            }
            @Override
            public boolean isThirteenOrphansHand(){
                return false;
            }
        };
    }

    private static IrregularHand ofThirteenOrphans(List<Tile> handTiles){
        return new IrregularHand(handTiles){
            @Override
            public boolean isSevenPairsHand(){
                return false;
            }
            @Override
            public boolean isThirteenOrphansHand(){
                return true;
            }
        };
    }
}
