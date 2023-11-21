package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class InitResponseWP extends InitResponse {
  public InitResponseWP(int territory_id,
                        PlayerInfo player_info,
                        int num_unit,
                        String name,
                        ArrayList<Integer> neighbors,
                        int food_prod,
                        int tech_prod) {
    super(territory_id, player_info, num_unit, name, neighbors);
    this.food_prod = food_prod;
    this.tech_prod = tech_prod;
  }
}
