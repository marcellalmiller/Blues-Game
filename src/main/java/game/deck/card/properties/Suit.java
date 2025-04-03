package game.deck.card.properties;

/**
 * The Suit of a card (defined from strongest to weakest).
 */
public enum Suit {
  BLUE(0, Color.BLUE, "♦"),

  DASH(1, Color.BLACK, "~"),

  DROP(2, Color.BLACK, "⬯"),

  HEX(3, Color.BLACK, "⬡"),

  BOLT(4, Color.BLACK, "≷"),

  CROSS(5, Color.RED, "x"),

  HEART(6, Color.RED, "♡"),

  STAR(7, Color.RED, "⭒");

  /**
   * The point value of this Suit. Blue is 0 points, dash is 1, drop is 2, etc. (Ending with star,
   *     which is 7.)
   * @return the point value of this Suit
   */
  public int points() {
    return points;
  }

  /**
   * Returns the color of this suit. BLUE is blue, DASH, DROP, HEX, and BOLT are black, and CROSS,
   *     HEART, and STAR are red.
   * @return the color of this suit
   */
  public Color color() {
    return color;
  }

  /**
   * The symbol that corresponds with this Suit.
   * @return the symbol that corresponds with this Suit
   */
  public String symbol() {
    return symbol;
  }

  private final int points;
  private final Color color;
  private final String symbol;

  Suit(int points, Color color, String symbol) {
    this.points = points;
    this.color = color;
    this.symbol = symbol;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return symbol();
  }
}
