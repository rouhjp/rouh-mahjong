package jp.rouh.mahjong.tile;

import java.util.concurrent.ThreadLocalRandom;

/**
 * サイコロを扱うユーティリティクラス。
 * @author Rouh
 * @version 1.0
 */
final class Dices{
    private Dices(){
        throw new AssertionError("no instance");
    }

    /**
     * サイコロの目としてランダムに1..6の整数を取得します。
     * @return サイコロの目(1..6)
     */
    public static int rollDice(){
        return ThreadLocalRandom.current().nextInt(6) + 1;
    }
}
