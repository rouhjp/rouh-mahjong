package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Wind;

import java.util.HashMap;
import java.util.Map;

public class Game implements GameMaster{
    private final Map<Wind, GamePlayer> gamePlayers;
    private final Wind initialDealerOrderWind;

    /**
     * コンストラクタ
     * @param p1 東側に着席したプレイヤー(仮東)
     * @param p2 南側に着席したプレイヤー(仮南)
     * @param p3 西側に着席したプレイヤー(仮西)
     * @param p4 北側に着席したプレイヤー(仮北)
     */
    public Game(Player p1, Player p2, Player p3, Player p4){
        gamePlayers = new HashMap<>();
        gamePlayers.put(Wind.EAST, new GamePlayer(p1, Wind.EAST, this));
        gamePlayers.put(Wind.SOUTH, new GamePlayer(p2, Wind.SOUTH, this));
        gamePlayers.put(Wind.WEST, new GamePlayer(p3, Wind.WEST, this));
        gamePlayers.put(Wind.NORTH, new GamePlayer(p4, Wind.NORTH, this));
        initialDealerOrderWind = decideInitialDealer();
        start();
    }

    /**
     * 親決めを実施します。
     * @return 起家の席方向
     */
    private Wind decideInitialDealer(){
        //仮親決め
        var diceSum1 = playerAtOrder(Wind.EAST).rollDices();
        var tempDealerOrderWind = Side.of(diceSum1).of(Wind.EAST);
        notifySeatUpdated(tempDealerOrderWind);
        //親決め
        var diceSum2 = playerAtOrder(tempDealerOrderWind).rollDices();
        var initialDealerOrderWind = Side.of(diceSum2).of(tempDealerOrderWind);
        notifySeatUpdated(initialDealerOrderWind);
        return initialDealerOrderWind;
    }

    private void start(){
        for(var wind:Wind.values()){
            playerAtOrder(wind).roundStarted(Wind.EAST, 1, 0, 0);
        }
//        Round round = new FieldRound(Wind.EAST, 1, 0, 0);
//        for(var side:Side.values()){
//            round.join(Wind.EAST, playerAtOrder(side.of(initialDealerOrderWind)));
//        }

    }

    /**
     * プレイヤー全員にプレイヤーの状態変更を通知します。
     */
    private void notifySeatUpdated(Wind dealerOrderWind){
        for(var orderWind:Wind.values()){
            var gamePlayer = playerAtOrder(orderWind);
            var seatWind = orderWind.from(dealerOrderWind).of(Wind.EAST);
            var name = gamePlayer.getName();
            var score = gamePlayer.getScore();
            for(var otherWind:Wind.values()){
                playerAtOrder(otherWind).seatUpdated(seatWind.from(otherWind), seatWind, name, score);
            }
        }
    }

    /**
     * 指定した方角に着席したプレイヤーを取得します。
     * @param orderWind 席方向
     * @return プレイヤー
     */
    GamePlayer playerAtOrder(Wind orderWind){
        return gamePlayers.get(orderWind);
    }
}
