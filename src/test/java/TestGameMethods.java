import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.IGame;
import game.StandardGame;
import game.deck.TstDeck;
import game.deck.card.Card;
import game.deck.card.properties.Position;
import game.rends.NBCall;
import game.rends.REnd;
import game.rends.REndState;
import game.score.ScoreSheet;
import player.IPlayer;
import player.TstPlayer;

public class TestGameMethods {
  IPlayer p1;
  IPlayer p2;
  IPlayer p3;
  IPlayer p4;
  List<IPlayer> players;
  IGame game;

  @Test
  public void testConstructor() {
    initTests();

    Assert.assertFalse(game.gameOver());
    Assert.assertFalse(game.roundOver());
    Assert.assertEquals(game.getPond(), new ArrayList<>());
    Assert.assertEquals(game.getWell(), new ArrayList<>());
    Assert.assertEquals(game.getDeck(), new TstDeck());
    Assert.assertEquals(game.getPlayers(), new ArrayList<>(players));
    Assert.assertEquals(game.gamePoints(), 0);
    Assert.assertEquals(game.getRendState(), Optional.empty());
    Assert.assertEquals(game.getScoreSheet(), new ScoreSheet(game.getPlayers()));
    Assert.assertNull(game.getGameWinner());
  }

  @Test
  public void testStartRound() {
    initTests();
    game.startRound();

    Assert.assertEquals(p1.getHand(), List.of(Z.oneBlue, Z.twoDash, Z.threeDrop, Z.fiveBlue,
            Z.sixDash));
    Assert.assertEquals(p2.getHand(), List.of(Z.twoBlue, Z.threeDash, Z.fourDrop, Z.sixBlue,
            Z.sevenDash));
    Assert.assertEquals(p3.getHand(), List.of(Z.oneDrop, Z.threeBlue, Z.fourDash, Z.fiveDrop,
            Z.sevenBlue));
    Assert.assertEquals(p4.getHand(), List.of(Z.oneDash, Z.twoDrop, Z.fourBlue, Z.fiveDash,
            Z.sixDrop));
    Assert.assertEquals(game.getDeck().getDealt().size(), 20);
    Assert.assertEquals(game.getDeck().getCards().size(), 36);

    game = new StandardGame(players, new TstDeck(Z.immediateBlues4p)); // deals p1 a winning hand
    game.startRound();
    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.getScoreSheet().getRound(1).getWinner(), p1);
  }

  @Test
  public void testFlipWell() {
    initTests();
    game.startRound();
    game.flipWell();

    Assert.assertEquals(game.getWell().size(), 4);
    Assert.assertEquals(game.getWell(), List.of(Z.sevenDrop, Z.oneHex, Z.twoHex, Z.threeHex));
    Assert.assertEquals(game.getDeck().getDealt().size(), 24);
    Assert.assertThrows(IllegalStateException.class, () -> game.flipWell());
  }

  @Test
  public void testCollectPond() {
    initTests();
    game.startRound();

    Assert.assertThrows(IllegalStateException.class, () -> game.collectPond());
    game.flipWell();
    game.collectPond();
    Assert.assertThrows(IllegalStateException.class, () -> game.collectPond());
    Assert.assertEquals(game.getPond().size(), 4);
    Assert.assertEquals(game.getPond(), List.of(Z.sixDash, Z.threeDash, Z.fiveDrop, Z.fourBlue));
    for (Card c : game.getPond()) Assert.assertEquals(c.getPosition(), Position.POND_H);

    Assert.assertEquals(p1.getHand(), List.of(Z.oneBlue, Z.twoDash, Z.threeDrop, Z.fiveBlue));
    Assert.assertEquals(p1.getPondCard(), Optional.of(Z.sixDash));
    Assert.assertEquals(p2.getHand(), List.of(Z.twoBlue, Z.fourDrop, Z.sixBlue, Z.sevenDash));
    Assert.assertEquals(p2.getPondCard(), Optional.of(Z.threeDash));
    Assert.assertEquals(p3.getHand(), List.of(Z.oneDrop, Z.threeBlue, Z.fourDash, Z.sevenBlue));
    Assert.assertEquals(p3.getPondCard(), Optional.of(Z.fiveDrop));
    Assert.assertEquals(p4.getHand(), List.of(Z.oneDash, Z.twoDrop, Z.fiveDash, Z.sixDrop));
    Assert.assertEquals(p4.getPondCard(), Optional.of(Z.fourBlue));
  }

  @Test
  public void testCollectNBCs() {
    initTests(); // all players pass
    game.startRound();
    Assert.assertThrows(IllegalStateException.class, () -> game.collectNBCs());
    game.flipWell();
    Assert.assertThrows(IllegalStateException.class, () -> game.collectNBCs());
    game.collectPond();
    Assert.assertEquals(game.collectNBCs(), Optional.empty());

    initTests(); // one player calls
    Z.setupCase4(p1, p2, p3, p4);
    game.startRound();
    turn();
    game.flipWell();
    game.collectPond();
    Assert.assertEquals(game.collectNBCs(), Optional.of(new NBCall(p4, p1)));
    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.TRUE_NO, new NBCall(p4, p1), p4)));
    for (Card c : game.getPond()) Assert.assertEquals(c.getPosition(), Position.POND_F);

    initTests(); // multiple players call
    Z.setupCase7(p1, p2, p3, p4);
    game.startRound();
    for (int i = 0; i < 3; i++) turn();
    game.flipWell();
    game.collectPond();
    Assert.assertEquals(game.collectNBCs(), Optional.of(new NBCall(p2, p1)));
    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.getRendState(),
            Optional.of(new REndState(REnd.FALSE_NO, new NBCall(p2, p1), p1)));
  }

  @Test
  public void testFlipPond() {
    initTests();
    game.startRound();
    game.flipWell();

    Assert.assertThrows(IllegalStateException.class, () -> game.flipPond());
    game.collectPond();
    game.collectNBCs();
    game.flipPond();
    for (Card c : game.getPond()) Assert.assertEquals(c.getPosition(), Position.POND_F);
  }

  @Test
  public void testAllowChoices() {
    initTests(); // allow choices doesn't end round
    game.startRound();
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());
    game.flipWell();
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());
    game.collectPond();
    game.collectNBCs();
    Assert.assertThrows(IllegalStateException.class, () -> game.allowChoices());
    game.flipPond();
    game.allowChoices();

    Assert.assertTrue(game.getPond().isEmpty());
    Assert.assertTrue(game.getWell().isEmpty());
    for (IPlayer p : players) {
      Assert.assertEquals(p.getPondCard(), Optional.empty());
      Assert.assertEquals(p.getHand().size(), 5);
    }
    Assert.assertEquals(p1.getHand(), List.of(Z.oneBlue, Z.twoDash, Z.threeDrop, Z.fiveBlue,
            Z.fiveDrop));
    Assert.assertEquals(p2.getHand(), List.of(Z.twoBlue, Z.fourBlue, Z.fourDrop, Z.sixBlue,
            Z.sevenDash));
    Assert.assertEquals(p3.getHand(), List.of(Z.oneDrop, Z.threeBlue, Z.threeDash, Z.fourDash,
            Z.sevenBlue));
    Assert.assertEquals(p4.getHand(), List.of(Z.oneDash, Z.twoDrop, Z.threeHex, Z.fiveDash,
            Z.sixDrop));

    initTests(); // allow choices ends round
    game.startRound();
    for (int i = 0; i < 3; i++) turn();
    Assert.assertTrue(game.roundOver());
    Assert.assertFalse(game.getPond().isEmpty());
    Assert.assertFalse(game.getWell().isEmpty());

    Assert.assertEquals(p1.getHand(), List.of(Z.oneBlue, Z.twoDash, Z.threeDrop, Z.fourBolt,
            Z.fiveDrop));
    Assert.assertEquals(p2.getHand(), List.of(Z.twoBlue, Z.fiveHex, Z.sixBlue, Z.sevenDash));
    Assert.assertEquals(p3.getHand(), List.of(Z.oneDrop, Z.threeDash, Z.fourDash, Z.fourDrop,
            Z.fiveBlue));
    Assert.assertEquals(p4.getHand(), List.of(Z.oneDash, Z.twoDrop, Z.threeHex, Z.fiveDash,
            Z.sixDrop));
  }

  @Test
  public void testResetNewRound() {
    initTests();
    game.startRound();
    for (int i = 0; i < 3; i++) turn();
    int[] preReset = new int[4];
    for (int i = 0; i < 4; i++) preReset[i] = players.get(i).getPoints();

    Assert.assertTrue(game.roundOver());
    Assert.assertEquals(game.getRendState(), Optional.of(new REndState(REnd.BLUES, p1)));
    Assert.assertEquals(game.getDeck().getDealt().size(), 32);
    Assert.assertFalse(game.getPond().isEmpty());
    Assert.assertFalse(game.getWell().isEmpty());
    for (IPlayer p : players) Assert.assertFalse(p.getHand().isEmpty());

    game.resetNewRound();

    Assert.assertFalse(game.roundOver());
    Assert.assertEquals(game.getRendState(), Optional.empty());
    Assert.assertEquals(game.getDeck().getDealt().size(), 0);
    Assert.assertTrue(game.getPond().isEmpty());
    Assert.assertTrue(game.getWell().isEmpty());
    for (IPlayer p : players) Assert.assertTrue(p.getHand().isEmpty());
    for (int i = 0; i < 4; i++) Assert.assertEquals(players.get(i).getPoints(), preReset[i]);
  }

  @Test
  public void testResetNewGame() {
    initTests();
    Z.setupCase3(p1, p2, p3, p4); // +6
    game.startRound();
    for (int i = 0; i < 2; i++) turn();
    Z.setupCase6(p1, p2, p3, p4); // +25 = 31
    game.startRound();
    for (int i = 0; i < 2; i++) turn();
    Z.setupCase9(p1, p2, p3, p4); // +60 = 91
    game.startRound();
    for (int i = 0; i < 9; i++) turn();
    Z.setupCase2(p1, p2, p3, p4); // +10 = 101
    game.startRound();
    for (int i = 0; i < 3; i++) turn();

    Assert.assertTrue(game.roundOver());
    Assert.assertTrue(game.gameOver());
    Assert.assertEquals(game.gamePoints(), 101);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 5);
    Assert.assertFalse(game.getRendState().isEmpty());
    Assert.assertNotNull(game.getGameWinner());
    Assert.assertFalse(game.getPond().isEmpty());
    Assert.assertFalse(game.getWell().isEmpty());

    game.resetNewGame();

    Assert.assertFalse(game.roundOver());
    Assert.assertFalse(game.gameOver());
    Assert.assertEquals(game.gamePoints(), 0);
    Assert.assertEquals(game.getScoreSheet().getCurrentRound(), 1);
    Assert.assertTrue(game.getRendState().isEmpty());
    Assert.assertNull(game.getGameWinner());
    Assert.assertTrue(game.getPond().isEmpty());
    Assert.assertTrue(game.getWell().isEmpty());
    for (IPlayer p : players) {
      Assert.assertTrue(p.getHand().isEmpty());
      Assert.assertEquals(p.getPoints(), 0);
      Assert.assertFalse(p.getPondCard().isPresent());
    }
  }

  @Test
  public void testGameGCOs() {
    initTests();
    Assert.assertTrue(game.equals(new StandardGame(players, new TstDeck())));
    Assert.assertFalse(game.equals(new Object()));
    Assert.assertEquals(game.hashCode(), game.hashCode());
    game.startRound();
    turn();
    Assert.assertFalse(game.equals(new StandardGame(players, new TstDeck())));
    Assert.assertNotEquals(game.hashCode(), new StandardGame(players, new TstDeck()).hashCode());
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    p1 = new TstPlayer("P1");
    p2 = new TstPlayer("P2");
    p3 = new TstPlayer("P3");
    p4 = new TstPlayer("P4");
    players = List.of(p1, p2, p3, p4);
    Z.setupCase1(p1, p2, p3, p4);
    game = new StandardGame(players, new TstDeck());
  }

  private void turn() {
    game.flipWell();
    game.collectPond();
    game.collectNBCs();
    if (!game.roundOver()) {
      game.flipPond();
      game.allowChoices();
    }
  }
}
