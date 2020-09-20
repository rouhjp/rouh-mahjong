package jp.rouh.mahjong.score;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.util.OperableList;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * 和了時の手牌を表すクラス。
 * @author Rouh
 * @version 1.0
 */
public class WinningHand{
    private final List<Tile> handTiles;
    private final List<Meld> openMelds;
    private final Tile winningTile;
    private final boolean selfDraw;

    /**
     * 和了時の手牌を生成します。
     * @throws IllegalArgumentException 牌の数が不正の場合
     * @throws IllegalArgumentException 手牌が完成形でない場合
     * @param winningTile 和了牌
     * @param handTiles 和了時の門前手牌
     * @param openMelds 和了時の公開面子
     * @param selfDraw ツモ(嶺上自摸を含む)による和了かどうか
     */
    private WinningHand(Tile winningTile, List<Tile> handTiles, List<Meld> openMelds, boolean selfDraw){
        if(handTiles.size() + openMelds.size()*3!=13){
            throw new IllegalArgumentException("illegal size of hand tiles");
        }
        if(!HandTiles.isCompleted(handTiles, winningTile)){
            throw new IllegalArgumentException("non completed hand tiles");
        }
        this.handTiles = OperableList.copyOf(handTiles).added(winningTile).sorted().toUnmodifiable();
        this.openMelds = List.copyOf(openMelds);
        this.winningTile = winningTile;
        this.selfDraw = selfDraw;
    }

    /**
     * 手牌を整形して整形済み手牌{@link FormattedHand}のセットを取得します。
     * <p>手牌は複数の並べ替えパターン
     *
     * @throws NoSuchElementException フォーマット不可能な場合
     * @return 整形済み手牌のセット
     */
    public Set<FormattedHand> format(){
        var hands = new HashSet<FormattedHand>();
        if(HandTiles.isSevenPairs(handTiles, winningTile)){
            hands.add(new SevenPairsHand(handTiles, winningTile));
        }
        if(HandTiles.isThirteenOrphans(handTiles, winningTile)){
            hands.add(new ThirteenOrphansHand(handTiles, winningTile));
        }
        for(var pattern: HandTiles.arrange(handTiles, winningTile)){
            var head = new Head(pattern.get(0));
            var tailPattern = pattern.subList(1, pattern.size());
            for(var meldTiles:tailPattern){
                var melds = OperableList.copyOf(tailPattern).removed(meldTiles)
                        .stream().map(Meld::makeHandMeld).collect(toList());
                var winningMeld = selfDraw? Meld.makeHandMeld(meldTiles):Meld.makeClaimedHandMeld(meldTiles);
                melds.add(winningMeld);
                melds.addAll(openMelds);
                var wait = Wait.of(winningMeld, winningTile);
                hands.add(new MeldHand(head, melds, wait));
            }
        }
        if(hands.isEmpty()){
            throw new NoSuchElementException();
        }
        return hands;
    }

    /**
     * 和了牌を取得します。
     * @return 和了牌
     */
    public Tile getWinningTile(){
        return winningTile;
    }

    /**
     * 和了牌を加えたソート済みの門前手牌を新たな可変リストとして取得します。
     * @return 門前手牌の可変リスト
     */
    public List<Tile> getCompletedHandTiles(){
        return new OperableList<>(handTiles).added(winningTile);
    }

    /**
     * 公開面子の不変リストを取得します。
     * @return 公開面子の不変リスト
     */
    public List<Meld> getOpenMelds(){
        return openMelds;
    }

    /**
     * ツモによって成立した和了手牌を生成します。
     * @param winningTile ツモ和了牌
     * @param handTiles 門前手牌
     * @param openMelds 公開面子
     * @return 和了時の手牌
     */
    public static WinningHand ofDraw(Tile winningTile, List<Tile> handTiles, List<Meld> openMelds){
        return new WinningHand(winningTile, handTiles, openMelds, true);
    }

    /**
     * ロンによって成立した和了手牌を生成します。
     * @param winningTile ロン和了牌
     * @param handTiles 門前手牌
     * @param openMelds 公開面子
     * @return 和了時の手牌
     */
    public static WinningHand ofGrab(Tile winningTile, List<Tile> handTiles, List<Meld> openMelds){
        return new WinningHand(winningTile, handTiles, openMelds, false);
    }

    private static class MeldHand implements FormattedHand{
        private final Head head;
        private final List<Meld> melds;
        private final Wait wait;
        private MeldHand(Head head, List<Meld> melds, Wait wait){
            this.head = head;
            this.melds = List.copyOf(melds);
            this.wait = wait;
        }

        @Override
        public boolean isMeldHand(){
            return true;
        }

        @Override
        public boolean isSevenPairsHand(){
            return false;
        }

        @Override
        public boolean isThirteenOrphansHand(){
            return false;
        }

        @Override
        public Head getHead(){
            return head;
        }

        @Override
        public List<Meld> getMelds(){
            return melds;
        }

        @Override
        public List<HandComponent> getComponents(){
            return OperableList.<HandComponent>copyOf(melds).added(head);
        }

        @Override
        public List<Tile> getTilesSorted(){
            return getComponents().stream()
                    .map(HandComponent::getTilesSorted)
                    .flatMap(List::stream)
                    .sorted()
                    .collect(toList());
        }

        @Override
        public List<Tile> getTilesTruncated(){
            return getMelds().stream()
                    .map(Meld::getTilesTruncated)
                    .flatMap(List::stream)
                    .collect(toCollection(OperableList::new))
                    .addedAll(head.getTilesSorted())
                    .sorted();
        }

        @Override
        public Wait getWait(){
            return wait;
        }
    }

    private static abstract class NonMeldHand implements FormattedHand{
        private final List<Tile> completedTiles;
        private NonMeldHand(List<Tile> completedTiles){
            this.completedTiles = List.copyOf(completedTiles);
        }

        @Override
        public boolean isMeldHand(){
            return false;
        }

        @Override
        public abstract boolean isSevenPairsHand();

        @Override
        public abstract boolean isThirteenOrphansHand();

        @Override
        public Head getHead(){
            throw new UnsupportedOperationException("no head in irregular hand");
        }

        @Override
        public List<Meld> getMelds(){
            throw new UnsupportedOperationException("no melds in irregular hand");
        }

        @Override
        public List<HandComponent> getComponents(){
            throw new UnsupportedOperationException("no components in irregular hand");
        }

        @Override
        public List<Tile> getTilesSorted(){
            return completedTiles;
        }

        @Override
        public List<Tile> getTilesTruncated(){
            return completedTiles;
        }

        @Override
        public Wait getWait(){
            return Wait.SINGLE_HEAD;
        }
    }

    private static class SevenPairsHand extends NonMeldHand implements FormattedHand{
        private SevenPairsHand(List<Tile> handTiles, Tile winningTile){
            super(OperableList.copyOf(handTiles).added(winningTile));
        }
        @Override
        public boolean isSevenPairsHand(){
            return true;
        }
        @Override
        public boolean isThirteenOrphansHand(){
            return false;
        }
    }

    private static class ThirteenOrphansHand extends NonMeldHand implements FormattedHand{
        private ThirteenOrphansHand(List<Tile> handTiles, Tile winningTile){
            super(OperableList.copyOf(handTiles).added(winningTile));
        }
        @Override
        public boolean isSevenPairsHand(){
            return false;
        }
        @Override
        public boolean isThirteenOrphansHand(){
            return true;
        }
    }
}
