package player.strategy.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.WinningHand;

/**
 * Defines common functionality between strategies whose file names begin with 'StrategyWin'.
 */
public abstract class AStrategyWin implements IStrategy {
  final Approach approach;
  List<WinningHand> pursuableWHs;

  public AStrategyWin(Approach approach) {
    this.approach = approach;
    this.resetNewRound();
  }

  @Override
  public void resetNewRound() {
    this.pursuableWHs = new ArrayList<>();
  }

  /**
   * Calls 'getPursuableWHs'. If 'pursuableWHs' is empty, returns the first card
   *   recommended by 'approach'. Else gets the list of cards that can be discarded if the player
   *   pursues the first hand in pursuable winning hands. Iterates through the cards in the order
   *   recommended by 'approach' and returns the first one identified as discardable.
   */
  @Override
  public Card recommendedDiscard(List<Card> hand, List<Card> well) {
    getPursuableWHs(hand);
    if (pursuableWHs.isEmpty()) return approach.recDiscard(hand).getFirst();

    List<Card> discIfDesired = WinningHand.discardIfDesired(pursuableWHs.getFirst(), hand);

    for (Card c : approach.recDiscard(hand)) {
      if (discIfDesired.contains(c)) return c;
    }

    return approach.recDiscard(discIfDesired).getFirst();
  }

  /**
   * Calls 'getPursuableWHs()'. If 'pursuableWHs' is empty, returns the first card recommended by
   *   'approach'. Else selects the first hand in 'pursuableWHs' and identifies all cards in the
   *   well or pond that help that winning hand. If there are no helpful cards, returns the first
   *   choice recommended by 'approach'. Else iterates through the cards in the order recommended by
   *   'approach' and returns the first one identified as helpful.
   */
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> well, List<Card> pond) {
    getPursuableWHs(hand);
    if (pursuableWHs.isEmpty()) return approach.recChoose(hand, well, pond).getFirst();

    List<Card> wend = new ArrayList<>(well);
    wend.addAll(pond);
    List<Card> helpful = new ArrayList<>();

    for (Card c : wend) {
      if (WinningHand.helps(pursuableWHs.getFirst(), hand, c)) helpful.add(c);
    }

    for (Card c : approach.recChoose(hand, well, pond)) {
      if (helpful.contains(c)) return c;
    }

    if (helpful.size() > 1) return helpful.getFirst();

    return approach.recChoose(hand, well, pond).getFirst();
  }

  /**
   * Returns an empty optional.
   */
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    return Optional.empty();
  }

  /**
   * Iterates through 'pursuableWHs' and removes all hands that aren't as close to the current hand
   *   as the closest hand. For example, if a pursuable winning hand 'a' is 2 cards away from the
   *   current hand but the closest pursuable winning hands 'b' and 'c' are each only 1 card away
   *   from the current hand, 'a' is removed.
   * @param hand the current hand
   */
  void refreshPursuableWHs(List<Card> hand) {
    List<WinningHand> closest = new ArrayList<>();
    int closestAway = 5;

    for (WinningHand wh : pursuableWHs) {
      int whDistance = WinningHand.cardsAwayFrom(wh, hand);
      if (whDistance < closestAway) {
        closestAway = whDistance;
        closest = new ArrayList<>();
        closest.add(wh);
      }
      else if (whDistance == closestAway) {
        closest.add(wh);
      }
    }

    pursuableWHs = closest;
    sortPursuableWHs(hand);
  }

  abstract void getPursuableWHs(List<Card> hand);
  abstract void sortPursuableWHs(List<Card> hand);
}
