package jp.rouh.mahjong.table;

import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;
import jp.rouh.mahjong.tile.Wind;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Wall{
    private final List<Tile> values;
    private final Round round;
    private int drawCount = 0;
    private int quadCount = 0;
    private int revealCount = 0;
    private final Address firstDrawAddress;
    Wall(Round round, int d1, int d2){
        this.round = round;
        var list = Tiles.fullSet();
        Collections.shuffle(list);
        this.values = List.copyOf(list);
        this.firstDrawAddress = Address.of(d1, d2);
        round.players().forEach(RoundPlayer::wallGenerated);
    }

    List<Tile> takeFourTiles(){
        return List.of(takeTile(), takeTile(), takeTile(), takeTile());
    }

    Tile takeTile(){
        assert hasDrawableTile();
        return takeTileAt(drawCount++);
    }

    Tile takeQuadTile(){
        assert quadCount<=3;
        return takeTileAt(new int[]{134, 135, 132, 133}[quadCount++]);
    }

    private Tile takeTileAt(int index){
        var address = firstDrawAddress.shift(drawCount);
        round.players().forEach(player->{
            var side = address.wind().from(player.getSeatWind());
            player.wallTileTaken(side, address.column(), address.floor());
        });
        return values.get(index);
    }

    void notifyTurnFinished(){
        while(revealCount<quadCount){
            revealIndicator();
        }
    }

    void revealIndicator(){
        var index = new int[]{130, 128, 126, 124, 122}[revealCount++];
        var address = firstDrawAddress.shift(index);
        var tile = values.get(index);
        round.players().forEach(player->{
            var side = address.wind().from(player.getSeatWind());
            player.wallTileRevealed(side, address.column(), address.floor(), tile);
        });
    }

    int drawableTileCount(){
        return 122 - drawCount - quadCount;
    }

    boolean hasDrawableTile(){
        return drawableTileCount()>=1;
    }

    List<Tile> getUpperIndicators(){
        return IntStream.of(130, 128, 126, 124, 122)
                .limit(revealCount)
                .mapToObj(values::get)
                .collect(Collectors.toUnmodifiableList());
    }

    List<Tile> getLowerIndicators(){
        return IntStream.of(131, 129, 127, 125, 123)
                .limit(revealCount)
                .mapToObj(values::get)
                .collect(Collectors.toUnmodifiableList());
    }

    private static class Address{
        private final int index;
        private Address(Wind wind, int column, int floor){
            assert column<17;
            assert floor<2;
            index = wind.ordinal()*34 + column*2 + floor;
        }
        private Address(int index){
            assert index<136;
            this.index = index;
        }
        private Wind wind(){
            return Wind.values()[index/34];
        }
        private int column(){
            return index%34/2;
        }
        public int floor(){
            return index%2;
        }
        private Address shift(int n){
            return new Address((index + n)%136);
        }
        private static Address of(int d1, int d2){
            var wind = Side.of(d1, d2).of(Wind.EAST);
            var column = d1 + d2;
            var floor = 0;
            return new Address(wind, column, floor);
        }
    }
}
