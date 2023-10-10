package edu.duke.ece651.team12.shared;

public class SpyMoveOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public SpyMoveOwnershipChecker() { this.next = new SpyMoveAdjacencyChecker(); }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t = map.territories.get(request.getSourceId());
    for (Spy s : t.spies) {
      if (s.owner.equals(player_info)) {
        return next.checkRule(request, map, player_info);
      }
    }
    throw new IllegalArgumentException(
        "Spy Move Invalid Instruction: There are no spies belonging to you in source territory.");
  }
}
