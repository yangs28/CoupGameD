package edu.up.cs301.Coup;

import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.BlockAction;
import edu.up.cs301.CoupActions.ChallengeAction;
import edu.up.cs301.CoupActions.CoupAction;
import edu.up.cs301.CoupActions.ExchangeAction;
import edu.up.cs301.CoupActions.ForeignAideAction;
import edu.up.cs301.CoupActions.IncomeAction;
import edu.up.cs301.CoupActions.StealAction;
import edu.up.cs301.CoupActions.TaxAction;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import android.util.Log;

import java.util.Random;

/**
 * A class that represents the state of a game. In our counter game, the only
 * relevant piece of information is the value of the game's counter. The
 * CounterState object is therefore very simple.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class CoupLocalGame extends LocalGame {

	// When a counter game is played, any number of players. The first player
	// is trying to get the counter value to TARGET_MAGNITUDE; the second player,
	// if present, is trying to get the counter to -TARGET_MAGNITUDE. The
	// remaining players are neither winners nor losers, but can interfere by
	// modifying the counter.
	public static final int TARGET_MAGNITUDE = 10;

	// the game's state
	private CoupState gameState;
	
	/**
	 * can this player move
	 * 
	 * @return
	 * 		true, because all player are always allowed to move at all times,
	 * 		as this is a fully asynchronous game
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		return true;
	}

	/**
	 * This ctor should be called when a new counter game is started
	 */
	public CoupLocalGame(GameState state) {
		// initialize the game state, with the counter value starting at 0
		if (! (state instanceof CoupState)) {
			state = new CoupState();
		}
		this.gameState = (CoupState)state;
		super.state = state;
	}

	/**
	 * The only type of GameAction that should be sent is CounterMoveAction
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		Log.i("action", action.getClass().toString());

		// Create a new Random instance
		Random rand = new Random();

		// Generate a random amount for player0Money (adjust range as needed)
		int randomMoney = rand.nextInt(10) + 1; // Generates a number between 1 and 10



		if (action instanceof AssassinateAction) { //todo
			AssassinateAction aa = (AssassinateAction) action;
			gameState.setPlayer0Money(gameState.getPlayer0Money() - 3);
			Log.d("Money", "Assassinate action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for AssassinateAction
			return true;
		}

		if (action instanceof BlockAction) { //todo
			BlockAction ba = (BlockAction) action;
			gameState.setPlayer0Money(randomMoney);
			Log.d("Money", "Block action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for BlockAction
			return true;
		}

		if (action instanceof ChallengeAction) { //todo
			ChallengeAction ca = (ChallengeAction) action;
			gameState.setPlayer0Money(randomMoney);
			Log.d("Money", "Challenge action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for ChallengeAction
			return true;
		}

		if (action instanceof ExchangeAction) { //todo
			ExchangeAction ea = (ExchangeAction) action;
			gameState.setPlayer0Money(randomMoney);
			Log.d("Money", "Exchange action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for ExchangeAction
			return true;
		}

		if (action instanceof ForeignAideAction) { //todo
			ForeignAideAction faa = (ForeignAideAction) action;
			gameState.setPlayer0Money(gameState.getPlayer0Money() + 2); //actually adds 2 coins
			Log.d("Money", "Foreign Aide action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for ForeignAideAction
			return true;
		}

		if (action instanceof IncomeAction) {
			IncomeAction ia = (IncomeAction) action;
			gameState.setPlayer0Money(gameState.getPlayer0Money() + 1);
			Log.d("Money", "Income action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for IncomeAction
			return true;
		}

		if (action instanceof StealAction) { //todo
			StealAction sa = (StealAction) action;
			gameState.setPlayer0Money(randomMoney);
			Log.d("Money", "Steal action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for StealAction
			return true;
		}

		if (action instanceof TaxAction) { //todo
			TaxAction ta = (TaxAction) action;
			gameState.setPlayer0Money(gameState.getPlayer0Money() + 3); //correct money count
			Log.d("Money", "Tax action was called. Money is " + gameState.getPlayer0Money());
			// Additional logic for TaxAction
			return true;
		}

		if (action instanceof CoupMoveAction) { //todo
			CoupMoveAction cma = (CoupMoveAction) action;
			gameState.setPlayer0Money(randomMoney);
			Log.d("Money", "Coup Move action was called. Money is " + gameState.getPlayer0Money());
			// Denote that this was a legal/successful move
			return true;
		}

		else {
			// denote that this was an illegal move
			return false;
		}
	}//makeMove
	
	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new CoupState(this.gameState));
		
	}//sendUpdatedSate
	
	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	protected String checkIfGameOver() {
		
			return "false";
		}

	}


// class CounterLocalGame
