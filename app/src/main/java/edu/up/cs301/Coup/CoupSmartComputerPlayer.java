package edu.up.cs301.Coup;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.CoupDeteAction;
import edu.up.cs301.CoupActions.ExchangeAction;
import edu.up.cs301.CoupActions.ForeignAideAction;
import edu.up.cs301.CoupActions.IncomeAction;
import edu.up.cs301.CoupActions.StealAction;
import edu.up.cs301.CoupActions.TaxAction;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GameComputerPlayer;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.GameFramework.utilities.Tickable;

/**
 * A smarter computer-version of a coup-player. This smarter version
 * uses a chain of ideal steps with a probability to try different
 * paths depending on its cards and a RNG
 *
 * @author Sean Yang, Clint Sizemore, Kanoa Martin
 * @version 4-24-25
 */
public class CoupSmartComputerPlayer extends GameComputerPlayer implements Tickable {

    /**
     * Constructor for objects of class CounterComputerPlayer1
     *
     * @param name
     * 		the player's name
     */

    //monies

    CoupState tempSmartState;
    //assassinationBlockedCheck checks to see if a performed assasination action was blocked by the human player
    boolean assassinationBlockedCheck;
    boolean assCall = false;
    //playerDead checks to see the status of the human player's cards
    boolean[] playerDead;
    boolean runOnce = false;



    public CoupSmartComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();
        assassinationBlockedCheck = false;
    }

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        //NOTE: Rare bug with thread.sleep. Conflicts with game and causes a turn error. Mechanism unknown.

        if(!(info instanceof CoupState)) {
            return;
        }

        tempSmartState = (CoupState) info;


        if(tempSmartState.getPlayerId() != this.playerNum) {
            return;
        }

        if(tempSmartState.getPlayerId() == this.playerNum) {

            playerDead = tempSmartState.checkplayer1Dead();
            Log.d("Block", "Status of assBlock is " + assassinationBlockedCheck);
            if(assCall == true) {
                if(runOnce == false) {
                    assassinationBlockedCheck = isAssassinationBlockedCheck();
                }
            }

            Random rng = new Random();
            double probability = rng.nextDouble();
            //Retrieves copy of your own hand
            GameAction[] myHand = tempSmartState.getplayer1Hand();
            //Checks AI and Opponent money values
            int myMoney = tempSmartState.getPlayer1Money();
            int oppMoney = tempSmartState.getPlayer0Money();
            //Checks your hand for dead Influences
            boolean[] myDead = tempSmartState.checkplayer0Dead();

            // 1) Always try to Coup first (guranteed, no probability check)
            if (myMoney >= 7) {
                CoupDeteAction coup = new CoupDeteAction(this);
                this.game.sendAction(coup);

                // 2) Then Assassinate if possible (70% chance)
            }
            else if (((myHand[0] instanceof Assassin && myDead[0] == false) || (myHand[1] instanceof Assassin && myDead[1] == false))
                    && myMoney >= 3
                    && probability < 0.7
                    && assassinationBlockedCheck == false) {
                AssassinateAction assassinate = new AssassinateAction(this);
                this.game.sendAction(assassinate);
                assCall = true;

                // 3) Then Exchange (30% chance)
            }
            else if (((myHand[0] instanceof Ambassador && myDead[0] == false) || (myHand[1] instanceof Ambassador && myDead[1] == false))
                    && probability < 0.3) {
                ExchangeAction exchange = new ExchangeAction(this);
                this.game.sendAction(exchange);

                // 4) Then Tax (Always use tax as it's the best way to make money)
            }
            else if ((myHand[0] instanceof Duke && myDead[0] == false) || (myHand[1] instanceof Duke && myDead[1] == false)) {
                TaxAction tax = new TaxAction(this);
                this.game.sendAction(tax);

                // 5) Then Foreign Aid (Always use Foreign Aid if other actions not present)
            } else if (((myHand[0] instanceof Duke && myDead[0] == false) || (myHand[1] instanceof Duke && myDead[1] == false))
                    && probability < 0.3) {
                ForeignAideAction foreignAide = new ForeignAideAction(this);
                this.game.sendAction(foreignAide);

                // 6) Then Steal (RNG for steal since it can be blocked)
            } else if (((myHand[0] instanceof Captain && myDead[0] == false) || (myHand[1] instanceof Captain && myDead[1] == false))
                    && probability < 0.5 && oppMoney > 0) {
                StealAction steal = new StealAction(this);
                this.game.sendAction(steal);

                // 7) Income as a last resort
            } else {
                IncomeAction inc = new IncomeAction(this);
                this.game.sendAction(inc);
            }
        }

    }

    public boolean isAssassinationBlockedCheck() {
        if (Arrays.equals(playerDead, tempSmartState.checkplayer1Dead())) {
                runOnce = true;
                return true;
            }
            return false;

    }

    /**
     * callback method: the timer ticked
     */
    protected void timerTicked() {

        //CLINT REMOVED THIS SO IT STOPS MOVING RANDOMLY (CounterGame code)

        // 5% of the time, increment or decrement the counter
		/*if (Math.random() >= 0.05) return; // do nothing 95% of the time

		// "flip a coin" to determine whether to increment or decrement
		boolean move = Math.random() >= 0.5;

		// send the move-action to the game
		game.sendAction(new CoupMoveAction(this, move));*/
    }
}