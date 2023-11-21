

package edu.duke.ece651.team12.shared;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Response implements Serializable {
  private static final long serialVersionUID = 10;
  protected final int territory_id;
  protected PlayerInfo player_info;
  protected final int num_unit;
  public Response(int territory_id, PlayerInfo player_info, int num_unit) {
    this.territory_id = territory_id;
    this.player_info = player_info;
    this.num_unit = num_unit;
  }

  public int getTerritoryId() { return this.territory_id; }
  public int getNumUnit() { return this.num_unit; }
  public PlayerInfo getPlayerInfo() { return this.player_info; }
}
