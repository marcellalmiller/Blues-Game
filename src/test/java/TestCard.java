import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.deck.card.properties.Rank;
import game.deck.card.properties.Color;
import game.deck.card.SCard;
import game.deck.card.properties.Suit;

public class TestCard {

  @Test
  public void testCardConstructor() {
    Assert.assertEquals(Color.RED, Z.oneStar.getColor());
    Assert.assertEquals(Rank.ONE, Z.oneStar.getRank());
    Assert.assertEquals(Suit.STAR, Z.oneStar.getSuit());
    Assert.assertEquals(Position.DECK, Z.oneStar.getPosition());

    Assert.assertEquals(Color.BLACK, Z.sevenDash.getColor());
    Assert.assertEquals(Rank.SEVEN, Z.sevenDash.getRank());
    Assert.assertEquals(Suit.DASH, Z.sevenDash.getSuit());
    Assert.assertEquals(Position.DECK, Z.sevenDash.getPosition());

    Assert.assertEquals(Color.BLUE, Z.sixBlue.getColor());
    Assert.assertEquals(Rank.SIX, Z.sixBlue.getRank());
    Assert.assertEquals(Suit.BLUE, Z.sixBlue.getSuit());
    Assert.assertEquals(Position.DECK, Z.sixBlue.getPosition());
  }

  @Test
  public void testCardMethods() {
    Z.oneStar.setPosition(Position.HAND);
    Assert.assertEquals(Position.HAND, Z.oneStar.getPosition());
    Z.oneStar.setPosition(Position.DISCARDED);
    Assert.assertEquals(Position.DISCARDED, Z.oneStar.getPosition());
  }

  @Test
  public void testRankMethods() {
    Assert.assertEquals(1, Z.oneStar.getRank().number());
    Assert.assertEquals(1, Z.oneDash.getRank().number());
    Assert.assertEquals(2, Z.twoHeart.getRank().number());
    Assert.assertEquals(3, Z.threeCross.getRank().number());
    Assert.assertEquals(4, Z.fourBolt.getRank().number());
    Assert.assertEquals(5, Z.fiveHex.getRank().number());
    Assert.assertEquals(6, Z.sixBlue.getRank().number());
    Assert.assertEquals(6, Z.sixDrop.getRank().number());
    Assert.assertEquals(7, Z.sevenDash.getRank().number());
    Assert.assertEquals(7, Z.sevenBlue.getRank().number());
  }

  @Test
  public void testSuitMethods() {
    Assert.assertEquals(7, Z.oneStar.getSuit().points());
    Assert.assertEquals(1, Z.oneDash.getSuit().points());
    Assert.assertEquals(6, Z.twoHeart.getSuit().points());
    Assert.assertEquals(5, Z.threeCross.getSuit().points());
    Assert.assertEquals(4, Z.fourBolt.getSuit().points());
    Assert.assertEquals(3, Z.fiveHex.getSuit().points());
    Assert.assertEquals(2, Z.sixDrop.getSuit().points());
    Assert.assertEquals(1, Z.sevenDash.getSuit().points());
    Assert.assertEquals(0, Z.sixBlue.getSuit().points());
    Assert.assertEquals(0, Z.sevenBlue.getSuit().points());

    Assert.assertEquals(Color.BLUE, Z.sixBlue.getSuit().color());
    Assert.assertEquals(Color.RED, Z.oneStar.getSuit().color());
    Assert.assertEquals(Color.RED, Z.twoHeart.getSuit().color());
    Assert.assertEquals(Color.RED, Z.threeCross.getSuit().color());
    Assert.assertEquals(Color.BLACK, Z.fourBolt.getSuit().color());
    Assert.assertEquals(Color.BLACK, Z.fiveHex.getSuit().color());
    Assert.assertEquals(Color.BLACK, Z.sixDrop.getSuit().color());
    Assert.assertEquals(Color.BLACK, Z.sevenDash.getSuit().color());

    Assert.assertEquals("♦", Z.sixBlue.getSuit().symbol());
    Assert.assertEquals("⭒", Z.oneStar.getSuit().symbol());
    Assert.assertEquals("♡", Z.twoHeart.getSuit().symbol());
    Assert.assertEquals("x", Z.threeCross.getSuit().symbol());
    Assert.assertEquals("≷", Z.fourBolt.getSuit().symbol());
    Assert.assertEquals("⬡", Z.fiveHex.getSuit().symbol());
    Assert.assertEquals("⬯", Z.sixDrop.getSuit().symbol());
    Assert.assertEquals("~", Z.sevenDash.getSuit().symbol());
  }

