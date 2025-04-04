package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import game.deck.IDeck;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.deck.card.SCard;
import game.rends.REnd;
import game.rends.REndState;
import game.rends.NBCall;
import game.score.ScoreSheet;
import game.observer.EventType;
import game.observer.Observer;
import player.IPlayer;
import utility.Utility;

/**
 * A standard game of Blues with 2 to 4 players.
 */
public class StandardGame implements IGame {
  private final IDeck deck;
  private final ArrayList<IPlayer> players;
  private ArrayList<Card> well;
  private ArrayList<Card> pond;
  private ScoreSheet scoreSheet;
  private int totalGamePoints;
  private Optional<REndState> rendState;
  private IPlayer gameWinner;
  private List<Observer> observers;
  private boolean bluesBroken; // TODO: implement
  public int turn;

  /**
   * Constructs a new game with the passed players.
   */
  public StandardGame(List<IPlayer> players, IDeck deck) {
    this.deck = deck;
    deck.shuffle();
    this.players = new ArrayList<>(players);
    this.well = new ArrayList<>();
    this.pond = new ArrayList<>();
    this.scoreSheet = new ScoreSheet(this.players);
    this.totalGamePoints = 0;
    this.rendState = Optional.empty();
    this.gameWinner = null;
    this.turn = 0;
    this.observers = new ArrayList<>();
    for (IPlayer p : players) {
      observers.add(p);
      if (p.getDisplay() != null) observers.add(p.getDisplay());
    }
  }

  //************************************************************************************ SELF MODIFY
  @Override
  public void startRound() {
    resetNewRound();
    this.turn = 0;

    for (int i = 0; i < 5; i++) {
      for (IPlayer p : players) {
        p.dealCard(deck.popCard());
      }
    }

    for (IPlayer p : players) {
      if (Utility.isBlues(p.getHand())) {
        rendState = Optional.of(new REndState(REnd.BLUES, p));
        bluesEnd(); // TODO: all ends need to update observers
      }
    }
  }

  @Override
  public void flipWell() {
    turn += 1;
    if (!well.isEmpty()) {
      throw new IllegalStateException("Cannot flip new well if existing well isn't empty");
    }
    for (int i = 0; i < 4; i++) {
      Card toAdd = deck.popCard();
      well.add(toAdd);
      toAdd.setPosition(Position.WELL);
    }
  }

  @Override
  public void flipPond() {
    if (pond.size() != 4) {
      throw new IllegalStateException("Pond must have 4 cards");
    }
    for (IPlayer p : players) {
      Card c = p.getPondCard().get();
      c.setPosition(Position.POND_F);
      updateObservers(EventType.PLAYER_DISCARD, List.of(p, c, well));
    }
    updateObservers(EventType.POND_FLIPPED, List.of());
  }

  @Override
  public void resetNewRound() {
    rendState = Optional.empty();
    deck.resetDeck();
    deck.shuffle();
    clearWater();

    for (IPlayer p : players) {
      p.resetNewRound();
    }
  }

  @Override
  public void resetNewGame() {
    for (IPlayer p : players) {
      p.resetNewGame();
    }
    totalGamePoints = 0;
    scoreSheet = new ScoreSheet(players);
    rendState = Optional.empty();
    gameWinner = null;
    deck.resetDeck();
    deck.shuffle();
    clearWater();
  }

  //*********************************************************************** SELF +/ PLAYER(S) MODIFY
  @Override
  public void collectPond() {
    if (well.size() != 4) {
      throw new IllegalStateException("Cannot collect pond because well doesn't have 4 cards");
    }
    if (!pond.isEmpty()) {
      throw new IllegalStateException("Cannot collect pond because it isn't empty");
    }
    for (IPlayer p : players) {
      Card c = p.discard(well);
      p.setPondCard(c);
      p.throwCard(c);
      c.setPosition(Position.POND_H);
      pond.add(c);
    }
  }

