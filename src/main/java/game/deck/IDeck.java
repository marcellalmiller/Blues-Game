package game.deck;

import java.util.ArrayList;
import java.util.Deque;

import game.deck.card.Card;

/**
 * A deck of cards.
 */
public interface IDeck {
  //************************************************************************************** MODIFIERS
  /**
   * Shuffles the cards in a random order.
   */
  void shuffle();

  /**
   * Adds the top card to list 'dealt', removes it from 'cards', and returns it.
   * @return the top card
   * @throws IllegalArgumentException if deck is empty
   */
  Card popCard();

  /**
   * Resets the deck to its state upon construction.
   */
  void resetDeck();

  //**************************************************************************************** GETTERS
  /**
   * Returns the top card, does not remove it from 'cards' or add it to 'dealt'.
   * @return the top card
   * @throws IllegalArgumentException if deck is empty
   */
  Card peekCard();

  /**
   * Returns the amount of cards left in the deck.
   * @return the amount of cards left in the deck
   */
  int cardsSize();

  /**
   * Returns the cards that have been dealt from the deck
   * @return the cards that have been dealt from the deck
   */
  ArrayList<Card> getDealt();

  /**
   * Returns the cards in the deck (this does not include the dealt cards).
   * @return the cards in the deck
   */
  Deque<Card> getCards();

  /**
   * Returns the amount of cards that have been dealt.
   * @return the amount of cards that have been dealt
   */
  int dealtSize();
}
