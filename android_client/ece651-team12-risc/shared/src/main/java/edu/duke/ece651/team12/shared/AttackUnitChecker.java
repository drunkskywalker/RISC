package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

/*Define a private method compareUnits that takes two ArrayList<Integer> parameters r and t and returns true if all elements of r are less than or equal to their corresponding elements in t, otherwise returns false*/

public class AttackUnitChecker implements RuleChecker {
  private RuleChecker next;
  public AttackUnitChecker() { this.next = new AttackAdjacencyChecker(); }

  private boolean compareUnits(ArrayList<Integer> r, ArrayList<Integer> t) {
    for (int i = 0; i < 7; i++) {
      if (r.get(i) > t.get(i)) {
        return false;
      }
    }
    return true;
  }


  /* The method returns a boolean value. If the action complies with the compare unit  rule being checked, the method returns true, and the action is passed to the next rule checker in the chain. If the action does not comply with the rule, the method typically throws an exception with an informative error message describing why the action is invalid. */
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int count = TerritoryWP.genCount(request.units);
    if (count == 0) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: You need at least one unit.");
    }
    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        if (compareUnits(request.units, t.units)) {
          return next.checkRule(request, map, player_info);
        }
        else {
          throw new IllegalArgumentException(
              "Attack Invalid Instruction: There isn't enough units in the starting territory.");
        }
      }
    }
    throw new IllegalArgumentException(
        "Attack Invalid Instruction: Territory does not exist.");
  }
}
