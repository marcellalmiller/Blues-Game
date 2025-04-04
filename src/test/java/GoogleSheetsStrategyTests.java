import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import utility.SheetsServiceUtil;

public class GoogleSheetsStrategyTests {
  IGame g;
  public TypeDeck deck = new TypeDeck(DeckType.STANDARD);
  List<IPlayer> players;
  int[] pRWins;
  int[] pGWins;
  int[] rScores;
  int[] gScores;
  int rounds;
  int games;

  public static List<String> possiblePlayers;
  private final Random random = new Random();
  private static int uniqueCodeLength = 8;

  public static int numberOfSamples = 100;
  public static int sampleSize = 5000;
  public static String spreadsheetId = "1BnLq8vVLhUG3d4hblsfTvhUHtwNHlPoNObfFZte_5cI";
  public static Sheets sheets;

  @BeforeClass
  public static void setup() throws GeneralSecurityException, IOException {
    possiblePlayers = List.of("minE", "maxE", "ranE", "minW", "maxW", "ranW", "minWP", "maxWP",
            "ranWP", "minWPM100", "minWPM10", "maxWPM100", "maxWPM10", "ranWPM100", "ranWPM10");
    sheets = SheetsServiceUtil.getSheetsService();
  }

  @Test
  public void runStudy() throws IOException {
    for (int i = 0; i < numberOfSamples; i++) {
      createPlayers();
      initGame();

      System.out.println("RUNNING SAMPLE " + i);
      runSample();

      writeToSpreadSheet();
    }
  }

  //*********************************************************************************** TEST HELPERS
  private void createPlayers() {
    players = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      String name = possiblePlayers.get(random.nextInt(possiblePlayers.size()));
      switch (name) {
        case "minE" -> players.add(new AIPlayer(name, new StrategyEmpty(Approach.MIN_POINTS)));
        case "maxE" -> players.add(new AIPlayer(name, new StrategyEmpty(Approach.MAX_TRUMP)));
        case "ranE" -> players.add(new AIPlayer(name, new StrategyEmpty(Approach.RANDOM)));
        case "minW" -> players.add(new AIPlayer(name, new StrategyWin(Approach.MIN_POINTS)));
        case "maxW" -> players.add(new AIPlayer(name, new StrategyWin(Approach.MAX_TRUMP)));
        case "ranW" -> players.add(new AIPlayer(name, new StrategyWin(Approach.RANDOM)));
        case "minWP" -> players.add(new AIPlayer(name, new StrategyWinProb(Approach.MIN_POINTS)));
        case "maxWP" -> players.add(new AIPlayer(name, new StrategyWinProb(Approach.MAX_TRUMP)));
        case "ranWP" -> players.add(new AIPlayer(name, new StrategyWinProb(Approach.RANDOM)));
        case "minWPM100" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.MIN_POINTS, 100)));
        case "minWPM10" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.MIN_POINTS, 10)));
        case "maxWPM100" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.MAX_TRUMP, 100)));
        case "maxWPM10" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.MAX_TRUMP, 10)));
        case "ranWPM100" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.RANDOM, 100)));
        case "ranWPM10" -> players.add(new AIPlayer(name, new StrategyWinProbMem(Approach.RANDOM, 10)));
      }
    }
  }

  private void initGame() {
    for (IPlayer p : players) p.resetNewGame();
    g = new StandardGame(players, deck);

    pRWins = new int[4];
    pGWins = new int[4];
    rScores = new int[4];
    gScores = new int[4];
    rounds = 0;
    games = 0;
  }

  private void runSample() {
    for (int i = 0; i < sampleSize; i++) {
      runGame();
      RScore finalScores = g.getScoreSheet().getLast();
      for (int j = 0; j < players.size(); j++) gScores[j] += finalScores.getTotals()[j];
      g.resetNewGame();
    }
  }

  private void runGame() {
    if (rounds % 1000 == 0) System.out.println("Running round " + rounds);
    g.startRound();
    while (!g.roundOver()) {
      turn();
    }

    rounds++;
    for (int i = 0; i < 4; i++) {
      rScores[i] += g.getScoreSheet().getLast().getDeltas()[i];
    }
    pRWins[players.indexOf(g.getScoreSheet().getLast().getWinner())]++;

    if (!g.gameOver()) runGame();
    else {
      pGWins[players.indexOf(g.getGameWinner())]++;
      games++;
      System.out.println("Starting game " + games);
    }
  }

  private void turn() {
    g.flipWell();
    g.collectPond();
    g.collectNBCs();
    g.flipPond();
    if (g.roundOver()) return;
    g.allowChoices();
  }

  private List<Double> numsForAnalysis(int gamesPlayed, int playerIdx) {
    List<Double> nums = new ArrayList<>();
    nums.add(Math.round((double) rScores[playerIdx] / (double) rounds * 10.0) / 10.0);
    nums.add(Math.round((double) gScores[playerIdx] / gamesPlayed * 10.0) / 10.0);
    nums.add(Math.round((double) pRWins[playerIdx] / (double) rounds * 100.0 * 10.0) / 10.0);
    nums.add(Math.round((double) pGWins[playerIdx] / (double) gamesPlayed * 1000.0) / 10.0);
    return nums;
  }

  private void writeToSpreadSheet() throws IOException {
    String uniqueCode = uniqueCode();

    for (int i = 0; i < players.size(); i++) {
      List<Double> nums = numsForAnalysis(sampleSize, i);
      StringBuilder opponents = new StringBuilder();

      for (int j = 0; j < players.size(); j++) {
        if (j != i) opponents.append(players.get(j).name()).append("  ");
      }

      editSpreadSheet(players.get(i).name(), nums.getFirst(), nums.get(1), nums.get(2), nums.get(3),
              sampleSize, opponents.toString(), uniqueCode);
    }
  }

  private void editSpreadSheet(String player, double pointsPerRound, double pointsPerGame,
                               double roundWinRate, double gameWinRate, double sampleSize,
                               String opponents, String uniqueCode) throws IOException {

    ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(pointsPerRound,
            pointsPerGame, roundWinRate, gameWinRate, sampleSize, opponents, uniqueCode)));

    AppendValuesResponse result =
            sheets.spreadsheets().values()
                    .append(spreadsheetId, player + "!A1", body)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .setIncludeValuesInResponse(true)
                    .execute();
  }

  private String uniqueCode() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345678901234567890";
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < uniqueCodeLength; i++) {
      int randomIdx = random.nextInt(characters.length());
      code.append(characters.charAt(randomIdx));
    }

    return code.toString();
  }
}
