package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class RailwaySabotageOwnershipCheckerTest {
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
    RuleChecker cker = new RailwaySabotageOwnershipChecker();
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

    Spy s = new Spy(p0);
    Spy s2 = new Spy(p0);
    Spy s3 = new Spy(p1);
    s2.hasMoved = true;

    map.territories.get(0).spies.add(s);
    map.territories.get(23).spies.add(s3);

    map.territories.get(23).spies.add(s2);
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwaySabotageRequest(p0, 0), map, p0));
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwaySabotageRequest(p0, 22), map, p0));
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwaySabotageRequest(p0, 23), map, p0));

    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               500,
                               1000,
                               2,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }
    map.update(trs);

    s2.hasMoved = false;
    map.territories.get(23).spies.add(s2);

    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwaySabotageRequest(p0, 23), map, p0));

    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               0,
                               0,
                               5,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }
    map.update(trs);

    s2.hasMoved = false;
    map.territories.get(23).spies.add(s2);
    map.territories.get(23).distmodifiers[22] = 1;
    map.territories.get(22).distmodifiers[23] = 1;
    assertThrows(IllegalArgumentException.class,
                 () -> cker.checkRule(new RailwaySabotageRequest(p0, 23), map, p0));
    for (Territory t : map.territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               TerritoryWP.genUnit(1000),
                               100,
                               100,
                               5,
                               new ArrayList<>(),
                               0,
                               0,
                               new int[24]));
    }
    map.update(trs);

    s2.hasMoved = false;
    map.territories.get(23).spies.add(s2);
    map.territories.get(23).distmodifiers[22] = 1;
    map.territories.get(22).distmodifiers[23] = 1;
    assertTrue(cker.checkRule(new RailwaySabotageRequest(p0, 23), map, p0));
  }
}
