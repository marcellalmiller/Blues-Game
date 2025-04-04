package display;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import game.IGame;
import game.StandardGame;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.observer.Observer;
import game.rends.REndState;
import game.observer.EventType;
import player.IPlayer;
import utility.Utility;

/**
 * A String display for a 4 player game of Blues. Displays by printing to the terminal, gets
 *   feedback with a Scanner.
 */
public class StringDisplay implements IDisplay, Observer {
  private final ArrayList<IPlayer> opponents;
  private final IPlayer player;
  private final ArrayList<IPlayer> allPlayers;
  private final IGame game;
  private Scanner scanner;

  /**
   * Creates a new String display for a game of blues. Field 'player' is set equal to first element
   *     of parameter players, 'opponents' is set equal to all elements of players but the first.
   * @param game the game to display
   * @param players the players
   */
  public StringDisplay(IGame game, List<IPlayer> players) {
    this.game = game;
    ((StandardGame) game).addObserver(this);
    this.player = players.getFirst();
    this.allPlayers = new ArrayList<>(players);
    player.setDisplay(this);
    this.scanner = new Scanner(System.in);

    opponents = new ArrayList<>();
    for (int i = 1; i < players.size(); i++) {
      opponents.add(players.get(i));
    }
  }

  //************************************************************************************* USER INPUT
  @Override
  public Card askDiscard() {
    List<Character> charIdx = List.of('A', 'B', 'C', 'D', 'E');
    String ogMessage = "Choose a card to discard from your hand by entering its corresponding "
            + "letter\n" + cardChoiceString(player.getHand(), charIdx);
    System.out.println(ogMessage);

    char answer = getValidInput(charIdx, ogMessage,
            "Your entry must be one of the corresponding letters listed:");

    return player.getHand().get(charIdx.indexOf(answer));
  }

  @Override
  public Card askChoose() {
    List<Character> full = List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
    int pSize = game.getPond().size();
    List<Character> pCharIdx = full.subList(0, pSize);
    List<Character> wCharIdx = full.subList(pSize, pSize + game.getWell().size());
    StringBuilder sb = new StringBuilder();

    String water = "the pond";
    if (!game.getWell().isEmpty()) water = "the pond or the well";
    sb.append("Choose a card to take from ").append(water)
            .append(" by entering its corresponding letter");
    sb.append("\nPOND CARDS\n").append(cardChoiceString(game.getPond(), pCharIdx));
    if (!game.getWell().isEmpty()) {
      sb.append("\nWELL CARDS\n").append(cardChoiceString(game.getWell(), wCharIdx));
    }

    String ogMessage = sb.toString();
    System.out.println(ogMessage);
    char answer = getValidInput(full.subList(0, pSize + game.getWell().size()), ogMessage,
            "Your entry must be one of the corresponding letters listed:");

    if (pCharIdx.contains(answer)) {
      return game.getPond().get(pCharIdx.indexOf(answer));
    }
    return game.getWell().get(wCharIdx.indexOf(answer));
  }

  @Override
  public Optional<IPlayer> askCall() {
    List<Character> validAnswers = List.of('N', 'X', 'Y', 'Z').subList(0, opponents.size() + 1);
    List<Character> charIdx = List.of('X', 'Y', 'Z').subList(0, opponents.size());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < opponents.size(); i++) {
      sb.append("\n").append(charIdx.get(i)).append(": ").append(opponents.get(i).name());
    }
    String ogMessage = "Would you like to call 'No Blues' on an opponent?\nEnter 'N' to pass or "
            + "enter the intended receiver's corresponding letter" + sb;
    System.out.println(ogMessage);
    char answer = getValidInput(validAnswers, ogMessage,
            "Your entry must be one of the corresponding letters listed:");

    if (answer == 'N') {
      return Optional.empty();
    }

