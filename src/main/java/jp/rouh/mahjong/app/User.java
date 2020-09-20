package jp.rouh.mahjong.app;

import jp.rouh.mahjong.table.Player;
import jp.rouh.mahjong.table.TableStrategy;

class User extends Player{
    User(TableStrategy viewer){
        super("you", viewer);
    }
}
