package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import display.IDisplay;
import display.TerminalDisplay;
import display.TextDisplay;
import game.IGame;
import game.StandardGame;
import game.deck.DeckType;
import game.deck.TypeDeck;
import player.AIPlayer;
import player.HPlayer;
import player.IPlayer;
import player.strategy.Approach;
import player.strategy.strategies.StrategyEmpty;
import player.strategy.strategies.StrategyWin;
import player.strategy.strategies.StrategyWinProb;
import player.strategy.strategies.StrategyWinProbMem;

/**
 * A standard controller for a game of Blues. Displays game state to a specific player.
 */
public class MainController implements IController {
  private IGame game;
  private IDisplay display;

  /**
   * Creates a MainController object without a pre-exising game or display.
   */
  public MainController() {
  }

  public void go() {
    start();
    play();
  }

  //**************************************************************************************** HELPERS
  private void start() {
    this.display = new TextDisplay();
    List<IPlayer> players = createPlayers(display.askDifficulty());
    this.game = new StandardGame(players, new TypeDeck(DeckType.STANDARD));
    display.setGame(game, players);
    display.renderWelcome();
  }

  private void play() {
    boolean repeat = true;
    while (repeat) {
      runGame();
      repeat = display.askPlayAgain();
    }
  }

  private void runGame() {
    game.startRound();
    while (!game.roundOver()) runTurn();
    if (!game.gameOver()) runGame();
  }

  private void runTurn() {
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

  private List<IPlayer> createPlayers(int difficulty) {
    List<IPlayer> players = new ArrayList<>();
    players.add(new HPlayer("YOU"));

    switch (difficulty) {
      case 1 -> {
        players.add(new AIPlayer("Bot 1", new StrategyEmpty(Approach.MIN_POINTS)));
        players.add(new AIPlayer("Bot 2", new StrategyEmpty(Approach.MAX_TRUMP)));
        players.add(new AIPlayer("Bot 3", new StrategyEmpty(Approach.RANDOM)));
      }
      case 2 -> {
        players.add(new AIPlayer("Bot 1", new StrategyWinProbMem(Approach.RANDOM, 10)));
        players.add(new AIPlayer("Bot 2", new StrategyWin(Approach.MIN_POINTS)));
        players.add(new AIPlayer("Bot 3", new StrategyWinProbMem(Approach.MIN_POINTS, 100)));
      }
      case 3 -> {
        players.add(new AIPlayer("Bot 1", new StrategyWinProbMem(Approach.MAX_TRUMP, 100)));
        players.add(new AIPlayer("Bot 2", new StrategyWinProb(Approach.MIN_POINTS)));
        players.add(new AIPlayer("Bot 3", new StrategyWinProb(Approach.MAX_TRUMP)));
      }
    }

    return players;
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    MainController o = (MainController) other;
    return this.game.equals(o.game) && this.display.equals(o.display);
  }

  @Override
  public int hashCode() {
    return Objects.hash(game, display);
  }
}
