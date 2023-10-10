package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class TurnResponse extends Response {
  protected ArrayList<Integer> units = new ArrayList<>();
  public int food_accu;
  public int tech_accu;
  public int tech_lvl;
  public int cloak_rounds;
  public int scr_rounds;
  public ArrayList<Spy> spies;

  public int[] modifiers;

  private static final long serialVersionUID = 1110;

  public TurnResponse(int territory_id,
                      PlayerInfo player_info,
                      ArrayList<Integer> units,
                      int food_accu,
                      int tech_accu,
                      int tech_lvl) {
    this(territory_id,
         player_info,

         units,
         food_accu,
         tech_accu,
         tech_lvl,
         new ArrayList<>(),
         0,
         0,
         new int[24]);
  }

  public TurnResponse(int territory_id,
                      PlayerInfo player_info,
                      int num_unit,
                      ArrayList<Integer> units,
                      int food_accu,
                      int tech_accu,
                      int tech_lvl,
                      ArrayList<Spy> spies,
                      int cloakR,
                      int scrR,
                      int[] modifiers) {
    super(territory_id, player_info, num_unit);
    this.food_accu = food_accu;
    this.tech_accu = tech_accu;
    this.units = units;
    this.tech_lvl = tech_lvl;
    this.spies = spies;
    this.cloak_rounds = cloakR;
    this.scr_rounds = scrR;
    this.modifiers = modifiers;
  }
  public TurnResponse(int territory_id, PlayerInfo player_info, int num_unit) {
    this(territory_id,
         player_info,
         num_unit,
         TerritoryWP.genUnit(num_unit),
         0,
         0,
         0,
         new ArrayList<Spy>(),
         0,
         0,
         new int[24]);
  }

  public TurnResponse(int territory_id,
                      PlayerInfo player_info,
                      ArrayList<Integer> units,
                      int food_accu,
                      int tech_accu,
                      int tech_lvl,
                      ArrayList<Spy> spies,
                      int cloakR,
                      int scrR,
                      int[] modifiers) {
    this(territory_id,
         player_info,
         TerritoryWP.genCount(units),
         units,
         food_accu,
         tech_accu,
         tech_lvl,
         spies,
         cloakR,
         scrR,
         modifiers);
  }

  @Override
  public String toString() {
    String res = "" + territory_id + ", owned by " + player_info.toString() + ", with " +
                 TerritoryWP.genCount(units) + " units. ";
    return res;
  }
}
