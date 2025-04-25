package edu.up.cs301.Coup;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Contessa;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.CoupAction;
import edu.up.cs301.CoupActions.CoupDeteAction;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.w3c.dom.Text;

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
	private TextView player1DabloonsText = null;
	private TextView player2DabloonsText = null;

	private ImageView cardLeft = null;
	private ImageView cardRight = null;
	private ImageView deckButton = null;




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

		//General
		if (button.getId() == R.id.taxButton) {
			game.sendAction(new TaxAction(this));
			Log.d("Click", "Tax action was called");
		} else if (button.getId() == R.id.incomeButton) {
			game.sendAction(new IncomeAction(this));
			Log.d("Click", "Income action was called");
		} else if (button.getId() == R.id.foreignAidButton) {
			game.sendAction(new ForeignAideAction(this));
			Log.d("Click", "Foreign Aid action was called");
		} else if (button.getId() == R.id.assassinateButton) {
			game.sendAction(new AssassinateAction(this));
			Log.d("Click", "Assassinate action was called");
		} else if (button.getId() == R.id.stealButton) {
			game.sendAction(new StealAction(this));
			Log.d("Click", "Steal action was called");
		} else if (button.getId() == R.id.exchangeButton) {
			game.sendAction(new ExchangeAction(this));
			Log.d("Click", "Exchange action was called");
		}
		else if(button.getId() == R.id.coupButton){
			game.sendAction(new CoupDeteAction(this));
			Log.d("Click", "Coup action was called");
		}
		else if(button.getId() == R.id.cardBackCenter) {
			//game.sendAction(new IncomeAction(this));
		}

	}// onClick

	/**
	 * callback method when we get a message (e.g., from the game)
	 *
	 * @param info* 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		// ignore the message if it's not a CounterState message
		if (!(info instanceof CoupState)) return;

		if(state != null) {
			player1DabloonsText.setText(String.valueOf(state.getPlayer0Money()) + " francs");
			player2DabloonsText.setText(String.valueOf(state.getPlayer1Money()) + " francs");
			//deckText.setText(String.valueOf(state.getPlayer0Money()));
		}

		// update our state; then update the display
		state = (CoupState)info;

		//Creates a temporary variable of the Left and Right card
		GameAction tempLeft  = this.state.getplayer0Hand()[0];
		GameAction tempRight = this.state.getplayer0Hand()[1];

		//Initializes the drawable
		// left card
		if (tempLeft instanceof Ambassador) {
			cardLeft.setImageResource(R.drawable.bambassador);
		} else if (tempLeft instanceof Contessa) {
			cardLeft.setImageResource(R.drawable.bcontessa);
		} else if (tempLeft instanceof Captain) {
			cardLeft.setImageResource(R.drawable.bcaptain);
		} else if (tempLeft instanceof Assassin) {
			cardLeft.setImageResource(R.drawable.bassasin);
		} else if (tempLeft instanceof Duke) {
			cardLeft.setImageResource(R.drawable.bduke);
		}

		// right card
		if (tempRight instanceof Ambassador) {
			cardRight.setImageResource(R.drawable.bambassador);
		} else if (tempRight instanceof Contessa) {
			cardRight.setImageResource(R.drawable.bcontessa);
		} else if (tempRight instanceof Captain) {
			cardRight.setImageResource(R.drawable.bcaptain);
		} else if (tempRight instanceof Assassin) {
			cardRight.setImageResource(R.drawable.bassasin);
		} else if (tempRight instanceof Duke) {
			cardRight.setImageResource(R.drawable.bduke);
		}

		Log.d("Ass", "This.state.checkdead 0 is " + state.checkDead()[0]);

		if (state.checkDead()[0] == true) {
				Log.d("Ass", "Updating cardLeft to bduke");
				cardLeft.setImageResource(R.drawable.bduke);
			}

			if(state.checkDead()[1] == true) {
				Log.d("Ass", "Updating deckButton to bduke");
				deckButton.setImageResource(R.drawable.bduke);
			}

			updateDisplay();





		//if(state != null) {
		//	deckText.setText(String.valueOf(state.getPlayer0Money()));
		//}
	}

	/**
	 * A method to append a new description of an event to a TextView.
	 *
	 * @param test
	 * 		The textview to use as a reference
	 * @param event
	 * 	 * 	The text event that is happening. Use this to append to the target TextView
	 */
	public void describeAction(TextView test, String event) {
		String tempText = test.getText().toString();
		test.append("" + event);
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
		activity.setContentView(R.layout.coup_layout);

		//testResultsTextView = activity.findViewById(R.id.edit_text);

		//Button testButton = (Button) activity.findViewById(R.id.testButton);

		//testButton.setOnClickListener(this);

		//deez nuts lmao

		deckText = (TextView)activity.findViewById(R.id.deckText);
		player1DabloonsText = (TextView)activity.findViewById((R.id.playerDabloonsText));
		player2DabloonsText = (TextView)activity.findViewById((R.id.secondPlayerDabloonsText));

//
		// Set this object as the listener for all buttons
		Button taxButton = (Button) activity.findViewById(R.id.taxButton);
		taxButton.setOnClickListener(this);
//
		Button incomeButton = (Button) activity.findViewById(R.id.incomeButton);
		incomeButton.setOnClickListener(this);
//
		Button foreignAidButton = (Button) activity.findViewById(R.id.foreignAidButton);
		foreignAidButton.setOnClickListener(this);
//
		Button assassinateButton = (Button) activity.findViewById(R.id.assassinateButton);
		assassinateButton.setOnClickListener(this);

		Button stealButton = (Button) activity.findViewById(R.id.stealButton);
		stealButton.setOnClickListener(this);

		Button exchangeButton = (Button) activity.findViewById(R.id.exchangeButton);
		exchangeButton.setOnClickListener(this);

		Button coupButton = (Button) activity.findViewById(R.id.coupButton);
		coupButton.setOnClickListener(this);

		Button card1 = (Button) activity.findViewById(R.id.buttonCardOne);
		card1.setOnClickListener(this);

		Button card2 = (Button) activity.findViewById(R.id.buttonCardTwo);
		card2.setOnClickListener(this);

		Button challenge = (Button) activity.findViewById(R.id.buttonChallenge);
		challenge.setOnClickListener(this);

		this.cardLeft = (ImageView) activity.findViewById(R.id.playerCharacterCardLeft);
		this.cardRight = (ImageView) activity.findViewById(R.id.playerCharacterCardRight);

		this.deckButton = (ImageView) activity.findViewById(R.id.cardBackCenter);
		deckButton.setOnClickListener(this);

	}

}// class CounterHumanPlayer

