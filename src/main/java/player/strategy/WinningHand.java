package player.strategy;

import java.util.ArrayList;
import java.util.List;

import game.deck.card.Card;
import game.deck.card.SCard;
import game.deck.card.UCard;
import utility.Utility;

/**
 * The 57 distinct winning hands, only taking into account the 'Color' and 'Rank' of each card, not
 *   the 'Suit'. Each 'WinningHand' represents multiple hands of actual cards (its 'permutations'
 *   field denotes how many).
 */
public enum WinningHand {
  // one red four black
  R1_B25(List.of(UCard.R1, UCard.B2, UCard.B3, UCard.B4, UCard.B5), 0, 768),
  R2_B36(List.of(UCard.R2, UCard.B3, UCard.B4, UCard.B5, UCard.B6), 0, 768),
  R3_B47(List.of(UCard.R3, UCard.B4, UCard.B5, UCard.B6, UCard.B7), 0, 768),
  B14_R5(List.of(UCard.B1, UCard.B2, UCard.B3, UCard.B4, UCard.R5), 0, 768),
  B25_R6(List.of(UCard.B2, UCard.B3, UCard.B4, UCard.B5, UCard.R6), 0, 768),
  B36_R7(List.of(UCard.B3, UCard.B4, UCard.B5, UCard.B6, UCard.R7), 0, 768),
  // one black four red
  B1_R25(List.of(UCard.B1, UCard.R2, UCard.R3, UCard.R4, UCard.R5), 0, 324),
  B2_R36(List.of(UCard.B2, UCard.R3, UCard.R4, UCard.R5, UCard.R6), 0, 324),
  B3_R47(List.of(UCard.B3, UCard.R4, UCard.R5, UCard.R6, UCard.R7), 0, 324),
  R14_B5(List.of(UCard.R1, UCard.R2, UCard.R3, UCard.R4, UCard.B5), 0, 324),
  R25_B6(List.of(UCard.R2, UCard.R3, UCard.R4, UCard.R5, UCard.B6), 0, 324),
  R36_B7(List.of(UCard.R3, UCard.R4, UCard.R5, UCard.R6, UCard.B7), 0, 324),
  // one blue four black starting rank one
  A1_B25(List.of(UCard.A1, UCard.B2, UCard.B3, UCard.B4, UCard.B5), -5, 256),
  B1_A2_B35(List.of(UCard.B1, UCard.A2, UCard.B3, UCard.B4, UCard.B5), -5, 256),
  B12_A3_B45(List.of(UCard.B1, UCard.B2, UCard.A3, UCard.B4, UCard.B5), -5, 256),
  B13_A4_B5(List.of(UCard.B1, UCard.B2, UCard.B3, UCard.A4, UCard.B5), -5, 256),
  B14_A5(List.of(UCard.B1, UCard.B2, UCard.B3, UCard.B4, UCard.A5), -5, 256),
  // one blue four black starting rank two
  A2_B36(List.of(UCard.A2, UCard.B3, UCard.B4, UCard.B5, UCard.B6), -5, 256),
  B2_A3_B46(List.of(UCard.B2, UCard.A3, UCard.B4, UCard.B5, UCard.B6), -5, 256),
  B23_A4_B56(List.of(UCard.B2, UCard.B3, UCard.A4, UCard.B5, UCard.B6), -5, 256),
  B24_A5_B6(List.of(UCard.B2, UCard.B3, UCard.B4, UCard.A5, UCard.B6), -5, 256),
  B25_A6(List.of(UCard.B2, UCard.B3, UCard.B4, UCard.B5, UCard.A6), -5, 256),
  // one blue four black starting rank three
  A3_B47(List.of(UCard.A3, UCard.B4, UCard.B5, UCard.B6, UCard.B7), -5, 256),
  B3_A4_B57(List.of(UCard.B3, UCard.A4, UCard.B5, UCard.B6, UCard.B7), -5, 256),
  B34_A5_B67(List.of(UCard.B3, UCard.B4, UCard.A5, UCard.B6, UCard.B7), -5, 256),
  B35_A6_B7(List.of(UCard.B3, UCard.B4, UCard.B5, UCard.A6, UCard.B7), -5, 256),
  B36_A7(List.of(UCard.B3, UCard.B4, UCard.B5, UCard.B6, UCard.A7), -5, 256),
  // one blue four red starting rank one
  A1_R25(List.of(UCard.A1, UCard.R2, UCard.R3, UCard.R4, UCard.R5), -5, 81),
  R1_A2_R35(List.of(UCard.R1, UCard.A2, UCard.R3, UCard.R4, UCard.R5), -5, 81),
  R12_A3_R45(List.of(UCard.R1, UCard.R2, UCard.A3, UCard.R4, UCard.R5), -5, 81),
  R13_A4_R5(List.of(UCard.R1, UCard.R2, UCard.R3, UCard.A4, UCard.R5), -5, 81),
  R14_A5(List.of(UCard.R1, UCard.R2, UCard.R3, UCard.R4, UCard.A5), -5, 81),
  // one blue four red starting rank two
  A2_R36(List.of(UCard.A2, UCard.R3, UCard.R4, UCard.R5, UCard.R6), -5, 81),
  R2_A3_R46(List.of(UCard.R2, UCard.A3, UCard.R4, UCard.R5, UCard.R6), -5, 81),
  R23_A4_R56(List.of(UCard.R2, UCard.R3, UCard.A4, UCard.R5, UCard.R6), -5, 81),
  R24_A5_R6(List.of(UCard.R2, UCard.R3, UCard.R4, UCard.A5, UCard.R6), -5, 81),
  R25_A6(List.of(UCard.R2, UCard.R3, UCard.R4, UCard.R5, UCard.A6), -5, 81),
  // one blue four red starting rank three
  A3_R47(List.of(UCard.A3, UCard.R4, UCard.R5, UCard.R6, UCard.R7), -5, 81),
  R3_A4_R57(List.of(UCard.R3, UCard.A4, UCard.R5, UCard.R6, UCard.R7), -5, 81),
  R34_A5_R67(List.of(UCard.R3, UCard.R4, UCard.A5, UCard.R6, UCard.R7), -5, 81),
  R35_A6_R7(List.of(UCard.R3, UCard.R4, UCard.R5, UCard.A6, UCard.R7), -5, 81),
  R36_A7(List.of(UCard.R3, UCard.R4, UCard.R5, UCard.R6, UCard.A7), -5, 81),
  // one black four blue
  B1_A25(List.of(UCard.B1, UCard.A2, UCard.A3, UCard.A4, UCard.A5), -10, 4),
  B2_A36(List.of(UCard.B2, UCard.A3, UCard.A4, UCard.A5, UCard.A6), -10, 4),
  B3_A47(List.of(UCard.B3, UCard.A4, UCard.A5, UCard.A6, UCard.A7), -10, 4),
  A14_B5(List.of(UCard.A1, UCard.A2, UCard.A3, UCard.A4, UCard.B5), -10, 4),
  A25_B6(List.of(UCard.A2, UCard.A3, UCard.A4, UCard.A5, UCard.B6), -10, 4),
  A36_B7(List.of(UCard.A3, UCard.A4, UCard.A5, UCard.A6, UCard.B7), -10, 4),
  // one red four blue
  R1_A25(List.of(UCard.R1, UCard.A2, UCard.A3, UCard.A4, UCard.A5), -10, 3),
  R2_A36(List.of(UCard.R2, UCard.A3, UCard.A4, UCard.A5, UCard.A6), -10, 3),
  R3_A47(List.of(UCard.R3, UCard.A4, UCard.A5, UCard.A6, UCard.A7), -10, 3),
  A14_R5(List.of(UCard.A1, UCard.A2, UCard.A3, UCard.A4, UCard.R5), -10, 3),
  A25_R6(List.of(UCard.A2, UCard.A3, UCard.A4, UCard.A5, UCard.R6), -10, 3),
  A36_R7(List.of(UCard.A3, UCard.A4, UCard.A5, UCard.A6, UCard.R7), -10, 3),
  // five blue
  A15(List.of(UCard.A1, UCard.A2, UCard.A3, UCard.A4, UCard.A5), -25, 1),
  A26(List.of(UCard.A2, UCard.A3, UCard.A4, UCard.A5, UCard.A6), -25, 1),
  A37(List.of(UCard.A3, UCard.A4, UCard.A5, UCard.A6, UCard.A7), -25, 1);

