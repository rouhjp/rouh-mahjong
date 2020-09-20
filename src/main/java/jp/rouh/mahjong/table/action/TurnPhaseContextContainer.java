package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

public class TurnPhaseContextContainer implements TurnPhaseContext{
    private final List<Tile> tiles;
    private final Set<Tile> readyTiles;
    private final Set<Tile> addQuadTiles;
    private final Set<Tile> selfQuadTiles;
    private final Set<Tile> callShiftTiles;
    private final boolean win;
    private final boolean draw;
    private TurnPhaseContextContainer(List<Tile> tiles,
                                      Set<Tile> readyTiles,
                                      Set<Tile> addQuadTiles,
                                      Set<Tile> selfQuadTiles,
                                      Set<Tile> callShiftTiles,
                                      boolean win, boolean draw){
        this.tiles = tiles;
        this.readyTiles = readyTiles;
        this.addQuadTiles = addQuadTiles;
        this.selfQuadTiles = selfQuadTiles;
        this.callShiftTiles = callShiftTiles;
        this.win = win;
        this.draw = draw;
    }
    @Override
    public List<Tile> getTiles(){
        return tiles;
    }
    @Override
    public boolean canDeclareDraw(){
        return draw;
    }
    @Override
    public boolean canDeclareWin(){
        return win;
    }
    @Override
    public boolean canDeclareReady(){
        return !readyTiles.isEmpty();
    }
    @Override
    public boolean canDeclareQuad(){
        return !addQuadTiles.isEmpty() || !selfQuadTiles.isEmpty();
    }
    @Override
    public boolean canDiscard(Tile tile){
        return !callShiftTiles.contains(tile);
    }
    @Override
    public boolean canDeclareReady(Tile tile){
        return readyTiles.contains(tile);
    }
    @Override
    public boolean canDeclareQuad(Tile tile){
        return canDeclareAddQuad(tile) || canDeclareSelfQuad(tile);
    }
    @Override
    public boolean canDeclareAddQuad(Tile tile){
        return addQuadTiles.contains(tile);
    }
    @Override
    public boolean canDeclareSelfQuad(Tile tile){
        return selfQuadTiles.contains(tile);
    }
    public static TurnPhaseContext of(List<Tile> tiles, Set<Tile> callShiftTiles){
        return new TurnPhaseContextContainer(tiles,
                emptySet(), emptySet(), emptySet(),
                callShiftTiles, false, false);
    }

    public static TurnPhaseContext of(List<Tile> tiles, Set<Tile> readyTiles, Set<Tile> addQuadTiles,
                                      Set<Tile> selfQuadTiles, boolean win, boolean draw){
        return new TurnPhaseContextContainer(tiles, readyTiles, addQuadTiles, selfQuadTiles, emptySet(), win, draw);
    }
}
