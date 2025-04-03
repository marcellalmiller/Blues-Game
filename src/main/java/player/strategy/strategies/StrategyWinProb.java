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

// TODO: javadoc
public class StrategyWinProb implements IStrategy {
  private final Approach approach;
  private List<WinningHand> pursuableWHs;
  private boolean regetPursuable;
  private List<Card> idealChoices;
  private List<WinningHand> idealChoiceWHs;

  // TODO: javadoc
  public StrategyWinProb(Approach approach) {
    this.approach = approach;
    resetNewRound();
  }

  // TODO: javadoc
  @Override
  public void resetNewRound() {
    this.pursuableWHs = new ArrayList<>();
    this.regetPursuable = true;
    this.idealChoices = new ArrayList<>();
    this.idealChoiceWHs = new ArrayList<>();
  }

  // TODO: javadoc
  @Override
  public Card recommendedDiscard(List<Card> hand, List<Card> well) {
    // reget/refresh pursuableWHs, reset regetPursuable, return approach rec if pursuableWHs = empty
    if (regetPursuable) {
      getPursuableWHs(hand);
      if (!pursuableWHs.isEmpty()) regetPursuable = false;
    } else refreshPursuable(hand);
    if (pursuableWHs.isEmpty()) return approach.recDiscard(hand).getFirst();

    // get idealChoices, if empty return first elem of approach rec the most pursuableWHs don't need
    getIdealChoices(hand, well);
    if (idealChoices.isEmpty()) {
      List<Card> commonDiscIfDesired = getCommonDiscIfDesired(hand);
      for (Card c : approach.recDiscard(hand)) {
        if (commonDiscIfDesired.contains(c)) return c;
      }
    }

    // get discIfDesired for first elem of idealChoices, if discIfDesired size = 1 or first elem
    //   isn't Blue return first elem
    List<Card> discIfDesired =
            Utility.sortHandByTrump(WinningHand.discardIfDesired(idealChoiceWHs.getFirst(), hand));
    if (discIfDesired.size() == 1 || !discIfDesired.getFirst().getColor().equals(Color.BLUE)) {
      return discIfDesired.getFirst();
    }

    // get number of desirable well cards (Color.BLUE or percentContainedBy > 50) and number of blue
    //   well cards
    int desirableInWell = 0;
    int bluesInWell = 0;
    for (Card c : well) {
      if (c.getColor().equals(Color.BLUE)) {
        desirableInWell++;
        bluesInWell++;
      }
      if (UCard.toUCard(c).percentContainedBy() > 50) desirableInWell++;
    }

    // if well has high desirability: return discIfDesired 2nd elem if it's blue, else 1st elem
    //  high desirability means: >= 3 well cards are blue/percentContainedBy > 50 OR >= 2 well cards
    //  are blue OR ideal choices first elem is blue/percentContainedBy > 50
    if (desirableInWell > 2 || bluesInWell > 1
            || idealChoices.getFirst().getColor().equals(Color.BLUE)
            || UCard.toUCard(idealChoices.getFirst()).percentContainedBy() > 50) {
      if (discIfDesired.get(1).getColor().equals(Color.BLUE)) return discIfDesired.get(1);
      else return discIfDesired.getFirst();
    }

    // else if discIfDesired 2nd elem is blue or has Rank.ONE or Rank.TWO return 2nd elem
    if (discIfDesired.get(1).getColor().equals(Color.BLUE)
            || discIfDesired.get(1).getRank().equals(Rank.ONE)
            || discIfDesired.get(1).getRank().equals(Rank.TWO)) {
      return discIfDesired.get(1);
    }

    // else return discIfDesired 1st elem
    return discIfDesired.getFirst();
  }

