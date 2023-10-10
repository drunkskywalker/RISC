package edu.duke.ece651.team12.shared;

public class BlankChecker implements RuleChecker {
  public BlankChecker() {}
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    return true;
  }
}
