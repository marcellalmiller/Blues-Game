package game.score;

import java.util.Arrays;
import java.util.Objects;

import game.rends.REndState;
import player.IPlayer;

/**
 * Player scores for a single round of Blues - instantiated exclusively by ScoreSheet's 'addRound()'
 *   method. Most fields are determined based on values stored in an enveloping ScoreSheet.
 */
public class RScore {
  private final int round;
  private final int[] deltas;
  private final int[] totals;
  private final IPlayer winner;
  private final REndState rendState;
  private final IPlayer[] players; // for 'toString()' purposes

  /**
   * Player scores for a single round of Blues. 'winner' is self-explanatory for BLUES and NO calls
   *   - if round ends with FALSE_NO, 'winner' is incorrect receiver - if round ends with
   *   DECK_EMPTY, 'winner' is player with the least points (tie goes to player with the most recent
   *   win - if nobody in the tie has won yet, 'winner' has the best card).
   * @param round the round
   * @param deltas the amount of points each player gained this round
   * @param totals the total points each player has
   * @param rendState the REndState of this round
   * @param players the players
   */
  public RScore(int round, int[] deltas, int[] totals, REndState rendState, IPlayer[] players) {
    this.round = round;
    this.deltas = deltas;
    this.totals = totals;
    this.winner = rendState.getWinner();
    this.rendState = rendState;
    this.players = players;
  }

  //**************************************************************************************** GETTERS
  /**
   * Returns the total scores of all players. Score indices correspond to 'players' indices.
   * @return the total scores of all players
   */
  public int[] getTotals() {
    return totals;
  }

  /**
   * Returns the winner of this round.
   * @return the winner of this round.
   */
  public IPlayer getWinner() {
    return winner;
  }

  /**
   * Returns the REndState of this round.
   * @return the REndState of this round
   */
  public REndState getRendState() {
    return rendState;
  }

  /**
   * Returns total points gained this round - the sum of all players' point deltas.
   * @return total points gained this round
   */
  public int[] getDeltas() {
    return deltas;
  }

  //****************************************************************************** STRING FORMATTING
  /**
   * Returns a String formatted for inclusion in ScoreSheet's override of toString().
   * @return formatted String
   */
  public String ssToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("|").append(round).append("     ");
    if (round < 10) {
      sb.append(" ");
    }
    sb.append("|");
    for (int i = 0; i < deltas.length; i++) {
      if (deltas[i] >= 0) {
        sb.append("+");
      }
      sb.append(deltas[i]).append("   ");
      if (deltas[i] < 10 && deltas[i] > -10) {
        sb.append(" ");
      }

      if (totals[i] > -1) {
        sb.append(" ");
      }
      sb.append(totals[i]);
      if (totals[i] < 100 && totals[i] > -100) {
        sb.append(" ");
        if (totals[i] < 10 && totals[i] > -10) {
          sb.append(" ");
        }
      }
      sb.append("|");
      if (i == deltas.length - 2) {
        sb.append("|");
      }
    }

    sb.append("\n");
    return sb.toString();
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return rendState.toString() + "\n" + ScoreSheet.header(players) + ssToString() + "‾‾‾‾‾‾‾‾‾‾"
            + "‾‾‾‾‾‾‾‾‾‾‾".repeat(deltas.length);

  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    RScore o = (RScore) other;
    return this.toString().equals(other.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(round, Arrays.hashCode(deltas), Arrays.hashCode(totals), winner,
            Arrays.hashCode(players));
  }
}
