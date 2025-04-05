package display;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

import game.IGame;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.observer.Observer;
import game.rends.REndState;
import game.observer.EventType;
import player.IPlayer;
import utility.Utility;

/**
 * A text display for a 4 player game of Blues. Displays by printing to a Java Swing JFrame, gets
 *   feedback with a JTextField.
 */
public class TextDisplay implements IDisplay, Observer {
  private ArrayList<IPlayer> opponents;
  private IPlayer player;
  private ArrayList<IPlayer> allPlayers;
  private IGame game;

  private JFrame frame;
  private JLabel gameText;
  private JLabel scoreText;
  private JLabel directionText;
  private JTextField inputField;

  private String userInput;

  /**
   * Creates an empty String display for a game of Blues. Fields must be set using 'setGame' before
   *   a game can be played.
   */
  public TextDisplay() {
    userInput = "";

    frame = new JFrame("BLUES");
    frame.setLayout(new BorderLayout());
    gameText = new JLabel();
    gameText.setPreferredSize(new Dimension(300, 300));

    scoreText = new JLabel();
    scoreText.setPreferredSize(new Dimension(600, 300));
    scoreText.setHorizontalAlignment(SwingConstants.CENTER);
    scoreText.setVerticalAlignment(SwingConstants.TOP);

    directionText = new JLabel();
    directionText.setPreferredSize(new Dimension(300, 300));
    directionText.setHorizontalAlignment(SwingConstants.LEFT);
    directionText.setVerticalAlignment(SwingConstants.TOP);

    inputField = new JTextField(20);
    inputField.setPreferredSize(new Dimension(150, 30));
    inputField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        userInput = inputField.getText();
        inputField.setText("");
      }
    });

    JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    tablePanel.setPreferredSize(new Dimension(400, 300));
    tablePanel.add(gameText);

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    inputPanel.setPreferredSize(new Dimension(400, 300));
    inputPanel.add(directionText);
    inputPanel.add(inputField);

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setPreferredSize(new Dimension(800, 200));
    topPanel.add(scoreText, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    bottomPanel.setPreferredSize(new Dimension(800, 300));
    bottomPanel.add(tablePanel);
    bottomPanel.add(inputPanel);

    frame.add(topPanel, BorderLayout.NORTH);
    frame.add(bottomPanel, BorderLayout.SOUTH);
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Creates a new String display for a game of Blues. Field 'player' is set equal to first element
   *     of parameter players, 'opponents' is set equal to all elements of players but the first.
   * @param game the game to display
   * @param players the players
   */
  public TextDisplay(IGame game, List<IPlayer> players) {
    setGame(game, players);
  }

  //*************************************************************************** CALLED BY CONTROLLER
  @Override
  public void setGame(IGame game, List<IPlayer> players) {
    this.game = game;
    game.addObserver(this);
    this.player = players.getFirst();
    this.allPlayers = new ArrayList<>(players);
    player.setDisplay(this);

    opponents = new ArrayList<>();
    for (int i = 1; i < players.size(); i++) {
      opponents.add(players.get(i));
    }

    scoreText.setText(game.getScoreSheet().htmlToString());
  }

  //************************************************************************************* USER INPUT
  @Override
  public int askDifficulty() {
    List<Character> charIdx = List.of('1', '2', '3');
    String ogMessage = """
            <html>Please select the game difficulty by entering its number:<br>
            <br>
            1. EASY<br>
            2. NORMAL<br>
            3. HARD<br>
            """;
    directionText.setText(ogMessage);
    char answer = getValidInput(charIdx);

    return Character.getNumericValue(answer);
  }

  @Override
  public Card askDiscard() {
    throwIfNullFields();
    scoreText.setText(game.getScoreSheet().htmlToString());

    List<Character> charIdx = List.of('A', 'B', 'C', 'D', 'E');
    String ogMessage = "<html>Choose a card to discard from your hand by entering its<br>corresponding "
            + "letter<br><br>" + cardChoiceString(player.getHand(), charIdx);
    directionText.setText(ogMessage);

    char answer = getValidInput(charIdx);
    gameText.setText(this.toString());

    return player.getHand().get(charIdx.indexOf(answer));
  }

  @Override
  public Card askChoose() {
    throwIfNullFields();
    List<Character> full = List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
    int pSize = game.getPond().size();
    List<Character> pCharIdx = full.subList(0, pSize);
    List<Character> wCharIdx = full.subList(pSize, pSize + game.getWell().size());
    StringBuilder sb = new StringBuilder();

    String water = "the pond";
    if (!game.getWell().isEmpty()) water = "the pond or the well";
    sb.append("<html>Choose a card to take from ").append(water)
            .append(" by entering<br>its corresponding letter:<br>");
    sb.append("<br>POND CARDS<br>").append(cardChoiceString(game.getPond(), pCharIdx))
            .append("<br>");
    if (!game.getWell().isEmpty()) {
      sb.append("<br>WELL CARDS<br>").append(cardChoiceString(game.getWell(), wCharIdx));
    }
    sb.append("</html>");

    String ogMessage = sb.toString();
    directionText.setText(ogMessage);
    char answer = getValidInput(full.subList(0, pSize + game.getWell().size()));

    if (pCharIdx.contains(answer)) {
      return game.getPond().get(pCharIdx.indexOf(answer));
    }
    gameText.setText(this.toString());

    return game.getWell().get(wCharIdx.indexOf(answer));
  }

  @Override
  public Optional<IPlayer> askCall() {
    throwIfNullFields();
    List<Character> validAnswers = List.of('N', 'X', 'Y', 'Z').subList(0, opponents.size() + 1);
    List<Character> charIdx = List.of('X', 'Y', 'Z').subList(0, opponents.size());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < opponents.size(); i++) {
      sb.append("<br>").append(charIdx.get(i)).append(": ").append(opponents.get(i).name());
    }
    String ogMessage = "<html>Would you like to call 'No Blues' on an opponent?<br>Enter 'N' to pass or "
            + "enter the intended receiver's corresponding letter:<br>" + sb;
    directionText.setText(ogMessage);
    char answer = getValidInput(validAnswers);

    if (answer == 'N') {
      return Optional.empty();
    }
    gameText.setText(this.toString());

    return Optional.of(opponents.get(charIdx.indexOf(answer)));
  }

  @Override
  public boolean askPlayAgain() {
    throwIfNullFields();
    String ogMessage = "<html>Would you like to play another game?<br>Enter 'Y' to accept, or 'N' "
            + "to quit</html>";
    directionText.setText(ogMessage);

    char answer = getValidInput(List.of('Y', 'N'));
    gameText.setText(this.toString());

    return answer == 'Y';
  }

  //***************************************************************************************** RENDER
  @Override
  public void renderWelcome() {
  }

  @Override
  public void renderTable() {
    throwIfNullFields();
    gameText.setText(this.toString());
  }

  @Override
  public void renderRoundOver() {
    scoreText.setText(game.getScoreSheet().htmlToString());

    throwIfNullFields();
    if (game.getRendState().isEmpty()) {
      throw new IllegalStateException("Couldn't fetch round end state");
    }
    REndState endState = game.getRendState().get();

    StringBuilder sb = new StringBuilder("<html>Round over...");
    switch (endState.getEnd()) {
      case BLUES: sb.append(rendBluesS(endState)); break;
      case TRUE_NO: sb.append(rendNoS(endState)); break;
      case FALSE_NO: sb.append(rendFalseNoS(endState)); break;
      case DECK_EMPTY: sb.append("There aren't enough cards left in the deck to refill the well<br>"
              + "All players gain the points in their current hand</html>"); break;
    }

    JOptionPane.showMessageDialog(frame, sb.toString(), "ROUND OVER", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void renderGameOver() {
    throwIfNullFields();
    if (!game.gameOver()) throw new IllegalStateException("The game isn't over");
    if (game.getGameWinner() == null) throw new IllegalStateException("Winner hasn't been set");
    IPlayer winner = game.getGameWinner();
    StringBuilder sb = new StringBuilder("Game over... ");

    if (winner.equals(player)) sb.append("CONGRATULATIONS! :D\n");
    else sb.append("BETTER LUCK NEXT TIME :(\n");
    sb.append(dName(winner, true)).append(" won the game with ").append(winner.getPoints())
            .append(" points.");

    JOptionPane.showMessageDialog(frame, sb.toString(), "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
  }

  //*************************************************************************************** OBSERVER
  @Override
  public void update(EventType event, List<Object> data) {
    throwIfNullFields();
    switch (event) {
      case ROUND_OVER -> renderRoundOver();
      case GAME_OVER -> renderGameOver();
    }
  }

  //*********************************************************************************** REND HELPERS
  /**
   * Formulates a String to display to player if the round ends with a 'Blues' call - changes based
   *   on whether the player is the winner.
   * @param rend the round's end state
   * @return the formulated String
   */
  private String rendBluesS(REndState rend) {
    StringBuilder sb = new StringBuilder();
    IPlayer w = rend.getWinner();

    if (w.equals(player)) sb.append("<html>VICTORY!<br>You");
    else sb.append("<html>DEFEAT.<br>").append(w.name());

    sb.append(" called 'Blues' and won the round with this hand: ");
    for (Card c : Utility.sortHandByRank(w.getHand())) {
      sb.append(c.htmlTS()).append(" ");
    }
    sb.append("</html>");

    return sb.toString();
  }

  /**
   * Formulates a String to display to player after the rounds ends with a correct 'No Blues' call -
   *   changes based on whether the player won, lost, or neither.
   * @param rend the round's end state
   * @return the formulated String
   */
  private String rendNoS(REndState rend) {
    StringBuilder sb = new StringBuilder();
    IPlayer w = rend.getNbc().caller();
    IPlayer l = rend.getNbc().receiver();

    if (w.equals(player)) sb.append("</html>SUCCESS!<br>");
    else if (l.equals(player)) sb.append("</html>Victory DENIED!<br>");
    else sb.append("<html>Caught in the crossfire!<br>");

    sb.append(dName(w, true)).append(" successfully called 'No Blues' on ")
            .append(dName(l, false)).append(",<br> who could've won the round")
            .append("by taking the ");

    Card fifthCard = Utility.noBlues5thCard(game.getPond(), game.getWell(), l.getHand());
    sb.append(fifthCard.htmlTS()).append("<br> from the ");
    if (game.getPond().contains(fifthCard)) sb.append("pond");
    else if (game.getWell().contains(fifthCard)) sb.append("well");
    else {
      throw new IllegalStateException("Neither card nor well contains fifth card for REndState: '"
              + rend + "'");
    }

    sb.append(" and adding it to this hand: ");
    for (Card c : Utility.sortHandByRank(l.getHand())) {
      sb.append(c.htmlTS()).append(" ");
    }
    sb.append("</html>");

    return sb.toString();
  }

  /**
   * Formulates a String to display to player after the round ends with an incorrect 'No Blues'
   *   call - changes based on whether the player was the caller.
   * @param rend the round's end state
   * @return the formulated String
   */
  private String rendFalseNoS(REndState rend) {
    StringBuilder sb = new StringBuilder();
    IPlayer l = rend.getNbc().caller();
    IPlayer r = rend.getNbc().receiver();

    if (l.equals(player)) sb.append("<html>FAILURE!<br>");
    else if (r.equals(player)) sb.append("<html>Was it luck or deception?<br>");
    else sb.append("<html>Fate is on your side!<br>");
    sb.append(dName(l, true)).append(" incorrectly called 'No Blues' on ")
            .append(dName(r, false)).append("</html>");

    return sb.toString();
  }

  //********************************************************************************* FORMAT HELPERS
  /**
   * Returns a String obtained by placing each element in a list of cards next to a corresponding
   *   char so player can choose a card by entering a letter into the terminal. Created to format
   *   the pond, well, or player hand.
   * @param cards the cards to format
   * @param charIdx the corresponding chars
   * @return the formatted list of cards
   */
  private String cardChoiceString(List<Card> cards, List<Character> charIdx) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < cards.size(); i++) {
      sb.append(charIdx.get(i)).append(": ").append(cards.get(i).htmlTS());
      if (i != cards.size() - 1) sb.append("<br>");
    }

    return sb.toString();
  }

  /**
   * Returns a String with s spaces.
   * @param s the amount of spaces
   * @return a String with s spaces
   */
  private String spaces(int s) {
    return " ".repeat(Math.max(0, s));
  }

  /**
   * Returns the name a player should be referred to with in messages. If p equals this display's
   *   player, returns "you" (capitalized if caps is true). Returns p.name() otherwise and value of
   *   caps is ignored.
   * @param p the player
   * @param caps true if "you" should be capitalized
   * @return the String p should be referred to with in messages
   */
  private String dName(IPlayer p, boolean caps) {
    if (p.equals(player)) {
      if (caps) return "You";
      else return "you";
    }
    return p.name();
  }

  //*************************************************************************** PLAYER INPUT HELPERS
  /**
   * Returns input from player (expected input is a char). Repeats this loop until valid input is
   *   given: gets input from player via scanner and passes it to 'handleCommand' - if
   *   'handleCommand' returns true (meaning the input was a valid command), prints parameter
   *   'ogMessage' and restarts the loop - if not, checks if parameter 'validAnswers' contains
   *   their choice - if it does, breaks loop and returns choice, if not, prints parameter
   *   'invalidMessage' and continues loop.
   * @param validAns valid answers a player could give
   */
  private char getValidInput(List<Character> validAns) {
    char answer = 'Q';

    while (answer == 'Q') {
      if (!userInput.isEmpty()) {
        answer = Character.toUpperCase(userInput.charAt(0));

        if (validAns.contains(answer)) break;
        else answer = 'Q';
      }

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    userInput = "";

    return answer;
  }

  //****************************************************************************** TO STRING HELPERS
  /**
   * Returns this player's hand and name formatted for inclusion in this display.
   * @param leftSpaces spaces on the left - equal to the length of opponents[1]'s name
   * @return a formatted String with this player's hand and name
   */
  private String playerHandString(String leftSpaces) {
    StringBuilder sb = new StringBuilder();
    List<Card> ph = player.getHand();
    String[] hs = new String[5];

    if (ph.size() < 4) for (int i = 0; i < 5; i++) hs[i] = "X ";
    if (ph.size() > 3) {
      hs[0] = ph.get(0).htmlTS();
      hs[1] = ph.get(1).htmlTS();
      if (ph.size() == 4) {
        hs[2] = "  "; // empty slot
        hs[3] = ph.get(2).htmlTS();
        hs[4] = ph.get(3).htmlTS();
      } else {
        hs[2] = ph.get(2).htmlTS();
        hs[3] = ph.get(3).htmlTS();
        hs[4] = ph.get(4).htmlTS();
      }
    }

    sb.append(leftSpaces).append("  ").append(hs[0]).append(" ").append(hs[1]).append(" ")
            .append(hs[2]).append(" ").append(hs[3]).append(" ").append(hs[4]).append("\n");
    sb.append(leftSpaces).append("  ").append(player.name()).append("\n");
    return sb.toString();
  }

  /**
   * Returns an array of Strings formatted for inclusion in this display - the string at index 'i'
   *   in this array corresponds to the pond card at index 'i' in game's pond list. Each card's
   *   corresponding string is: an empty slot (two spaces) if the pond is empty or if the card was
   *   chosen; a rectangle character if the pond has been placed but not flipped; the card's
   *   'coloredTS' return value if the pond has been placed and flipped and the card hasn't been
   *   chosen.
   * @return an array of Strings that correspond to pond cards formatted for this display
   */
  private String[] pondStrings() {
    String[] pondStrings = new String[4];
    for (int i = 0; i < allPlayers.size(); i++) {
      IPlayer p = allPlayers.get(i);
      if (p.getPondCard().isPresent()) {
        Card c = p.getPondCard().get();
        if (c.getPosition().equals(Position.POND_F)) {
          if (i != 1) {
            pondStrings[i] = c.htmlTS() + " ";
          } else pondStrings[i] = " " + c.htmlTS();
        } else {
          pondStrings[i] = " ▯ ";
        }
      } else {
        pondStrings[i] = "   "; // empty slot
      }
    }
    return pondStrings;
  }

  /**
   * Returns an array of Strings formatted for inclusion in this display - the string at index 'i'
   *   in this array corresponds to the well card at index 'i' in game's well list. Each card's
   *   corresponding string is: an empty slot (two spaces) if the well is empty or if the card was
   *   chosen; the card's 'coloredTS' return value if the well isn't empty and the card hasn't been
   *   chosen.
   * @return an array of Strings that correspond to well cards formatted for this display
   */
  private String[] wellStrings() {
    String[] wellStrings = new String[4];
    for (int i = 0; i < 4; i++) {
      if (i + 1 <= game.getWell().size()) {
        wellStrings[i] = game.getWell().get(i).htmlTS();
      } else {
        wellStrings[i] = "  "; // empty slot
      }
    }
    return wellStrings;
  }

  /**
   * Returns an array of Strings formatted to appear where each opponent's third card would be if
   *   they had five cards in their hand - the string at index 'i' in this array corresponds to the
   *   opponent at index 'i' in 'opponents'. Each opponent's corresponding string is: a rectangle
   *   character if they have five cards; an empty slot (two spaces) if they have four cards (i.e.,
   *   one of their cards is in the pond). This ensures that the empty space from a missing card in
   *   a four-card hand always appears in the middle of a player's hand regardless of what index
   *   they discarded.
   * @return an array of Strings formatted to appear where each opponent's third card would be if
   *         they had five cards in their hand
   */
  private String[] middleStrings() {
    String[] middleStrings = new String[3];
    for (int i = 0; i < opponents.size(); i++) {
      if (opponents.get(i).getHand().size() == 5) {
        middleStrings[i] = "▯";
      } else {
        middleStrings[i] = " ";
      }
    }
    return middleStrings;
  }

  //****************************************************************************** EXCEPTION HELPERS
  private void throwIfNullFields() {
    if (opponents == null || player == null || allPlayers == null || game == null) {
      throw new IllegalStateException("Assign a game and players using 'setGame'!");
    }
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String leftSpaces = " ".repeat(Math.max(0, opponents.get(1).name().length() + 1));
    String middleSpaces = "               ";

    String[] ps = pondStrings();
    String[] ws = wellStrings();
    String[] ms = middleStrings();

    sb.append("<html><pre>").append(leftSpaces).append("  ").append(opponents.get(1).name())
            .append("<br>");
    sb.append(leftSpaces).append("  ").append("▯  ▯  ").append(ms[1]).append("  ▯  ▯<br>");
    sb.append(leftSpaces).append(spaces(7)).append(ps[2]).append("<br>");
    sb.append(leftSpaces).append("▯").append(middleSpaces).append("▯").append("<br>");
    sb.append(leftSpaces).append("▯     ").append(ws[0]).append(" ").append(ws[1]).append("     ▯")
            .append("<br>");
    sb.append(opponents.get(0).name()).append(" ").append(ms[0]).append(ps[1]).append(spaces(9))
            .append(ps[3]).append(ms[2]).append(" ");
    sb.append(opponents.get(2).name()).append("<br>");
    sb.append(leftSpaces).append("▯     ").append(ws[2]).append(" ").append(ws[3]).append("     ▯")
            .append("<br>");
    sb.append(leftSpaces).append("▯").append(middleSpaces).append("▯").append("<br>");
    sb.append(leftSpaces).append(spaces(7)).append(ps[0]).append("<br>");
    sb.append(playerHandString(leftSpaces)).append("</pre></html>");

    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TerminalDisplay o = (TerminalDisplay) other;
    return o.toString().equals(toString());
  }

  /**
   * Doesn't hash fields because it creates an infinite loop - APlayer's hashCode() method calls
   *   TerminalDisplay's hashCode() method which calls APlayer's hashCode() method, etc. etc.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
