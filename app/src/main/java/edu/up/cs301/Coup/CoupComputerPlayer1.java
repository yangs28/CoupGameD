package edu.up.cs301.Coup;

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
 * A computer-version of a counter-player.  Since this is such a simple game,
 * it just sends "+" and "-" commands with equal probability, at an average
 * rate of one per second. 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2013
 */
public class CoupComputerPlayer1 extends GameComputerPlayer implements Tickable {
	
    /**
     * Constructor for objects of class CounterComputerPlayer1
     * 
     * @param name
     * 		the player's name
     */

	//monies

	CoupState tempState;



    public CoupComputerPlayer1(String name) {
        // invoke superclass constructor
        super(name);
        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();

    }
    
    /**
     * callback method--game's state has changed
     * 
     * @param info
     * 		the information (presumably containing the game's state)
     */
	@Override
	protected void receiveInfo(GameInfo info) {
		//Automatically performs an Income action for now.
		//NOTE: Rare bug with thread.sleep. Conflicts with game and causes a turn error. Mechanism unknown.

		tempState = (CoupState) info;


		if(!(info instanceof CoupState)) {
			return;
		}

		if(tempState.getPlayerId() != this.playerNum) {
			return;
		}

		if(tempState.getPlayerId() == this.playerNum) {

			Random rng = new Random();
			double probability = rng.nextDouble();
			GameAction[] myHand = tempState.getplayer1Hand();
			int myMoney = tempState.getPlayer1Money();

			// 1) Always try to Coup first (guranteed, no probability check)
			if (myMoney >= 7) {
				CoupDeteAction coup = new CoupDeteAction(this);
				this.game.sendAction(coup);

			// 2) Then Assassinate if possible (70% chance)
			} else if ((myHand[0] instanceof Assassin || myHand[1] instanceof Assassin)
					&& myMoney >= 3
					&& probability < 0.7) {
				AssassinateAction assassinate = new AssassinateAction(this);
				this.game.sendAction(assassinate);

			// 3) Then Exchange (30% chance)
			} else if ((myHand[0] instanceof Ambassador || myHand[1] instanceof Ambassador)
					&& probability < 0.3) {
				ExchangeAction exchange = new ExchangeAction(this);
				this.game.sendAction(exchange);

			// 4) Then Tax (Always use tax as it's the best way to make money)
			} else if ((myHand[0] instanceof Duke || myHand[1] instanceof Duke)) {
				TaxAction tax = new TaxAction(this);
				this.game.sendAction(tax);

			// 5) Then Foreign Aid (Always use Foreign Aid if other actions not present)
			} else if (probability < 0.3) {
				ForeignAideAction foreignAide = new ForeignAideAction(this);
				this.game.sendAction(foreignAide);

			// 6) Then Steal (Always use steal if other actions not present)
			} else if ((myHand[0] instanceof Captain || myHand[1] instanceof Captain)) {
				StealAction steal = new StealAction(this);
				this.game.sendAction(steal);

			// 7) Income as a last resort
			} else {
				IncomeAction inc = new IncomeAction(this);
				this.game.sendAction(inc);
			}



		}

	}
	
	/**
	 * callback method: the timer ticked
	 */
	protected void timerTicked() {

		//CLINT REMOVED THIS SO IT STOP MOVING RANDOMLY

		// 5% of the time, increment or decrement the counter
		/*if (Math.random() >= 0.05) return; // do nothing 95% of the time

		// "flip a coin" to determine whether to increment or decrement
		boolean move = Math.random() >= 0.5;
		
		// send the move-action to the game
		game.sendAction(new CoupMoveAction(this, move));*/
	}
}
