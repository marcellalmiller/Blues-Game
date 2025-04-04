package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import game.deck.card.Card;
import game.observer.EventType;
import player.strategy.IMemory;
import player.strategy.IStrategy;

/**
 * An AI player in a Blues game. Makes decisions according to Strategies.
 */
public class AIPlayer extends APlayer {
  protected final IStrategy strategy;

  public AIPlayer(String n, IStrategy s) {
    super(n);
    this.strategy = s;
  }

  public AIPlayer(IStrategy s) {
    super();
    this.strategy = s;
  }

  //************************************************************************************ SELF MODIFY
  @Override
  public Card discard(List<Card> well) {
    return strategy.recommendedDiscard(hand, well);
  }

  @Override
  public Card chooseCard(List<Card> pond, List<Card> well) {
    return strategy.recommendedChoose(hand, pond, well);
  }

  @Override
  public Optional<IPlayer> callNo(List<IPlayer> opponents, List<Card> well) {
    return strategy.recommendedCall(opponents, well);
  }

  @Override
  public void resetNewRound() {
    super.resetNewRound();
    strategy.resetNewRound();
  }

  //*************************************************************************************** OBSERVER
  @Override
  public void update(EventType event, List<Object> data) {
    if (!(strategy instanceof IMemory)) return;
    switch (event) {
      case PLAYER_CHOICE -> {
        if (data.getFirst() != this) ((IMemory) strategy)
                .notifyOfPlayerChoice((IPlayer) data.get(0), (Card) data.get(1),
                        (String) data.get(2), (List<Card>) data.get(3), (List<Card>) data.get(4));
      }
      case PLAYER_DISCARD -> {
        if (data.getFirst() != this) ((IMemory) strategy)
                .notifyOfPlayerDiscard((IPlayer) data.get(0), (Card) data.get(1),
                        (List<Card>) data.get(2));
      }
      case CARDS_CLEARED -> {
        List<Card> cardList = new ArrayList<>();
        for (Object o : data) cardList.add((Card) o);
        ((IMemory) strategy).notifyOfCardsCleared(cardList);
      }
    }
  }

  //***************************************************************************** ADDITIONAL GETTERS
  // TODO: javadoc
  public IStrategy getStrat() {
    return strategy;
  }

  // TODO: javadoc
  public void resetStrat() {
    strategy.resetNewRound();
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  // TODO: implement (current implementation is sufficient for testing purposes ONLY)
  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other.getClass().equals(getClass())) {
      return this.getStrat().equals(((AIPlayer) other).getStrat());
    }
    return false;
  }

  // TODO: implement (current implementation is sufficient for testing purposes ONLY)
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), strategy);
  }
}
