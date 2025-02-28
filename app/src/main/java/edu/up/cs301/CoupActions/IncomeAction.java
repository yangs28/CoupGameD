package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//action to generate 1 coin
public class IncomeAction extends GameAction {
    public IncomeAction(GamePlayer player) {
        super(player,false);
    }

}
