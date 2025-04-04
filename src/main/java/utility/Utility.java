package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.deck.card.Card;
import game.deck.card.properties.Color;
import player.IPlayer;

/**
 * Useful methods and constants whose values are integral to the rules of the game and will
 *   never change.
 */
public class Utility {

  /**
   * Winning hand values.
   */
  public static final int ORDINARY = 0;     // 1 red 4 black or 1 black 4 red
  public static final int RICH_WOMANS = -5; // 1 blue 4 black or 1 blue 4 red
  public static final int IMPERFECT = -10;  // 1 black 4 blue or 1 red 4 blue
  public static final int PERFECT = -25;    // 5 blue or correct no caller
  public static final int PUNISHMENT = 25;  // correct no receiver

  /**
   * Returns true if hand is blues, false otherwise. A winning blues hand must contain 5 cards with
   *      consecutive ranks. Four of the cards must be the same color and one must be another color
   *      (referred to as odd-colored), unless all of them are blue. If the odd-colored card is red
   *      or black, it must be at the end; if it is blue, it can be anywhere in the sequence.
   * @param hand the hand to check
   * @return true if hand is blues
   */
  public static boolean isBlues(List<Card> hand) {
    if (hand.size() != 5) {
      throw new IllegalArgumentException("Hand must contain 5 cards");
    }

    List<Card> sortedHand;
    if (consecutive(hand)) {
      sortedHand = sortHandByRank(hand);
    } else {
      return false;
    }

    int blue = 0;
    int black = 0;
    int red = 0;

    for (Card c : hand) {
      switch (c.getColor()) {
        case BLUE -> blue += 1;
        case RED -> red += 1;
        case BLACK -> black += 1;
      }
    }
    // -5 POINTS, -25 POINTS
    if ((blue == 1 && black == 4) || (blue == 1 && red == 4) || (blue == 5)) {
      return true;
    }

    Color c0Col = sortedHand.get(0).getColor();
    Color c1Col = sortedHand.get(1).getColor();
    Color c2Col = sortedHand.get(2).getColor();
    Color c3Col = sortedHand.get(3).getColor();
    Color c4Col = sortedHand.get(4).getColor();

    switch (c0Col) {
      case BLACK -> {
        switch (c1Col) {
          case BLACK -> { // 0 POINTS - if card0, 1 = black :: card2, 3 = black, card4 = red
            return c2Col.equals(Color.BLACK) && c3Col.equals(Color.BLACK)
                    && c4Col.equals(Color.RED);
          }
          case RED -> { // 0 POINTS - if card0 = black, card1 = red :: card2, 3 = red, card4 = black
            return c2Col.equals(Color.RED) && c3Col.equals(Color.RED) && c4Col.equals(Color.RED);
          }
          case BLUE -> { // -10 POINTS - if card0 = black, card1 = blue :: card2, 3, 4 = blue
            return c2Col.equals(Color.BLUE) && c3Col.equals(Color.BLUE) && c4Col.equals(Color.BLUE);
          }
        }
      }
      case RED -> {
        switch (c1Col) {
          case RED -> { // 0 POINTS - if card0, 1 = red :: card2, 3 = red, card4 = black
            return c2Col.equals(Color.RED) && c3Col.equals(Color.RED) && c4Col.equals(Color.BLACK);
          }
          case BLACK -> { // 0 POINTS - if card0 = red, card1 = black :: card2, 3, 4 = black
            return c2Col.equals(Color.BLACK) && c3Col.equals(Color.BLACK)
                    && c4Col.equals(Color.BLACK);
          }
          case BLUE -> { // -10 POINTS - if card0 = red, card1 = blue :: card2, 3, 4 = blue
            return c2Col.equals(Color.BLUE) && c3Col.equals(Color.BLUE) && c4Col.equals(Color.BLUE);
          }
        }
      }
      case BLUE -> {
        switch (c1Col) {
          case BLUE -> { // -10 POINTS - if card0, 1 = blue :: card2, 3, = blue
            return c2Col.equals(Color.BLUE) && c3Col.equals(Color.BLUE);
          }
          case BLACK, RED -> { // false (-5 point correct calls are caught earlier)
            return false;
          }
        }
      }
    }

    return false;
  }

