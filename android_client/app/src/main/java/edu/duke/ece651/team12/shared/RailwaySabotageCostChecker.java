package edu.duke.ece651.team12.shared;

public class RailwaySabotageCostChecker implements RuleChecker {
  private RuleChecker next;
  public RailwaySabotageCostChecker() { this.next = new BlankChecker(); }
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    Integer accu = map.tech_accus.get(player_info);
    int adjs = 0;
    Territory t0 = map.territories.get(request.getSourceId());

    for (Territory t : map.territories) {
      // is adjacent, does not partially belong to the issuer, and has railway
      if (t.isAdjacentTo(t0) && !t.belongsTo(player_info) &&
          t.distmodifiers[t0.id] == 1) {
        adjs += 5;
      }
    }

    if (adjs > accu) {
      throw new IllegalArgumentException(
          "Railway Sabotage Invalid Instruction: You don't have enough tech points to sabotage the railway.");
    }
    return next.checkRule(request, map, player_info);
  }
}
