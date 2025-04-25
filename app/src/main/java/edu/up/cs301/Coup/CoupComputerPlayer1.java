package edu.up.cs301.Coup;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.CoupDeteAction;
import edu.up.cs301.CoupActions.ExchangeAction;
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

			GameAction[] myHand = tempState.getplayer1Hand();
			int myMoney = tempState.getPlayer1Money();

			if (myHand[0] instanceof Duke || myHand[1] instanceof Duke) {
				TaxAction tax = new TaxAction(this);
				this.game.sendAction(tax);
			} else if ((myHand[0] instanceof Assassin || myHand[1] instanceof Assassin)
					&& myMoney >= 3) {
				AssassinateAction assassinate = new AssassinateAction(this);
				this.game.sendAction(assassinate);
			} else if (myHand[0] instanceof Captain || myHand[1] instanceof Captain) {
				StealAction steal = new StealAction(this);
				this.game.sendAction(steal);
			} else if (myHand[0] instanceof Ambassador || myHand[1] instanceof Ambassador) {
				ExchangeAction exchange = new ExchangeAction(this);
				this.game.sendAction(exchange);
			} else if (myMoney >= 7) {
				CoupDeteAction coup = new CoupDeteAction(this);
				this.game.sendAction(coup);
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
