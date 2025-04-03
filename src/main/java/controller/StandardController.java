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
 * A standard controller for a game of Blues. Displays game state to a specific player.
 */
public class StandardController implements IController {
  private final IGame game;
  private final IDisplay display;

  /**
   * Creates a new controller for pre-existing game and display. Sets fields equal to the
   *   corresponding parameters.
   * @param g this controller's game
   * @param d this controller's display
   */
  public StandardController(IGame g, IDisplay d) {
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
  @Override
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
  @Override
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
  public static void main(String[] args) {
    IController c = startMain(args);
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
  private static IController startMain(String[] args) {
    List<String> playerNames = List.of("Player 1", "Player 2", "Player 3", "Player 4");
    List<IPlayer> players = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      char[] chars = args[i].toCharArray();
      // throwForInvalidArg(chars);
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
    IController c = new StandardController(g, d);
    System.setIn(System.in);

    d.renderWelcome();
    return c;
  }

  /**
   * Throws an IllegalArgumentException if the char array 'chars' cannot be read by 'startMain'. The
   *   first char in a valid arg specifies what type of player this string describes: either 'A' for
   *   AIPlayer or 'H' for HumanPlayer (if the first char is 'H', there shouldn't be any other
   *   chars). The next three chars in the arg specify the approach the AIPlayer will use: either
   *   'min' for MIN_POINTS, 'max' for MAX_TRUMP, or 'ran' for RANDOM. The next chars specify the
   *   strategy the AIPlayer will use: either 'E' for StrategyEmpty, 'W' for StrategyWin, 'WP' for
   *   StrategyWinProb, 'WPM' for StrategyWinProbMem, or 'WPMP' for StrategyWinProbMemProf (if the
   *   chars are 'E', 'W', or 'WP', there shouldn't be anything else in the array). The next chars
   *   specify the accuracy of the strategy and correspond to a number from 0 to 100.
   * @param chars the char array of a string passed to 'main'
   * @throws IllegalArgumentException if 'chars' cannot be read by 'startMain'
   */
  private static void throwForInvalidArg(char[] chars) {
    boolean throwEx = false;
    if (chars.length == 1 && chars[0] == 'H') return;
    List<Integer> validLengths = new ArrayList<>(List.of(6, 8, 9, 10));

    while (!throwEx) {
      // verify that arg begins with "Amin" or "Amax" or "Aran"
      if (chars[0] != 'A' || !validLengths.contains(chars.length)) {
        throwEx = true;
        break;
      }
      String appStr = String.valueOf(chars[1]) + chars[2] + chars[3];
      if (!(appStr.equals("min") || appStr.equals("max") || appStr.equals("ran"))) {
        throwEx = true;
        break;
      } if (chars.length == 5) { // if chars.length == 5, arg should end with 'W' or 'E'
        if (chars[4] != 'W' && chars[4] != 'E') throwEx = true;
        break;
      } if (chars.length == 6) { // if chars.length == 6, arg should end with 'WP'
        if (chars[4] != 'W' || chars[5] != 'P') throwEx = true;
        break;
      } // remaining valid args have 'WPM' at indices 4 - 6
      if (chars[4] != 'W' || chars[5] != 'P' || chars[6] != 'M') {
        throwEx = true;
        break;
      } if (chars.length == 8) { // if chars.length == 8, arg should end with a digit
        if (!Character.isDigit(chars[7])) throwEx = true;
        break;
      } // remaining valid args have either a digit at 7 + a digit at 8 or 'P' at 7 and a digit at 8
      if (Character.isDigit(chars[7]) && !Character.isDigit(chars[8])) throwEx = true;
      if (chars[7] != 'P' || !Character.isDigit(chars[8])) throwEx = true;
      // handle remaining possibilities by breaking according to chars.length and/or checking that
      //   characters in chars are digits
      if (chars.length == 9) break;
      if (!Character.isDigit(chars[9])) throwEx = true;
      if (chars.length == 10) break;
      if (!Character.isDigit(chars[10])) throwEx = true;
    }

    if (throwEx) throw new IllegalArgumentException("""
            Each String arg must look like one of the following:
            
            'H' (human player)
            'AxxxE' (AI player with StrategyEmpty)
            'AxxxW' (AI player with StrategyWin)
            'AxxxWP' (AI player with StrategyWinProb)
            'AxxxWPMddd' (AI player with StrategyWinProbMem)
            'AxxxWPMPddd' (AI player with StrategyWinProbMemProf)
            
            'xxx' is where an abbreviated version of the player's approach should go:
              - 'min' (for MIN_POINTS)
              - 'max' (for MAX_TRUMP)
              - 'ran' (for RANDOM)
            
            'ddd' is where the player's accuracy should go. This int value can be between 0 and 100
            even though it's displayed as being three characters here.
            """);
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    StandardController o = (StandardController) other;
    return this.game.equals(o.game) && this.display.equals(o.display);
  }

  @Override
  public int hashCode() {
    return Objects.hash(game, display);
  }
}
