import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import game.rends.REnd;
import game.rends.REndState;
import game.score.RScore;
import game.score.ScoreSheet;
import player.IPlayer;
import player.TstPlayer;

public class TestScoreSheet {
  IPlayer player1; // name is "P1"
  IPlayer player2; // name is "Player 2 "
  IPlayer player3; // name is "Player 3333333333"
  IPlayer player4; // name is "New player"
  IPlayer[] fourPlayers;
  int[] deltas1; // winner = player3
  REndState rendState1;
  RScore rScore1;
  int[] deltas2; // winner = player1
  int[] totals2;
  REndState rendState2;
  RScore rScore2;
  int[] deltas3; // winner = player4
  int[] totals3;
  REndState rendState3;
  RScore rScore3;
  ScoreSheet ss;

  String rScore2SSTS = "|2      |+0     8  |+2     8  |+10    5  |+5     18 ||+17    39 |\n";
  String rScore3TS = """
          Round ended with a Blues call by New player
          _________________________________________________________________
          |ROUND  |P1        |Player 2  |Player 33 |New playe ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |3      |+25    33 |+25    33 |+25    30 |+0     18 ||+75    114|
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";
  String scoreSheetTS = """
          _________________________________________________________________
          |ROUND  |P1        |Player 2  |Player 33 |New playe ||TOTAL     |
          |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
          |-------|----------|----------|----------|----------||----------|
          |1      |+8     8  |+6     6  |-5    -5  |+13    13 ||+22    22 |
          |2      |+0     8  |+2     8  |+10    5  |+5     18 ||+17    39 |
          |3      |+25    33 |+25    33 |+25    30 |+0     18 ||+75    114|
          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾""";

  @Test
  public void testRScore() {
    initTests();
    Assert.assertEquals(rScore3.getTotals(), totals3);
    Assert.assertEquals(rScore3.getWinner(), player4);
    Assert.assertNotEquals(scoreSheetTS, rScore2.toString());
    Assert.assertEquals(rScore2.ssToString(), rScore2SSTS);
    Assert.assertEquals(rScore3.toString(), rScore3TS);
    Assert.assertEquals(new RScore(2, deltas2, totals2, rendState2,
            fourPlayers).hashCode(), rScore2.hashCode());
  }

  @Test
  public void testScoreSheet() {
    initTests();
    ss = new ScoreSheet(Arrays.stream(fourPlayers).toList());

    Assert.assertThrows(IllegalArgumentException.class, () ->
            ss.addRound(new int[] {8, 9, 10}, rendState1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            ss.addRound(new int[] {5, 0, 5, 10, 20}, new REndState(REnd.BLUES,
                    new TstPlayer("P5"))));
    Assert.assertThrows(IllegalStateException.class, () -> ss.addRound(deltas1, rendState1));

    updatePlayerScores(fourPlayers, deltas1);
    ss.addRound(deltas1, rendState1);
    Assert.assertEquals(ss.getCurrentRound(), 2);
    Assert.assertEquals(ss.getRound(1), rScore1);

    updatePlayerScores(fourPlayers, deltas2);
    ss.addRound(deltas2, rendState2);
    Assert.assertEquals(ss.getCurrentRound(), 3);
    Assert.assertEquals(ss.getRound(2), rScore2);

    updatePlayerScores(fourPlayers, deltas3);
    ss.addRound(deltas3, rendState3);
    Assert.assertEquals(ss.getCurrentRound(), 4);
    Assert.assertEquals(ss.getRound(3), rScore3);

    Assert.assertThrows(IllegalArgumentException.class, () -> ss.getRound(-500));
    Assert.assertEquals(ss.toString(), scoreSheetTS);
  }

  @Test
  public void testScoreSheetGCOs() {
    initTests();
    IPlayer player1B = new TstPlayer("P1");
    IPlayer player2B = new TstPlayer("Player 2 ");
    IPlayer player3B = new TstPlayer("Player 3333333333");
    IPlayer player4B = new TstPlayer("New player");
    IPlayer[] fourPlayersB = new IPlayer[] {player1B, player2B, player3B, player4B};
    ss = new ScoreSheet(Arrays.stream(fourPlayers).toList());
    ScoreSheet fourPScoreSheetB = new ScoreSheet(Arrays.stream(fourPlayersB).toList());
    ScoreSheet fourPScoreSheetN = new ScoreSheet(Arrays.stream(fourPlayersB).toList());

    updatePlayerScores(fourPlayers, deltas1);
    updatePlayerScores(fourPlayersB, deltas1);
    ss.addRound(deltas1, rendState1);
    fourPScoreSheetB.addRound(deltas1, new REndState(REnd.BLUES, player3B));
    updatePlayerScores(fourPlayers, deltas2);
    updatePlayerScores(fourPlayersB, deltas2);
    ss.addRound(deltas2, rendState2);
    fourPScoreSheetB.addRound(deltas2, new REndState(REnd.BLUES, player1B));

    Assert.assertEquals(ss, fourPScoreSheetB);
    Assert.assertEquals(ss.hashCode(), fourPScoreSheetB.hashCode());
    Assert.assertNotEquals(ss, fourPScoreSheetN);
    Assert.assertNotEquals(ss.hashCode(), fourPScoreSheetN.hashCode());
    Assert.assertNotEquals(new Object(), ss);
    Assert.assertNotEquals(ss, new ScoreSheet(Arrays.stream(fourPlayersB).toList()));
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    player1 = new TstPlayer("P1");
    player2 = new TstPlayer("Player 2 ");
    player3 = new TstPlayer("Player 3333333333");
    player4 = new TstPlayer("New player");
    fourPlayers = new IPlayer[] {player1, player2, player3, player4};

    deltas1 = new int[] {8, 6, -5, 13, 22}; // winner = player3
    rendState1 = new REndState(REnd.BLUES, player3);
    rScore1 = new RScore(1, deltas1, deltas1, rendState1, fourPlayers);

    deltas2 = new int[] {0, 2, 10, 5, 17}; // winner = player1
    totals2 = new int[] {8, 8, 5, 18, 39};
    rendState2 = new REndState(REnd.BLUES, player1);
    rScore2 = new RScore(2, deltas2, totals2, rendState2, fourPlayers);

    deltas3 = new int[] {25, 25, 25, 0, 75}; // winner = player4
    totals3 = new int[] {33, 33, 30, 18, 114};
    rendState3 = new REndState(REnd.BLUES, player4);
    rScore3 = new RScore(3, deltas3, totals3, rendState3, fourPlayers);
  }

  private void updatePlayerScores(IPlayer[] players, int[] deltas) {
    for (int i = 0; i < players.length; i++) {
      IPlayer p = players[i];
      p.addPoints(deltas[i]);
    }
  }
}
