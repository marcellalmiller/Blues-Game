package player;

import java.util.List;
import java.util.Optional;

import display.IDisplay;
import game.deck.card.Card;
import game.observer.Observer;

/**
 * A player in a Blues game.
 */
public interface IPlayer extends Observer {
  //************************************************************************************ SELF MODIFY
  /**
   * Chooses a card to discard in the pond, which will determine whether this Player can pick from
   *     the well and pond and when their turn is.
   * @param well the four well cards to consider
   * @return the card to discard
   */
  Card discard(List<Card> well);

  /**
   * Chooses a card to add to this Player's hand from the remaining pond and well cards. If the
   *     player is third or fourth to choose, the well is empty.
   * @param pond the remaining pond cards
   * @param well the remaining well cards
   * @return the chosen addition
   */
  Card chooseCard(List<Card> pond, List<Card> well);

  // TODO: update javadoc
  /**
   * This method is called every round after the pond has been placed but not flipped. If this
   *     Player thinks one of its opponents will win during this round, it can call "no blues" by
   *     returning a player. Else, it returns an empty optional.
   * @param opponents
   * @param well
   * @return an opposing Player if this Player thinks that opponent is about to win, empty otherwise
   */
  Optional<IPlayer> callNo(List<IPlayer> opponents, List<Card> well);

  // TODO: javadoc
  void resetNewGame();

  // TODO: javadoc
  void resetNewRound();

  //************************************************************************************ GAME MODIFY
  /**
   * Adds Card c to this player's hand.
   * @param c the card to add
   */
  void dealCard(Card c);

  /**
   * Removes Card c from this player's hand if it exists.
   * @param c the card to remove
   * @throws IllegalArgumentException if this player's hand does not contain c
   */
  void throwCard(Card c);

  /**
   * Adds newPoints points to this Player's hand.
   * @param p the amount of points to add
   */
  void addPoints(int p);

  //**************************************************************************************** SETTERS
  /**
   * Changes this Player's name field to new name
   * @param newName the desired name
   */
  void editName(String newName);

  /**
   * Sets this Player's display to d.
   * @param d the display
   */
  void setDisplay(IDisplay d);

  /**
   * Sets this Player's pond card to c and changes c's position to POND_H.
   * @param c the pond card
   */
  void setPondCard(Card c);

  /**
   * Sets this Player's pond card to an empty optional.
   */
  void setPondCard();

  //**************************************************************************************** GETTERS
  /**
   * Returns this player's hand.
   * @return this player's hand
   */
  List<Card> getHand();

  /**
   * Returns this Player's name.
   * @return this Player's name
   */
  String name();

  /**
   * Returns the amount of points this Player has accumulated over the course of the game.
   * @return the amount of points this Player has accumulated
   */
  int getPoints();

  /**
   * Returns this Player's pond card if they have one placed, else an empty optional.
   * @return this Player's pond card
   */
  Optional<Card> getPondCard();

  /**
   * Returns this Player's display, or null if none has been set.
   * @return this Player's display or null if none has been set
   */
  IDisplay getDisplay();

  //*************************************************************************** GOOD CLASS OVERRIDES
  String toString();

  boolean equals(Object other);

  int hashCode();
}
