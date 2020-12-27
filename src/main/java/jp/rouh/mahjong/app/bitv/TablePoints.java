package jp.rouh.mahjong.app.bitv;

import java.awt.*;

/**
 * 麻雀卓上の牌の座標を取得するユーティリティクラス。
 * @author Rouh
 * @version 1.0
 */
final class TablePoints{
    private static final Point CENTER = new Point(580/2, 580/2);
    private TablePoints(){
        throw new AssertionError("no instance for you!");
    }


    static Point ofHandBlock(Direction dir, int index, boolean isolated){
        return PointWalker.from(CENTER, dir)
                .goStraight(260)
                .goRight(220)
                .goLeft(index*20)
                .goLeft(isolated? 10:0)
                .get();
    }

    static Point ofRiverBlock(Direction dir, int index){
        return ofRiverBlock(dir, index, -1);
    }

    static Point ofMeldBlock(Direction dir, int offset, boolean rotated, boolean added){
        return PointWalker.from(CENTER, dir)
                .goStraight(260)
                .goStraight(rotated? 5:0)
                .goStraight(added? -20:0)
                .goLeft(230)
                .goRight(offset)
                .goRight(rotated? 5:0)
                .get();
    }

    static Point ofRiverBlock(Direction dir, int index, int readyIndex){
        int row = index/6;
        int col = index%6;
        if(readyIndex==-1){
            return ofRiverBlock(dir, row, col, false, false);
        }
        int readyRow = readyIndex/6;
        boolean readyRotation = index==readyIndex;
        boolean readyTranslation = row==readyRow && readyIndex<index;
        return ofRiverBlock(dir, row, col, readyRotation, readyTranslation);
    }

    private static Point ofRiverBlock(Direction dir, int row, int col, boolean rr, boolean rt){
        return PointWalker.from(CENTER, dir)
                .goStraight(75)
                .goStraight(row*30)
                .goStraight(rr? 5:0)
                .goRight(50)
                .goLeft(col*20)
                .goLeft(rr? 5:0)
                .goLeft(rt? 10:0).get();
    }

    static Point ofWallBlock(Direction dir, int col, int floor){
        return PointWalker.from(CENTER, dir)
                .goStraight(205)
                .goRight(160)
                .goLeft(col*20)
                .translate(Direction.TOP, floor*10)
                .get();
    }

    static Point ofReadyBar(Direction dir){
        return PointWalker.from(CENTER, dir)
                .goStraight(50)
                .get();
    }

    static Point ofPlayerName(Direction dir){
        return PointWalker.from(CENTER, dir)
                .goStraight(35)
                .get();
    }

    static Point ofPlayerScore(Direction dir){
        return PointWalker.from(CENTER, dir)
                .goStraight(25)
                .get();
    }

    static Point ofPlayerWind(Direction dir){
        return PointWalker.from(CENTER, dir)
                .goStraight(45)
                .goRight(40)
                .get();
    }

    static Point ofPlayerMessage(Direction dir){
        return PointWalker.from(CENTER, dir)
                .goStraight(210)
                .get();
    }

    private static class PointWalker{
        private int x;
        private int y;
        private Direction d;
        private PointWalker(int x, int y, Direction d){
            this.x = x;
            this.y = y;
            this.d = d;
        }
        private PointWalker(Point p, Direction d){
            this(p.x, p.y, d);
        }
        private void walk(Direction d, int k){
            switch(d){
                case TOP:
                    y -= k;
                    break;
                case RIGHT:
                    x += k;
                    break;
                case BOTTOM:
                    y += k;
                    break;
                case LEFT:
                    x -= k;
                    break;
            }
        }
        private PointWalker goStraight(int k){
            walk(d, k);
            return this;
        }
        private PointWalker goRight(int k){
            walk(d.turnRight(), k);
            return this;
        }
        private PointWalker goLeft(int k){
            walk(d.turnLeft(), k);
            return this;
        }
        private PointWalker translate(Direction d, int k){
            walk(d, k);
            return this;
        }
        private Point get(){
            return new Point(x, y);
        }
        private static PointWalker from(Point p, Direction d){
            return new PointWalker(p, d);
        }
    }
}
