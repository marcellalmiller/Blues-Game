import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import game.deck.card.SCard;
import game.deck.card.properties.Rank;
import game.deck.card.properties.Suit;
import player.IPlayer;
import player.TstPlayer;

/**
 * Constants and static methods defined for testing purposes.
 */
public class Z {
  /**
   * All cards in a standard 56 card deck.
   */
  // Blue 1 - 7
  public static Card oneBlue = new Card(SCard.BLUE_1);
  public static Card twoBlue = new Card(SCard.BLUE_2);
  public static Card threeBlue = new Card(SCard.BLUE_3);
  public static Card fourBlue = new Card(SCard.BLUE_4);
  public static Card fiveBlue = new Card(SCard.BLUE_5);
  public static Card sixBlue = new Card(SCard.BLUE_6);
  public static Card sevenBlue = new Card(SCard.BLUE_7);
  // Dash 1 - 7
  public static Card oneDash = new Card(SCard.DASH_1);
  public static Card twoDash = new Card(SCard.DASH_2);
  public static Card threeDash = new Card(SCard.DASH_3);
  public static Card fourDash = new Card(SCard.DASH_4);
  public static Card fiveDash = new Card(SCard.DASH_5);
  public static Card sixDash = new Card(SCard.DASH_6);
  public static Card sevenDash = new Card(SCard.DASH_7);
  // Drop 1 - 7
  public static Card oneDrop = new Card(SCard.DROP_1);
  public static Card twoDrop = new Card(SCard.DROP_2);
  public static Card threeDrop = new Card(SCard.DROP_3);
  public static Card fourDrop = new Card(SCard.DROP_4);
  public static Card fiveDrop = new Card(SCard.DROP_5);
  public static Card sixDrop = new Card(SCard.DROP_6);
  public static Card sevenDrop = new Card(SCard.DROP_7);
  // Hex 1 - 7
  public static Card oneHex = new Card(SCard.HEX_1);
  public static Card twoHex = new Card(SCard.HEX_2);
  public static Card threeHex = new Card(SCard.HEX_3);
  public static Card fourHex = new Card(SCard.HEX_4);
  public static Card fiveHex = new Card(SCard.HEX_5);
  public static Card sixHex = new Card(SCard.HEX_6);
  public static Card sevenHex = new Card(SCard.HEX_7);
  // Bolt 1 - 7
  public static Card oneBolt = new Card(SCard.BOLT_1);
  public static Card twoBolt = new Card(SCard.BOLT_2);
  public static Card threeBolt = new Card(SCard.BOLT_3);
  public static Card fourBolt = new Card(SCard.BOLT_4);
  public static Card fiveBolt = new Card(SCard.BOLT_5);
  public static Card sixBolt = new Card(SCard.BOLT_6);
  public static Card sevenBolt = new Card(SCard.BOLT_7);
  // Cross 1 - 7
  public static Card oneCross = new Card(SCard.CROSS_1);
  public static Card twoCross = new Card(SCard.CROSS_2);
  public static Card threeCross = new Card(SCard.CROSS_3);
  public static Card fourCross = new Card(SCard.CROSS_4);
  public static Card fiveCross = new Card(SCard.CROSS_5);
  public static Card sixCross = new Card(SCard.CROSS_6);
  public static Card sevenCross = new Card(SCard.CROSS_7);
  // Heart 1 - 7
  public static Card oneHeart = new Card(SCard.HEART_1);
  public static Card twoHeart = new Card(SCard.HEART_2);
  public static Card threeHeart = new Card(SCard.HEART_3);
  public static Card fourHeart = new Card(SCard.HEART_4);
  public static Card fiveHeart = new Card(SCard.HEART_5);
  public static Card sixHeart = new Card(SCard.HEART_6);
  public static Card sevenHeart = new Card(SCard.HEART_7);
  // Star 1 - 7
  public static Card oneStar = new Card(SCard.STAR_1);
  public static Card twoStar = new Card(SCard.STAR_2);
  public static Card threeStar = new Card(SCard.STAR_3);
  public static Card fourStar = new Card(SCard.STAR_4);
  public static Card fiveStar = new Card(SCard.STAR_5);
  public static Card sixStar = new Card(SCard.STAR_6);
  public static Card sevenStar = new Card(SCard.STAR_7);
  // List of four cards:
  public static List<Card> fourCards = List.of(oneBlue, oneDash, oneDrop, oneHex);
  // List of all cards:
  public static List<Card> deck56 = List.of(oneBlue, twoBlue, threeBlue, fourBlue, fiveBlue,
          sixBlue, sevenBlue, oneDash, twoDash, threeDash, fourDash, fiveDash, sixDash, sevenDash,
          oneDrop, twoDrop, threeDrop, fourDrop, fiveDrop, sixDrop, sevenDrop, oneHex, twoHex,
          threeHex, fourHex, fiveHex, sixHex, sevenHex, oneBolt, twoBolt, threeBolt, fourBolt,
          fiveBolt, sixBolt, sevenBolt, oneCross, twoCross, threeCross, fourCross, fiveCross,
          sixCross, sevenCross, oneHeart, twoHeart, threeHeart, fourHeart, fiveHeart, sixHeart,
          sevenHeart, oneStar, twoStar, threeStar, fourStar, fiveStar, sixStar, sevenStar);
  public static List<Card> deckByTrumpAmt = List.of(oneBlue, twoBlue, threeBlue, fourBlue, fiveBlue,
          sixBlue, sevenBlue, oneDash, oneDrop, oneHex, oneBolt, oneCross, oneHeart, oneStar,
          twoDash, twoDrop, twoHex, twoBolt, twoCross, twoHeart, twoStar, threeDash, threeDrop,
          threeHex, threeBolt, threeCross, threeHeart, threeStar, fourDash, fourDrop, fourHex,
          fourBolt, fourCross, fourHeart, fourStar, fiveDash, fiveDrop, fiveHex, fiveBolt,
          fiveCross, fiveHeart, fiveStar, sixDash, sixDrop, sixHex, sixBolt, sixCross, sixHeart,
          sixStar, sevenDash, sevenDrop, sevenHex, sevenBolt, sevenCross, sevenHeart, sevenStar);
  public static List<Card> immediateBlues4p = List.of(oneBlue, twoBlue, threeBlue, fourBlue,
          twoStar, sixBlue, sevenBlue, oneDash,
          threeStar, twoDash, threeDash, fourDash,
          fourStar, sixDash, sevenDash, oneDrop,
          fiveStar, threeDrop, fourDrop, fiveDrop,
          sixDrop, sevenDrop, oneHex, twoHex);

