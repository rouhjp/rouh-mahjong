package jp.rouh.mahjong.tile;

import jp.rouh.util.OperableList;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

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
public final class HandGroupPatterns{
    private HandGroupPatterns(){
        throw new AssertionError("no instance for you");
    }
    private static final class HandGroupPattern implements Comparable<HandGroupPattern>{
        private final OperableList<Integer> values;
        private HandGroupPattern(Integer... integers){
            this(OperableList.of(integers));
        }
        private HandGroupPattern(OperableList<Integer> values){
            values.sort(reverseOrder());
            this.values = values;
        }
        private List<Integer> getValues(){
            return values;
        }
        private Set<HandGroupPattern> children(){
            var patterns = new TreeSet<HandGroupPattern>();
            patterns.add(this);
            if(values.size()<2){
                return patterns;
            }
            for(var combination: values.combinationSizeOf(2)){
                var childValue = new OperableList<>(values).removedEach(combination).added(combination.get(0) + combination.get(1));
                var pattern = new HandGroupPattern(childValue);
                if(patterns.add(pattern)){
                    patterns.addAll(pattern.children());
                }
            }
            return patterns;
        }

        @Override
        public int compareTo(HandGroupPattern o){
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
        var pattern3n1 = new TreeSet<HandGroupPattern>();
        pattern3n1.add(new HandGroupPattern(1));
        var twoTwo = OperableList.of(2, 2);
        var treeOne = OperableList.of(3, 1);
        for(int i = 0; i<4; i++){
            pattern3n1.addAll(new HandGroupPattern(twoTwo).children());
            pattern3n1.addAll(new HandGroupPattern(treeOne).children());
            twoTwo.add(3);
            treeOne.add(3);
        }
        var pattern3n2 = new TreeSet<HandGroupPattern>();
        var two = OperableList.of(2);
        for(int i = 0; i<4; i++){
            pattern3n2.addAll(new HandGroupPattern(two).children());
            two.add(3);
        }
        PATTERN_3N1 = pattern3n1.stream()
                .map(HandGroupPattern::getValues)
                .collect(Collectors.toSet());
        PATTERN_3N2 = pattern3n2.stream()
                .map(HandGroupPattern::getValues)
                .collect(Collectors.toSet());
    }

    /**
     * 手牌が聴牌形かどうか簡易検査します。
     *
     * <p>この検査の適合は聴牌形であることの保証ではありません。
     * この簡易検査に適合したときのみ聴牌形かどうかの本検査を実施することで
     * 計算量を抑えることが可能です。
     * @param tiles 手牌(長さ3n + 1)
     * @throws IllegalArgumentException 長さが不正の場合
     * @return true  この手牌が聴牌形の条件の一部を満たす場合
     *         false この手牌が聴牌形でない場合
     */
    public static boolean matchReady(List<Tile> tiles){
        if(tiles.size()%3!=1){
            throw new IllegalArgumentException("invalid length");
        }
        var values = toPattern(grouped(tiles));
        return PATTERN_3N1.contains(values);
    }

    /**
     * 手牌が和了形かどうか簡易検査します。
     *
     * <p>この検査の適合は和了形であることの保証ではありません。
     * この簡易検査に適合したときのみ和了形かどうかの本検査を実施することで
     * 計算量を抑えることが可能です。
     * @param tiles 手牌(長さ3n + 2)
     * @throws IllegalArgumentException 長さが不正の場合
     * @return true  この手牌が和了形の条件の一部を満たす場合
     *         false この手牌が和了形でない場合
     */
    public static boolean matchCompleted(List<Tile> tiles){
        if(tiles.size()%3!=2){
            throw new IllegalArgumentException("invalid length");
        }
        var values = toPattern(grouped(tiles));
        return PATTERN_3N2.contains(values);
    }

    private static List<Integer> toPattern(List<List<Tile>> grouped){
        return grouped.stream().map(List::size)
                .sorted(reverseOrder())
                .collect(toList());
    }

    /**
     * 手牌を理牌したのち対子/塔子を構成しない牌間で分割します。
     * @param tiles 手牌
     * @return グループ化した手牌
     */
    static List<List<Tile>> grouped(List<Tile> tiles){
        return new OperableList<>(tiles)
                .sorted(Tile.comparator())
                .separateByDiff(Tiles::isNeighbour);
    }
}
