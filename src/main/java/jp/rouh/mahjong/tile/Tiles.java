package jp.rouh.mahjong.tile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

/**
 * 牌に関する処理を提供するユーティリティクラス。
 * @author Rouh
 * @version 1.0
 */
public final class Tiles{
    private Tiles(){
        throw new AssertionError("no instance");
    }
    /** 赤ドラ牌を含まない牌一式34枚 */
    private static final List<Tile> TILE_SET;

    /** 赤ドラ牌を含む牌一式34枚 */
    private static final List<Tile> RED_TILE_SET;

    /** 定義された全麻雀牌37枚のセット */
    private static final Set<Tile> VALUES = Set.of(Tile.values());

    /** 么九牌13枚のセット */
    private static final Set<Tile> ORPHANS;

    static{
        TILE_SET = Stream.of(Tile.values())
                .filter(not(Tile::isPrisedRed))
                .collect(toUnmodifiableList());
        RED_TILE_SET = TILE_SET.stream()
                .map(Tiles::redTileOf)
                .collect(toUnmodifiableList());
        ORPHANS = Stream.of(Tile.values())
                .filter(Tile::isOrphan)
                .collect(toUnmodifiableSet());
    }

    /**
     * 赤ドラを各数牌1枚ずつ含む136枚の牌を可変のリスト形式で供給します。
     * @return 麻雀牌一式のリスト
     */
    public static List<Tile> fullSet(){
        return Stream.of(TILE_SET, TILE_SET, TILE_SET, RED_TILE_SET)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * 全ての非赤ドラ牌と赤ドラ牌を1枚ずつ含む37枚の牌をリスト形式で供給します。
     * @return 全種類の麻雀牌のリスト
     */
    public static Set<Tile> values(){
        return VALUES;
    }

    /**
     * 么九牌1枚ずつ含む13枚の牌を可変のリスト形式で供給します。
     * @return 么九牌のリスト
     */
    public static Set<Tile> orphans(){
        return ORPHANS;
    }

    /**
     * 与えられた牌に対応する赤ドラ牌を返却します。
     * 該当する赤ドラ牌が存在しない場合は、与えられた牌をそのまま返します。
     * @param tile 牌
     * @return 対応する赤ドラ牌
     */
    public static Tile redTileOf(Tile tile){
        switch(tile){
            case M5: return Tile.M5R;
            case P5: return Tile.P5R;
            case S5: return Tile.S5R;
        }
        return tile;
    }

    /**
     * 与えられた牌に対応する非赤ドラ牌を返却します。
     * 与えられた牌が赤ドラ牌でない場合, 与えられた牌をそのまま返却します。
     * @param tile 牌
     * @return 対応する非赤ドラ牌
     */
    public static Tile baseTileOf(Tile tile){
        switch(tile){
            case M5R: return Tile.M5;
            case P5R: return Tile.P5;
            case S5R: return Tile.S5;
        }
        return tile;
    }

    /**
     * 与えられた牌の順子を構成する上で周囲の牌をリスト形式で返却します。
     * 返却されるリストには, 必ず与えられた牌が含まれますが,
     * 与えられた牌が赤ドラ牌の場合は, 対応する通常牌が代わりに挿入されます。
     * 例えば, 赤五萬の周囲の牌は, [四萬, 五萬, 六萬] となります。
     * また, 九萬の周囲の牌は, [八萬, 九萬] となります。
     * また, 東の周囲の牌は, [東] となります。
     * @param tile 牌
     * @return 周囲の牌のリスト
     */
    public static List<Tile> aroundTilesOf(Tile tile){
        var list = new ArrayList<Tile>();
        if(tile.hasPrevious()){
            list.add(tile.previous());
        }
        list.add(baseTileOf(tile));
        if(tile.hasNext()){
            list.add(tile.next());
        }
        return list;
    }

    /**
     * 与えられた牌と牌が対子・塔子を構成しうる近隣の牌であるか検査します。
     * 例えば, 一萬と一萬は塔子を構成し得るため検査に適合します。
     * 例えば, 一萬と三萬は塔子を構成し得るため検査に適合します。
     * 例えば, 一萬と四萬は対子・塔子いずれも構成し得ないため適合しません。
     * 例えば, 東と西は対子・塔子いずれも構成し得ないため適合しません。
     * @param a 1つ目の牌
     * @param b 2つ目の牌
     * @throws NullPointerException どちらか一方でも{@code null}の場合
     * @return true  近隣の牌である場合
     *         false 近隣の牌でない場合
     */
    public static boolean isNeighbour(Tile a, Tile b){
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.isSameTypeOf(b) &&
                Math.abs(a.tileNumber() - b.tileNumber()) < 3;
    }

    /**
     * 与えられた牌のリストが順子の構成牌たり得るか検査します。
     * @param tiles 牌のリスト
     * @return true  順子の構成牌である場合
     *         false 順子の構成牌出ない場合
     */
    public static boolean isStraight(List<Tile> tiles){
        var sorted = tiles.stream().sorted().collect(toList());
        return sorted.size()==3
                && sorted.get(1).isNextOf(sorted.get(0))
                && sorted.get(2).isNextOf(sorted.get(1));
    }

    /**
     * 与えられた牌のリストが刻子の構成牌たり得るか検査します。
     * @param tiles 牌のリスト
     * @return true  刻子の構成牌である場合
     *         false 刻子の構成牌出ない場合
     */
    public static boolean isTriple(List<Tile> tiles){
        return tiles.size()==3
                && tiles.get(0).equalsIgnoreRed(tiles.get(1))
                && tiles.get(1).equalsIgnoreRed(tiles.get(2));
    }

    /**
     * 与えられた牌のリストが槓子の構成牌たり得るか検査します。
     * @param tiles 牌のリスト
     * @return true  槓子の構成牌である場合
     *         false 槓子の構成牌出ない場合
     */
    public static boolean isQuad(List<Tile> tiles){
        return tiles.size()==4
                && tiles.get(0).equalsIgnoreRed(tiles.get(1))
                && tiles.get(1).equalsIgnoreRed(tiles.get(2))
                && tiles.get(2).equalsIgnoreRed(tiles.get(3));
    }
}
