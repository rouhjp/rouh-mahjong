package jp.rouh.mahjong.score;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 面子を表すクラス。
 *
 * <p>副露などの場に出た面子や, 得点計算の際の並べ替え時に利用されます。
 * <p>面子の生成は, 副露, 暗槓, 手牌からの並べ替えの3種類です。
 * このインターフェースではこれら全てに対応するファクトリーメソッドが
 * 提供されています。このうち, 手牌からの並べ替えによる生成は,
 * {@link #makeHandMeld}と{@link #makeClaimedHandMeld}
 * の２つのメソッドが提供されています。これは, ロン牌を含んだ刻子は
 * 明刻扱いになることに起因しています。ロン牌を含む面子を生成する際には
 * 後者を利用することで, 適切に明刻が生成されます。
 * <p>このインターフェースは, 得点計算上の要素のみならず, 副露や暗槓
 * といった公開された面子の描画のため, {@link #isAddQuad()}および
 * {@link #isSelfQuad()}メソッドが提供されています。
 *
 * @see HandComponent
 * @author Rouh
 * @version 1.0
 */
public class Meld implements HandComponent{
    private final List<Tile> sorted;
    private final List<Tile> formed;
    private final Side source;
    private final boolean concealed;
    private final boolean added;
    private Meld(List<Tile> tiles, boolean concealed){
        this.sorted = tiles.stream().sorted().collect(toList());
        this.formed = sorted;
        this.source = Side.SELF;
        this.concealed = concealed;
        this.added = false;
    }
    private Meld(List<Tile> base, Tile called, Side source){
        var formed = new ArrayList<>(base);
        int index;
        switch(source){
            case LEFT: index = 0; break;
            case ACROSS: index = 1; break;
            case RIGHT: index = base.size(); break;
            default: throw new IllegalArgumentException();
        }
        formed.add(index, called);
        this.formed = formed;
        this.sorted = formed.stream().sorted().collect(toList());
        this.source = source;
        this.concealed = false;
        this.added = false;
    }
    private Meld(Meld triple, Tile added){
        var formed = new ArrayList<>(triple.getTilesFormed());
        formed.add(added);
        this.formed = formed;
        this.sorted = formed.stream().sorted().collect(toList());
        this.source = triple.getSourceSide();
        this.concealed = false;
        this.added = true;
    }

    /**
     * 面子を構成する牌をグラフィック上の並びで取得します。
     *
     * <p>例えば, 手牌中の[2, 4]に対して[3]をチーして成立した順子に対し
     * このメソッドを呼び出した場合, 返されるリストは[3, 2, 4]の順になります。
     * また, 加槓された牌は最後に追加されます。
     * @return 面子構成牌のリスト
     */
    public List<Tile> getTilesFormed(){
        return formed;
    }

    /**
     * {@inheritDoc}
     * @return 面子の構成牌のリスト
     */
    @Override
    public List<Tile> getTilesSorted(){
        return sorted;
    }

    /**
     * 面子から三枚の構成牌を取得します。
     *
     * <p>槓子の場合, 構成牌の一枚を無視した三枚の構成牌を返します。
     * @return 長さ3の面子構成牌のリスト
     */
    public List<Tile> getTilesTruncated(){
        return sorted.subList(0, 3);
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
    public Side getSourceSide(){
        return source;
    }

    /**
     * この面子が順子であるか検査します。
     * @return true  順子の場合
     *         false 順子でない場合
     */
    public boolean isStraight(){
        return sorted.size()==3 && !getLast().equalsIgnoreRed(getLast());
    }

    /**
     * この面子が刻子であるか検査します。
     * @return true  刻子の場合
     *         false 刻子でない場合
     */
    public boolean isTriple(){
        return sorted.size()==3 && getFirst().equalsIgnoreRed(getLast());
    }

    /**
     * この面子が槓子であるか検査します。
     * @return true  槓子の場合
     *         false 槓子でない場合
     */
    public boolean isQuad(){
        return sorted.size()==4;
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
    @Deprecated
    public boolean isAddQuad(){
        return added;
    }

    /**
     * この面子が暗槓であるか検査します。
     * @return true  暗槓の場合
     *         false 暗槓でない場合
     */
    @Deprecated
    public boolean isSelfQuad(){
        return isQuad() && isConcealed();
    }

    /**
     * この面子が暗面子であるかどうか検査します。
     *
     * <p>暗槓はこの検査に適合します。ロンによって成立した刻子は適合しません。
     * @return true  暗面子の場合
     *         false 明面子の場合
     */
    public boolean isConcealed(){
        return concealed;
    }

    /**
     * この面子の符を計算し取得します。
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
    public int getMeldBasicPoint(){
        if(isStraight()) return 0;
        return 2*(isQuad()?4:1)*(isConcealed()?2:1)*(isTerminal()?2:1);
    }

    /**
     * 手牌から面子を生成します。
     *
     * <p>この操作で生成された面子は副露ではありません。
     * 手牌の点数計算に用いるため手牌の一部を面子として
     * 解釈する際に利用されます。
     * @param tiles 構成牌
     * @throws IllegalArgumentException 構成牌が刻子でも順子でもない場合
     * @return 暗刻または暗順
     */
    public static Meld makeHandMeld(List<Tile> tiles){
        if(!Tiles.isStraight(tiles) && !Tiles.isTriple(tiles)){
            throw new IllegalArgumentException();
        }
        return new Meld(tiles, true);
    }

    /**
     * 手牌からロンによる和了牌を含む面子を生成します。
     *
     * <p>指定した構成牌が刻子の構成牌の場合, 面子は明刻になります。
     * <p>この操作で生成された面子は副露ではありません。
     * 手牌の点数計算に用いるため手牌の一部を面子として
     * 解釈する際に利用されます。
     * @param tiles 構成牌
     * @throws IllegalArgumentException 構成牌が刻子でも順子でもない場合
     * @return 副露でない明刻, または暗順
     */
    public static Meld makeClaimedHandMeld(List<Tile> tiles){
        if(!Tiles.isStraight(tiles) && !Tiles.isTriple(tiles)){
            throw new IllegalArgumentException();
        }
        boolean concealed = !Tiles.isTriple(tiles);
        return new Meld(tiles, concealed);
    }

    /**
     * ポンによって明刻を生成します。
     * @param base 手牌中の構成牌のリスト(長さ2)
     * @param called 副露牌
     * @param source 副露元
     * @throws IllegalArgumentException 構成牌が刻子でない場合
     *                                  副露元に自家を指定した場合
     * @return 明刻
     */
    public static Meld callTriple(List<Tile> base, Tile called, Side source){
        if(base.size()!=2 || !Tiles.isTriple(List.of(base.get(0), base.get(1), called))){
            throw new IllegalArgumentException();
        }
        return new Meld(base, called, source);
    }

    /**
     * カンによって大明槓を生成します。
     * @param base 手牌中の構成牌のリスト(長さ3)
     * @param called 副露牌
     * @param source 副露元
     * @throws IllegalArgumentException 構成牌が槓子を構成し得ない場合
     * @return 明槓
     */
    public static Meld callQuad(List<Tile> base, Tile called, Side source){
        if(base.size()!=3 || !Tiles.isQuad(List.of(base.get(0), base.get(1), base.get(2), called))){
            throw new IllegalArgumentException();
        }
        return new Meld(base, called, source);
    }

    /**
     * カンによって暗槓を生成します。
     * @param tiles 手牌中の構成牌のリスト(長さ4)
     * @throws IllegalArgumentException 構成牌が槓子を構成し得ない場合
     * @return 暗槓
     */
    public static Meld makeSelfQuad(List<Tile> tiles){
        if(!Tiles.isQuad(tiles)){
            throw new IllegalArgumentException();
        }
        return new Meld(tiles, true);
    }

    /**
     * カンによって加槓を生成します。
     * @param triple 元となる明刻
     * @param tile 追加牌
     * @throws IllegalArgumentException 指定された面子が刻子でない場合
     *                                  追加牌と刻子が加槓を構成し得ない場合
     * @return 加槓
     */
    public static Meld makeAddQuad(Meld triple, Tile tile){
        if(!triple.isTriple() || !triple.getFirst().equalsIgnoreRed(tile)){
            throw new IllegalArgumentException();
        }
        return new Meld(triple, tile);
    }

    /**
     * チーによって順子を生成します。
     * @param base 手牌中の構成牌のリスト(長さ2)
     * @param called 副露牌
     * @throws IllegalArgumentException 構成牌が順子を構成しない場合
     * @return 明順
     */
    public static Meld callStraight(List<Tile> base, Tile called){
        if(base.size()!=2 || !Tiles.isStraight(List.of(base.get(0), base.get(1), called))){
            throw new IllegalArgumentException();
        }
        return new Meld(base, called, Side.LEFT);
    }
}
