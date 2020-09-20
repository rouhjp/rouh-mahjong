package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.score.WinningContext;
import jp.rouh.mahjong.score.WinningType;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.util.List;

class WinningContextFacade implements WinningContext{
    private final RoundPlayer player;
    private final FieldRound round;
    private final WinningType winningType;
    private final Tile winningTile;
    WinningContextFacade(RoundPlayer player, FieldRound round,
                         WinningType winningType, Tile winningTile){
        this.player = player;
        this.round = round;
        this.winningType = winningType;
        this.winningTile = winningTile;
    }
    @Override
    public Wind getRoundWind(){
        return round.getRoundWind();
    }
    @Override
    public Wind getSeatWind(){
        return player.getSeatWind();
    }
    @Override
    public boolean isDealer(){
        return player.getSeatWind()==Wind.EAST;
    }
    @Override
    public boolean isConcealed(){
        return player.isConcealed();
    }
    @Override
    public boolean isReady(){
        return player.isReady();
    }
    @Override
    public boolean isFirstAroundReady(){
        return player.isFirstAroundReady();
    }
    @Override
    public boolean isReadyAroundWin(){
        return player.isReadyAround();
    }
    @Override
    public boolean isFirstAroundWin(){
        return round.isFirstAround();
    }
    @Override
    public boolean isLastTileWin(){
        return round.isLastTurn();
    }
    @Override
    public WinningType getWinningType(){
        return winningType;
    }
    @Override
    public int getQuadCount(){
        return player.getQuadCount();
    }
    @Override
    public List<Tile> getOpenPrisedTiles(){
        return round.getUpperPrisedTiles();
    }
    @Override
    public List<Tile> getHiddenPrisedTiles(){
        return round.getLowerPrisedTiles();
    }
}
