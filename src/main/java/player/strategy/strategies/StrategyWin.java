package player.strategy.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import game.deck.card.UCard;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.IStrategy;
import player.strategy.WinningHand;

// TODO: javadoc
public class StrategyWin implements IStrategy {
  private final Approach approach;
  private List<WinningHand> pursuableWHs;

  // TODO: javadoc
  public StrategyWin(Approach approach) {
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

    throw new IllegalStateException("No recommended discard"); // TODO: this is messy
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

    if (helpful.isEmpty()) return approach.recChoose(hand, well, pond).getFirst();
    if (helpful.size() == 1) return helpful.getFirst();

    for (Card c : approach.recChoose(hand, well, pond)) {
      if (helpful.contains(c)) return c;
    }

    throw new IllegalStateException("No recommended choice"); // TODO: messsyyyy
  }

  // TODO: javadoc
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
    return Optional.empty();
  }

  //******************************************************************************* GETBLUES HELPERS
  // TODO: javadoc
  private void getPursuableWHs(List<Card> hand) {
    if (pursuableWHs.isEmpty()) {
      pursuableWHs = WinningHand.closestTo(hand);
      sortPursuableWHs();
    } else refreshPursuableWHs(hand);
  }

  // TODO: javadoc
  private void sortPursuableWHs() {
    switch (approach) {
      case MIN_POINTS -> { // TODO: curious to see what adding MAX_TRUMP here would do
        pursuableWHs.sort(Comparator.comparingInt(WinningHand::points));
      }
      case RANDOM -> {
        Collections.shuffle(pursuableWHs);
      }
    }
  }

  // TODO: javadoc
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
