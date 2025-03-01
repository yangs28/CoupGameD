package edu.up.cs301.Characters;

import edu.up.cs301.GameFramework.Game;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class Ambassador extends GameAction {

    protected GamePlayer player;

    public Ambassador(GamePlayer player) {
        super(player);
    }
}