    return Optional.of(opponents.get(charIdx.indexOf(answer)));
  }

  @Override
  public boolean askPlayAgain() {
    String ogMessage = "Would you like to play another game?\nEnter 'Y' to accept, or 'N' to quit";
    System.out.println(ogMessage);

    char answer = getValidInput(List.of('Y', 'N'), ogMessage, "Invalid input - enter "
            + "Y if you would like to play another game, N otherwise:");

    return answer == 'Y';
  }

  //***************************************************************************************** RENDER
  @Override
  public void renderWelcome() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nWelcome to a new game of Blues!\n").append("You have ").append(opponents.size());
    if (opponents.size() == 1) sb.append(" opponent: ");
    else sb.append(" opponents: ");

    for (int i = 0; i < opponents.size(); i++) {
      IPlayer p = opponents.get(i);
      sb.append(p.name());
      if (i == opponents.size() - 2) sb.append(", and ") ;
      if (i < opponents.size() - 2) sb. append(", ");
    }
    sb.append("\nTo see the command menu, enter 'help'\n");

    System.out.println(sb);
  }

  @Override
  public void renderTable() {
    System.out.println(this);
  }

  @Override
  public void renderRoundOver() {
    if (game.getRendState().isEmpty()) {
      throw new IllegalStateException("Couldn't fetch round end state");
    }
    REndState endState = game.getRendState().get();

    StringBuilder sb = new StringBuilder("Round over... ");
    switch (endState.getEnd()) {
      case BLUES: sb.append(rendBluesS(endState)); break;
      case TRUE_NO: sb.append(rendNoS(endState)); break;
      case FALSE_NO: sb.append(rendFalseNoS(endState)); break;
      case DECK_EMPTY: sb.append("There aren't enough cards left in the deck to refill the well.\n"
              + "All players gain the points in their current hand."); break;
    }

    sb.append("\n\n");
    sb.append(game);
    System.out.println(sb);
  }

  @Override
  public void renderGameOver() {
    if (!game.gameOver()) throw new IllegalStateException("The game isn't over");
    if (game.getGameWinner() == null) throw new IllegalStateException("Winner hasn't been set");
    IPlayer winner = game.getGameWinner();
    StringBuilder sb = new StringBuilder("Game over... ");

    if (winner.equals(player)) sb.append("CONGRATULATIONS! :D\n");
    else sb.append("BETTER LUCK NEXT TIME :(\n");
    sb.append(dName(winner, true)).append(" won the game with ").append(winner.getPoints())
            .append(" points.");

    System.out.println(sb);
  }

  //*************************************************************************************** OBSERVER
  @Override
  public void update(EventType event, List<Object> data) {
    switch (event) {
      case PLAYER_CHOICE -> {
        System.out.println("\033[33m" + ((IPlayer) data.getFirst()).name() + " chose " +
                ((Card) data.get(1)).coloredTS() + "\033[33m from the " + data.get(2) + "\033[0m");
        if (data.size() > 5 && data.get(5).equals(player)) renderTable();
      }
      case PLAYER_DISCARD -> System.out.println("\033[33m" + ((IPlayer) data.getFirst()).name()
              + " threw the " + ((Card) data.get(1)).coloredTS() + "\033[33m into the pond\033[0m");
      case CARDS_CLEARED -> {
        StringBuilder sb = new StringBuilder();
        if (data.isEmpty()) break;
        for (Object o : data) {
          Card c = (Card) o;
          sb.append(c.coloredTS()).append(" ");
        }
        String verb = "\033[33mwere";
        if (data.size() == 1) verb = "\033[33mwas";
        System.out.println(sb + verb + " thrown into the sea\033[0m");
      }
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

    if (w.equals(player)) sb.append("VICTORY!\nYou");
    else sb.append("DEFEAT.\n").append(w.name());

    sb.append(" called 'Blues' and won the round with this hand: ");
    for (Card c : Utility.sortHandByRank(w.getHand())) {
      sb.append(c.coloredTS()).append(" ");
    }

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

    if (w.equals(player)) sb.append("SUCCESS!\n");
    else if (l.equals(player)) sb.append("Victory DENIED!\n");
    else sb.append("Caught in the crossfire!\n");

    sb.append(dName(w, true)).append(" successfully called 'No Blues' on ")
            .append(dName(l, false)).append(", who could've won the round\n")
            .append("by taking the ");

    Card fifthCard = Utility.noBlues5thCard(game.getPond(), game.getWell(), l.getHand());
    sb.append(fifthCard.coloredTS()).append(" from the ");
    if (game.getPond().contains(fifthCard)) sb.append("pond");
    else if (game.getWell().contains(fifthCard)) sb.append("well");
    else {
      throw new IllegalStateException("Neither card nor well contains fifth card for REndState: '"
              + rend + "'");
    }

    sb.append(" and adding it to this hand: ");
    for (Card c : Utility.sortHandByRank(l.getHand())) {
      sb.append(c.coloredTS()).append(" ");
    }
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

    if (l.equals(player)) sb.append("FAILURE!\n");
    else if (r.equals(player)) sb.append("Was it luck or deception?\n");
    else sb.append("Fate is on your side!\n");
    sb.append(dName(l, true)).append(" incorrectly called 'No Blues' on ")
            .append(dName(r, false)).append(".");

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
      sb.append(charIdx.get(i)).append(": ").append(cards.get(i).coloredTS());
      if (i != cards.size() - 1) sb.append("\n");
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
   * @param ogMessage the input prompt displayed before this method was called
   * @param invalidMessage message to print if player enters an invalid answer
   */
  private char getValidInput(List<Character> validAns, String ogMessage, String invalidMessage) {
    // resetScanner();
    String nl = scanner.nextLine().trim();
    while (handleCommand(nl)) {
      System.out.println(ogMessage);
      nl = scanner.nextLine().trim();
    }
    // placeholder in case nl is empty - no callers will pass a 'validAnswers' list containing 'Q'
    char answer = 'Q';
    if (!nl.isEmpty()) answer = Character.toUpperCase(nl.toCharArray()[0]);

    while (!(validAns.contains(answer) && nl.length() == 1)) {
      boolean nlHandled = handleCommand(nl);
      if (!nlHandled) System.out.println(invalidMessage);
      else System.out.println(ogMessage);

      nl = scanner.nextLine().trim();
      answer = 'Q';
      if (!nl.isEmpty()) answer = Character.toUpperCase(nl.toCharArray()[0]);
    }

    return answer;
  }

  /**
   * Checks to see if the String is a valid command and if it isn't immediately returns false. If it
   *   is a valid command, finds and executes expected behavior and returns true. These are the four
   *   valid commands:
   *   1. 'help' - prints a list of valid commands - also accessible via 'commands' or 'command'
   *   2. 'rules' - prints the rules - also accessible via 'rule'
   *   3. 'scores' - prints current score-sheet - also accessible via 'score', 'scoresheet', or
   *      'score sheet'
   *   4. 'name' - allows player to change their display name - also accessible via 'change name',
   *      'change', 'edit name', or 'edit'
   * @param command the potential command
   * @return true if command is valid and was handled correctly, false if command is invalid
   * @throws IllegalStateException if the command is flagged as valid but not handled by switch
   *                               statement
   */
  private boolean handleCommand(String command) {
    String c = command.toLowerCase();
    if (!List.of("help", "commands", "command", "rule", "rules", "scores", "score",
                    "scoresheet", "score sheet", "change name", "name", "change", "edit name", "edit",
                    "sort", "sort hand", "hand", "order", "order hand", "quit", "q", "stop", "end")
            .contains(c)) {
      return false;
    }

    switch (c) {
      case "help", "commands", "command" -> System.out.println("""
              
              VALID COMMANDS:
              'help' -> display valid commands
              'rules' -> display game rules
              'scores' -> display current scores
              'name' -> change your display name
              """);
      case "rule", "rules" -> System.out.println("\n" + Utility.gameRules);
      case "scores", "score", "scoresheet", "score sheet" -> {
        if (game.getScoreSheet().getCurrentRound() == 1)
          System.out.println("\nNo scores to display yet!\n");
        else System.out.println("\nCURRENT SCORES:\n" + game.getScoreSheet().toString() + "\n");
      }
      case "change name", "name", "change", "edit name", "edit" -> {
        System.out.println("\nEnter new display name:");
        String newName = scanner.nextLine();
        player.editName(newName);
        System.out.println("Display name is now '" + player.name() + "'\n");
      }
      default -> throw new IllegalStateException("handleCommand didn't return false even though "
              + "entry isn't in list of valid commands");
    }

    return true;
  }

  /**
   * SOLELY FOR TESTING PURPOSES - resets the scanner so test methods can redirect system input.
   *   * If this method isn't commented out from 'getValidInput' after tests are finished, the
   *   * controller will not work!
   */
  private void resetScanner() {
    if (scanner != null) {
      scanner.close();
    }
    scanner = new Scanner(System.in);
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
      hs[0] = ph.get(0).coloredTS();
      hs[1] = ph.get(1).coloredTS();
      if (ph.size() == 4) {
        hs[2] = "  "; // empty slot
        hs[3] = ph.get(2).coloredTS();
        hs[4] = ph.get(3).coloredTS();
      } else {
        hs[2] = ph.get(2).coloredTS();
        hs[3] = ph.get(3).coloredTS();
        hs[4] = ph.get(4).coloredTS();
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
            pondStrings[i] = c.coloredTS() + " ";
          } else pondStrings[i] = " " + c.coloredTS();
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
        wellStrings[i] = game.getWell().get(i).coloredTS();
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

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String leftSpaces = " ".repeat(Math.max(0, opponents.get(1).name().length() + 1));
    String middleSpaces = "               ";

    String[] ps = pondStrings();
    String[] ws = wellStrings();
    String[] ms = middleStrings();

    sb.append(leftSpaces).append("  ").append(opponents.get(1).name()).append("\n");
    sb.append(leftSpaces).append("  ").append("▯  ▯  ").append(ms[1]).append("  ▯  ▯\n");
    sb.append(leftSpaces).append(spaces(7)).append(ps[2]).append("\n");
    sb.append(leftSpaces).append("▯").append(middleSpaces).append("▯").append("\n");
    sb.append(leftSpaces).append("▯     ").append(ws[0]).append(" ").append(ws[1]).append("     ▯")
            .append("\n");
    sb.append(opponents.get(0).name()).append(" ").append(ms[0]).append(ps[1]).append(spaces(9))
            .append(ps[3]).append(ms[2]).append(" ");
    sb.append(opponents.get(2).name()).append("\n");
    sb.append(leftSpaces).append("▯     ").append(ws[2]).append(" ").append(ws[3]).append("     ▯")
            .append("\n");
    sb.append(leftSpaces).append("▯").append(middleSpaces).append("▯").append("\n");
    sb.append(leftSpaces).append(spaces(7)).append(ps[0]).append("\n");
    sb.append(playerHandString(leftSpaces));

    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    StringDisplay o = (StringDisplay) other;
    return o.toString().equals(toString());
  }

  /**
   * Doesn't hash fields because it creates an infinite loop - APlayer's hashCode() method calls
   *   StringDisplay's hashCode() method which calls APlayer's hashCode() method, etc. etc.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
