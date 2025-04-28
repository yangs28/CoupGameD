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
 * A smarter computer-version of a coup-player.
 * This computer will prioritize certain actions based on a hierarchy. It draws information from the
 * player and the computer's hand to determine which actions to perform and prioritize.
 * The computer will also review past actions to see which actions failed to obtain an estimate of the
 * player's hand, which will inform the computer player of what future actions to take
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

    //Copy of the retrieved CoupState
    CoupState tempSmartState;
    //assassinationBlockedCheck checks to see if a performed assasination action was blocked by the human player
    boolean assassinationBlockedCheck;
    //The unfortunately named assCall boolean checks if an assassination action was performed. By default set to false
    boolean assCall = false;
    //playerDead checks to see the status of the human player's cards
    boolean[] playerDead;
    //runOnce is a boolean used for a logic check to see if an assassination was performed successfully
    boolean runOnce = false;



    public CoupSmartComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();
        //Sets blocked check to be false initially (assassination was not blocked successfully)
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

        //Returns immediately if not an instance of CoupState. Fixes null object bug
        if(!(info instanceof CoupState)) {
            return;
        }

        //Sets the CoupState to be from the info variable
        tempSmartState = (CoupState) info;

        //If the ID does not match, just return
        if(tempSmartState.getPlayerId() != this.playerNum) {
            return;
        }

        //If the ID does match,
        if(tempSmartState.getPlayerId() == this.playerNum) {

            //Retrieves an initial copy of the opponent's starting hand to check their Life status
            //In the beginning all of their Influences will be alive
            playerDead = tempSmartState.checkplayer1Dead();

            //If an assassination was performed, call a helper method to compare the opponent's starting hand
            //with their current hand after an assassination was performed
            if(assCall == true) {
                //Runs only once to prevent a rare bug that breaks the logic checking
                if(runOnce == false) {
                    //If the opponent's hand was not changed, it means an assassination did not go through
                    //This is because an assassination action would change the opponent's hand by killing one of their influences
                    //Thus, we set this boolean to be true if the assassination is blocked so AI no longer performs ineffective assassinations
                    assassinationBlockedCheck = isAssassinationBlockedCheck();
                }
            }

            //RNG for probability check
            Random rng = new Random();
            double probability = rng.nextDouble();
            //Retrieves copy of your own hand
            GameAction[] myHand = tempSmartState.getplayer1Hand();
            //Checks AI and Opponent money values
            int myMoney = tempSmartState.getPlayer1Money();
            int oppMoney = tempSmartState.getPlayer0Money();
            //Checks your hand for dead Influences. Avoids calling ineffective actions from dead Influences
            boolean[] myDead = tempSmartState.checkplayer0Dead();

            //Actions are still performed based on a hierarchy, but they account for dead Influences
            //Leads to smarter decision-making so AI only prioritizes effective actions

            // 1) Always try to Coup first (guranteed, no probability check)
            if (myMoney >= 7) {
                CoupDeteAction coup = new CoupDeteAction(this);
                this.game.sendAction(coup);

                // 2) Then Assassinate if possible (70% chance)
            }
            else if (((myHand[0] instanceof Assassin && myDead[0] == false) || (myHand[1] instanceof Assassin && myDead[1] == false))
                    && myMoney >= 3
                    && probability < 0.7
                    //Updated logic of assassinate so we only perform assassinations if they are effective and not blocked previously
                    && assassinationBlockedCheck == false) {
                //Sends new assassination action
                AssassinateAction assassinate = new AssassinateAction(this);
                this.game.sendAction(assassinate);
                //Sets the unfortunately named variable to be true, indicating an assassination action has been called
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

    //Helper method to check for if an assassination attempt by the AI was blocked
    public boolean isAssassinationBlockedCheck() {
        //Compares the player's starting hand with their starting hand to check for change
        if (Arrays.equals(playerDead, tempSmartState.checkplayer1Dead())) {
                //Sets runOnce to be true to avoid for repeating calling of this function
                runOnce = true;
                //If the starting hand and current hand has not changed after an assassination attempt,
                //it means no cards were assassinated and the action failed, so return true
                return true;
            }
            //If the starting hand and current hand HAS changed, return false (meaning assassination attempt was successful)
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