package edu.up.cs301.CoupActions;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//action to generate 1 coin
public class IncomeAction extends CoupAction implements Serializable {
    public IncomeAction(GamePlayer player) {
        super(player,false);
    }

}
