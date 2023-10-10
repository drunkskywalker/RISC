package edu.duke.ece651.team12.shared;

public class RailwayConstructTechChecker implements RuleChecker {
  private RuleChecker next;
  public RailwayConstructTechChecker() {
    this.next = new RailwayConstructAdjacentChecker();
  }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer tech_lvl = map.tech_lvls.get(player_info);
    if (tech_lvl < 2) {
      throw new IllegalArgumentException(
          "Railway Construct Invalid Instruction: You don't know how to constuct railways.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
