package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import display.IDisplay;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import utility.Utility;

/**
 * An abstract player that defines the common functionality between HumanPlayer and AIPlayer.
 */
public abstract class APlayer implements IPlayer {
  public String name;
  protected ArrayList<Card> hand; // private
  protected int totalGamePoints; // protected
  protected Optional<Card> pondCard; // protected
  protected IDisplay display; // protected


  /**
   * Creates a new player and sets its name to n.
   * @param n the name
   */
  public APlayer(String n) {
    this.name = n;
    this.hand = new ArrayList<>();
    this.totalGamePoints = 0;
    this.pondCard = Optional.empty();
    this.display = null;
  }

  /**
   * Creates a new player and sets its name to "New player".
   */
  public APlayer() {
    this.name = "New player";
    this.hand = new ArrayList<>();
    this.totalGamePoints = 0;
    this.pondCard = Optional.empty();
    this.display = null;
  }

  //************************************************************************************ SELF MODIFY
  @Override
  public void resetNewGame() {
    resetNewRound();
    this.totalGamePoints = 0;
  }

  @Override
  public void resetNewRound() {
    this.hand = new ArrayList<>();
    this.pondCard = Optional.empty();
  }

  //************************************************************************************ GAME MODIFY
  @Override
  public void dealCard(Card c) {
    hand.add(c);
    c.setPosition(Position.HAND);
    // TODO: figure out this situation
    // pondCard = Optional.empty();
    hand = new ArrayList<>(Utility.sortHandByRank(hand));
  }

  @Override
  public void throwCard(Card c) {
    if (!hand.contains(c)) {
      throw new IllegalArgumentException("Hand doesn't contain card " + c.toString());
    }
    hand.remove(c);
  }

  @Override
  public void addPoints(int p) {
    totalGamePoints+= p;
  }

  //**************************************************************************************** SETTERS
  @Override
  public void editName(String newName) {
    this.name = newName;
  }

  @Override
  public void setDisplay(IDisplay d) {
    this.display = d;
  }

  @Override
  public void setPondCard(Card c) {
    c.setPosition(Position.POND_H);
    pondCard = Optional.of(c);
  }

  @Override
  public void setPondCard() {
    pondCard = Optional.empty();
  }

  //**************************************************************************************** GETTERS
  @Override
  public List<Card> getHand() {
    return hand;
  }

  @Override
  public int getPoints() {
    return totalGamePoints;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Optional<Card> getPondCard() {
    return pondCard;
  }

  @Override
  public IDisplay getDisplay() {
    if (display == null) {
      return null;
    }
    return display;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return name + " (total points: " + totalGamePoints + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    APlayer o = (APlayer) other;
    if ((o.getDisplay() == null && display != null) || (o.getDisplay() != null && display == null)) {
      return false;
    }
    if ((display == null && o.getDisplay() == null) || o.getDisplay().equals(getDisplay())) {
      return o.name().equals(name()) && o.getPoints() == getPoints()
              && o.getHand().equals(getHand())
              && o.getPondCard().equals(((APlayer) other).getPondCard());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, hand, totalGamePoints, pondCard, display);
  }
}