  // All blues winning hands by color (there are 11,666 total winning hands).
  // 0 points 0 - 3
  public static List<Card> blues1R4B = List.of(oneCross, twoDrop, threeDash, fourBolt, fiveHex); // 15
  public static List<Card> blues4B1R = List.of(twoDash, threeBolt, fourHex, fiveDash, sixStar); // 15
  public static List<Card> blues1B4R = List.of(threeDrop, fourHeart, fiveCross, sixCross, sevenHeart); // 13
  public static List<Card> blues4R1B = List.of(oneHeart, twoCross, threeStar, fourCross, fiveBolt); // 22
  // - 5 points 4 - 13
  public static List<Card> blues1A4B = List.of(twoBlue, threeBolt, fourDrop, fiveDrop, sixDrop); // 6
  public static List<Card> blues1B1A3B = List.of(threeBolt, fourBlue, fiveHex, sixDash, sevenDrop); // 10
  public static List<Card> blues2B1A2B = List.of(oneHex, twoBolt, threeBlue, fourDash, fiveHex); // 6
  public static List<Card> blues3B1A1B = List.of(twoHex, threeHex, fourHex, fiveBlue, sixHex); // 3
  public static List<Card> blues4B1A = List.of(threeDrop, fourDash, fiveBolt, sixBolt, sevenBlue); // 7
  public static List<Card> blues1A4R = List.of(oneBlue, twoHeart, threeCross, fourStar, fiveHeart); // 18
  public static List<Card> blues1R1A3R = List.of(twoStar, threeBlue, fourStar, fiveStar, sixStar); // 7
  public static List<Card> blues2R1A2R = List.of(threeHeart, fourCross, fiveBlue, sixHeart, sevenStar); // 18
  public static List<Card> blues3R1A1R = List.of(oneStar, twoCross, threeStar, fourBlue, fiveCross); // 12
  public static List<Card> blues4R1A = List.of(twoHeart, threeStar, fourCross, fiveStar, sixBlue); // 18
  // - 10 points 14 - 17
  public static List<Card> blues1B4A = List.of(threeHex, fourBlue, fiveBlue, sixBlue, sevenBlue); // 3
  public static List<Card> blues4A1B = List.of(oneBlue, twoBlue, threeBlue, fourBlue, fiveDrop); // 2
  public static List<Card> blues1R4A = List.of(twoStar, threeBlue, fourBlue, fiveBlue, sixBlue); // 7
  public static List<Card> blues4A1R = List.of(threeBlue, fourBlue, fiveBlue, sixBlue, sevenHeart); // 6
  // - 25 points 18
  public static List<Card> blues5A = List.of(oneBlue, twoBlue, threeBlue, fourBlue, fiveBlue); // 0
  // LIST:
  public static List<List<Card>> bluesHands = List.of(blues1R4B, blues4B1R, blues1B4R, blues4R1B,
          blues1A4B, blues1B1A3B, blues2B1A2B, blues3B1A1B, blues4B1A, blues1A4R, blues1R1A3R,
          blues2R1A2R, blues3R1A1R, blues4R1A, blues1B4A, blues4A1B, blues1R4A, blues4A1R, blues5A);

