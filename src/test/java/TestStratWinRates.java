import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import game.IGame;
import game.StandardGame;
import game.deck.DeckType;
import game.deck.TypeDeck;
import game.score.RScore;
import player.AIPlayer;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.strategies.StrategyEmpty;
import player.strategy.strategies.StrategyWin;
import player.strategy.strategies.StrategyWinProb;
import player.strategy.strategies.StrategyWinProbMem;
import player.strategy.strategies.StrategyWinProbMemProf;

public class TestStratWinRates {
  IGame g;
  List<IPlayer> players;
  int rounds;
  int[] pRWins;
  int[] pGWins;
  int[] rScores;
  int[] gScores;
  public TypeDeck deck = new TypeDeck(DeckType.STANDARD);

  @Test //# TEST A
  public void test4minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("4minWPM100---------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST B
  public void test2minWPM50_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 50)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MIN_POINTS, 50)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2minWPM50_2minWPM100-----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST C
  public void test2minWPM100_2minWPM50() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 50)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 50))
    );
    initTests();
    analyze(1000);
    System.out.println("2minWPM100_2minWPM50-----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST D
  public void test2minWPM10_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 10)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MIN_POINTS, 10)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2minWPM10_2minWPM100-----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST E
  public void test2maxWPM100_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2maxWPM100_2minWPM100----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST F
  public void test2ranWPM100_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.RANDOM, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.RANDOM, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2ranWPM100_2minWPM100----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST G
  public void test2maxWPM100_2ranWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.RANDOM, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.RANDOM, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2maxWPM100_2ranWPM100----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test
  public void test4minWP() { //# TEST H
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("4minWP-------------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test
  public void test2maxWP_2minWP() { //# TEST I
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProb(Approach.MAX_TRUMP)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.MAX_TRUMP)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("2maxWP_2minWP------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test
  public void test2ranWP_2minWP() { //# TEST J
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProb(Approach.RANDOM)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.RANDOM)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("2ranWP_2minWP------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test
  public void test2maxWP_2ranWP() { //# TEST K
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProb(Approach.MAX_TRUMP)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.MAX_TRUMP)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.RANDOM)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.RANDOM))
    );
    initTests();
    analyze(1000);
    System.out.println("2maxWP_2ranWP------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST L
  public void test2minWP_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2minWP_2minWPM100--------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST M
  public void test1minWPM100_3maxWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 2", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MAX_TRUMP, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("1minWPM100_3maxWPM100----------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST N
  public void test4minW() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWin(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("4minW--------------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST O
  public void test2minW_2ranW() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWin(Approach.RANDOM)),
            new AIPlayer("Player 4", new StrategyWin(Approach.RANDOM))
    );
    initTests();
    analyze(1000);
    System.out.println("2minW_2ranW--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST P
  public void test2minW_2minWP() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("2minW_2minWP--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST Q
  public void test2minW_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2minW_2minWPM100---------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST R
  public void test2minW_2maxW() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWin(Approach.MAX_TRUMP)),
            new AIPlayer("Player 4", new StrategyWin(Approach.MAX_TRUMP))
    );
    initTests();
    analyze(1000);
    System.out.println("2minW_2maxW--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST S
  public void test4minE() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyEmpty(Approach.MIN_POINTS))
    );
    initTests();
    analyze(10000);
    System.out.println("4minE--------------------------------------------------------------------");
    printAnalysis(10000);
  }

  @Test //# TEST T
  public void test2minE_2maxE() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyEmpty(Approach.MAX_TRUMP)),
            new AIPlayer("Player 4", new StrategyEmpty(Approach.MAX_TRUMP))
    );
    initTests();
    analyze(1000);
    System.out.println("2minE_2maxE--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST U
  public void test2minE_2ranE() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyEmpty(Approach.RANDOM)),
            new AIPlayer("Player 4", new StrategyEmpty(Approach.RANDOM))
    );
    initTests();
    analyze(1000);
    System.out.println("2minE_2ranE--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST V
  public void test2ranE_2maxE() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.RANDOM)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.RANDOM)),
            new AIPlayer("Player 3", new StrategyEmpty(Approach.MAX_TRUMP)),
            new AIPlayer("Player 4", new StrategyEmpty(Approach.MAX_TRUMP))
    );
    initTests();
    analyze(1000);
    System.out.println("2ranE_2maxE--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST W
  public void test2minE_2minW() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWin(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("2minE_2minW--------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST X
  public void test2minE_2minWP() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyWinProb(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("2minE_2minWP-------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST Y
  public void test2minE_2minWPM100() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 2", new StrategyEmpty(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 4", new StrategyWinProbMem(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(1000);
    System.out.println("2minE_2minWPM------------------------------------------------------------");
    printAnalysis(1000);
  }

  @Test //# TEST Z
  public void oneEach() {
    players = List.of(
            new AIPlayer("Player 1", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("Player 2", new StrategyWinProb(Approach.MIN_POINTS)),
            new AIPlayer("Player 3", new StrategyWin(Approach.MIN_POINTS)),
            new AIPlayer("Player 4", new StrategyEmpty(Approach.MIN_POINTS))
    );
    initTests();
    analyze(1000);
    System.out.println("1minWPM100_1minWP_1minW_1minE--------------------------------------------");
    printAnalysis(1000);
  }

  @Test
  public void test2minWPM100_2minWPMP100() {
    players = List.of(
            new AIPlayer("minWPM100", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("minWPM100", new StrategyWinProbMem(Approach.MIN_POINTS, 100)),
            new AIPlayer("minWPMP100", new StrategyWinProbMemProf(Approach.MIN_POINTS, 100)),
            new AIPlayer("minWPMP100", new StrategyWinProbMemProf(Approach.MIN_POINTS, 100))
    );
    initTests();
    analyze(100);
    System.out.println("2WPM100_2WPMP100");
    printAnalysis(100);
  }

  @Test
  public void runRandom() {
    test2ranWPM100_2minWPM100(); //# F
    test2maxWPM100_2ranWPM100(); //# G
    test2ranWP_2minWP(); //# J
    test2maxWP_2ranWP(); //# K
  }

  //*********************************************************************************** TEST HELPERS
  private void initTests() {
    g = new StandardGame(players, deck);
    for (IPlayer p : players) p.resetNewGame();
    rounds = 0;
    rScores = new int[4];
    gScores = new int[4];
    pRWins = new int[4];
    pGWins = new int[4];
  }

  private void analyze(int gamesPlayed) {
    for (int i = 0; i < gamesPlayed; i++) {
      run();
      RScore finalScores = g.getScoreSheet().getLast();
      for (int j = 0; j < players.size(); j++) gScores[j] += finalScores.getTotals()[j];
      g.resetNewGame();
    }
  }

  //************************************************************************ MOCK CONTROLLER METHODS
  private void run() {
    System.out.println("Running round " + rounds);
    g.startRound();
    while (!g.roundOver()) {
      turn();
    }

    rounds++;
    for (int i = 0; i < 4; i++) {
      rScores[i] += g.getScoreSheet().getLast().getDeltas()[i];
    }
    pRWins[players.indexOf(g.getScoreSheet().getLast().getWinner())]++;

    if (!g.gameOver()) run();
    else pGWins[players.indexOf(g.getGameWinner())]++;
  }

  private void turn() {
    g.flipWell();
    g.collectPond();
    g.collectNBCs();
    g.flipPond();
    if (g.roundOver()) return;
    g.allowChoices();
  }

  //********************************************************************************** PRINT HELPERS
  private void printAnalysis(int gamesPlayed) {
    for (int i = 0; i < 4; i++) {
      System.out.println("SCORES FOR '" + players.get(i).name() + "'");
      System.out.println(numsForPrintAnalysis(gamesPlayed, i) + "\n");
    }
    System.out.println();
  }

  private void printAnalysisWithExp(int gamesPlayed) {
    for (int i = 0; i < 4; i++) {
      System.out.println("SCORES FOR '" + players.get(i).name() + "'");
      List<Double> nums = numsForPrintAnalysis(gamesPlayed, i);
      System.out.println(" - Average round score: " + nums.getFirst());
      System.out.println(" - Average game score: " + nums.get(1));
      System.out.println(" - Rounds won: " + pRWins[i] + " / " +rounds + " (" + nums.get(2) + "%)");
      System.out.println(" - Games won: " + nums.get(3));
    }
  }

  private List<Double> numsForPrintAnalysis(int gamesPlayed, int playerIdx) {
    List<Double> nums = new ArrayList<>();
    nums.add(Math.round((double) rScores[playerIdx] / (double) rounds * 10.0) / 10.0);
    nums.add(Math.round((double) gScores[playerIdx] / gamesPlayed * 10.0) / 10.0);
    nums.add(Math.round((double) pRWins[playerIdx] / (double) rounds * 100.0 * 10.0) / 10.0);
    nums.add(Math.round((double) pGWins[playerIdx] / (double) gamesPlayed * 1000.0) / 10.0);
    return nums;
  }
}
