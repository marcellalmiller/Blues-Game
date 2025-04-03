import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import game.deck.card.SCard;
import game.deck.card.UCard;
import game.deck.card.properties.Color;
import game.deck.card.properties.Rank;
import player.strategy.WinningHand;

public class TestUCard {

  @Test
  public void testGetters() {
    Assert.assertEquals(UCard.R3.color(), Color.RED);
    Assert.assertEquals(UCard.B7.rank(), Rank.SEVEN);
    Assert.assertEquals(UCard.A1.cards(), List.of(SCard.BLUE_1));
    Assert.assertEquals(UCard.R5.toString(), "Red five");
  }

  @Test
  public void testToUCard() {
    Assert.assertEquals(UCard.B2, UCard.toUCard(Z.twoBolt));
    Assert.assertEquals(UCard.A6, UCard.toUCard(Z.sixBlue));
    Assert.assertEquals(UCard.R4, UCard.toUCard(Z.fourStar));
  }

  @Test
  public void testSatisfiedBy() {
    Assert.assertTrue(UCard.R1.satisfiedBy(Z.oneHeart));
    Assert.assertTrue(UCard.B6.satisfiedBy(Z.sixDrop));
    Assert.assertTrue(UCard.A3.satisfiedBy(Z.threeBlue));

    Assert.assertFalse(UCard.B4.satisfiedBy(Z.fourHeart));
    Assert.assertFalse(UCard.A7.satisfiedBy(Z.sixBlue));
  }

  @Test
  public void testContainedBy() {
    Assert.assertEquals(UCard.A1.containedBy(), List.of(WinningHand.A1_B25, WinningHand.A1_R25,
            WinningHand.A14_B5, WinningHand.A14_R5, WinningHand.A15));
    Assert.assertEquals(UCard.B4.containedBy(), List.of(WinningHand.R1_B25, WinningHand.R2_B36,
            WinningHand.R3_B47, WinningHand.B14_R5, WinningHand.B25_R6, WinningHand.B36_R7,
            WinningHand.A1_B25, WinningHand.B1_A2_B35, WinningHand.B12_A3_B45, WinningHand.B14_A5,
            WinningHand.A2_B36, WinningHand.B2_A3_B46, WinningHand.B24_A5_B6, WinningHand.B25_A6,
            WinningHand.A3_B47, WinningHand.B34_A5_B67, WinningHand.B35_A6_B7, WinningHand.B36_A7));
    Assert.assertEquals(UCard.R7.containedBy(), List.of(WinningHand.B36_R7, WinningHand.B3_R47,
            WinningHand.A3_R47, WinningHand.R3_A4_R57, WinningHand.R34_A5_R67,
            WinningHand.R35_A6_R7, WinningHand.A36_R7));
  }

  @Test
  public void testPercentContainedBy() {
    for (UCard u : UCard.values()) {
      System.out.println(u.percentContainedBy());
      int permSum = 0;
      for (WinningHand w : u.containedBy()) permSum += w.permutations();
      Assert.assertEquals(u.percentContainedBy(), permSum / 11652.0 * 100);
    }
  }
}
