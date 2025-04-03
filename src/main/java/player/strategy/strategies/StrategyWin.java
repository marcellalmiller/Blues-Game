package player.strategy.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IStrategy;
import player.strategy.WinningHand;

/**
 * A strategy that attempts to win. Does not use probability, memory, or opponent profiles.
 *   Identifies which 'WinningHand's the player's hand is closest to and makes choices that bring
 *   them even closer. The strategy's 'Approach' determines which 'WinningHand' to pursue if the
 *   player's hand is close to multiple, which card to choose if multiple desired cards are
 *   available, and which card to discard if multiple cards are unneeded. If the player's hand isn't
 *   within pursuable distance of any 'WinningHand', 'Approach' fully determines which cards they
 *   choose and discard. This strategy never calls No Blues on opponents.
 */
public class StrategyWin implements IStrategy {
  private final Approach approach;
  private List<WinningHand> pursuableWHs; // pursuable winning hands

  public StrategyWin(Approach approach) {
    this.approach = approach;
    resetNewRound();
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

  //**************************************************************************************** HELPERS
  /**
   * If 'pursuableWHs' is empty, calls static method 'closestTo' in 'WinningHand' to get a list of
   *   all winning hands the current hand is closest to, then calls 'sortPursuableWHs' and sets
   *   'pursuableWHs' equal to the list. Else calls 'refreshPursuableWHs'.
   * @param hand the current hand
   */
  private void getPursuableWHs(List<Card> hand) {
    if (pursuableWHs.isEmpty()) {
      pursuableWHs = WinningHand.closestTo(hand);
      sortPursuableWHs();
    }
    else refreshPursuableWHs(hand);
  }

  /**
   * Sorts 'pursuableWHs' according to 'approach'. For 'Approach.MIN_POINTS' and
   *   'Approach.MAX_TRUMP', hands are sorted from lowest to highest point values. For
   *   'Approach.RANDOM', hands are sorted randomly using 'Collections.shuffle()'.
   */
  private void sortPursuableWHs() {
    switch (approach) {
      case MIN_POINTS, MAX_TRUMP -> pursuableWHs.sort(Comparator.comparingInt(WinningHand::points));
      case RANDOM -> Collections.shuffle(pursuableWHs);
    }
  }

  /**
   * Iterates through 'pursuableWHs' and removes all hands that aren't as close to the current hand
   *   as the closest hand. For example, if a pursuable winning hand 'a' is 2 cards away from the
   *   current hand but the closest pursuable winning hands 'b' and 'c' are each only 1 card away
   *   from the current hand, 'a' is removed.
   * @param hand the current hand
   */
  private void refreshPursuableWHs(List<Card> hand) {
    if (pursuableWHs.size() == 1) return;
    int closestAway = 5;
    List<WinningHand> closest = new ArrayList<>();

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
    sortPursuableWHs();
  }
}
