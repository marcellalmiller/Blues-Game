package player.strategy;

import java.util.List;

import game.deck.card.Card;

/**
 * The six distinct ways a player can win.  by point denominations. 'Perfect' and 'No Blues' are separated
 *   despite having the same point value because of the differences in win type ('Perfect' is a
 *   'Blues' call, 'No Blues' is a 'No Blues' call).
 */
public enum WinType {
  ORDINARY(0),
  RICH_WOMANS(-5),
  IMPERFECT(-10),
  PERFECT(-25),
  NO_BLUES(-25),
  FALSE_NO(0);

  /**
   * Returns the points this WinType corresponds to.
   * @return the points this WinType corresponds to
   */
  public int points() {
    return points;
  }

  // TODO: implement
  /**
   *
   * @param hand the hand to get the WinType for
   * @return
   * @throws IllegalArgumentException if hand is not a WinningHand
   */
  public static WinType winningHandType(List<Card> hand) {
    return null;
  }

  private final int points;

  WinType(int points) {
    this.points = points;
  }
}