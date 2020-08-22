package jp.rouh.mahjong.score;

/**
 * 和了方法を表すクラス。
 *
 * <p>和了方法は, 山牌からの自摸, 嶺上牌からの嶺上自摸,
 * 河からのロン, 槓からの槍槓ロンの４つです。
 * <p>和了方法は役判定に用いられます。
 * @author Rouh
 * @version 1.0
 */
public enum WinningType{
    /** 山牌からの自摸 */
    WALL_DRAW,

    /** 嶺上牌からの嶺上自摸 */
    QUAD_DRAW,

    /** 河からのロン */
    RIVER_GRAB,

    /** 槓からの槍槓ロン */
    QUAD_GRAB;

    /**
     * 和了が自摸または嶺上自摸であるかどうか検査します。
     * @return true  自摸または嶺上自摸の場合
     *         false ロンまたは槍槓ロンの場合
     */
    public boolean isSelfDraw(){
        return this==WALL_DRAW || this==QUAD_DRAW;
    }
}
