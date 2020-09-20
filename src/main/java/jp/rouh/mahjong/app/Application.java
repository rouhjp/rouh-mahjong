package jp.rouh.mahjong.app;

import jp.rouh.mahjong.table.Game;

public class Application{
    public static void main(String[] args){
        new Game(new User(new ConsoleViewer()),
                new Bot("Anna"),
                new Bot("Gina"),
                new Bot("Mina"));
    }
}
