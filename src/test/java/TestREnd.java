import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

import game.rends.NBCall;
import game.rends.REnd;
import game.rends.REndState;
import player.IPlayer;
import player.TstPlayer;

public class TestREnd {
  NBCall nbc;
  IPlayer p1;
  IPlayer p2;
  REndState bluesRend;
  REndState noBluesRend;
  REndState falseNoRend;
  REndState emptyDeckRend;

  @Test
  public void testNBCall() {
    initTests();
    Assert.assertEquals(p1, nbc.caller());
    Assert.assertEquals(p2, nbc.receiver());

    Assert.assertEquals(nbc.toString(), "Player 1 called No Blues on Player 2");
    Assert.assertEquals(nbc, new NBCall(p1, p2));
    Assert.assertEquals(nbc.hashCode(), Objects.hash(p1, p2));
    Assert.assertNotEquals(nbc, new NBCall(p2, p1));
    Assert.assertNotEquals(new Object(), nbc);
  }

  @Test
  public void testBadRendStateConstruction() {
    // Blues/empty deck REndState without REnd.BLUES or REnd.DECK_EMPTY
    Assert.assertThrows(IllegalArgumentException.class, () -> new REndState(REnd.TRUE_NO, p1));
    Assert.assertThrows(IllegalArgumentException.class,
            () -> new REndState(REnd.FALSE_NO, p2));

    // No Blues REndState without REnd.NO or REnd.FALSE_NO
    Assert.assertThrows(IllegalArgumentException.class, () -> new REndState(REnd.BLUES, nbc, p1));
    Assert.assertThrows(IllegalArgumentException.class,
            () -> new REndState(REnd.DECK_EMPTY, nbc, p2));
  }

  @Test
  public void testRendStateMethods() {
    initTests();

    // Method calls for REnd.NO REndState
    Assert.assertEquals(noBluesRend.getEnd(), REnd.TRUE_NO);
    Assert.assertEquals(noBluesRend.getWinner(), p1);
    Assert.assertEquals(noBluesRend.getNbc(), nbc);

    // Method calls for REnd.FALSE_NO REndState
    Assert.assertEquals(falseNoRend.getEnd(), REnd.FALSE_NO);
    Assert.assertEquals(falseNoRend.getNbc(), nbc);
    Assert.assertEquals(falseNoRend.getWinner(), p2);

    // Method calls for REnd.DECK_EMPTY REndState
    Assert.assertEquals(emptyDeckRend.getEnd(), REnd.DECK_EMPTY);
    Assert.assertEquals(emptyDeckRend.getWinner(), p1);
    Assert.assertThrows(IllegalStateException.class, () -> emptyDeckRend.getNbc());

    // Method calls for REnd.BLUES REndState
    Assert.assertEquals(bluesRend.getEnd(), REnd.BLUES);
    Assert.assertEquals(bluesRend.getWinner(), p1);
    Assert.assertThrows(IllegalStateException.class, () -> bluesRend.getNbc());
  }

  @Test
  public void testRendGCOs() {
    initTests();
    Assert.assertEquals(bluesRend.getEnd().toString(), "BLUES");
    Assert.assertEquals(REnd.TRUE_NO.toString(), "TRUE_NO");

    Assert.assertEquals(bluesRend.toString(), "Round ended with a Blues call by Player 1");
    Assert.assertEquals(noBluesRend.toString(), "Round ended with a correct No Blues call"
            + " by Player 1 on Player 2");
    Assert.assertEquals(falseNoRend.toString(), "Round ended with an incorrect No Blues "
            + "call by Player 1 on Player 2");
    Assert.assertEquals(emptyDeckRend.toString(),"Round ended because the deck was empty");

    Assert.assertNotEquals(bluesRend, noBluesRend);
    Assert.assertNotEquals(new Object(), falseNoRend);
    Assert.assertEquals(emptyDeckRend, new REndState(REnd.DECK_EMPTY, p1));
    Assert.assertEquals(noBluesRend, new REndState(REnd.TRUE_NO, nbc, p1));
    Assert.assertEquals(falseNoRend.hashCode(), new REndState(REnd.FALSE_NO, nbc, p2).hashCode());
    Assert.assertEquals(noBluesRend.hashCode(), Objects.hash(noBluesRend.getEnd(),
            noBluesRend.getWinner(), noBluesRend.getNbc()));
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    p1 = new TstPlayer("Player 1");
    p2 = new TstPlayer("Player 2");
    nbc = new NBCall(p1, p2);
    bluesRend = new REndState(REnd.BLUES, p1);
    emptyDeckRend = new REndState(REnd.DECK_EMPTY, p1);
    noBluesRend = new REndState(REnd.TRUE_NO, nbc, p1);
    falseNoRend = new REndState(REnd.FALSE_NO, nbc, p2);
  }
}
