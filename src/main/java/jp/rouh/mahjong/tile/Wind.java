package jp.rouh.mahjong.tile;

import java.util.List;
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
    EAST(0),
    /** 南風 */
    SOUTH(1),
    /** 西風 */
    WEST(2),
    /** 北風 */
    NORTH(3);
    
    private final int orderNumber;
    Wind(int orderNumber){
        this.orderNumber = orderNumber;
    }

    /**
     * 東南西北の順に習い、次の方角を返します。
     * 例えば次のような等式が成り立ちます。
     * {@code EAST.next()==SOUTH }
     * {@code NORTH.next()==EAST }
     * @return 次の方角
     */
    public Wind next(){
        return shift(1);
    }

    /**
     * 東南西北の順に習い、n個次の方角を返します。
     * 例えば次のような等式が成り立ちます。
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
     * 例えば {@code SOUTH.others()} は, [EAST, WEST, NORTH]と等価です。
     * @return 残りの方角のリスト
     */
    public List<Wind> others(){
        return Stream.of(values()).filter(not(this::equals)).collect(toList());
    }

    /**
     * この方角の, 引数で与えられた基準の方角からみた場合の相対位置を返します。
     * 例えば以下の等式が成り立ちます。
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
            case EAST: return BaseTile.WE;
            case SOUTH: return BaseTile.WS;
            case WEST: return BaseTile.WW;
            case NORTH: return BaseTile.WN;
        }
        throw new AssertionError("implementation error");
    }
}
