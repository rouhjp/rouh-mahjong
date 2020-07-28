package jp.rouh.mahjong.table.impl;

import jp.rouh.mahjong.table.Player;
import jp.rouh.mahjong.table.TableStrategy;

public class User extends Player{
    public User(TableStrategy strategy){
        super("you", strategy);
    }
}
