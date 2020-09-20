package jp.rouh.mahjong.table;

import jp.rouh.mahjong.table.action.CallAction;
import jp.rouh.mahjong.table.action.CallPhaseContext;
import jp.rouh.mahjong.table.action.TurnAction;
import jp.rouh.mahjong.table.action.TurnPhaseContext;


public interface TableStrategy extends TableObserver{

    TurnAction askTurnAction(TurnPhaseContext context);

    CallAction askCallAction(CallPhaseContext context);
}
