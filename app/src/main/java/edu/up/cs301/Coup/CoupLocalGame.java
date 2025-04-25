package edu.up.cs301.Coup;

import edu.up.cs301.Characters.Ambassador;
import edu.up.cs301.Characters.Assassin;
import edu.up.cs301.Characters.Captain;
import edu.up.cs301.Characters.Contessa;
import edu.up.cs301.Characters.Duke;
import edu.up.cs301.CoupActions.AssassinateAction;
import edu.up.cs301.CoupActions.BlockAction;
import edu.up.cs301.CoupActions.ChallengeAction;
import edu.up.cs301.CoupActions.CoupDeteAction;
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

    private boolean wasBlocked = false;

    /**
     * can this player move
     *
     * @return true, because all player are always allowed to move at all times,
     * as this is a fully asynchronous game
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
        if (!(state instanceof CoupState)) {
            state = new CoupState();
        }
        this.gameState = (CoupState) state;
        super.state = state;
    }

    /**
     * The only type of GameAction that should be sent is CounterMoveAction
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());

        Log.d("Player", "Player idx[0] is " + getPlayerIdx(players[0]));
        Log.d("Player", "Player idx[1] is " + getPlayerIdx(players[1]));
        Log.d("Player", "PLayer ID is " + gameState.getPlayerId());
        Log.d("Ass", "Assassinate action was not called. Boolean makeDead 0 is " + gameState.checkDead()[0]);



        // Create a new Random instance
        Random rand = new Random();

        if (gameState.getPlayerId() == getPlayerIdx(players[0]) && players[0] instanceof CoupHumanPlayer) {

            if (action instanceof AssassinateAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Assassin || tempHand[1] instanceof Assassin) {
                    if (gameState.getPlayer0Money() >= 3) {
                        gameState.setPlayer0Money(gameState.getPlayer0Money() - 3);

                        // Grab the current dead cards
                        boolean[] deadInfluences = gameState.checkDead();

                        // Randomly pick either 0 or 1 to kill
                        Random random = new Random();
                        int victim = random.nextInt(2);  // returns 0 or 1

                        // Keep choosing new victims until we find somebody alive
                        while (deadInfluences[victim]) {
                            victim = random.nextInt(2);
                        }

                        gameState.makeDead(victim);
                        Log.d("Ass", "Assassinate action was called. Boolean makeDead 0 is " + gameState.checkDead()[0]);



                        Log.d("Money", "Assassinate action was called. Money is " + gameState.getPlayer0Money());
                        // Additional logic for AssassinateAction
                    }
                }
            }

            if (action instanceof BlockAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Contessa || tempHand[1] instanceof Contessa) {
                    Log.d("Money", "Block action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for BlockAction
                }
            }

            if (action instanceof ChallengeAction) { //todo
                    Log.d("Money", "Challenge action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for ChallengeAction
            }

            if (action instanceof ExchangeAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Ambassador || tempHand[1] instanceof Ambassador) {
                    //Creates a copy of the original deck
                    GameAction[] deckCopy = gameState.getDeck();
                    int deckSize = deckCopy.length;

                    // pick two random cards from the deck
                    int randCard1 = rand.nextInt(deckSize);
                    int randCard2 = rand.nextInt(deckSize);
                    //Ensures we get 2 unique cards

                    while (randCard2 == randCard1) {
                        randCard2 = rand.nextInt(deckSize);
                    }

                    //draw those two cards from the deck
                    GameAction newCard1 = deckCopy[randCard1];
                    GameAction newCard2 = deckCopy[randCard2];

                    //Uses those cards to set the player's hand with a new hand
                    gameState.setplayer0Hand(newCard1, newCard2);

                    Log.d("Money", "Exchange action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for ExchangeAction
                }
            }

            if (action instanceof ForeignAideAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Duke || tempHand[1] instanceof Duke) {
                    gameState.setPlayer0Money(gameState.getPlayer0Money() + 2);
                    Log.d("Money", "Foreign Aide action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for ForeignAideAction
                }
            }

            if (action instanceof IncomeAction) {

                    gameState.setPlayer0Money(gameState.getPlayer0Money() + 1);
                    Log.d("Money", "Income action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for IncomeAction
            }

            if (action instanceof StealAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Captain || tempHand[1] instanceof Captain
                        || tempHand[0] instanceof Ambassador || tempHand[1] instanceof Ambassador) {
                    if (gameState.getPlayer1Money() >= 2) {
                        gameState.setPlayer1Money(gameState.getPlayer1Money() - 2);
                        gameState.setPlayer0Money(gameState.getPlayer0Money() + 2);
                    } else if (gameState.getPlayer1Money() >= 1) {
                        gameState.setPlayer1Money(gameState.getPlayer1Money() - 1);
                        gameState.setPlayer0Money(gameState.getPlayer0Money() + 1);
                    }
                    Log.d("Money", "Steal action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for StealAction
                }
            }

            if (action instanceof TaxAction) { //todo
                GameAction[] tempHand = gameState.getplayer0Hand();
                if (tempHand[0] instanceof Duke || tempHand[1] instanceof Duke) {
                    gameState.setPlayer0Money(gameState.getPlayer0Money() + 3);
                    Log.d("Money", "Tax action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for TaxAction
                }
            }

            if (action instanceof CoupDeteAction) { //todo
                if ( gameState.getPlayer0Money() >= 7) {
                    gameState.setPlayer0Money(gameState.getPlayer0Money() - 7);

                    // Grab the current dead cards
                    boolean[] deadInfluences = gameState.checkDead();

                    // Randomly pick either 0 or 1 to kill
                    Random random = new Random();
                    int victim = random.nextInt(2);  // returns 0 or 1

                    // Keep choosing new victims until we find somebody alive
                    while (deadInfluences[victim]) {
                        victim = random.nextInt(2);
                    }

                    gameState.makeDead(victim);

                    Log.d("Money", "Coup action was called. Money is " + gameState.getPlayer0Money());
                    // Additional logic for CoupAction
                }
            }

            gameState.setPlayerId(getPlayerIdx(players[1]));
            return true;

        }

        if (gameState.getPlayerId() == getPlayerIdx(players[1]) && players[1] instanceof CoupComputerPlayer1) {

            if (action instanceof AssassinateAction) { //todo
                GameAction[] tempHand = gameState.getplayer1Hand();
                if (tempHand[0] instanceof Assassin || tempHand[1] instanceof Assassin) {
                    if (gameState.getPlayer1Money() >= 3) {
                        gameState.setPlayer1Money(gameState.getPlayer1Money() - 3);
                        Log.d("Money", "Assassinate action was called. Money is " + gameState.getPlayer1Money());
                        // Additional logic for AssassinateAction
                    }
                }
            }

            if (action instanceof BlockAction) { //todo
                GameAction[] tempHand = gameState.getplayer1Hand();
                if (tempHand[0] instanceof Contessa || tempHand[1] instanceof Contessa) {
                    Log.d("Money", "Block action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for BlockAction
                }
            }

            if (action instanceof ChallengeAction) { //todo
                    Log.d("Money", "Challenge action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for ChallengeAction
            }

            if (action instanceof ExchangeAction) { //todo
                    GameAction[] tempHand = gameState.getplayer0Hand();

                    if (tempHand[0] instanceof Ambassador || tempHand[1] instanceof Ambassador) {
                        //Creates a copy of the original deck
                        GameAction[] deckCopy = gameState.getDeck();
                        int deckSize = deckCopy.length;

                        // pick two random cards from the deck
                        int randCard1 = rand.nextInt(deckSize);
                        int randCard2 = rand.nextInt(deckSize);
                        //Ensures we get 2 unique cards

                        while (randCard2 == randCard1) {
                            randCard2 = rand.nextInt(deckSize);
                        }

                        //draw those two cards from the deck
                        GameAction newCard1 = deckCopy[randCard1];
                        GameAction newCard2 = deckCopy[randCard2];

                        //Uses those cards to set the player's hand with a new hand
                        gameState.setplayer2Hand(newCard1, newCard2);


                    Log.d("Money", "Exchange action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for ExchangeAction
                }
            }

            if (action instanceof ForeignAideAction) { //todo
                GameAction[] tempHand = gameState.getplayer1Hand();
                if (tempHand[0] instanceof Duke || tempHand[1] instanceof Duke) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() + 2);
                    Log.d("Money", "Foreign Aide action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for ForeignAideAction
                }
            }

            if (action instanceof IncomeAction) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() + 1);
                    Log.d("Money", "Income action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for IncomeAction
            }

            if (action instanceof StealAction) { //todo
                GameAction[] tempHand = gameState.getplayer1Hand();
                if (tempHand[0] instanceof Captain || tempHand[1] instanceof Captain
                        || tempHand[0] instanceof Ambassador || tempHand[1] instanceof Ambassador) {
                    if (gameState.getPlayer0Money() >= 2) {
                        gameState.setPlayer0Money(gameState.getPlayer0Money() - 2);
                        gameState.setPlayer1Money(gameState.getPlayer1Money() + 2);
                    } else if (gameState.getPlayer0Money() >= 1) {
                        gameState.setPlayer0Money(gameState.getPlayer0Money() - 1);
                        gameState.setPlayer1Money(gameState.getPlayer1Money() + 1);
                    }
                    Log.d("Money", "Steal action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for StealAction
                }
            }

            if (action instanceof TaxAction) { //todo
                GameAction[] tempHand = gameState.getplayer1Hand();
                if (tempHand[0] instanceof Duke || tempHand[1] instanceof Duke) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() + 3);
                    Log.d("Money", "Tax action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for TaxAction
                }
            }

            if (action instanceof CoupDeteAction) { //todo
                if (gameState.getPlayer1Money() >= 7) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() - 7);
                    Log.d("Money", "Coup action was called. Money is " + gameState.getPlayer1Money());
                    // Additional logic for CoupAction
                }
            }

            gameState.setPlayerId(getPlayerIdx(players[0]));
            return true;
        }

        return false;
    } // makeMove


    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        // this is a perfect-information game, so we'll make a
        // complete copy of the state to send to the player

        GameState tempState = gameState.getGameState();
        p.sendInfo(tempState);

    }//sendUpdatedSate

    /**
     * Check if the game is over. It is over, return a string that tells
     * who the winner(s), if any, are. If the game is not over, return null;
     *
     * @return a message that tells who has won the game, or null if the
     * game is not over
     */
    @Override
    protected String checkIfGameOver() {
        Log.d("Over", "We're so back");


        if (gameState.checkDead()[0] == true && gameState.checkDead()[1] == true) {
            Log.d("Over", "It's so over");
            return "The game is over! Player 1 won by killing all Influences! ";
        }

        return null;
    }

}


// class CounterLocalGame
