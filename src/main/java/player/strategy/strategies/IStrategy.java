package player.strategy.strategies;

import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import player.IPlayer;

/**
 * A strategy used by an instance of 'AIPlayer'. Determines the cards they choose and discard and
 *   the opponents they call No Blues on.
 */
public interface IStrategy {
  /**
   * Resets all fields to their original state in preparation for a new game.
   */
  void resetNewRound();

  /**
   * Returns a card from the player's current hand that they should discard.
   * @param hand the player's current hand
   * @param well the current well
   * @return recommended card to discard
   */
  Card recommendedDiscard(List<Card> hand, List<Card> well);

  /**
   * Returns a card from the well or pond that the player should choose.
   * @param hand the player's current hand
   * @param well the current well
   * @param pond the current pond
   * @return recommended card to choose
   */
  Card recommendedChoose(List<Card> hand, List<Card> well, List<Card> pond);

  /**
   * Returns an optional of the opponent the player should call No Blues on, or an empty Optional
   *   if they shouldn't call No Blues on an opponent.
   * @param opponents the player's opponents
   * @param well the current well
   * @return an optional of the recommended opponent to call No Blues on or an empty Optional
   */
  Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well);
}
