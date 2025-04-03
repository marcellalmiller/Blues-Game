import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import display.StringDisplay;
import game.IGame;
import game.StandardGame;
import game.deck.TstDeck;
import game.deck.card.Card;
import player.IPlayer;
import player.TstPlayer;
import utility.Utility;

/**
 * These tests will not run unless private helper resetScanner() call is un-commented out in
 *   StringDisplay class!
 */
public class TestStringDisplay {
  IPlayer p1;
  IPlayer p2;
  IPlayer p3;
  IPlayer p4;
  List<IPlayer> fourPs;
  IGame g;
  StringDisplay d;

  InputStream ogIn;
  PrintStream ogOut;
  ByteArrayOutputStream output;

  @Test
  public void testConstructor() {
    initTests();

    Assert.assertNull(p1.getDisplay());
    d = new StringDisplay(g, fourPs);
    Assert.assertEquals(p1.getDisplay(), d);
  }

  @Test
  public void testStringDisplayGCOs() {
    initTests();
    IPlayer p1b = new TstPlayer("Player 1");
    IPlayer p2b = new TstPlayer("Player 2");
    IPlayer p3b = new TstPlayer("Player 3");
    IPlayer p4b = new TstPlayer("Player 4");
    List<IPlayer> fourPsb = List.of(p1b, p2b, p3b, p4b);
    IGame gb = new StandardGame(fourPsb, new TstDeck());

    Z.setupCase1(p1, p2, p3, p4);
    Z.setupCase1(p1b, p2b, p3b, p4b);
    g.startRound();
    gb.startRound();

    d = new StringDisplay(g, fourPs);
    StringDisplay db = new StringDisplay(gb, fourPsb);
    for (int i = 0; i < 3; i++) {
      turn(g);
      turn(gb);
    }

    Assert.assertEquals(d.toString(), db.toString());
    Assert.assertEquals(d, db);
    Assert.assertEquals(d.hashCode(), db.hashCode());
    Z.setupCase2(p1, p2, p3, p4);
    g.startRound();
    Assert.assertFalse(db.equals(d));
    Assert.assertNotEquals(d.hashCode(), db.hashCode());
    Assert.assertFalse(d.equals(new Object()));
    Assert.assertFalse(db.equals(new StringDisplay(g, fourPs)));
  }

  //**************************************************************************** TEST RENDER METHODS
  @Test
  public void testRenderWelcome() {
    initREndTests();
    initOutIn("");
    d.renderWelcome();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            
            Welcome to a new game of Blues!
            You have 3 opponents: Player 2, Player 3, and Player 4
            To see the command menu, enter 'help'
            
            """);
  }

  @Test
  public void testRenderTable() {
    initREndTests();
    Z.setupCase1(p1, p2, p3, p4);
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯  ▯  ▯  ▯
                              \s
                     ▯               ▯
                     ▯               ▯
            Player 2 ▯               ▯ Player 4
                     ▯               ▯
                     ▯               ▯
                              \s
                       \033[36m1♦\033[0m \033[37m2~\033[0m \033[37m3⬯\033[0m \033[36m5♦\033[0m\
             \033[37m6~\033[0m
                       Player 1
            
            """);

