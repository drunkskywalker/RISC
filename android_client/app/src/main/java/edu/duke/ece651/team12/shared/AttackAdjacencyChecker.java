package edu.duke.ece651.team12.shared;

public class AttackAdjacencyChecker implements RuleChecker {
  private RuleChecker next;
  
  public AttackAdjacencyChecker() { this.next = new AttackCostChecker(); }

  /*apply change of responsibility to check the rules*/
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    
    Territory t1 = null;
    Territory t2 = null;
    
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        t1 = t;
      }
      else if (t.id == request.getDestinationId()) {
        t2 = t;
      }
    }
    if (t1 != null && t2 != null && t1.isAdjacentTo(t2)) {
      return next.checkRule(request, map, player_info);
    }
    else if (t1 == null || t2 == null) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: Territory does not exist.");
    }
    else {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: The starting and destination territories are not adjacent.");
    }
  }
  
}
