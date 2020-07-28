package jp.rouh.mahjong.tile;

import java.util.Comparator;

/**
 * 麻雀牌を表すインターフェース。
 *
 * <p>実装は{@link BaseTile}クラス及び{@link RedTile}クラスを使用します。
 * 牌の比較を行う場合は赤ドラ牌に注意が必要です。
 * 赤ドラ牌の可能性がある牌を、比較する際は,
 * このインターフェースが提供する{@link #equalsIgnoreRed}メソッドを使用する必要があります。
 * このため, コレクションフレームワークでの重複の削除等の処理には注意が必要です。
 *
 * <p>また, このインターフェスは{@link Comparable}インタフェースのサブタイプではありません。
 * 理牌の際には, {@link #comparator}メソッドで供給される{@link Comparator}クラスを利用してください。
 *
 * @see BaseTile
 * @see RedTile
 * @author Rouh
 * @version 1.0
 */
public interface Tile{

    /**
     * 一萬から中までの牌の順序を表す数(0..135)を返します。
     * 序数はドラ表示牌の参照や理牌に用いられます。
     * @return 一萬から中までの牌の順序を表す数
     */
    int tileNumber();

    /**
     * この牌が数牌であれば牌の数(1..9)を返します。
     * この牌が字牌であれば0を返します。
     * @return 1..9 数牌の場合, 数牌の数値
     *         0    字牌の場合
     */
    int suitNumber();

    /**
     * この牌の牌の種類を取得します。
     * @see TileType
     * @return 牌の種類
     */
    default TileType tileType(){
        if(isCharacter()) return TileType.CHARACTERS;
        if(isCircle()) return TileType.CIRCLES;
        if(isBamboo()) return TileType.BAMBOOS;
        if(isWind()) return TileType.WINDS;
        if(isDragon()) return TileType.DRAGONS;
        throw new AssertionError("implementation error");
    }

    /**
     * この牌が萬子かどうか検査します。
     * @return true  萬子の場合
     *         false 萬子以外の場合
     */
    boolean isCharacter();

    /**
     * この牌が筒子かどうか検査します。
     * @return true  筒子の場合
     *         false 筒子以外の場合
     */
    boolean isCircle();

    /**
     * この牌が索子かどうか検査します。
     * @return true  索子の場合
     *         false 索子以外の場合
     */
    boolean isBamboo();

    /**
     * この牌が風牌かどうか検査します。
     * @return true  風牌の場合
     *         false 風牌以外の場合
     */
    boolean isWind();

    /**
     * この牌が三元牌かどうか検査します。
     * @return true  三元牌の場合
     *         false 三元牌以外の場合
     */
    boolean isDragon();

    /**
     * この牌が字牌かどうか検査します。
     * @return true  字牌の場合
     *         false 字牌以外の場合
     */
    default boolean isHonor(){
        return suitNumber()==0;
    }

    /**
     * この牌が老頭牌かどうか検査します。
     * @return true  老頭牌の場合
     *         false 老頭牌以外の場合
     */
    default boolean isTerminal(){
        return suitNumber()==1 || suitNumber()==9;
    }

    /**
     * この牌が么九牌かどうか検査します。
     * @return true  么九牌の場合
     *         false 么九牌以外の場合
     */
    default boolean isOrphan(){
        return isHonor() || isTerminal();
    }

    /**
     * この牌が緑一色の構成牌となり得るか検査します。
     * @return true  緑一色の構成牌の場合
     *         false 緑一色の構成牌以外の場合
     */
    boolean isGreen();

    /**
     * この牌に順子を構成する上で必要となる前の数牌が存在するか検査します。
     * @return true  順子の2..9番目の牌の場合
     *         false 順子の1番目の牌もしくは字牌の場合
     */
    default boolean hasPrevious(){
        return suitNumber()>=2 && suitNumber()<=9;
    }

    /**
     * この牌に順子を構成する上で必要となる次の数牌が存在するか検査します。
     * @return true  順子の1..8番目の牌の場合
     *         false 順子の9番目の牌もしくは字牌の場合
     */
    default boolean hasNext(){
        return suitNumber()>=1 && suitNumber()<=8;
    }

    /**
     * この牌が指定した牌の順子を構成する上で次に来る牌であるかどうか検査します。
     * 比較の際, 赤ドラかどうかは無視されます。
     * @param other 検査対象
     * @return true  指定した牌の次の牌の場合
     *         false 指定した牌の次の牌でない場合
     *               指定した牌に次の牌が存在しない場合
     */
    default boolean isNextOf(Tile other){
        return other.hasNext() && other.next().equalsIgnoreRed(this);
    }

    /**
     * この牌と指定した牌を赤ドラ牌かどうかを無視して等価か検査します。
     * 例えば, 赤五筒と五筒は検査に適合します。
     * また, 五筒と五筒の場合も検査に適合します。
     * @param other 検査対象
     * @return true  等価な場合
     *         false 等価でない場合
     */
    default boolean equalsIgnoreRed(Tile other){
        return this.tileNumber()==other.tileNumber();
    }

    /**
     * この牌と指定した牌が同種の牌かどうかを検査します。
     * @param other 検査対象
     * @return true  同種の場合
     *         false 同種でない場合
     */
    default boolean isSameTypeOf(Tile other){
        return this.tileType()==other.tileType();
    }

    /**
     * この牌が順子を構成する上で必要となる前の数牌を返します。
     * {@link #hasPrevious}メソッドで事前に例外の発生を防ぐことができます。
     * 例えば, 四筒に対してこのメソッドを呼んだ場合, 三筒が結果として返されます。
     * また, 一萬に対してこのメソッドを呼んだ場合, 前の数牌が存在しないため例外がスローされます。
     * @return 次の連続する数牌
     * @throws java.util.NoSuchElementException 字牌または九牌の場合
     */
    Tile previous();

    /**
     * この牌が順子を構成する上で必要となる次の数牌を返します。
     * {@link #hasNext}メソッドで事前に例外の発生を防ぐことができます。
     * 例えば, 四筒に対してこのメソッドを呼んだ場合, 五筒が結果として返されます。
     * また, 九萬に対してこのメソッドを呼んだ場合, 次の数牌が存在しないため例外がスローされます。
     * @return 次の連続する数牌
     * @throws java.util.NoSuchElementException 字牌または九牌の場合
     */
    Tile next();

    /**
     * この牌がドラ表示牌の場合のドラ牌を返します。
     * 例えば, 四筒に対してこのメソッドを呼んだ場合, 五筒が結果として返されます。
     * また, 九萬に対してこのメソッドを呼んだ場合, 一筒が結果として返されます。
     * @return 次の牌
     */
    Tile indicates();

    /**
     * この牌が赤ドラ牌かどうか検査します。
     * @return true  赤ドラの場合
     *         false 赤ドラ以外の場合
     */
    boolean isPrisedRed();

    /**
     * 理牌する際に用いる順序を整数で返します。
     * 牌は{@code tileNumber}の順に並び、
     * かつ同じ数牌の数の赤ドラ牌は通常の牌の後ろに並びます。
     * @return 理牌順を表す数
     */
    default int sortNumber(){
        return tileNumber()*2 + (isPrisedRed()? 1:0);
    }

    /**
     * 理牌のためのコンパレータを供給します。
     * @see Comparator
     * @return コンパレータ
     */
    static Comparator<Tile> comparator(){
        return Comparator.comparing(Tile::sortNumber);
    }
}
