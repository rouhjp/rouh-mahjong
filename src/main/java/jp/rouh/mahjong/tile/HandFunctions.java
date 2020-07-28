package jp.rouh.mahjong.tile;

import jp.rouh.util.OperableList;

import java.util.*;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * 手牌に関する関数ユーティリティクラス。
 *
 * @author Rouh
 * @version 1.0
 */
public class HandFunctions{

    /**
     * 手牌が七対子形であるかどうか検査します。
     *
     * <p>このメソッドは役判定ではなく, 手牌の形の成立を判定するために用います。
     * なお, 定義的に七対子は7種類の異なる牌の対子で成立します。
     * 例えば [1 1 2 2 3 3 4 4 5 5 6 6 7 7]は検査に適合しますが,
     * [1 1 2 2 3 3 4 4 5 5 6 6 6 6]は検査に適合しません。
     * @param tiles 手牌のリスト
     * @return true  七対子形と解釈可能な場合
     *         false 七対子形ではない場合
     */
    public static boolean isSevenPairsHand(List<Tile> tiles){
        return tiles.size()==14
                && tiles.stream().collect(groupingBy(Tile::tileNumber))
                .values().stream().allMatch(list->list.size()==2);
    }

    /**
     * 手牌が国士無双形であるかどうか検査します。
     *
     * <p>このメソッドは役判定ではなく, 手牌の形の成立を判定するために用います。
     * @param tiles 手牌のリスト
     * @return true  国士無双形の場合
     *         false 国士無双形でない場合
     */
    public static boolean isThirteenOrphansHand(List<Tile> tiles){
        return tiles.size()==14
                && tiles.stream().allMatch(Tile::isOrphan)
                && tiles.stream().collect(groupingBy(Tile::tileNumber)).size()==13;
    }

    /**
     * 与えられた牌のリストを並べ替え, 雀頭構成牌または面子構成牌(牌のリスト)のリストに変換します。
     *
     * <p>雀頭は常にリストの先頭に配置されます。
     * 一つのリストから複数パターンの解釈が可能な場合があるため,
     * 結果はパターンが格納されたセット形式で返されます。
     * 並べ替え不成立の場合は空のセットが返されます。
     * <pre>
     * [2 2 3 3 4 4 5 5 6 6 7 7 8 8]              :{@code List<Tile> input}
     *    +-> [[2 2][3 4 5][3 4 5][6 7 8][6 7 8]] :{@code List<List<Tile>> pattern1}
     *    +-> [[5 5][2 3 4][2 3 4][6 7 8][6 7 8]] :{@code List<List<Tile>> pattern2}
     *    +-> [[8 8][2 3 4][2 3 4][5 6 7][5 6 7]] :{@code List<List<Tile>> pattern3}
     *                                            :{@code Set<List<List<Tile>>> output}
     * </pre>
     * @param tiles 手牌のリスト(長さ3n+2 (n=0..))
     * @return 雀頭及び面子の並び替えパターンのセット
     */
    public static Set<List<List<Tile>>> arrange(List<Tile> tiles){
        if(tiles.size()<2 || tiles.size()%3!=2) throw new IllegalArgumentException("invalid length");
        var patterns = new HashSet<List<List<Tile>>>();
        for(var heads: pickHeads(tiles)){
            var tail = new OperableList<>(tiles).removedEach(heads);
            for(var melds: arrangeTail(tail)){
                var pattern = new ArrayList<List<Tile>>();
                pattern.add(heads);
                pattern.addAll(melds);
                patterns.add(pattern);
            }
        }
        return patterns;
    }