  @Override
  public Optional<NBCall> collectNBCs() {
    if (well.size() != 4 || pond.size() != 4) {
      System.out.println(players.size());
      System.out.println("Well: " + well);
      System.out.println("Pond: " + pond);
      for (IPlayer p : players) {
        System.out.println(p.name() + "'s hand: " + p.getHand());
      }
      throw new IllegalStateException("Cannot ask players for 'No Blues' calls if well and pond are"
              + " not full\n");
    }
    List<NBCall> calls = new ArrayList<>();
    List<IPlayer> callers = new ArrayList<>();
    NBCall call;

    for (IPlayer p : players) {
      ArrayList<IPlayer> withoutP = new ArrayList<>(players);
      withoutP.remove(p);
      Optional<IPlayer> receiver = p.callNo(withoutP, well);
      if (receiver.isPresent()) {
        calls.add(new NBCall(p, receiver.get()));
        callers.add(p);
      }
    }

    if (calls.isEmpty()) return Optional.empty();
    if (calls.size() == 1) call = calls.getFirst();
    else call = calls.get(callers.indexOf(breakTie(callers)));

    flipPond();
    if (Utility.isNoBlues(pond, well, call.receiver().getHand())) {
      rendState = Optional.of(new REndState(REnd.TRUE_NO, call, call.caller()));
      trueNoEnd();
    } else {
      rendState = Optional.of(new REndState(REnd.FALSE_NO, call, call.receiver()));
      falseNoEnd();
    }

    return Optional.of(call);
  }

  @Override
  public void allowChoices() {
    if (pond.size() != 4 || well.size() != 4) {
      throw new IllegalStateException("Pond and well must have 4 cards each");
    }
    for (Card c : pond) {
      if (!c.getPosition().equals(Position.POND_F)) {
        throw new IllegalStateException("Pond hasn't been flipped");
      }
    }

    List<IPlayer> order = Utility.playerChoiceOrder(pond, players);

    for (int i = 0; i < order.size(); i++) {
      if (i == 2 && !well.isEmpty()) { // clear well for 3rd and 4th trump
        List<Object> data = new ArrayList<>(well);
        updateObservers(EventType.CARDS_CLEARED, data);
        for (Card c : well) {
          c.setPosition(Position.DISCARDED);
        }
        well = new ArrayList<>();
      }

      IPlayer p = order.get(i);
      Card c = p.chooseCard(pond, well);
      String location = "pond";
      if (well.contains(c)) location = "well";
      handleSelection(p, c);

      if (i != order.size() - 1) {
        updateObservers(EventType.PLAYER_CHOICE, List.of(p, c, location, well, pond,
                order.get(i + 1)));
      } else updateObservers(EventType.PLAYER_CHOICE, List.of(p, c, location, well, pond));

      if (Utility.isBlues(p.getHand())) {
        rendState = Optional.of(new REndState(REnd.BLUES, p));
        bluesEnd();
        return;
      }
    }
    List<Object> data = new ArrayList<>(well);
    clearWater();
    // TODO: figure this out, maybe should be included in clear water?
    for (IPlayer p : players) {
      p.setPondCard();
    }
    updateObservers(EventType.CARDS_CLEARED, data);
    updateObservers(EventType.TURN_OVER, List.of());

    if (deck.getCards().size() < 4) {
      deckEmptyEnd(); // rendState set in this method
    }
  }

  //**************************************************************************************** GETTERS
  @Override
  public boolean gameOver() {
    return totalGamePoints >= players.size() * 100;
  }

  @Override
  public boolean roundOver() {
    return rendState.isPresent();
  }

  @Override
  public List<Card> getPond() {
    return pond;
  }

  @Override
  public List<Card> getWell() {
    return well;
  }

  @Override
  public IDeck getDeck() {
    return deck;
  }

  @Override
  public List<IPlayer> getPlayers() { return players; }

  @Override
  public int gamePoints() {
    return totalGamePoints;
  }

  @Override
  public Optional<REndState> getRendState() {
    return rendState;
  }

