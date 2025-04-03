package player.strategy.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import game.deck.card.Card;
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
  private boolean regetPursuable;
  private List<Card> idealChoices;
  private List<WinningHand> idealChoiceWHs;

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
    for (OppProfile op : profiles.values()) op.resetNewRound();
    inHand = new ArrayList<>();
    discarded = new ArrayList<>();
    pursuableWHs = new ArrayList<>();
    regetPursuable = true;
    idealChoices = new ArrayList<>();
    idealChoiceWHs = new ArrayList<>();
  }

  // TODO: implement, javadoc
  @Override
  public Card recommendedDiscard(List<Card> hand, List<Card> well) {
    return null;
  }

  // TODO: implement, javadoc
  @Override
  public Card recommendedChoose(List<Card> hand, List<Card> pond, List<Card> well) {
    return null;
  }

  // TODO: implement, javadoc
  @Override
  public Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well) {
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

  //********************************************************************************* MEMORY HELPERS
  // TODO: implement, javadoc
  private void initMem() {

  }
}
