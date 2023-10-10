package edu.duke.ece651.team12.shared;

public interface RuleChecker {
  public boolean checkRule(Request request, Map map, PlayerInfo player_info);
}