  /**
   * Returns a list of UCards (unsuited cards) - the player needs to obtain a Card that satisfies
   *   each UCard to get desired (i.e. makes method 'satisfiedBy' in UCard return true). An empty
   *   list means current is already desired.
   * @param desired the desired WinningHand
   * @param current the current hand
   * @return list of UCards the player needs to get desired
   */
  public static List<UCard> cardsNeededFor(WinningHand desired, List<Card> current) {
    List<Card> sorted = Utility.sortHandByRank(new ArrayList<>(current));
    List<UCard> needed = new ArrayList<>();

    for (UCard uCard : desired.unsuitedCardList) {
      boolean has = false;
      for (Card c : sorted) if (uCard.satisfiedBy(c)) has = true;
      if (!has) needed.add(uCard);
    }
    return needed;
  }

  /**
   * Returns true if adding helper to current makes current closer to desired, false otherwise.
   * @param desired the desired WinningHand
   * @param current the current hand
   * @param helper the card whose addition to current may or may not make current closer to desired
   * @return true if adding helper to current makes current closer to desired
   */
  public static boolean helps(WinningHand desired, List<Card> current, Card helper) {
    List<Card> currentWithHelper = new ArrayList<>(current);
    currentWithHelper.add(helper);
    return cardsAwayFrom(desired, currentWithHelper) < cardsAwayFrom(desired, current);
  }

