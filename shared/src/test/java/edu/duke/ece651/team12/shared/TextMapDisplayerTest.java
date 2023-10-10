package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class TextMapDisplayerTest {
  @Test
  public void test() {
    PlayerMap m = new PlayerMap("Middle Earth");
    ArrayList A0 = new ArrayList<Integer>();
    A0.add(1);
    A0.add(3);
    ArrayList A1 = new ArrayList<Integer>();
    A1.add(0);
    A1.add(3);
    ArrayList A3 = new ArrayList<Integer>();
    A3.add(0);
    A3.add(1);

    m.init(new InitResponse(
        0, new PlayerInfo(0, "Sauron", new Color(0, 0, 0)), 0, "Mordor", A0));
    m.init(new InitResponse(
        1, new PlayerInfo(1, "Aragon", new Color(120, 120, 120)), 1, "Gondor", A1));
    m.init(new InitResponse(2,
                            new PlayerInfo(2, "Dodo", new Color(234, 234, 234)),
                            2,
                            "Dordor",
                            new ArrayList<Integer>()));
    m.init(new InitResponse(
        3, new PlayerInfo(1, "Aragon", new Color(120, 120, 120)), 1, "Gondorrrrr", A3));

    TextMapDisplayer tmd =
        new TextMapDisplayer(m, new PlayerInfo(0, "Sauron", new Color(0, 0, 0)));
    System.out.println(tmd.display());
    System.out.println(tmd.adjacency());
    // assertTrue(false);
  }
}
