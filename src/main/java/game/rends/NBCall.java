package game.rends;

import java.util.Objects;

import player.IPlayer;

/**
 * A 'No Blues' call.
 */
public class NBCall {
  private final IPlayer caller;
  private final IPlayer receiver;

  /**
   * A 'No Blues' call. Sets this call's fields equal to corresponding passed parameters.
   * @param caller the 'No Blues' caller
   * @param receiver the 'No Blues' receiver
   */
  public NBCall(IPlayer caller, IPlayer receiver) {
    this.caller = caller;
    this.receiver = receiver;
  }

  //**************************************************************************************** GETTERS
  /**
   * Returns the 'No Blues' caller.
   * @return the 'No Blues' caller
   */
  public IPlayer caller() {
    return caller;
  }

  /**
   * Returns the 'No Blues' receiver.
   * @return the 'No Blues' receiver
   */
  public IPlayer receiver() {
    return receiver;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  public String toString() {
    return caller.name() + " called No Blues on " + receiver.name();
  }

  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    NBCall o = (NBCall) other;
    return this.caller().equals(o.caller()) && this.receiver().equals(o.receiver());
  }

  public int hashCode() {
    return Objects.hash(caller, receiver);
  }
}
