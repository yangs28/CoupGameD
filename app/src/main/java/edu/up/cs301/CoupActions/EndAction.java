package edu.up.cs301.CoupActions;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;
//allows clint to make a blank action bc idk how
public class EndAction extends CoupAction implements Serializable {

    public EndAction(GamePlayer player){
        super(player,false);
    }
}
