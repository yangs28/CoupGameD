package edu.up.cs301.Coup;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;

/**
 * This contains the state for the Coup game. This stores the current state of the
 * game as well as all possible executable actions
 *
 * SPEEDRUN: To win quickly, you can
 * 
 * @author Sean Yang, Clint Sizemore, Kanoa Martin
 * @version 2-28-25
 */
public class CoupState extends GameState {

	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;

	//Player 0 is the human
	private int player0Money;
	//Player 1 is computer
	private int player1Money;
	//Int for player turn, from 0-max number of players
	private int playerId;

	private int gameStage;


	public CoupState() {
		player0Money = 0;
		player1Money = 0;
		playerId = 0;
	}

	public CoupState(int _player0Money, int _player1Money, int _playerId) {
		this.player0Money = _player0Money;
		this.player1Money = _player1Money;
		this.playerId = _playerId;
	}

	/**
	 * copy constructor; makes a copy of the original object
	 *
	 * @param orig the object from which the copy should be made
	 */
	public CoupState(CoupState orig) {
		//Sets currencies and ID of the players to be the copy of Orig
		this.player0Money = orig.player0Money;
		this.player1Money = orig.player1Money;
		this.playerId = orig.playerId;
	}

	//action methods

	//generic actions
	public boolean makeIncomeAction(GameAction action, GamePlayer player){return false;}
	public boolean makeAideAction(GameAction action, GamePlayer player){return false;}
	public boolean makeCoupAction(GameAction action, GamePlayer player){return false;}

	//class actions
	public boolean makeAssnAction(GameAction action, GamePlayer player){return false;}
	public boolean makeTaxAction(GameAction action, GamePlayer player){return false;}
	public boolean makeStealAction(GameAction action, GamePlayer player){return false;}
	public boolean makeExcAction(GameAction action, GamePlayer player){return false;}

	//reactions
	public boolean makeBlockAction(GameAction action, GamePlayer player){return false;}
	public boolean makeChalAction(GameAction action, GamePlayer player){return false;}






	// Getter and Setter for player0Money
	public int getPlayer0Money() {
		return player0Money;
	}

	public void setPlayer0Money(int _player0Money) {
		this.player0Money = _player0Money;
	}

	// Getter and Setter for player1Money
	public int getPlayer1Money() {
		return player1Money;
	}

	public void setPlayer1Money(int _player1Money) {
		this.player1Money = _player1Money;
	}

	// Getter and Setter for playerId
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int _playerId) {
		this.playerId = _playerId;
	}



	@Override
	public String toString() {
		return "Player 0 has " + String.valueOf(player0Money) + " dabloons." +
				"Player 1 has " + String.valueOf(player1Money) + " dabloons" +
				"It is currently player " + String.valueOf(getPlayerId()) + "'s turn";
	}
}

