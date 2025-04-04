package player;

import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import game.observer.EventType;

/**
 * A human player in a Blues game. All non-void public methods' return values are obtained by
 *     calling IDisplay's methods, which read this player's input through a display.
 */
public class HPlayer extends APlayer {
  /**
   * Creates a new human player with name n using APlayer's constructor. Sets display to null.
   * @param n the name
   */
  public HPlayer(String n) {
    super(n);
    this.display = null;
  }

  /**
   * Creates a new human player with name "New player" using APlayer's constructor. Sets display to
   *     null.
   */
  public HPlayer() {
    super();
    this.display = null;
  }

  //************************************************************************************ SELF MODIFY
  /**
   * Calls and returns IDisplay's 'Card askDiscard()' method (which gets this player's input through
   *     the display). Removes the card to discard from this player's hand.
   * @param well the four well cards to consider
   * @return the card to discard
   * @throws IllegalStateException if no display
   */
  @Override
  public Card discard(List<Card> well) {
    throwIfNoDisplay();
    return display.askDiscard();
  }

  /**
   * Calls and returns IDisplay's 'Card askChoose()' method (which gets this player's input through
   *     the display).
   * @param pond the remaining pond cards
   * @param well the remaining well cards
   * @return the chosen addition
   * @throws IllegalStateException if no display
   */
  @Override
  public Card chooseCard(List<Card> pond, List<Card> well) {
    throwIfNoDisplay();
    return display.askChoose();
  }

  /**
   * Calls and returns IDisplay's 'Optional<IPlayer> askNoBlues()' method (which gets this player's
   *     input through the display).
   * @param opponents
   * @param well
   * @return an opposing Player if this Player thinks that opponent is about to win, empty otherwise
   * @throws IllegalStateException if no display
   */
  @Override
  public Optional<IPlayer> callNo(List<IPlayer> opponents, List<Card> well) {
    throwIfNoDisplay();
    return display.askCall();
  }

  //*************************************************************************************** OBSERVER
  /**
   * 'HumanPlayer's never use 'IMemory', so this method is empty.
   * @param event the 'EventType'
   * @param data the event's data
   */
  @Override
  public void update(EventType event, List<Object> data) {
  }

  //**************************************************************************************** HELPERS
  /**
   * Throws an exception if display has not been set because HPlayer's methods rely on display's
   *     return values (obtained by communicating with this player).
   * @throws IllegalStateException if display is null
   */
  private void throwIfNoDisplay() {
    if (display == null) {
      throw new IllegalStateException("No display set");
    }
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  // TODO: implement (current implementation is sufficient for testing purposes ONLY)
  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  // TODO: implement (current implementation is sufficient for testing purposes ONLY)
  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
