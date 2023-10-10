
package edu.duke.ece651.team12.shared;
public class ResearchDuplicationChecker implements RuleChecker {
  private RuleChecker next;
  public ResearchDuplicationChecker() { next = new ResearchResourceChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    if (map.research_barrier.get(player_info)) {
      throw new IllegalArgumentException(
          "Research Invalid Instruction: Another research request has been issued in this round.");
    }
    else {
      map.research_barrier.put(player_info, true);
      return next.checkRule(request, map, player_info);
    }
  }
}
