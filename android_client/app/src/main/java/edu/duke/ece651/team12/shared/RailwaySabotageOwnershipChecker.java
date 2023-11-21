package edu.duke.ece651.team12.shared;

public class RailwaySabotageOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public RailwaySabotageOwnershipChecker() {
    this.next = new RailwaySabotageSpyChecker();
  }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t = map.territories.get(request.getSourceId());
    if (t.belongsTo(player_info)) {
      throw new IllegalArgumentException(
          "Spy Train Invalid Instruction: why do you want to blow up your own railwayr?");
    }
    return next.checkRule(request, map, player_info);
  }
}
