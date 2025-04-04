import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import display.IDisplay;
import display.TerminalDisplay;
import game.StandardGame;
import game.deck.DeckType;
import game.deck.TypeDeck;
import game.deck.card.Card;
import player.HPlayer;
import player.IPlayer;

public class TestAPlayer {
  IPlayer hPlayerN; // named HPlayer ("HPlayer")
  IPlayer hPlayerNn; // no name HPlayer ("New player")
  IPlayer aiPlayerN; // named AIPlayer ("AIPlayer"), Approach.RANDOM
  IPlayer aiPlayerNn; // no name AIPlayer ("New player"), Approach.RANDOM
  List<IPlayer> players;

  @Test
  public void testConstructors() {
    initTests();
    List<String> names = List.of("HPlayer", "New player", "AIPlayer", "New player");

    for (int i = 0; i < players.size(); i++) {
      IPlayer p = players.get(i);
      Assert.assertEquals(names.get(i), p.name());
      Assert.assertEquals(p.getHand(), new ArrayList<>());
      Assert.assertEquals(p.getPoints(), 0);
      Assert.assertEquals(p.getPondCard(), Optional.empty());
      Assert.assertNull(p.getDisplay());
    }
  }

  @Test
  public void testGameModifiers() {
    initTests();
    for (IPlayer p : players) {
      p.dealCard(Z.fiveHeart);
      p.dealCard(Z.sixStar);
      Assert.assertTrue(p.getHand().contains(Z.fiveHeart));
      Assert.assertFalse(p.getHand().contains(Z.sevenBlue));
      Assert.assertThrows(IllegalArgumentException.class, () -> p.throwCard(Z.sevenBlue));

      p.throwCard(Z.fiveHeart);
      Assert.assertEquals(p.getHand().size(), 1);
      Assert.assertTrue(p.getHand().contains(Z.sixStar));
      p.resetNewRound();
      Assert.assertEquals(p.getHand().size(), 0);
      Assert.assertFalse(p.getHand().contains(Z.sixStar));

      Assert.assertEquals(p.getPoints(), 0);
      p.addPoints(10);
      Assert.assertEquals(p.getPoints(), 10);
      p.resetNewGame();
      Assert.assertEquals(p.getPoints(), 0);
    }
  }

  @Test
  public void testSettersGetters() { // test getDisplay() with equals() once it's implemented
    initTests();

    for (int i = 0; i < players.size(); i++) {
      IPlayer p = players.get(i);
      Assert.assertNull(p.getDisplay());
      p.editName("New name");
      Assert.assertEquals(p.name(), "New name");

      IDisplay display = new TerminalDisplay(new StandardGame(players,
              new TypeDeck(DeckType.STANDARD)), players);
      p.setDisplay(display);
      Assert.assertNotNull(p.getDisplay());

      Assert.assertEquals(p.getPondCard(), Optional.empty());
      p.setPondCard(Z.fourDash);
      Assert.assertEquals(p.getPondCard(), Optional.of(Z.fourDash));
      p.setPondCard();
      Assert.assertEquals(p.getPondCard(), Optional.empty());

      for (Card c : Z.disjoint2R1A2R) { // fourBlue, fourCross, fourHeart, fourStar, fiveHeart
        p.dealCard(c);
      }
      Assert.assertEquals(p.getHand(), Z.disjoint2R1A2R);
    }
  }

  @Test
  public void testAPlayerGCOs() {
    initTests();
    ArrayList<Integer> equalsCount = new ArrayList<>();
    for (IPlayer p : players) {
      Assert.assertEquals(p.toString(), p.name() + " (total points: " + p.getPoints()
              + ")");
      Assert.assertNotEquals(null, p);
      Assert.assertNotEquals(new Object(), p);
      //Assert.assertNotEquals(new AIPlayer("Test", new PStrategy(Approach.RANDOM, Memory.NONE, 0,
      //        false, false)), p);

      int equals = 0;
      for (IPlayer o : players) {
        if (p.equals(o)) {
          equals++;
          Assert.assertEquals(p.hashCode(), o.hashCode());
        }
      }
      equalsCount.add(equals);
    }
    for (int i : equalsCount) {
      Assert.assertEquals(i, 1);
    }

    Assert.assertFalse(aiPlayerNn.equals(Optional.of(aiPlayerNn)));
    Assert.assertFalse(hPlayerNn.equals(new Object()));
    // Assert.assertFalse(hPlayerN.equals(new AIPlayer(new PStrategy(Approach.RANDOM, Memory.NONE, 0,
    //         false, false))));
    Assert.assertFalse(aiPlayerN.equals(new HPlayer("AIPlayer")));
    // TODO: should work once strategy is fixed
    // Assert.assertTrue(aiPlayerN.equals(new AIPlayer("AIPlayer",
    //        new PStrategy(Approach.MIN_POINTS, Memory.NONE, 0, false, false))));
    // Assert.assertTrue(aiPlayerNn.equals(new AIPlayer(new PStrategy(Approach.RANDOM, Memory.NONE, 0,
    //        false, false))));
    Assert.assertTrue(hPlayerN.equals(new HPlayer("HPlayer")));
    Assert.assertTrue(hPlayerNn.equals(new HPlayer()));
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    hPlayerN = new HPlayer("HPlayer");
    hPlayerNn = new HPlayer();
    // aiPlayerN = new AIPlayer("AIPlayer", new PStrategy(Approach.RANDOM, Memory.NONE, 0,
    //        false, false));
    // aiPlayerNn = new AIPlayer(new PStrategy(Approach.RANDOM, Memory.NONE, 0,false, false));
    players = List.of(hPlayerN, hPlayerNn, aiPlayerN, aiPlayerNn);
  }
}
