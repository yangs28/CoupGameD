package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;
//action to generate 2 coins, but can be blocked
public class ForeignAideAction extends CoupAction {
    public ForeignAideAction(GamePlayer player) {
        super(player,true);
        //this action can be blocked
    }

}
