package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;

import java.util.List;

public class CallAction{
    private final Type type;
    private final List<Tile> tiles;
    private enum Type{

        /** ロン */
        DECLARE_WIN(3),

        /** カン(大明槓) */
        CALL_QUAD(2),

        /** ポン */
        CALL_TRIPLE(2),

        /** チー */
        CALL_STRAIGHT(1),

        /** パス */
        PASS(0);

        private final int priority;
        Type(int priority){
            this.priority = priority;
        }
    }
    private CallAction(Type type, List<Tile> tiles){
        this.type = type;
        this.tiles = tiles;
    }

    public boolean isRiverGrabWinDeclaration(){
        return type==Type.DECLARE_WIN;
    }

    public boolean isMeldCallDeclaration(){
        return isQuadCallDeclaration()
                || isTripleCallDeclaration()
                || isStraightCallDeclaration();
    }

    public boolean isQuadCallDeclaration(){
        return type==Type.CALL_QUAD;
    }
    public boolean isTripleCallDeclaration(){
        return type==Type.CALL_TRIPLE;
    }
    public boolean isStraightCallDeclaration(){
        return type==Type.CALL_STRAIGHT;
    }

    public int getPriority(){
        return type.priority;
    }

    public List<Tile> getSelectedTiles(){
        return tiles;
    }

    public static CallAction ofStraight(List<Tile> base){
        return new CallAction(Type.CALL_STRAIGHT, base);
    }

    public static CallAction ofTriple(List<Tile> base){
        return new CallAction(Type.CALL_TRIPLE, base);
    }

    public static CallAction ofQuad(List<Tile> base){
        return new CallAction(Type.CALL_QUAD, base);
    }

    public static CallAction ofWin(){
        return new CallAction(Type.DECLARE_WIN, null);
    }

    public static CallAction ofPass(){
        return new CallAction(Type.PASS, null);
    }
}
