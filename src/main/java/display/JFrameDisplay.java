package display;

import java.util.List;
import java.util.Optional;

import game.deck.card.Card;
import game.observer.EventType;
import game.observer.Observer;
import player.IPlayer;

// TODO: implement
public class JFrameDisplay implements IDisplay, Observer {
  public JFrameDisplay() {

  }

  //************************************************************************************* USER INPUT
  @Override
  public Card askDiscard() {
    return null;
  }

  @Override
  public Card askChoose() {
    return null;
  }

  @Override
  public Optional<IPlayer> askCall() {
    return Optional.empty();
  }

  @Override
  public boolean askPlayAgain() {
    return false;
  }

  //***************************************************************************************** RENDER
  @Override
  public void renderWelcome() {

  }

  @Override
  public void renderTable() {

  }

  @Override
  public void renderRoundOver() {

  }

  @Override
  public void renderGameOver() {

  }

  //*************************************************************************************** OBSERVER
  @Override
  public void update(EventType event, List<Object> data) {

  }

  //*************************************************************************** GOOD CLASS OVERRIDES
  @Override
  public String toString() {
    return null;
  }

  @Override
  public boolean equals(Object other) {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
