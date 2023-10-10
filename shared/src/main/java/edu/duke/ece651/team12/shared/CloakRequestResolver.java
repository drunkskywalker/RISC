package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class CloakRequestResolver implements RequestResolver {
  private ServerMap map;
  private PrintStream out;
  public CloakRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  @Override
  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Integer tech_accu = map.tech_accus.get(r.getIssuerId());
      int adjs = 0;
      Territory t0 = map.territories.get(r.getSourceId());
      for (Territory t : map.territories) {
        if (t0.isAdjacentTo(t)) {
          adjs++;
        }
      }
      int cost = adjs * 5;
      tech_accu -= cost;
      map.tech_accus.put(r.getIssuerId(), tech_accu);
      t0.clockRounds = 4;
      out.println("Cloaking " + t0.name + ".");
    }
  }
}
