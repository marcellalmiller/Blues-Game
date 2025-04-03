package game.deck.card;

import java.util.Arrays;
import java.util.Objects;

import game.deck.card.properties.Color;
import game.deck.card.properties.Position;
import game.deck.card.properties.Rank;
import game.deck.card.properties.Suit;

/**
 * A Card in a standard 56 card deck - a wrapper around an 'SCard' enum value. Allows classes to
 *   monitor a card's position in a game and multiple instances of the "same" card at once.
 */
public class Card {
  SCard card; // the associated enum value
  Position p; // pond hidden, pond flipped, well, deck, hand, or discarded

  /**
   * Creates a card for gameplay. Position is set to card.Position.DECK by default.
   * @param card the wrapped enum value
   */
  public Card(SCard card) {
    this.card = card;
    this.p = Position.DECK;
  }

  //**************************************************************************************** SETTERS
  /**
   * Sets p to newPosition.
   * @param newPosition the new position
   */
  public void setPosition(Position newPosition) {
    this.p = newPosition;
  }

  //**************************************************************************************** GETTERS
  /**
   * Returns this Card's color.
   * @return this Card's color
   */
  public Color getColor() {
    return card.getSuit().color();
  }

  /**
   * Returns this Card's rank.
   * @return this Card's rank
   */
  public Rank getRank() {
    return card.getRank();
  }

  /**
   * Returns this Card's suit.
   * @return this Card's suit
   */
  public Suit getSuit() {
    return card.getSuit();
  }

  /**
   * Returns this Card's position.
   * @return this Card's position
   */
  public Position getPosition() {
    return p;
  }

  /**
   * Returns toString() with by ANSI color codes corresponding to this card's color - for use in
   *   instances of StringDisplay.
   * @return toString() with by ANSI color codes
   */
  public String coloredTS() {
    return card.getSuit().color().ansiColor().getFirst() + this
            + card.getSuit().color().ansiColor().getLast();
  }

  /**
   * Returns the amount of cards in a StandardDeck this card trumps.
   * @return the amount of cards in a StandardDeck this card trumps
   */
  public int trumpsAmount() {
    int trumpsAmount = 0;
    if (this.getColor() != Color.BLUE) {
      trumpsAmount += 7 * (7 - this.getRank().number());
      trumpsAmount += 7 - Arrays.stream(Suit.values()).toList().indexOf(this.getSuit());
      return trumpsAmount;
    }
    trumpsAmount += 56 - this.getRank().number();
    return trumpsAmount;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return card.getRank().number() + card.getSuit().symbol();
  }

  /**
   * Does NOT take into account Position. Two cards with identical rank and suit but different
   *   positions are equal according to this method.
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Card o = (Card) other;
    return this.card.equals(o.card);
  }

  @Override
  public int hashCode() {
    return Objects.hash(card);
  }
}
