package player.strategy;

import java.util.List;

import game.deck.card.Card;
import player.IPlayer;

public interface IMemory {
  void notifyOfPlayerDiscard(IPlayer p, Card c, List<Card> well);

  void notifyOfPlayerChoice(IPlayer p, Card c, String location, List<Card> well, List<Card> pond);

  void notifyOfCardsCleared(List<Card> cards);
}
