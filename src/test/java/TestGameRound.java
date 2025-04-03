import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.IGame;
import game.StandardGame;
import game.deck.TstDeck;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.rends.NBCall;
import game.rends.REnd;
import game.rends.REndState;
import game.score.RScore;
import player.IPlayer;
import player.TstPlayer;
import utility.Utility;

/**
 * Tests rounds of 4 player StandardGames.
 */
public class TestGameRound {
  IPlayer player1;
  IPlayer player2;
  IPlayer player3;
  IPlayer player4;
  List<IPlayer> fourPlayers;
  IGame game;
  String emptySSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";;
  String postBluesSSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+3     3  |+3     3  |-5    -5  |+3     3  ||+4     4  |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";
  String postNoSSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+3     3  |+3     3  |+25    25 |-25   -25 ||+6     6  |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";
  String postNoTiedSSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+3     3  |-25   -25 |+25    25 |+3     3  ||+6     6  |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";;
  String postFalseNoSSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+0     0  |+0     0  |+0     0  |+25    25 ||+25    25 |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";
  String postDeckEmptySSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+1     1  |+3     3  |+5     5  |+3     3  ||+12    12 |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";
  String postDeckEmptyTiedSSTS = """
          _________________________________________________________________
          |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+6     6  |+3     3  |+3     3  |+3     3  ||+15    15 |
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";

  // STARTING SITUATION
  // Player 1: oneBlue, fiveBlue, twoDash, sixDash, threeDrop
  // Player 2: twoBlue, sixBlue, threeDash, sevenDash, fourDrop
  // Player 3: threeBlue, sevenBlue, fourDash, oneDrop, fiveDrop
  // Player 4: fourBlue, oneDash, fiveDash, twoDrop, sixDrop
  // Well: sevenDrop, oneHex, twoHex, threeHex

  // NORMAL TURN SCENARIO
  // Player 1 discards twoDash: oneBlue, fiveBlue, sixDash, threeDrop
  // Player 2 discards fourDrop: twoBlue, sixBlue, threeDash, sevenDash
  // Player 3 discards fourDash: threeBlue, sevenBlue, oneDrop, fiveDrop
  // Player 4 discards twoDrop: fourBlue, oneDash, fiveDash, sixDrop
  // Pond: twoDash, fourDrop, fourDash, twoDrop
  // Player 1 chooses oneHex from well: oneBlue, fiveBlue, sixDash, threeDrop, oneHex
  // Player 4 chooses fourDash from pond: fourBlue, oneDash, fiveDash, sixDrop, fourDash
  // Player 2 chooses twoDash from pond: twoBlue, sixBlue, threeDash, sevenDash, twoDash
  // Player 3 chooses fourDrop from pond: threeBlue, sevenBlue, oneDrop, fiveDrop, fourDrop
  @Test
  public void testNormalTurn() {
    initTests();
    game.startRound();

    Assert.assertEquals(player1.getHand(), new ArrayList<>(List.of(Z.oneBlue, Z.twoDash,
            Z.threeDrop, Z.fiveBlue, Z.sixDash)));
    Assert.assertEquals(player2.getHand(), new ArrayList<>(List.of(Z.twoBlue, Z.threeDash,
            Z.fourDrop, Z.sixBlue, Z.sevenDash)));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(List.of(Z.oneDrop, Z.threeBlue,
            Z.fourDash, Z.fiveDrop, Z.sevenBlue)));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(List.of(Z.oneDash, Z.twoDrop, Z.fourBlue,
            Z.fiveDash, Z.sixDrop)));
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 1);
    Assert.assertThrows(IllegalStateException.class, () -> game.collectPond());
    Assert.assertThrows(IllegalStateException.class, () -> game.collectNBCs());
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());

    game.flipWell();
    Assert.assertThrows(IllegalStateException.class, () -> game.flipWell());
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());
    Assert.assertThrows(IllegalStateException.class, () -> game.collectNBCs());
    Assert.assertEquals(game.getWell(), List.of(Z.sevenDrop, Z.oneHex, Z.twoHex, Z.threeHex));
    for (Card c : game.getWell()) {
      Assert.assertEquals(c.getPosition(), Position.WELL);
    }

    ((TstPlayer) player1).setDiscard(Z.twoDash);
    ((TstPlayer) player2).setDiscard(Z.fourDrop);
    ((TstPlayer) player3).setDiscard(Z.fourDash);
    ((TstPlayer) player4).setDiscard(Z.twoDrop);
    game.collectPond();
    Assert.assertEquals(game.getPond(), List.of(Z.twoDash, Z.fourDrop, Z.fourDash, Z.twoDrop));
    Assert.assertThrows(IllegalStateException.class, () -> game.collectPond());
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());
    for (IPlayer p : game.getPlayers()) {
      Assert.assertEquals(p.getHand().size(), 4);
      Assert.assertTrue(p.getPondCard().isPresent());
    }
    for (Card c : game.getPond()) {
      Assert.assertEquals(c.getPosition(), Position.POND_H);
      int i = 0;
      for (IPlayer p : game.getPlayers()) {
        if (p.getPondCard().isPresent()) {
          if (p.getPondCard().get().equals(c)) {
            i++;
          }
        }
      }
      Assert.assertEquals(i, 1);
    }

    for (IPlayer p : game.getPlayers()) {
      ((TstPlayer) p).setCallNo(Optional.empty());
    }
    Assert.assertEquals(game.collectNBCs(), Optional.empty());
    Assert.assertFalse(game.gameOver());
    Assert.assertFalse(game.roundOver());
    Assert.assertEquals(game.getRendState(), Optional.empty());

    game.flipPond();
    for (Card c : game.getPond()) {
      Assert.assertEquals(c.getPosition(), Position.POND_F);
    }
    List<Card> pond = game.getPond();

    ((TstPlayer) player1).setChooseCard(Z.oneHex);
    ((TstPlayer) player4).setChooseCard(Z.fourDash);
    ((TstPlayer) player2).setChooseCard(Z.twoDash);
    ((TstPlayer) player3).setChooseCard(Z.fourDrop);
    // order is player1, player4, player2, player3
    game.allowChoices();
    Assert.assertTrue(game.getPond().isEmpty());
    Assert.assertTrue(game.getWell().isEmpty());
    for (IPlayer p : game.getPlayers()) {
      Assert.assertTrue(p.getPondCard().isEmpty());
    }
    Assert.assertEquals(player1.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.oneBlue,
            Z.fiveBlue, Z.sixDash, Z.threeDrop, Z.oneHex))));
    Assert.assertEquals(player2.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.twoBlue,
            Z.sixBlue, Z.threeDash, Z.sevenDash, Z.twoDash))));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.threeBlue,
            Z.sevenBlue, Z.oneDrop, Z.fiveDrop, Z.fourDrop))));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.fourBlue,
            Z.oneDash, Z.fiveDash, Z.sixDrop, Z.fourDash))));
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 1);
    Assert.assertEquals(game.getScoreSheet().toString(), emptySSTS);

    int hand = 0;
    int discarded = 0;
    int other = 0;
    for (Card c : game.getDeck().getDealt()) {
      if (c.getPosition() == Position.HAND) {
        hand++;
      } else if (c.getPosition() == Position.DISCARDED) {
        discarded++;
      } else {
        other++;
      }
    }
    Assert.assertEquals(hand, 20);
    Assert.assertEquals(discarded, 4);
    Assert.assertEquals(other, 0);
  }

  // REnd.BLUES SCENARIO
  // Player 3 chooses twoDash from pond -> wins with -5 points
  // Player 1 gains 3 points
  // Player 2 gains 3 points
  // Player 4 gains 3 points
  @Test
  public void testRendBlues() {
    initPlayerRends();
    ((TstPlayer) player4).setCallNo(Optional.empty());
    game.collectNBCs();
    game.flipPond();
    Assert.assertEquals(game.getRendState(), Optional.empty());
    game.allowChoices();

    Assert.assertEquals(game.getRendState(), Optional.of(new REndState(REnd.BLUES, player3)));
    Assert.assertFalse(game.gameOver());
    Assert.assertTrue(game.roundOver());
    Assert.assertFalse(game.getWell().isEmpty()); // game doesn't clear well until next round starts
    Assert.assertFalse(game.getPond().isEmpty()); // game doesn't clear pond until next round starts
    Assert.assertEquals(game.gamePoints(), 4);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 2);
    Assert.assertEquals(game.getScoreSheet().toString(), postBluesSSTS);
    int[] nums = new int[] {3, 3, -5, 3, 4};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.BLUES, player3), fourPlayers.toArray(new IPlayer[0])));

    for (IPlayer p : game.getPlayers()) {
      Assert.assertTrue(p.getPondCard().isEmpty());
      for (Card c : p.getHand()) {
        Assert.assertEquals(c.getPosition(), Position.HAND);
      }
    }

    Assert.assertEquals(player1.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.oneBlue,
            Z.fiveBlue, Z.sixDash, Z.threeDrop, Z.twoDash))));
    Assert.assertEquals(player2.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.twoBlue,
            Z.sixBlue, Z.threeDash, Z.sevenDash, Z.fourDrop))));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.threeBlue,
            Z.fourDash, Z.oneDrop, Z.fiveDrop, Z.twoDrop))));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.fourBlue,
            Z.oneDash, Z.fiveDash, Z.sixDrop))));

    int hand = 0;
    int pond = 0;
    int well = 0;
    int other = 0;
    for (Card c : game.getDeck().getDealt()) {
      if (c.getPosition() == Position.HAND) {
        hand++;
      } else if (c.getPosition() == Position.POND_F) {
        pond++;
      } else if (c.getPosition() == Position.WELL) {
        well++;
      } else {
        other++;
      }
    }
    Assert.assertEquals(hand, 19);
    Assert.assertEquals(well, 4);
    Assert.assertEquals(pond, 1);
    Assert.assertEquals(other, 0);
  }

  // REnd.NO SCENARIO
  // Player 4 calls 'No Blues' on Player 3 -> wins with -25 points
  // Player 1 gains 3 points
  // Player 2 gains 3 points
  // Player 3 gains 25 points
  @Test
  public void testRendNo() {
    initPlayerRends();
    ((TstPlayer) player4).setCallNo(Optional.of(player3));
    Assert.assertFalse(game.getRendState().isPresent());
    Assert.assertTrue(game.collectNBCs().isPresent());

    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.TRUE_NO, new NBCall(player4, player3), player4)));
    Assert.assertFalse(game.gameOver());
    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.getWell().size(), 4);
    // game returns pond card to call Utility.points() and then removes it from hand
    Assert.assertEquals(game.getPond().size(), 4);
    Assert.assertEquals(game.gamePoints(), 6);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 2);
    Assert.assertEquals(game.getScoreSheet().toString(), postNoSSTS);
    int[] nums = new int[] {3, 3, 25, -25, 6};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.TRUE_NO, new NBCall(player4, player3), player4),
            game.getPlayers().toArray(new IPlayer[0])));

    Assert.assertTrue(player1.getPondCard().isPresent());
    // game returns pond card to call Utility.points() and then removes it from hand
    Assert.assertTrue(player2.getPondCard().isPresent());
    Assert.assertTrue(player3.getPondCard().isPresent());
    Assert.assertTrue(player4.getPondCard().isPresent());

    Assert.assertEquals(player1.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.oneBlue,
            Z.fiveBlue, Z.sixDash, Z.threeDrop))));
    // game returns pond card to call Utility.points() and then removes it from hand
    Assert.assertEquals(player2.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.twoBlue,
            Z.sixBlue, Z.threeDash, Z.sevenDash))));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.threeBlue,
            Z.fourDash, Z.oneDrop, Z.fiveDrop))));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.fourBlue,
            Z.oneDash, Z.fiveDash, Z.sixDrop))));

    int hand = 0;
    int well = 0;
    int pond = 0;
    int other = 0;
    for (Card c : game.getDeck().getDealt()) {
      if (c.getPosition() == Position.HAND) {
        hand++;
      } else if (c.getPosition() == Position.POND_F) {
        pond++;
      } else if (c.getPosition() == Position.WELL) {
        well++;
      } else {
        other++;
      }
    }

    // game returns pond card to call Utility.points() and then removes it from hand
    Assert.assertEquals(hand, 16);
    Assert.assertEquals(well, 4);
    Assert.assertEquals(pond, 4);
    Assert.assertEquals(other, 0);
  }

  // REnd.NO TIED SCENARIO
  // Player 2 and Player 4 both call 'No Blues' on Player 3 -> Player 2 wins with -25 points
  // Player 1 gains 3 points
  // Player 3 gains 25 points
  // Player 4 gains 3 points
  @Test
  public void testRendNoTied() {
    initPlayerRends();
    ((TstPlayer) player4).setCallNo(Optional.of(player3));
    ((TstPlayer) player2).setCallNo(Optional.of(player3));
    Optional<NBCall> call = game.collectNBCs();

    Assert.assertTrue(game.roundOver());
    Assert.assertTrue(call.isPresent());
    Assert.assertEquals(new NBCall(player2, player3), call.get());
    Assert.assertTrue(game.getRendState().isPresent());
    Assert.assertEquals(game.getRendState().get(), new REndState(REnd.TRUE_NO, call.get(),
            call.get().caller()));

    int[] nums = new int[] {3, -25, 25, 3, 6};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.TRUE_NO, call.get(), call.get().caller()),
            game.getPlayers().toArray(new IPlayer[0])));
    Assert.assertEquals(game.getScoreSheet().toString(), postNoTiedSSTS);
  }

  // REnd.FALSE_NO SCENARIO
  // Player 4 calls 'No Blues' on Player 2 -> loses with 25 points
  // Player 1 gains 0 points
  // Player 2 gains 0 points
  // Player 3 gains 0 points
  @Test
  public void testRendFalseNo() {
    initPlayerRends();
    ((TstPlayer) player4).setCallNo(Optional.of(player2));
    Assert.assertFalse(game.getRendState().isPresent());
    Assert.assertTrue(game.collectNBCs().isPresent());

    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.FALSE_NO, new NBCall(player4, player2), player2)));
    Assert.assertFalse(game.gameOver());
    Assert.assertTrue(game.roundOver());
    Assert.assertFalse(game.getWell().isEmpty());
    Assert.assertFalse(game.getPond().isEmpty());
    Assert.assertEquals(game.gamePoints(), 25);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 2);
    Assert.assertEquals(game.getScoreSheet().toString(), postFalseNoSSTS);
    int[] nums = new int[] {0, 0, 0, 25, 25};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.FALSE_NO, new NBCall(player4, player2), player2),
            game.getPlayers().toArray(new IPlayer[0])));

    for (IPlayer p : game.getPlayers()) {
      Assert.assertTrue(p.getPondCard().isPresent());
    }
    Assert.assertEquals(player1.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.oneBlue,
            Z.fiveBlue, Z.sixDash, Z.threeDrop))));
    Assert.assertEquals(player2.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.twoBlue,
            Z.sixBlue, Z.threeDash, Z.sevenDash))));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.threeBlue,
            Z.fourDash, Z.oneDrop, Z.fiveDrop))));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.fourBlue,
            Z.oneDash, Z.fiveDash, Z.sixDrop))));

    int hand = 0;
    int well = 0;
    int pond = 0;
    int other = 0;
    for (Card c : game.getDeck().getDealt()) {
      if (c.getPosition() == Position.HAND) {
        hand++;
      } else if (c.getPosition() == Position.POND_F) {
        pond++;
      } else if (c.getPosition() == Position.WELL) {
        well++;
      } else {
        other++;
      }
    }
    Assert.assertEquals(hand, 16);
    Assert.assertEquals(well, 4);
    Assert.assertEquals(pond, 4);
    Assert.assertEquals(other, 0);
  }

  // REnd.DECK_EMPTY SCENARIO
  // Player 1 discards threeDrop: oneBlue, fiveBlue, twoDash, sixDash
  // Player 2 discards sevenDash: twoBlue, sixBlue, threeDash, fourDrop
  // Player 3 discards fourDash: threeBlue, sevenBlue, oneDrop, fiveDrop
  // Player 4 discards sixDrop: fourBlue, oneDash, fiveDash, twoDrop
  // Pond: threeDrop, sevenDash, fourDash, sixDrop
  // Player 1 chooses fourDash from pond: oneBlue, fiveBlue, twoDash, sixDash, fourDash
  // Player 3 chooses oneHex from well: threeBlue, sevenBlue, oneDrop, fiveDrop, oneHex
  // Player 4 chooses sevenDash from pond: fourBlue, oneDash, fiveDash, twoDrop, sevenDash
  // Player 2 chooses sixDrop from pond: twoBlue, sixBlue, threeDash, fourDrop, sixDrop
  // Well cannot be flipped because the deck has less than 4 cards -> Player 1 wins a 1 point gain
  // Player 2 gains 3 points
  // Player 3 gains 5 points
  // Player 4 gains 2 points
  @Test
  public void testRendDeckEmpty() {
    initDeckRends();
    ((TstPlayer) player1).setDiscard(Z.threeDrop); // first
    ((TstPlayer) player2).setDiscard(Z.sevenDash); // fourth
    ((TstPlayer) player3).setDiscard(Z.fourDash); // second
    ((TstPlayer) player4).setDiscard(Z.sixDrop); // third
    ((TstPlayer) player1).setChooseCard(Z.fourDash); // 1 point
    ((TstPlayer) player3).setChooseCard(Z.oneHex); // 5 points
    ((TstPlayer) player4).setChooseCard(Z.sevenDash); // 2 points
    ((TstPlayer) player2).setChooseCard(Z.sixDrop); // 3 points
    game.flipWell();
    game.collectPond();
    game.collectNBCs();
    game.flipPond();
    game.allowChoices();

    Assert.assertTrue(game.getRendState().isPresent());
    Assert.assertTrue(game.roundOver());
    for (IPlayer p : game.getPlayers()) {
      Assert.assertTrue(p.getPondCard().isEmpty());
      Assert.assertEquals(p.getHand().size(), 5);
    }

    Assert.assertTrue(game.getWell().isEmpty());
    Assert.assertTrue(game.getPond().isEmpty());
    Assert.assertTrue(game.getRendState().isPresent());
    Assert.assertEquals(game.getRendState().get(), new REndState(REnd.DECK_EMPTY, player1));
    Assert.assertEquals(game.gamePoints(), 12);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 2);
    Assert.assertEquals(game.getScoreSheet().toString(), postDeckEmptySSTS);
    int[] nums = new int[] {1, 3, 5, 3, 12};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.DECK_EMPTY, player1), game.getPlayers().toArray(new IPlayer[0])));

    Assert.assertEquals(player1.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.oneBlue,
            Z.fiveBlue, Z.twoDash, Z.sixDash, Z.fourDash))));
    Assert.assertEquals(player2.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.twoBlue,
            Z.sixBlue, Z.threeDash, Z.fourDrop, Z.sixDrop))));
    Assert.assertEquals(player3.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.threeBlue,
            Z.sevenBlue, Z.oneDrop, Z.fiveDrop, Z.oneHex))));
    Assert.assertEquals(player4.getHand(), new ArrayList<>(Utility.sortHandByRank(List.of(Z.fourBlue,
            Z.oneDash, Z.fiveDash, Z.twoDrop, Z.sevenDash))));

    int hand = 0;
    int discarded = 0;
    int other = 0;
    for (Card c : game.getDeck().getDealt()) {
      if (c.getPosition() == Position.HAND) {
        hand++;
      } else if (c.getPosition() == Position.DISCARDED) {
        discarded++;
      } else {
        other++;
      }
    }
    Assert.assertEquals(hand, 20);
    Assert.assertEquals(discarded, 4);
    Assert.assertEquals(other, 0);
  }

  // REnd.DECK_EMPTY TIED SCENARIO
  // Player 1 discards twoDash: oneBlue, fiveBlue, sixDash, threeDrop
  // Player 2 discards fourDrop: twoBlue, sixBlue, threeDash, sevenDash
  // Player 3 discards sevenBlue: threeBlue, fourDash, oneDrop, fiveDrop
  // Player 4 discards twoDrop: fourBlue, oneDash, fiveDash, sixDrop
  // Pond: twoDash, fourDrop, sevenBlue, twoDrop
  // Player 3 chooses sevenDrop from the well: threeBlue, fourDash, oneDrop, fiveDrop, sevenDrop
  // Player 1 chooses oneHex from the well: oneBlue, fiveBlue, sixDash, threeDrop, oneHex
  // Player 4 chooses twoDash from the pond: fourBlue, oneDash, fiveDash, sixDrop, twoDash
  // Player 2 chooses twoDrop from the pond: twoBlue, sixBlue, threeDash, sevenDash, twoDrop
  // Well cannot be flipped because the deck has less than 4 cards -> Player 2, Player 3, and Player
  //   4 tie for the least points, Player 2 is the winner because they have the best card among
  //   those who tied (the 2♦).
  // Player 1 gains 6 points
  // Player 2 gains 3 points
  // Player 3 gains 3 points
  // Player 4 gains 3 points
  @Test
  public void testRendDeckEmptyTied() {
    initDeckRends();
    ((TstPlayer) player1).setDiscard(Z.twoDash);
    ((TstPlayer) player2).setDiscard(Z.fourDrop);
    ((TstPlayer) player3).setDiscard(Z.sevenBlue);
    ((TstPlayer) player4).setDiscard(Z.twoDrop);
    ((TstPlayer) player3).setChooseCard(Z.sevenDrop);
    ((TstPlayer) player1).setChooseCard(Z.oneHex);
    ((TstPlayer) player4).setChooseCard(Z.twoDash);
    ((TstPlayer) player2).setChooseCard(Z.twoDrop);
    game.flipWell();
    game.collectPond();
    game.collectNBCs();
    game.flipPond();
    game.allowChoices();

    Assert.assertTrue(game.roundOver());
    Assert.assertTrue(game.getRendState().isPresent());
    Assert.assertEquals(game.getRendState().get(), new REndState(REnd.DECK_EMPTY, player2));
    int[] nums = new int[] {6, 3, 3, 3, 15};
    Assert.assertEquals(game.getScoreSheet().getRound(1), new RScore(1, nums, nums,
            new REndState(REnd.DECK_EMPTY, player2), game.getPlayers().toArray(new IPlayer[0])));
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), player2);
    Assert.assertEquals(game.getScoreSheet().toString(), postDeckEmptyTiedSSTS);
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    player1 = new TstPlayer("Player 1");
    player2 = new TstPlayer("Player 2");
    player3 = new TstPlayer("Player 3");
    player4 = new TstPlayer("Player 4");
    fourPlayers = List.of(player1, player2, player3, player4);
    game = new StandardGame(fourPlayers, new TstDeck());
  }

  // PLAYER REND SCENARIOS
  // Player 1 discards twoDash: oneBlue, fiveBlue, sixDash, threeDrop
  // Player 2 discards fourDrop: twoBlue, sixBlue, threeDash, sevenDash
  // Player 3 discards sevenBlue: threeBlue, fourDash, oneDrop, fiveDrop
  // Player 4 discards twoDrop: fourBlue, oneDash, fiveDash, sixDrop
  // Pond: twoDash, fourDrop, sevenBlue, twoDrop
  // Player 1 chooses oneHex from the well: oneBlue, fiveBlue, sixDash, threeDrop, oneHex
  // Player 2 chooses twoDrop from the pond: twoBlue, sixBlue, threeDash, sevenDash, twoDrop
  // Player 3 chooses sevenDrop from the well: threeBlue, fourDash, oneDrop, fiveDrop, sevenDrop
  // Player 4 chooses twoDash from the pond: fourBlue, oneDash, fiveDash, sixDrop, twoDash
  private void initPlayerRends() {
    initTests();
    game.startRound();
    game.flipWell();
    ((TstPlayer) player1).setDiscard(Z.twoDash);
    ((TstPlayer) player2).setDiscard(Z.fourDrop);
    ((TstPlayer) player3).setDiscard(Z.sevenBlue);
    ((TstPlayer) player4).setDiscard(Z.twoDrop);
    ((TstPlayer) player1).setChooseCard(Z.oneHex);
    ((TstPlayer) player2).setChooseCard(Z.twoDash);
    ((TstPlayer) player3).setChooseCard(Z.twoDrop);
    ((TstPlayer) player4).setChooseCard(Z.sevenBlue);
    game.collectPond();

    ((TstPlayer) player1).setCallNo(Optional.empty());
    ((TstPlayer) player2).setCallNo(Optional.empty());
    ((TstPlayer) player3).setCallNo(Optional.empty());
  }

  // DECK REND SCENARIOS
  // All players pass on making No Blues Calls
  // The deck is a standard deck from 1 - 26
  private void initDeckRends() {
    initTests();
    List<Card> cs = List.of(Z.oneBlue, Z.twoBlue, Z.threeBlue, Z.fourBlue, Z.fiveBlue, Z.sixBlue,
            Z.sevenBlue, Z.oneDash, Z.twoDash, Z.threeDash, Z.fourDash, Z.fiveDash, Z.sixDash,
            Z.sevenDash, Z.oneDrop, Z.twoDrop, Z.threeDrop, Z.fourDrop, Z.fiveDrop, Z.sixDrop,
            Z.sevenDrop, Z.oneHex, Z.twoHex, Z.threeHex, Z.fourHex, Z.fiveHex);
    game = new StandardGame(List.of(player1, player2, player3, player4), new TstDeck(cs));
    ((TstPlayer) player1).setCallNo(Optional.empty());
    ((TstPlayer) player2).setCallNo(Optional.empty());
    ((TstPlayer) player3).setCallNo(Optional.empty());
    ((TstPlayer) player4).setCallNo(Optional.empty());
    game.startRound();
  }

  private ArrayList<IPlayer> playerOrder(ArrayList<Card> pondCards, ArrayList<IPlayer> players) {
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
}