  /**
   * Returns the number of cards away from WinningHand desired the current hand is.
   * @param desired desired WinningHand
   * @param current current hand
   * @return the number of cards away from desired current hand is
   */
  public static int cardsAwayFrom(WinningHand desired, List<Card> current) {
    return cardsNeededFor(desired, current).size();
  }

  /**
   * Returns a list of the cards a player with hand current should keep if they want to win with
   *   WinningHand desired.
   * @param desired desired WinningHand
   * @param current current hand
   * @return cards a player should keep to win with desired
   */
  public static ArrayList<Card> keepIfDesired(WinningHand desired, List<Card> current) {
    ArrayList<Card> keep = new ArrayList<>();
    List<Card> sorted = Utility.sortHandByRank(new ArrayList<>(current));
    Card[] has = new Card[5];

    for (Card c : sorted) {
      for (int i = 0; i < current.size(); i++) {
        if (desired.unsuitedCardList.get(i).satisfiedBy(c)) {
          if (has[i] == null) has[i] = c;
          else if (Utility.trumps(has[i], c)) has[i] = c;
        }
      }
    }

    for (int i = 0; i < 5; i++) {
      if (has[i] != null) keep.add(has[i]);
    }

    return keep;
  }

  /**
   * Returns a list of the cards a player with hand current should discard if they want to win with
   *   WinningHand desired.
   * @param desired desired WinningHand
   * @param current current hand
   * @return cards a player should discard to win with desired
   */
  public static ArrayList<Card> discardIfDesired(WinningHand desired, List<Card> current) {
    ArrayList<Card> discard = new ArrayList<>();
    List<Card> keep = keepIfDesired(desired, current);
    for (Card c : current) {
      if (!keep.contains(c)) discard.add(c);
    }
    return discard;
  }

