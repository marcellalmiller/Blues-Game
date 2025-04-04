package player.strategy.strategies;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.deck.card.Card;
import player.strategy.Approach;
import player.strategy.WinningHand;

/**
 * A strategy that attempts to win. Does not use probability or memory. Identifies which
 *   'WinningHand's the player's hand is closest to and makes choices that bring them even closer.
 *   The strategy's 'Approach' determines which 'WinningHand' to pursue if the player's hand is
 *   close to multiple, which card to choose if multiple desired cards are available, and which card
 *   to discard if multiple cards are unneeded. If the player's hand isn't within pursuable distance
 *   of any 'WinningHand', 'Approach' fully determines which cards are chosen and discarded. This
 *   strategy never calls No Blues on opponents.
 */
public class StrategyWin extends AStrategyWin {

  public StrategyWin(Approach approach) {
    super(approach);
    resetNewRound();
  }

  /**
   * If 'pursuableWHs' is empty, calls static method 'closestTo' in 'WinningHand' to get a list of
   *   all 'WinningHand's the current hand is closest to, then calls 'sortPursuableWHs' and sets
   *   'pursuableWHs' equal to the list. Else calls 'refreshPursuableWHs'.
   * @param hand the current hand
   */
  void getPursuableWHs(List<Card> hand) {
    if (pursuableWHs.isEmpty()) {
      pursuableWHs = WinningHand.closestTo(hand);
      sortPursuableWHs(hand);
    }
    else super.refreshPursuableWHs(hand);
  }

  /**
   * Sorts 'pursuableWHs' according to 'approach'. For 'Approach.MIN_POINTS' and
   *   'Approach.MAX_TRUMP', hands are sorted from lowest to highest point values. For
   *   'Approach.RANDOM', hands are sorted randomly using 'Collections.shuffle()'.
   */
  void sortPursuableWHs(List<Card> hand) {
    switch (approach) {
      case MIN_POINTS, MAX_TRUMP -> pursuableWHs.sort(Comparator.comparingInt(WinningHand::points));
      case RANDOM -> Collections.shuffle(pursuableWHs);
    }
  }
}
