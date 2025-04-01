package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action to spend 7 coins and eliminate a player's influence
public class CoupAction extends GameAction {


    private boolean isBlockable;

    public CoupAction(GamePlayer player, boolean blockable){
        super(player);
        this.isBlockable = blockable;
    }
    public boolean isBlockable(){
        return isBlockable;
    }

    public boolean getIsBlockable() {
        return isBlockable;
    }
    public void setIsBlockable(boolean b) {
        this.isBlockable = b;
    }
}
