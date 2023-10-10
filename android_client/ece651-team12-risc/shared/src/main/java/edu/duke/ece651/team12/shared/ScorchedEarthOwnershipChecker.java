package edu.duke.ece651.team12.shared;

public class ScorchedEarthOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public ScorchedEarthOwnershipChecker() { this.next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t = map.territories.get(request.getSourceId());
    if (!t.belongsTo(player_info)) {
      throw new IllegalArgumentException(
          "Scorched Earth Invalid Instruction: territory does not belong to you, but " +
          t.player_info.player_name + ".");
    }
    return next.checkRule(request, map, player_info);
  }
}
