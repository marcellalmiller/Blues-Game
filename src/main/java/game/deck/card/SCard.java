package game.deck.card;

import game.deck.card.properties.Rank;
import game.deck.card.properties.Suit;

/**
 * Suited Card
 */
public enum SCard {
  // Blue suits
  // Blue
  BLUE_1(Suit.BLUE, Rank.ONE),
  BLUE_2(Suit.BLUE, Rank.TWO),
  BLUE_3(Suit.BLUE, Rank.THREE),
  BLUE_4(Suit.BLUE, Rank.FOUR),
  BLUE_5(Suit.BLUE, Rank.FIVE),
  BLUE_6(Suit.BLUE, Rank.SIX),
  BLUE_7(Suit.BLUE, Rank.SEVEN),

  // Black suits
  // Dash
  DASH_1(Suit.DASH, Rank.ONE),
  DASH_2(Suit.DASH, Rank.TWO),
  DASH_3(Suit.DASH, Rank.THREE),
  DASH_4(Suit.DASH, Rank.FOUR),
  DASH_5(Suit.DASH, Rank.FIVE),
  DASH_6(Suit.DASH, Rank.SIX),
  DASH_7(Suit.DASH, Rank.SEVEN),
  // Drop
  DROP_1(Suit.DROP, Rank.ONE),
  DROP_2(Suit.DROP, Rank.TWO),
  DROP_3(Suit.DROP, Rank.THREE),
  DROP_4(Suit.DROP, Rank.FOUR),
  DROP_5(Suit.DROP, Rank.FIVE),
  DROP_6(Suit.DROP, Rank.SIX),
  DROP_7(Suit.DROP, Rank.SEVEN),
  // Hex
  HEX_1(Suit.HEX, Rank.ONE),
  HEX_2(Suit.HEX, Rank.TWO),
  HEX_3(Suit.HEX, Rank.THREE),
  HEX_4(Suit.HEX, Rank.FOUR),
  HEX_5(Suit.HEX, Rank.FIVE),
  HEX_6(Suit.HEX, Rank.SIX),
  HEX_7(Suit.HEX, Rank.SEVEN),
  // Bolt BOLT
  BOLT_1(Suit.BOLT, Rank.ONE),
  BOLT_2(Suit.BOLT, Rank.TWO),
  BOLT_3(Suit.BOLT, Rank.THREE),
  BOLT_4(Suit.BOLT, Rank.FOUR),
  BOLT_5(Suit.BOLT, Rank.FIVE),
  BOLT_6(Suit.BOLT, Rank.SIX),
  BOLT_7(Suit.BOLT, Rank.SEVEN),

  // Red suits
  // Cross
  CROSS_1(Suit.CROSS, Rank.ONE),
  CROSS_2(Suit.CROSS, Rank.TWO),
  CROSS_3(Suit.CROSS, Rank.THREE),
  CROSS_4(Suit.CROSS, Rank.FOUR),
  CROSS_5(Suit.CROSS, Rank.FIVE),
  CROSS_6(Suit.CROSS, Rank.SIX),
  CROSS_7(Suit.CROSS, Rank.SEVEN),
  // Heart
  HEART_1(Suit.HEART, Rank.ONE),
  HEART_2(Suit.HEART, Rank.TWO),
  HEART_3(Suit.HEART, Rank.THREE),
  HEART_4(Suit.HEART, Rank.FOUR),
  HEART_5(Suit.HEART, Rank.FIVE),
  HEART_6(Suit.HEART, Rank.SIX),
  HEART_7(Suit.HEART, Rank.SEVEN),
  // Star
  STAR_1(Suit.STAR, Rank.ONE),
  STAR_2(Suit.STAR, Rank.TWO),
  STAR_3(Suit.STAR, Rank.THREE),
  STAR_4(Suit.STAR, Rank.FOUR),
  STAR_5(Suit.STAR, Rank.FIVE),
  STAR_6(Suit.STAR, Rank.SIX),
  STAR_7(Suit.STAR, Rank.SEVEN);

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  private final Suit suit;
  private final Rank rank;

  SCard(Suit s, Rank r) {
    this.suit = s;
    this.rank = r;
  }
}
