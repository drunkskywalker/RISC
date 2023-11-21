package edu.duke.ece651.team12.shared;

public class CloakOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public CloakOwnershipChecker() { this.next = new CloakTechChecker(); }
  @Override

  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t = map.territories.get(request.getSourceId());
    if (t.belongsTo(player_info)) {
      return next.checkRule(request, map, player_info);
    }
    else {
      throw new IllegalArgumentException(
          "Cloak Illegal Instrcution: The territory does not belong to you, but " +
          t.getPlayerInfo().player_name);
    }
  }
}