  // TODO: javadoc
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> well, List<Card> pond) {
    // combine well and pond into wend, return 1st elem of idealChoices also in wend if it exists
    List<Card> wend = new ArrayList<>(well);
    wend.addAll(pond);
    for (Card c : idealChoices) if (wend.contains(c)) return c;

    // get pursuable WHs for each card in wend, if empty return approach choice, else get cumulative
    //   permutations for each list of pursuable WHs
    List<List<WinningHand>> pursueByCC = getPursuableWHsByCardChoice(hand, wend);
    if (pursueByCC.isEmpty()) {
      regetPursuable = true;
      return approach.recChoose(hand, well, pond).getFirst();
    }
    List<Integer> pursueByCCPerms = getPursuableByCCCumulativePerms(pursueByCC, hand);

    // get indices of card choices associated with WHs with highest cumulative permutations
    Set<Integer> idxsOfHighestPerms = new HashSet<>();
    int largest = 0;
    for (int i = 0; i < pursueByCCPerms.size(); i++) {
      int perms = pursueByCCPerms.get(i);
      if (perms > largest) {
        largest = perms;
        idxsOfHighestPerms = new HashSet<>();
        idxsOfHighestPerms.add(i);
      }
      if (perms == largest) idxsOfHighestPerms.add(i);
    }

    // return first card in approach rec choose list that has highest permutations
    for (Card c : approach.recChoose(hand, well, pond)) {
      for (Integer i : idxsOfHighestPerms) {
        if (wend.get(i).equals(c)) return c;
      }
    }

    throw new IllegalStateException("No recommended choice");
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
  // TODO: also sort by strength of betting cards ?
  private void getPursuableWHs(List<Card> hand) {
    // if cards away from closest WHs is > 2 return, else get closest to
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
  private List<List<WinningHand>> getPursuableWHsByCardChoice(List<Card> hand, List<Card> choices) {
    List<List<WinningHand>> pursuableByCC = new ArrayList<>();

    // for each card in choices, create a list of WHs the card helps and add it to pursuableByCC
    for (Card c : choices) {
      List<WinningHand> helpedByC = new ArrayList<>();
      for (WinningHand w : WinningHand.values()) {
        List<Card> handWithC = new ArrayList<>(hand);
        handWithC.add(c);
        int cardsAwayWithC = WinningHand.cardsAwayFrom(w, handWithC);
        if (cardsAwayWithC < 3 && cardsAwayWithC < WinningHand.cardsAwayFrom(w, hand)) {
          helpedByC.add(w);
        }
      }
      pursuableByCC.add(helpedByC);
    }

    // if every list in pursuableByCC is empty return an empty list
    boolean allEmpty = true;
    for (List<WinningHand> l : pursuableByCC) {
      if (!l.isEmpty()) {
        allEmpty = false;
        break;
      }
    }
    if (allEmpty) return List.of();

    // return pursuableByCC
    return pursuableByCC;
  }

  // TODO: javadoc
  private List<Integer> getPursuableByCCCumulativePerms(List<List<WinningHand>> WHLists,
                                                        List<Card> hand) {
    // for each list of WHs in WHLists, sum each WH's permsWOHas and add to cumulativePermsList
    List<Integer> cumulativePermsList = new ArrayList<>();
    for (List<WinningHand> l : WHLists) {
      int cumulativePerms = 0;
      if (l.isEmpty()) {
        cumulativePermsList.add(0);
        continue;
      }
      for (WinningHand w : l) {
        cumulativePerms += WinningHand.permsWOHas(w, hand);
      }
      cumulativePermsList.add(cumulativePerms);
    }
    return cumulativePermsList;
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

  // TODO: javadoc
  private List<Card> getCommonDiscIfDesired(List<Card> hand) {
    Set<Card> commonKeepIfDesired = new HashSet<>();
    // add each WHs keepIfDesired cards to a list, if an addition makes list contain all hand cards,
    //   remove addition and break loop
    for (WinningHand w : pursuableWHs) {
      List<Card> wKeepIfDesired = WinningHand.keepIfDesired(w, hand);
      commonKeepIfDesired.addAll(wKeepIfDesired);
      if (commonKeepIfDesired.size() != 5) continue;

      wKeepIfDesired.forEach(commonKeepIfDesired::remove);
      break;
    }

    // remove common keepIfDesired cards from hand and return result
    List<Card> commonDiscIfDesired = new ArrayList<>(hand);
    commonDiscIfDesired.removeAll(commonKeepIfDesired);
    return commonDiscIfDesired;
  }

  // TODO: javadoc
  private void getIdealChoices(List<Card> hand, List<Card> well) {
    idealChoices = new ArrayList<>();
    idealChoiceWHs = new ArrayList<>();
    // if card c in well helps WH wh in pursuableWHs, add c to idealChoices and wh to idealChoiceWHs
    for (WinningHand wh : pursuableWHs) {
      for (int i = 0; i < well.size(); i++) {
        if (WinningHand.helps(wh, hand, well.get(i))) {
          idealChoices.add(well.get(i));
          idealChoiceWHs.add(wh);
        }
      }
    }
    // sort updated ideal choices
    sortIdealChoices(hand);
  }

  // TODO: implement, javadoc
  private void sortIdealChoices(List<Card> hand) {
    List<WinningHand> ICWHsCopy = new ArrayList<>(idealChoiceWHs);
    List<Card> ICCopy = new ArrayList<>();

    sortPursuableWHs(ICWHsCopy, hand);
    List<Integer> sortedIdxs = new ArrayList<>();
    for (int i = 0; i < ICWHsCopy.size(); i++) {
      WinningHand w = ICWHsCopy.get(i);
      int idx = idealChoiceWHs.indexOf(w);
      ICCopy.add(idealChoices.get(idx));
    }

    idealChoices = ICCopy;
    idealChoiceWHs = ICWHsCopy;
  }
}