    initOutIn("");
    g.flipWell();
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯  ▯  ▯  ▯
                              \s
                     ▯               ▯
                     ▯     \033[37m7⬯\033[0m \033[37m1⬡\033[0m     ▯
            Player 2 ▯               ▯ Player 4
                     ▯     \033[37m2⬡\033[0m \033[37m3⬡\033[0m     ▯
                     ▯               ▯
                              \s
                       \033[36m1♦\033[0m \033[37m2~\033[0m \033[37m3⬯\033[0m \033[36m5♦\033[0m\
             \033[37m6~\033[0m
                       Player 1
            
            """);

    initOutIn("");
    g.collectPond();
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯     ▯  ▯
                             ▯\s
                     ▯               ▯
                     ▯     \033[37m7⬯\033[0m \033[37m1⬡\033[0m     ▯
            Player 2   ▯           ▯   Player 4
                     ▯     \033[37m2⬡\033[0m \033[37m3⬡\033[0m     ▯
                     ▯               ▯
                             ▯\s
                       \033[36m1♦\033[0m \033[37m2~\033[0m    \033[37m3⬯\033[0m \033[36m5♦\033[0m
                       Player 1
            
            """);

    initOutIn("");
    g.collectNBCs();
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯     ▯  ▯
                             ▯\s
                     ▯               ▯
                     ▯     \033[37m7⬯\033[0m \033[37m1⬡\033[0m     ▯
            Player 2   ▯           ▯   Player 4
                     ▯     \033[37m2⬡\033[0m \033[37m3⬡\033[0m     ▯
                     ▯               ▯
                             ▯\s
                       \033[36m1♦\033[0m \033[37m2~\033[0m    \033[37m3⬯\033[0m \033[36m5♦\033[0m
                       Player 1
            
            """);

    initOutIn("");
    g.flipPond();
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯     ▯  ▯
                             \033[37m5⬯\033[0m
                     ▯               ▯
                     ▯     \033[37m7⬯\033[0m \033[37m1⬡\033[0m     ▯
            Player 2  \033[37m3~\033[0m           \033[36m4♦\033[0m  Player 4
                     ▯     \033[37m2⬡\033[0m \033[37m3⬡\033[0m     ▯
                     ▯               ▯
                             \033[37m6~\033[0m
                       \033[36m1♦\033[0m \033[37m2~\033[0m    \033[37m3⬯\033[0m \033[36m5♦\033[0m
                       Player 1
            
            """);

    initOutIn("");
    g.allowChoices();
    d.renderTable();
    resetOutIn();
    Assert.assertEquals(output.toString(), """
                       Player 3
                       ▯  ▯  ▯  ▯  ▯
                              \s
                     ▯               ▯
                     ▯               ▯
            Player 2 ▯               ▯ Player 4
                     ▯               ▯
                     ▯               ▯
                              \s
                       \033[36m1♦\033[0m \033[37m2~\033[0m \033[37m3⬯\033[0m \033[36m5♦\033[0m\
             \033[37m5⬯\033[0m
                       Player 1
            
            """);
  }

  @Test
  public void testRenderRoundOverBluesCases() {
    //* displayed player wins round
    initREndTests();
    Z.setupCase1(p1, p2, p3, p4);
    for (int i = 0; i < 3; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... VICTORY!
            You called 'Blues' and won the round with this hand: \033[36m1♦\033[0m\
             \033[37m2~\033[0m \033[37m3⬯\033[0m \033[37m4≷\033[0m \033[37m5⬯\033[0m\s
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |-5    -5  |+4     4  |+3     3  |+6     6  ||+8     8  |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);

    //* displayed player opponent wins round
    initREndTests();
    Z.setupCase2(p1, p2, p3, p4);
    for (int i = 0; i < 3; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... DEFEAT.
            Player 3 called 'Blues' and won the round with this hand: \033[37m1⬯\033[0m\
             \033[37m2⬯\033[0m \033[37m3~\033[0m \033[37m4~\033[0m \033[36m5♦\033[0m\s
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+3     3  |+6     6  |-5    -5  |+6     6  ||+10    10 |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);
  }

  @Test
  public void testRenderRoundOverTrueNBCases() {
    //* displayed player correctly calls on opponent
    initREndTests();
    Z.setupCase3(p1, p2, p3, p4);
    for (int i = 0; i < 2; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... SUCCESS!
            You successfully called 'No Blues' on Player 4, who could've won the round
            by taking the \033[36m4♦\033[0m from the pond and adding it to this hand:\
             \033[37m1~\033[0m \033[37m2⬯\033[0m \033[37m3⬡\033[0m \033[37m5~\033[0m\s
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |-25   -25 |+3     3  |+3     3  |+25    25 ||+6     6  |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);

    //* opponent correctly calls on displayed player
    initREndTests();
    Z.setupCase4(p1, p2, p3, p4);
    for (int i = 0; i < 2; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... Victory DENIED!
            Player 4 successfully called 'No Blues' on you, who could've won the round
            by taking the \033[37m4⬡\033[0m from the well and adding it to this hand:\
             \033[36m1♦\033[0m \033[37m2~\033[0m \033[37m3⬯\033[0m \033[37m5⬯\033[0m\s
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+25    25 |+3     3  |+3     3  |-25   -25 ||+6     6  |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);

    //* opponent correctly calls on opponent
    initREndTests();
    Z.setupCase5(p1, p2, p3, p4);
    for (int i = 0; i < 2; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... Caught in the crossfire!
            Player 2 successfully called 'No Blues' on Player 4, who could've won the round
            by taking the \033[36m4♦\033[0m from the pond and adding it to this hand:\
             \033[37m1~\033[0m \033[37m2⬯\033[0m \033[37m3⬡\033[0m \033[37m5~\033[0m\s
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+3     3  |-25   -25 |+3     3  |+25    25 ||+6     6  |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);
  }

  @Test
  public void testRenderRoundOverFalseNBCases() {
    //* displayed player incorrectly calls on opponent
    initREndTests();
    Z.setupCase6(p1, p2, p3, p4);
    for (int i = 0; i < 2; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... FAILURE!
            You incorrectly called 'No Blues' on Player 3.
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+25    25 |+0     0  |+0     0  |+0     0  ||+25    25 |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);

    //* opponent incorrectly calls on displayed player
    initREndTests();
    Z.setupCase7(p1, p2, p3, p4);
    for (int i = 0; i < 4; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... Was it luck or deception?
            Player 2 incorrectly called 'No Blues' on you.
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+0     0  |+25    25 |+0     0  |+0     0  ||+25    25 |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);

    //* opponent incorrectly calls on opponent
    initREndTests();
    Z.setupCase8(p1, p2, p3, p4);
    for (int i = 0; i < 2; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Round over... Fate is on your side!
            Player 3 incorrectly called 'No Blues' on Player 2.
            
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |+0     0  |+0     0  |+25    25 |+0     0  ||+25    25 |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            """);
  }

  @Test
  public void testRenderRoundOverDeckEmptyCase() {
    initREndTests();
    Z.setupCase9(p1, p2, p3, p4);
    for (int i = 1; i < 10; i++) turn(g);
    d.renderRoundOver();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
                    Round over... There aren't enough cards left in the deck to refill the well.
                    All players gain the points in their current hand.
                    
                    _________________________________________________________________
                    |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
                    |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
                    |-------|----------|----------|----------|----------||----------|
                    |1      |+23    23 |+13    13 |+14    14 |+10    10 ||+60    60 |
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                    """);
  }

  @Test
  public void testRenderGameOver() {
    //* displayed player wins game
    initREndTests();
    Z.setupCase8(p1, p2, p3, p4); // +25 to p3, +0 to others = 25 total
    for (int i = 0; i < 2; i++) turn(g);
    Z.setupCase7(p1, p2, p3, p4); // +25 to p2, +0 to others = 50 total
    g.startRound();
    for (int i = 0; i < 4; i++) turn(g);
    Z.setupCase3(p1, p2, p3, p4); // -25 to p1, +3 to p2, +3 to p3, +25 to p4 = 56 total
    g.startRound();
    for (int i = 0; i < 2; i++) turn(g);
    Z.setupCase9(p1, p2, p3, p4); // +23 to p1, +13 to p2, +14 to p3, +10 to p4 = 116 total
    g.startRound();
    for (int i = 1; i < 10; i++) turn(g);
    d.renderGameOver();
    resetOutIn();

    Assert.assertTrue(g.gameOver());
    Assert.assertEquals(output.toString(), """
            Game over... CONGRATULATIONS! :D
            You won the game with -2 points.
            """);

    //* opponent wins game
    initREndTests();
    Z.setupCase9(p1, p2, p3, p4);
    for (int i = 1; i < 10; i++) turn(g);
    Z.setupCase6(p1, p2, p3, p4);
    g.startRound();
    for (int i = 0; i < 2; i++) turn(g);
    Z.setupCase7(p1, p2, p3, p4);
    g.startRound();
    for (int i = 0; i < 4; i++) turn(g);
    d.renderGameOver();
    resetOutIn();

    Assert.assertTrue(g.gameOver());
    Assert.assertEquals(output.toString(), """
            Game over... BETTER LUCK NEXT TIME :(
            Player 4 won the game with 10 points.
            """);
  }

  //******************************************************************************* TEST ASK METHODS
  @Test
  public void testAskDiscard() {
    initAskTests();
    initOutIn("99\nRaining\nD\n");
    Card c = d.askDiscard();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Choose a card to discard from your hand by entering its corresponding letter
            A: \033[36m1♦\033[0m
            B: \033[37m2~\033[0m
            C: \033[37m3⬯\033[0m
            D: \033[36m5♦\033[0m
            E: \033[37m6~\033[0m
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            """);
    Assert.assertEquals(c, Z.fiveBlue);
  }

  @Test
  public void testAskChoose() {
    //* askChoose() from only pond
    initAskTests();
    g.collectPond();
    // manually removing cards to simulate first three player choices (aligns with setupBluesRend)
    g.getWell().remove(3); // player 4 choice
    g.getPond().remove(3); // player 2 choice
    g.getPond().remove(1); // player 3 choice
    // manually removing cards to simulate well cleared
    g.getWell().remove(2);
    g.getWell().remove(1);
    g.getWell().remove(0);
    initOutIn("Blue\nE\nHello world!\n\nB\n");
    Card c = d.askChoose();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Choose a card to take from the pond by entering its corresponding letter
            POND CARDS
            A: \033[37m6~\033[0m
            B: \033[37m5⬯\033[0m
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            """);
    Assert.assertEquals(c, Z.fiveDrop);

    //* askChoose() from pond and well
    initAskTests();
    g.collectPond();
    // manually removing card to simulate first player choice (doesn't align w/ setupBluesRend)
    g.getPond().remove(3);
    initOutIn("N\n\nGo\ng   \n");
    Card c1 = d.askChoose();
    resetOutIn();

    Assert.assertEquals(output.toString(), """
            Choose a card to take from the pond or the well by entering its corresponding letter
            POND CARDS
            A: \033[37m6~\033[0m
            B: \033[37m3~\033[0m
            C: \033[37m5⬯\033[0m
            WELL CARDS
            D: \033[37m7⬯\033[0m
            E: \033[37m1⬡\033[0m
            F: \033[37m2⬡\033[0m
            G: \033[37m3⬡\033[0m
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            Your entry must be one of the corresponding letters listed:
            """);
    Assert.assertEquals(c1, Z.threeHex);
  }

  @Test
  public void testAskCallDecline() {
    //* player declines call
    initAskTests();
    initOutIn("XXXXX\n\nYo\nN   \n");
    Optional<IPlayer> p = d.askCall();
    resetOutIn();

    Assert.assertEquals(p, Optional.empty());
    Assert.assertEquals(output.toString(),
            """
                    Would you like to call 'No Blues' on an opponent?
                    Enter 'N' to pass or enter the intended receiver's corresponding letter
                    X: Player 2
                    Y: Player 3
                    Z: Player 4
                    Your entry must be one of the corresponding letters listed:
                    Your entry must be one of the corresponding letters listed:
                    Your entry must be one of the corresponding letters listed:
                    """);

    //* player accepts call
    initAskTests();
    initOutIn("XXXXX\n\nYo\nz   \n");
    Optional<IPlayer> p1 = d.askCall();
    resetOutIn();

    Assert.assertEquals(p1, Optional.of(p4));
    Assert.assertEquals(output.toString(),
            """
                    Would you like to call 'No Blues' on an opponent?
                    Enter 'N' to pass or enter the intended receiver's corresponding letter
                    X: Player 2
                    Y: Player 3
                    Z: Player 4
                    Your entry must be one of the corresponding letters listed:
                    Your entry must be one of the corresponding letters listed:
                    Your entry must be one of the corresponding letters listed:
                    """);
  }

  @Test
  public void testAskPlayAgain() {
    //* displayed player accepts
    initAskTests();
    initOutIn("A\ny \n");
    boolean askPlayAgain = d.askPlayAgain();
    resetOutIn();

    Assert.assertTrue(askPlayAgain);
    Assert.assertEquals(output.toString(), """
            Would you like to play another game?
            Enter 'Y' to accept, or 'N' to quit
            Invalid input - enter Y if you would like to play another game, N otherwise:
            """);

    //* displayed player quits

    initAskTests();
    initOutIn("A\nyes \n\nN\n");
    boolean askPlayAgain1 = d.askPlayAgain();
    resetOutIn();

    Assert.assertFalse(askPlayAgain1);
    Assert.assertEquals(output.toString(), """
            Would you like to play another game?
            Enter 'Y' to accept, or 'N' to quit
            Invalid input - enter Y if you would like to play another game, N otherwise:
            Invalid input - enter Y if you would like to play another game, N otherwise:
            Invalid input - enter Y if you would like to play another game, N otherwise:
            """);
  }

  //********************************************************************************** TEST COMMANDS
  @Test
  public void testIndividualCommands() {
    String chooseCardS = """
            Choose a card to discard from your hand by entering its corresponding letter
            A: \033[36m1♦\033[0m
            B: \033[37m2~\033[0m
            C: \033[37m3⬯\033[0m
            D: \033[36m5♦\033[0m
            E: \033[37m6~\033[0m
            """;
    for (String s : List.of("HELP", "cOmmAnds", "command     ")) {
      initAskTests();
      initOutIn("xxx\n" + s + "\nD\n");
      Card c = d.askDiscard();
      resetOutIn();

      Assert.assertEquals(c, Z.fiveBlue);
      Assert.assertEquals(output.toString(), chooseCardS + """
              Your entry must be one of the corresponding letters listed:
              
              VALID COMMANDS:
              'help' -> display valid commands
              'rules' -> display game rules
              'scores' -> display current scores
              'name' -> change your display name
              
              """ + chooseCardS);
    }

    for (String s : List.of("     RULE       ", "rules")) {
      initAskTests();
      initOutIn("Oooo\n" + s + "\n  b  \n");
      Card c = d.askDiscard();
      resetOutIn();

      Assert.assertEquals(c, Z.twoDash);
      Assert.assertEquals(output.toString(), chooseCardS + """
              Your entry must be one of the corresponding letters listed:
              
              """ + Utility.gameRules +
              """
              
              """ + chooseCardS);
    }

    for (String s : List.of(" scORES ", "score", "SCORESHEET  ", "score sheet   ")) {
      initAskTests();
      initOutIn(s + "\n000\n a ");
      Card c = d.askDiscard();
      resetOutIn();

      Assert.assertEquals(c, Z.oneBlue);
      Assert.assertEquals(output.toString(), chooseCardS + "\nNo scores to display yet!"
              + "\n\n" + chooseCardS
              + "Your entry must be one of the corresponding letters listed:\n");
    }

    for (String s : List.of("NaMe", "change NAME", "  change ", "edit name", "   EDIT   ")) {
      initAskTests();
      initOutIn(s + "\nBruhhh\n     e   \n");
      Card c = d.askDiscard();
      resetOutIn();

      Assert.assertEquals(c, Z.sixDash);
      Assert.assertEquals(p1.name(), "Bruhhh");
      Assert.assertEquals(output.toString(), chooseCardS + """
              
              Enter new display name:
              Display name is now 'Bruhhh'
              
              """ + chooseCardS);
    }
  }

  @Test
  public void testMultipleCommands() {
    String askCall = """
            Would you like to call 'No Blues' on an opponent?
            Enter 'N' to pass or enter the intended receiver's corresponding letter
            X: Player 2
            Y: Player 3
            Z: Player 4
            """;

    initREndTests();
    Z.setupCase1(p1, p2, p3, p4);
    for (int i = 0; i < 3; i++) turn(g);
    initOutIn("help\nblah\nrules\nscore\nname\nMe\nZ");
    Optional<IPlayer> p = d.askCall();
    resetOutIn();

    Assert.assertEquals(p, Optional.of(p4));
    Assert.assertEquals(p1.name(), "Me");
    Assert.assertEquals(output.toString(), askCall + """
            
            VALID COMMANDS:
            'help' -> display valid commands
            'rules' -> display game rules
            'scores' -> display current scores
            'name' -> change your display name
            
            """ + askCall + """
            Your entry must be one of the corresponding letters listed:
            
            """ + Utility.gameRules + "\n" + askCall + """
            
            CURRENT SCORES:
            _________________________________________________________________
            |ROUND  |Player 1  |Player 2  |Player 3  |Player 4  ||TOTAL     |
            |       | Δ     ∑  | Δ     ∑  | Δ     ∑  | Δ     ∑  || Δ     ∑  |
            |-------|----------|----------|----------|----------||----------|
            |1      |-5    -5  |+4     4  |+3     3  |+6     6  ||+8     8  |
            ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
            
            """ + askCall + """
            
            Enter new display name:
            Display name is now 'Me'
            
            """ + askCall);

  }

  //*********************************************************************************** INIT HELPERS
  private void initTests() {
    p1 = new TstPlayer("Player 1");
    p2 = new TstPlayer("Player 2");
    p3 = new TstPlayer("Player 3");
    p4 = new TstPlayer("Player 4");
    fourPs = List.of(p1, p2, p3, p4);
    g = new StandardGame(fourPs, new TstDeck());
  }

  private void initOutIn(String simPut) {
    ogOut = System.out;
    output = new ByteArrayOutputStream();
    PrintStream printed = new PrintStream(output);
    System.setOut(printed);

    ogIn = System.in;
    if (!Objects.equals(simPut, "")) {
      InputStream input = new ByteArrayInputStream(simPut.getBytes());
      System.setIn(input);
    }
  }

  private void resetOutIn() {
    System.setOut(ogOut);
    System.setIn(ogIn);
  }

  private void initAskTests() {
    initTests();
    Z.setupCase1(p1, p2, p3, p4);
    g.startRound();
    d = new StringDisplay(g, fourPs);
    g.flipWell();
  }

  private void initREndTests() {
    initTests();
    g.startRound();
    d = new StringDisplay(g, fourPs);
    initOutIn("");
  }

  //************************************************************************ MOCK CONTROLLER HELPERS
  /**
   * Mock controller method without calls to display. Doesn't handle round endings.
   */
  private void turn(IGame game) {
    game.flipWell();
    game.collectPond();
    game.collectNBCs();

    if (!game.roundOver()) {
      game.flipPond();
      game.allowChoices();
    }
  }
}
