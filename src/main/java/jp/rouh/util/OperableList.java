package jp.rouh.util;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * 拡張的なシンタックスを提供するリストクラス
 *
 * <p>このクラスの実装は{@link java.util.ArrayList}です。
 * @param <E> 要素の型
 * @author Rouh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class OperableList<E> extends ArrayList<E>{
    /**
     * 空のリストを作成します。
     */
    public OperableList(){
        super();
    }

    /**
     * 指定された初期容量で空のリストを作成します。
     * @param initialCapacity 初期容量
     */
    public OperableList(int initialCapacity){
        super(initialCapacity);
    }

    /**
     * 指定されたコレクションの要素が含まれているリストを、要素がコレクションのイテレータによって返される順序で作成します。
     * @param c コレクション
     */
    public OperableList(Collection<? extends E> c){
        super(c);
    }

    /**
     * 指定した述語関数に適合する最初の要素の位置を取得します。
     * @param p 述語関数
     * @return 適合する要素の位置
     *         -1 適合する要素が見つからなかった場合
     */
    public int indexOf(Predicate<? super E> p){
        Objects.requireNonNull(p);
        for(int i = 0; i<size(); i++){
            if(p.test(get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定した述語関数に適合する最後の要素の位置を取得します。
     * @param p 述語関数
     * @return 適合する要素の位置
     *         -1 適合する要素が見つからなかった場合
     */
    public int lastIndexOf(Predicate<? super E> p){
        Objects.requireNonNull(p);
        for(int i = size() - 1; i>=0; i--){
            if(p.test(get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定した述語関数に適合する要素の数を返します。
     * @param p 述語関数
     * @return 適合する要素数
     */
    public int countIf(Predicate<? super E> p){
        return (int)stream().filter(p).count();
    }

    /**
     * 重複を取り除いた後, 自身の参照を返します。
     * @return 重複を取り除いたリスト
     */
    public OperableList<E> distinct(){
        for(int i = 0; i<size(); i++){
            int lastIndex;
            if((lastIndex = lastIndexOf(get(i)))!=i){
                remove(lastIndex);
            }
        }
        return this;
    }

    /**
     * ソート処理{@link Collections#sort}を実施した後, 自身の参照を返します。
     * @param comparator 要素のコンパレータ
     * @return ソート済みリスト
     */
    public OperableList<E> sorted(Comparator<E> comparator){
        sort(comparator);
        return this;
    }

    /**
     * シャッフル処理{@link Collections#shuffle}を実施した後, 自身の参照を返します。
     * @return シャッフル済みリスト
     */
    public OperableList<E> shuffled(){
        Collections.shuffle(this);
        return this;
    }

    /**
     * シャッフル処理{@link Collections#shuffle}を実施した後, 自身の参照を返します。
     * @param rand シャッフルに用いる乱数オブジェクト
     * @return シャッフル済みリスト
     */
    public OperableList<E> shuffled(Random rand){
        Collections.shuffle(this, rand);
        return this;
    }

    /**
     * 追加処理{@link List#add}を実施した後, 自身の参照を返します。
     * @param e 追加する要素
     * @return 要素を追加したリスト
     */
    public OperableList<E> added(E e){
        add(e);
        return this;
    }

    /**
     * 追加処理{@link List#addAll}を実施した後, 自身の参照を返します。
     * @param collection 追加する要素のコレクション
     * @return 要素を追加したリスト
     */
    public OperableList<E> addedAll(Collection<? extends E> collection){
        addAll(collection);
        return this;
    }

    /**
     * 削除処理{@link List#remove}を実施した後, 自身の参照を返します。
     * @param e 削除する要素
     * @return 要素を削除したリスト
     */
    public OperableList<E> removed(E e){
        remove(e);
        return this;
    }

    /**
     * 削除処理{@link List#removeAll}を実施した後, 自身の参照を返します。
     * @param collection 削除する要素のコレクション
     * @return 要素を削除したリスト
     */
    public OperableList<E> removedAll(Collection<? extends E> collection){
        removeAll(collection);
        return this;
    }

    /**
     * 指定したコレクションの要素を一つずつ削除します。
     *
     * <p>{@link List#removeAll}とは異なり, このコレクションの要素に重複がある場合
     * 削除対象として引数に指定したコレクションに含まれる同じ要素の数だけ削除が実施されます。
     * @param collection 削除する要素のコレクション
     */
    public void removeEach(Collection<? extends E> collection){
        collection.forEach(this::remove);
    }

    /**
     * 削除処理{@link OperableList#removeEach}を実施した後, 自身の参照を返します。
     * @param collection 削除する溶損のコレクション
     * @return 要素を削除したリスト
     */
    public OperableList<E> removedEach(Collection<? extends E> collection){
        collection.forEach(this::remove);
        return this;
    }

    /**
     * このリストを分割します。
     *
     * <p>リストの先頭から順に隣接する2つの要素を与えられた二項述語関数で検査し
     * 検査に適合した箇所を境として分割します。
     * このメソッドを実施する前に, 任意の方法で要素をソートすることが想定されます。
     * @param separator 境となる2要素が与えられた場合のみtrueを返す二項述語関数
     * @return 分割した要素郡のリスト
     */
    public List<List<E>> separateByDiff(BiPredicate<E, E> separator){
        var result = new ArrayList<List<E>>();
        var container = new ArrayList<E>();
        for(int i = 1; i<size(); i++){
            var left = get(i - 1);
            var right = get(i);
            container.add(left);
            if(separator.test(left, right)){
                result.add(new ArrayList<>(container));
                container.clear();
            }
        }
        if(!container.isEmpty()){
            result.add(new ArrayList<>(container));
        }
        return result;
    }

    /**
     * リストから, 全ての要素の組み合わせのリストを取得します。
     *
     * <p>例えば, このリストが["A", "B", "C"]の時, 全ての要素の組み合わせは
     * ["A"], ["B"], ["C"], ["A", "B"], ["A", "C"], ["B", "C"], ["A", "B", "C"] となります。
     * @return 全ての要素の組み合わせ
     */
    public List<List<E>> combination(){
        return IntStream.range(1, size()).mapToObj(this::combinationSizeOf)
                .flatMap(List::stream).collect(toList());
    }

    /**
     * リストから, 指定したサイズの全ての要素の組み合わせのリストを取得します。
     *
     * <p>例えば, このリストが["A", "B", "C"]の時, 長さ2の要素の組み合わせは
     * ["A", "B"], ["A", "C"], ["B", "C"] となります。
     * 必然的に, 全ての要素の組み合わせのリストの長さは,
     * リストサイズnと組み合わせのサイズmに対し, nCmとなり,
     * 残りの要素のリストの長さは n - nCm となります。
     * @param size 組み合わせのサイズ
     * @throws IndexOutOfBoundsException 与えられた組み合わせのサイズがリストの長さより大きい場合
     *                                   与えられた組み合わせのサイズが0または負の場合
     * @return 要素の組合わせ
     */
    public List<List<E>> combinationSizeOf(int size){
        if(size <= 0) throw new IndexOutOfBoundsException("combination size must be positive");
        if(size > size()) throw new IndexOutOfBoundsException("combination size out of list size");
        return new CombinationSupport(size, size()).getAllCombinationIndexes().stream()
                .map(indexes->indexes.stream().map(this::get).collect(toList())).collect(toList());
    }

    private static class CombinationSupport{
        private final int combinationSize;
        private final int collectionSize;
        private final List<List<Integer>> allCombinationIndexes;
        public CombinationSupport(int combinationSize, int collectionSize){
            this.combinationSize = combinationSize;
            this.collectionSize = collectionSize;
            this.allCombinationIndexes = new ArrayList<>();
            searchCombinationIndexes(new LinkedList<>());
        }
        private void searchCombinationIndexes(Deque<Integer> indexes){
            int initialIndex = indexes.isEmpty()?0:indexes.getLast() + 1;
            for(int i = initialIndex; i<collectionSize; i++){
                indexes.add(i);
                if(indexes.size()==combinationSize){
                    allCombinationIndexes.add(List.copyOf(indexes));
                }else{
                    searchCombinationIndexes(indexes);
                }
                indexes.removeLast();
            }
        }
        private List<List<Integer>> getAllCombinationIndexes(){
            return allCombinationIndexes;
        }
    }

    /**
     * 任意の数の要素を含むリストを返します。
     * @param elements 要素
     * @param <E> 要素の型
     * @return リスト
     */
    @SafeVarargs
    public static <E> OperableList<E> of(E...elements){
        var list = new OperableList<E>();
        if(elements.length>1){
            list.addAll(Arrays.asList(elements));
        }
        return list;
    }
}
