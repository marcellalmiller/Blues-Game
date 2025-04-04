package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import game.deck.card.Card;
import game.observer.EventType;
import player.strategy.IMemory;
import player.strategy.strategies.IStrategy;

/**
 * An AIPlayer in a Blues game. Makes decisions by calling the methods of its 'IStrategy'.
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
  /**
   * Calls and returns 'strategy.recommendDiscard()' with 'hand' and argument 'well'.
   * @param well the four well cards to consider
   * @return the 'Card' to discard
   */
  @Override
  public Card discard(List<Card> well) {
    return strategy.recommendedDiscard(hand, well);
  }

  /**
   * Calls and returns 'strategy.recommendChoose()' with 'hand' and arguments 'pond' and 'well'.
   * @param pond the remaining pond cards
   * @param well the remaining well cards
   * @return the 'Card' to choose
   */
  @Override
  public Card chooseCard(List<Card> pond, List<Card> well) {
    return strategy.recommendedChoose(hand, pond, well);
  }

  /**
   * Calls and returns 'strategy.recommendCall()' with arguments 'opponents' and 'well'.
   * @param opponents the opponents
   * @param well the current well
   * @return an opposing 'IPlayer' if this thinks that opponent is about to win, empty otherwise
   */
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
  /**
   * This method is called by instances of 'IGame' to notify this 'IPlayer' of game events. If
   *   'strategy' is not an instance of 'IMemory', returns. Else calls 'IMemory's methods to inform
   *   'strategy' if the EventType is 'EventType.PLAYER_CHOICE', 'EventType.PLAYER_DISCARD', or
   *   'EventType.CARDS_CLEARED'. All other EventTypes are ignored.
   * @param event the 'EventType'
   * @param data the event's data
   */
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
  /**
   * Returns this AIPlayer's 'strategy'.
   * @return this player's strategy
   */
  public IStrategy getStrat() {
    return strategy;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other.getClass().equals(getClass())) {
      return this.getStrat().equals(((AIPlayer) other).getStrat());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), strategy);
  }
}
