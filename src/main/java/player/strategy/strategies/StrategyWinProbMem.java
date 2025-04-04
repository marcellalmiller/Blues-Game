package player.strategy.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import game.deck.card.Card;
import game.deck.card.SCard;
import game.deck.card.UCard;
import game.deck.card.properties.Color;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IMemory;
import player.strategy.WinningHand;

/**
 *
 */
public class StrategyWinProbMem extends AStrategyWin implements IMemory {
  private final int accuracy;
  private List<Card> discarded;

  public StrategyWinProbMem(Approach approach, int memoryAccuracyPercentage) {
    super(approach);
    this.accuracy = memoryAccuracyPercentage;
    resetNewRound();
  }

  @Override
  public void resetNewRound() {
    discarded = new ArrayList<>();
    super.resetNewRound();
  }

  @Override
  public void notifyOfPlayerDiscard(IPlayer p, Card c, List<Card> well) {
    if (new Random().nextInt(0, 100) <= accuracy) discarded.remove(c);
  }

  /**
   * Notifies this strategy of an EventType.PLAYER_CHOICE. If a random int between zero and 100 is
   *   less than or equal to 'accuracy', Card 'c' is added to 'discarded'. Else nothing.
   * @param p the IPlayer who chose Card c
   * @param c the Card that was chosen
   * @param location where Card c was chosen from
   * @param well the current well
   * @param pond the current pond
   */
  @Override
  public void notifyOfPlayerChoice(IPlayer p, Card c, String location, List<Card> well,
                                   List<Card> pond) {
    if (new Random().nextInt(0, 100) <= accuracy) discarded.add(c);
  }

  /**
   * Notifies this strategy of an EventType.CARDS_CLEARED. If a random int between zero and 100 is
   *   less than or equal to 'accuracy', Card 'c' is added to 'discarded'. Else nothing.
   * @param cards the Cards cleared
   */
  @Override
  public void notifyOfCardsCleared(List<Card> cards) {
    if (new Random().nextInt(0, 100) <= accuracy) discarded.addAll(cards);
  }

  /**
   * If 'pursuableWHs' isn't empty, calls 'refreshPursuableWHs' method in superclass and returns.
   *   Else calls static method 'closestTo()' in 'WinningHand' to get a list of all 'WinningHand's
   *   the current hand is closest to. If the closest WinningHand is more than 2 cards away,
   *   returns. Else identifies all the 'WinningHand's returned by 'closestTo' that have blue cards,
   *   calls 'notPursuableWithBlues()' with them, and removes the results from 'pursuableWHs'.
   *   Removes any 'WinningHand's whose Cards are all in 'discarded' from 'pursuableWHs'. Calls
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
    List<WinningHand> toRemove = new ArrayList<>();
    for (WinningHand wh : closestTo) {
      if (blues.stream().anyMatch(wh.unsuitedCardList()::contains)) hasBlues.add(wh);

      for (UCard u : wh.unsuitedCardList()) {
        List<Card> cardList = new ArrayList<>();
        for (SCard s : u.cards()) cardList.add(new Card(s));
        if (new HashSet<>(discarded).containsAll(cardList)) {
          toRemove.add(wh);
        }
      }
    }

    closestTo.removeAll(toRemove);
    closestTo.removeAll(notPursuableWithBlues(hasBlues, hand));
    pursuableWHs = closestTo;

    sortPursuableWHs(hand);
  }

  /**
   * Sorts 'pursuableWHs' from highest to lowest remaining permutations, taking into account the
   *   Cards in 'discarded' and in 'hand'. If two 'WinningHand's have the same permutations, sorts
   *   from lowest to highest point value. If they also have the same point value, sorts from
   *   highest to lowest strength of the hands returned by calling 'WinningHand.discardIfDesired()'.
   * @param hand the current hand
   */
  void sortPursuableWHs(List<Card> hand) {
    pursuableWHs.sort((a, b) -> {
      if (WinningHand.permsWOHasAndDiscarded(a, hand, discarded)
              > WinningHand.permsWOHasAndDiscarded(b, hand, discarded)) return -1;
      if (WinningHand.permsWOHasAndDiscarded(a, hand, discarded)
              < WinningHand.permsWOHasAndDiscarded(b, hand, discarded)) return 1;

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
   * Determines which 'WinningHand's that contain blue cards in 'pursuableWHs' are not pursuable due
   *   to the rarity and desirability of blue cards. If players need to obtain more than one blue
   *   card to get a WinningHand, it is considered not pursuable.
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
      if (UCard.toUCards(discarded).stream().anyMatch(bluesNeeded::contains)) notPursuable.add(w);
    }

    return notPursuable;
  }
}
