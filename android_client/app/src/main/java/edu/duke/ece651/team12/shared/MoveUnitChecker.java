package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class MoveUnitChecker implements RuleChecker {
  private RuleChecker next;

  public MoveUnitChecker() { this.next = new MoveCostChecker(); }

  private boolean compareUnits(ArrayList<Integer> r, ArrayList<Integer> t) {
    for (int i = 0; i < 7; i++) {
      if (r.get(i) > t.get(i)) {
        return false;
      }
    }
    return true;
  }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int count = TerritoryWP.genCount(request.units);
    if (count == 0) {
      throw new IllegalArgumentException(
          "Move Invalid Instruction: You need at least one unit.");
    }

    for (Territory t : map.territories) {
      if (t.id == request.getSourceId()) {
        if (compareUnits(request.units, t.units)) {
          return next.checkRule(request, map, player_info);
        }
        else {
          throw new IllegalArgumentException(
              "Move Invalid Instruction: There isn't enough units in the starting territory.");
        }
      }
    }
    throw new IllegalArgumentException(
        "Move Invalid Instruction: Territory does not exist.");
  }
}
