package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class BasicMapFactoryTest {
  private ArrayList<String> genNames(int n) {
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < n; i++) {
      String name = "t" + i;
      names.add(name);
    }
    return names;
  }

  private ArrayList<PlayerInfo> genPlayers(int n, ArrayList<Color> c) {
    ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();
    for (int i = 0; i < n; i++) {
      String name = "Player " + i;
      PlayerInfo p = new PlayerInfo(i, name, c.get(i));
      players.add(p);
    }
    return players;
  }

  @Test
  public void test_basic_map_factory() {
    ArrayList<String> names = genNames(24);
    ArrayList<Color> colors = new ArrayList<Color>();
    colors.add(new Color(255, 0, 0));
    colors.add(new Color(0, 255, 0));
    colors.add(new Color(0, 0, 255));
    ArrayList<PlayerInfo> players = genPlayers(3, colors);
    BasicMapFactory bmf = new BasicMapFactory(players, 3, 24);
    ArrayList<InitResponse> ixx = bmf.createInitResponses();
    ixx = bmf.createInitResponses(24, false);
    ArrayList<InitResponse> ir = bmf.createInitResponses();
    ServerMap map = new ServerMap("MD");
    TextMapDisplayer tmd = new TextMapDisplayer(map, players.get(0));
    for (InitResponse i : ir) {
      map.init(i);
    }
    System.out.println(tmd.display());
  }
}
