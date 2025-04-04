package game;

import java.util.List;
import java.util.Optional;

import game.deck.IDeck;
import game.deck.card.Card;
import game.observer.EventType;
import game.observer.Observer;
import game.rends.REndState;
import game.rends.NBCall;
import game.score.ScoreSheet;
import player.IPlayer;

/**
 * A game of Blues.
 */
public interface IGame {
  //************************************************************************************ SELF MODIFY
  /**
   * Clears the well, pond, and player hands. Shuffles the deck and deals each player five cards.
   */
  void startRound();

  /**
   * Adds cards to the well and sets their Position to WELL.
   * @throws IllegalStateException if the well isn't empty to start with
   */
  void flipWell();

  /**
   * Sets all pond card Positions to POND_F.
   * @throws IllegalStateException if the pond contains an invalid number of cards
   */
  void flipPond();

  /**
   * Resets fields in preparation for a new round.
   */
  void resetNewRound();

  /**
   * Resets fields in preparation for a new game with the same players and deck.
   */
  void resetNewGame();

  //*********************************************************************** SELF +/ PLAYER(S) MODIFY

  /**
   * Calls each IPlayer's askDiscard() method to get their desired discard card, then removes it
   *   from their hand via throwCard(), adds it to the pond, and sets its Positions to POND_H.
   * @throws IllegalStateException if the well or pond contains an invalid number of cards
   */
  void collectPond();

  /**
   * Calls each IPlayer's callNo() method (which returns an Optional NBCall) to get any 'No Blues'
   *   calls. Returns the call if there is one, an empty Optional if there isn't. If there are
   *   multiple, returns the call whose caller holds the best card (i.e., a card that trumps all the
   *   other caller's/callers' cards).
   * @return a call (Optional NBCall) if any are made, else an empty Optional
   * @throws IllegalStateException if the well or pond contains an invalid number of cards
   */
  Optional<NBCall> collectNBCs();

  /**
   * Calls each IPlayer's chooseCard() method in order of best to worst discarded pond card to get
   *   the card they want to add to their hand. Adds the card to their hand, sets its Position to
   *   HAND, and removes it from the well or pond. Prevents further choices if an addition gives a
   *   player a winning hand and handles blues REnd. Clears well after first two players have chosen
   *   and clears pond after all players have chosen. If there aren't enough cards in the deck to
   *   deal a full well for the next turn, handles deck empty REnd.
   * @throws IllegalStateException if the well or pond contains an invalid number of cards or if the
   *   pond hasn't been flipped
   */
  void allowChoices();

  //**************************************************************************************** GETTERS
  /**
   * Returns true if game points are greater or equal to amount of players * 25, false otherwise.
   * @return true if the game is over
   */
  boolean gameOver();

  /**
   * Returns true if the round is over, false otherwise.
   * @return true if the round is over
   */
  boolean roundOver();

  /**
   * Returns this game's pond. If the pond is empty, returns an empty list.
   * @return this game's pond
   */
  List<Card> getPond();

  /**
   * Returns this game's well. If the well is empty, returns an empty list.
   * @return this game's well
   */
  List<Card> getWell();

  /**
   * Returns this game's deck.
   * @return this game's deck
   */
  IDeck getDeck();

  /**
   * Returns a list of this game's players.
   * @return this game's players
   */
  List<IPlayer> getPlayers();

  /**
   * Returns this game's game points.
   * @return this game's game points
   */
  int gamePoints();

  /**
   * Returns an Optional of this game's round end state if the round is over, an empty Optional
   *   otherwise.
   * @return this game's round end state
   */
  Optional<REndState> getRendState();

  /**
   * Returns this game's score-sheet.
   * @return this game's score-sheet
   */
  ScoreSheet getScoreSheet();

  /**
   * Returns the player with the least points after the game is over. If multiple players are tied
   *   for the least points, the player who most recently won a round is the winner. If the game
   *   isn't over, returns null.
   * @return the winner of this game or null if the game isn't over
   */
  IPlayer getGameWinner();

  //************************************************************************** OBSERVER INTERACTIONS
  /**
   * Subscribes Observer 'o' to EventType notifications from the game.
   * @param o the Observer to be added
   */
  void addObserver(Observer o);

  /**
   * Notifies all subscribed Observers of the EventType and its associated data.
   * @param event the EventType
   * @param data the associated data
   */
  void updateObservers(EventType event, List<Object> data);

  //*************************************************************************** GOOD CLASS OVERRIDES
  String toString();

  boolean equals(Object other);

  int hashCode();
}
