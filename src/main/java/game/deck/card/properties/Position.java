package game.deck.card.properties;

/**
 * The position of a card; where the card is.
 * POND_F means pond flipped (visible to all players), POND_H means hidden (not yet flipped).
 */
public enum Position {
  POND_F, POND_H, WELL, DECK, HAND, DISCARDED;

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return this.name();
  }
}
