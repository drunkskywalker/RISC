package edu.duke.ece651.team12.shared;

public class SpyTrainCostChecker implements RuleChecker {
  private RuleChecker next;
  public SpyTrainCostChecker() { this.next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer tech = map.tech_accus.get(player_info);
    if (tech < 20) {
      throw new IllegalArgumentException(
          "Spy Train Invalid Instruction: You don't have enough technology to train a spy.");
    }
    Integer unit0 = map.territories.get(request.getSourceId()).units.get(0);
    if (unit0 == 0) {
      throw new IllegalArgumentException(
          "Spy Train Invalid Instruction: You don't have enough unit to train a spy.");
    }
    return next.checkRule(request, map, player_info);
  }
}