  /**
   * Returns a list of the WinningHands current is closest to.
   * @param current current hand
   * @return list of WinningHands current is closest to
   */
  public static ArrayList<WinningHand> closestTo(List<Card> current) {
    ArrayList<WinningHand> closestTo = new ArrayList<>();
    int cardsAway = 3;

    for (WinningHand h : WinningHand.values()) {
      int away = cardsAwayFrom(h, current);
      if (away == cardsAway) closestTo.add(h);
      if (away < cardsAway) {
        cardsAway = away;
        closestTo = new ArrayList<>();
        closestTo.add(h);
      }
    }

    return closestTo;
  }

  /**
   * Returns the number of cards away current is from the WinningHand(s) current is closest to.
   * @param current current hand
   * @return number of cards away current is from WinningHand(s) current is closest to
   */
  public static int cardsAwayFromClosestTo(List<Card> current) {
    List<WinningHand> closestTo = closestTo(current);
    if (closestTo.isEmpty()) return 5;
    return cardsAwayFrom(closestTo(current).getFirst(), current);
  }

  /**
   * Returns the number of ways current could become desired. For example, if desired was 'R1_B25'
   *   and current was [2~, 2⬯, 3⬯, 4≷, 7x], this method would return 12.
   *   'R1_B25.unsuitedCardList()' is [R1, B2, B3, B4, B5], and current already satisfies three of
   *   these UCards: B2 with 2~ or 2⬯, B3 with 3⬯, and B4 with 4≷. The unsatisfied UCards are R1
   *   (which could be 1x, 1♡, or 1⭒) and B5 (which could be 5~, 5⬯, 5, or 5≷). R1 has 3
   *   corresponding cards, B5 has 4 corresponding cards, 3 * 4 = 12.
   * @param desired desired WinningHand
   * @param current current hand
   * @return number of ways current could become desired
   */
  public static int permsWOHas(WinningHand desired, List<Card> current) {
    int permsConsObtained = 1;
    for (UCard u : cardsNeededFor(desired, current)) {
      permsConsObtained *= u.cards().size();
    }
    return permsConsObtained;
  }

  /**
   * Returns the number of ways current could become desired considering cards that are gone (in
   *   another player's hand or discarded). For example, if desired was 'R1_B25', current was
   *   [2~, 2⬯, 3⬯, 4≷, 7x], and gone was [1x, 6♡, 3~, 2♡], this method would return 8.
   *   'R1_B25.unsuitedCardList()' is [R1, B2, B3, B4, B5], and current already satisfies three of
   *   these UCards: B2 with 2~ or 2⬯, B3 with 3⬯, and B4 with 4≷. The unsatisfied UCards are R1
   *   (which could be 1♡ or 1⭒, not 1x because 1x is in gone) and B5 (which could be 5~, 5⬯, 5, or
   *   5≷). R1 has 2 corresponding cards, B5 has 4 corresponding cards, 2 * 4 = 8.
   * @param desired desired winning hand
   * @param current current hand
   * @param gone cards that are gone from the round or in another player's hand
   * @return number of ways current could become desired
   */
  public static int permsWOHasAndDiscarded(WinningHand desired, List<Card> current,
                                           List<Card> gone) {
    int perms = 1;
    for (UCard u : cardsNeededFor(desired, current)) {
      List<SCard> cards = u.cards();
      int cardsLeft = cards.size();
      for (SCard c : cards) {
        if (gone.contains(new Card(c))) cardsLeft--;
      }
      perms *= cardsLeft;
    }
    return perms;
  }

  public int permutations() {
    return permutations;
  }

  public List<UCard> unsuitedCardList() {
    return unsuitedCardList;
  }

  public int points() {
    return points;
  }

  private final int permutations;
  private final List<UCard> unsuitedCardList;
  private final int points;

  WinningHand(List<UCard> unsuitedCardList, int points, int permutations) {
    this.unsuitedCardList = unsuitedCardList;
    this.points = points;
    this.permutations = permutations;
  }
}
