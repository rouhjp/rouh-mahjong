package jp.rouh.mahjong.score;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;
import jp.rouh.util.OperableList;

import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * 手牌パターンの分析により手牌の立直/和了の判定を補助するユーティリティクラス。
 *
 * <p>手牌を理牌した後, 対子/塔子を構成しない牌間で分割する操作を想定します。例えば,
 * <ul><li>[M1, M2, M3, P1, P2, P3, P7, P8, P9, 東, 東, 東, 白, 白]</li></ul>
 * この手牌は次のようなグループに分割が可能です。
 * <ul><li>[M1, M2, M3][P1, P2, P3][P7, P8, P9][東, 東, 東][白, 白]</li></ul>
 * 牌の内容を無視し, グループごとの牌の枚数に注目すると,
 * グルーピングの結果は次のように表すことができます。
 * <ul><li>| 3 | 3 | 3 | 3 | 2|</li></ul>
 *
 * <p>この操作を面子手の和了時の手牌に行った場合,
 * ４つの面子と雀頭の最大でも5つのグループまでにしか分割されません。
 * つまり, ある14枚の手牌に対してこの分割操作を行った結果,
 * グループが6以上できた場合, その手牌は和了していないことが検査できます。
 * また同様に, ある13枚の手牌に対してこの分割操作を行った結果,
 * グループが7以上できた場合, その手牌は聴牌していないことが検査できます。
 *
 * <p>和了形のうち, 複数の面子/雀頭が隣接する牌で構成されていた場合は
 * グループが連鎖して5つより少ないグループ数になります。例えば次の手牌
 * <ul><li>[M1, M1, M1, M2, M3, M4, P7, P8, P9, 東, 東, 東, 白, 白]</li></ul>
 * は分割操作を行うと以下のようになります。
 * <ul><li>|   6  | 3 | 3 | 2|</li></ul>
 * これは一萬の刻子と, 二/三/四萬の順子が隣接するためグループが連鎖した状態です。
 * この連鎖のパターンを全て列挙すると以下の通りになります。
 * <ul>
 * <li>|  3  |  3  |  3  |  3  | 2 |</li>
 * <li>|    5    |  3  |  3  |  3  |</li>
 * <li>|     6     |  3  |  3  | 2 |</li>
 * <li>|     6     |    5    |  3  |</li>
 * <li>|     6     |     6     | 2 |</li>
 * <li>|       8       |  3  |  3  |</li>
 * <li>|       8       |     6     |</li>
 * <li>|        9        |  3  | 2 |</li>
 * <li>|        9        |    5    |</li>
 * <li>|         11          |  3  |</li>
 * <li>|          12           | 2 |</li>
 * <li>|            14             |</li>
 * </ul>
 *
 * <p>また聴牌形の13枚に対する連鎖パターンは
 * <ul><li>|  3  |  3  |  3  | 2 | 2 |</li>
 *     <li>|  3  |  3  |  3  |  3  |1|</li></ul>
 * 上の２つのパターンとその連鎖した形となるため, 全て列挙すると以下の通りになります。
 * <ul>
 * <li>|  3  |  3  |  3  | 2 | 2 |</li>
 * <li>|  3  |  3  |  3  |  3  |1|</li>
 * <li>|   4   |  3  |  3  |  3  |</li>
 * <li>|    5    |  3  |  3  | 2 |</li>
 * <li>|    5    |    5    |  3  |</li>
 * <li>|     6     |  3  | 2 | 2 |</li>
 * <li>|     6     |  3  |  3  |1|</li>
 * <li>|     6     |   4   |  3  |</li>
 * <li>|     6     |    5    | 2 |</li>
 * <li>|     6     |     6     |1|</li>
 * <li>|      7      |  3  |  3  |</li>
 * <li>|      7      |     6     |</li>
 * <li>|       8       |  3  | 2 |</li>
 * <li>|       8       |    5    |</li>
 * <li>|        9        | 2 | 2 |</li>
 * <li>|        9        |  3  |1|</li>
 * <li>|        9        |   4   |</li>
 * <li>|        10         |  3  |</li>
 * <li>|         11          | 2 |</li>
 * <li>|          12           |1|</li>
 * <li>|           13            |</li>
 * </ul>
 *
 * <p>上記の表に照らし合わせることで, ある13枚が聴牌形であるかどうかを判定できます。
 * また, 1枚のグループは単騎待ちの牌, 2枚のグループはその他の待ちの対子/塔子であるため,
 * 和了牌は1枚のグループまたは2枚のグループの周囲の牌となります。
 * そのため, 2枚もしくは1枚のグループ, もしくはそれらが連鎖したグループ,
 * つまり3の倍数枚でないグループの周囲の牌が和了牌となります。
 * このように, グルーピングによって和了牌の候補となる牌を絞り込むことが可能です。
 *
 * @author Rouh
 * @version 1.0
 */