  @Test
  public void testColorSGetters() {
    Assert.assertEquals(Color.RED.ansiColor(), List.of("\033[31m", "\033[0m"));
    Assert.assertEquals(Color.BLACK.ansiColor(), List.of("\033[37m", "\033[0m"));
    Assert.assertEquals(Color.BLUE.ansiColor(), List.of("\033[36m", "\033[0m"));

    Assert.assertEquals(Color.RED.suits(), List.of(Suit.CROSS, Suit.HEART, Suit.STAR));
    Assert.assertEquals(Color.BLACK.suits(), List.of(Suit.DASH, Suit.DROP, Suit.HEX, Suit.BOLT));
    Assert.assertEquals(Color.BLUE.suits(), List.of(Suit.BLUE));

    Assert.assertEquals(Z.fourDrop.ansiTS(), "\033[37m4⬯\033[0m");
    Assert.assertEquals(Z.sevenBlue.ansiTS(), "\033[36m7♦\033[0m");
    Assert.assertEquals(Z.oneHeart.ansiTS(), "\033[31m1♡\033[0m");
  }

  @Test
  public void testTrumpsAmount() {
    int trumpsAmt = 55;
    for (Card c : Z.deckByTrumpAmt) {
      Assert.assertEquals(trumpsAmt, c.trumpsAmount());
      trumpsAmt--;
    }
  }

  /*
  @Test
  public void testDesirability() {
    List<Card> sorted = new ArrayList<>(Z.deck56);
    sorted.sort(Comparator.comparingDouble(Card::desirability).reversed());
    double averageDesirability = 0;
    int between66and100 = 0;
    int between33and66 = 0;
    int between0and33 = 0;
    for (Card c : sorted) {
      double desirability = c.desirability();
      if (desirability > 66.6) between66and100++;
      else if (desirability < 33.3) between0and33++;
      else between33and66++;

      averageDesirability += desirability;
      System.out.println(c.coloredTS() + " desirability = " + desirability);
      System.out.println("    - trumps " + c.trumpsAmount() + " cards");
      System.out.println("    - contained by "
              + (Math.round(UCard.toUCard(c).percentContainedBy() * 10.0) / 10.0)
              + "% of WinningHands");
    }
    System.out.println("\nAverage desirability: " + (averageDesirability / 56));
    System.out.println("Median desirability: "
            + ((sorted.get(27).desirability() + sorted.get(28).desirability()) / 2));
    System.out.println(between0and33 + " cards below 33.3 desirability (low desirability)");
    System.out.println(between33and66 + " cards between 33.3 and 66.6 desirability (medium "
            + "desirability)");
    System.out.println(between66and100 + " cards above 66.6 desirability (high desirability)");
  }

   */

  @Test
  public void testToString() {
    Assert.assertEquals("1⭒", Z.oneStar.toString());
    Assert.assertEquals("2♡", Z.twoHeart.toString());
    Assert.assertEquals("3x", Z.threeCross.toString());
    Assert.assertEquals("4≷", Z.fourBolt.toString());
    Assert.assertEquals("5⬡", Z.fiveHex.toString());
    Assert.assertEquals("6⬯", Z.sixDrop.toString());
    Assert.assertEquals("7~", Z.sevenDash.toString());
    Assert.assertEquals("7♦", Z.sevenBlue.toString());
  }

  @Test
  public void testCardGCOs() {
    Assert.assertEquals(new Card(SCard.STAR_1), Z.oneStar);
    Assert.assertEquals(new Card(SCard.DASH_7), Z.sevenDash);
    Assert.assertEquals(new Card(SCard.HEX_1), Z.oneHex);
    Assert.assertNotEquals(new Card(SCard.HEX_4), Z.fourBolt);
    Assert.assertNotEquals(new Object(), Z.threeBlue);
    Assert.assertEquals(new Card(SCard.STAR_1).hashCode(), Z.oneStar.hashCode());
    Assert.assertEquals(new Card(SCard.BLUE_5).hashCode(), Z.fiveBlue.hashCode());
    Assert.assertNotEquals(new Object().hashCode(), Z.sevenCross.hashCode());
    Assert.assertNotEquals(Z.fourBlue.hashCode(), Z.fiveBlue.hashCode());
  }

  @Test
  public void testEnumGCOs() {
    Assert.assertEquals(Color.RED.toString(), "Red");
    Assert.assertEquals(Position.DISCARDED.toString(), "DISCARDED");
    Assert.assertEquals(Rank.FIVE.toString(), "five");
    Assert.assertEquals(Suit.BOLT.toString(), "≷");
  }
}
