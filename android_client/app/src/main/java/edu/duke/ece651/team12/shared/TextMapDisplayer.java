package edu.duke.ece651.team12.shared;
import java.util.ArrayList;
import java.util.HashSet;
public class TextMapDisplayer {  // implements MapDisplayer {
  public final Map to_display;
  public final PlayerInfo player_info;

  public TextMapDisplayer(Map to_display, PlayerInfo player_info) {
    this.to_display = to_display;
    this.player_info = player_info;
  }

  /** displays the map in string form.
   */

  public String display() {
    String res = "";
    res += to_display.name;
    res += " has ";
    res += to_display.territories.size();
    res += " territoires, controlled by ";
    HashSet<PlayerInfo> players = new HashSet<PlayerInfo>();
    for (Territory t : to_display.territories) {
      if (!t.getPlayerInfo().equals(this.player_info)) {
        players.add(t.getPlayerInfo());
      }
    }
    res += (players.size() + 1);
    res += " players.\nYour territories:\n";
    for (Territory t : to_display.territories) {
      if (t.getPlayerInfo().equals(this.player_info)) {
        res += t.displayMyTerritory();
        res += "\n";
      }
    }
    res += "\n====================\n";

    res += "\nEnemy Territorires:\n\n";
    for (PlayerInfo pi : players) {
      for (Territory t : to_display.territories) {
        if (t.getPlayerInfo().equals(pi)) {
          if (to_display.hasVision(t, this.player_info)) {
            res += t.displayEnemyTerritory();
            res += "\n";
          }
          else if (to_display.old_info.containsKey(t.id)) {
            res += "\nUsing old intel:\n";
            res += to_display.old_info.get(t.id).displayEnemyTerritory();
            res += "\n";
          }
          else {
            res += "you have no intel on territory " + t.id + ".\n";
          }
        }
      }
      res += "\n====================\n\n";
    }

    return res;
  }

  /** displays the adjacency matrix. 
   */
  public String adjacency() {
    int size = to_display.territories.size();
    String re = "Adjacency Matrix:\n\n   |";
    for (int i = 0; i < size; i++) {
      re += "0";
      if (i < 10) {
        re += "0";
      }
      re += i;
      re += "|";
    }
    re += "\n";
    for (int i = 0; i < size; i++) {
      Territory t = to_display.territories.get(i);
      re += "0";
      if (i < 10) {
        re += "0";
      }
      re += i;
      re += "|";
      for (int j = 0; j < size; j++) {
        if (t.isAdjacentTo(j)) {
          re += " * |";
        }
        else {
          re += "   |";
        }
      }
      re += "0";
      if (i < 10) {
        re += "0";
      }
      re += i;

      re += "\n";
    }
    return re;
  }

  public String endTurnInfo(ArrayList<TurnResponse> responses) {
    String res = "Another sunset upon the land of " + to_display.name + ".\n";
    for (Territory t : to_display.territories) {
      for (TurnResponse r : responses) {
        if (t.id == r.territory_id) {
          if (t.player_info.equals(r.player_info)) {
            String own, sub;
            if (t.player_info.equals(this.player_info)) {
              own = "You remain ";
              sub = ", now with the force of " + r.num_unit + " unit(s);\n";
            }
            else {
              own = t.player_info.player_name;
              own += " remains ";
              sub = ";\n";
            }
            res += own + "control of " + t.name + sub;
          }
          else if (r.player_info.equals(this.player_info)) {
            res += "You have now expelled " + t.player_info.player_name +
                   "'s forces from the land of " + t.name + ", with " + r.num_unit +
                   " unit(s) securing the territory;\n";
          }
          else if (t.player_info.equals(this.player_info)) {
            res += "The enemy, " + r.player_info.player_name +
                   ", have driven you out of " + t.name + ";\n";
          }
          else {
            res += r.player_info.player_name + " has driven " +
                   t.player_info.player_name + " out of " + t.name + ";\n";
          }
          break;
        }
      }
      continue;
    }
    return res;
  }

  public String info() {
    String res = ""
                 + "Your tech level: " + to_display.tech_lvls.get(player_info);
    return res;
  }
}
