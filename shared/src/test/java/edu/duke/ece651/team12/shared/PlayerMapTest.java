package edu.duke.ece651.team12.shared;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class PlayerMapTest {
  @Test
  public void test() {
    PlayerMap map = new PlayerMap("Middle Earth");
    map.init(new InitResponse(
        0, new PlayerInfo(0, "x", new Color()), 12, "Gondor", new ArrayList<Integer>()));
    map.init(new InitResponse(
        1, new PlayerInfo(1, "y", new Color()), 12, "Mordor", new ArrayList<Integer>()));
    //assertEquals(map.territories.get(0).toString(),
    //             "0. Gondor. Units: 12\n====================\n");
    //assertEquals(map.territories.get(1).toString(),
    //             "1. Mordor. Units: 12\n====================\n");
    System.out.println(map);
  }

  @Test
  public void test2() {
    PlayerMap map = new PlayerMap("ME");
    ArrayList<PlayerInfo> ps = new ArrayList<>();
    PlayerInfo p1 = new PlayerInfo(0, "GDF", new Color(255, 0, 0));
    ps.add(p1);
    PlayerInfo p2 = new PlayerInfo(1, "YD", new Color(0, 255, 0));
    ps.add(p2);
    PlayerInfo p3 = new PlayerInfo(2, "ANK", new Color(0, 0, 255));
    ps.add(p3);

    V2MapFactory v2mf = new V2MapFactory(ps);
    ArrayList<InitResponse> irs = v2mf.createInitResponses();
    for (InitResponse i : irs) {
      map.init(i);
    }
    int[][] ans = map.shortestDistance(p1);
    for (int[] x : ans) {
      System.out.println(Arrays.toString(x));
    }
  }
}
