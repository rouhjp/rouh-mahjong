package jp.rouh.mahjong.app;

import jp.rouh.mahjong.app.bitv.BlockImageTableViewer;
import jp.rouh.mahjong.table.Game;
import jp.rouh.mahjong.table.impl.Bot;
import jp.rouh.mahjong.table.impl.User;

public class Application{
    public static void main(String[] args){
        new Game(new User(new BlockImageTableViewer()),
                new Bot("Anna"),
                new Bot("Gina"),
                new Bot("Mina"));
    }
}