public final class HandSections{
    private HandSections(){
        throw new AssertionError("no instance for you");
    }
    private static final class HandSection implements Comparable<HandSection>{
        private final OperableList<Integer> values;
        private HandSection(Integer... integers){
            this(OperableList.of(integers));
        }
        private HandSection(OperableList<Integer> values){
            values.sort(reverseOrder());
            this.values = values;
        }
        private List<Integer> getValues(){
            return values;
        }
        private Set<HandSection> children(){
            var patterns = new TreeSet<HandSection>();
            patterns.add(this);
            if(values.size()<2){
                return patterns;
            }
            for(var combination: values.combinationSizeOf(2)){
                var childValue = new OperableList<>(values).removedEach(combination).added(combination.get(0) + combination.get(1));
                var pattern = new HandSection(childValue);
                if(patterns.add(pattern)){
                    patterns.addAll(pattern.children());
                }
            }
            return patterns;
        }

        @Override
        public int compareTo(HandSection o){
            for(int i = 0; ; i++){
                if(values.size()==i && o.values.size()==i) return 0;
                if(values.size()==i) return -1;
                if(o.values.size()==i) return 1;
                var diff = values.get(i) - o.values.get(i);
                if(diff!=0) return diff;
            }
        }

        @Override
        public int hashCode(){
            return Objects.hash(values);
        }

        @Override
        public String toString(){
            var sb = new StringBuilder("|");
            for(Integer value: values){
                int digit = String.valueOf(value).length();
                sb.append(" ".repeat(Math.max(0, value - digit)));
                sb.append(value);
                sb.append(" ".repeat(value - 1));
                sb.append("|");
            }
            return sb.toString();
        }
    }
    private static final Set<List<Integer>> PATTERN_3N1;
    private static final Set<List<Integer>> PATTERN_3N2;
    static{
        var pattern3n1 = new TreeSet<HandSection>();
        pattern3n1.add(new HandSection(1));
        var twoTwo = OperableList.of(2, 2);
        var treeOne = OperableList.of(3, 1);
        for(int i = 0; i<4; i++){
            pattern3n1.addAll(new HandSection(twoTwo).children());
            pattern3n1.addAll(new HandSection(treeOne).children());
            twoTwo.add(3);
            treeOne.add(3);
        }
        var pattern3n2 = new TreeSet<HandSection>();
        var two = OperableList.of(2);
        for(int i = 0; i<4; i++){
            pattern3n2.addAll(new HandSection(two).children());
            two.add(3);
        }
        PATTERN_3N1 = pattern3n1.stream()
                .map(HandSection::getValues)
                .collect(toSet());
        PATTERN_3N2 = pattern3n2.stream()
                .map(HandSection::getValues)
                .collect(toSet());
    }

    /**
     * 手牌が面子手の聴牌形かどうか簡易検査します。
     *
     * <p>この検査の適合は聴牌形であることの保証ではありません。
     * この簡易検査に適合したときのみ聴牌形かどうかの本検査を実施することで
     * 計算量を抑えることが可能です。
     * <p>ただし, この検査に不適合の場合であっても,
     * 国士無双形および七対子形の聴牌形である可能性があります。
     * @param handTiles 手牌(長さ3n + 1)
     * @throws IllegalArgumentException 長さが不正の場合
     * @return true  この手牌が面子手の聴牌形の条件の一部を満たす場合
     *         false この手牌が面子手の聴牌形でない場合
     */
    public static boolean matchReady(List<Tile> handTiles){
        HandTiles.validateHandTiles(handTiles);
        return PATTERN_3N1.contains(patternOf(sectioned(handTiles)));
    }

