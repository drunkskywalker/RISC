package edu.duke.ece651.team12.shared;

public class CloakTechChecker implements RuleChecker {
  private RuleChecker next;
  public CloakTechChecker() { this.next = new CloakResourceChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer tech_lvl = map.tech_lvls.get(player_info);
    if (tech_lvl >= 3) {
      return next.checkRule(request, map, player_info);
    }
    else {
      throw new IllegalArgumentException(
          "Cloak Illegal Instrcution: You don't know how to cloak your territory yet. ");
    }
  }
}
