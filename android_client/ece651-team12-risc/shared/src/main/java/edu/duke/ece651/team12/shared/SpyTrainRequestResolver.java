package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class SpyTrainRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;

  public SpyTrainRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Territory t = map.territories.get(r.getSourceId());
      t.spies.add(new Spy(r.getIssuerId()));
      Integer unit = t.units.get(0);
      unit--;
      t.units.set(0, unit);
      int tech_acc = map.tech_accus.get(r.getIssuerId());
      tech_acc -= 20;
      map.tech_accus.put(r.getIssuerId(), tech_acc);
      out.println("player " + r.getIssuerId().player_name + " trained a spy in " +
                  r.getSourceId());
    }
  }
}