  @Override
  public ScoreSheet getScoreSheet() {
    return scoreSheet;
  }

  @Override
  public IPlayer getGameWinner() {
    return gameWinner;
  }

  //************************************************************************************** OBSERVERS
  // TODO: javadoc
  // Pull method to IGame interface?
  public void addObserver(Observer o) {
    observers.add(o);
  }

  // TODO: javadoc
  // Pull method to IGame interface?
  public void updateObservers(EventType event, List<Object> data) {
    for (Observer o : observers) o.update(event, data);
  }

  //**************************************************************************************** ENDINGS
  /**
   * Called when a player has won by calling blues. Tallies points accordingly: the caller gains
   *   their blues hand points, other players gain their hand points. Adds round to scoreSheet and
   *   calls 'setGameWinner' if 'gameOver' returns true. DOESN'T clear table or change player hands.
   * @throws IllegalArgumentException if 'rendState' isn't present or its end isn't REnd.BLUES
   */
  private void bluesEnd() {
    if (rendState.isEmpty() || rendState.get().getEnd() != REnd.BLUES) {
      throw new IllegalArgumentException("Call to 'bluesEnd' without REnd.BLUES");
    }
    returnPond();
    int[] deltas = new int[players.size() + 1];

    for (int i = 0; i < players.size(); i++) {
      int points;
      IPlayer p = players.get(i);
      if (!p.equals(rendState.get().getWinner())) {
        points = Utility.points(p.getHand());
      } else {
        points = Utility.bluesPoints(p.getHand());
      }
      p.addPoints(points);
      totalGamePoints += points;
      deltas[i] = points;
    }
    deltas[players.size()] = Arrays.stream(deltas).sum();

    finishRound(deltas);
  }

  /**
   * Called when a player has won by calling no blues. Tallies points accordingly: the caller gains
   *   -25 points, the receiver gains 25 points, and the other two players gain their hand points.
   *   Adds round to scoreSheet and calls 'setGameWinner' if 'gameOver' returns true. DOESN'T clear
   *   table or change player hands.
   * @throws IllegalArgumentException if 'rendState' isn't present or its end isn't REnd.TRUE_NO
   */
  private void trueNoEnd() {
    if (rendState.isEmpty() || rendState.get().getEnd() != REnd.TRUE_NO) {
      throw new IllegalArgumentException("Call to 'trueNoEnd' without REnd.TRUE_NO");
    }
    int[] deltas = new int[players.size() + 1];
    Card fifth = Utility.noBlues5thCard(pond, well, rendState.get().getNbc().receiver().getHand());

    for (int i = 0; i < players.size(); i++) {
      IPlayer p = players.get(i);
      int points;
      if (p.equals(rendState.get().getNbc().caller())) {
        points = Utility.PERFECT;
      } else if (p.equals(rendState.get().getNbc().receiver())) {
        points = Utility.PUNISHMENT;
      } else {
        Card pondCard = null;
        if (p.getPondCard().isPresent() && p.getHand().size() == 4
                && !(p.getPondCard().get().equals(fifth))) {
          pondCard = p.getPondCard().get();
          p.dealCard(p.getPondCard().get());
        }
        points = Utility.points(p.getHand());
        if (pondCard != null) {
          p.throwCard(pondCard);
          p.setPondCard(pondCard);
          pondCard.setPosition(Position.POND_F);
        }
      }
      totalGamePoints += points;
      p.addPoints(points);
      deltas[i] = points;
    }
    deltas[players.size()] = Arrays.stream(deltas).sum();
    finishRound(deltas);
  }

