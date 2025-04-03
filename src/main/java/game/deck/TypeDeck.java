package game.deck;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import game.deck.card.Card;
import game.deck.card.SCard;

/**
 * A deck consisting of 56 cards, 1 through 7 for each of the 8 suits.
 */
public class TypeDeck implements IDeck {
  Deque<Card> cards;
  ArrayList<Card> dealt;
  DeckType type;

  /**
   * Creates an un-shuffled deck. If parameter dt is 'STANDARD', the deck will contain 56 cards
   *   (7 cards with ranks 1 - 7 for each of the 8 suits). If parameter dt is 'SUPERSTITIOUS', the
   *   deck will contain 49 cards (7 cards with ranks 1 - 7 for all suits except for Suit.STAR).
   */
  public TypeDeck(DeckType dt) {
    cards = new ArrayDeque<>();
    dealt = new ArrayList<>();
    type = dt;
    switch (dt) {
      case STANDARD -> {
        for (SCard c : SCard.values()) {
          cards.add(new Card(c));
        }
      }
      case SUPERSTITIOUS -> {
        for (int i = 0; i < 49; i++) {
          cards.add(new Card(SCard.values()[i]));
        }
      }
    }
  }

  //************************************************************************************** MODIFIERS
  @Override
  public void shuffle() {
    List<Card> cardList = new ArrayList<>(cards.stream().toList());
    Collections.shuffle(cardList);
    cards = new ArrayDeque<>(cardList);
  }

  @Override
  public Card popCard() {
    if (cards.isEmpty()) {
      throw new IllegalArgumentException("Deck empty");
    }
    dealt.add(cards.peek());
    return cards.pop();
  }

  @Override
  public void resetDeck() {
    TypeDeck dummy = new TypeDeck(type);
    cards = dummy.getCards();
    dealt = new ArrayList<>();
  }

  //**************************************************************************************** GETTERS
  @Override
  public Card peekCard() {
    if (cards.isEmpty()) {
      throw new IllegalArgumentException("Deck empty");
    }
    return cards.peek();
  }

  @Override
  public Deque<Card> getCards() {
    return cards;
  }

  @Override
  public ArrayList<Card> getDealt() {
    return dealt;
  }

  @Override
  public int cardsSize() {
    return cards.size();
  }

  @Override
  public int dealtSize() {
    return dealt.size();
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return "Cards: " + cards.toString() + "\nDealt: " + dealt.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TypeDeck o = (TypeDeck) other;
    return this.getCards().toString().equals(o.getCards().toString())
            && this.getDealt().toString().equals(o.getDealt().toString())
            && this.type.equals(o.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cards, dealt, type);
  }
}
