package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/*
public class MoveAdjacencyCheckerTest {
  @Test
  @Disabled
  public void test() {
    ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
    PlayerInfo p0 = new PlayerInfo(0, "Red", new Color(255, 0, 0));
    PlayerInfo p1 = new PlayerInfo(1, "Blue", new Color(0, 255, 0));
    PlayerInfo p2 = new PlayerInfo(2, "Green", new Color(0, 0, 255));
    pis.add(p0);
    pis.add(p1);
    pis.add(p2);
    BasicMapFactory bmf = new BasicMapFactory(pis, 3, 6);
    ServerMap map = new ServerMap("Middle Earth");
    MoveRequestResolver mrr = new MoveRequestResolver(map, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses(6, false);
    for (InitResponse ir : irs) {
      map.init(ir);
    }
    map.generateResonse();
    ArrayList<Request> mvs = new ArrayList<Request>();
    MoveAdjacencyChecker mac = new MoveAdjacencyChecker();
    Request r1 = new MoveRequest(p0, 1, 0, 1);
    assertTrue(mac.checkRule(r1, map, p0));
    Request r2 = new MoveRequest(p1, 2, 3, 5);
    assertThrows(IllegalArgumentException.class, () -> mac.checkRule(r2, map, p1));

    mvs.add(r1);
    mrr.resolve(mvs);
    TextMapDisplayer tmd = new TextMapDisplayer(map, p0);
    System.out.println(tmd.display());
    System.out.println(tmd.adjacency());
  }
}
*/
