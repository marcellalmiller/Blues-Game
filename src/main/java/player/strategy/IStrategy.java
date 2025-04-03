package player.strategy;

import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import player.IPlayer;

public interface IStrategy {
  void resetNewRound();

  Card recommendedDiscard(List<Card> hand, List<Card> well);

  Card recommendedChoose(List<Card> hand, List<Card> well, List<Card> pond);

  Optional<IPlayer> recommendedCall(List<IPlayer> opponents, List<Card> well);
}
