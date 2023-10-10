package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/*
public class AttackUnitCheckerTest {
  @Disabled
  @Test
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
    AttackRequestResolver arr = new AttackRequestResolver(map, 1, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses(6, false);
    for (InitResponse ir : irs) {
      map.init(ir);
    }
    map.generateResonse();
    ArrayList<Request> aks = new ArrayList<Request>();
    AttackUnitChecker auc = new AttackUnitChecker();
    Request r1 = new AttackRequest(p1, 1, 3, 4);
    assertTrue(auc.checkRule(r1, map, p1));
    Request r2 = new AttackRequest(p1, 4, 3, 4);
    assertTrue(auc.checkRule(r2, map, p1));
    Request r3 = new AttackRequest(p2, 6, 5, 0);
    assertThrows(IllegalArgumentException.class, () -> auc.checkRule(r3, map, p2));
    Request r4 = new AttackRequest(p2, 123, 5, 0);
    assertThrows(IllegalArgumentException.class, () -> auc.checkRule(r4, map, p2));

    aks.add(r1);
    aks.add(r2);
    arr.resolve(aks);
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);
    System.out.println(tmd.display());
    System.out.println(tmd.adjacency());
  }
}
*/