    /**
     * 与えられた牌のリストを並べ替え, 面子構成牌(牌のリスト)のリストに変換します。
     *
     * <p>一つのリストから複数パターンの解釈が可能な場合があるため,
     * 結果はパターンが格納されたセット形式で返されます。
     * 並べ替え不成立の場合は空のセットが返されます。
     * <pre>
     * [1 1 1 2 2 2 3 3 3]            :{@code List<Tile> input}
     *    +-> [[1 1 1][2 2 2][3 3 3]] :{@code List<List<Tile>> pattern1}
     *    +-> [[1 2 3][1 2 3][1 2 3]] :{@code List<List<Tile>> pattern2}
     *                                :{@code Set<List<List<Tile>>> output}
     * </pre>
     * @param tiles 手牌のリスト(長さ3n (n=0..))
     * @throws IllegalArgumentException 手牌のリストの長さが不正な場合
     * @return 面子の並べ替えパターンのセット
     */
    private static Set<List<List<Tile>>> arrangeTail(List<Tile> tiles){
        if(tiles.size()%3!=0) throw new IllegalArgumentException("invalid length");
        var tail = new OperableList<>(tiles).sorted(Tile.comparator());
        var patterns = new HashSet<List<List<Tile>>>();
        var melds = new OperableList<List<Tile>>(tiles.size()/3);
        while(tail.size()>0){
            assert tail.size()%3==0;
            var meld = new ArrayList<Tile>();
            if(tail.get(0).equalsIgnoreRed(tail.get(2))){
                //TRIPLE like [1, 1, 1]
                meld.add(tail.remove(2));
                meld.add(tail.remove(1));
                meld.add(tail.remove(0));
            }else{
                try{
                    // STRAIGHT like [1, 2, 3]
                    meld.add(tail.remove(0));
                    meld.add(tail.remove(tail.indexOf(tile->tile.isNextOf(meld.get(0)))));
                    meld.add(tail.remove(tail.indexOf(tile->tile.isNextOf(meld.get(0)))));
                }catch(IndexOutOfBoundsException e){
                    // NOT FOUND like [2, 3, 9]
                    /* IndexOutOfBoundsException might be thrown by List#remove,
                     * for the return value of OperableList#indexOf is -1,
                     * when there is no appropriate tile to make a straight-meld
                     */
                    return Collections.emptySet();
                }
            }
            melds.add(meld);
        }
        patterns.add(melds);
        if(melds.size()>=3){
            /* check three consequence triples
             * like [1 1 1][2 2 2][3 3 3], which can be rearranged
             * in three same straights: [1 2 3][1 2 3][1 2 3]
             */
            triples: for(var triples:melds.combinationSizeOf(3)){
                if(triples.stream().anyMatch(not(Tiles::isTriple))) continue;
                var theOthers = new OperableList<>(melds).removedEach(triples);
                var straights = new ArrayList<List<Tile>>();
                for(int i = 0; i<3; i++){
                    var straight = new ArrayList<Tile>();
                    straight.add(triples.get(0).get(i));
                    straight.add(triples.get(1).get(i));
                    straight.add(triples.get(2).get(i));
                    if(Tiles.isStraight(straight)) continue triples;
                    straights.add(straight);
                }
                straights.addAll(theOthers);
                patterns.add(straights);
            }
        }
        return patterns;
    }

    /**
     * 与えられた牌のリストから, 対子を抽出します。
     *
     * <p>各対子は長さ2のリストとして結果セットに格納されます。
     * 例えば, [1 1 2 3 4 4 4 5 5R 6 6 6]の場合は,
     * [1 1][4 4][5 5R][6 6]が結果として返されます。
     * また, [5 5 5 5R]のような手の場合, 対子の抽出の方法としては
     * [5 5]と[5 5R]のように, 赤ドラを含むパターンとそうでないパターンが
     * 考えられますが, 赤ドラの手牌構成上の位置は点数計算上に影響を及ぼさないため
     * このような差異は無視されいずれかのみが結果セットに格納されます。
     * このメソッドは引数のリストに副作用を与えません。
     * @param tiles 牌のリスト
     * @return 対子のセット
     */
    private static Set<List<Tile>> pickHeads(List<Tile> tiles){
        return tiles.stream()
                .collect(groupingBy(Tile::tileNumber))
                .values()
                .stream()
                .filter(list->list.size()>=2)
                .map(list->list.subList(0, 2))
                .collect(toSet());
    }
}
