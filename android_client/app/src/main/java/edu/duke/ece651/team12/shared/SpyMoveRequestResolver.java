package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class SpyMoveRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;

  public SpyMoveRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Territory source = map.territories.get(r.getSourceId());
      Territory dest = map.territories.get(r.getDestinationId());

      Spy tomove = null;
      for (Spy s : source.spies) {
        if (!s.hasMoved && s.owner.equals(r.getIssuerId())) {
          tomove = s;
          tomove.hasMoved = true;
          break;
        }
      }
      source.spies.remove(tomove);
      dest.spies.add(tomove);

      int food_acc = map.food_accus.get(r.getIssuerId());
      food_acc -= map.distances[r.getSourceId()][r.getDestinationId()];
      map.food_accus.put(r.getIssuerId(), food_acc);
    }
  }
}
