import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import game.deck.DeckType;
import game.deck.IDeck;
import game.deck.TypeDeck;
import game.deck.card.Card;
import player.IPlayer;
import player.TstPlayer;
import utility.Utility;

public class TestUtility {

  @Test
  public void testIsBlues() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.isBlues(List.of()));

    for (List<Card> hand : Z.bluesHands) {
      Assert.assertTrue(Utility.isBlues(hand));
    }
    for (List<Card> hand : Z.notBluesHands) {
      Assert.assertFalse(Utility.isBlues(hand));
    }
  }

  @Test
  public void testIsNoBlues() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.isNoBlues(Z.blues1A4B,
            List.of(), Z.fourCards));
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.isNoBlues(
            Z.fourCards, List.of(), List.of()));
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.isNoBlues(
            Z.fourCards, Z.blues1R1A3R, Z.fourCards));

    Assert.assertTrue(Utility.isNoBlues(List.of(Z.twoHeart, Z.fiveDrop, Z.fourBlue, Z.sixStar),
            List.of(), List.of(Z.fourBolt, Z.threeDrop, Z.sixHex, Z.fiveDash)));
    Assert.assertTrue(Utility.isNoBlues(List.of(Z.twoHex, Z.oneStar, Z.twoDrop, Z.sevenDash),
            List.of(Z.fiveBlue, Z.twoDash, Z.threeBolt, Z.sixCross),
            List.of(Z.fiveCross, Z.threeHeart, Z.sixCross, Z.fourCross)));
    Assert.assertTrue(Utility.isNoBlues(List.of(Z.oneDash, Z.threeCross, Z.sevenStar, Z.fourStar),
            List.of(), List.of(Z.fourBlue, Z.threeBolt, Z.twoBolt, Z.fiveHex)));
    Assert.assertTrue(Utility.isNoBlues(List.of(Z.threeStar, Z.sevenBlue, Z.oneCross, Z.sixHeart),
            List.of(Z.twoBlue, Z.fiveHex, Z.oneCross, Z.threeDrop),
            List.of(Z.sevenDash, Z.sixBolt, Z.fourBlue, Z.threeDash)));

    Assert.assertFalse(Utility.isNoBlues(List.of(Z.fiveBlue, Z.sevenHex, Z.oneDash, Z.fourHeart),
            List.of(), List.of(Z.twoHex, Z.fiveBolt, Z.threeHex, Z.oneDrop)));
    Assert.assertFalse(Utility.isNoBlues(List.of(Z.oneBlue, Z.oneCross, Z.fourBolt, Z.sixHeart),
            List.of(Z.twoStar, Z.sevenBolt, Z.threeDrop, Z.sixHex),
            List.of(Z.threeBlue, Z.fiveBlue, Z.sevenBlue, Z.sixBlue)));
  }

  @Test
  public void testNoBlues5thCard() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.noBlues5thCard(Z.joint5B,
            List.of(), List.of()));
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.noBlues5thCard(List.of(
            Z.fiveBlue, Z.sevenHex, Z.oneDash, Z.fourHeart), List.of(Z.threeBlue, Z.fiveBlue,
            Z.sevenBlue, Z.sixBlue), List.of(Z.oneHex, Z.oneCross, Z.twoDash, Z.threeDrop)));

    Assert.assertEquals(Z.twoHeart, Utility.noBlues5thCard(List.of(Z.twoHeart, Z.fiveDrop, Z.fourBlue,
            Z.sixStar), List.of(), List.of(Z.fourBolt, Z.threeDrop, Z.sixHex, Z.fiveDash)));
    Assert.assertEquals(Z.twoHex, Utility.noBlues5thCard(List.of(Z.twoHex, Z.oneStar, Z.twoDrop,
                    Z.sevenDash), List.of(Z.fiveBlue, Z.twoDash, Z.threeBolt, Z.sixCross),
            List.of(Z.fiveCross, Z.threeHeart, Z.sixCross, Z.fourCross)));
    Assert.assertEquals(Z.oneDash, Utility.noBlues5thCard(List.of(Z.oneDash, Z.threeCross, Z.sevenStar,
            Z.fourStar), List.of(), List.of(Z.fourBlue, Z.threeBolt, Z.twoBolt,
            Z.fiveHex)));
    Assert.assertEquals(Z.fiveHex, Utility.noBlues5thCard(List.of(Z.threeStar, Z.sevenBlue, Z.oneCross,
                    Z.sixHeart), List.of(Z.twoBlue, Z.fiveHex, Z.oneCross, Z.threeDrop),
            List.of(Z.sevenDash, Z.sixBolt, Z.fourBlue, Z.threeDash)));
  }

  @Test
  public void testBluesPoints() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.bluesPoints(Z.fourCards));
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.bluesPoints(Z.disjoint1B4R));

    for (int i = 0; i < Z.bluesHands.size(); i++) {
      if (i < 4) {
        Assert.assertEquals(Utility.ORDINARY, Utility.bluesPoints(Z.bluesHands.get(i)));
      } else if (i < 14) {
        Assert.assertEquals(Utility.RICH_WOMANS, Utility.bluesPoints(Z.bluesHands.get(i)));
      } else if (i < 18) {
        Assert.assertEquals(Utility.IMPERFECT, Utility.bluesPoints(Z.bluesHands.get(i)));
      } else if (i == 18) {
        Assert.assertEquals(Utility.PERFECT, Utility.bluesPoints(Z.bluesHands.get(i)));
      }
    }
  }

  @Test
  public void testPoints() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.points(List.of()));
    Assert.assertThrows(IllegalArgumentException.class, () -> Utility.points(List.of(Z.oneBlue,
            Z.oneDash, Z.oneDrop, Z.oneHex, Z.oneBolt, Z.oneCross)));

    Assert.assertEquals(10, Utility.points(Z.disjoint4B1A));
    Assert.assertEquals(6, Utility.points(Z.fourCards));
    Assert.assertEquals(22, Utility.points(Z.blues4R1B));
    Assert.assertEquals(3, Utility.points(Z.blues1B4A));
    Assert.assertEquals(11, Utility.points(Z.disjoint4R1A));
    Assert.assertEquals(0, Utility.points(Z.disjoint5A));
    Assert.assertEquals(17, Utility.points(Z.disjoint1B1R1A1R1B));
    Assert.assertEquals(8, Utility.points(Z.joint5B));
  }

  @Test
  public void testTrumps() {
    IDeck deck = new TypeDeck(DeckType.STANDARD);
    int sevenBlueTrumps = 0; // 49
    int oneBlueTrumps = 0; // 55
    int sevenStarTrumps = 0; // 0
    int twoHexTrumps = 0; // 39
    int fourHeartTrumps = 0; // 22
    int sixDashTrumps = 0; // 13
    for (Card c : deck.getCards()) {
      if (Utility.trumps(Z.sevenBlue, c)) {
        sevenBlueTrumps++;
      }
      if (Utility.trumps(Z.oneBlue, c)) {
        oneBlueTrumps++;
      }
      if (Utility.trumps(Z.sevenStar, c)) {
        sevenStarTrumps++;
      }
      if (Utility.trumps(Z.twoHex, c)) {
        twoHexTrumps++;
      }
      if (Utility.trumps(Z.fourHeart, c)) {
        fourHeartTrumps++;
      }
      if (Utility.trumps(Z.sixDash, c)) {
        sixDashTrumps++;
      }
    }
    Assert.assertEquals(49, sevenBlueTrumps);
    Assert.assertEquals(55, oneBlueTrumps);
    Assert.assertEquals(0, sevenStarTrumps);
    Assert.assertEquals(39, twoHexTrumps);
    Assert.assertEquals(22, fourHeartTrumps);
    Assert.assertEquals(13, sixDashTrumps);
  }

  @Test
  public void testSortHandByRank() {
    Assert.assertEquals(List.of(Z.oneBlue, Z.oneDash, Z.oneDrop, Z.oneHeart),
            Utility.sortHandByRank(List.of(Z.oneDash, Z.oneHeart, Z.oneDrop, Z.oneBlue)));
    Assert.assertEquals(Z.joint2A1B2A,
            Utility.sortHandByRank(List.of(Z.fiveBlue, Z.threeBlue, Z.sixBlue, Z.fourDash, Z.twoBlue)));
    Assert.assertEquals(Z.joint5B,
            Utility.sortHandByRank(List.of(Z.sevenBolt, Z.sixDash, Z.fourHex, Z.fiveDash, Z.threeDash)));
    Assert.assertEquals(Z.disjoint2R1A2R,
            Utility.sortHandByRank(List.of(Z.fiveHeart, Z.fourCross, Z.fourStar, Z.fourHeart, Z.fourBlue)));
  }

  @Test
  public void testConsecutive() {
    Assert.assertFalse(Utility.consecutive(Z.disjoint1B4R));
    Assert.assertFalse(Utility.consecutive(Z.disjoint2R1A2R));
    Assert.assertFalse(Utility.consecutive(Z.disjoint3A2R));
    Assert.assertFalse(Utility.consecutive(Z.disjoint1B1R1A1R1B));
    Assert.assertFalse(Utility.consecutive(Z.disjoint5A));

    Assert.assertTrue(Utility.consecutive(Z.blues1R4B));
    Assert.assertTrue(Utility.consecutive(Z.joint2R3B));
    Assert.assertTrue(Utility.consecutive(Z.joint3R1B1R));
    Assert.assertTrue(Utility.consecutive(Z.joint5B));
    Assert.assertTrue(Utility.consecutive(Z.blues4R1B));
  }

  @Test
  public void testPlayerChoiceOrder() {
    IPlayer player1 = new TstPlayer("Player 1");
    IPlayer player2 = new TstPlayer("Player 2");
    IPlayer player3 = new TstPlayer("Player 3");
    IPlayer player4 = new TstPlayer("Player 4");
    ArrayList<IPlayer> fourPlayers = new ArrayList<>(List.of(player1, player2, player3, player4));
    ArrayList<IPlayer> order1 = new ArrayList<>(List.of(player4, player3, player1, player2));
    ArrayList<IPlayer> order2 = new ArrayList<>(List.of(player3, player2, player4, player1));
    ArrayList<IPlayer> order3 = new ArrayList<>(List.of(player1, player4, player3, player2));
    ArrayList<Card> pond1 = new ArrayList<>(List.of(Z.fiveHex, Z.sevenHeart, Z.oneStar, Z.twoBlue));
    ArrayList<Card> pond2 = new ArrayList<>(List.of(Z.sixCross, Z.twoStar, Z.twoBolt, Z.fiveHeart));
    ArrayList<Card> pond3 = new ArrayList<>(List.of(Z.sevenBlue, Z.fourCross, Z.twoHex, Z.oneDash));

    Assert.assertEquals(Utility.playerChoiceOrder(pond1, fourPlayers), order1);
    Assert.assertEquals(Utility.playerChoiceOrder(pond2, fourPlayers), order2);
    Assert.assertEquals(Utility.playerChoiceOrder(pond3, fourPlayers), order3);
  }
}
