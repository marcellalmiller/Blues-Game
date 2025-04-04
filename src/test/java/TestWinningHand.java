import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import player.strategy.WinningHand;

public class TestWinningHand {

  @Test
  public void testPoints() {
    Assert.assertEquals(WinningHand.R3_B47.points(), 0);
    Assert.assertEquals(WinningHand.B1_R25.points(), 0);
    Assert.assertEquals(WinningHand.B14_A5.points(), -5);
    Assert.assertEquals(WinningHand.B24_A5_B6.points(), -5);
    Assert.assertEquals(WinningHand.B34_A5_B67.points(), -5);
    Assert.assertEquals(WinningHand.R1_A2_R35.points(), -5);
    Assert.assertEquals(WinningHand.A2_R36.points(), -5);
    Assert.assertEquals(WinningHand.R36_A7.points(), -5);
    Assert.assertEquals(WinningHand.A25_B6.points(), -10);
    Assert.assertEquals(WinningHand.A14_R5.points(), -10);
    Assert.assertEquals(WinningHand.A37.points(), -25);
  }

  @Test
  public void testPermutations() {
    Assert.assertEquals(WinningHand.B36_R7.permutations(), 768);
    Assert.assertEquals(WinningHand.R25_B6.permutations(), 324);
    Assert.assertEquals(WinningHand.B12_A3_B45.permutations(), 256);
    Assert.assertEquals(WinningHand.B2_A3_B46.permutations(), 256);
    Assert.assertEquals(WinningHand.A3_B47.permutations(), 256);
    Assert.assertEquals(WinningHand.R14_A5.permutations(), 81);
    Assert.assertEquals(WinningHand.R24_A5_R6.permutations(), 81);
    Assert.assertEquals(WinningHand.R34_A5_R67.permutations(), 81);
    Assert.assertEquals(WinningHand.B2_A36.permutations(), 4);
    Assert.assertEquals(WinningHand.R1_A25.permutations(), 3);
    Assert.assertEquals(WinningHand.A26.permutations(), 1);
  }

  @Test
  public void testCardsNeededFor() {
    /*
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B25_R6, Z.hand1),
            List.of(List.of(), Card.cardsWith(Rank.THREE, Color.BLACK), List.of(),
                    Card.cardsWith(Rank.FIVE, Color.BLACK),
                    Card.cardsWith(Rank.SIX, Color.RED)));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B1_R25, Z.hand2),
            List.of(List.of(), Card.cardsWith(Rank.TWO, Color.RED),
                    Card.cardsWith(Rank.THREE, Color.RED),
                    Card.cardsWith(Rank.FOUR, Color.RED), List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B14_A5, Z.hand3),
            List.of(List.of(), Card.cardsWith(Rank.TWO, Color.BLACK),
                    Card.cardsWith(Rank.THREE, Color.BLACK),
                    Card.cardsWith(Rank.FOUR, Color.BLACK), List.of(Z.fiveBlue)));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B23_A4_B56, Z.hand4),
            List.of(List.of(), List.of(), List.of(), List.of(), List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B35_A6_B7, Z.hand5),
            List.of(Card.cardsWith(Rank.THREE, Color.BLACK), Card.cardsWith(Rank.FOUR, Color.BLACK),
                    Card.cardsWith(Rank.FIVE, Color.BLACK), List.of(Z.sixBlue), List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.R12_A3_R45, Z.hand6),
            List.of(Card.cardsWith(Rank.ONE, Color.RED), Card.cardsWith(Rank.TWO, Color.RED),
                    List.of(Z.threeBlue), Card.cardsWith(Rank.FOUR, Color.RED),
                    Card.cardsWith(Rank.FIVE, Color.RED)));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.A2_R36, Z.hand7),
            List.of(List.of(), List.of(), Card.cardsWith(Rank.FOUR, Color.RED),
                    Card.cardsWith(Rank.FIVE, Color.RED), List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.R3_A4_R57, Z.hand8),
            List.of(Card.cardsWith(Rank.THREE, Color.RED), List.of(Z.fourBlue),
                    Card.cardsWith(Rank.FIVE, Color.RED), Card.cardsWith(Rank.SIX, Color.RED),
                    List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.B3_A47, Z.hand9),
            List.of(Card.cardsWith(Rank.THREE, Color.BLACK), List.of(Z.fourBlue),
                    List.of(Z.fiveBlue), List.of(Z.sixBlue), List.of(Z.sevenBlue)));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.A14_R5, Z.hand10),
            List.of(List.of(), List.of(Z.twoBlue), List.of(), List.of(), List.of()));
    Assert.assertEquals(WinningHand.cardsNeededFor(WinningHand.A37, Z.hand11),
            List.of(List.of(Z.threeBlue), List.of(Z.fourBlue), List.of(Z.fiveBlue),
                    List.of(Z.sixBlue), List.of(Z.sevenBlue)));
     */
  }

