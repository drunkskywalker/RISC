package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class RailwayConstructOwnershipCheckerTest {
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
    RuleChecker cker = new RailwayConstructOwnershipChecker();
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
                               0,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }

    map.update(trs);

    RailwayConstructRequest rcr = new RailwayConstructRequest(p0, 0, 1);
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 0, 21), map, p0));

    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 21, 0), map, p0));
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 0, 0), map, p0));

    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 25, 0), map, p0));
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 0, 1), map, p0));

    trs = new ArrayList<>();

    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               0,
                               1000,
                               3,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }

    map.update(trs);
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 0, 1), map, p0));
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwayConstructRequest(p0, 0, 5), map, p0));
    trs = new ArrayList<>();

    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               100,
                               1000,
                               3,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }

    map.update(trs);
    assertTrue(cker.checkRule(new RailwayConstructRequest(p0, 0, 1), map, p0));
  }
}
