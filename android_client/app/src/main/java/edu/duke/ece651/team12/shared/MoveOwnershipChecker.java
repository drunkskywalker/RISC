package edu.duke.ece651.team12.shared;

public class MoveOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public MoveOwnershipChecker() { this.next = new MoveUnitChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t1 = null;
    Territory t2 = null;

    // first find the corresponding territories for source and destination
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        t1 = t;
      }
      if (t.id == request.getDestinationId()) {
        t2 = t;
      }
    }

    // check if sourceID / destinationID has a territory that does not exist in the map
    if (t1 == null || t2 == null) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: Territory does not exist.");
    }

    // check if the source Territory is NOT equal to the destination Territory
    // because it makes no sense to move from one place to the same place
    if (t1 == t2) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: You can't move to the same place.");
    }

    // check if the starting territory belongs to the current player
    if (!t1.player_info.equals(player_info)) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: Starting territory does not belong to you, but " +
          t1.player_info.player_name + ".");
    }

    // check if the destination territory belongs to the current player
    if (!t2.player_info.equals(player_info)) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: Destination territory does not belong to you, but " +
          t2.player_info.player_name + ".");
    }

    return next.checkRule(request, map, player_info);
  }
}
