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
  // These three methods ('setDiscard', 'setChooseCard', and 'setCallNo') set overrode methods'
  // return values individually. Calling any one of them sets field 'auto' to false.

  public void setDiscard(Card c) {
    auto = false;
    this.discard = c;
  }

  public void setChooseCard(Card c) {
    auto = false;
    this.chooseCard = c;
  }

  public void setCallNo(Optional<IPlayer> p) {
    auto = false;
    this.callNo = p;
  }

  //************************************************************************************ AUTO = TRUE
  // These three methods ('setWillDiscard', 'setWillChoose', and 'setWillCall') set overrode methods'
  // return values in bulk. Calling any one of them sets field 'auto' to true.

  public void setWillDiscard(List<Card> willDiscard) {
    auto = true;
    this.willDiscard = willDiscard;
    idx = 0;
    all3 = new ArrayList<>();
  }

  public void setWillChoose(List<Card> willChoose) {
    auto = true;
    this.willChoose = willChoose;
    idx = 0;
    all3 = new ArrayList<>();
  }

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

  // TODO: javadoc
  private void resetObsObservers() {
    dataStrings = new ArrayList<>();
    datas = new ArrayList<>();
    events = new ArrayList<>();
  }

  // TODO: javadoc
  private void dataListify(EventType event, List<Object> data) {
    events.add(event);
    datas.add(data);

    switch (event) {
      case PLAYER_CHOICE -> {
        StringBuilder sb = new StringBuilder(((IPlayer) data.getFirst()).name() + " chose the " +
                ((Card) data.get(1)).coloredTS() + " from the " +  data.get(2));
        sb.append(", 4th list index: ");
        if (data.size() == 3) sb.append("empty");
        else sb.append(((IPlayer) data.get(3)).name());
        dataStrings.add(sb.toString());
      }

      case PLAYER_DISCARD -> dataStrings.add(((IPlayer) data.getFirst()).name()+ " threw the "
              + ((Card) data.get(1)).coloredTS() + " into the pond");

      case CARDS_CLEARED -> {
        StringBuilder sb = new StringBuilder();
        if (data.isEmpty()) break;
        for (Object o : data) {
          Card c = (Card) o;
          sb.append(c.coloredTS()).append(" ");
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
