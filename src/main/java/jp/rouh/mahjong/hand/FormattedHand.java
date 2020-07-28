package jp.rouh.mahjong.hand;

import jp.rouh.mahjong.tile.HandFunctions;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.util.OperableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * 得点計算のため面子構成及び待ちの解釈を一つに限定した整形済み手牌を表すインターフェース。
 *
 * <p>麻雀のルール上, 和了時の14枚の手牌に対して必ずしも面子の並べ替え方が一つ
 * に対応するとは限りません。例えば, 以下のような和了時の14枚の手牌を想定します。
 * <ul><li>[2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8]</li></ul>
 * この手は大車輪と呼ばれる形で, 雀頭のとり方によって以下の３つの並べ替えが可能です。
 * <ul><li>[2, 2][3, 4, 5][3, 4, 5][6, 7, 8][6, 7, 8]</li>
 *     <li>[5, 5][2, 3, 4][2, 3, 4][6, 7, 8][6, 7, 8]</li>
 *     <li>[8, 8][2, 3, 4][2, 3, 4][5, 6, 7][5, 6, 7]</li></ul>
 * また, この手は面子手の解釈ができると同時に, 七対子形としても解釈が可能です。
 * <ul><li>[2, 2][3, 3][4, 4][5, 5][6, 6][7, 7][8, 8]</li></ul>
 *
 * <p>他にも,以下のような和了時の14枚の手牌を想定します。
 * <ul><li>[1, 1, 1, 2, 2, 2, 3, 3, 3, 7, 8, 9, 9, 9]</li></ul>
 * この手は三連刻と呼ばれる形で, 面子の解釈方法は以下の二通り存在します。
 * <ul><li>[9, 9][1, 1, 1][2, 2, 2][3, 3, 3][7, 8, 9]</li>
 *     <li>[9, 9][1, 2, 3][1, 2, 3][1, 2, 3][7, 8, 9]</li></ul>
 * １つ目の並べ替えパターンでは(刻子部分が暗刻であれば)三暗刻が付与されます。
 * 一方２つ目の並べ替えパターンでは一盃口および混全帯么九が付与されます。
 *
 * <p>このように, 麻雀には面子構成に依存する役が存在するため, 役判定を行うには
 * まず手牌の面子構成を確定させる必要があり, 全ての並べ替えパターンに対応する
 * 整形済み手牌{@link FormattedHand}のインスタンスが必要です。
 *
 * <p>また, ロン牌によって成立した刻子は明刻扱いになるため,
 * 面子{@link Meld}を確定するには, 和了牌と和了方法(ロン/ツモ)を
 * 把握する必要があります。例えば, 以下の手牌を想定します。
 * <ul><li>[1, 1, 1, 1, 2, 3, 東, 東, 東, 南, 南, 南, 白, 白]</li></ul>
 * この手牌は以下のように面子構成が解釈されます。
 * <ul><li>[白, 白][1, 1, 1][1, 2, 3][東, 東, 東][南, 南, 南]</li></ul>
 * この時この手牌が[1]をロンして成立した場合, 刻子の[1, 1, 1]がロン牌が構成する面子
 * となるパターンと, 順子の[1, 2, 3]がロン牌を構成する面子となるパターンの
 * ２つの解釈が生まれます。前者の場合は刻子が明刻扱いとなるため三暗刻がつきません。
 * このように, 和了牌(特にロン牌)が構成する面子, もしくは雀頭を確定させる必要があります。
 *
 * <p>この操作は, 同時に手牌の待ち{@link Wait}を限定することになります。
 * 待ちは符の計算や平和の判定などに影響を及ぼすため、複数の解釈パターンが存在する場合
 * 全ての待ちパターンに対応する整形済み手牌{@link FormattedHand}のインスタンスが
 * 必要になります。先の手牌の例では, ロン牌が刻子の[1, 1, 1]を構成したと解釈した場合は
 * 対子[1, 1]に対する双碰待ち, ロン牌が順子の[1, 2, 3]を構成したと解釈した場合は
 * 塔子[2, 3]に対する両面待ちといったように待ちを解釈することが可能です。
 *
 * @author Rouh
 * @version 1.0
 */
public interface FormattedHand{
    /**
     * 手牌が面子形かどうか検査します。
     * @return true  面子形の場合
     *         false 七対子形か国士無双形の場合
     */
    boolean isMeldHand();

    /**
     * 手牌が七対子形かどうか検査します。
     * この検査に適合した整形済み手牌は必ず七対子役が付与されます。
     * @return true  七対子形の場合
     *         false 面子形か国士無双形の場合
     */
    boolean isSevenPairsHand();