  /**
   * Hands that aren't blues.
   */
  // not consecutive, correct color ratio, correct color order
  public static List<Card> disjoint1R4B = List.of(twoHeart, threeBolt, fourDrop, sixBolt, sevenHex);
  public static List<Card> disjoint4B1R = List.of(threeHex, fourDash, fiveBolt, sixHex, sixCross);
  public static List<Card> disjoint1B4R = List.of(oneDash, twoHeart, fourStar, sixHeart, sevenCross);
  public static List<Card> disjoint4R1B = List.of(twoCross, threeStar, fiveCross, sixCross, sevenDash);
  public static List<Card> disjoint1A4B = List.of(oneBlue, oneDrop, twoHex, threeBolt, fourDash);
  public static List<Card> disjoint1B1A3B = List.of(oneBolt, fourBlue, fiveDash, sixDrop, sevenBolt);
  public static List<Card> disjoint2B1A2B = List.of(twoDrop, threeBlue, threeDash, fourHex, fiveDrop);
  public static List<Card> disjoint3B1A1B = List.of(twoHex, threeDrop, fourDash, sixBlue, sevenDash);
  public static List<Card> disjoint4B1A = List.of(fiveHex, fiveDrop, fiveDash, sixBolt, sevenBlue); // 10
  public static List<Card> disjoint1A4R = List.of(twoBlue, threeHeart, fourCross, sixStar, sevenStar);
  public static List<Card> disjoint1R1A3R = List.of(oneStar, threeBlue, fourStar, fourHeart, fiveCross);
  public static List<Card> disjoint2R1A2R = List.of(fourBlue, fourCross, fourHeart, fourStar, fiveHeart);
  public static List<Card> disjoint3R1A1R = List.of(twoCross, threeCross, fourStar, fiveBlue, sevenHeart);
  public static List<Card> disjoint4R1A = List.of(oneCross, twoHeart, threeCross, fourHeart, sixBlue);
  public static List<Card> disjoint1B4A = List.of(twoDash, fourBlue, fiveBlue, sixBlue, sevenBlue);
  public static List<Card> disjoint4A1B = List.of(oneBlue, threeBlue, fourBlue, fiveBlue, sixDash);
  public static List<Card> disjoint1R4A = List.of(oneHeart, twoBlue, fourBlue, fiveBlue, sevenBlue);
  public static List<Card> disjoint4A1R = List.of(threeBlue, fourBlue, fiveBlue, sixBlue, sixStar);
  public static List<Card> disjoint5A = List.of(twoBlue, threeBlue, fourBlue, sixBlue, sevenBlue);
  // not consecutive, correct color ratio, incorrect color order
  public static List<Card> disjoint2R1B2R = List.of(oneHeart, twoCross, fourDash, sixStar, sixCross);
  public static List<Card> disjoint3B1R1B = List.of(twoDash, threeDrop, fourHex, fourHeart, fiveDrop);
  public static List<Card> disjoint3A1B1A = List.of(oneBlue, twoBlue, threeBlue, fourDash, fourBlue);
  public static List<Card> disjoint1A1R3A = List.of(twoBlue, threeStar, fourBlue, sixBlue, sevenBlue);
  public static List<Card> disjoint1R1B3R = List.of(threeStar, fourBolt, fiveStar, sevenStar, sevenHeart);
  public static List<Card> disjoint2B1R2B = List.of(oneHex, twoDrop, fiveHeart, sixHex, sevenDash);
  // not consecutive, incorrect color ratio
  public static List<Card> disjoint1B1R2B1A = List.of(oneHex, threeHeart, twoDrop, fourBolt, fiveBlue);
  public static List<Card> disjoint3A2R = List.of(oneBlue, threeBlue, fourBlue, fiveCross, sixHeart);
  public static List<Card> disjoint1B1R1A1R1B = List.of(threeBolt, threeCross, fourBlue, fiveHeart, sevenDrop);
  public static List<Card> disjoint5B = List.of(twoHex, fourBolt, fiveBolt, sixHex, sevenBolt);
  public static List<Card> disjoint5R = List.of(threeStar, threeHeart, fourStar, sevenCross, sevenStar);
  public static List<Card> disjoint1R1B1A2B = List.of(twoHeart, threeDrop, fiveBlue, sixDash, sevenBolt);
  // consecutive, correct color ratio, incorrect color order
  public static List<Card> joint2B1R2B = List.of(oneDash, twoHex, threeHeart, fourDash, fiveDash);
  public static List<Card> joint1B1R3B = List.of(twoBolt, threeCross, fourHex, fiveDash, sixDrop);
  public static List<Card> joint3R1B1R = List.of(threeHeart, fourStar, fiveStar, sixHex, sevenCross);
  public static List<Card> joint2R1B2R = List.of(oneStar, twoStar, threeHex, fourHeart, fiveHeart);
  public static List<Card> joint2A1B2A = List.of(twoBlue, threeBlue, fourDash, fiveBlue, sixBlue);
  public static List<Card> joint1A1R3A = List.of(threeBlue, fourCross, fiveBlue, sixBlue, sevenBlue);
  // consecutive, incorrect color ratio
  public static List<Card> joint5B = List.of(threeDash, fourHex, fiveDash, sixDash, sevenBolt);
  public static List<Card> joint5R = List.of(twoHeart, threeStar, fourHeart, fiveCross, sixStar);
  public static List<Card> joint3B2A = List.of(oneHex, twoDash, threeBolt, fourHeart, fiveHeart);
  public static List<Card> joint1A3B1R = List.of(threeBlue, fourHex, fiveDrop, sixDash, sevenHeart);
  public static List<Card> joint2R3B = List.of(twoCross, threeStar, fourBolt, fiveDash, sixHex);
  public static List<Card> joint1B1R3A = List.of(oneDash, twoStar, threeBlue, fourBlue, fiveBlue);
  // LIST:
  public static List<List<Card>> notBluesHands = List.of(disjoint1R4B, disjoint4B1R, disjoint1B4R,
          disjoint4R1B, disjoint1A4B, disjoint1B1A3B, disjoint2B1A2B, disjoint3B1A1B, disjoint4B1A,
          disjoint1A4R, disjoint1R1A3R, disjoint2R1A2R, disjoint3R1A1R, disjoint4R1A, disjoint1B4A,
          disjoint4A1B, disjoint1R4A, disjoint4A1R, disjoint5A, disjoint2R1B2R, disjoint3B1R1B,
          disjoint3A1B1A, disjoint1A1R3A, disjoint1R1B3R, disjoint2B1R2B, disjoint1B1R2B1A,
          disjoint3A2R, disjoint1B1R1A1R1B, disjoint5B, disjoint5R, disjoint1R1B1A2B, joint2B1R2B,
          joint1B1R3B, joint3R1B1R, joint2R1B2R, joint2A1B2A, joint1A1R3A, joint5B, joint5R,
          joint3B2A, joint1A3B1R, joint2R3B, joint1B1R3A);