    /**
     * 手牌が面子手の和了形かどうか簡易検査します。
     *
     * <p>この検査の適合は和了形であることの保証ではありません。
     * この簡易検査に適合したときのみ和了形かどうかの本検査を実施することで
     * 計算量を抑えることが可能です。
     * <p>ただし, この検査に不適合の場合であっても,
     * 国士無双形および七対子形の和了形である可能性があります。
     * @param handTiles 手牌(長さ3n + 2)
     * @throws IllegalArgumentException 長さが不正の場合
     * @return true  この手牌が面子手の和了形の条件の一部を満たす場合
     *         false この手牌が面子手の和了形でない場合
     */
    public static boolean matchCompleted(List<Tile> handTiles, Tile winningTile){
        HandTiles.validateHandTiles(handTiles);
        var fullTiles = OperableList.copyOf(handTiles).added(winningTile);
        return PATTERN_3N2.contains(patternOf(sectioned(fullTiles)));
    }

    private static List<Integer> patternOf(List<List<Tile>> sectioned){
        return sectioned.stream().map(List::size)
                .sorted(reverseOrder())
                .collect(toList());
    }

    private static List<List<Tile>> sectioned(List<Tile> tiles){
        return new OperableList<>(tiles).sorted()
                .separateByDiff(Tiles::isNeighbour);
    }

    /**
     * 与えられた手牌のリストから面子手の和了牌の候補牌を返します。
     *
     * <p>与えられた手牌のリストから, 未完成の構成要素である対子や塔子に対し,
     * 順子や刻子を構成しうる各牌の周囲の牌を和了牌の候補牌として導出します。
     * <p>国士無双13面待ちの和了牌を除いて, 手牌の和了牌は全てこの和了牌の候補牌に
     * 含まれることを保証します。一方で, 候補牌の中には和了牌ではない牌も含まれます。
     * この処理は34種類全ての牌での和了牌チェックをしないための計算量短縮用処理です。
     * <p>手牌が聴牌でない場合は大量の候補牌が返される可能性があります。
     * そのため候補牌を列挙する前に, 聴牌かどうかの絞り込みを{@link #matchReady}
     * メソッドで事前検査しておくことが推奨されます。
     * <p>また、[1 1 1 2 2 2 3 3 3 9 9 9 9]といった手牌に対し
     * 存在しない5枚目の[9]の和了が不可能であるように, 既に手牌中に4枚
     * 重複のある牌は候補牌には含みません。
     * <p>結果として返される和了牌の候補のセットには赤ドラ牌は含みません。
     * <pre>
     *     [1 1 1 3 4 4 4 8 8 8 9 9 9]    ... {@code List<Tile>}
     *   =>[[1 1 1 3 4 4 4][8 8 8 9 9 9]]
     *   =>[[1 1 1 3 4 4 4]]
     *   =>[1 3 4]
     *   =>[[1 2][2 3 4][3 4 5]]
     *   =>[1 2 3 4 5]                    ... {@code Set<Tile>}
     * </pre>
     * @param handTiles 手牌(長さ3n+1(n>=0))
     * @throws IllegalArgumentException 手牌のリストの長さが不正な場合
     * @return 和了牌の候補のセット
     */
    public static Set<Tile> winningTileCandidatesOf(List<Tile> handTiles){
        return sectioned(handTiles).stream()
                .filter(section->section.size()%3!=0)
                .flatMap(List::stream)
                .flatMap(tile->Tiles.aroundTilesOf(tile).stream())
                .filter(tile->handTiles.stream()
                        .filter(tile::equalsIgnoreRed).count()<4)
                .collect(toSet());
    }
}
