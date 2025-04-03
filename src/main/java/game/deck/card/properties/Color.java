package game.deck.card.properties;

import java.util.List;

/**
 * The color of a card.
 */
public enum Color {
  RED, BLACK, BLUE;

  /**
   * Returns a list with this color's start and end ANSI codes (start is index 0, end is index 1).
   * @return this color's ANSI codes
   */
  public List<String> ansiColor() {
    return switch (this) {
      case RED -> List.of("\033[31m", "\033[0m");
      case BLACK -> List.of("\033[37m", "\033[0m");
      case BLUE -> List.of("\033[36m", "\033[0m");
    };
  }

  /**
   * Returns a list of the suits of this color.
   * @return the suits of this color
   */
  public List<Suit> suits() {
    return switch (this) {
      case RED -> List.of(Suit.CROSS, Suit.HEART, Suit.STAR);
      case BLACK -> List.of(Suit.DASH, Suit.DROP, Suit.HEX, Suit.BOLT);
      case BLUE -> List.of(Suit.BLUE);
    };
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
  }
}
