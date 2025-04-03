package game.deck;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import game.deck.card.Card;
import game.deck.card.SCard;

/**
 * FOR TESTING PURPOSES ONLY.
 * Identical to TypeDeck unless otherwise specified by Javadoc.
 */
public class TstDeck implements IDeck {
  Deque<Card> cards;
  ArrayList<Card> dealt;
  Deque<Card> ogCards;

  /**
   * FOR TESTING PURPOSES ONLY.
   * 56-card un-shuffled deck. Order: 1 - 7 of each suit starting with blue.
   */
  public TstDeck() {
    cards = new ArrayDeque<>();
    dealt = new ArrayList<>();
    for (SCard c : SCard.values()) {
      cards.add(new Card(c));
    }
    this.ogCards = cards;
  }

  /**
   * FOR TESTING PURPOSES ONLY.
   * Deck with custom card order and size.
   */
  public TstDeck(List<Card> cards) {
    this.cards = new ArrayDeque<>(cards);
    this.dealt = new ArrayList<>();
    this.ogCards = this.cards;
  }

  /**
   * Method body is empty (doesn't shuffle the deck).
   */
  @Override
  public void shuffle() {
  }

  @Override
  public Card popCard() {
    if (cards.isEmpty()) {
      throw new IllegalArgumentException("Deck empty");
    }
    dealt.add(cards.peek());
    return cards.pop();
  }

  /**
   * Sets cards equal to the cards initially passed to the constructor, or if none were passed, the
   *   56-card un-shuffled deck the constructor created.
   */
  @Override
  public void resetDeck() {
    cards = new ArrayDeque<>(ogCards);
    dealt = new ArrayList<>();
  }

  @Override
  public Card peekCard() {
    if (cards.isEmpty()) {
      throw new IllegalArgumentException("Deck empty");
    }
    return cards.peek();
  }

  @Override
  public int cardsSize() {
    return cards.size();
  }

  @Override
  public ArrayList<Card> getDealt() {
    return dealt;
  }

  @Override
  public Deque<Card> getCards() {
    return cards;
  }

  @Override
  public int dealtSize() {
    return dealt.size();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TstDeck o = (TstDeck) other;
    return this.getCards().toString().equals(o.getCards().toString())
            && this.getDealt().toString().equals(o.getDealt().toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(cards, dealt);
  }
}
