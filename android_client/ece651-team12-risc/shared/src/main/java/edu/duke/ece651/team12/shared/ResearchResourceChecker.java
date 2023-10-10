

package edu.duke.ece651.team12.shared;
public class ResearchResourceChecker implements RuleChecker {
  private RuleChecker next;
  public ResearchResourceChecker() { next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int curr_lvl = map.tech_lvls.get(player_info);
    int amount = map.tech_accus.get(player_info);
    if (amount < lvl_requirement()[curr_lvl]) {
      throw new IllegalArgumentException(
          "Research Invalid Instruction: Not enough technology points.");
    }
    else {
      amount -= lvl_requirement()[curr_lvl];
      map.tech_accus.put(player_info, amount);
      return next.checkRule(request, map, player_info);
    }
  }

  private static int[] lvl_requirement() {
    int[] ans = new int[6];
    ans[0] = 10;
    ans[1] = 20;
    ans[2] = 40;
    ans[3] = 75;
    ans[4] = 120;
    ans[5] = 150;
    return ans;
  }
}