  /**
   * Handles false no blues calls. Tallies points accordingly: the caller gains 25 points, all other
   *   players gain 0. Adds round to scoreSheet and calls 'setGameWinner' if 'gameOver' returns
   *   true. DOESN'T clear table or change player hands.
   * @throws IllegalArgumentException if 'rendState' isn't present or its end isn't REnd.FALSE_NO
   */
  private void falseNoEnd() {
    if (rendState.isEmpty() || rendState.get().getEnd() != REnd.FALSE_NO) {
      throw new IllegalArgumentException("Call to 'falseNoEnd' without REnd.FALSE_NO");
    }
    int[] deltas = new int[players.size() + 1];

    for (int i = 0; i < players.size(); i++) {
      int points = 0;
      if (players.get(i).equals(rendState.get().getNbc().caller())) {
        points = Utility.PUNISHMENT;
      }
      players.get(i).addPoints(points);
      totalGamePoints += points;
      deltas[i] = points;
    }
    deltas[players.size()] = Arrays.stream(deltas).sum();
    finishRound(deltas);
  }

  /**
   * Handles a deck empty ending, which occurs when there are 3 or fewer cards in the deck when
   *   'flipWell' is called. Tallies points accordingly: all players gain their hand points. Adds
   *   round to scoreSheet and calls 'setGameWinner' if 'gameOver' returns true. DOESN'T clear table
   *   or change player hands.
   */
  private void deckEmptyEnd() {
    returnPond();
    int[] deltas = new int[players.size() + 1];
    IPlayer lowest = null;
    List<IPlayer> tied = new ArrayList<>();

    for (int i = 0; i < players.size(); i++) {
      IPlayer p = players.get(i);
      int points = Utility.points(p.getHand());
      p.addPoints(points);
      totalGamePoints += points;
      deltas[i] = points;

      if (lowest == null) {
        if (tied.isEmpty()) {
          lowest = p;
        } else {
          if (p.getPoints() < tied.getFirst().getPoints()) {
            lowest = p;
            tied = new ArrayList<>();
          } else if (p.getPoints() == tied.getFirst().getPoints()) {
            tied.add(p);
          }
        }
      } else {
        if (p.getPoints() < lowest.getPoints()) {
          lowest = p;
        } else if (p.getPoints() == lowest.getPoints()) {
          tied.add(lowest);
          tied.add(p);
          lowest = null;
        }
      }
    }
    if (lowest == null) {
      lowest = breakTie(tied);
    }
    rendState = Optional.of(new REndState(REnd.DECK_EMPTY, lowest));
    deltas[players.size()] = Arrays.stream(deltas).sum();
    finishRound(deltas);
  }

  //**************************************************************************************** HELPERS
  /**
   * Gets chosen card from player and deals them the card, reassigns its position to
   *     Position.DISCARDED, and removes it from the well/pond.
   * @param p player
   */
  private void handleSelection(IPlayer p, Card c) {
    p.dealCard(c);                        // gives player chosen card
    well.remove(c);                       // removes chosen card from well/pond
    pond.remove(c);

    for (IPlayer pl : players) {
      if (pl.getPondCard().isPresent()) {
        if (pl.getPondCard().get().getPosition().equals(Position.HAND)) {
          pl.setPondCard();
        }
      }
    }
  }

  /**
   * Returns pond cards to losing players' hands after a correct blues/no blues call so points can
   *     be tallied.
   */
  private void returnPond() {
    for (IPlayer p : players) {

      if (p.getHand().size() == 4 && p.getPondCard().isPresent()
              && (p.getPondCard().get().getPosition() == Position.POND_F
              || p.getPondCard().get().getPosition() == Position.POND_H)) {
        pond.remove(p.getPondCard().get());
        p.dealCard(p.getPondCard().get());
      }
    }
  }

  /**
   * Sets pond and well equal to empty lists and sets their former cards' Positions to DISCARDED.
   */
  private void clearWater() {
    List<Card> pondAndWell = new ArrayList<>(pond);
    pondAndWell.addAll(well);
    for (Card c : pondAndWell) {
      c.setPosition(Position.DISCARDED);
    }
    pond = new ArrayList<>();
    well = new ArrayList<>();
  }

