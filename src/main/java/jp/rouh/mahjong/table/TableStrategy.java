package jp.rouh.mahjong.table;

import java.util.List;

public interface TableStrategy extends TableObserver{

    TurnAction moveTurnPhase(List<TurnAction> choices);
}
