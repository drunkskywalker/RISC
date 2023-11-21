package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;

public class UpgradeRequestResolver implements RequestResolver {
  ServerMap map;
  PrintStream out;
  public UpgradeRequestResolver(ServerMap map, PrintStream out) {
    this.map = map;
    this.out = out;
  }

  private int calculate_cost(Request request) {
    int[] costs = UpgradeCostChecker.costs();
    int cost = 0;
    for (int i = 0; i < 6; i++) {
      cost += request.units.get(i) * costs[i];
    }
    return cost;
  }

  private void upgrade_units(ArrayList<Integer> t, ArrayList<Integer> r) {
    for (int i = 0; i < 6; i++) {
      int x = r.get(i);
      int p = t.get(i);
      int n = t.get(i + 1);
      t.set(i, p - x);
      t.set(i + 1, n + x);
    }
  }
  @Override
  public void resolve(ArrayList<Request> requests) {
    for (Request r : requests) {
      PlayerInfo p = r.getIssuerId();
      int cost = calculate_cost(r);
      int tech_accu = map.tech_accus.get(p);
      map.tech_accus.put(p, tech_accu - cost);
      upgrade_units(map.territories.get(r.getSourceId()).units, r.units);
    }
  }
}