    /**
     * 手牌が国士無双形がどうか検査します。
     * この検査に適合した整形済み手牌は必ず国士無双もしくは国士無双十三面が付与されます。
     * @return true  国士無双形の場合
     *         false 面子形か七対子形の場合
     */
    boolean isThirteenOrphansHand();

    /**
     * 手牌の雀頭を返します。<br>
     * ただし手牌が七対子や国士無双といった特殊形の場合例外をスローします。<br>
     * 例外の発生を防ぐためには, {@link #isMeldHand()}で事前検査する必要があります。
     * @throws UnsupportedOperationException 面子形ではない場合
     * @return 雀頭
     */
    Head getHead();

    /**
     * 手牌から面子を取得します。<br>
     * ただし手牌が七対子や国士無双といった特殊形の場合例外をスローします。<br>
     * 例外の発生を防ぐためには, {@link #isMeldHand()}で事前検査する必要があります。
     * @throws UnsupportedOperationException 面子形ではない場合
     * @return 面子
     */
    List<Meld> getMelds();

    /**
     * 手牌のうち副露または暗槓されていない面子を返します。
     * ただし手牌が七対子や国士無双といった特殊形の場合例外をスローします。<br>
     * 例外の発生を防ぐためには, {@link #isMeldHand()}で事前検査する必要があります。
     * @throws UnsupportedOperationException 面子形ではない場合
     * @return 公開されていない面子
     */
    List<Meld> getHandMelds();

    /**
     * 手牌のうち副露または暗槓された面子を返します。
     * ただし手牌が七対子や国士無双といった特殊形の場合例外をスローします。<br>
     * 例外の発生を防ぐためには, {@link #isMeldHand()}で事前検査する必要があります。
     * @throws UnsupportedOperationException 面子形ではない場合
     * @return 公開された面子
     */
    List<Meld> getOpenMelds();

    /**
     * 手牌の構成要素をリストとして返します。<br>
     * @throws UnsupportedOperationException 面子形ではない場合
     * @return 手牌構成要素のリスト
     */
    List<HandComponent> getComponents();

    /**
     * 手牌の全構成牌をソートした状態で取得します。<br>
     * 手牌に槓子がある場合, 4枚の構成牌全てが結果のリストに格納されます。<br>
     * @return 構成牌のリスト(長さ14..18)
     */
    List<Tile> getTilesSorted();

    /**
     * 手牌から14枚の牌をリスト形式で取得します。<br>
     * 手牌に槓子が含まれる場合, 合計の手牌が14枚になるよう<br>
     * 各槓子の構成牌の一枚を切り捨てます。<br>
     * @return 構成牌のリスト(長さ14)
     */
    List<Tile> getTilesTruncated();

    /**
     * この手牌の待ちの種類を返します。<br>
     * このて牌が七対子または国士無双の場合, 単騎待ちを返します。
     * @return 待ちの種類 面子形の場合
     *         単騎待ち   特殊形の場合
     */
    Wait getWait();

    /**
     * 全ての手牌の面子構成及び待ちパターンに対応する,
     * 整形済みの手牌{@link FormattedHand}のセットを取得します。
     * @param handTiles 手牌中の牌(和了牌を含む)
     * @param openMelds 公開された面子
     * @param winningTile 和了牌
     * @param selfDraw ツモ和了かどうか
     * @return 全ての整形済み手牌のセット
     */
    static Set<FormattedHand> of(List<Tile> handTiles, List<Meld> openMelds,
                                 Tile winningTile, boolean selfDraw){
        var hands = new HashSet<FormattedHand>();
        IrregularHand.tryGetSevenPairs(handTiles).ifPresent(hands::add);
        IrregularHand.tryGetThirteenOrphans(handTiles).ifPresent(hands::add);
        for(var pattern: HandFunctions.arrange(handTiles)){
            var head = new Head(pattern.get(0));
            var meldTilesList = pattern.subList(1, pattern.size());
            for(var meldTiles:meldTilesList){
                var melds = new OperableList<>(meldTilesList)
                        .removed(meldTiles)
                        .stream()
                        .map(Meld::of)
                        .collect(toList());
                var winningMeld = selfDraw?
                        Meld.of(meldTiles):
                        Meld.ofGrabbed(meldTiles);
                melds.add(winningMeld);
                var wait = Wait.of(winningMeld, winningTile);
                hands.add(new MeldHand(head, melds, openMelds, wait));
            }
        }
        return hands;
    }
}
