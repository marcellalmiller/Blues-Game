package game.deck.card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import game.deck.card.properties.Color;
import game.deck.card.properties.Rank;
import player.strategy.WinningHand;

/**
 * UCards/unsuited cards are mock Card objects that aren't used in gameplay. UCards have Color and
 *   Rank but not Suit or Position. A UCard beginning with 'BLACK' has four associated cards - the
 *   four black Suits with said UCard's rank. It follows that a UCard beginning with 'RED' has three
 *   associated cards for each of the three red Suits and a UCard beginning with 'BLUE' has just one
 *   associated card.
 */
public enum UCard {
  A1(Color.BLUE, Rank.ONE, List.of(SCard.BLUE_1)),
  A2(Color.BLUE, Rank.TWO, List.of(SCard.BLUE_2)),
  A3(Color.BLUE, Rank.THREE, List.of(SCard.BLUE_3)),
  A4(Color.BLUE, Rank.FOUR, List.of(SCard.BLUE_4)),
  A5(Color.BLUE, Rank.FIVE, List.of(SCard.BLUE_5)),
  A6(Color.BLUE, Rank.SIX, List.of(SCard.BLUE_6)),
  A7(Color.BLUE, Rank.SEVEN, List.of(SCard.BLUE_7)),

  B1(Color.BLACK, Rank.ONE, List.of(SCard.DASH_1, SCard.DROP_1, SCard.HEX_1, SCard.BOLT_1)),
  B2(Color.BLACK, Rank.TWO, List.of(SCard.DASH_2, SCard.DROP_2, SCard.HEX_2, SCard.BOLT_2)),
  B3(Color.BLACK, Rank.THREE, List.of(SCard.DASH_3, SCard.DROP_3, SCard.HEX_3, SCard.BOLT_3)),
  B4(Color.BLACK, Rank.FOUR, List.of(SCard.DASH_4, SCard.DROP_4, SCard.HEX_4, SCard.BOLT_4)),
  B5(Color.BLACK, Rank.FIVE, List.of(SCard.DASH_5, SCard.DROP_5, SCard.HEX_5, SCard.BOLT_5)),
  B6(Color.BLACK, Rank.SIX, List.of(SCard.DASH_6, SCard.DROP_6, SCard.HEX_6, SCard.BOLT_6)),
  B7(Color.BLACK, Rank.SEVEN, List.of(SCard.DASH_7, SCard.DROP_7, SCard.HEX_7, SCard.BOLT_7)),

  R1(Color.RED, Rank.ONE, List.of(SCard.CROSS_1, SCard.HEART_1, SCard.STAR_1)),
  R2(Color.RED, Rank.TWO, List.of(SCard.CROSS_2, SCard.HEART_2, SCard.STAR_2)),
  R3(Color.RED, Rank.THREE, List.of(SCard.CROSS_3, SCard.HEART_3, SCard.STAR_3)),
  R4(Color.RED, Rank.FOUR, List.of(SCard.CROSS_4, SCard.HEART_4, SCard.STAR_4)),
  R5(Color.RED, Rank.FIVE, List.of(SCard.CROSS_5, SCard.HEART_5, SCard.STAR_5)),
  R6(Color.RED, Rank.SIX, List.of(SCard.CROSS_6, SCard.HEART_6, SCard.STAR_6)),
  R7(Color.RED, Rank.SEVEN, List.of(SCard.CROSS_7, SCard.HEART_7, SCard.STAR_7));

  /**
   * Returns true if Card c is part of this UCard's associated cards/if this UCard has the same Rank
   *   and Color as c, false otherwise.
   * @param c the Card to check
   * @return true if Card c has the same Rank and Color as this UCard
   */
  public boolean satisfiedBy(Card c) {
    return c.getColor().equals(color) && c.getRank().equals(rank);
  }

  /**
   * Returns the list of WinningHands that contain this UCard sorted from highest to lowest
   *   permutations.
   * @return the list of WinningHands that contain this UCard
   */
  public List<WinningHand> containedBy() {
    List<WinningHand> containedBy = new ArrayList<>();
    for (WinningHand w : WinningHand.values()) {
      if (w.unsuitedCardList().contains(this)) containedBy.add(w);
    }
    containedBy.sort(Comparator.comparingInt(WinningHand::permutations).reversed());
    return containedBy;
  }

  /**
   * Returns the percentage of all possible WinningHands that contain this UCard. Calculated by
   *   getting all possible ways each WinningHand that contains this UCard can be arranged
   *   (permutations) and dividing the sum of these permutations by the number of ways all possible
   *   WinningHands can be arranged, then multiplying the result by one hundred.
   * @return the percentage of all possible WinningHands that contain this UCard
   */
  public double percentContainedBy() {
    List<WinningHand> containedBy = containedBy();
    int permutations = 0;
    for (WinningHand w : containedBy) {
      permutations += w.permutations();
    }
    // 11652 = number of ways all possible WinningHands can be arranged
    return permutations / 11652.0 * 100;
  }

  /**
   * Returns the UCard with the same Rank and Color as c.
   * @param c the card to convert
   * @return the UCard with the same Rank and Color as c
   */
  public static UCard toUCard(Card c) {
    switch (c.getRank()) {
      case ONE -> {
        for (UCard u : List.of(A1, B1, R1)) if (c.getColor().equals(u.color)) return u;
      }
      case TWO -> {
        for (UCard u : List.of(A2, B2, R2)) if (c.getColor().equals(u.color)) return u;
      }
      case THREE -> {
        for (UCard u : List.of(A3, B3, R3)) if (c.getColor().equals(u.color)) return u;
      }
      case FOUR -> {
        for (UCard u : List.of(A4, B4, R4)) if (c.getColor().equals(u.color)) return u;
      }
      case FIVE -> {
        for (UCard u : List.of(A5, B5, R5)) if (c.getColor().equals(u.color)) return u;
      }
      case SIX -> {
        for (UCard u : List.of(A6, B6, R6)) if (c.getColor().equals(u.color)) return u;
      }
      case SEVEN -> {
        for (UCard u : List.of(A7, B7, R7)) if (c.getColor().equals(u.color)) return u;
      }
    }
    throw new IllegalStateException("No corresponding UCard");
  }

  /**
   * Returns a list of UCards with the same Rank and Color as each Card in cards. Calls 'toUCard'
   *   to convert each card
   * @param cards the list of Cards to convert
   * @return list of UCards with same Rank and Color as each Card in cards
   */
  public static List<UCard> toUCards(List<Card> cards) {
    List<UCard> uCards = new ArrayList<>();
    for (Card c : cards) uCards.add(toUCard(c));
    return uCards;
  }

  /**
   * Returns this UCard's Color.
   * @return this UCard's Color
   */
  public Color color() {
    return color;
  }

  /**
   * Returns this UCard's Rank.
   * @return this UCard's Rank
   */
  public Rank rank() {
    return rank;
  }

  /**
   * Returns the suited cards (SCards) that this UCard represents.
   * @return the SCards this represents
   */
  public List<SCard> cards() {
    return cards;
  }

  private final Rank rank;
  private final Color color;
  private final List<SCard> cards;

  UCard(Color c, Rank r, List<SCard> cards) {
    this.rank = r;
    this.color = c;
    this.cards = cards;
  }

  @Override
  public String toString() {
    return color.toString() + " " + rank.toString();
  }
}