  /**
   * Returns the player with the best card in their hand (i.e. a card that trumps ALL the other
   *   players' cards). The best card may be a player's pond card.
   * @param tied the tied players
   * @return the player with the best card in their hand
   */
  private IPlayer breakTie(List<IPlayer> tied) {
    ArrayList<Card> bestCards = new ArrayList<>();
    Card best;

    for (IPlayer p : tied) {
      if (p.getPondCard().isPresent()) {
        best = p.getPondCard().get();
      } else {
        best = new Card(SCard.STAR_7); // the worst card in the deck
      }
      for (Card c : p.getHand()) {
        if (Utility.trumps(c, best)) {
          best = c;
        }
      }
      bestCards.add(best);
    }

    best = bestCards.getFirst();
    for (int i = 1; i < bestCards.size(); i++) {
      if (Utility.trumps(bestCards.get(i), best)) best = bestCards.get(i);
    }

    return tied.get(bestCards.indexOf(best));
  }

  /**
   * Adds this round to the score sheet using the passed deltas and field 'rendState' and calls
   *   'setGameWinner' if the game is over.
   * @param deltas player deltas and game delta for this round
   */
  private void finishRound(int[] deltas) {
    // rendState is always present because method is only called by end handlers
    scoreSheet.addRound(deltas, rendState.get());
    updateObservers(EventType.ROUND_OVER, List.of(rendState.get()));
    if (gameOver()) {
      setGameWinner();
      updateObservers(EventType.GAME_OVER, List.of(gameWinner, gameWinner.getPoints()));

    }
  }

  /**
   * Changes this game's 'gameWinner' field to the player with the least total points. If multiple
   *   players are tied for least points, the player who most recently won a round is the winner.
   */
  private void setGameWinner() {
    if (!gameOver()) {
      throw new IllegalStateException("Game isn't over");
    }
    List<IPlayer> sorted = new ArrayList<>(players);
    sorted.sort(Comparator.comparingInt(IPlayer::getPoints));

    List<IPlayer> tied = new ArrayList<>();
    for (int i = 1; i < players.size(); i++) {
      if (sorted.get(i).getPoints() == sorted.getFirst().getPoints()) {
        if (tied.isEmpty()) tied.add(sorted.getFirst());
        tied.add(sorted.get(i));
      }
    }

    if (tied.isEmpty()) {
      gameWinner = sorted.getFirst();
      return;
    } else { // tie goes to player who won a round most recently
      for (int i = scoreSheet.getCurrentRound() - 1; i > 0; i--) {
        IPlayer winner = scoreSheet.getRound(i).getWinner();
        for (IPlayer p : tied) {
          if (winner.equals(p)) {
            gameWinner = p;
            return;
          }
        }
      }
    }

    for (int i = scoreSheet.getCurrentRound() - 1; i > 0; i--) {
      for (IPlayer p1 : tied) {
        int hasLessPtsThan = 0;
        for (IPlayer p2 : tied) {
          if (p1.equals(p2)) continue;
          if (scoreSheet.getRound(i).getTotals()[players.indexOf(p1)]
                  < scoreSheet.getRound(i).getTotals()[players.indexOf(p2)]) {
            hasLessPtsThan++;
          }
        }
        if (hasLessPtsThan == players.size() - 1) {
          gameWinner = p1;
          return;
        }
      }
    }

    if (gameWinner == null) {
      throw new IllegalStateException("Set game winner unsuccessful");
    }
  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return scoreSheet.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    StandardGame o = (StandardGame) other;
    return this.gameOver() == o.gameOver() && this.roundOver() == o.roundOver()
            && this.getPond().equals(o.getPond()) && this.getWell().equals(o.getWell())
            && this.getDeck().equals(o.getDeck()) && this.getPlayers().equals(o.getPlayers())
            && this.gamePoints() == o.gamePoints() && this.getRendState().equals(o.getRendState())
            && this.getScoreSheet().equals(o.getScoreSheet());
  }

  @Override
  public int hashCode() {
    return Objects.hash(deck, players, well, pond, scoreSheet, totalGamePoints, rendState);
  }
}
