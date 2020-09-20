package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.Meld;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningHand;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 手牌の特徴量を表すクラス。
 * @author Rouh
 * @version 1.0
 */
public class HandFeature{
    private final int dragonWhiteCount;
    private final int dragonGreenCount;
    private final int dragonRedCount;
    private final int dragonCount;
    private final int windCount;
    private final int roundWindCount;
    private final int seatWindCount;
    private final int winningTileCount;
    private final int terminalCount;
    private final int honorCount;
    private final int orphanCount;
    private final int greenTileCount;
    private final int largestDuplicationCount;
    private final int tileKind;
    private final int suitTypeKind;
    private final int openPrisedTileCount;
    private final int hiddenPrisedTileCount;
    private final int redPrisedTileCount;
    public HandFeature(WinningHand hand, WinningContext context){
        var fullTiles = Stream.concat(hand.getCompletedHandTiles().stream(),
                hand.getOpenMelds().stream().map(Meld::getTilesSorted).flatMap(List::stream))
                .collect(toList());
        var fourteenTiles = Stream.concat(hand.getCompletedHandTiles().stream(),
                hand.getOpenMelds().stream().map(Meld::getTilesTruncated).flatMap(List::stream))
                .collect(toList());
        Tile roundWindTile = context.getRoundWind().toTile();
        Tile seatWindTile = context.getSeatWind().toTile();
        Tile winningTile = hand.getWinningTile();
        int dragonWhiteCount = 0;
        int dragonGreenCount = 0;
        int dragonRedCount = 0;
        int dragonCount = 0;
        int windCount = 0;
        int roundWindCount = 0;
        int seatWindCount = 0;
        int winningTileCount = 0;
        int terminalCount = 0;
        int honorCount = 0;
        int orphanCount = 0;
        int greenTileCount = 0;
        for(var tile:fourteenTiles){
            if(tile==Tile.DW) dragonWhiteCount++;
            if(tile==Tile.DG) dragonGreenCount++;
            if(tile==Tile.DR) dragonRedCount++;
            if(tile.isDragon()) dragonCount++;
            if(tile.isWind()) windCount++;
            if(tile.equalsIgnoreRed(roundWindTile)) roundWindCount++;
            if(tile.equalsIgnoreRed(seatWindTile)) seatWindCount++;
            if(tile.equalsIgnoreRed(winningTile)) winningTileCount++;
            if(tile.isTerminal()) terminalCount++;
            if(tile.isHonor()) honorCount++;
            if(tile.isOrphan()) orphanCount++;
            if(tile.isGreen()) greenTileCount++;
        }
        this.dragonWhiteCount = dragonWhiteCount;
        this.dragonGreenCount = dragonGreenCount;
        this.dragonRedCount = dragonRedCount;
        this.dragonCount = dragonCount;
        this.windCount = windCount;
        this.roundWindCount = roundWindCount;
        this.seatWindCount = seatWindCount;
        this.winningTileCount = winningTileCount;
        this.terminalCount = terminalCount;
        this.honorCount = honorCount;
        this.orphanCount = orphanCount;
        this.greenTileCount = greenTileCount;
        this.largestDuplicationCount = fourteenTiles.stream()
                .collect(groupingBy(Tile::tileNumber)).values().stream()
                .mapToInt(List::size).max().orElseThrow();
        this.tileKind = (int)fullTiles.stream()
                .mapToInt(Tile::tileNumber).distinct().count();
        this.suitTypeKind = fullTiles.stream().filter(not(Tile::isHonor))
                .collect(groupingBy(Tile::tileType)).size();
        this.openPrisedTileCount = count(fullTiles, context.getOpenPrisedTiles());
        this.hiddenPrisedTileCount = count(fullTiles, context.getHiddenPrisedTiles());
        this.redPrisedTileCount = (int)fullTiles.stream()
                .filter(Tile::isPrisedRed).count();
    }
    private static int count(List<Tile> tiles, List<Tile> prisedTiles){
        var count = 0;
        for(var prisedTile:prisedTiles){
            for(var tile:tiles){
                if(tile.equalsIgnoreRed(prisedTile)){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 手牌14枚中の白の枚数を取得します。
     *
     * <p>白の槓子は白3枚として計上します。
     * @return 白の枚数
     */
    public int getDragonWhiteCount(){
        return dragonWhiteCount;
    }

    /**
     * 手牌14枚中の發の枚数を取得します。
     *
     * <p>發の槓子は發3枚として計上します。
     * @return 發の枚数
     */
    public int getDragonGreenCount(){
        return dragonGreenCount;
    }

    /**
     * 手牌14枚中の中の枚数を取得します。
     *
     * <p>中の槓子は中3枚として計上します。
     * @return 中の枚数
     */
    public int getDragonRedCount(){
        return dragonRedCount;
    }

    /**
     * 手牌14枚中の三元牌の枚数を取得します。
     *
     * <p>三元牌の槓子は三元牌3枚として計上します。
     * @return 三元牌の枚数
     */
    public int getDragonCount(){
        return dragonCount;
    }

    /**
     * 手牌14枚中の風牌の枚数を取得します。
     *
     * <p>風牌の槓子は風牌3枚として計上します。
     * @return 風牌の枚数
     */
    public int getWindCount(){
        return windCount;
    }

    /**
     * 手牌14枚中の場風牌の枚数を取得します。
     *
     * <p>場風牌の槓子は場風牌3枚として計上します。
     * @return 場風牌の枚数
     */
    public int getRoundWindCount(){
        return roundWindCount;
    }

    /**
     * 手牌14枚中の自風牌の枚数を取得します。
     *
     * <p>自風牌の槓子は自風牌3枚として計上します。
     * @return 自風牌の枚数
     */
    public int getSeatWindCount(){
        return seatWindCount;
    }

    /**
     * 手牌14枚中の和了牌と同種の牌の枚数を取得します。
     *
     * <p>和了牌1枚は必ず計上されるため, 結果枚数は1以上の値になります。
     * <p>赤ドラ牌と非赤ドラ牌の区別なく検査されます。
     * @return 和了牌と同種の牌の枚数
     */
    public int getWinningTileCount(){
        return winningTileCount;
    }

    /**
     * 手牌14枚中の老頭牌の枚数を取得します。
     *
     * <p>老頭牌の槓子は老頭牌3枚として計上します。
     * @return 老頭牌の枚数
     */
    public int getTerminalCount(){
        return terminalCount;
    }

    /**
     * 手牌14枚中の字牌の枚数を取得します。
     *
     * <p>字牌の槓子は字牌3枚として計上します。
     * @return 字牌の枚数
     */
    public int getHonorCount(){
        return honorCount;
    }

    /**
     * 手牌14枚中の么九牌の枚数を取得します。
     *
     * <p>么九牌の槓子は么九牌3枚として計上します。
     * @return 么九牌の枚数
     */
    public int getOrphanCount(){
        return orphanCount;
    }

    /**
     * 手牌14枚中の緑一色構成牌の枚数を取得します。
     *
     * <p>緑一色構成牌の槓子は緑一色構成牌3枚として計上します。
     * @return 緑一色構成牌の枚数
     */
    public int getGreenTileCount(){
        return greenTileCount;
    }

    /**
     * 手牌14枚中の重複のある牌のうち, 最も多い重複の枚数を取得します。
     *
     * <p>槓子は3枚の重複として計上されます。
     * 門前手牌中に4枚の重複のある牌がある場合(未槓子)は4枚と計上されます。
     * <p>赤ドラ牌と非赤ドラ牌の区別なく検査されます。
     * @return 最も多い重複の枚数
     */
    public int getLargestDuplicationCount(){
        return largestDuplicationCount;
    }

    /**
     * 手牌14枚中に使われた牌の種類の数を取得します。
     *
     * <p>赤ドラ牌と非赤ドラ牌の区別なく検査されます。
     * @return 牌の種類の数
     */
    public int getTileKind(){
        return tileKind;
    }

    /**
     * 手牌14枚中に使われた数牌の種類(萬子/筒子/索子)の数を取得します。
     *
     * <p>萬子/筒子/索子全てを含む場合は3,
     * 萬子/筒子/索子のうち2種類を含む場合は2,
     * 萬子/筒子/索子のうち1種類を含む場合は1,
     * 字牌のみの場合は0が返されます。
     * @return 数牌の種類(萬子/筒子/索子)の数
     */
    public int getSuitTypeKind(){
        return suitTypeKind;
    }

    /**
     * 手牌中の表ドラの数を取得します。
     * @return 表ドラの数
     */
    public int getOpenPrisedTileCount(){
        return openPrisedTileCount;
    }

    /**
     * 手牌中の裏ドラの数を取得します。
     * @return 裏ドラの数
     */
    public int getHiddenPrisedTileCount(){
        return hiddenPrisedTileCount;
    }

    /**
     * 手牌中の赤ドラの数を取得します。
     * @return 赤ドラの数
     */
    public int getRedPrisedTileCount(){
        return redPrisedTileCount;
    }
}
