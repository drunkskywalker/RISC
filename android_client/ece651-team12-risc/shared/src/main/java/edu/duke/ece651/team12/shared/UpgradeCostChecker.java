
package edu.duke.ece651.team12.shared;

public class UpgradeCostChecker implements RuleChecker {
  private RuleChecker next;
  public UpgradeCostChecker() { this.next = new UpgradeTechChecker(); }
  public static int[] costs() {
    int[] ans = new int[6];
    ans[0] = 3;
    ans[1] = 11;
    ans[2] = 19;
    ans[3] = 25;
    ans[4] = 35;
    ans[5] = 50;
    return ans;
  }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int cost = 0;
    int[] table = costs();
    for (int i = 0; i < 6; i++) {
      cost += request.units.get(i) * table[i];
    }
    int storage = map.tech_accus.get(player_info);

    if (cost > storage) {
      throw new IllegalArgumentException(
          "Upgrade Invalid Instruction: Tech stockpile cannot support this order.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
