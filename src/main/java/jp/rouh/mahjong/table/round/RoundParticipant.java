package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.tile.Wind;

/**
 * 局の参加者を表すクラス。
 * @author Rouh
 * @version 1.0
 */
public interface RoundParticipant extends TableStrategy{

    /**
     * 名前を取得します。
     * @return 名前
     */
    String getName();

    /**
     * 席順を取得します。
     * @return 席順
     */
    Wind getOrderWind();

    /**
     * 得点を取得します。
     * @return 得点
     */
    int getScore();

    /**
     * 得点に追加点を与えます。
     * @param score 追加点(負の数を含む)
     */
    void applyScore(int score);
}
