package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Tile;

import java.util.Objects;

public class TurnAction{
    private final TurnActionType type;
    private final Tile tile;
    public TurnAction(TurnActionType type, Tile tile){
        this.type = type;
        this.tile = tile;
    }
    public TurnActionType type(){
        return type;
    }
    public Tile tile(){
        if(type==TurnActionType.DECLARE_WIN || type==TurnActionType.DECLARE_DRAW){
            throw new IllegalStateException("must check action type before.");
        }
        return tile;
    }
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        TurnAction that = (TurnAction)o;
        return type==that.type && Objects.equals(tile, that.tile);
    }
    @Override
    public int hashCode(){
        return Objects.hash(type, tile);
    }
}
