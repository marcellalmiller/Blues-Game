package game.rends;

import java.util.Objects;

import player.IPlayer;

/**
 * The state of a game after the round ends - Optional fields are dependent on the type of ending
 *   (see endings in 'REnd.java' class). Constructed by any IGame subclass and assigned to its
 *   Optional rendState field while scores are tallied and end of round visuals are displayed.
 *   Discarded once a new round is started.
 */
public class REndState {
  REnd end; // BLUES, NO, FALSE_NO, or DECK_EMPTY
  IPlayer winner;
  NBCall nbc; // non-null if end is NO or FALSE_NO

  /**
   * The state of a game after a round ends with a 'Blues' call.
   * @param end the type of end - BLUES or DECK_EMPTY
   * @param winner the winner - caller if BLUES, player with the fewest hand points if DECK_EMPTY
   * @throws IllegalArgumentException if parameter 'end' isn't REnd.BLUES or REnd.DECK_EMPTY
   */
  public REndState(REnd end, IPlayer winner) {
    if (!(end.equals(REnd.BLUES) || end.equals(REnd.DECK_EMPTY))) {
      throw new IllegalArgumentException("Use alternate constructor for NO, FALSE_NO, or DECK_EMPTY"
              + " round endings");
    }
    this.end = end;
    this.winner = winner;
    this.nbc = null;
  }

  /**
   * The state of a game after a round ends with a 'No Blues' call (correct or incorrect).
   * @param end the type of end - TRUE_NO or FALSE_NO
   * @param nbc the 'No Blues' call
   * @param winner the winner - if TRUE_NO, the caller, if FALSE_NO, the receiver
   * @throws IllegalArgumentException if parameter 'end' isn't REnd.TRUE_NO or REnd.FALSE_NO
   */
  public REndState(REnd end, NBCall nbc, IPlayer winner) {
    if (!end.equals(REnd.TRUE_NO) && !end.equals(REnd.FALSE_NO)) {
      throw new IllegalArgumentException("Use alternate constructor for BLUES or DECK_EMPTY round "
              + "endings");
    }
    this.end = end;
    this.nbc = nbc;
    this.winner = winner;
  }

  //**************************************************************************************** GETTERS
  /**
   * Returns this round ending's end type. See REnd.java for all end types.
   * @return this round ending's end type
   */
  public REnd getEnd() {
    return end;
  }

  /**
   * Returns this round's winner.
   * @return this round's winner
   */
  public IPlayer getWinner() {
    return winner;
  }

  /**
   * Returns this round ending's 'No Blues' call if it exists.
   * @return this round ending's 'No Blues' call if it exists
   * @throws IllegalStateException if 'nbc' is null
   */
  public NBCall getNbc() {
    if (nbc == null) {
      throw new IllegalStateException("Round didn't end with a 'No Blues' call");
    }
    return nbc;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Round ended ");
    switch (end) {
      case BLUES -> {
        sb.append("with a Blues call by ").append(winner.name());
      }
      case TRUE_NO -> {
        sb.append("with a correct No Blues call by ").append(nbc.caller().name()).append(" on ")
                .append(nbc.receiver().name());
      }
      case FALSE_NO -> {
        sb.append("with an incorrect No Blues call by ").append(nbc.caller().name()).append(" on ")
                .append(nbc.receiver().name());
      }
      case DECK_EMPTY -> {
        sb.append("because the deck was empty");
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    REndState o = (REndState) other;
    if (this.getEnd().equals(o.getEnd())) {
      return switch (end) {
        case BLUES, DECK_EMPTY -> this.getWinner().equals(o.getWinner());
        case TRUE_NO, FALSE_NO -> this.getWinner().equals(o.getWinner())
                && this.getNbc().equals(o.getNbc());
      };
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(end, winner, nbc);
  }
}
