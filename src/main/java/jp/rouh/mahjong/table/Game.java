package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Wind;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Game{
    public static final int DEFAULT_SCORE = 25000;
    private final List<GamePlayer> players;
    private final Wind initialDealerDirection;
    public Game(Player east, Player south, Player west, Player north){
        this(Map.of(Wind.EAST, east, Wind.SOUTH, south, Wind.WEST, west, Wind.NORTH, north));
    }
    private Game(Map<Wind, Player> map){
        players = map.entrySet().stream()
                .map(entry->new GamePlayer(this, entry.getValue(), entry.getKey()))
                .collect(toList());
        var tempDealerDirection = playerAt(Wind.EAST).rollDicesToPickDirection();
        initialDealerDirection = playerAt(tempDealerDirection).rollDicesToPickDirection();
    }
    public Wind getInitialDealerDirection(){
        return initialDealerDirection;
    }
    public List<GamePlayer> players(){
        return players;
    }
    GamePlayer playerAt(Wind direction){
        return players.get(direction.ordinal());
    }

}
