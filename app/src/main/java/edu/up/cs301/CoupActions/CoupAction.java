package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action to spend 7 coins and eliminate a player's influence
public class CoupAction extends GameAction {
    public CoupAction(GamePlayer player){
        super(player,false);
    }
}
