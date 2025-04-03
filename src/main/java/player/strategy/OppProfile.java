package player.strategy;

import java.util.ArrayList;
import java.util.List;

import game.deck.card.Card;
import player.IPlayer;

public class OppProfile {
  IPlayer opponent;
  List<Card> discarded;
  List<Card> chosen;
  List<Card> hand;
  List<WinningHand> pursuingWHs;

  public OppProfile(IPlayer opponent) {
    this.opponent = opponent;
    resetNewRound();
  }

  public void resetNewRound() {
    discarded = new ArrayList<>();
    chosen = new ArrayList<>();
    hand = new ArrayList<>();
    pursuingWHs = new ArrayList<>();
  }

  public void addChoice(Card c, String location, List<Card> well, List<Card> pond) {
    chosen.add(c);
    hand.add(c);
  }

  public void addDiscard(Card c, List<Card> well) {

  }
}
