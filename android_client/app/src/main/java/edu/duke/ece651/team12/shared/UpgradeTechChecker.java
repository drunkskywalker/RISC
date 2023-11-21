

package edu.duke.ece651.team12.shared;

public class UpgradeTechChecker implements RuleChecker {
  private RuleChecker next;
  public UpgradeTechChecker() { this.next = new BlankChecker(); }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int req_max = 0;
    for (int i = 0; i < 6; i++) {
      if (request.units.get(i) > 0) {
        req_max = i + 1;
      }
    }
    int max = map.tech_lvls.get(player_info);
    if (req_max > max) {
      throw new IllegalArgumentException(
          "Upgrade Invalid Instruction: Research level does not support requested upgrade.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
