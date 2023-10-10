package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MoveOwnershipCheckerTest {
  //@Disabled
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
    MoveOwnershipChecker mosc = new MoveOwnershipChecker();
    ArrayList<InitResponse> irs = bmf.createInitResponses();
    for (InitResponse ir : irs) {
      map.init(ir);
    }
    map.generateResonse();
    ArrayList<TurnResponse> trs = new ArrayList<>();

    for (Territory t : map.territories) {
      trs.add(
          new TurnResponse(t.id, t.player_info, TerritoryWP.genUnit(1000), 500, 1000, 3));
    }
    map.update(trs);

    MoveRequest mr = new MoveRequest(p0, 0, 1, TerritoryWP.genUnit(1));
    assertTrue(mosc.checkRule(mr, map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> mosc.checkRule(
                         new MoveRequest(p0, 0, 21, TerritoryWP.genUnit(1)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> mosc.checkRule(
                         new MoveRequest(p0, 21, 0, TerritoryWP.genUnit(1)), map, p0));

    assertThrows(IllegalArgumentException.class,
                 ()
                     -> mosc.checkRule(
                         new MoveRequest(p0, 25, 25, TerritoryWP.genUnit(1)), map, p0));
    assertThrows(
        IllegalArgumentException.class,
        () -> mosc.checkRule(new MoveRequest(p0, 0, 0, TerritoryWP.genUnit(1)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> mosc.checkRule(
                         new MoveRequest(p0, 0, 1, TerritoryWP.genUnit(2000)), map, p0));

    assertThrows(IllegalArgumentException.class,
                 ()
                     -> mosc.checkRule(
                         new MoveRequest(p0, 0, 1, TerritoryWP.genUnit(1000)), map, p0));
    assertThrows(
        IllegalArgumentException.class,
        () -> mosc.checkRule(new MoveRequest(p0, 0, 1, TerritoryWP.genUnit(0)), map, p0));

    RuleChecker arc = new AttackOwnershipChecker();

    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 25, 25, TerritoryWP.genUnit(0)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 23, 20, TerritoryWP.genUnit(0)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 0, 1, TerritoryWP.genUnit(0)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 0, 0, TerritoryWP.genUnit(0)), map, p0));
    assertThrows(
        IllegalArgumentException.class,
        ()
            -> arc.checkRule(
                new AttackRequest(p0, 7, 8, TerritoryWP.genUnit(10000)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 7, 8, TerritoryWP.genUnit(0)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> arc.checkRule(
                         new AttackRequest(p0, 7, 23, TerritoryWP.genUnit(1)), map, p0));
    System.out.println(map.shortestDistance(p2)[4][8]);
    System.out.println(map.distances[4][8]);
    assertThrows(
        IllegalArgumentException.class,
        ()
            -> arc.checkRule(
                new AttackRequest(p0, 4, 8, TerritoryWP.genUnit(1000)), map, p0));

    RuleChecker urc = new UpgradeOwnershipChecker();
    ArrayList<Integer> upg = TerritoryWP.genUnit(1);
    upg.remove(6);
    assertTrue(urc.checkRule(new UpgradeRequest(p0, 0, upg), map, p0));
    assertThrows(
        IllegalArgumentException.class,
        ()
            -> arc.checkRule(
                new AttackRequest(p0, 4, 8, TerritoryWP.genUnit(1000)), map, p0));

    assertThrows(IllegalArgumentException.class,
                 ()
                     -> urc.checkRule(
                         new UpgradeRequest(p0, 0, TerritoryWP.genUnit(1000)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> urc.checkRule(
                         new UpgradeRequest(p0, 0, TerritoryWP.genUnit(4000)), map, p0));

    assertThrows(IllegalArgumentException.class,
                 ()
                     -> urc.checkRule(
                         new UpgradeRequest(p0, 25, TerritoryWP.genUnit(1000)), map, p0));
    assertThrows(IllegalArgumentException.class,
                 ()
                     -> urc.checkRule(
                         new UpgradeRequest(p0, 23, TerritoryWP.genUnit(1000)), map, p0));
    upg.set(0, 1);

    for (Territory t : map.territories) {
      trs.add(
          new TurnResponse(t.id, t.player_info, TerritoryWP.genUnit(1000), 500, 1000, 0));
    }
    map.update(trs);
    assertThrows(IllegalArgumentException.class,
                 () -> urc.checkRule(new UpgradeRequest(p0, 0, upg), map, p0));
    map.research_barrier.put(p0, true);
    RuleChecker rrc = new ResearchDuplicationChecker();
    assertThrows(IllegalArgumentException.class,
                 () -> rrc.checkRule(new ResearchRequest(p0), map, p0));
  }
}
