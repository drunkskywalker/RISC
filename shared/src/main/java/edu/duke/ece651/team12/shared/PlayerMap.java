
package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class PlayerMap extends Map {
  public PlayerMap(String name) { super(name); }

  @Override
  public String toString() {
    String re = "";
    for (Territory t : territories) {
      re += t.toString();
    }
    return re;
  }
}
