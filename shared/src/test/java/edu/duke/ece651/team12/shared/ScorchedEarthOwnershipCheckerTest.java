package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ScorchedEarthOwnershipCheckerTest {
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

    RuleChecker rc = new ScorchedEarthOwnershipChecker();

    assertThrows(IllegalArgumentException.class,
                 () -> rc.checkRule(new ScorchedEarthRequest(p2, 0), map, p2));

    rc.checkRule(new ScorchedEarthRequest(p0, 0), map, p0);
  }
}
