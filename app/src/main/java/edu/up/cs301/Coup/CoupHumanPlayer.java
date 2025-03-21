package edu.up.cs301.Coup;

import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.ExchangeAction;
import edu.up.cs301.CoupActions.ForeignAideAction;
import edu.up.cs301.CoupActions.IncomeAction;
import edu.up.cs301.CoupActions.StealAction;
import edu.up.cs301.CoupActions.TaxAction;
import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * A GUI of a counter-player. The GUI displays the current value of the counter,
 * and allows the human player to press the '+' and '-' buttons in order to
 * send moves to the game.
 * 
 * Just for fun, the GUI is implemented so that if the player presses either button
 * when the counter-value is zero, the screen flashes briefly, with the flash-color
 * being dependent on whether the player is player 0 or player 1.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class CoupHumanPlayer extends GameHumanPlayer implements OnClickListener {

	/* instance variables */
	
	// The TextView the displays the current counter value
	private TextView testResultsTextView;
	
	// the most recent game state, as given to us by the CounterLocalGame
	private CoupState state;
	
	// the android activity that we are running
	private GameMainActivity myActivity;

	private TextView deckText = null;




	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public CoupHumanPlayer(String name) {
		super(name);
	}

	/**
	 * Returns the GUI's top view object
	 * 
	 * @return
	 * 		the top object in the GUI's view heirarchy
	 */
	public View getTopView() {
		return myActivity.findViewById(R.id.coup_New_Layout);
	}
	
	/**
	 * sets the counter value in the text view
	 */
	protected void updateDisplay() {
		// set the text in the appropriate widget
	}

	/**
	 * this method gets called when the user clicks the '+' or '-' button. It
	 * creates a new CounterMoveAction to return to the parent activity.
	 * 
	 * @param button
	 * 		the button that was clicked
	 */
	public void onClick(View button) {
		// if we are not yet connected to a game, ignore
		if (game == null) return;

		// Construct the action and send it to the game
		GameAction action = null;

		//Clear Text From TextView
		testResultsTextView = new TextView(null);
		CoupState firstInstance = new CoupState();
		CoupState firstCopy = new CoupState(firstInstance);

		//Methods that Speedrun the game
		//creates the other player
		CoupHumanPlayer player2 = new CoupHumanPlayer("player2");
		//Player must draw assassin card otherwise they should restart
		//Use assasinate action on the other player twice to remove both their cards
		firstInstance.makeAssnAction(new AssassinateAction(this), player2,this);
		firstInstance.makeAssnAction(new AssassinateAction(this), player2,this);
		//if there are 3 players repeat
		CoupHumanPlayer player3 = new CoupHumanPlayer("player3");
		firstInstance.makeAssnAction(new AssassinateAction(this), player3,this);
		firstInstance.makeAssnAction(new AssassinateAction(this), player3,this);
		//Player 1 wins!

		//Makes a second base instance
		CoupState secondInstance = new CoupState();
		CoupState secondCopy = new CoupState(secondInstance);

		//Sends the String states to logcat
		String state1 = firstInstance.toString();
		String state2 = secondInstance.toString();
		Log.d("state1", state1);
		Log.d("state2", state2);


		//General
		/*if (button.getId() == R.id.taxButton) {
			action = new TaxAction(this);
			Log.d("Click", "Tax action was called");
		} else if (button.getId() == R.id.incomeButton) {
			action = new IncomeAction(this);
			Log.d("Click", "Income action was called");
		} else if (button.getId() == R.id.foreignAidButton) {
			action = new ForeignAideAction(this);
			Log.d("Click", "Foreign Aid action was called");
		} else if (button.getId() == R.id.assassinateButton) {
			action = new AssassinateAction(this);
			Log.d("Click", "Assassinate action was called");
		} else if (button.getId() == R.id.stealButton) {
			action = new StealAction(this);
			Log.d("Click", "Steal action was called");
		} else if (button.getId() == R.id.exchangeButton) {
			action = new ExchangeAction(this);
			Log.d("Click", "Exchange action was called");
		}*/
	}// onClick
	
	/**
	 * callback method when we get a message (e.g., from the game)
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		// ignore the message if it's not a CounterState message
		if (!(info instanceof CoupState)) return;

		if(state != null) {
			deckText.setText(String.valueOf(state.getPlayer0Money()));
		}

		// update our state; then update the display
		this.state = (CoupState)info;
		updateDisplay();

		//if(state != null) {
		//	deckText.setText(String.valueOf(state.getPlayer0Money()));
		//}
	}
	
	/**
	 * callback method--our game has been chosen/rechosen to be the GUI,
	 * called from the GUI thread
	 * 
	 * @param activity
	 * 		the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		this.myActivity = activity;

		// Load the layout resource for our GUI
		activity.setContentView(R.layout.coup_test_layout);

		testResultsTextView = activity.findViewById(R.id.edit_text);

		Button testButton = (Button) activity.findViewById(R.id.testButton);



		//deez nuts lmao

//		this.deckText = (TextView)activity.findViewById(R.id.deckText);
//
//		// Set this object as the listener for all buttons
//		Button taxButton = (Button) activity.findViewById(R.id.taxButton);
//		taxButton.setOnClickListener(this);
//
//		Button incomeButton = (Button) activity.findViewById(R.id.incomeButton);
//		incomeButton.setOnClickListener(this);
//
//		Button foreignAidButton = (Button) activity.findViewById(R.id.foreignAidButton);
//		foreignAidButton.setOnClickListener(this);
//
//		Button assassinateButton = (Button) activity.findViewById(R.id.assassinateButton);
//		assassinateButton.setOnClickListener(this);
//
//		Button stealButton = (Button) activity.findViewById(R.id.stealButton);
//		stealButton.setOnClickListener(this);
//
//		Button exchangeButton = (Button) activity.findViewById(R.id.exchangeButton);
//		exchangeButton.setOnClickListener(this);

	}

}// class CounterHumanPlayer

