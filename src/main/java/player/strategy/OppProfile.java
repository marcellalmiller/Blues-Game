package player.strategy;

import java.util.ArrayList;
import java.util.List;

import game.deck.card.Card;
import game.deck.card.UCard;
import player.IPlayer;

public class OppProfile {
  IPlayer opponent;
  List<Card> discarded;
  List<Card> chosen;
  List<Card> hand;
  List<UCard> wanted;
  List<UCard> unwanted;
  List<WinningHand> pursuingWHs;

  public OppProfile(IPlayer opponent) {
    this.opponent = opponent;
    resetNewRound();
  }

  public void resetNewRound() {
    discarded = new ArrayList<>();
    chosen = new ArrayList<>();
    hand = new ArrayList<>();
    wanted = new ArrayList<>();
    unwanted = new ArrayList<>();
    pursuingWHs = new ArrayList<>();
  }

  public void addChoice(Card c, String location, List<Card> well, List<Card> pond) {
    chosen.add(c);
    hand.add(c);

    List<Card> wend = new ArrayList<>(well);
    wend.addAll(pond);

    for (Card card : wend) {
      unwanted.add(UCard.toUCard(card));
    }

    wanted.add(UCard.toUCard(c));
  }

  public void addDiscard(Card c, List<Card> well) {
    discarded.add(c);
    hand.remove(c);

    unwanted.add(UCard.toUCard(c));
    wanted.remove(UCard.toUCard(c));
  }

  public List<WinningHand> getPursuingWHs() {
    return pursuingWHs;
  }

  public List<Card> getHand() {
    return hand;
  }

  public void updatePursuingWHs() {
    List<WinningHand> closestTo = WinningHand.closestTo(hand);
    for (WinningHand wh : closestTo) {
      if (wh.unsuitedCardList().stream().anyMatch(unwanted::contains)) {
        closestTo.remove(wh);
      }
    }
  }
}
