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
  void go();
}
