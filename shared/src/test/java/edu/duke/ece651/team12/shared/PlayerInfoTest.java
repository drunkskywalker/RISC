package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerInfoTest {
  @Test
  public void test() {
    PlayerInfo pi = new PlayerInfo(0, "Gandalf", new Color(255, 255, 255));
    assertEquals(pi.toString(), "ID: 0\nName: Gandalf\nColor: White(debug only)");
    assertNotEquals(pi, new PlayerInfo(0, "Gandalf", new Color(255, 25, 255)));
    assertNotEquals(pi, 2);
  }
}