  public static List<Card> hand1 = List.of(twoHex, twoDash, threeCross, fourBlue, fourHex);
  public static List<Card> hand2 = List.of(oneBolt, twoBlue, fiveStar, sevenBlue, sevenStar);
  public static List<Card> hand3 = List.of(oneDrop, threeStar, fourHeart, sixHeart, sevenDash);
  public static List<Card> hand4 = List.of(twoBolt, fiveBolt, sixDrop, threeDash, fourBlue);
  public static List<Card> hand5 = List.of(twoDrop, sixCross, sevenDrop, sevenCross, sevenHeart);
  public static List<Card> hand6 = List.of(oneDash, oneHex, oneBolt, sixBolt, sevenBlue);
  public static List<Card> hand7 = List.of(twoBlue, threeCross, fourDash, fiveHex, sixHeart);
  public static List<Card> hand8 = List.of(oneHex, threeBlue, sixHex, sevenBolt, sevenStar);
  public static List<Card> hand9 = List.of(fourDash, fiveDash, fiveCross, sevenHeart, sevenStar);
  public static List<Card> hand10 = List.of(oneBlue, threeBlue, fourDash, fourBlue, fiveCross);
  public static List<Card> hand11 = List.of(oneDrop, oneBolt, twoBolt, threeBolt, threeCross);

