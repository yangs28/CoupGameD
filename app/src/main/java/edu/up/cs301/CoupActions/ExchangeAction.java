package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

//an action which draws two cards and allows you to exchange both, one, or neither of your current hand
//this will need access to the Deck
public class ExchangeAction extends GameAction {

    public ExchangeAction(GamePlayer player){
        super(player,false);
    }
}
