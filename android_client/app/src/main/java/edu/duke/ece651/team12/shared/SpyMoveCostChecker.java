package edu.duke.ece651.team12.shared;

public class SpyMoveCostChecker implements RuleChecker {
  private RuleChecker next;
  public SpyMoveCostChecker() { this.next = new SpyMoveRepeatChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int cost = map.distances[request.getSourceId()][request.getDestinationId()];
    int storage = map.food_accus.get(player_info);
    if (cost > storage) {
      throw new IllegalArgumentException(
          "Spy Move Invalid Instruction: Food stockpile cannot support this order.");
    }
    else {
      System.out.println("SM cost check passed");
      return next.checkRule(request, map, player_info);
    }
  }
}
