package player.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.deck.card.Card;
import game.deck.card.properties.Color;
import utility.Utility;

/**
 * The approaches an 'IStrategy' can use to play a game of Blues.
 */
public enum Approach {
  /**
   * MIN_POINTS attempts to minimize the points in a player's hand.
   */
  MIN_POINTS {
    /**
     * Returns hand in order of recommended discard. Cards are ordered from highest to lowest
     *   suit points. If two cards have the same suit, the one with the lower rank appears first.
     * @param hand the hand to reorder
     * @return hand in order of recommended discard
     */
    @Override
    public List<Card> recDiscard(List<Card> hand) {
      List<Card> sorted = new ArrayList<>(hand);
      sorted.sort(Comparator.comparing((Card card) -> card.getSuit().points()).reversed()
              .thenComparing(card -> card.getRank().number()));
      return sorted;
    }

    /**
     * Returns combined well and pond in order of recommended choice. Cards are ordered from lowest
     *   to highest amount of points hand gains if the card is added. If two cards add the same
     *   amount of points to hand, the card with the lower rank appears first. If two cards add the
     *   same amount of points and have the same rank, the card with the lower suit points appears
     *   first.
     * @param hand the hand to consider
     * @param well the well to reorder
     * @param pond the pond to reorder
     * @return combined well and pond in order of recommended choice
     */
    @Override
    public List<Card> recChoose(List<Card> hand, List<Card> well, List<Card> pond) {
      ArrayList<Card> sorted = new ArrayList<>(pond);
      sorted.addAll(well);
      ArrayList<Integer> addsPoints = new ArrayList<>();

      for (Card c : sorted) {
        List<Card> handCopy = new ArrayList<>(hand);
        int without = Utility.points(hand);
        handCopy.add(c);
        int with = Utility.points(handCopy);
        addsPoints.add(with - without);
      }

      sorted.sort(Comparator.comparingInt((Card card) -> addsPoints.get(sorted.indexOf(card)))
              .thenComparingInt(card -> card.getRank().number())
              .thenComparingInt(card -> card.getSuit().points()));
      return sorted;
    }
  },

  /**
   * MAX_TRUMP attempts to maximize the amount of trump cards in a player's hand.
   */
  MAX_TRUMP {
    /**
     * Returns hand in order of recommended choice. Red and black cards appear first, ordered from
     *   highest to lowest rank number - if two cards have the same rank, the card with the highest
     *   suit points appears first. Blue cards appear last, ordered from highest to lowest rank
     *   number.
     * @param hand the hand to reorder
     * @return combined well and pond in order of recommended choice
     */
    @Override
    public List<Card> recDiscard(List<Card> hand) {
      List<Card> handCopy = new ArrayList<>(hand);
      List<Card> sorted = new ArrayList<>();
      for (Card c : hand) {
        if (c.getColor().equals(Color.BLUE)) {
          sorted.add(c);
          handCopy.remove(c);
        }
      }
      sorted.sort(Comparator.comparingInt((Card card) -> card.getRank().number()).reversed());
      handCopy.sort(Comparator.comparing((Card card) -> card.getRank().number()).reversed()
              .thenComparing(card -> card.getSuit().points()).reversed());
      handCopy.addAll(sorted);
      return handCopy;
    }

    /**
     * Returns combined well and pond in order of recommended choice. Blue cards appear first,
     *   ordered from lowest to highest rank number. Black and red cards appear last, ordered from
     *   lowest to highest rank number. If two cards have the same rank, the one with lower suit
     *   points appears first.
     * @param hand ignored in this implementation
     * @param well the well to reorder
     * @param pond the pond to reorder
     * @return combined well and pond in order of recommended choice
     */
    @Override
    public List<Card> recChoose(List<Card> hand, List<Card> well, List<Card> pond) {
      ArrayList<Card> wend = new ArrayList<>(pond);
      wend.addAll(well);
      ArrayList<Card> wendCopy = new ArrayList<>(wend);
      ArrayList<Card> sorted = new ArrayList<>();
      for (Card c : wend) {
        if (c.getColor().equals(Color.BLUE)) {
          sorted.add(c);
          wendCopy.remove(c);
        }
      }

      sorted.sort(Comparator.comparingInt((Card card) -> card.getRank().number()));
      wendCopy.sort(Comparator.comparing((Card card) -> card.getRank().number())
              .thenComparing(card -> card.getSuit().points()));
      sorted.addAll(wendCopy);
      return sorted;
    }
  },

  /**
   * RANDOM makes random recommendations.
   */
  RANDOM {
    /**
     * Returns hand in random order.
     * @param hand the hand to reorder
     * @return combined well and pond in random order
     */
    @Override
    public List<Card> recDiscard(List<Card> hand) {
      List<Card> randomOrder = new ArrayList<>(hand);
      Collections.shuffle(randomOrder);
      return randomOrder;
    }

    /**
     * Returns combined well and pond in random order.
     * @param hand ignored in this implementation
     * @param well the well to reorder
     * @param pond the pond to reorder
     * @return combined well and pond in random order
     */
    @Override
    public List<Card> recChoose(List<Card> hand, List<Card> well, List<Card> pond) {
      ArrayList<Card> wend = new ArrayList<>(pond);
      wend.addAll(well);
      Collections.shuffle(wend);
      return wend;
    }
  };

  //******************************************************************************* ABSTRACT METHODS
  /**
   * Returns hand in order of recommended discard.
   * @param hand the hand to reorder
   * @return hand in order of recommended discard
   */
  public abstract List<Card> recDiscard(List<Card> hand);

  /**
   * Returns combined well and pond in order of recommended choice.
   * @param hand the hand to consider
   * @param well the well to reorder
   * @param pond the pond to reorder
   * @return combined well and pond in order of recommended choice
   */
  public abstract List<Card> recChoose(List<Card> hand, List<Card> well, List<Card> pond);
}
