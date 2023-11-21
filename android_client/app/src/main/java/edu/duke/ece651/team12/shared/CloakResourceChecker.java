package edu.duke.ece651.team12.shared;

public class CloakResourceChecker implements RuleChecker {
  private RuleChecker next;
  public CloakResourceChecker() { this.next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer tech_accu = map.tech_accus.get(player_info);
    int adjs = 0;
    Territory t0 = map.territories.get(request.getSourceId());
    for (Territory t : map.territories) {
      if (t0.isAdjacentTo(t)) {
        adjs++;
      }
    }
    int cost = adjs * 5;
    if (cost <= tech_accu) {
      return next.checkRule(request, map, player_info);
    }
    else {
      throw new IllegalArgumentException(
          "Cloak Illegal Instrcution: You don't have enough technology resource to cloak this territory.");
    }
  }
}
