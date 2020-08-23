package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;

import java.util.List;

/**
 * 面子を表すインターフェース。
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
    List<Tile> getTilesTruncated();

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
    boolean isStraight();

    /**
     * この面子が刻子であるか検査します。
     * @return true  刻子の場合
     *         false 刻子でない場合
     */
    boolean isTriple();

    /**
     * この面子が槓子であるか検査します。
     * @return true  槓子の場合
     *         false 槓子でない場合
     */
    boolean isQuad();

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
     * この面子が暗槓であるか検査します。
     * @return true  暗槓の場合
     *         false 暗槓でない場合
     */
    boolean isSelfQuad();

    /**
     * この面子が暗面子であるかどうか検査します。
     *
     * <p>暗槓はこの検査に適合します。ロンによって成立した刻子は適合しません。
     * @return true  暗面子の場合
     *         false 明面子の場合
     */
    boolean isConcealed();

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
    int getMeldBasicPoint();

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
    static Meld makeHandMeld(List<Tile> tiles){
        if(!Tiles.isStraight(tiles) && !Tiles.isTriple(tiles)){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(tiles, true);
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
    static Meld makeClaimedHandMeld(List<Tile> tiles){
        if(!Tiles.isStraight(tiles) && !Tiles.isTriple(tiles)){
            throw new IllegalArgumentException();
        }
        boolean concealed = !Tiles.isTriple(tiles);
        return new BaseMeld(tiles, concealed);
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
    static Meld callTriple(List<Tile> base, Tile called, Side source){
        if(base.size()!=2 || !Tiles.isTriple(List.of(base.get(0), base.get(1), called))){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(base, called, source);
    }

    /**
     * カンによって大明槓を生成します。
     * @param base 手牌中の構成牌のリスト(長さ3)
     * @param called 副露牌
     * @param source 副露元
     * @throws IllegalArgumentException 構成牌が槓子を構成し得ない場合
     * @return 明槓
     */
    static Meld callQuad(List<Tile> base, Tile called, Side source){
        if(base.size()!=3 || !Tiles.isQuad(List.of(base.get(0), base.get(1), base.get(2), called))){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(base, called, source);
    }

    /**
     * カンによって暗槓を生成します。
     * @param base 手牌中の構成牌のリスト(長さ4)
     * @throws IllegalArgumentException 構成牌が槓子を構成し得ない場合
     * @return 暗槓
     */
    static Meld makeSelfQuad(List<Tile> base){
        if(!Tiles.isQuad(base)){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(base, true);
    }

    /**
     * カンによって加槓を生成します。
     * @param triple 元となる明刻
     * @param tile 追加牌
     * @throws IllegalArgumentException 指定された面子が刻子でない場合
     *                                  追加牌と刻子が加槓を構成し得ない場合
     * @return 加槓
     */
    static Meld makeAddQuad(Meld triple, Tile tile){
        if(!triple.isTriple() || !triple.getFirst().equalsIgnoreRed(tile)){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(triple, tile);
    }

    /**
     * チーによって順子を生成します。
     * @param base 手牌中の構成牌のリスト(長さ2)
     * @param called 副露牌
     * @throws IllegalArgumentException 構成牌が順子を構成しない場合
     * @return 明順
     */
    static Meld callStraight(List<Tile> base, Tile called){
        if(base.size()!=2 || !Tiles.isStraight(List.of(base.get(0), base.get(1), called))){
            throw new IllegalArgumentException();
        }
        return new BaseMeld(base, called, Side.LEFT);
    }
}
