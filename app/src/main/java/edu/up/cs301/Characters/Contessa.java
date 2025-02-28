package edu.up.cs301.Characters;

import edu.up.cs301.GameFramework.Game;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class Contessa extends GameAction {

    protected GamePlayer player;

    public Contessa(GamePlayer player) {
        super(player);
    }
}
