package player.strategy.strategies;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import game.deck.card.Card;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IStrategy;
// TODO: javadoc
public class StrategyEmpty implements IStrategy {
  private final Approach approach;

  // TODO: javadoc
  public StrategyEmpty(Approach approach) {
    this.approach = approach;
  }

  // TODO: javadoc
  @Override
  public void resetNewRound() {
  }

  // TODO: javadoc
  @Override
  public Card recommendedDiscard(List<Card> hand, List<Card> well) {
    return approach.recDiscard(hand).getFirst();
  }

  // TODO: javadoc
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> pond, List<Card> well) {
    return approach.recChoose(hand, pond, well).getFirst();
  }

  // TODO: javadoc
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    //Random r = new Random();
    //if (approach.equals(Approach.RANDOM) && r.nextInt(0, 101) % 100 == 0) {
    //  return Optional.of(opponents.get(r.nextInt(0, opponents.size())));
    //}
    return Optional.empty();
  }
}
