package player.strategy.strategies;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import game.deck.card.Card;
import player.IPlayer;
import player.strategy.Approach;

/**
 * A strategy that makes choices solely based on its 'Approach' and does not attempt to win.
 */
public class StrategyEmpty implements IStrategy {
  private final Approach approach;

  public StrategyEmpty(Approach approach) {
    this.approach = approach;
  }

  @Override
  public void resetNewRound() {
  }

  /**
   * Returns the first element of the list returned by 'approach.recDiscard()' with arguments 'hand'
   *   and 'well'.
   * @param hand the player's current hand
   * @param well the current well
   * @return first element of the list returned by 'approach.recDiscard()'
   */
  @Override
  public Card recommendedDiscard(List<Card> hand, List<Card> well) {
    return approach.recDiscard(hand).getFirst();
  }

  /**
   * Returns the first element of the list returned by 'approach.recChoose()' with arguments 'hand',
   *   'pond', and 'well'.
   * @param hand the player's current hand
   * @param pond the current well
   * @param well the current pond
   * @return the first element of the list returned by 'approach.recChoose()'
   */
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> pond, List<Card> well) {
    return approach.recChoose(hand, pond, well).getFirst();
  }

  /**
   * If 'approach' is 'Approach.MIN_POINTS' or 'Approach.MAX_TRUMP', always returns an empty
   *   'Optional'. If 'approach' is 'Approach.RANDOM', there is a 1% chance it returns an 'Optional'
   *   of a random opponent and a 99% chance it returns and empty 'Optional'.
   * @param opponents the player's opponents
   * @param well the current well
   * @return an empty Optional or an Optional of an opponent
   */
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    if (approach.equals(Approach.MIN_POINTS) || approach.equals(Approach.MAX_TRUMP)) {
      return Optional.empty();
    }

    Random r = new Random();
    if (approach.equals(Approach.RANDOM) && r.nextInt(0, 101) % 100 == 0) {
      return Optional.of(opponents.get(r.nextInt(0, opponents.size())));
    }
    return Optional.empty();
  }
}
