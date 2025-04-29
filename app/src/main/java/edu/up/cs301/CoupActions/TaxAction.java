package edu.up.cs301.CoupActions;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action to generate 2 coins which cannot be blocked
public class TaxAction extends CoupAction implements Serializable {
    public TaxAction(GamePlayer player){
        super(player,false);
    }

}
