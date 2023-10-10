package edu.duke.ece651.team12.shared;

public class SpyMoveRepeatChecker implements RuleChecker {
  private RuleChecker next;
  public SpyMoveRepeatChecker() { this.next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Territory source = map.territories.get(request.getSourceId());
    for (Spy s : source.spies) {
      if (s.owner.equals(player_info) && !s.hasMoved) {
        System.out.println("SM repeat check passed");
        return next.checkRule(request, map, player_info);
      }
    }
    throw new IllegalArgumentException(
        "Spy Move Invalid Instruction: All spies have moved in this turn in this territory.");
  }
}
