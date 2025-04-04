package player.strategy.strategies;

import java.util.ArrayList;
import java.util.List;

import game.deck.card.Card;
import game.deck.card.UCard;
import game.deck.card.properties.Color;
import player.strategy.Approach;
import player.strategy.WinningHand;

/**
 * A strategy that attempts to win using probability. Does not use memory. Identifies which
 *   'WinningHand's the player's hand is closest to and uses probability to find and pursue the one
 *   with the greatest number of permutations. The strategy's 'Approach' determines which
 *   'WinningHand' to pursue if the player's hand is close to multiple with the same amount of
 *   permutations, which card to choose if multiple desired cards are available, and which card to
 *   discard if multiple cards are unneeded. If the player's hand isn't within pursuable distance of
 *   any 'WinningHand', 'Approach' fully determines which cards are chosen and discarded. This
 *   strategy never calls No Blues on opponents.
 */
public class StrategyWinProb extends AStrategyWin {

  public StrategyWinProb(Approach approach) {
    super(approach);
  }

  /**
   * If 'pursuableWHs' isn't empty, calls 'refreshPursuableWHs' method in superclass and returns.
   *   Else calls static method 'closestTo()' in 'WinningHand' to get a list of all 'WinningHand's
   *   the current hand is closest to. If the closest WinningHand is more than 2 cards away,
   *   returns. Else identifies all the 'WinningHand's returned by 'closestTo' that have blue cards,
   *   calls 'notPursuableWithBlues()' with them, and removes the results from 'pursuableWHs'. Calls
   *   'sortPursuableWHs()' and returns.
   * @param hand the current hand
   */
  void getPursuableWHs(List<Card> hand) {
    if (!pursuableWHs.isEmpty()) {
      super.refreshPursuableWHs(hand);
      return;
    }

    if (WinningHand.cardsAwayFromClosestTo(hand) > 2) return;
    List<WinningHand> closestTo = WinningHand.closestTo(hand);

    List<WinningHand> hasBlues = new ArrayList<>();
    List<UCard> blues = List.of(UCard.A1, UCard.A2, UCard.A3, UCard.A4, UCard.A5, UCard.A6,
            UCard.A7);
    for (WinningHand wh : closestTo) {
      if (blues.stream().anyMatch(wh.unsuitedCardList()::contains)) hasBlues.add(wh);
    }

    closestTo.removeAll(notPursuableWithBlues(hasBlues, hand));
    pursuableWHs = closestTo;
    sortPursuableWHs(hand);
  }

  /**
   * Sorts 'pursuableWHs' from highest to lowest permutations taking into account the cards in
   *   'hand'. If two 'WinningHand's have the same permutations, sorts from lowest to highest point
   *   value. If they also the same point value, sorts from highest to lowest strength of the hands
   *   returned by calling 'WinningHand.discardIfDesired()'.
   * @param hand the current hand
   */
  void sortPursuableWHs(List<Card> hand) {
    pursuableWHs.sort((a, b) -> {
      if (WinningHand.permsWOHas(a, hand) > WinningHand.permsWOHas(b, hand)) return -1;
      if (WinningHand.permsWOHas(a, hand) < WinningHand.permsWOHas(b, hand)) return 1;

      if (a.points() < b.points()) return -1;
      if (a.points() > b.points()) return 1;

      int aStrength = 0;
      int bStrength = 0;
      for (Card c : WinningHand.discardIfDesired(a, hand)) aStrength += c.trumpsAmount();
      for (Card c : WinningHand.discardIfDesired(b, hand)) aStrength += c.trumpsAmount();
      return Integer.compare(bStrength, aStrength);
    });
  }

  /**
   * Determines which 'WinningHand's that contain blue cards in 'pursuableWHs' are not pursuable
   *   due to the rarity and desirability of blue cards. If players need to obtain more than one
   *   blue card to get a WinningHand, it is considered not pursuable.
   * @param WHs the 'WinningHand's with blue cards in 'pursuableWHs'
   * @param hand the current hand
   * @return the 'WinningHand's with blue cards that aren't pursuable
   */
  private List<WinningHand> notPursuableWithBlues(List<WinningHand> WHs, List<Card> hand) {
    List<WinningHand> notPursuable = new ArrayList<>();
    for (WinningHand w : WHs) {
      List<UCard> bluesNeeded = new ArrayList<>();
      for (UCard c : w.unsuitedCardList()) if (c.color().equals(Color.BLUE)) bluesNeeded.add(c);

      if (bluesNeeded.isEmpty()) continue;

      if (bluesNeeded.size() > 1) notPursuable.add(w);
    }

    return notPursuable;
  }
}
