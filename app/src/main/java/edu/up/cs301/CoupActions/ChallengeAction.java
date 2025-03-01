package edu.up.cs301.CoupActions;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;
//an action to challenge any character action
public class ChallengeAction extends GameAction {
    public ChallengeAction(GamePlayer player){super(player,false);}
}
