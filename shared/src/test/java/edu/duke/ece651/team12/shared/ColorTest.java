package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ColorTest {
  @Test
  public void test() {
    Color c = new Color(255, 255, 255);
    assertNotEquals(c.toString(), "#ffffff");
    assertNotEquals(c, 1);
    Color c0 = new Color(2, 2, 2);
    assertEquals(c0.toString(), "#020202");
    Color c1 = new Color(255, 255, 254);
    assertEquals(c1.toString(), "#fffffe");
    Color c2 = new Color(255, 0, 0);
    assertEquals(c2.toString(), "Red");
    Color c3 = new Color(0, 255, 0);
    assertEquals(c3.toString(), "Green");
    Color c4 = new Color(0, 0, 255);
    assertEquals(c4.toString(), "Blue");
    Color c5 = new Color(255, 255, 0);
    assertEquals(c5.toString(), "Yellow");
  }
}
