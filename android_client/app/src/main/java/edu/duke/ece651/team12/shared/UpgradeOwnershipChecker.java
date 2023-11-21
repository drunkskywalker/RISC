package edu.duke.ece651.team12.shared;

public class UpgradeOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public UpgradeOwnershipChecker() { this.next = new UpgradeUnitChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t1 = null;

    // first find the corresponding territories for source and destination
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        t1 = t;
      }
    }

    // check if sourceID / destinationID has a territory that does not exist in the map
    if (t1 == null) {
      throw new IllegalArgumentException(
          "Upgrade Invalid Instruction: Territory does not exist.");
    }

    // check if the selected territory belongs to the current player
    if (!t1.player_info.equals(player_info)) {
      throw new IllegalArgumentException(
          "Upgrade Invalid Instruction: This territory does not belong to you, but " +
          t1.player_info.player_name + ".");
    }

    return next.checkRule(request, map, player_info);
  }
}
