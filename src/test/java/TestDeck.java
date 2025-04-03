import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import game.deck.DeckType;
import game.deck.IDeck;
import game.deck.TypeDeck;
import game.deck.card.Card;

public class TestDeck {
  IDeck shufStandard;
  IDeck unShufStandard;
  IDeck shufSuper;
  IDeck unShufSuper;

  @Test
  public void testStandardDeck() {
    initTests();
    ArrayList<Card> shufStandardDealt = new ArrayList<>();

    Assert.assertEquals(shufStandard.getDealt(), shufStandardDealt);
    shufStandardDealt.add(shufStandard.peekCard());
    Assert.assertEquals(shufStandard.cardsSize(), 56);
    Assert.assertNotEquals(unShufStandard.popCard(), shufStandard.popCard());
    Assert.assertEquals(55, unShufStandard.cardsSize());
    Assert.assertEquals(55, shufStandard.cardsSize());

    shufStandardDealt.add(shufStandard.peekCard());
    shufStandard.popCard();
    unShufStandard.popCard();
    shufStandardDealt.add(shufStandard.peekCard());
    shufStandard.popCard();

    Assert.assertEquals(53, shufStandard.cardsSize());
    Assert.assertEquals(54, unShufStandard.cardsSize());
    unShufStandard.popCard();
    Assert.assertEquals(53, unShufStandard.cardsSize());

    shufStandardDealt.add(shufStandard.peekCard());
    Assert.assertNotEquals(unShufStandard.popCard(), shufStandard.popCard());
    Assert.assertEquals(4, unShufStandard.dealtSize());
    Assert.assertEquals(4, shufStandard.dealtSize());
    Assert.assertEquals(shufStandard.getDealt(), shufStandardDealt);

    Assert.assertNotEquals(unShufStandard, new TypeDeck(DeckType.STANDARD));
    Assert.assertNotEquals(unShufStandard, shufStandard);
    unShufStandard.resetDeck();
    shufStandard.resetDeck();
    Assert.assertEquals(shufStandard, new TypeDeck(DeckType.STANDARD));
    Assert.assertEquals(unShufStandard, shufStandard);
    Assert.assertNotEquals(shufStandard.getDealt(), shufStandardDealt);
  }

  @Test
  public void testSuperstitiousDeck() {
    initTests();
    ArrayList<Card> shufSuperDealt = new ArrayList<>();

    Assert.assertEquals(shufSuper.getDealt(), shufSuperDealt);
    Assert.assertEquals(shufSuper.cardsSize(), 49);
    shufSuperDealt.add(shufSuper.peekCard());
    Assert.assertNotEquals(unShufSuper.popCard(), shufSuper.popCard());
    Assert.assertEquals(48, unShufSuper.cardsSize());
    Assert.assertEquals(48, shufSuper.cardsSize());

    shufSuperDealt.add(shufSuper.peekCard());
    shufSuper.popCard();
    unShufSuper.popCard();
    shufSuperDealt.add(shufSuper.peekCard());
    shufSuper.popCard();

    Assert.assertEquals(46, shufSuper.cardsSize());
    Assert.assertEquals(47, unShufSuper.cardsSize());
    unShufSuper.popCard();
    Assert.assertEquals(46, unShufSuper.cardsSize());

    shufSuperDealt.add(shufSuper.peekCard());
    Assert.assertNotEquals(unShufSuper.popCard(), shufSuper.popCard());
    Assert.assertEquals(4, unShufSuper.dealtSize());
    Assert.assertEquals(4, shufSuper.dealtSize());
    Assert.assertEquals(shufSuper.getDealt(), shufSuperDealt);

    Assert.assertNotEquals(unShufSuper, new TypeDeck(DeckType.STANDARD));
    Assert.assertNotEquals(unShufSuper, shufSuper);
    unShufSuper.resetDeck();
    shufSuper.resetDeck();
    Assert.assertEquals(shufSuper, new TypeDeck(DeckType.SUPERSTITIOUS));
    Assert.assertEquals(unShufSuper, shufSuper);
    Assert.assertNotEquals(shufSuper.getDealt(), shufSuperDealt);
  }

  @Test
  public void testDeckGCOs() {
    initTests();
    Assert.assertEquals(unShufStandard, new TypeDeck(DeckType.STANDARD));
    Assert.assertEquals(unShufSuper, new TypeDeck(DeckType.SUPERSTITIOUS));
    Assert.assertNotEquals(unShufStandard, shufStandard);
    Assert.assertNotEquals(unShufSuper, shufSuper);
    Assert.assertNotEquals(unShufSuper, unShufStandard);
    Assert.assertNotEquals(shufStandard, unShufSuper);

    String uss = unShufStandard.toString();
    String usS = unShufSuper.toString();
    Assert.assertNotEquals(uss.hashCode(), unShufStandard.hashCode());
    Assert.assertNotEquals(usS.hashCode(), unShufSuper.hashCode());
    Assert.assertNotEquals(new Object(), shufStandard);
    Assert.assertNotEquals(unShufSuper, new Object());

    shufStandard.resetDeck();
    unShufSuper.resetDeck();
    shufStandard.peekCard();
    unShufSuper.peekCard();
    Assert.assertEquals(new TypeDeck(DeckType.STANDARD), shufStandard);
    Assert.assertEquals(new TypeDeck(DeckType.SUPERSTITIOUS), unShufSuper);
    shufStandard.popCard();
    unShufSuper.popCard();
    Assert.assertNotEquals(new TypeDeck(DeckType.STANDARD), shufStandard);
    Assert.assertNotEquals(new TypeDeck(DeckType.SUPERSTITIOUS), unShufSuper);
    Assert.assertNotEquals(shufStandard, shufSuper);

    Assert.assertEquals(DeckType.STANDARD.toString(), "STANDARD");
    Assert.assertEquals(DeckType.SUPERSTITIOUS.toString(), "SUPERSTITIOUS");
  }

  @Test
  public void testDeckThrows() {
    initTests();
    for (int i = 0; i < 56; i++) {
      unShufStandard.peekCard();
    }
    Assert.assertEquals(unShufStandard.cardsSize(), 56);
    Assert.assertEquals(unShufStandard.dealtSize(), 0);

    for (int i = 0; i < 56; i++) {
      shufStandard.popCard();
    }
    Assert.assertEquals(shufStandard.cardsSize(), 0);
    Assert.assertEquals(shufStandard.dealtSize(), 56);

    Assert.assertThrows(IllegalArgumentException.class, () -> shufStandard.peekCard());
    Assert.assertThrows(IllegalArgumentException.class, () -> shufStandard.popCard());
  }

  //**************************************************************************************** HELPERS
  private void initTests() {
    unShufStandard = new TypeDeck(DeckType.STANDARD);
    shufStandard = new TypeDeck(DeckType.STANDARD);
    shufStandard.shuffle();

    unShufSuper = new TypeDeck(DeckType.SUPERSTITIOUS);
    shufSuper = new TypeDeck(DeckType.SUPERSTITIOUS);
    shufSuper.shuffle();
  }
}
