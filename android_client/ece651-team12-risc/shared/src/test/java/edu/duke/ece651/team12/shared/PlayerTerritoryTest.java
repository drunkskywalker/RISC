package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class PlayerTerritoryTest {
  @Test
  public void test() {
    ArrayList<Integer> a0 = new ArrayList<Integer>();
    a0.add(1);
    a0.add(2);
    Territory t0 =
        new PlayerTerritory(0, "Gondor", 0, a0, new PlayerInfo(0, "x", new Color()));
    Territory t1 = new PlayerTerritory(
        1, "Mordor", 1, new ArrayList<Integer>(), new PlayerInfo(1, "y", new Color()));
    Territory t2 = new PlayerTerritory(
        3, "Rivendell", 2, new ArrayList<Integer>(), new PlayerInfo(2, "z", new Color()));
    assertTrue(t0.isAdjacentTo(t1));
    assertTrue(!t0.isAdjacentTo(t2));
  }
}
