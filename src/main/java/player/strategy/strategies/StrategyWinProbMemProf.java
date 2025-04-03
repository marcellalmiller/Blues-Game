package player.strategy.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import game.deck.card.Card;
import game.deck.card.SCard;
import game.deck.card.UCard;
import game.deck.card.properties.Color;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IMemory;
import player.strategy.IStrategy;
import player.strategy.OppProfile;
import player.strategy.WinningHand;

// TODO: javadoc
public class StrategyWinProbMemProf implements IStrategy, IMemory {
  private final Approach approach;
  private final int accuracy;
  private Map<IPlayer, OppProfile> profiles;
  private List<Card> inHand;
  private List<Card> discarded;
  private List<WinningHand> pursuableWHs;

  // TODO: javadoc
  public StrategyWinProbMemProf(Approach approach, int memoryAccuracyPercentage) {
    this.approach = approach;
    this.accuracy = memoryAccuracyPercentage;
    this.profiles = new HashMap<>();
    resetNewRound();
  }

  // TODO: javadoc
  @Override
  public void resetNewRound() {
    if (this.profiles != null) for (OppProfile op : profiles.values()) op.resetNewRound();
    inHand = new ArrayList<>();
    discarded = new ArrayList<>();
    pursuableWHs = new ArrayList<>();
  }

  // TODO: implement, javadoc
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

  // TODO: implement, javadoc
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> pond, List<Card> well) {
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

    return approach.recChoose(hand, pond, well).getFirst();
  }

  // TODO: implement, javadoc
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    for (IPlayer p : profiles.keySet()) {
      if (profiles.get(p) == null) continue;
      profiles.get(p).updatePursuingWHs();
      if (profiles.get(p).getPursuingWHs().size() == 1) {
        WinningHand wh = profiles.get(p).getPursuingWHs().getFirst();
        for (Card c : well) {
          if (WinningHand.helps(wh, profiles.get(p).getHand(), c)) {
            System.out.println("Calling on " + p.name());
            return Optional.of(p);
          }
        }
      }
    }

    return Optional.empty();
  }

  // TODO: implement, javadoc
  @Override
  public void notifyOfPlayerDiscard(IPlayer p, Card c, List<Card> well) {
    if (!profiles.containsKey(p)) profiles.put(p, new OppProfile(p));
    inHand.remove(c);
    profiles.get(p).addDiscard(c, well);
  }

  // TODO: implement, javadoc
  @Override
  public void notifyOfPlayerChoice(IPlayer p, Card c, String location, List<Card> well,
                                   List<Card> pond) {
    if (!profiles.containsKey(p)) profiles.put(p, new OppProfile(p));
    inHand.add(c);
    profiles.get(p).addChoice(c, location, well, pond);
  }

  // TODO: implement, javadoc
  @Override
  public void notifyOfCardsCleared(List<Card> cards) {
    discarded.addAll(cards);
  }

  //**************************************************************************************** HELPERS
  private void getPursuableWHs(List<Card> hand) {
    // if cards away from closest WHs is > 2 return, else get closest to
    if (!pursuableWHs.isEmpty()) {
      refreshPursuable(hand);
      return;
    }

    if (WinningHand.cardsAwayFromClosestTo(hand) > 2) return;
    List<WinningHand> closestTo = WinningHand.closestTo(hand);

    // get all the WHs that have blues and remove any that are impossible to obtain
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

    sortPursuableWHs(closestTo, hand);
    pursuableWHs = closestTo;
  }

  // TODO: javadoc
  private void sortPursuableWHs(List<WinningHand> WHs, List<Card> hand) {
    WHs.sort((a, b) -> {
      // switch if b has higher permsWOHas than a
      if (WinningHand.permsWOHasAndDiscarded(a, hand, discarded)
              > WinningHand.permsWOHasAndDiscarded(b, hand, discarded)) return -1;
      if (WinningHand.permsWOHasAndDiscarded(a, hand, discarded)
              < WinningHand.permsWOHasAndDiscarded(b, hand, discarded)) return 1;

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
    for (WinningHand w : WHs) {
      List<UCard> bluesNeeded = new ArrayList<>();
      for (UCard c : w.unsuitedCardList()) if (c.color().equals(Color.BLUE)) bluesNeeded.add(c);

      if (bluesNeeded.isEmpty()) continue;

      if (bluesNeeded.size() > 1) notPursuable.add(w);
      if (UCard.toUCards(discarded).stream().anyMatch(bluesNeeded::contains)) notPursuable.add(w);
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
