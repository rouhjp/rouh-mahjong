package jp.rouh.mahjong.tile;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 通常牌(非赤ドラ牌)を表すクラス。
 *
 * <p>このクラスは{@link Tile}インターフェースの実装です。
 * @see Tile
 * @see RedTile
 *
 * @author Rouh
 * @version 1.0
 */
public enum BaseTile implements Tile{
    /** 一萬 */
    M1(0, 1),
    /** 二萬 */
    M2(1, 2),
    /** 三萬 */
    M3(2, 3),
    /** 四萬 */
    M4(3, 4),
    /** 五萬 */
    M5(4, 5),
    /** 六萬 */
    M6(5, 6),
    /** 七萬 */
    M7(6, 7),
    /** 八萬 */
    M8(7, 8),
    /** 九萬 */
    M9(8, 9),
    /** 一筒 */
    P1(9, 1),
    /** 二筒 */
    P2(10, 2),
    /** 三筒 */
    P3(11, 3),
    /** 四筒 */
    P4(12, 4),
    /** 五筒 */
    P5(13, 5),
    /** 六筒 */
    P6(14, 6),
    /** 七筒 */
    P7(15, 7),
    /** 八筒 */
    P8(16, 8),
    /** 九筒 */
    P9(17, 9),
    /** 一索 */
    S1(18, 1),
    /** 二索 */
    S2(19, 2),
    /** 三索 */
    S3(20, 3),
    /** 四索 */
    S4(21, 4),
    /** 五索 */
    S5(22, 5),
    /** 六索 */
    S6(23, 6),
    /** 七索 */
    S7(24, 7),
    /** 八索 */
    S8(25, 8),
    /** 九索 */
    S9(26, 9),
    /** 東 */
    WE(27, 0),
    /** 南 */
    WS(28, 0),
    /** 西 */
    WW(29, 0),
    /** 北 */
    WN(30, 0),
    /** 白 */
    DW(31, 0),
    /** 發 */
    DG(32, 0),
    /** 中 */
    DR(33, 0);

    private static final List<BaseTile> CHARACTERS = List.of(M1, M2, M3, M4, M5, M6, M7, M8, M9);
    private static final List<BaseTile> CIRCLES = List.of(P1, P2, P3, P4, P5, P6, P7, P8, P9);
    private static final List<BaseTile> BAMBOOS = List.of(S1, S2, S3, S4, S5, S6, S7, S8, S9);
    private static final List<BaseTile> WINDS = List.of(WE, WS, WW, WN);
    private static final List<BaseTile> DRAGONS = List.of(DW, DG, DR);
    private static final List<BaseTile> GREENS = List.of(S2, S3, S4, S6, S8, DG);
    private final int tileNumber;
    private final int suitNumber;
    BaseTile(int tileNumber, int suitNumber){
        this.tileNumber = tileNumber;
        this.suitNumber = suitNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int tileNumber(){
        return tileNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int suitNumber(){
        return suitNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCharacter(){
        return CHARACTERS.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCircle(){
        return CIRCLES.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBamboo(){
        return BAMBOOS.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWind(){
        return WINDS.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDragon(){
        return DRAGONS.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreen(){
        return GREENS.contains(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile previous(){
        if(!hasPrevious()) throw new NoSuchElementException();
        return valueOf((tileNumber - 1)%34);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile next(){
        if(!hasNext()) throw new NoSuchElementException();
        return indicates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile indicates(){
        return valueOf((tileNumber + 1)%34);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPrisedRed(){
        return false;
    }

    private static BaseTile valueOf(int tileNumber){
        for(BaseTile tile: values())
            if(tile.tileNumber()==tileNumber) return tile;
        throw new IllegalArgumentException("no such tile: " + tileNumber);
    }
}
