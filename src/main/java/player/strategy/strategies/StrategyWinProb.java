package player.strategy.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import game.deck.card.Card;
import game.deck.card.UCard;
import game.deck.card.properties.Color;
import game.deck.card.properties.Rank;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IStrategy;
import player.strategy.WinningHand;
import utility.Utility;

/**
 * A strategy that attempts to win using probability. Does not use memory or opponent profiles.
 *   Identifies which 'WinningHand's the player's hand is closest to and makes choices that bring
 *   them even closer. Uses probability to pursue the 'WinningHand' with the greatest number of
 *   permutations. The strategy's 'Approach' determines which
 */
public class StrategyWinProb implements IStrategy {
  private final Approach approach;
  private List<WinningHand> pursuableWHs;

  public StrategyWinProb(Approach approach) {
    this.approach = approach;
    resetNewRound();
  }

  // TODO: javadoc
  @Override
  public void resetNewRound() {
    this.pursuableWHs = new ArrayList<>();
  }

  // TODO: javadoc
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

  // TODO: javadoc
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

  // TODO: javadoc
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    // Random r = new Random();
    // if (approach.equals(Approach.RANDOM) && r.nextInt(0, 101) % 100 == 0) {
    //  return Optional.of(opponents.get(r.nextInt(0, opponents.size())));
    //}
    //else if (r.nextInt(0, 501) % 500 == 0) {
    //  return Optional.of(opponents.get(r.nextInt(0, opponents.size())));
    //}
    return Optional.empty();
  }

  //******************************************************************************* GETBLUES HELPERS
  // TODO: javadoc
  private void getPursuableWHs(List<Card> hand) {
    // if cards away from closest WHs is > 2 return, else get closest to
    if (!pursuableWHs.isEmpty()) {
      refreshPursuable(hand);
      return;
    }

    if (WinningHand.cardsAwayFromClosestTo(hand) > 2) return;
    List<WinningHand> closestTo = WinningHand.closestTo(hand);

    // get all the WHs that have blues
    List<WinningHand> hasBlues = new ArrayList<>();
    List<UCard> blues = List.of(UCard.A1, UCard.A2, UCard.A3, UCard.A4, UCard.A5, UCard.A6,
            UCard.A7);
    for (WinningHand wh : closestTo) {
      if (blues.stream().anyMatch(wh.unsuitedCardList()::contains)) hasBlues.add(wh);
    }

    // remove not pursuable WHs with blues from closestTo, sort closestTo and set pursuableWHs
    closestTo.removeAll(notPursuableWithBlues(hasBlues, hand));
    sortPursuableWHs(closestTo, hand);
    pursuableWHs = closestTo;
  }

  // TODO: javadoc
  private void sortPursuableWHs(List<WinningHand> WHs, List<Card> hand) {
    WHs.sort((a, b) -> {
      // switch if b has higher permsWOHas than a
      if (WinningHand.permsWOHas(a, hand) > WinningHand.permsWOHas(b, hand)) return -1;
      if (WinningHand.permsWOHas(a, hand) < WinningHand.permsWOHas(b, hand)) return 1;

      // a and b have equal permsWOHas, switch if a is worth less points than b
      if (a.points() < b.points()) return -1;
      if (a.points() > b.points()) return 1;

      // a and b have equal permsWOHas and points, switch if a's discardIfDesired weaker than b's
      int aStrength = 0;
      int bStrength = 0;
      for (Card c : WinningHand.discardIfDesired(a, hand)) aStrength += c.trumpsAmount();
      for (Card c : WinningHand.discardIfDesired(b, hand)) aStrength += c.trumpsAmount();
      return Integer.compare(bStrength, aStrength);
    });
  }

  // TODO: javadoc
  private List<WinningHand> notPursuableWithBlues(List<WinningHand> WHs, List<Card> hand) {
    List<WinningHand> notPursuable = new ArrayList<>();
    // for each WH w in WHs
    for (WinningHand w : WHs) {
      List<UCard> bluesNeeded = new ArrayList<>();
      // get the number of blue cards in w
      for (UCard c : w.unsuitedCardList()) if (c.color().equals(Color.BLUE)) bluesNeeded.add(c);
      switch (bluesNeeded.size()) {
        // if one blue card, add w to notPursuable if hand doesn't already contain that card
        case 1 -> {
          if (!UCard.toUCards(hand).contains(bluesNeeded.getFirst())) notPursuable.add(w);
        }
        // if four blue cards, add w to notPursuable if hand doesn't contain 3+ of those cards
        case 4 -> {
          int hasBlues = 0;
          for (UCard c : UCard.toUCards(hand)) if (bluesNeeded.contains(c)) hasBlues++;
          if (hasBlues < 3) notPursuable.add(w);
        }
        // if five blue cards, add w to notPursuable if hand doesn't contain 4+ of those cards
        case 5 -> {
          int hasBlues = 0;
          for (UCard c : UCard.toUCards(hand)) if (bluesNeeded.contains(c)) hasBlues++;
          if (hasBlues < 4) notPursuable.add(w);
        }
      }
    }
    return notPursuable;
  }

  // TODO: javadoc
  private void refreshPursuable(List<Card> hand) {
    List<WinningHand> closest = new ArrayList<>();
    int closestAway = 5;
    // get list of WHs (closest) that have lowest cardsAwayFrom
    for (WinningHand w : pursuableWHs) {
      int wCardsAwayFrom = WinningHand.cardsAwayFrom(w, hand);
      if (wCardsAwayFrom < closestAway) {
        closestAway = wCardsAwayFrom;
        closest = new ArrayList<>();
        closest.add(w);
      }
      if (wCardsAwayFrom == closestAway) closest.add(w);
    }
    // set pursuableWHs equal to closest
    sortPursuableWHs(closest, hand);
    pursuableWHs = closest;
  }
}
