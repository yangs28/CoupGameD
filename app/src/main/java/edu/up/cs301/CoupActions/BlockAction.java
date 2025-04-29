package edu.up.cs301.CoupActions;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action which can be used in response to any blockable action
public class BlockAction extends CoupAction implements Serializable {

    public BlockAction(GamePlayer player){
        super(player,false);
    }
}
