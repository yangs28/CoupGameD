package edu.up.cs301.Coup;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.GameFramework.infoMessage.GameState;


/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
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
		// set the counter to that of the original
		//this.counter = orig.counter;
	}
}