  @Test
  public void testCardsAwayFrom() {
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B25_R6, Z.hand1), 3);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B1_R25, Z.hand2), 3);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B14_A5, Z.hand3), 4);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B23_A4_B56, Z.hand4), 0);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B35_A6_B7, Z.hand5), 4);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.R12_A3_R45, Z.hand6), 5);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.A2_R36, Z.hand7), 2);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.R3_A4_R57, Z.hand8), 4);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.B3_A47, Z.hand9), 5);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.A14_R5, Z.hand10), 1);
    Assert.assertEquals(WinningHand.cardsAwayFrom(WinningHand.A37, Z.hand11), 5);
  }

  @Test
  public void testKeepIfDesired() {
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B25_R6, Z.hand1),
            List.of(Z.twoHex, Z.fourHex));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B1_R25, Z.hand2),
            List.of(Z.oneBolt, Z.fiveStar));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B14_A5, Z.hand3), List.of(Z.oneDrop));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B23_A4_B56, Z.hand4),
            List.of(Z.twoBolt, Z.threeDash, Z.fourBlue, Z.fiveBolt, Z.sixDrop));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B35_A6_B7, Z.hand5),
            List.of(Z.sevenDrop));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.R12_A3_R45, Z.hand6), List.of());
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.A2_R36, Z.hand7),
            List.of(Z.twoBlue, Z.threeCross, Z.sixHeart));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.R3_A4_R57, Z.hand8),
            List.of(Z.sevenStar));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.B3_A47, Z.hand9), List.of());
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.A14_R5, Z.hand10),
            List.of(Z.oneBlue, Z.threeBlue, Z.fourBlue, Z.fiveCross));
    Assert.assertEquals(WinningHand.keepIfDesired(WinningHand.A37, Z.hand11), List.of());
  }

  @Test
  public void testDiscardIfDesired() {
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B25_R6, Z.hand1),
            List.of(Z.twoDash, Z.threeCross, Z.fourBlue));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B1_R25, Z.hand2),
            List.of(Z.twoBlue, Z.sevenBlue, Z.sevenStar));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B14_A5, Z.hand3),
            List.of(Z.threeStar, Z.fourHeart, Z.sixHeart, Z.sevenDash));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B23_A4_B56, Z.hand4), List.of());
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B35_A6_B7, Z.hand5),
            List.of(Z.twoDrop, Z.sixCross, Z.sevenCross, Z.sevenHeart));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.R12_A3_R45, Z.hand6),
            List.of(Z.oneDash, Z.oneHex, Z.oneBolt, Z.sixBolt, Z.sevenBlue));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.A2_R36, Z.hand7),
            List.of(Z.fourDash, Z.fiveHex));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.R3_A4_R57, Z.hand8),
            List.of(Z.oneHex, Z.threeBlue, Z.sixHex, Z.sevenBolt));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.B3_A47, Z.hand9),
            List.of(Z.fourDash, Z.fiveDash, Z.fiveCross, Z.sevenHeart, Z.sevenStar));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.A14_R5, Z.hand10),
            List.of(Z.fourDash));
    Assert.assertEquals(WinningHand.discardIfDesired(WinningHand.A37, Z.hand11),
            List.of(Z.oneDrop, Z.oneBolt, Z.twoBolt, Z.threeBolt, Z.threeCross));
  }

  @Test
  public void testClosestTo() {
    System.out.println(WinningHand.closestTo(Z.hand1));
    System.out.println(WinningHand.closestTo(Z.hand2));
    System.out.println(WinningHand.closestTo(Z.hand3));
    System.out.println(WinningHand.closestTo(Z.hand4));
    System.out.println(WinningHand.closestTo(Z.hand5));
    System.out.println(WinningHand.closestTo(Z.hand6));
    System.out.println(WinningHand.closestTo(Z.hand7));
    System.out.println(WinningHand.closestTo(Z.hand8));
    System.out.println(WinningHand.closestTo(Z.hand9));
    System.out.println(WinningHand.closestTo(Z.hand10));
    System.out.println(WinningHand.closestTo(Z.hand11));
  }

  @Test
  public void testPermsWOHasAndDiscarded() {
    System.out.println("Total perms: " + WinningHand.R34_A5_R67.permutations());
    System.out.println("Perms without has, has doesn't contain: " + WinningHand.permsWOHas(WinningHand.R34_A5_R67, List.of(Z.sevenDash, Z.oneCross, Z.sixHex, Z.fiveHex, Z.sevenBlue)));
    System.out.println("Perms without has, has contains: " + WinningHand.permsWOHas(WinningHand.R34_A5_R67, List.of(Z.threeStar, Z.fourCross, Z.threeBlue, Z.sixHex, Z.fiveDrop)));
    System.out.println("Perms without has and discarded, has doesn't contain, discarded doesn't contain: " + WinningHand.permsWOHasAndDiscarded(WinningHand.R34_A5_R67, List.of(Z.sevenDash, Z.oneCross, Z.sixHex, Z.fiveHex, Z.sevenBlue), List.of()));
    System.out.println("Perms without has and discarded, has contains, discarded doesn't contain: " + WinningHand.permsWOHasAndDiscarded(WinningHand.R34_A5_R67, List.of(Z.threeStar, Z.fourCross, Z.threeBlue, Z.sixHex, Z.fiveDrop), List.of()));
    System.out.println("Perms without has and discarded, has contains, discarded contains: " + WinningHand.permsWOHasAndDiscarded(WinningHand.R34_A5_R67, List.of(Z.threeStar, Z.fourCross, Z.threeBlue, Z.sixHex, Z.fiveDrop), List.of(Z.fiveBlue, Z.sixHeart)));
    System.out.println("Perms without has and discarded, has doesn't contain, discarded contains: " + WinningHand.permsWOHasAndDiscarded(WinningHand.R34_A5_R67, List.of(Z.sevenDash, Z.oneCross, Z.sixHex, Z.fiveHex, Z.sevenBlue), List.of(Z.fiveBlue, Z.sixHeart)));
  }
}
