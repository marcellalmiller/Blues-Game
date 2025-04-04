package player.strategy;

import java.util.List;

import game.deck.card.Card;
import player.IPlayer;

/**
 * Functions as a proxy between an IGame and an IPlayer with memory capabilities. IPlayers should
 *   not be notified of all EventTypes. This class notifies them of only the relevant ones.
 */
public interface IMemory {
  /**
   * Notifies the player an EventType.PLAYER_DISCARD.
   * @param p the IPlayer who discarded Card c
   * @param c the Card that was discarded
   * @param well the current well
   */
  void notifyOfPlayerDiscard(IPlayer p, Card c, List<Card> well);

  /**
   * Notifies the player of an EventType.PLAYER_CHOICE.
   * @param c the Card that was chosen
   * @param location where Card c was chosen from
   * @param well the current well
   * @param pond the current pond
   */
  void notifyOfPlayerChoice(IPlayer p, Card c, String location, List<Card> well, List<Card> pond);

  /**
   * Notifies this strategy of an EventType.CARDS_CLEARED. If a random int between zero and 100 is
   *   less than or equal to 'accuracy', Card 'c' is added to 'discarded'. Else nothing.
   * @param cards the Cards cleared
   */
  void notifyOfCardsCleared(List<Card> cards);
}
