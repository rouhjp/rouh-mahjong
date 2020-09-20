package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;
import jp.rouh.util.OperableList;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public class CallPhaseContextContainer implements CallPhaseContext{
    private final List<Tile> tiles;
    private final Tile callTile;
    private final Set<List<Tile>> straightBases;
    private final Set<List<Tile>> tripleBases;
    private final List<Tile> quadBases;
    private final boolean win;
    private CallPhaseContextContainer(List<Tile> tiles, Tile callTile,
                                      Set<List<Tile>> straightBases,
                                      Set<List<Tile>> tripleBases,
                                      List<Tile> quadBases, boolean win){
        this.tiles = tiles;
        this.callTile = callTile;
        this.straightBases = straightBases;
        this.tripleBases = tripleBases;
        this.quadBases = quadBases;
        this.win = win;
    }
    @Override
    public List<Tile> getTiles(){
        return null;
    }
    @Override
    public Tile getCallTile(){
        return null;
    }
    @Override
    public boolean canCallStraight(){
        return !straightBases.isEmpty();
    }
    @Override
    public boolean canCallTriple(){
        return !tripleBases.isEmpty();
    }
    @Override
    public boolean canCallQuad(){
        return !quadBases.isEmpty();
    }
    @Override
    public boolean canCallWin(){
        return win;
    }
    @Override
    public boolean canCallStraight(Tile tile){
        return straightBases.stream().flatMap(List::stream).anyMatch(tile::equals);
    }
    @Override
    public boolean canCallTriple(Tile tile){
        return tripleBases.stream().flatMap(List::stream).anyMatch(tile::equals);
    }
    @Override
    public boolean canCallQuad(Tile tile){
        return quadBases.stream().anyMatch(tile::equals);
    }
    @Override
    public boolean canCallStraight(Tile tile, Tile selected){
        return straightBases.stream()
                .filter(target->target.contains(selected))
                .anyMatch(target->new OperableList<>()
                        .removed(selected).contains(tile));
    }
    @Override
    public boolean canCallTriple(Tile tile, Tile selected){
        return straightBases.stream()
                .filter(target->target.contains(selected))
                .anyMatch(target->new OperableList<>()
                        .removed(selected).contains(tile));
    }

    public static CallPhaseContext ofWinning(List<Tile> tiles, Tile callTile){
        return new CallPhaseContextContainer(tiles, callTile, emptySet(), emptySet(), emptyList(), true);
    }

    public static CallPhaseContext of(List<Tile> tiles, Tile callTile,
                                      Set<List<Tile>> straightBases,
                                      Set<List<Tile>> tripleBases,
                                      List<Tile> quadBases, boolean win){
        return new CallPhaseContextContainer(tiles, callTile, straightBases, tripleBases, quadBases, win);
    }
}
