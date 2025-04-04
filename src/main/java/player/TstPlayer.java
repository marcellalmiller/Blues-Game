package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import game.observer.EventType;

/**
 * FOR TESTING PURPOSES ONLY. Overrode methods' return values (action values) are set by additional
 *   public methods.
 */
public class TstPlayer extends APlayer {
  public Card discard;
  public Card chooseCard;
  public Optional<IPlayer> callNo;
  public boolean auto;
  public List<Card> willDiscard;
  public List<Card> willChoose;
  public List<Optional<IPlayer>> willCall;
  public int idx;
  public List<Integer> all3;
  public List<EventType> events;
  public List<List<Object>> datas;
  public List<String> dataStrings;

  /**
   * Creates a new TstPlayer. Because no action lists are passed, auto is set to false, but will be
   *   changed if any methods to set action lists are called ('setWillDiscard', 'setWillChoose', or
   *   'setWillCall').
   * @param name this player's name
   */
  public TstPlayer(String name) {
    super(name);
    this.discard = null;
    this.chooseCard = null;
    this.callNo = Optional.empty();
    this.auto = false;
    resetObsObservers();
  }

  /**
   * Creates a new TstPlayer. Three lists are passed whose values are returned by 'discard',
   *   'chooseCard' and 'callNo', respectively. Auto is set to true, but will be changed if any
   *   methods to set individual return values are called ('setDiscard', 'setChooseCard', or
   *   'setCallNo').
   * @param name this player's name
   * @param willDiscard the cards this player will discard
   * @param willChoose the cards this player will choose
   * @param willCall the players this player will call No Blues on
   */
  public TstPlayer(String name, List<Card> willDiscard, List<Card> willChoose,
                   List<Optional<IPlayer>> willCall) {
    super(name);
    this.auto = true;
    this.willDiscard = willDiscard;
    this.willChoose = willChoose;
    this.willCall = willCall;
    this.idx = 0;
    this.all3 = new ArrayList<>();
    resetObsObservers();
  }

  //************************************************************************************** OVERRIDES
  /**
   *
   * @param well the four well cards to consider
   * @return the 'Card' to discard
   */
  @Override
  public Card discard(List<Card> well) {
    if (auto) {
      Card c = willDiscard.get(idx);
      updateAll3();
      return c;
    }

    for (Card c : this.getHand()) {
      if (c.equals(discard)) {
        return c;
      }
    }
    throw new IllegalStateException("No hand cards match the set discard card");
  }

  /**
   *
   * @param pond the remaining pond cards
   * @param well the remaining well cards
   * @return the Card to choose
   */
  @Override
  public Card chooseCard(List<Card> pond, List<Card> well) {
    if (auto) {
      Card c = willChoose.get(idx);
      updateAll3();
      return c;
    }

    List<Card> choices = new ArrayList<>(pond);
    choices.addAll(well);
    for (Card c : choices) {
      if (c.equals(chooseCard)) {
        return c;
      }
    }
    throw new IllegalStateException("No well or pond cards match the set chooseCard card");
  }

  /**
   *
   * @param opponents the opponents
   * @param well the current well
   * @return an opposing 'IPlayer' if this thinks that opponent is about to win, empty otherwise
   */
  @Override
  public Optional<IPlayer> callNo(List<IPlayer> opponents, List<Card> well) {
    if (auto) {
      Optional<IPlayer> op = willCall.get(idx);
      updateAll3();
      return op;
    }

    return callNo;
  }

  //*************************************************************************************** OBSERVER
  /**
   * Called by instances of 'IGame' to notify 'Observer's of game events. This method passes its
   *   arguments to 'dataListify' so they can be stored for testing purposes.
   * @param event the EventType
   * @param data the event's data
   */
  @Override
  public void update(EventType event, List<Object> data) {
    dataListify(event, data);
  }

  //**************************************************************************************** SETTERS
  /**
   * Manually sets this player's 'auto' field.
   * @param auto new value for this player's 'auto' field
   */
  public void setAuto(boolean auto) {
    this.auto = auto;
  }

  //*********************************************************************************** AUTO = FALSE
  /**
   * Sets the next return value of 'discard'. Calling this method sets 'auto' to false.
   * @param c the next return value of 'discard'
   */
  public void setDiscard(Card c) {
    auto = false;
    this.discard = c;
  }

