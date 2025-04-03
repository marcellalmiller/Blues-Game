package game.deck;

/**
 * Variations of a Blues deck. 'Standard' is a standard 56 card deck. 'Superstitious' is a 49 card
 *   deck with all star-suited cards removed.
 */
public enum DeckType {
  STANDARD, SUPERSTITIOUS;

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return this.name();
  }
}
