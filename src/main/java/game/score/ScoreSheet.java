package game.score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import game.rends.REndState;
import player.IPlayer;

/**
 * A score-sheet for a game of blues. Keeps track of how many points each player gains per round,
 *   how many points the game gains per round, and each round's winner.
 */
public class ScoreSheet {
  ArrayList<RScore> sheet;
  int currentRound;
  int size; // amount of players
  IPlayer[] order; // order of players

  /**
   * Creates a new score-sheet. Initializes currentRound to 0.
   * @param players the players in order
   */
  public ScoreSheet(List<IPlayer> players) {
    sheet = new ArrayList<>();
    currentRound = 1;
    size = players.size();
    order = players.toArray(new IPlayer[0]);
  }

  //************************************************************************************** MODIFIERS
  /**
   * Adds a round to this score-sheet. Calculates new totals by adding values of parameter
   *   playerDeltas to values of previous round. Calculates game delta by adding all values of
   *   playerDeltas. 'playerDeltas' indices correspond to 'order' indices.
   * @param deltas points each player gained this round and the sum of all point gains (last index)
   * @param rendState this round's REndState
   * @throws IllegalArgumentException if playerDeltas.length doesn't equal size + 1 or if order
   *                                  doesn't contain rendState's winner
   */
  public void addRound(int[] deltas, REndState rendState) {
    if (deltas.length != size + 1) {
      throw new IllegalArgumentException(deltas.length + " point deltas passed to addRound() - "
              + size + " should be passed, one for each of the " + size + " players and 1 for the "
              + "sum\n");
    }
    if (!Arrays.stream(order).toList().contains(rendState.getWinner())) {
      throw new IllegalArgumentException("This score sheet doesn't contain the passed winner :"
              + rendState.getWinner());
    }

    int[] totals = new int[size + 1];
    int[] previousTotals;
    if (!sheet.isEmpty()) previousTotals = sheet.get(currentRound - 2).getTotals();
    else previousTotals = new int[size + 1];

    for (int i = 0; i < size + 1; i++) totals[i] = previousTotals[i] + deltas[i];

    sheet.add(new RScore(currentRound, deltas, totals, rendState, order));
    currentRound++;
    throwIfDiscrepancy(totals);
  }

  //**************************************************************************************** GETTERS
  /**
   * Returns the roundNum-th round.
   * @param roundNum the round to get
   * @return the roundNum-th round
   * @throws IllegalArgumentException if roundNum-th round doesn't exist
   */
  public RScore getRound(int roundNum) {
    if (roundNum < 1 || roundNum > sheet.size()) {
      throw new IllegalArgumentException("Round " + roundNum + " doesn't exist");
    }
    return sheet.get(roundNum - 1);
  }

  /**
   * Returns the index of the round currently being played. This index does NOT have an RScore
   *   associated with it yet.
   * @return the index of the current round
   */
  public int getCurrentRound() {
    return currentRound;
  }

  /**
   * Returns the last (most recent) round recorded in this ScoreSheet.
   * @return the last round in this ScoreSheet
   * @throws IllegalArgumentException if 'sheet' is empty
   */
  public RScore getLast() {
    if (sheet.isEmpty()) throw new IllegalArgumentException("No rounds recorded");
    return sheet.getLast();
  }

  /**
   * Returns the first (least recent) round recorded in this ScoreSheet.
   * @return the first round in this score sheet
   * @throws IllegalArgumentException if 'sheet' is empty
   */
  public RScore getFirst() {
    if (sheet.isEmpty()) throw new IllegalArgumentException("No rounds recorded");
    return sheet.getFirst();
  }

  /**
   * Returns the desired player's win percentage - the percentage of rounds they have won.
   * @param p desired player
   * @return the desired player's win percentage
   */
  public double getWinPercentage(IPlayer p) {
    int won = 0;
    for (RScore r : sheet) {
      if (r.getWinner().equals(p)) won++;
    }
    return (won * 1.00) / sheet.size();
  }

  // TODO: implement
  public List<Integer> getWinTypePercentages(IPlayer p) {

    return List.of();
  }

  //**************************************************************************************** HELPERS
  /**
   * Throws IllegalStateException if a score-sheet total currently being added doesn't match the
   *   actual total of the corresponding player.
   * @throws IllegalStateException if a score-sheet total and actual total aren't equal
   */
  private void throwIfDiscrepancy(int[] totals) {
    for (int i = 0; i < order.length; i++) {
      if (totals[i] != order[i].getPoints()) {
        sheet.remove(currentRound - 2);
        currentRound--;
        throw new IllegalStateException("Discrepancy between score sheet and player score! "
                + order[i].name() + " has " + totals[i] + " points according to score sheet, but "
                + order[i].getPoints() + " points according to player records\n");
      }
    }
  }

  /**
   * Creates the header for this score-sheet.
   * @return the header for this score-sheet
   */
  public static String header(IPlayer[] o) {
    StringBuilder sb = new StringBuilder();
    sb.append("__________").append("___________".repeat(o.length + 1))
            .append("\n|ROUND  |");
    for (int i = 0; i < o.length; i++) {
      String full = o[i].name();
      String trimmed = full.length() > 9 ? full.substring(0, 9) : full;
      sb.append(trimmed).append(" ".repeat(9 - trimmed.length())).append(" |");
    }
    sb.append("|TOTAL     |\n|       |").append(" Δ     ∑  |".repeat(o.length))
            .append("| Δ     ∑  |\n");
    sb.append("|-------|").append("----------|".repeat(o.length)).append("|----------|\n");
    return sb.toString();
  }

  public String htmlToString() {
    StringBuilder sb = new StringBuilder();

    sb.append("<html><pre>__________").append("___________".repeat(order.length + 1))
            .append("<br>|ROUND  |");

    for (int i = 0; i < order.length; i++) {
      String full = order[i].name();
      String trimmed = full.length() > 9 ? full.substring(0, 9) : full;
      sb.append(trimmed).append(" ".repeat(9 - trimmed.length())).append(" |");
    }

    sb.append("|TOTAL     |<br>|       |").append(" Δ     ∑  |".repeat(order.length))
            .append("| Δ     ∑  |<br>");
    sb.append("|-------|").append("----------|".repeat(order.length)).append("|----------|<br>");

    for (RScore r : sheet) {
      sb.append(r.ssToString()).append("<br>");
    }

    sb.append("‾‾‾‾‾‾‾‾‾‾").append("‾‾‾‾‾‾‾‾‾‾‾".repeat(order.length + 1));
    sb.append("</pre></html>");

    return sb.toString();
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(header(order));
    for (RScore r : sheet) {
      sb.append(r.ssToString());
    }
    sb.append("‾‾‾‾‾‾‾‾‾‾").append("‾‾‾‾‾‾‾‾‾‾‾".repeat(order.length + 1));

    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    ScoreSheet o = (ScoreSheet) other;
    return this.toString().equals(o.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(sheet, currentRound, size, Arrays.hashCode(order));
  }
}
