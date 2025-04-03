package display;

import java.util.Optional;

import game.deck.card.Card;
import game.observer.Observer;
import player.IPlayer;

/**
 * A display for a Blues game. This display is specific to a player, referred to in Javadoc as
 *     'player' and defined by field 'player'.
 */
public interface IDisplay extends Observer {
  //************************************************************************************* USER INPUT
  /**
   * Returns the Card player wants to discard from their hand by getting their input through
   *     this display. Called and returned by HPlayer's 'Card discard(List<Card> well)' method.
   * @return the Card player wants to discard
   */
  Card askDiscard();

  /**
   * Returns the Card player wants to choose by getting their input through this display. Called and
   *     returned by HPlayer's 'Card chooseCard(List<Card> pond, List<Card> well)' method.
   * @return the Card player wants to take
   */
  Card askChoose();

  /**
   * Determines whether player wants to call 'No Blues.' If they do, returns an Optional of the
   *     desired receiver, if not, returns an empty Optional. Called and return by HPlayer's
   *     'Optional<IPlayer> callNo()' method.
   * @return an Optional of the desired 'No Blues' receiver
   */
  Optional<IPlayer> askCall();

  /**
   * Returns true if player indicates they want to play another game, false otherwise
   * @return true if player indicates they want to play another game, false otherwise
   */
  boolean askPlayAgain();

  //**************************************************************************************** DISPLAY
  /**
   * Renders a welcome message with information about the game and player's opponents.
   */
  void renderWelcome();

  /**
   * Renders the table. player's hand is displayed face up on the bottom. Opponents' hands
   *     are displayed face down in the following positions: player 1 on the left, player 2 on the
   *     top, player 3 on the right. Well Cards are displayed in the middle.
   */
  void renderTable();

  /**
   * Displays a message when the round is over that changes based on whether player won or lost.
   */
  void renderRoundOver();

  /**
   * Displays a message when the game is over that changes based on whether player won or lost.
   */
  void renderGameOver();
}
