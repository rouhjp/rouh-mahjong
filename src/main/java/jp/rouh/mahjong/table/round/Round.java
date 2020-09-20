package jp.rouh.mahjong.table.round;

import jp.rouh.mahjong.table.TableSpectator;
import jp.rouh.mahjong.table.round.result.RoundResult;
import jp.rouh.mahjong.tile.Wind;

/**
 * 局を表すインターフェース。
 * @author Rouh
 * @version 1.0
 */
public interface Round{
//
//    /**
//     * 場風を取得します。
//     * @return 場風
//     */
//    Wind getRoundWind();
//
//    /**
//     * 局数を取得します。
//     * @return 局数(1..4)
//     */
//    int getRoundCount();
//
//    /**
//     * 本場数を取得します。
//     * @return 本場数(0..)
//     */
//    int getStreakCount();
//
//    /**
//     * 供託数を取得します。
//     * @return 供託数(0..)
//     */
//    int getDepositCount();

    /**
     * 参加者を指定した自風に登録します。
     * @param seatWind 自風
     * @throws IllegalStateException 自風に既に参加者が登録されている場合
     * @param participant 参加者
     */
    void join(Wind seatWind, RoundParticipant participant);

    /**
     * 観戦者を登録します。
     * @param spectator 観戦者
     */
    void addSpectator(TableSpectator spectator);

    /**
     * 指定した参加者で局を開始します。
     *
     * <p>この操作によって参加者の得点が操作されます。
     * @see RoundParticipant#applyScore
     * @throws IllegalStateException 参加者が適切に登録されていない場合
     * @return 局の結果
     */
    RoundResult start();
}
