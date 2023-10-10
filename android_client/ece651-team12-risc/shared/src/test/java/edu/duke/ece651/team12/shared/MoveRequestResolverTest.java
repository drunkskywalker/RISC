package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class MoveRequestResolverTest {
  @Test
  public void test() {
    ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
    PlayerInfo p1 = new PlayerInfo(0, "Gandalf", new Color(255, 0, 0));
    PlayerInfo p2 = new PlayerInfo(1, "Wilhelm II", new Color(0, 255, 0));
    PlayerInfo p3 = new PlayerInfo(2, "Yoda", new Color(0, 0, 255));
    pis.add(p1);
    pis.add(p2);
    pis.add(p3);
    V2MapFactory bmf = new V2MapFactory(pis);
    ServerMap map = new ServerMap("Middle Earth");
    MoveRequestResolver mrr = new MoveRequestResolver(map, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses();
    for (InitResponse ir : irs) {
      map.init(ir);
    }
    map.generateResonse();
    ArrayList<Request> mvs = new ArrayList<Request>();
    MoveOwnershipChecker moc = new MoveOwnershipChecker();
    map.territories.get(0).units.set(0, 100);
    map.territories.get(0).units.set(1, 100);
    Request r = new MoveRequest(p1, 50, 0, 1);
    ArrayList<Integer> orders = new ArrayList<>();
    orders.add(24);
    orders.add(14);
    orders.add(0);
    orders.add(0);
    orders.add(0);
    orders.add(0);
    orders.add(0);
    Request r2 = new MoveRequest(p1, 38, 0, 1, orders);
    //moc.checkRule(r, map, p1);
    mvs.add(r);
    mvs.add(r2);
    mrr.resolve(mvs);
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);
    System.out.println(tmd.display());
    System.out.println(tmd.adjacency());
    //  assertTrue(false);
  }
}
