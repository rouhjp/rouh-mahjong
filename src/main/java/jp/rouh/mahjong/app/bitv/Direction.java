package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.tile.Side;

/**
 * 画面の上下左右の向きを表すクラス。
 * @author Rouh
 * @version 1.0
 */
public enum Direction{
    /** 上向き */
    TOP(0),
    /** 右向き */
    RIGHT(1),
    /** 下向き */
    BOTTOM(2),
    /** 左向き */
    LEFT(3);

    private final int orderNumber;
    Direction(int orderNumber){
        this.orderNumber = orderNumber;
    }

    /**
     * この向きから相対的に右にあたる向きを返します。
     *
     * <p>例えば, {@code Direction.RIGHT.turnRight()==Direction.BOTTOM}が成立します。
     * @return 右にあたる向き
     */
    public Direction turnRight(){
        return valueOf((orderNumber + 1)%4);
    }

    /**
     * この向きから相対的に左にあたる向きを返します。
     *
     * <p>例えば, {@code Direction.RIGHT.turnLeft()==Direction.TOP}が成立します。
     * @return 左にあたる向き
     */
    public Direction turnLeft(){
        return valueOf((orderNumber + 3)%4);
    }

    /**
     * この向きから相対的に反対にあたる向きを返します。
     *
     * <p>例えば, {@code Direction.RIGHT.reversed()==Direction.LEFT}が成立します。
     * @return 反対にあたる向き
     */
    public Direction reversed(){
        return valueOf((orderNumber + 2)%4);
    }

    /**
     * この向きが左右方向の向きかどうか検査します。
     * @return true  左または右向きの場合
     *         false 上または下向きの場合
     */
    public boolean isSideways(){
        return this==RIGHT || this==LEFT;
    }

    /**
     * 上向きを基準に, 度数法の角度を返します。
     *
     * <p>対応表は以下のとおりです。
     * <table>
     * <tr><th>direction</th><th>direction.angle()</th></tr>
     * <tr><td>TOP</td><td></td>0</tr>
     * <tr><td>RIGHT</td><td>90</td></tr>
     * <tr><td>BOTTOM</td><td>180</td></tr>
     * <tr><td>LEFT</td><td>270</td></tr>
     * </table>
     * @return 角度
     */
    public int angle(){
        return orderNumber*90;
    }

    /**
     * 相対方向クラス{@link Side}を上下左右の向き{@link Direction}に変換します。
     *
     * <p>例えば自プレイヤーの方向を表す相対方向{@code Side.SELF}は,
     * 画面下部に表示するため, {@code Direction.BOTTOM}に変換されます。
     * 対応表は以下のとおりです。
     * <table>
     * <tr><th>Side</th><th>Direction</th></tr>
     * <tr><td>SELF</td><td>BOTTOM</td></tr>
     * <tr><td>RIGHT</td><td>RIGHT</td></tr>
     * <tr><td>ACROSS</td><td>TOP</td></tr>
     * <tr><td>LEFT</td><td>LEFT</td></tr>
     * </table>
     * @see Side
     * @param side 相対方向
     * @return 対応する上下左右の向き
     */
    public static Direction of(Side side){
        switch(side){
            case SELF: return BOTTOM;
            case RIGHT: return RIGHT;
            case ACROSS: return TOP;
            case LEFT: return LEFT;
        }
        throw new AssertionError("implementation error");
    }

    private static Direction valueOf(int order){
        for(Direction d: values()) if(d.orderNumber==order) return d;
        throw new IllegalArgumentException("no such direction: " + order);
    }
}
