package edu.duke.ece651.team12.shared;

public class MoveCostChecker implements RuleChecker {
  private RuleChecker next;
  public MoveCostChecker() { this.next = new BlankChecker(); }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int[][] distance = map.shortestDistance(player_info);
    int cost = distance[request.getSourceId()][request.getDestinationId()] *
               TerritoryWP.genCount(request.units);
    int storage = map.food_accus.get(player_info);
    if (cost > storage) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: Food stockpile cannot support this order.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
