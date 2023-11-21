package edu.duke.ece651.team12.shared;

public class SpyTrainTechChecker implements RuleChecker {
  private RuleChecker next;
  public SpyTrainTechChecker() { this.next = new SpyTrainCostChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer lvl = map.tech_lvls.get(player_info);
    if (lvl < 1) {
      throw new IllegalArgumentException(
          "Spy Train Invalid Instruction: You don't have the knowledge to train spies.");
    }
    return next.checkRule(request, map, player_info);
  }
}
