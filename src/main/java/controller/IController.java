package controller;

/**
 * A controller for a game of Blues. Acts as an intermediary between the game model and the display.
 *   Human players (HPlayer) can alter game state by interacting with display independent of
 *   controller - AI players (AIPlayer) cannot.
 */
public interface IController {

  /**
   * Runs a game of Blues.
   */
  void run();

  //! TODO: IController should only contain one method (run)
  /**
   * Returns true if controller should run another game, false otherwise.
   * @return true if controller should run another game
   */
  boolean repeat();
}
