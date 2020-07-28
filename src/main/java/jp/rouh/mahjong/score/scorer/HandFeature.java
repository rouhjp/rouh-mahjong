package jp.rouh.mahjong.score.scorer;

import jp.rouh.mahjong.hand.FormattedHand;
import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.tile.BaseTile;
import jp.rouh.mahjong.tile.Tile;

import java.util.List;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

/**
 * 手牌の特徴量を表すクラス
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
    public HandFeature(FormattedHand hand, WinningContext context){
        var fullTiles = hand.getTilesSorted();
        Tile roundWindTile = context.getRoundWind().toTile();
        Tile seatWindTile = context.getSeatWind().toTile();
        Tile winningTile = context.getWinningTile();
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
        for(var tile:hand.getTilesTruncated()){
            if(tile==BaseTile.DW) dragonWhiteCount++;
            if(tile==BaseTile.DG) dragonGreenCount++;
            if(tile==BaseTile.DR) dragonRedCount++;
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
        this.largestDuplicationCount = fullTiles.stream()
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

    public int getDragonWhiteCount(){
        return dragonWhiteCount;
    }
    public int getDragonGreenCount(){
        return dragonGreenCount;
    }
    public int getDragonRedCount(){
        return dragonRedCount;
    }
    public int getDragonCount(){
        return dragonCount;
    }
    public int getWindCount(){
        return windCount;
    }
    public int getRoundWindCount(){
        return roundWindCount;
    }
    public int getSeatWindCount(){
        return seatWindCount;
    }
    public int getWinningTileCount(){
        return winningTileCount;
    }
    public int getTerminalCount(){
        return terminalCount;
    }
    public int getHonorCount(){
        return honorCount;
    }
    public int getOrphanCount(){
        return orphanCount;
    }
    public int getGreenTileCount(){
        return greenTileCount;
    }
    public int getLargestDuplicationCount(){
        return largestDuplicationCount;
    }
    public int getTileKind(){
        return tileKind;
    }
    public int getSuitTypeKind(){
        return suitTypeKind;
    }
    public int getOpenPrisedTileCount(){
        return openPrisedTileCount;
    }
    public int getHiddenPrisedTileCount(){
        return hiddenPrisedTileCount;
    }
    public int getRedPrisedTileCount(){
        return redPrisedTileCount;
    }
}
