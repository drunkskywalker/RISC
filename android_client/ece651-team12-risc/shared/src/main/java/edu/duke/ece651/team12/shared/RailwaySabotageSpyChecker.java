package edu.duke.ece651.team12.shared;

public class RailwaySabotageSpyChecker implements RuleChecker {
  private RuleChecker next;
  public RailwaySabotageSpyChecker() { this.next = new RailwaySabotageTechChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory source = map.territories.get(request.getSourceId());
    boolean hasSpy = false;
    for (Spy s : source.spies) {
      if (s.owner.equals(player_info) && !s.hasMoved) {
        return next.checkRule(request, map, player_info);
      }
      if (s.owner.equals(player_info)) {
        hasSpy = true;
      }
    }
    if (hasSpy) {
      throw new IllegalArgumentException(
          "Railway Sabotage Invalid Instruction: All spies have just moved in this turn in this territory and cannot sabotage.");
    }
    else {
      throw new IllegalArgumentException(
          "Railway Sabotage Invalid Instruction: You don't have spies in the territory.");
    }
  }
}
