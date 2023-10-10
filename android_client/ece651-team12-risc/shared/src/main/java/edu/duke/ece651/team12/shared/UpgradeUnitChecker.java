
package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class UpgradeUnitChecker implements RuleChecker {
  private RuleChecker next;

  public UpgradeUnitChecker() { this.next = new UpgradeCostChecker(); }

  private boolean compareUnits(ArrayList<Integer> r, ArrayList<Integer> t) {
    for (int i = 0; i < 6; i++) {
      if (r.get(i) > t.get(i)) {
        return false;
      }
    }
    return true;
  }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        if (compareUnits(request.units, t.units)) {
          return next.checkRule(request, map, player_info);
        }
        else {
          throw new IllegalArgumentException(
              "Upgrade Invalid Instruction: There isn't enough units in the territory.");
        }
      }
    }
    throw new IllegalArgumentException(
        "Upgrade Invalid Instruction: Territory does not exist.");
  }
}
