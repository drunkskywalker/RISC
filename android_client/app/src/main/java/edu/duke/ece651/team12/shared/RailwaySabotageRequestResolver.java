package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class RailwaySabotageRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;

  public RailwaySabotageRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Territory t = map.territories.get(r.getSourceId());
      Spy s = null;
      for (Spy ss : t.spies) {
        if (ss.owner.equals(r.getIssuerId()) && !ss.hasMoved) {
          s = ss;
          break;
        }
      }
      for (Territory d : map.territories) {
        if (t.isAdjacentTo(d) && !d.belongsTo(r.getIssuerId())) {
          if (t.distmodifiers[d.id] == 1) {
            t.modify_dist(d.id, 0);
            d.modify_dist(t.id, 0);
          }
        }
        if (t.isAdjacentTo(d) && d.belongsTo(r.getIssuerId()) && !s.hasMoved) {
          t.spies.remove(s);
          d.spies.add(s);
          s.hasMoved = true;
          out.println(
              "All Railways passing through " + t.name +
              " has been sabotaged. The spy was revealed but fled to safety before captured by " +
              t.player_info.player_name + ".");
        }
      }
      if (!s.hasMoved) {
        t.spies.remove(s);
        out.println(
            "All Railways passing through " + t.player_info.player_name +
            " has been sabotaged. The spy was revealed, and was executed as retaliation.");
      }
    }
  }
}
