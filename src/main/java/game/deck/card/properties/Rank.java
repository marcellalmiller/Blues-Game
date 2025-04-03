package game.deck.card.properties;

/**
 * The rank of a card.
 * The lower the rank, the better (so ONE is the best).
 */
public enum Rank {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7);

  /**
   * The corresponding number value of this Rank.
   * @return the corresponding number value of this Rank
   */
  public int number() {
    return number;
  }

  private final int number;

  Rank(int number) {
    this.number = number;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
