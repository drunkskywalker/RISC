package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class ScorchedEarthRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;

  public ScorchedEarthRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Territory t = map.territories.get(r.getSourceId());
      t.ScorchedEarthRounds = 6;
    }
  }
}
