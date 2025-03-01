package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;
//an action that allows you to spend 3 coins to attempt an assassination
public class AssassinateAction extends GameAction {

    public AssassinateAction(GamePlayer player){
        super(player,true);
    }
}
