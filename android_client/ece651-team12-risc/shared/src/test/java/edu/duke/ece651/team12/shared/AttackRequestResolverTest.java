package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class AttackRequestResolverTest {
  @Test
  public void test() {
    ServerMap map = new ServerMap("MiddleEarth");

    InitResponse ir = new InitResponse(0,
                                       new PlayerInfo(0, "Dee", new Color(255, 0, 0)),
                                       10,
                                       "Bree",
                                       new ArrayList<Integer>());
    PlayerInfo p1 = new PlayerInfo(1, "Gandalf", new Color(0, 255, 0));

    PlayerInfo p2 = new PlayerInfo(2, "Gandulf", new Color(0, 0, 255));
    InitResponse ir2 = new InitResponse(1, p1, 100, "RRRR", new ArrayList<Integer>());
    InitResponse ir3 = new InitResponse(2, p2, 100, "BBBB", new ArrayList<Integer>());
    InitResponse ir4 = new InitResponse(3, p2, 2, "xxx", new ArrayList<Integer>());
    map.init(ir);
    map.init(ir2);
    map.init(ir3);
    map.init(ir4);
    AttackRequestResolver arr = new AttackRequestResolver(map, 20, System.out);

    ArrayList<Request> requests = new ArrayList<Request>();
    arr.resolve(requests);
    requests.add(new AttackRequest(p1, 3, 1, 0));
    requests.add(new AttackRequest(p2, 3, 2, 0));
    requests.add(new AttackRequest(p1, 2, 1, 0));
    arr.resolve(requests);
    //   assertTrue(false);
    requests = new ArrayList<Request>();
    requests.add(new AttackRequest(p1, 1, 1, 0));
    requests.add(new AttackRequest(p2, 1, 2, 0));
    requests.add(new AttackRequest(p1, 2, 1, 0));

    requests.add(new AttackRequest(p1, 200, 1, 0));
    ArrayList<Integer> units = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      units.add(0);
    }
    units.add(10);

    requests.add(new AttackRequest(p1, 1, 0, units));
    arr.resolve(requests);
    System.out.println(map.toString());
  }
  @Test
  public void testZero() {
    ServerMap map = new ServerMap("MiddleEarth");

    InitResponse ir = new InitResponse(0,
                                       new PlayerInfo(0, "Dee", new Color(255, 0, 0)),
                                       0,
                                       "Bree",
                                       new ArrayList<Integer>());
    PlayerInfo p1 = new PlayerInfo(1, "Gandalf", new Color(0, 255, 0));

    PlayerInfo p2 = new PlayerInfo(2, "Gandulf", new Color(0, 0, 255));
    InitResponse ir2 = new InitResponse(1, p1, 100, "RRRR", new ArrayList<Integer>());
    InitResponse ir3 = new InitResponse(2, p2, 100, "BBBB", new ArrayList<Integer>());
    InitResponse ir4 = new InitResponse(3, p2, 2, "xxx", new ArrayList<Integer>());
    map.init(ir);
    map.init(ir2);
    map.init(ir3);
    map.init(ir4);
    AttackRequestResolver arr = new AttackRequestResolver(map, 20, System.out);

    ArrayList<Request> requests = new ArrayList<Request>();
    arr.resolve(requests);
    requests.add(new AttackRequest(p1, 1, 2, 0));
    requests.add(new AttackRequest(p2, 1, 3, 0));
    requests.add(new AttackRequest(p1, 2, 2, 0));
    arr.resolve(requests);
  }
  @Test
  public void testEqual() {
    ServerMap map = new ServerMap("Middle Earth");

    PlayerInfo p0 = new PlayerInfo(0, "Mst. Skywalker", new Color(255, 0, 0));
    PlayerInfo p1 = new PlayerInfo(1, "Gandalf", new Color(0, 255, 0));
    PlayerInfo p2 = new PlayerInfo(2, "Kaiser Wilhelm II", new Color(0, 0, 255));
    ArrayList<PlayerInfo> ps = new ArrayList<>();
    ps.add(p0);
    ps.add(p1);
    ps.add(p2);
    V2MapFactory v2mf = new V2MapFactory(ps);
    ArrayList<InitResponse> irs = v2mf.createInitResponses();

    for (InitResponse i : irs) {
      map.init(i);
    }

    AttackRequestResolver arr = new AttackRequestResolver(map, 20, System.out);

    map.food_accus.put(p1, 1000);
    map.food_accus.put(p2, 1000);
    map.food_accus.put(p0, 1000);
    map.territories.get(0).player_info = p0;
    map.territories.get(3).player_info = p2;
    map.territories.get(2).player_info = p1;
    map.territories.get(0).units.set(0, 10);
    ArrayList<Request> requests = new ArrayList<Request>();
    arr.resolve(requests);

    ArrayList<Integer> p1u = new ArrayList<>();
    p1u.add(0);
    p1u.add(2);
    p1u.add(0);
    p1u.add(0);
    p1u.add(3);
    p1u.add(1);
    p1u.add(0);

    ArrayList<Integer> p2u = new ArrayList<>();
    p2u.add(0);
    p2u.add(2);
    p2u.add(0);
    p2u.add(0);
    p2u.add(3);
    p2u.add(1);
    p2u.add(0);

    ArrayList<Integer> p3u = new ArrayList<>();
    p3u.add(0);
    p3u.add(0);
    p3u.add(0);
    p3u.add(0);
    p3u.add(0);
    p3u.add(0);
    p3u.add(1);

    requests.add(new AttackRequest(p1, 2, 0, p1u));
    requests.add(new AttackRequest(p2, 3, 0, p2u));
    requests.add(new AttackRequest(p1, 2, 0, p3u));

    arr.resolve(requests);
    //   assertTrue(false);

    System.out.println(map.toString());
    for (PlayerInfo p : map.food_accus.keySet()) {
      System.out.println(p.player_name + "'s Food reserve: " + map.food_accus.get(p));
    }
    // assertTrue(false);
  }
}
