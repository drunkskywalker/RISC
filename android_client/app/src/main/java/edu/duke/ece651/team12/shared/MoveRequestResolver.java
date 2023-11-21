package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class MoveRequestResolver implements RequestResolver {
  private ServerMap map;
  private PrintStream out;
  public MoveRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  @Override
  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      move_unit(map.territories.get(r.getSourceId()),
                map.territories.get(r.getDestinationId()),
                r.units,
                r.getIssuerId());
    }
  }

  private void move_unit(Territory terr0,
                         Territory terr1,
                         ArrayList<Integer> order,
                         PlayerInfo p) {
    for (int i = 0; i < 7; i++) {
      Integer x = order.get(i);
      terr0.units.set(i, terr0.units.get(i) - x);
      terr1.units.set(i, terr1.units.get(i) + x);
      int cost = map.shortestDistance(p)[terr0.id][terr1.id] * x;
      Integer food_accu = map.food_accus.get(p);
      food_accu -= cost;
      map.food_accus.put(p, food_accu);
    }
  }
}
