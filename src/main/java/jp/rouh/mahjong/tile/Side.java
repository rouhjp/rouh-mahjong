package jp.rouh.mahjong.tile;

import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * ある方角{@link Wind}に対する相対位置を表すクラス。
 * @see Wind
 * @author Rouh
 * @version 1.0
 */
public enum Side{
    /** 自家 */
    SELF(0),
    /** 下家 */
    RIGHT(1),
    /** 対面 */
    ACROSS(2),
    /** 上家 */
    LEFT(3);

    private final int orderNumber;
    Side(int orderNumber){
        this.orderNumber = orderNumber;
    }

    /**
     * 引数で与えられた方角から, この相対位置に位置する方角を返します。
     *
     * <p>例えば次のような等式が成り立ちます。
     * {@code RIGHT.of(Wind.EAST)==Wind.SOUTH }
     * @see Wind
     * @param reference 基準の方角
     * @return 基準の方角からみて相対位置にある方角
     */
    public Wind of(Wind reference){
        return reference.shift(orderNumber);
    }

    /**
     * 相対位置から, 引数で与えられた相対位置にある相対位置を返します。
     *
     * <p>例えば次のような等式が成り立ちます。
     * {@code RIGHT.of(RIGHT)==ACROSS }
     * @param side 合成する相対位置
     * @return 合成結果
     */
    public Side of(Side side){
        return values()[(orderNumber + side.orderNumber)%4];
    }

    /**
     * この相対位置以外の相対位置をリスト形式で返します。
     *
     * <p>例えば {@code SELF.others()} は, [RIGHT, ACROSS, LEFT]と等価です。
     * @return 残りの相対位置のリスト
     */
    public List<Side> others(){
        return Stream.of(values()).filter(not(this::equals)).collect(toList());
    }

    /**
     * 2つのサイコロが示す相対位置を返します。
     * @param d1 1つ目のサイコロの目の値(1..6)
     * @param d2 2つ目のサイコロの目の値(1..6)
     * @throws IllegalArgumentException 与えられたサイコロの目が1~6の範囲外の場合
     * @return RIGHT  サイコロの目の合計が 2, 6, 10 のとき
     *         ACROSS サイコロの眼の合計が 3, 7, 11 のとき
     *         LEFT   サイコロの眼の合計が 4, 8, 12 のとき
     *         SELF   サイコロの眼の合計が 5, 9 のとき
     */
    public static Side of(int d1, int d2){
        if(d1<=0 || d2<=0 || d1>6 || d2>6)
            throw new IllegalArgumentException("not dice value");
        return values()[(d1 + d2 - 1)%4];
    }
}
