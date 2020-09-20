package jp.rouh.mahjong.tile;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * 方角を表すクラス。
 * @see Side
 * @author Rouh
 * @version 1.0
 */
public enum Wind{
    /** 東風 */
    EAST(0, "東"),
    /** 南風 */
    SOUTH(1, "南"),
    /** 西風 */
    WEST(2, "西"),
    /** 北風 */
    NORTH(3, "北");
    
    private final int orderNumber;
    private final String string;
    Wind(int orderNumber, String string){
        this.orderNumber = orderNumber;
        this.string = string;
    }

    /**
     * 東南西北の順に習い、次の方角を返します。
     *
     * <p>例えば次のような等式が成り立ちます。
     * {@code EAST.next()==SOUTH }
     * {@code NORTH.next()==EAST }
     * @return 次の方角
     */
    public Wind next(){
        return shift(1);
    }

    /**
     * 東南西北の順に習い、n個次の方角を返します。
     *
     * <p>例えば次のような等式が成り立ちます。
     * {@code EAST.shift(2)==WEST }
     * {@code EAST.shift(5)==SOUTH }
     * @param n 重み
     * @return n個次の方角
     */
    public Wind shift(int n){
        return values()[(orderNumber + n)%4];
    }

    /**
     * この方角以外の方角をリスト形式で返します。
     *
     * <p>例えば {@code SOUTH.others()} は, [EAST, WEST, NORTH]と等価です。
     * @return 残りの方角のリスト
     */
    public List<Wind> others(){
        return Stream.of(values()).filter(not(this::equals)).collect(toList());
    }

//    public void iterate(Consumer<Wind> action){
//        Stream.iterate(this, Wind::next).limit(4).forEachOrdered(action);
//    }

    /**
     * この方角の, 引数で与えられた基準の方角からみた場合の相対位置を返します。
     *
     * <p>例えば以下の等式が成り立ちます。
     * {@code EAST.from(SOUTH)==Side.LEFT }
     * @see Side
     * @param reference 基準の方角
     * @return 基準の方角からみたこの方角の相対位置
     */
    public Side from(Wind reference){
        return Side.values()[(4 + this.orderNumber - reference.orderNumber)%4];
    }

    /**
     * 対応する風牌を返します。
     * @return 対応する風牌
     */
    public Tile toTile(){
        switch(this){
            case EAST: return Tile.WE;
            case SOUTH: return Tile.WS;
            case WEST: return Tile.WW;
            case NORTH: return Tile.WN;
        }
        throw new AssertionError("implementation error");
    }

    @Override
    public String toString(){
        return string;
    }
}
