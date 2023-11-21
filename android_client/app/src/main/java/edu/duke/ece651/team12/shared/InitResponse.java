package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class InitResponse extends Response {
  private final String name;
  private ArrayList<Integer> neighbors;
  private static final long serialVersionUID = 14;
  public int food_prod = 0;
  public int tech_prod = 0;
  public InitResponse(int territory_id,
                      PlayerInfo player_info,
                      int num_unit,
                      String name,
                      ArrayList<Integer> neighbors) {
    super(territory_id, player_info, num_unit);
    this.name = name;
    this.neighbors = neighbors;
  }

  public String getName() { return this.name; }
  public ArrayList<Integer> getNeighbors() { return this.neighbors; }
}
