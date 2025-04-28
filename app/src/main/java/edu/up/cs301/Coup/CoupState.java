package edu.up.cs301.Coup;

import java.util.Random;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Contessa;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameState;

/**
 * This contains the state for the Coup game. This stores the current state to inform
 * other classes on the status of the two players
 *
 * Also stores which Influence a character has, whether or not those Influences
 * have been killed, the Deck, as well as methods for killing Influences
 *
 * SPEEDRUN: To win quickly, you can
 * Draw the Assassin card
 * Assassinate both of one players cards over 2 turns
 * Assasinate the other players two cards in your next two
 * Win game after 4 turns.
 *
 * @author Sean Yang, Clint Sizemore, Kanoa Martin
 * @version 4-24-25
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
	//Initializes each players hands
	private GameAction[] player0Hand;
	private GameAction[] player1Hand;
	//This boolean checks to see if the card in the Deck is drawn. If it has been drawn we cannot draw from that position
	private boolean[] isDrawn;
	//This boolean checks to see if the card has been killed and can no longer be drawn
	private boolean[] isKilled;
	//These two boolean arrays check for the status of player0 and player1's dead Influences respectively
	private boolean[] player0isDead;
	private boolean[] player1isDead;

	//Stores the position of each separate card in use. This allows us to reference those
	//positions from the Deck later and prevent them from being redrawn
	int temphand1;
	int temphand2;
	int temphand3;
	int temphand4;

	private int gameStage;


	//Default constructor for the game state. Initializes all starting values
	public CoupState() {
		//Initializes both players money as 0 for the beginning of every game
		player0Money = 0;
		player1Money = 0;
		//Sets the first players turn
		playerId = 0;

		//Initializes both player hands and the deck
		player0Hand = new GameAction[2];
		player1Hand = new GameAction[2];
		deck = new GameAction[15];
		isDrawn = new boolean[15];
		isKilled = new boolean[15];

		//Initializes the Life status of both players. Set as false (not dead) by default
		this.player0isDead = new boolean[2];
		this.player1isDead = new boolean[2];



		//Creates a 3 of each type of card and adds to the deck
		for(int k = 0; k<15;k++){
			if(k<=2){deck[k]=new Ambassador(null);}
			else if(k<=5){deck[k]=new Assassin(null);}
			else if(k<=8){deck[k]=new Captain(null);}
			else if(k<=11){deck[k]=new Contessa(null);}
			else{deck[k]=new Duke(null);}
		}

		//New rng for calculations
		Random r = new Random();

		//Randomly grabs 4 cards from the deck to be used by both players
		int temphand1 = r.nextInt(14);
		int temphand2 = r.nextInt(14);
		int temphand3 = r.nextInt(14);
		int temphand4 = r.nextInt(14);

		//Sets player 1 and player 2 hands. Sets those cards as being drawn
		player0Hand[0] = deck[temphand1];
		player0Hand[1] = deck[temphand2];
		isDrawn[temphand1] = true;
		isDrawn[temphand2] = true;


		player1Hand[0] = deck[temphand3];
		player1Hand[1] = deck[temphand4];
		isDrawn[temphand3] = true;
		isDrawn[temphand4] = true;

	}


	//Constructor that initializes everything with custom values
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


		this.isDrawn = new boolean[orig.isDrawn.length];
		for (int i = 0; i < orig.isDrawn.length; i++) {
			this.isDrawn[i] = orig.isDrawn[i];
		}

		this.isKilled = new boolean[orig.isKilled.length];
		for (int i = 0; i < orig.isKilled.length; i++) {
			this.isKilled[i] = orig.isKilled[i];
		}

		this.player0isDead = new boolean[orig.player0isDead.length];
		for (int i = 0; i < orig.player0isDead.length; i++) {
			this.player0isDead[i] = orig.player0isDead[i];
		}

		this.player1isDead = new boolean[orig.player1isDead.length];
		for (int i = 0; i < orig.player1isDead.length; i++) {
			this.player1isDead[i] = orig.player1isDead[i];
		}


		// Copy temporary hands
		this.temphand1 = orig.temphand1;
		this.temphand2 = orig.temphand2;
		this.temphand3 = orig.temphand3;
		this.temphand4 = orig.temphand4;


	}



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
	//Grabs the current hand of either players
	public GameAction[] getplayer0Hand(){
		return player0Hand;
	}

	//Sets the hand to be a certain Influence
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

	//The makeDead methods sets a specific card to be killed
	public void make0Dead(int x) {
		player0isDead[x] = true;
	}

	public void make1Dead(int x) {
		player1isDead[x] = true;
	}
	//Returns a copy of the isDead array to check which Influences are dead
	public boolean[] checkplayer0Dead() {
		return player0isDead;
	}

	public boolean[] checkplayer1Dead() {
		return player1isDead;
	}

	public void setPlayerId(int _playerId) {
		this.playerId = _playerId;
	}

	public CoupState getGameState() {
		return this;
	}


	//An overwritten toString method that returns the state of the game
	@Override
	public String toString() {
		return "Player 0 has " + (player0Money) + " dabloons." +
				" Player 1 has " + (player1Money) + " dabloons." +
				" It is currently player " + (getPlayerId()) + "'s turn.";
	}
}




