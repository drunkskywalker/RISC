package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class RailwayConstructRequestResolverTest {
  @Test
  public void test() {
    ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
    PlayerInfo p0 = new PlayerInfo(0, "Red", new Color(255, 0, 0));
    PlayerInfo p1 = new PlayerInfo(1, "Blue", new Color(0, 255, 0));
    PlayerInfo p2 = new PlayerInfo(2, "Green", new Color(0, 0, 255));
    pis.add(p0);
    pis.add(p1);
    pis.add(p2);
    V2MapFactory bmf = new V2MapFactory(pis);
    ServerMap map = new ServerMap("Middle Earth");
    ArrayList<InitResponse> irs = bmf.createInitResponses();
    for (InitResponse ir : irs) {
      map.init(ir);
    }
    map.generateResonse();
    ArrayList<TurnResponse> trs = new ArrayList<>();

    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               500,
                               1000,
                               5,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }

    map.update(trs);

    RequestResolver rr = new RailwayConstructRequestResolver(map, System.out);
    RailwayConstructRequest r = new RailwayConstructRequest(p0, 0, 1);
    ArrayList<Request> rs = new ArrayList<>();
    rs.add(r);
    rr.resolve(rs);
    assertEquals(map.territories.get(0).distmodifiers[1], 1);
    assertEquals(map.territories.get(1).distmodifiers[0], 1);
    assertEquals(map.food_accus.get(p0), 500 - 5 * map.distances[0][1]);
    Request r2 = new RailwaySabotageRequest(p2, 0);
    Spy s = new Spy(p2);
    Spy s0 = new Spy(p0);
    map.territories.get(0).spies.add(s0);
    map.territories.get(2).spies.add(s0);

    map.territories.get(0).spies.add(s);
    s.hasMoved = false;
    RequestResolver rsrr = new RailwaySabotageRequestResolver(map, System.out);
    rs.add(r2);
    rs.remove(r);
    rsrr.resolve(rs);
    assertEquals(map.territories.get(0).distmodifiers[1], 0);
    assertEquals(map.territories.get(1).distmodifiers[0], 0);
    assertEquals(map.territories.get(0).spies.size(), 1);
    map.territories.get(2).setPlayerInfo(p2);
    map.territories.get(0).spies.add(s);
    s.hasMoved = false;
    rsrr.resolve(rs);
    assertEquals(map.territories.get(0).distmodifiers[1], 0);
    assertEquals(map.territories.get(1).distmodifiers[0], 0);
    assertEquals(map.territories.get(0).spies.size(), 1);
    assertEquals(map.territories.get(2).spies.size(), 2);

    s.hasMoved = false;
    Request r3 = new SpyMoveRequest(p2, 2, 0);
    RequestResolver smrr = new SpyMoveRequestResolver(map, System.out);
    rs.remove(r2);
    rs.add(r3);
    smrr.resolve(rs);
    assertEquals(map.territories.get(0).spies.size(), 2);
    assertEquals(map.territories.get(2).spies.size(), 1);

    rs.remove(r3);
    rs.add(new ScorchedEarthRequest(p0, 0));
    RequestResolver SErr = new ScorchedEarthRequestResolver(map, System.out);
    SErr.resolve(rs);
    assertEquals(map.territories.get(0).ScorchedEarthRounds, 6);

    RequestResolver sptrrr = new SpyTrainRequestResolver(map, System.out);

    ArrayList<Request> rrrs2 = new ArrayList<>();
    rrrs2.add(new SpyCreateRequest(p2, 2));
    sptrrr.resolve(rrrs2);
    assertEquals(map.territories.get(2).spies.size(), 2);
  }
}
