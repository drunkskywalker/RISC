package edu.duke.ece651.team12.shared;

public class RailwayConstructAdjacentChecker implements RuleChecker {
  private RuleChecker next;
  public RailwayConstructAdjacentChecker() {
    this.next = new RailwayConstructCostChecker();
  }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t0 = map.territories.get(request.getSourceId());
    Territory t1 = map.territories.get(request.getDestinationId());
    if (!t0.isAdjacentTo(t1)) {
      throw new IllegalArgumentException(
          "Railway Construct Invalid Instruction: The two territories aren't adjacent.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
