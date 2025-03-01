package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action which allows you to steal up to 2 coins from another player
public class StealAction extends GameAction {

    public StealAction(GamePlayer player){
        super(player,true);
    }
}

