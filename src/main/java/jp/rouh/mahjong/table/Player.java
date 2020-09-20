package jp.rouh.mahjong.table;

public class Player{
    private final String name;
    private final TableStrategy viewer;
    public Player(String name, TableStrategy viewer){
        this.name = name;
        this.viewer = viewer;
    }
    public String getName(){
        return name;
    }
    public TableStrategy getViewer(){
        return viewer;
    }
}
