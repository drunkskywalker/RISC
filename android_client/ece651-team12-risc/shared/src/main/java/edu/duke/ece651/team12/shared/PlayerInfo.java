package edu.duke.ece651.team12.shared;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
  private static final long serialVersionUID = 16;
  public final int player_id;
  public final String player_name;
  public final Color color;
  public PlayerInfo(int player_id, String player_name, Color color) {
    this.player_id = player_id;
    this.player_name = player_name;
    this.color = color;
  }

  public PlayerInfo(int id) { this(id, "test" + id, new Color(0, 0, 0)); }

  @Override
  public boolean equals(Object rhs) {
    if (rhs != null && rhs.getClass().equals(this.getClass())) {
      PlayerInfo rhsPI = (PlayerInfo)rhs;
      return (this.player_id == rhsPI.player_id &&
              this.player_name.equals(rhsPI.player_name) &&
              this.color.equals(rhsPI.color));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public String toString() {
    String res = "";

    res += "ID: ";
    res += player_id;
    res += "\nName: ";
    res += player_name;
    res += "\nColor: ";
    res += color;
    return res;
  }
}
