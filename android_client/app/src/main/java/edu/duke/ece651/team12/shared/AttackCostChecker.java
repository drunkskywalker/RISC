package edu.duke.ece651.team12.shared;

public class AttackCostChecker implements RuleChecker {
  private RuleChecker next;
  public AttackCostChecker() { this.next = new BlankChecker(); }


  /*
    a. It calculates the cost of the attack by multiplying the distance between the source and destination territories, found in the map.distances array, by the total number of generated units involved in the attack
     
   b. If the calculated cost exceeds the player's food stockpile, an IllegalArgumentException is thrown with a message stating that the food stockpile cannot support the attack order. If the player has enough food in their stockpile, the method proceeds to call the checkRule method of the next rule checker in the chain.*/
  
  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    int cost = map.distances[request.getSourceId()][request.getDestinationId()] *
               TerritoryWP.genCount(request.units);
    int storage = map.food_accus.get(player_info);
    if (cost > storage) {
      throw new IllegalArgumentException(
          "Attack Invalid Instruction: Food stockpile cannot support this order.");
    }
    else {
      return next.checkRule(request, map, player_info);
    }
  }
}
