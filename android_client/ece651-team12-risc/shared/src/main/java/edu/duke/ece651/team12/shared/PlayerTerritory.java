package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class PlayerTerritory extends Territory {
  public PlayerTerritory(int id,
                         String name,
                         int num_unit,
                         ArrayList<Integer> neighbors,
                         PlayerInfo player_info) {
    super(id, name, num_unit, neighbors, player_info);
  }
}