  /**
   * Sets the next return value of 'chooseCard'. Calling this method sets 'auto' to false.
   * @param c the next return value of 'chooseCard'
   */
  public void setChooseCard(Card c) {
    auto = false;
    this.chooseCard = c;
  }

  /**
   * Sets the next return value of 'callNo'. Calling this method sets 'auto' to false.
   * @param p the next return value of 'callNo'
   */
  public void setCallNo(Optional<IPlayer> p) {
    auto = false;
    this.callNo = p;
  }

  //************************************************************************************ AUTO = TRUE
  /**
   * Sets the return values of 'discard' in bulk. Values of argument 'willDiscard' are returned by
   *   'discard' as long as 'auto' is true. Calling this method sets 'auto' to true.
   * @param willDiscard
   */
  public void setWillDiscard(List<Card> willDiscard) {
    auto = true;
    this.willDiscard = willDiscard;
    idx = 0;
    all3 = new ArrayList<>();
  }

  /**
   * Sets the return values of 'chooseCard' in bulk. Values of argument 'willChoose' are returned by
   *   'chooseCard' as long as 'auto' is true. Calling this method sets 'auto' to true.
   * @param willChoose
   */
  public void setWillChoose(List<Card> willChoose) {
    auto = true;
    this.willChoose = willChoose;
    idx = 0;
    all3 = new ArrayList<>();
  }

  /**
   * Sets the return values of 'callNo' in bulk. Values of argument 'willCall' are returned by
   *   'callNo' as long as 'auto' is true. Calling this method sets 'auto' to true.
   * @param willCall
   */
  public void setWillCall(List<Optional<IPlayer>> willCall) {
    auto = true;
    this.willCall = willCall;
    idx = 0;
    all3 = new ArrayList<>();
  }

  //**************************************************************************************** HELPERS
  /**
   * Iterates field 'idx' if it's been used to access return value lists three times. Assumes that
   *   overrode methods are called repeatedly in this order: 'discard', 'chooseCard', 'callNo'.
   */
  private void updateAll3() {
    all3.add(idx);
    if (all3.size() == 3) {
      idx++;
      all3 = new ArrayList<>();
    }
    if (all3.size() > 3) {
      throw new IllegalStateException("all3 is not working");
    }
  }

  /**
   * Resets fields that record notifications received by the 'update' method inherited from the
   *   'Observer' interface.
   */
  private void resetObsObservers() {
    dataStrings = new ArrayList<>();
    datas = new ArrayList<>();
    events = new ArrayList<>();
  }

  /**
   * Converts the notifications received by the 'update' method inherited from the 'Observer'
   *   interface to Strings and stores them in field 'dataStrings' for testing purposes. Also stores
   *   arguments 'events' and 'data'.
   * @param event the EventType
   * @param data the event's data
   */
  private void dataListify(EventType event, List<Object> data) {
    events.add(event);
    datas.add(data);

    switch (event) {
      case PLAYER_CHOICE -> {
        StringBuilder sb = new StringBuilder(((IPlayer) data.getFirst()).name() + " chose the " +
                ((Card) data.get(1)).ansiTS() + " from the " +  data.get(2));
        sb.append(", 4th list index: ");
        if (data.size() == 3) sb.append("empty");
        else sb.append(((IPlayer) data.get(3)).name());
        dataStrings.add(sb.toString());
      }

      case PLAYER_DISCARD -> dataStrings.add(((IPlayer) data.getFirst()).name()+ " threw the "
              + ((Card) data.get(1)).ansiTS() + " into the pond");

      case CARDS_CLEARED -> {
        StringBuilder sb = new StringBuilder();
        if (data.isEmpty()) break;
        for (Object o : data) {
          Card c = (Card) o;
          sb.append(c.ansiTS()).append(" ");
        }
        String verb = "were";
        if (data.size() == 1) verb = "was";
        dataStrings.add(sb + verb + " thrown into the sea");
      }

      case WELL_FLIPPED -> dataStrings.add("Well flipped");

      case POND_FLIPPED -> dataStrings.add("Pond flipped");

      case TURN_OVER -> dataStrings.add("Turn over");

      case ROUND_OVER -> dataStrings.add((data.getFirst().toString()));

      case GAME_OVER -> dataStrings.add(((IPlayer) data.getFirst()).name() + " won the game with "
              + data.get(1) + " points");
    }
  }
}