  //********************************************************* SETUP FOR THE 9 POSSIBLE ROUND ENDINGS
  // CASE 1: Displayed player calls Blues
  public static void setupCase1(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases12(p1, p2, p3, p4);
    ((TstPlayer) p1).setWillChoose(List.of(Z.fiveDrop, Z.fourBlue, Z.fourBolt));
    ((TstPlayer) p3).setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.fourDrop));
  }

  // CASE 2: Opponent calls Blues
  public static void setupCase2(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases12(p1, p2, p3, p4);
    ((TstPlayer) p1).setWillChoose(List.of(Z.fiveDrop, Z.fourBlue));
    ((TstPlayer) p3).setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.twoDrop));
  }

  // CASE 3: Displayed player correctly calls No Blues on opponent
  public static void setupCase3(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases34568(p1, p2, p3, p4);
    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.of(p4)));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.of(p4)));
  }

  // CASE 4: Opponent correctly calls No Blues on displayed player
  public static void setupCase4(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases34568(p1, p2, p3, p4);
    ((TstPlayer) p4).setWillCall(List.of(Optional.empty(), Optional.of(p1)));
  }

  // CASE 5: Opponent correctly calls No Blues on opponent
  public static void setupCase5(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases34568(p1, p2, p3, p4);
    ((TstPlayer) p2).setWillCall(List.of(Optional.empty(), Optional.of(p4)));
  }

  // CASE 6: Displayed player incorrectly calls No Blues on opponent
  public static void setupCase6(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases34568(p1, p2, p3, p4);
    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.of(p3)));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.of(p1)));
  }

  // CASE 7: Opponent incorrectly calls No Blues on displayed player
  public static void setupCase7(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    ((TstPlayer) p1).setWillDiscard(List.of(Z.sixDash, Z.fiveBlue, Z.fourBlue, Z.fiveDrop));
    ((TstPlayer) p2).setWillDiscard(List.of(Z.threeDash, Z.fourBlue, Z.fourDrop, Z.sixBlue));
    ((TstPlayer) p3).setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue, Z.threeBlue, Z.oneDrop));
    ((TstPlayer) p4).setWillDiscard(List.of(Z.fourBlue, Z.threeHex, Z.twoDrop, Z.fourBlue));

    ((TstPlayer) p1).setWillChoose(List.of(Z.fiveDrop, Z.fourBlue, Z.twoBolt));
    ((TstPlayer) p2).setWillChoose(List.of(Z.fourBlue, Z.fiveHex, Z.twoDrop));
    ((TstPlayer) p3).setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.oneBolt));
    ((TstPlayer) p4).setWillChoose(List.of(Z.threeHex, Z.threeHex, Z.fourBlue));

    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty()));
    ((TstPlayer) p2).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.of(p1)));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.of(p1)));
    ((TstPlayer) p4).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.of(p1)));
  }

  // CASE 8: Opponent incorrectly calls No Blues on opponent
  public static void setupCase8(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    preSetupCases34568(p1, p2, p3, p4);
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.of(p2)));
  }

  // CASE 9: Deck is empty
  public static void setupCase9(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    ((TstPlayer) p1).setWillDiscard(List.of(Z.sixDash, Z.fiveBlue, Z.fourBlue, Z.fiveDrop,
            Z.threeDrop, Z.oneDrop, Z.oneBlue, Z.twoDrop, Z.fourBlue));
    ((TstPlayer) p2).setWillDiscard(List.of(Z.threeDash, Z.fourBlue, Z.fourDrop, Z.sixBlue,
            Z.fiveHex, Z.twoDrop, Z.threeDash, Z.oneBlue, Z.sevenDash));
    ((TstPlayer) p3).setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue, Z.threeBlue, Z.oneDrop,
            Z.threeDash, Z.fiveBlue, Z.oneBolt, Z.fourBlue, Z.sixCross));
    ((TstPlayer) p4).setWillDiscard(List.of(Z.fourBlue, Z.threeHex, Z.twoDrop, Z.fourBlue,
            Z.threeHex, Z.oneDash, Z.sixDrop, Z.oneBolt, Z.fiveDash));

    ((TstPlayer) p1).setWillChoose(List.of(Z.fiveDrop, Z.fourBlue, Z.twoBolt, Z.oneDrop, Z.twoCross,
            Z.twoDrop, Z.fiveHeart, Z.fourBlue, Z.sevenStar));
    ((TstPlayer) p2).setWillChoose(List.of(Z.fourBlue, Z.fiveHex, Z.twoDrop, Z.oneCross,
            Z.threeDash, Z.oneDash, Z.oneBlue, Z.oneStar, Z.sixCross));
    ((TstPlayer) p3).setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.oneBolt, Z.fourBlue,
            Z.fourCross, Z.sixCross, Z.sixDrop, Z.sevenHeart, Z.fourBlue));
    ((TstPlayer) p4).setWillChoose(List.of(Z.threeHex, Z.threeHex, Z.fourBlue, Z.sixBlue, Z.fiveHex,
            Z.fiveBlue, Z.oneBolt, Z.oneBlue, Z.fiveStar));

    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    ((TstPlayer) p2).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    ((TstPlayer) p4).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
  }

  private static void preSetupCases12(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    ((TstPlayer) p1).setWillDiscard(List.of(Z.sixDash, Z.fiveBlue, Z.fourBlue));
    ((TstPlayer) p2).setWillDiscard(List.of(Z.threeDash, Z.fourBlue, Z.fourDrop));
    ((TstPlayer) p3).setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue, Z.threeBlue));
    ((TstPlayer) p4).setWillDiscard(List.of(Z.fourBlue, Z.threeHex, Z.twoDrop));

    ((TstPlayer) p2).setWillChoose(List.of(Z.fourBlue, Z.fiveHex));
    ((TstPlayer) p4).setWillChoose(List.of(Z.threeHex, Z.threeHex));

    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    ((TstPlayer) p2).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    ((TstPlayer) p4).setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  private static void preSetupCases34568(IPlayer p1, IPlayer p2, IPlayer p3, IPlayer p4) {
    ((TstPlayer) p1).setWillDiscard(List.of(Z.sixDash, Z.fiveBlue));
    ((TstPlayer) p2).setWillDiscard(List.of(Z.threeDash, Z.fourBlue));
    ((TstPlayer) p3).setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue));
    ((TstPlayer) p4).setWillDiscard(List.of(Z.fourBlue, Z.sixDrop));

    ((TstPlayer) p1).setWillChoose(List.of(Z.fiveDrop));
    ((TstPlayer) p2).setWillChoose(List.of(Z.fourBlue));
    ((TstPlayer) p3).setWillChoose(List.of(Z.threeDash));
    ((TstPlayer) p4).setWillChoose(List.of(Z.threeHex));

    ((TstPlayer) p1).setWillCall(List.of(Optional.empty(), Optional.empty()));
    ((TstPlayer) p2).setWillCall(List.of(Optional.empty(), Optional.empty()));
    ((TstPlayer) p3).setWillCall(List.of(Optional.empty(), Optional.empty()));
    ((TstPlayer) p4).setWillCall(List.of(Optional.empty(), Optional.empty()));
  }
}
