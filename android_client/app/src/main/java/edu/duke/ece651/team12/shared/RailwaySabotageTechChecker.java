package edu.duke.ece651.team12.shared;

public class RailwaySabotageTechChecker implements RuleChecker {
  private RuleChecker next;
  public RailwaySabotageTechChecker() { this.next = new RailwaySabotageCostChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer lvl = map.tech_lvls.get(player_info);
    if (lvl < 3) {
      throw new IllegalArgumentException(
          "Railway Sabotage Invalid Instruction: You don't have the knowledge to sabotage railway with spies.");
    }
    return next.checkRule(request, map, player_info);
  }
}
