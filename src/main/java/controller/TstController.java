package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import display.IDisplay;
import display.StringDisplay;
import game.IGame;
import game.StandardGame;
import game.deck.DeckType;
import game.deck.TypeDeck;
import player.AIPlayer;
import player.HPlayer;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.strategies.StrategyWin;
import player.strategy.strategies.StrategyWinProb;
import player.strategy.strategies.StrategyWinProbMem;

/**
 * A test controller for a game of Blues. Displays game state to a specific player.
 */
public class TstController {
  private IGame game;
  private IDisplay display;

  /**
   * Creates a new controller for pre-existing game and display. Sets fields equal to the
   *   corresponding parameters.
   * @param g this controller's game
   * @param d this controller's display
   */
  public TstController(IGame g, IDisplay d) {
    this.game = g;
    this.display = d;
  }

  /**
   * Directs game to start round, renders updated game state. Runs turns until game's roundOver
   *   field is false - once false, game updates scores and game logic, and this controller directs
   *   display to notify user that the round is over. If the game isn't over, calls itself
   *   recursively to begin another round - else, directs display to notify user that the game is
   *   over.
   */
  public void run() {
    game.startRound();
    while (!game.roundOver()) turn();
    if (!game.gameOver()) run();
  }

  /**
   * Directs display to ask player if they want to play another game. Returns true if they do, false
   *   otherwise.
   * @return true if controller should run another game, false otherwise
   */
  public boolean repeat() {
    if (display.askPlayAgain()) {
      game.resetNewGame();
      return true;
    }
    return false;
  }

  /**
   * Starts a game of Blues by passing parameter args to startMain, which parses the Strings, uses
   *   them to create a game and display, and returns a controller with said game and display. Runs
   *   games via this controller until player quits by entering 'N' into terminal after display
   *   calls 'askPlayAgain'.
   * @param args directions for game, player, and display creation - passed to startMain
   */
  public static void /*main*/ m(String[] args) {
    TstController c = startMain(args);
    boolean repeat = true;
    while (repeat) {
      c.run();
      repeat = c.repeat();
    }
  }

  //**************************************************************************************** HELPERS
  /**
   * Facilitates a turn of Blues. Directs game to flip the well, then renders updated game state.
   *   Directs game to collect the pond - game calls each of its players' askDiscard() methods -
   *   instances of AIPlayer return a value based on their code logic, instances of HumanPlayer
   *   return a value by getting user input via the display. Renders updated game state. Directs
   *   game to ask if there are any 'No Blues' calls and stores the answer. Directs game to flip the
   *   pond, then renders updated game state. If there WEREN'T any 'No Blues' calls: directs game to
   *   allow players to choose from the pond/well in order of best discard - game calls each of its
   *   players' askChoose() methods - instances of AIPlayer and HumanPlayer act as aforementioned -
   *   display renders updated game state independent of controller. Game clears the well when
   *   appropriate. If a player chooses a card that gives them 'Blues', game prevents any remaining
   *   players from choosing and ends the round so that roundOver() returns true. If there WERE 'No
   *   Blues' call(s): game ends the round so that roundOver() returns true and skips choosing.
   */
  private void turn() {
    game.flipWell();
    display.renderTable();
    game.collectPond(); // calls display.askDiscard() via player
    display.renderTable();
    game.collectNBCs(); // calls display.collectNBCs() via player
    game.flipPond();
    display.renderTable();
    if (game.roundOver()) return;
    game.allowChoices(); // calls display.askChoose() via IPlayer, terminates if player gets blues
  }

  /**
   * Creates four players based on the String[] 'args' that were passed to 'main'. Creates a new
   *   game with those four players, a display for the first player in the list, and returns a new
   *   controller for these four players. Calls 'throwForInvalidArgs' for each String in 'args'.
   * @param args strings that specify the four player types
   * @return a controller with the new players
   */
  private static TstController startMain(String[] args) {
    List<String> playerNames = List.of("Player 1", "Player 2", "Player 3", "Player 4");
    List<IPlayer> players = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      char[] chars = args[i].toCharArray();
      if (chars[0] == 'H') {
        players.add(new HPlayer(playerNames.get(i)));
        continue;
      }
      String approachStr = new StringBuilder().append(chars[1]).append(chars[2]).append(chars[3])
              .toString();
      String approachString = "ran";
      Approach approach = Approach.RANDOM;
      switch (approachStr) {
        case "min" -> {
          approachString = "min";
          approach = Approach.MIN_POINTS;
        }
        case "max" -> {
          approachString = "max";
          approach = Approach.MAX_TRUMP;
        }
      }
      if (chars.length == 5) {
        players.add(new AIPlayer(approachString + "W", new StrategyWin(approach)));
        continue;
      }
      if (chars.length == 6) {
        players.add(new AIPlayer(approachString + "WP", new StrategyWinProb(approach)));
        continue;
      }
      if (chars.length == 10) {
        players.add(new AIPlayer(approachString + "WPM", new StrategyWinProbMem(approach, 100)));
        continue;
      }
      if (chars.length == 9) {
        int accuracy = Integer.parseInt(new StringBuilder(chars[7]).append(chars[8]).toString());
        players.add(new AIPlayer(approachString + "WPM" + accuracy,
                new StrategyWinProbMem(approach, accuracy)));
        continue;
      }
      int accuracy = Integer.parseInt(String.valueOf(chars[7]));
      players.add(new AIPlayer(approachString + "WPM" + accuracy, new StrategyWinProbMem(approach, accuracy)));
    }

    IGame g = new StandardGame(players, new TypeDeck(DeckType.STANDARD));
    IDisplay d = new StringDisplay(g, players);
    TstController c = new TstController(g, d);
    System.setIn(System.in);

    d.renderWelcome();
    return c;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TstController o = (TstController) other;
    return this.game.equals(o.game) && this.display.equals(o.display);
  }

  @Override
  public int hashCode() {
    return Objects.hash(game, display);
  }
}
