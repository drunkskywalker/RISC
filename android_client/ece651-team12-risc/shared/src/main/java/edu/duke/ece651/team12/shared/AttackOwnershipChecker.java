package edu.duke.ece651.team12.shared;

public class AttackOwnershipChecker implements RuleChecker {
  private RuleChecker next;
  public AttackOwnershipChecker() { next = new AttackUnitChecker(); }



  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory t1 = null;
    Territory t2 = null;
    
    /*Find the source and destination territories involved in the attack action*/
    
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        t1 = t;
      }
      else if (t.id == request.getDestinationId()) {
        t2 = t;
      }
    }

    /*Check if the territories exist, and if not, throw an exception*/
    if (t1 == null || t2 == null) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: Territory does not exist.");
    }

    /*Check if the source territory belongs to the current player, and if not, throw an exception*/
    else if (!t1.player_info.equals(player_info)) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: Starting territory does not belong to you, " +
          player_info.player_name + ", but Player " + t1.player_info.player_name + ".");
    }
    
    /*Check if the destination territory belongs to the current player, and if so, throw an exception*/
    else if (t2.player_info.equals(player_info)) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: You cannot attack your own territory.");
    }
    /*If all ownership checks are passed, proceed to the next rule checker in the chain*/
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
