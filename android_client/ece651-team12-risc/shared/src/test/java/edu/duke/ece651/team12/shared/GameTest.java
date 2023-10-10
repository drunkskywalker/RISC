package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class GameTest {
  //  @Disabled
  @Test
  public void test() {
    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
    Game game = new Game(2, bfr, System.out, "Middle Earth", 3);
    assertEquals(game.getInitUnit(), 3);
    ArrayList<String> names = new ArrayList<>();
    names.add("GDF");
    names.add("Yoda");
    ArrayList<PlayerInfo> ps = game.initPlayerInfos(names);

    ArrayList<InitResponse> irs = game.initMap();
    System.out.println(irs.size());
    //assertTrue(false);
    ArrayList<TurnResponse> trs = new ArrayList<>();
    for (InitResponse r : irs) {
      trs.add(new TurnResponse(
          r.getTerritoryId(), r.getPlayerInfo(), TerritoryWP.genUnit(10), 200, 200, 1));
    }
    game.initUnit(trs);
    assertTrue(!game.checkEnd());
    ArrayList<Request> requests = new ArrayList<>();
    for (PlayerInfo i : ps) {
      System.out.println(i);
    }
    PlayerInfo x = ps.get(0);
    assertEquals(x.player_name, "GDF");

    ArrayList<TurnResponse> trs_old = game.turnResponse();
    for (TurnResponse t : trs_old) {
      System.out.println(t.toString());
    }
    requests.add(new MoveRequest(ps.get(0), 1, 0, 1));
    requests.add(new ResearchRequest(ps.get(0)));
    ArrayList<Integer> up = new ArrayList<>();
    up.add(1);
    for (int it = 0; it < 5; it++) {
      up.add(0);
    }
    requests.add(new UpgradeRequest(ps.get(0), 0, up));
    requests.add(new AttackRequest(ps.get(0), 1, 11, 12));
    ArrayList<PlayerInfo> pps = new ArrayList<>();
    pps.add(ps.get(0));
    pps.add(ps.get(0));
    pps.add(ps.get(0));
    pps.add(ps.get(0));

    game.turn(requests, pps);
    trs = new ArrayList<>();
    for (InitResponse r : irs) {
      trs.add(new TurnResponse(r.getTerritoryId(), ps.get(0), 1));
    }

    game.initUnit(trs);
    assertTrue(!game.checkLose(0));
    assertTrue(game.checkLose(1));
    assertTrue(game.checkEnd());

    //    assertTrue(false);
  }
}
