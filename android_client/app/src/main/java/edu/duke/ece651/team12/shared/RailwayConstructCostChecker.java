package edu.duke.ece651.team12.shared;

public class RailwayConstructCostChecker implements RuleChecker {
  private RuleChecker next;
  public RailwayConstructCostChecker() { this.next = new BlankChecker(); }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer food_accu = map.food_accus.get(player_info);
    Territory t0 = map.territories.get(request.getSourceId());
    Territory t1 = map.territories.get(request.getDestinationId());
    int cost = map.distances[t0.id][t1.id] * 5;
    if (cost > food_accu) {
      throw new IllegalArgumentException(
          "Railway Construct Invalid Instruction: you don't have enough food: " + cost +
          " to pay off the labor to build railways.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
