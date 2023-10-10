package edu.duke.ece651.team12.shared;

public class SpyMoveAdjacencyChecker implements RuleChecker {
  private RuleChecker next;
  public SpyMoveAdjacencyChecker() { this.next = new SpyMoveCostChecker(); }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t_s = map.territories.get(request.getSourceId());
    Territory t_t = map.territories.get(request.getDestinationId());
    if (!t_s.isAdjacentTo(t_t)) {
      throw new IllegalArgumentException(
          "Spy Move Invalid Instruction: The territories are not adjacent.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
