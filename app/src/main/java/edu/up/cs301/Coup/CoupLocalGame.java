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
 * A class that represents the state of a game. For the coup game
 * this contains all of the actions that can be used, and records
 * when a move is made. Also handles blocking, applicable actions and alternates turns between players
 *
 * @author Sean Yang, Clint Sizemore, Kanoa Martin
 * @version 4-24-25
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

        // Log calls for action state reports
        Log.i("action", action.getClass().toString());
        Log.d("Player", "Player idx[0] is " + getPlayerIdx(players[0]));
        Log.d("Player", "Player idx[1] is " + getPlayerIdx(players[1]));
        Log.d("Player", "Player ID is " + gameState.getPlayerId());
        Log.d("Ass", "Assassinate action was not called. Boolean makeDead 0 is " + gameState.checkplayer0Dead()[0]);

        // Create a new Random instance for any randomized choices
        Random rand = new Random();

        // Only proceed if it's player 0's turn and the opponent is a human player
        if (gameState.getPlayerId() == getPlayerIdx(players[0]) && players[0] instanceof CoupHumanPlayer) {

            // Handle an assassination attempt from the human player
            if (action instanceof AssassinateAction) {
                GameAction[] tempHand = gameState.getplayer0Hand();   // the human's cards
                GameAction[] oppHand  = gameState.getplayer1Hand();   // AI's cards

                // Check human has a live Assassin and enough coins to assassinate
                if ((tempHand[0] instanceof Assassin && !gameState.checkplayer1Dead()[0])
                        || (tempHand[1] instanceof Assassin && !gameState.checkplayer1Dead()[1])) {
                    if (gameState.getPlayer0Money() >= 3) {
                        // Deduct the assassination cost from the player
                        gameState.setPlayer0Money(gameState.getPlayer0Money() - 3);

                        //Grab the current dead cards for AI for reference
                        boolean[] deadInfluences = gameState.checkplayer0Dead();

                        // Randomly pick which influence to lose, retrying if that Influence is already dead
                        Random random = new Random();
                        int victim = random.nextInt(2);
                        while (deadInfluences[victim]) {
                            victim = random.nextInt(2);
                        }

                        // Only kill if the AI has no Contessa to block. Still deducts cost of assassination if it is blocked
                        if (!(oppHand[0] instanceof Contessa && !gameState.checkplayer1Dead()[0])
                                && !(oppHand[1] instanceof Contessa && !gameState.checkplayer1Dead()[1])) {
                            gameState.make0Dead(victim);
                        }

                        Log.d("Ass", "Assassinate action was called. Boolean makeDead 0 is "
                                + gameState.checkplayer0Dead()[0]);
                        Log.d("Money", "Assassinate action was called. Money is "
                                + gameState.getPlayer0Money());
                        // End of Assassinate handling
                    }
                }
            }

            // Handle a Contessa block by the human player
            if (action instanceof BlockAction) {
                GameAction[] tempHand = gameState.getplayer0Hand();
                // If human has a live Contessa, they successfully block
                if ((tempHand[0] instanceof Contessa && !gameState.checkplayer1Dead()[0])
                        || (tempHand[1] instanceof Contessa && !gameState.checkplayer1Dead()[1])) {
                    Log.d("Money", "Block action was called. Money is "
                            + gameState.getPlayer0Money());
                    // End of BlockAction handling
                }
            }

            // Handle any challenge action from the human
            if (action instanceof ChallengeAction) {
                Log.d("Money", "Challenge action was called. Money is "
                        + gameState.getPlayer0Money());
                // End of ChallengeAction handling
            }

            // Handle an exchange request from the human player
            if (action instanceof ExchangeAction) {
                GameAction[] tempHand = gameState.getplayer0Hand();
                // Only allow if human claims has a living Ambassador
                if ((tempHand[0] instanceof Ambassador && !gameState.checkplayer1Dead()[0])
                        || (tempHand[1] instanceof Ambassador && !gameState.checkplayer1Dead()[1])) {
                    // Copy the deck and pick two unique random cards from the deck
                    GameAction[] deckCopy = gameState.getDeck();
                    int deckSize = deckCopy.length;
                    int randCard1 = rand.nextInt(deckSize);
                    int randCard2 = rand.nextInt(deckSize);
                    while (randCard2 == randCard1) {
                        randCard2 = rand.nextInt(deckSize);
                    }
                    GameAction newCard1 = deckCopy[randCard1];
                    GameAction newCard2 = deckCopy[randCard2];

                    // Replace the human's hand with the new cards drawn from the deck
                    gameState.setplayer0Hand(newCard1, newCard2);
                    Log.d("Money", "Exchange action was called. Money is "
                            + gameState.getPlayer0Money());
                    // End of ExchangeAction handling
                }
            }

            // Handle Foreign Aid request from the human
            if (action instanceof ForeignAideAction) {
                GameAction[] oppHand = gameState.getplayer1Hand();  // AI's cards
                // Only award 2 coins if there is no Duke to block the action
                if (!(oppHand[0] instanceof Duke && !gameState.checkplayer1Dead()[0])
                        && !(oppHand[1] instanceof Duke && !gameState.checkplayer1Dead()[1])) {
                    gameState.setPlayer0Money(gameState.getPlayer0Money() + 2);
                    Log.d("Money", "Foreign Aide action was called. Money is "
                            + gameState.getPlayer0Money());
                    // End of ForeignAideAction handling
                }
            }

            // Handle Income action (always allowed)
            if (action instanceof IncomeAction) {
                gameState.setPlayer0Money(gameState.getPlayer0Money() + 1);
                Log.d("Money", "Income action was called. Money is "
                        + gameState.getPlayer0Money());
                // End of IncomeAction handling
            }

            // Handle Steal action from the human
            if (action instanceof StealAction) {
                GameAction[] tempHand = gameState.getplayer0Hand();   // human's cards
                GameAction[] oppHand  = gameState.getplayer1Hand();   // AI's cards
                // Must have a live Captain to attempt the steal
                if ((tempHand[0] instanceof Captain && !gameState.checkplayer1Dead()[0])
                        || (tempHand[1] instanceof Captain && !gameState.checkplayer1Dead()[1])) {
                    // Opponent must have no live Captain or Ambassador to block
                    if (!(oppHand[0] instanceof Ambassador && !gameState.checkplayer1Dead()[0])
                            && !(oppHand[1] instanceof Ambassador && !gameState.checkplayer1Dead()[1])
                            && !(oppHand[0] instanceof Captain && !gameState.checkplayer1Dead()[0])
                            && !(oppHand[1] instanceof Captain && !gameState.checkplayer1Dead()[1])) {
                        // Transfer coins up to the amount available (up to 2). Deducts stolen coins from other player
                        if (gameState.getPlayer1Money() >= 2) {
                            gameState.setPlayer1Money(gameState.getPlayer1Money() - 2);
                            gameState.setPlayer0Money(gameState.getPlayer0Money() + 2);
                        } else if (gameState.getPlayer1Money() >= 1) {
                            gameState.setPlayer1Money(gameState.getPlayer1Money() - 1);
                            gameState.setPlayer0Money(gameState.getPlayer0Money() + 1);
                        }
                        Log.d("Money", "Steal action was called. Money is "
                                + gameState.getPlayer0Money());
                        // End of StealAction handling
                    }
                }
            }

            // Handle Tax action if human claims Duke
            if (action instanceof TaxAction) {
                GameAction[] tempHand = gameState.getplayer0Hand();
                if ((tempHand[0] instanceof Duke && !gameState.checkplayer1Dead()[0])
                        || (tempHand[1] instanceof Duke && !gameState.checkplayer1Dead()[1])) {
                    gameState.setPlayer0Money(gameState.getPlayer0Money() + 3);
                    Log.d("Money", "Tax action was called. Money is "
                            + gameState.getPlayer0Money());
                    // End of TaxAction handling
                }
            }

            // Handle a Coup action from the human
            if (action instanceof CoupDeteAction) {
                if (gameState.getPlayer0Money() >= 7) {
                    // Deduct 7 coins for the coup. Coup cannot be blocked
                    gameState.setPlayer0Money(gameState.getPlayer0Money() - 7);

                    // Determine which influence to remove. Always ensures the Influence is alive (cannot kill a dead Influence)
                    boolean[] deadInfluences = gameState.checkplayer0Dead();
                    Random random = new Random();
                    int victim = random.nextInt(2);
                    while (deadInfluences[victim]) {
                        victim = random.nextInt(2);
                    }
                    gameState.make0Dead(victim);

                    Log.d("Money", "Coup action was called. Money is "
                            + gameState.getPlayer0Money());
                    // End of CoupDeteAction handling
                }
            }

            // Set next player turn to player 1 (AI) and indicate action was accepted. Ends turn
            gameState.setPlayerId(getPlayerIdx(players[1]));
            return true;
        }


        // Only proceed if it's player 1's turn and the opponent is a computer player
        if (gameState.getPlayerId() == getPlayerIdx(players[1]) && (players[1] instanceof CoupComputerPlayer1 || players[1] instanceof CoupSmartComputerPlayer)) {

            // Handle an assassination attempt from the computer player
            if (action instanceof AssassinateAction) {
                GameAction[] tempHand = gameState.getplayer1Hand();   // computer player's face-down cards
                GameAction[] oppHand  = gameState.getplayer0Hand();   // human's face-down cards
                // Check computer has a live Assassin and enough coins to assassinate
                if ((tempHand[0] instanceof Assassin && !gameState.checkplayer0Dead()[0])
                        || (tempHand[1] instanceof Assassin && !gameState.checkplayer0Dead()[1])) {
                    if (gameState.getPlayer1Money() >= 3) {
                        // Deduct the assassination cost from the computer
                        gameState.setPlayer1Money(gameState.getPlayer1Money() - 3);

                        // Grab the current dead cards for human for reference
                        boolean[] deadInfluences = gameState.checkplayer1Dead();

                        // Randomly pick either 0 or 1 to kill
                        Random random = new Random();
                        int victim = random.nextInt(2);  // returns 0 or 1

                        // Keep choosing new victims until we find somebody alive
                        while (deadInfluences[victim]) {
                            victim = random.nextInt(2);
                        }

                        // Only kill if the human has no Contessa to block. Still deducts cost if blocked
                        if (!(oppHand[0] instanceof Contessa && !gameState.checkplayer0Dead()[0])
                                && !(oppHand[1] instanceof Contessa && !gameState.checkplayer0Dead()[1])) {
                            gameState.make1Dead(victim);
                        }

                        Log.d("Ass", "Assassinate action was called. Boolean makeDead 0 is " + gameState.checkplayer0Dead()[victim]); // log result
                        Log.d("Money", "Assassinate action was called. Money is " + gameState.getPlayer1Money()); // log new money
                        // End of AssassinateAction handling
                    }
                }
            }

            // Handle a Contessa block by the computer player
            if (action instanceof BlockAction) {
                GameAction[] tempHand = gameState.getplayer1Hand(); // computer player's cards
                // If computer has a live Contessa, they successfully block
                if ((tempHand[0] instanceof Contessa && !gameState.checkplayer0Dead()[0])
                        || (tempHand[1] instanceof Contessa && !gameState.checkplayer0Dead()[1])) {
                    Log.d("Money", "Block action was called. Money is " + gameState.getPlayer1Money()); // log block
                    // End of BlockAction handling
                }
            }

            // Handle any challenge action from the computer player
            if (action instanceof ChallengeAction) {
                Log.d("Money", "Challenge action was called. Money is " + gameState.getPlayer1Money()); // log challenge
                // End of ChallengeAction handling
            }

            // Handle an exchange request from the computer player
            if (action instanceof ExchangeAction) {
                GameAction[] tempHand = gameState.getplayer1Hand(); // computer player's cards
                // Only allow if computer claims has a living Ambassador
                if ((tempHand[0] instanceof Ambassador && !gameState.checkplayer0Dead()[0])
                        || (tempHand[1] instanceof Ambassador && !gameState.checkplayer0Dead()[1])) {
                    // Copies the deck
                    GameAction[] deckCopy = gameState.getDeck();
                    int deckSize = deckCopy.length;
                    // Picks two random cards from the deck
                    int randCard1 = rand.nextInt(deckSize);
                    int randCard2 = rand.nextInt(deckSize);
                    // Ensures two unique cards
                    while (randCard2 == randCard1) {
                        randCard2 = rand.nextInt(deckSize);
                    }
                    // Draws those two cards from the deck
                    GameAction newCard1 = deckCopy[randCard1];
                    GameAction newCard2 = deckCopy[randCard2];
                    // Uses those cards to set the computer's hand with a new one
                    gameState.setplayer2Hand(newCard1, newCard2);
                    Log.d("Money", "Exchange action was called. Money is " + gameState.getPlayer1Money()); // log exchange
                    // End of ExchangeAction handling
                }
            }

            // Handle Foreign Aid request from the computer player
            if (action instanceof ForeignAideAction) {
                GameAction[] tempHand = gameState.getplayer1Hand(); // computer player's cards
                GameAction[] oppHand  = gameState.getplayer0Hand(); // human's cards
                // Only award 2 coins if there is no Duke to block the action
                if (!(oppHand[0] instanceof Duke && !gameState.checkplayer0Dead()[0])
                        && !(oppHand[1] instanceof Duke && !gameState.checkplayer0Dead()[1])) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() + 2); // gain 2 coins
                    Log.d("Money", "Foreign Aide action was called. Money is " + gameState.getPlayer1Money()); // log foreign aid
                    // End of ForeignAideAction handling
                }
            }

            // Handle Income action (always allowed)
            if (action instanceof IncomeAction) {
                gameState.setPlayer1Money(gameState.getPlayer1Money() + 1); // gain 1 coin
                Log.d("Money", "Income action was called. Money is " + gameState.getPlayer1Money()); // log income
                // End of IncomeAction handling
            }

            // Handle Steal action from the computer player
            if (action instanceof StealAction) {
                GameAction[] tempHand = gameState.getplayer1Hand();   // computer player's cards
                GameAction[] oppHand  = gameState.getplayer0Hand();   // human's cards
                // Must have a living Captain to attempt the steal
                if ((tempHand[0] instanceof Captain && !gameState.checkplayer0Dead()[0])
                        || (tempHand[1] instanceof Captain && !gameState.checkplayer0Dead()[1])) {
                    // Opponent must have no live Captain or Ambassador to block
                    if (!(oppHand[0] instanceof Captain && !gameState.checkplayer0Dead()[0])
                            && !(oppHand[1] instanceof Captain && !gameState.checkplayer0Dead()[1])
                            && !(oppHand[0] instanceof Ambassador && !gameState.checkplayer0Dead()[0])
                            && !(oppHand[1] instanceof Ambassador && !gameState.checkplayer0Dead()[1])) {
                        //Checks if the opposing player has enough money to steal. Awards up to 2 coins
                        //Also deducts value of stolen coins from opposing player
                        if (gameState.getPlayer0Money() >= 2) {
                            gameState.setPlayer0Money(gameState.getPlayer0Money() - 2); // steal two coins
                            gameState.setPlayer1Money(gameState.getPlayer1Money() + 2);
                        } else if (gameState.getPlayer0Money() >= 1) {
                            gameState.setPlayer0Money(gameState.getPlayer0Money() - 1); // steal one coin
                            gameState.setPlayer1Money(gameState.getPlayer1Money() + 1);
                        }
                        Log.d("Money", "Steal action was called. Money is " + gameState.getPlayer1Money()); // log steal
                        // End of StealAction handling
                    }
                }
            }

            // Handle Tax action if computer claims Duke
            if (action instanceof TaxAction) {
                GameAction[] tempHand = gameState.getplayer1Hand(); // computer player's cards
                // Check for live Duke before taxing
                if ((tempHand[0] instanceof Duke && !gameState.checkplayer0Dead()[0])
                        || (tempHand[1] instanceof Duke && !gameState.checkplayer0Dead()[1])) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() + 3); // gain 3 coins
                    Log.d("Money", "Tax action was called. Money is " + gameState.getPlayer1Money()); // log tax
                    // End of TaxAction handling
                }
            }

            // Handle a Coup action from the computer player
            if (action instanceof CoupDeteAction) {
                // Check if computer has enough money. If yes, deducts 7 points from the AI
                if (gameState.getPlayer1Money() >= 7) {
                    gameState.setPlayer1Money(gameState.getPlayer1Money() - 7); // pay cost
                    boolean[] deadInfluences = gameState.checkplayer1Dead(); // computer dead influences
                    //Picks a random, living Influence to execute (man this game is kind of violent)
                    Random random = new Random();
                    int victim = random.nextInt(2);
                    while (deadInfluences[victim]) {
                        victim = random.nextInt(2);
                    }
                    gameState.make1Dead(victim); // execute the coup and kills that Influence
                    Log.d("Money", "Coup action was called. Money is " + gameState.getPlayer1Money()); // log coup
                    // End of CoupDeteAction handling
                }
            }

            gameState.setPlayerId(getPlayerIdx(players[0])); // switch to human turn
            return true;
        }

        return false;
    }




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

        //Checks if a player has no cards left, and if so ends the game
        if (gameState.checkplayer0Dead()[0] == true && gameState.checkplayer0Dead()[1] == true) {
            Log.d("Over", "It's so over");
            return "The game is over! Player 1 won by killing all Influences! ";}

        if (gameState.checkplayer1Dead()[0] == true && gameState.checkplayer1Dead()[1] == true) {
            Log.d("Over", "It's so over");
            return "You lost! The computer won by killing all of the human player's Influences! ";
        }

        return null;
    }

}


// class CounterLocalGame
