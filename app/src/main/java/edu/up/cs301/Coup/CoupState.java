package edu.up.cs301.Coup;

import android.util.Log;

import java.util.Random;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Contessa;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.GameFramework.Game;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;

/**
 * This contains the state for the Coup game. This stores the current state of the
 * game as well as all possible executable actions
 *
 * SPEEDRUN: To win quickly, you can
 * Draw the Assassin card
 * Assassinate both of one players cards over 2 turns
 * Assasinate the other players two cards in your next two
 * Win game after 4 turns.
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
	//Initializes the deck
	private GameAction[] deck;
	private GameAction[] player0Hand;
	private GameAction[] player1Hand;

	private int gameStage;


	//Default constructor for the game state. Initializes everything as zero
	public CoupState() {
		player0Money = 0;
		player1Money = 0;
		playerId = 0;

		player0Hand = new GameAction[2];
		player1Hand = new GameAction[2];
		deck = new GameAction[15];

		for(int k = 0; k<15;k++){
			if(k<=2){deck[k]=new Ambassador(null);}
			else if(k<=5){deck[k]=new Assassin(null);}
			else if(k<=8){deck[k]=new Captain(null);}
			else if(k<=11){deck[k]=new Contessa(null);}
			else{deck[k]=new Duke(null);}
		}
		Random r = new Random();

		player0Hand[0] = deck[r.nextInt(14)];
		player0Hand[1] = deck[r.nextInt(14)];

		player1Hand[0] = deck[r.nextInt(14)];
		player1Hand[1] = deck[r.nextInt(14)];
	}


	//Constructor that sets the values
	public CoupState(int _player0Money, int _player1Money, int _playerId, GameAction[] _player0Hand, GameAction[] _player1Hand, GameAction[] _deck) {
		//Initializes ID and money
		this.player0Money = _player0Money;
		this.player1Money = _player1Money;
		this.playerId = _playerId;
		//Initializes the hand and deck
		this.player0Hand = _player0Hand.clone();
		this.player1Hand = _player1Hand.clone();
		this.deck = _deck.clone();
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

		//These if statements initializes the arrays and copies each position within the array
		this.player0Hand = new GameAction[orig.player0Hand.length];
		for (int i = 0; i < orig.player0Hand.length; i++) {
			this.player0Hand[i] = orig.player0Hand[i];
		}

		this.player1Hand = new GameAction[orig.player1Hand.length];
		for (int i = 0; i < orig.player1Hand.length; i++) {
			this.player1Hand[i] = orig.player1Hand[i];
		}

		this.deck = new GameAction[orig.deck.length];
		for (int i = 0; i < orig.deck.length; i++) {
			this.deck[i] = orig.deck[i];
		}


	}

/*
	//generic actions
	//every action can only be performed if you have less than 10 coins, otherwise yomi coup
	//actions that affect other players than the player performing it take an additional player variable
	public boolean makeIncomeAction(GameAction action, GamePlayer player){

		//gain one coin
		if(playerId == 0 && getPlayer0Money() <10){
			setPlayer0Money(getPlayer0Money()+1);
		}
		else if(playerId == 1 && getPlayer1Money() <10){
			setPlayer1Money(getPlayer1Money()+1);
		}
		else{return false;}
		return true;
	}

	public boolean makeAideAction(GameAction action, GamePlayer player){
		//gain 2 coins
		if(playerId == 0 && getPlayer0Money() <10){
			setPlayer0Money(getPlayer0Money()+2);
		}
		else if(playerId == 1 && getPlayer1Money() <10){
			setPlayer1Money(getPlayer1Money()+2);
		}
		else{return false;}
		return true;
	}
	public boolean makeCoupAction(GameAction action, GamePlayer player, GamePlayer victim){
		//pay 7 coins
		if(playerId == 0 && getPlayer0Money() >=7){
			setPlayer0Money(getPlayer0Money()-7);
		}
		else if(playerId == 1 && getPlayer1Money() >=7){
			setPlayer1Money(getPlayer1Money()-7);
		}
		else{return false;}
		return true;
	}

	//class actions
	public boolean makeAssnAction(GameAction action, GamePlayer player, GamePlayer victim){
		//pay 3 coins
		if(playerId == 0 && getPlayer0Money() >=3 && getPlayer0Money() <10){
			setPlayer0Money(getPlayer0Money()-3);
		}
		else if(playerId == 1 && getPlayer1Money() >=3 && getPlayer1Money() < 10){
			setPlayer1Money(getPlayer1Money()-3);
		}
		else{return false;}
		return true;
	}
	public boolean makeTaxAction(GameAction action, GamePlayer player){
		//gain 2 coins
		if(playerId == 0 && getPlayer0Money() <10){
			setPlayer0Money(getPlayer0Money()+2);
		}
		else if(playerId == 1 && getPlayer1Money() < 10){
			setPlayer1Money(getPlayer1Money()+2);
		}
		else{return false;}
		return true;
	}
	public boolean makeStealAction(GameAction action, GamePlayer player, GamePlayer victim){
		//remove 2 coins from another player, gain 2 coins
		if(playerId == 0 && getPlayer0Money() <10){
			setPlayer0Money(getPlayer0Money()+2);
			setPlayer1Money(getPlayer1Money()-2);

		}
		else if(playerId == 1 && getPlayer1Money() < 10){
			setPlayer1Money(getPlayer1Money()+2);
			setPlayer0Money(getPlayer0Money()-2);

		}
		else{return false;}
		return true;}

	public boolean makeExcAction(GameAction action, GamePlayer player){
		if(playerId == 0 && getPlayer0Money() <10){
			return false;
		}
		else if(playerId == 1 && getPlayer1Money() < 10){
			return false;
		}
		else{return true;}
	}

	//reactions
	public boolean makeBlockAction(GameAction action, GamePlayer player){return false;}
	public boolean makeChalAction(GameAction action, GamePlayer player){return false;}
*/





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

	public GameAction[] getplayer0Hand(){
		return player0Hand;
	}

	public void setplayer0Hand(GameAction card1, GameAction card2){
		player0Hand[0] = card1;
		player0Hand[1] = card2;
	}

	public GameAction[] getDeck() {
		return deck;
	}

	public GameAction[] getplayer1Hand(){
		return player1Hand;
	}

	public void setplayer2Hand(GameAction card1, GameAction card2){
		player1Hand[0] = card1;
		player1Hand[1] = card2;
	}

	public void setPlayerId(int _playerId) {
		this.playerId = _playerId;
	}


	//An overwritten toString method that returns the state of the game
	@Override
	public String toString() {
		return "Player 0 has " + (player0Money) + " dabloons." +
				" Player 1 has " + (player1Money) + " dabloons." +
				" It is currently player " + (getPlayerId()) + "'s turn.";
	}
}




