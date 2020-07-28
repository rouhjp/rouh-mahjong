package jp.rouh.mahjong.tile;

/**
 * 赤ドラ牌を表すクラス。
 *
 * <p>このクラスは{@link Tile}インターフェースの実装です。
 * @see Tile
 * @see BaseTile
 *
 * @author Rouh
 * @version 1.0
 */
public enum RedTile implements Tile{
    /** 赤五萬 */
    M5R(BaseTile.M5),

    /** 赤五筒 */
    P5R(BaseTile.P5),

    /** 赤五索 */
    S5R(BaseTile.S5);
    private final BaseTile tile;
    RedTile(BaseTile tile){
        this.tile = tile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int tileNumber(){
        return tile.tileNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int suitNumber(){
        return tile.suitNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCharacter(){
        return tile.isCharacter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCircle(){
        return tile.isCircle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBamboo(){
        return tile.isBamboo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWind(){
        return tile.isWind();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDragon(){
        return tile.isDragon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreen(){
        return tile.isGreen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile previous(){
        return tile.previous();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile next(){
        return tile.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile indicates(){
        return tile.indicates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPrisedRed(){
        return tile.isPrisedRed();
    }
}
