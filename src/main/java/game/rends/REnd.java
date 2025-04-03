package game.rends;

/**
 * All the ways a Blues round can end, respectively: a player calls 'Blues'; a player correctly
 *   calls 'No Blues'; a player incorrectly calls 'No Blues'; the deck runs out.
 */
public enum REnd {
  BLUES, TRUE_NO, FALSE_NO, DECK_EMPTY;

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return this.name();
  }
}
