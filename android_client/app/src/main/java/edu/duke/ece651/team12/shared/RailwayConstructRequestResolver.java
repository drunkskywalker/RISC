package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class RailwayConstructRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;

  public RailwayConstructRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      Territory t = map.territories.get(r.getSourceId());
      Territory d = map.territories.get(r.getDestinationId());
      t.modify_dist(d.id, 1);
      d.modify_dist(t.id, 1);
      out.println("player " + r.getIssuerId().player_name +
                  " constructed railway between " + r.getSourceId() + " and " +
                  r.getDestinationId());
      Integer food_accu = map.food_accus.get(r.getIssuerId());
      Territory t0 = map.territories.get(r.getSourceId());
      Territory t1 = map.territories.get(r.getDestinationId());
      int cost = map.distances[t0.id][t1.id] * 5;
      food_accu -= cost;
      map.food_accus.put(r.getIssuerId(), food_accu);
      out.println("The construction cost " + cost + " units of food.");
    }
  }
}