  /**
   * Returns true if one adding any of the cards in the pond or well makes hand.isBlues() return
   *     true, false otherwise.
   *     Functionality depends on Utility.noBluesCard(). This method surrounds noBluesCard() with a
   *     try/catch block and returns
   * @param pond pond cards
   * @param well well cards (will be empty if hand's player is 3rd or 4th to choose)
   * @param hand the hand to check
   * @return true if no blues
   * @throws IllegalArgumentException if pond size isn't 4, if hand size isn't 4, or if well size
   *   isn't 4 or 0
   */
  public static boolean isNoBlues(List<Card> pond, List<Card> well, List<Card> hand) {
    throwNBParam(pond, well, hand);
    try {
      noBlues5thCard(pond, well, hand);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  /**
   * Returns the pond or well card that makes hand.isBlues() return true. If there are multiple
   *   cards that satisfy this requirement, returns the first one it comes across.
   * @param pond pond cards
   * @param well well cards (will be empty if hand's player is 3rd of 4th to choose)
   * @param hand the hand to check
   * @return the pond or well card that makes hand.isBlues() return true
   * @throws IllegalArgumentException if no pond or well card makes hand.isBlues() return true
   * @throws IllegalArgumentException if pond size isn't 4, if hand size isn't 4, or if well size
   *   isn't 4 or 0
   */
  public static Card noBlues5thCard(List<Card> pond, List<Card> well, List<Card> hand) {
    throwNBParam(pond, well, hand);
    List<Card> copyHand = new ArrayList<>(hand);
    List<Card> fifthCards = new ArrayList<>(pond);
    Collections.addAll(fifthCards, well.toArray(new Card[0]));
    for (Card c : fifthCards) {
      copyHand.add(c);
      if (isBlues(copyHand)) {
        return c;
      }
      copyHand = new ArrayList<>(hand);
    }
    throw new IllegalArgumentException("Isn't 'No Blues'");
  }

  /**
   * Returns the amount of points a player will gain by calling blues with hand. See Utility class
   *     constants for information on how many points different hands equate to.
   * @param hand the hand to count
   * @return the points a player will gain by calling blues with hand
   */
  public static int bluesPoints(List<Card> hand) {
    if (hand.size() != 5 || !isBlues(hand)) {
      throw new IllegalArgumentException("Hand isn't blues");
    }
    int blue = 0;
    for (Card c : hand) {
      if (c.getColor().equals(Color.BLUE)) {
        blue++;
      }
    }
    if (blue == 1) {
      return RICH_WOMANS;
    }
    if (blue == 4) {
      return IMPERFECT;
    }
    if (blue == 5) {
      return PERFECT;
    }
    return ORDINARY;
  }

  /**
   * Returns the amount of points in hand by adding together the point values of every suit present.
   *     See Suit enum for each suit's point value. Suit points are only counted once regardless of
   *     how many cards of that suit the hand contains. For example, hand containing 1 heart card
   *     would add 6 points to the return value, as would hand containing 2 hearts (or 3, 4, or 5).
   * @param hand the cards to count
   * @return the amount of points in this Player's hand
   */
  public static int points(List<Card> hand) {
    if (hand.size() > 5 || hand.size() < 4) {
      System.out.println(hand);
      throw new IllegalArgumentException("Hand must contain either 4 or 5 cards");
    }

    Set<Integer> handSuits = new HashSet<>();
    int points = 0;

    for (Card c : hand) {
      handSuits.add(c.getSuit().points());
    }
    for (Integer i : handSuits) {
      points += i;
    }

    return points;
  }

  /**
   * Returns true if Card a trumps Card b. A blue card of any rank trumps any red/black card. If
   *     both cards are blue or neither are blue, the lower rank trumps (Rank.ONE is the best Rank).
   *     If neither card is blue, and they're the same rank, the better suit trumps (see Suit enum
   *     for Suit order).
   * @param a Card a
   * @param b Card b
   * @return true if Card a trumps Card b
   */
  public static boolean trumps(Card a, Card b) {
    if (a.equals(b)) {
      return false;
    }
    if (a.getColor().equals(Color.BLUE) && b.getColor().equals(Color.BLUE)) {
      return a.getRank().number() < b.getRank().number();
    }
    if (a.getColor().equals((Color.BLUE))) {
      return true;
    }
    if (b.getColor().equals(Color.BLUE)) {
      return false;
    }
    if (a.getRank().number() < b.getRank().number()) {
      return true;
    }
    if (a.getRank().number() == b.getRank().number()) {
      return a.getSuit().points() < b.getSuit().points();
    }
    return false;
  }

  /**
   * Sorts cards consecutively by increasing Rank.number(). If two or more cards have the same Rank,
   *     they are sorted based on Suit. Although this method is intended to be used to sort hands,
   *     it can be used on lists of any size.
   * @param hand the hand to sort
   * @return sorted cards
   */
  public static List<Card> sortHandByRank(List<Card> hand) {
    Card[] sorted = new Card[hand.size()];
    int[] beforeByIdxArr = new int[hand.size()];
    List<Integer> beforeByIdxList;
    List<Integer> beforeByIdxSorted;

    for (int i = 0; i < hand.size(); i++) {
      int beforeC = 0;
      Card c = hand.get(i);
      for (Card d : hand) {
        if (c.getRank().number() < d.getRank().number()) {
          beforeC++;
        }
        if (c.getRank().number() == d.getRank().number()) {
          if (Utility.trumps(c, d)) {
            beforeC++;
          }
        }
      }
      beforeByIdxArr[i] = beforeC;
    }

    beforeByIdxList = new ArrayList<>(Arrays.stream(beforeByIdxArr).boxed().toList());
    beforeByIdxSorted = new ArrayList<>(beforeByIdxList);
    beforeByIdxSorted.sort(Comparator.reverseOrder());

    for (int i = 0; i < beforeByIdxList.size(); i++) {
      Integer before = beforeByIdxSorted.get(i);
      Card corresponding = hand.get(beforeByIdxList.indexOf(before));
      sorted[i] = corresponding;
    }

    return Arrays.stream(sorted).toList();
  }

  /**
   * Sorts hand from highest to lowest trump value.
   * @param hand the hand to sort
   * @return hand sorted from highest to lowest trump value
   */
  public static List<Card> sortHandByTrump(List<Card> hand) {
    List<Card> sorted = new ArrayList<>(hand);
    sorted.sort((a, b) -> {
      if (Utility.trumps(a, b)) return -1;
      else if (Utility.trumps(b, a)) return 1;
      else return 0;
    });
    return sorted;
  }

  /**
   * Returns true if cards are consecutive (1, 2, 3, 4, 5, or 2, 3, 4, 5, 6, or 3, 4, 5, 6, 7).
   * @param cards the list of cards to check
   * @return true if cards is consecutive
   */
  public static boolean consecutive(List<Card> cards) {
    ArrayList<Integer> rankList = new ArrayList<>();
    for (Card c : cards) {
      rankList.add(c.getRank().number());
    }

    Collections.sort(rankList);
    for (int i = 0; i < 4; i++) {
      if (rankList.get(i) != rankList.get(i + 1) - 1) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the order in which players may choose cards from the pond/well by sorting them
   *   according to their pond cards from best to worst. The first two players can choose from the
   *   pond or the well, the remaining players can only choose from the pond.
   * @param pondCards the players' pond cards
   * @return the order players may choose cards in
   */
  public static ArrayList<IPlayer> playerChoiceOrder(ArrayList<Card> pondCards,
                                                     ArrayList<IPlayer> players) {
    ArrayList<Integer> indices = new ArrayList<>(pondCards.size());
    ArrayList<IPlayer> sortedPlayers = new ArrayList<>();
    for (int i = 0; i < pondCards.size(); i++) {
      Card currentCard = pondCards.get(i);
      int trumpCount = 0;

      for (int j = 0; j < pondCards.size(); j++) {
        if (Utility.trumps(currentCard, pondCards.get(j))) {
          trumpCount++;
        }
      }

      indices.add(i, trumpCount);
    }

    List<Integer> sortedIndices = new ArrayList<>(indices);
    sortedIndices.sort((a, b) -> Integer.compare(indices.get(b), indices.get(a)));
    for (Integer i : sortedIndices) {
      sortedPlayers.add(players.get(i));
    }

    return sortedPlayers;
  }

  //**************************************************************************************** HELPERS
  /**
   * Throws IllegalArgumentException if pond size isn't 4, if well size isn't 4, or if hand size
   *   isn't 0 or 4. Called in methods that verify 'No Blues' call validity - valid 'No Blues' calls
   *   can only be made if the aforementioned size requirements are satisfied.
   * @param pond pond cards
   * @param well well cards
   * @param hand hand cards
   */
  private static void throwNBParam(List<Card> pond, List<Card> well, List<Card> hand) {
    if (hand.size() != 4) {
      throw new IllegalArgumentException("Hand must contain 4 cards");
    }
    if (pond.size() != 4) {
      throw new IllegalArgumentException("Pond must contain 4 cards");
    }
    if (!well.isEmpty() && well.size() != 4) {
      throw new IllegalArgumentException("Well must contain either 0 or 4 cards");
    }
  }

  public static final String gameRulesAnsi = """
              
              """;

  public static final String gameRulesHTML = """
          <html><pre>BLUES RULES<br>
              Players: 2 - 4<br>
              Game goal: accumulate the LEAST amount of points throughout the rounds<br>
              Round goal: get a 5 card sequence - 4 cards of one color, 1 card of another<br>
               - If the odd colored card is <font color='#000000'>black</font> or <font color='#ff0000'>red</font>, it must appear at the end or the beginning<br>
                 of the sequence:<br>
                 <font color='#ff0000'>1<font color='#000000'> 2 3 4 5<font color='#ff0000'>     2<font color='#000000'> 3 4 5 6<font color='#ff0000'>     3<font color='#000000'> 4 5 6 7     1 2 3 4<font color='#ff0000'> 5<font color='#000000'>     2 3 4 5<font color='#ff0000'> 6<font color='#000000'>     3 4 5 6<font color='#ff0000'> 7</font><br>
                 <font color='#000000'>1<font color='#ff0000'> 2 3 4 5<font color='#000000'>     2<font color='#ff0000'> 3 4 5 6<font color='#000000'>     3<font color='#ff0000'> 4 5 6 7     1 2 3 4<font color='#000000'> 5<font color='#ff0000'>     2 3 4 5<font color='#000000'> 6<font color='#ff0000'>     3 4 5 6<font color='#000000'> 7</font><br>
                 ^ These common hands are worth 0 points<br>
                 <font color='#000000'>1<font color='#00BFFF'> 2 3 4 5<font color='#000000'>     2<font color='#00BFFF'> 3 4 5 6<font color='#000000'>     3<font color='#00BFFF'> 4 5 6 7     1 2 3 4 <font color='#000000'>5<font color='#00BFFF'>     2 3 4 5 <font color='#000000'>6<font color='#00BFFF'>     3 4 5 6 <font color='#000000'>7</font><br>
                 <font color='#ff0000'>1<font color='#00BFFF'> 2 3 4 5<font color='#ff0000'>     2<font color='#00BFFF'> 3 4 5 6<font color='#ff0000'>     3 <font color='#00BFFF'>4 5 6 7     1 2 3 4<font color='#ff0000'> 5<font color='#00BFFF'>     2 3 4 5<font color='#ff0000'> 6<font color='#00BFFF'>     3 4 5 6<font color='#ff0000'> 7</font><br>
                 ^ These rare hands are worth -10 points<br>
               - If the odd colored card is <font color='#00BFFF'>blue</font>, it may appear anywhere in the sequence:<br>
                 <font color='#00BFFF'>1<font color='#000000'> 2 3 4 5     1<font color='#00BFFF'> 2<font color='#000000'> 3 4 5     1 2 <font color='#00BFFF'>3<font color='#000000'> 4 5     1 2 3 <font color='#00BFFF'>4<font color='#000000'> 5     1 2 3 4 <font color='#00BFFF'>5</font><br>
                 <font color='#00BFFF'>2<font color='#000000'> 3 4 5 6     2 <font color='#00BFFF'>3<font color='#000000'> 4 5 6     2 3 <font color='#00BFFF'>4<font color='#000000'> 5 6     2 3 4 <font color='#00BFFF'>5<font color='#000000'> 6     2 3 4 5 <font color='#00BFFF'>6</font><br>
                 <font color='#00BFFF'>3<font color='#000000'> 4 5 6 7     3 <font color='#00BFFF'>4<font color='#000000'> 5 6 7     3 4 <font color='#00BFFF'>5<font color='#000000'> 6 7     3 4 5 <font color='#00BFFF'>6<font color='#000000'> 7     3 4 5 6 <font color='#00BFFF'>7</font><br>
                 <font color='#00BFFF'>1<font color='#ff0000'> 2 3 4 5     1 <font color='#00BFFF'>2<font color='#ff0000'> 3 4 5     1 2 <font color='#00BFFF'>3<font color='#ff0000'> 4 5     1 2 3 <font color='#00BFFF'>4<font color='#ff0000'> 5     1 2 3 4 <font color='#00BFFF'>5</font><br>
                 <font color='#00BFFF'>2<font color='#ff0000'> 3 4 5 6     2 <font color='#00BFFF'>3<font color='#ff0000'> 4 5 6     2 3 <font color='#00BFFF'>4<font color='#ff0000'> 5 6     2 3 4 <font color='#00BFFF'>5<font color='#ff0000'> 6     2 3 4 5 <font color='#00BFFF'>6</font><br>
                 <font color='#00BFFF'>3<font color='#ff0000'> 4 5 6 7     3 <font color='#00BFFF'>4<font color='#ff0000'> 5 6 7     3 4 <font color='#00BFFF'>5<font color='#ff0000'> 6 7     3 4 5 <font color='#00BFFF'>6<font color='#ff0000'> 7     3 4 5 6 <font color='#00BFFF'>7</font><br>
                 ^ These common hands are worth -5 points<br>
               - The ONLY exception to the '4 cards of one color, 1 card of another' rule is a<br>
                 sequence of five <font color='#00BFFF'>blue</font> cards:<br>
                 <font color='#00BFFF'>1 2 3 4 5     2 3 4 5 6     3 4 5 6 7</font><br>
                 ^ These extremely rare hands are worth -25 points<br>
          <br>
              Cards: a 56 card deck with 8 suits - each suit has numbers 1 through 7<br>
               - The eight suits are split up into three colors: <font color='#00BFFF'>blue</font>, <font color='#000000'>black</font>, and <font color='#ff0000'>red</font>. Each suit<br>
                 also has a point value - you want the LEAST amount of points.<br>
                 <font color='#00BFFF'>Blue</font> suit<br>
                  - <font color='#00BFFF'>Blue</font> (<font color='#00BFFF'>♦</font>) = 0 points<br>
                 <font color='#000000'>Black</font> suits<br>
                  - <font color='#000000'>Dash</font> (<font color='#000000'>~</font>) = 1 point<br>
                  - <font color='#000000'>Drop</font> (<font color='#000000'>⬯</font>) = 2 points<br>
                  - <font color='#000000'>Hex</font> (<font color='#000000'>⬡</font>) = 3 points<br>
                  - <font color='#000000'>Bolt</font> (<font color='#000000'>≷</font>) = 4 points<br>
                 <font color='#ff0000'>Red</font> suits<br>
                  - <font color='#ff0000'>Cross</font> (<font color='#ff0000'>x</font>) = 5 points<br>
                  - <font color='#ff0000'>Heart</font> (<font color='#ff0000'>♡</font>) = 6 points<br>
                  - <font color='#ff0000'>Star</font> (<font color='#ff0000'>⭒</font>) = 7 points<br>
               - Which cards "trump"?<br>
                  - The most important factor is rank - i.e., the number on the card. A <font color='#ff0000'>red</font>/<font color='#000000'>black</font><br>
                    card 'card1' trumps another <font color='#ff0000'>red</font>/<font color='#000000'>black</font> card 'card2' if card1 has a lower rank<br>
                    than card2. For example:<br>
                     - <font color='#ff0000'>1⭒</font> trumps <font color='#000000'>2~</font> even though <font color='#ff0000'>star</font> (<font color='#ff0000'>⭒</font>) has a higher point value than <font color='#000000'>dash</font> (<font color='#000000'>~</font>)<br>
                       because <font color='#ff0000'>1⭒</font>'s rank (1) is lower than <font color='#000000'>2~</font>'s rank (2)<br>
                  - Suit comes into play if <font color='#ff0000'>red</font>/<font color='#000000'>black</font> card 'card1' has the same rank as <font color='#ff0000'>red</font>/<font color='#000000'>black</font><br>
                    card 'card2'. For example:<br>
                     - <font color='#000000'>4≷</font> trumps <font color='#ff0000'>4x</font> because they have the same rank (4) and suit <font color='#000000'>bolt</font> (<font color='#000000'>≷</font>) has a<br>
                       lower point value than suit <font color='#ff0000'>cross</font> (<font color='#ff0000'>x</font>)<br>
                  - However, a <font color='#00BFFF'>blue</font> card 'card1' of any rank will always trump a <font color='#ff0000'>red</font>/<font color='#000000'>black</font> 'card2'<br>
                    of any rank because <font color='#00BFFF'>blue</font> is the trump suit. For example:<br>
                     - <font color='#00BFFF'>7♦</font> trumps <font color='#000000'>1~</font> even though <font color='#00BFFF'>7♦</font> has a higher rank (7) than <font color='#000000'>1~</font> (1) because <font color='#00BFFF'>blue</font> is<br>
                       the trump suit<br>
                  - A <font color='#00BFFF'>blue</font> card 'card1' trumps another <font color='#00BFFF'>blue</font> card 'card2' if card1 has a lower rank<br>
                    than card 2. For example:<br>
                     - <font color='#00BFFF'>3♦</font> trumps <font color='#00BFFF'>6♦</font> because both cards are<font color='#00BFFF'> blue</font> and <font color='#00BFFF'>3♦</font> has a lower rank (3) than <font color='#00BFFF'>6♦</font><br>
                       (6)<br>
          
              Gameplay:<br>
              1. Each player is dealt 5 cards<br>
              2. 4 cards taken from the deck are placed face-up in the middle - called "the well"<br>
              3. Each player puts a card face-down in front of them - called "the pond"<br>
              4. Each player has the opportunity to call 'No Blues' on an opponent (explained below)<br>
              5. Pond cards are flipped over<br>
              6. The player that threw the best card into the pond (first trump) chooses a card from<br>
                 the pond OR the well - if they now have a winning hand (see "Round goal" section),<br>
                 the round ends (explained below in "Round endings" section)<br>
              7. The player that threw the second best card into the pond (second trump) chooses a<br>
                 card from the pond OR the well - if they now have a winning hand, the round ends<br>
              8. The well cards are removed; if there are only two players, the pond cards are<br>
                 removed and steps 9 and 10 are skipped<br>
              9. Third trump chooses a card from the pond - if they now have a winning hand, the<br>
                 round ends; if there are only three players, pond cards are removed and step 10 is<br>
                 skipped<br>
              10.Fourth trump chooses a card from the pond - if they now have a winning hand, the<br>
                 round ends; else, pond cards are removed<br>
              11.Steps 2 - 10 are repeated until: a player "calls Blues" (gets a winning hand),<br>
                 "calls No Blues", or the deck has less than 4 cards and a full well cannot be dealt<br>
                 for the next turn (these endings are explained below in "Round endings")<br>
              12.Points are tallied and added to the score sheet (explained below in "Points")<br>
              13.Steps 1 - 12 are repeated until the game is over - i.e. when players' cumulative<br>
                 points ("game points") reach a certain threshold (explained below in "Game ending")<br>
          <br>
              Round endings:<br>
              - 'Blues' calls<br>
                  - A 'Blues' call asserts that the caller has obtained a winning hand; the most<br>
                    common round ending by far<br>
                  - 'Blues' calls are not explicitly made in the digital version of this game - the<br>
                    program will detect if a player has a winning hand and end the game<br>
               - 'No Blues' calls<br>
                  - During each turn in a round, after the pond is placed but before it is flipped,<br>
                    all players may call 'No Blues' on an opponent or pass<br>
                     - If multiple players call 'No Blues' the same turn, the player with the best<br>
                       card (including their pond card) may go through with their call<br>
                  - A 'No Blues' call accuses the receiver of being able to call 'Blues' after<br>
                    choosing a card during the current turn<br>
                  - If a 'No Blues' call is made, the round ends, the pond is flipped, and the<br>
                    receiver reveals their cards<br>
                     - If they threw first or second trump, well and pond cards are considered, if<br>
                       not, only pond cards are considered<br>
                     - If adding any one of the considered cards to the receiver's hand gives them<br>
                       a winning hand, the call is true; otherwise, the call is false<br>
               - Empty deck<br>
                  - If the deck has less than 4 cards after a turn has finished the game ends<br>
                    because there aren't enough cards to deal a full well<br>
          <br>
              Points:<br>
               - To tally "hand points", the point value of each suit that appears in the hand is<br>
                 added once, regardless of how many times the suit appears in the hand. For example:<br>
                     - <font color='#000000'>1~ 2~ 5~ 6~<font color='#ff0000'> 6♡</font> = <font color='#000000'>~</font> (1 point) + <font color='#ff0000'>♡</font> (6 points)                 = 7 points<br>
                     - <font color='#ff0000'>1♡ 2♡ 5♡ 6♡ <font color='#000000'>6~</font> = <font color='#ff0000'>♡</font> (6 points) + <font color='#000000'>~</font> (1 point)                 = 7 points<br>
                     - <font color='#000000'>6~ 6⬯ 7~ 7⬯ 7≷</font> = <font color='#000000'>~</font> (1 point) + <font color='#000000'>⬯</font> (2 points) + <font color='#000000'>≷</font> (4 points)  = 7 points<br>
                     - <font color='#ff0000'>1⭒<font color='#00BFFF'> 2♦ <font color='#ff0000'>3⭒ 4⭒ 6⭒</font> = <font color='#ff0000'>⭒</font> (7 points) + <font color='#00BFFF'>♦</font> (0 points)               = 7 points<br>
                     - The most points a single hand can contain is 25: <font color='#000000'>⬡</font> (3 points), <font color='#000000'>≷</font> (4 points),<br>
                       <font color='#ff0000'>x</font> (5 points), <font color='#ff0000'>♡</font> (6 points), <font color='#ff0000'>⭒</font> (7 points), the least is 0: all <font color='#00BFFF'>♦</font> (0 points)<br>
               - If a round ends with a 'Blues' call, the caller gains the points associated with<br>
                 their winning hand (see "Round goal" section above), all other players gain their<br>
                 hand points<br>
               - If a round ends with a true 'No Blues' call, the caller gains -25 points, the<br>
                 receiver gains 25 points, all other players gain their hand points<br>
               - If a round ends with a false 'No Blues' call, the caller gains 25 points, all other<br>
                 players gain 0 points<br>
               - If the round ends because of an empty deck, all players gain their hand points<br>
          <br>
              Game ending:<br>
               - The game ends when players' cumulative points ("game points") reach a certain<br>
                 threshold<br>
                  - Usually this threshold equals 25 times the number of players (50 for 2 players,<br>
                    75 for 3 players, 100 for 4 players)<br>
                  - After every round, each player's points (see "Points" section above) are added<br>
                    to their game total - all players' game totals are added to calculate the game<br>
                    points<br>
               - The winner is whichever player has the least total points - ties for least total<br>
                 points go to the player who most recently won a round<br>
          """;
}