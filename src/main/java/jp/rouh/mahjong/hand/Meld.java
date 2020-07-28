package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * 面子を表すインターフェース。
 *
 * <p>副露などの場に出た面子や, 得点計算の際の並べ替え時に利用されます。
 * TODO: write specific document
 * <table>
 * <tr><th>種類</th>
 *     <th>{@link #isConcealed}</th>
 *     <th>{@link #isCalled}</th>
 *     <th>{@link #isExposed}</th>
 *     <th>{@link #isAddQuad}</th></tr>
 * <tr><th>手牌中の刻子</th>
 *     <td>{@code true}</td>
 *     <td>{@code false}</td>
 *     <td>{@code false}</td>
 *     <td>{@code false}</td></tr>
 * <tr><th>手牌中のロンした刻子</th>
 *     <td>{@code false}</td>
 *     <td>{@code false}</td>
 *     <td>{@code false}</td>
 *     <td>{@code false}</td></tr>
 * <tr><th>ポンした刻子</th>
 *     <td>{@code false}</td>
 *     <td>{@code true}</td>
 *     <td>{@code true}</td>
 *     <td>{@code false}</td></tr>
 * <tr><th>暗槓</th>
 *     <td>{@code true}</td>
 *     <td>{@code false}</td>
 *     <td>{@code true}</td>
 *     <td>{@code false}</td></tr>
 * <tr><th>大明槓</th>
 *     <td>{@code false}</td>
 *     <td>{@code true}</td>
 *     <td>{@code true}</td>
 *     <td>{@code false}</td></tr>
 * <tr><th>加槓</th>
 *     <td>{@code false}</td>
 *     <td>{@code true}</td>
 *     <td>{@code true}</td>
 *     <td>{@code true}</td></tr>
 * </table>
 *
 * @see HandMeld
 * @see CalledMeld
 * @see SelfQuad
 * @see AddQuad
 *
 * @see HandComponent
 * @author Rouh
 * @version 1.0
 */
public interface Meld extends HandComponent{

    /**
     * 面子を構成する牌をグラフィック上の並びで取得します。
     *
     * <p>例えば, 手牌中の[2, 4]に対して[3]をチーして成立した順子に対し
     * このメソッドを呼び出した場合, 返されるリストは[3, 2, 4]の順になります。
     * また, 加槓された牌は最後に追加されます。
     * @return 面子構成牌のリスト
     */
    List<Tile> getTilesFormed();

    /**
     * {@inheritDoc}
     * @return 面子の構成牌のリスト
     */
    @Override
    List<Tile> getTilesSorted();

    /**
     * 面子から三枚の構成牌を取得します。
     *
     * <p>槓子の場合, 構成牌の一枚を無視した三枚の構成牌を返します。
     * @return 長さ3の面子構成牌のリスト
     */
    default List<Tile> getTilesTruncated(){
        return getTilesSorted().subList(0, 3);
    }

    /**
     * この面子が副露されたものである場合, 副露元の相対位置を返します。
     *
     * <p>副露されたものでない場合, {@code Side.SELF}を返します。
     * 暗槓の場合は, {@code Side.SELF}を返します。
     * 加槓の場合は, 元の明刻子の副露元の相対位置を返します。
     * @return 副露元相対位置 この面子が副露面子(暗槓を含まない)の場合
     *         SELF        この面子が副露面子でない場合
     */
    Side getSourceSide();

    /**
     * この面子が順子であるか検査します。
     * @return true  順子の場合
     *         false 順子でない場合
     */
    default boolean isStraight(){
        return getTilesSorted().size()==3 && !getLast().equalsIgnoreRed(getLast());
    }

    /**
     * この面子が刻子であるか検査します。
     * @return true  刻子の場合
     *         false 刻子でない場合
     */
    default boolean isTriple(){
        return getTilesSorted().size()==3 && getFirst().equalsIgnoreRed(getLast());
    }

    /**
     * この面子が槓子であるか検査します。
     * @return true  槓子の場合
     *         false 槓子でない場合
     */
    default boolean isQuad(){
        return getTilesSorted().size()==4;
    }

    /**
     * この面子が加槓であるか検査します。
     *
     * <p>加槓と大明槓は手牌の点数計算上の差異はありませんが,
     * 一般的にグラフィック上の違いを持つため,
     * ビューの実装が, 面子が加槓かどうか検査できるようこのメソッドが用意されています。
     * @return true  加槓の場合
     *         false 加槓でない場合
     */
    boolean isAddQuad();

    /**
     * この面子が暗面子であるかどうか検査します。
     *
     * <p>暗槓はこの検査に適合します。ロンによって成立した刻子は適合しません。
     * @return true  暗面子の場合
     *         false 明面子の場合
     */
    boolean isConcealed();

    /**
     * この面子が副露(他家からのポン, カン, チー)によって成立したかどうか検査します。
     *
     * <p>暗槓及びロンによって成立した刻子は適合しません。
     * @return true  副露牌の場合
     *         false 副露牌でない場合
     */
    boolean isCalled();

    /**
     * この面子が他家から見える面子であるかどうか検査します。
     *
     * <p>副露牌及び暗槓はこの検査に適合します。ロンによって成立した刻子は成立しません。
     * @return true  公開されている場合
     *         false 公開されていない場合
     */
    boolean isExposed();

    /**
     * {@inheritDoc}
     *
     * <p>面子の符は以下のように計算されます。
     * 順子の場合は0
     * 刻子の場合は以下の表に従う
     * <table>
     *     <tr><th></th><th>中張</th><th>么九</th></tr>
     *     <tr><th>明刻</th><td>2</td><td>4</td></tr>
     *     <tr><th>暗刻</th><td>4</td><td>8</td></tr>
     *     <tr><th>明槓</th><td>8</td><td>16</td></tr>
     *     <tr><th>暗槓</th><td>16</td><td>32</td></tr>
     * </table>
     * @return 面子の符
     */
    default int getMeldBasicPoint(){
        if(isStraight()) return 0;
        return 2*(isQuad()?4:1)*(isConcealed()?2:1)*(isTerminal()?2:1);
    }

    /**
     * 手牌の面子を作成するファクトリーメソッド。
     * @param tiles 構成牌
     * @return 暗刻または暗順
     */
    static Meld of(List<Tile> tiles){
        return new HandMeld(tiles, true);
    }

    /**
     * ロンした面子を作成するファクトリーメソッド。
     *
     * <p>指定した構成牌が刻子の構成牌の場合, 面子は明刻になります。
     * @param tiles 構成牌
     * @return 副露でない明刻, または暗順
     */
    static Meld ofGrabbed(List<Tile> tiles){
        return new HandMeld(tiles, Tiles.isTriple(tiles));
    }

    /**
     * 副露した面子を作成するファクトリーメソッド。
     * @param base 手牌中の構成牌
     * @param called 副露した牌
     * @param source 副露元の相対位置
     * @return 副露した明刻, 明順, 明槓
     */
    static Meld ofCalled(List<Tile> base, Tile called, Side source){
        var allTiles = new ArrayList<>(base);
        allTiles.add(called);
        return new CalledMeld(allTiles, called, source);
    }

    /**
     * 加槓を作成するファクトリーメソッド。
     * @param triple 副露した明刻
     * @param added 加えた牌
     * @return 加槓
     */
    static Meld ofAdded(CalledMeld triple, Tile added){
        var allTiles = new ArrayList<>(triple.getTilesSorted());
        allTiles.add(added);
        return new AddQuad(allTiles, triple.getCalledTile(), added, triple.getSourceSide());
    }

    /**
     * 暗槓を作成するファクトリーメソッド。
     * @param tiles 暗槓を構成する牌
     * @return 暗槓
     */
    static Meld ofSelfQuad(List<Tile> tiles){
        return new SelfQuad(tiles);
    }

}
