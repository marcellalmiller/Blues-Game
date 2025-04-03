import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import game.IGame;
import game.StandardGame;
import game.deck.TstDeck;
import game.rends.NBCall;
import game.rends.REnd;
import game.rends.REndState;
import player.IPlayer;
import player.TstPlayer;

/**
 * Tests full four player games (with multiple rounds).
 */
public class TestGameFull {
  IPlayer player1;
  IPlayer player2;
  IPlayer player3;
  IPlayer player4;
  List<IPlayer> players;
  IGame game;

  @Test
  public void testFullGame() {
    System.out.println("FULL GAME----------------------------------------------------------------");
    initTests();

    System.out.println("Starting Blues rend (case 1)");
    setupBluesRend();
    game.startRound();
    for (int i = 1; i < 4; i++) turn();

    System.out.println("Starting No Blues rend (case 3)");
    setupNoRend();
    game.startRound();
    for (int i = 1; i < 3; i++) turn();

    System.out.println("Starting false No Blues rend (case 6)");
    setupFalseNoRend();
    game.startRound();
    for (int i = 1; i < 3; i++) turn();

    System.out.println("Starting deck empty rend (case 9)");
    setupDeckEmptyRend();
    game.startRound();
    for (int i = 1; i < 10; i++) turn();

    System.out.println("Starting Blues rend (case 2)");
    setupBluesRend();
    ((TstPlayer) player3).setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.twoDrop));
    game.startRound();
    for (int i = 1; i < 4; i++) turn();

    Assert.assertTrue(game.roundOver());
    Assert.assertTrue(game.gameOver());
    Assert.assertEquals(game.getGameWinner(), player3);
    Assert.assertEquals(game.getScoreSheet().toString(), """
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |-5    -5  |+4     4  |+3     3  |+6     6  ||+8     8  |
            |2      |-25   -30 |+3     7  |+3     6  |+25    31 ||+6     14 |
            |3      |+25   -5  |+0     7  |+0     6  |+0     31 ||+25    39 |
            |4      |+23    18 |+13    20 |+14    20 |+10    41 ||+60    99 |
            |5      |+3     21 |+6     26 |-5     15 |+6     47 ||+10    109|
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""");
  }

  @Test
  public void testBluesRend() {
    System.out.println("BLUES ROUND--------------------------------------------------------------");
    initTests();
    setupBluesRend();
    startingLists();
    System.out.println("Starting round");
    game.startRound();

    for (int i = 1; i < 4; i++) {
      System.out.println("Starting turn " + i);
      for (IPlayer p : players) {
        System.out.println("  " + p.name() + " current hand: " + p.getHand());
      }
      turn();
    }

    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.gamePoints(), 8);
    Assert.assertEquals(player1.getPoints(), -5);
    Assert.assertEquals(player2.getPoints(), 4);
    Assert.assertEquals(player3.getPoints(), 3);
    Assert.assertEquals(player4.getPoints(), 6);

    Assert.assertEquals(game.getScoreSheet().toString(),
            """
                    _________________________________________________________________
                    |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
                    |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
                    |-------|----------|----------|----------|----------||----------|
                    |1      |-5    -5  |+4     4  |+3     3  |+6     6  ||+8     8  |
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""");
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), player1);
    Assert.assertEquals(game.getRendState(), Optional.of(new REndState(REnd.BLUES, player1)));
  }

  @Test
  public void testNoRend() {
    System.out.println("NO BLUES ROUND-----------------------------------------------------------");
    initTests();
    setupNoRend();
    startingLists();
    System.out.println("Starting round");
    game.startRound();

    for (int i = 1; i < 3; i++) {
      System.out.println("Starting turn " + i);
      for (IPlayer p : players) {
        System.out.println("  " + p.name() + " current hand: " + p.getHand());
      }
      turn();
    }

    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.gamePoints(), 6);
    Assert.assertEquals(player1.getPoints(), -25);
    Assert.assertEquals(player2.getPoints(), 3);
    Assert.assertEquals(player3.getPoints(), 3);
    Assert.assertEquals(player4.getPoints(), 25);

    Assert.assertEquals(game.getScoreSheet().toString(),
            """
                    _________________________________________________________________
                    |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
                    |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
                    |-------|----------|----------|----------|----------||----------|
                    |1      |-25   -25 |+3     3  |+3     3  |+25    25 ||+6     6  |
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""");
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), player1);
    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.TRUE_NO, new NBCall(player1, player4), player1)));
  }

  @Test
  public void testFalseNoRend() {
    System.out.println("FALSE NO BLUES ROUND-----------------------------------------------------");
    initTests();
    setupFalseNoRend();
    startingLists();
    System.out.println("Starting round");
    game.startRound();

    for (int i = 1; i < 3; i++) {
      System.out.println("Starting turn " + i);
      for (IPlayer p : players) {
        System.out.println("  " + p.name() + " current hand: " + p.getHand());
      }
      turn();
    }

    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.gamePoints(), 25);
    Assert.assertEquals(player1.getPoints(), 25);
    Assert.assertEquals(player2.getPoints(), 0);
    Assert.assertEquals(player3.getPoints(), 0);
    Assert.assertEquals(player4.getPoints(), 0);

    Assert.assertEquals(game.getScoreSheet().toString(),
            """
                    _________________________________________________________________
                    |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
                    |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
                    |-------|----------|----------|----------|----------||----------|
                    |1      |+25    25 |+0     0  |+0     0  |+0     0  ||+25    25 |
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""");
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), player3);
    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.FALSE_NO, new NBCall(player1, player3), player3)));
  }

  @Test
  public void testDeckEmptyRend() {
    System.out.println("DECK EMPTY ROUND---------------------------------------------------------");
    initTests();
    setupDeckEmptyRend();
    startingLists();
    System.out.println("Starting round");
    game.startRound();

    for (int i = 1; i < 10; i++) {
      System.out.println("Starting turn " + i);
      for (IPlayer p : players) {
        System.out.println("  " + p.name() + " current hand: " + p.getHand());
      }
      turn();
    }

    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.gamePoints(), 60);
    Assert.assertEquals(player1.getPoints(), 23);
    Assert.assertEquals(player2.getPoints(), 13);
    Assert.assertEquals(player3.getPoints(), 14);
    Assert.assertEquals(player4.getPoints(), 10);

    Assert.assertEquals(game.getScoreSheet().toString(),
            """
                    _________________________________________________________________
                    |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
                    |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
                    |-------|----------|----------|----------|----------||----------|
                    |1      |+23    23 |+13    13 |+14    14 |+10    10 ||+60    60 |
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""");
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), player4);
    Assert.assertEquals(game.getRendState(), Optional.of(new REndState(REnd.DECK_EMPTY, player4)));
  }

  //************************************************************************************* INIT/SETUP

  private void initTests() {
    player1 = new TstPlayer("Player 1");
    player2 = new TstPlayer("Player 2");
    player3 = new TstPlayer("Player 3");
    player4 = new TstPlayer("Player 4");
    players = List.of(player1, player2, player3, player4);
    game = new StandardGame(players, new TstDeck());
  }

  private void setupBluesRend() { // case 1
    TstPlayer cast1 = (TstPlayer) player1;
    TstPlayer cast2 = (TstPlayer) player2;
    TstPlayer cast3 = (TstPlayer) player3;
    TstPlayer cast4 = (TstPlayer) player4;

    cast1.setWillDiscard(List.of(Z.sixDash, Z.fiveBlue, Z.fourBlue));
    cast2.setWillDiscard(List.of(Z.threeDash, Z.fourBlue, Z.fourDrop));
    cast3.setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue, Z.threeBlue));
    cast4.setWillDiscard(List.of(Z.fourBlue, Z.threeHex, Z.twoDrop));

    cast1.setWillChoose(List.of(Z.fiveDrop, Z.fourBlue, Z.fourBolt));
    cast2.setWillChoose(List.of(Z.fourBlue, Z.fiveHex));
    cast3.setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.fourDrop));
    cast4.setWillChoose(List.of(Z.threeHex, Z.threeHex));

    cast1.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    cast2.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    cast3.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
    cast4.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  private void setupNoRend() { // case 3
    TstPlayer cast1 = (TstPlayer) player1;
    TstPlayer cast2 = (TstPlayer) player2;
    TstPlayer cast3 = (TstPlayer) player3;
    TstPlayer cast4 = (TstPlayer) player4;

    cast1.setWillDiscard(List.of(Z.sixDash, Z.fiveBlue));
    cast2.setWillDiscard(List.of(Z.threeDash, Z.fourBlue));
    cast3.setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue));
    cast4.setWillDiscard(List.of(Z.fourBlue, Z.sixDrop));

    cast1.setWillChoose(List.of(Z.fiveDrop));
    cast2.setWillChoose(List.of(Z.fourBlue));
    cast3.setWillChoose(List.of(Z.threeDash));
    cast4.setWillChoose(List.of(Z.threeHex));

    cast1.setWillCall(List.of(Optional.empty(), Optional.of(player4)));
    cast2.setWillCall(List.of(Optional.empty(), Optional.empty()));
    cast3.setWillCall(List.of(Optional.empty(), Optional.of(player4)));
    cast4.setWillCall(List.of(Optional.empty(), Optional.empty()));
  }

  private void setupFalseNoRend() { // case 6
    TstPlayer cast1 = (TstPlayer) player1;
    TstPlayer cast2 = (TstPlayer) player2;
    TstPlayer cast3 = (TstPlayer) player3;
    TstPlayer cast4 = (TstPlayer) player4;

    cast1.setWillDiscard(List.of(Z.sixDash, Z.fiveBlue));
    cast2.setWillDiscard(List.of(Z.threeDash, Z.fourBlue));
    cast3.setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue));
    cast4.setWillDiscard(List.of(Z.fourBlue, Z.sixDrop));

    cast1.setWillChoose(List.of(Z.fiveDrop));
    cast2.setWillChoose(List.of(Z.fourBlue));
    cast3.setWillChoose(List.of(Z.threeDash));
    cast4.setWillChoose(List.of(Z.threeHex));

    cast1.setWillCall(List.of(Optional.empty(), Optional.of(player3)));
    cast2.setWillCall(List.of(Optional.empty(), Optional.empty()));
    cast3.setWillCall(List.of(Optional.empty(), Optional.of(player1)));
    cast4.setWillCall(List.of(Optional.empty(), Optional.empty()));
  }

  private void setupDeckEmptyRend() { // case 9
    TstPlayer cast1 = (TstPlayer) player1;
    TstPlayer cast2 = (TstPlayer) player2;
    TstPlayer cast3 = (TstPlayer) player3;
    TstPlayer cast4 = (TstPlayer) player4;

    cast1.setWillDiscard(List.of(Z.sixDash, Z.fiveBlue, Z.fourBlue, Z.fiveDrop, Z.threeDrop,
            Z.oneDrop, Z.oneBlue, Z.twoDrop, Z.fourBlue));
    cast2.setWillDiscard(List.of(Z.threeDash, Z.fourBlue, Z.fourDrop, Z.sixBlue, Z.fiveHex,
            Z.twoDrop, Z.threeDash, Z.oneBlue, Z.sevenDash));
    cast3.setWillDiscard(List.of(Z.fiveDrop, Z.sevenBlue, Z.threeBlue, Z.oneDrop, Z.threeDash,
            Z.fiveBlue, Z.oneBolt, Z.fourBlue, Z.sixCross));
    cast4.setWillDiscard(List.of(Z.fourBlue, Z.threeHex, Z.twoDrop, Z.fourBlue, Z.threeHex,
            Z.oneDash, Z.sixDrop, Z.oneBolt, Z.fiveDash));

    cast1.setWillChoose(List.of(Z.fiveDrop, Z.fourBlue, Z.twoBolt, Z.oneDrop, Z.twoCross, Z.twoDrop,
            Z.fiveHeart, Z.fourBlue, Z.sevenStar));
    cast2.setWillChoose(List.of(Z.fourBlue, Z.fiveHex, Z.twoDrop, Z.oneCross, Z.threeDash,
            Z.oneDash, Z.oneBlue, Z.oneStar, Z.sixCross));
    cast3.setWillChoose(List.of(Z.threeDash, Z.fiveBlue, Z.oneBolt, Z.fourBlue, Z.fourCross,
            Z.sixCross, Z.sixDrop, Z.sevenHeart, Z.fourBlue));
    cast4.setWillChoose(List.of(Z.threeHex, Z.threeHex, Z.fourBlue, Z.sixBlue, Z.fiveHex,
            Z.fiveBlue, Z.oneBolt, Z.oneBlue, Z.fiveStar));

    cast1.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    cast2.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    cast3.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
    cast4.setWillCall(List.of(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty()));
  }

  //********************************************************************************** OTHER HELPERS
  /**
   * Mock controller
   */
  private void turn() {
    System.out.println("Flipping well");
    game.flipWell();
    System.out.println("  Well: " + game.getWell());
    System.out.println("Collecting pond");
    game.collectPond();
    System.out.println("  Pond: " + game.getPond());
    System.out.println("Collecting NBCs");
    game.collectNBCs();
    if (game.roundOver()) {
      System.out.println("\n(!) " + game.getRendState().get());
      System.out.println(" ");
      System.out.println(game.toString());
      return;
    }

    System.out.println("Flipping pond");
    game.flipPond();
    System.out.println("Allowing choices");
    game.allowChoices();
    if (game.roundOver()) {
      System.out.println("\n(!) " + game.getRendState().get());
      System.out.println(game.toString());
    }
    System.out.println(" ");
  }

  /**
   * Prints starting lists which makes game easier to follow.
   */
  private void startingLists() {
    System.out.println("Predetermined player choice lists: ");
    for (IPlayer p : players) {
      TstPlayer pCast = (TstPlayer) p;
      System.out.println("  for " + p.name() + ": ");
      System.out.println("    'willDiscard' = " + pCast.willDiscard);
      System.out.println("    'willChoose' = " + pCast.willChoose);
      System.out.println("    'willCall' = " + pCast.willCall);
    }
    System.out.println(" ");
  }
}
