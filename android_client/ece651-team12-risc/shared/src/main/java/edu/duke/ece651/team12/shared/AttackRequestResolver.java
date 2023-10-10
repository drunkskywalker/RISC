package edu.duke.ece651.team12.shared;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
public class AttackRequestResolver implements RequestResolver {
  private ServerMap map;
  private final int dice;
  private PrintStream out;
  public AttackRequestResolver(ServerMap map, int dice, PrintStream out) {
    this.map = map;
    this.dice = dice;
    this.out = out;
    buffs.add(0);
    buffs.add(1);
    buffs.add(3);
    buffs.add(5);
    buffs.add(8);
    buffs.add(11);
    buffs.add(15);
  }

  private ArrayList<Integer> buffs = new ArrayList<>();

  /* selects all requests targeting the territory.
   * @returns a list of qualified requests.
   */
  private ArrayList<Request> select(int territoryID, ArrayList<Request> requests) {
    ArrayList<Request> req = new ArrayList<Request>();
    for (Request r : requests) {
      if (r.getDestinationId() == territoryID) {
        req.add(r);
      }
    }
    return req;
  }

  /* resolves the unit loss. removes combatant from battle if units are all spent.
   * @returns true if the loser has no units.
   */

  private boolean loseFight(int x,
                            int combatant,
                            ArrayList<ArrayList<Integer> > units,
                            ArrayList<PlayerInfo> ids) {
    /*
    out.println("Player " + ids.get(x).player_name + " lost the fight.");
    Integer temp = units.get(x);
    temp--;
    units.set(x, temp);
    if (temp == 0) {
      out.println(
          "Player " + ids.get(x).player_name +
          " was forced to withdraw from the battle, having lost all their units on the front.");
      units.remove(x);
      ids.remove(x);
    }

    out.println("==============");
    */

    out.println(ids.get(x).player_name + " loses skirmish with level " + combatant +
                " unit.");
    ArrayList<Integer> lost_army = units.get(x);
    int lost_unit = lost_army.get(combatant);
    lost_unit--;
    lost_army.set(combatant, lost_unit);
    units.set(x, lost_army);
    if (TerritoryWP.genCount(lost_army) == 0) {
      out.println(ids.get(x).player_name + " withdraws.");

      units.remove(x);
      ids.remove(x);
      return true;
    }
    else {
      out.println(ids.get(x).player_name + " continues fight with " +
                  TerritoryWP.genCount(lost_army) + " units.");
    }
    out.println("Fight continue.");
    return false;
  }

  /* merges two request's units, combine their forces.
   * @returns the combined arraylist.
   */

  private ArrayList<Integer> merge(ArrayList<Integer> a1, ArrayList<Integer> a2) {
    out.println("a1 length: " + a1.size() + "; a2 length: " + a2.size());
    ArrayList<Integer> ans = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      ans.add(a1.get(i) + a2.get(i));
    }
    return ans;
  }

  /* resolves a fight. attacker represents the side with inferior units. defender the superior.
   * @param units is the arraylist of all involved parties units.
   */

  private int fight(int att,
                    int def,
                    ArrayList<ArrayList<Integer> > units,
                    ArrayList<PlayerInfo> ids) {
    Random ran = new Random();

    out.println(ids.get(def).player_name + " gets advantage. ");
    int att_dice = ran.nextInt(dice) + 1;
    out.println("Weak side throws " + att_dice);
    int def_dice = ran.nextInt(dice) + 1;
    out.println("Strong side throws " + def_dice);
    int def_combat = -1;
    int atk_combat = -1;

    for (int u = 6; u >= 0; u--) {
      out.println("Defender has lvl " + u + " units number: " + units.get(def).get(u));
      if (units.get(def).get(u) > 0) {
        def_combat = u;
        def_dice += buffs.get(def_combat);
        out.println("Strong side gets lvl " + u + " unit. final dice: " + def_dice +
                    ". remaining such level units: " + units.get(def).get(u));
        break;
      }
    }
    for (int u = 0; u < 7; u++) {
      out.println("Attacker has lvl " + u + " units number: " + units.get(att).get(u));
      if (units.get(att).get(u) > 0) {
        atk_combat = u;
        att_dice += buffs.get(atk_combat);
        out.println("Weak side gets lvl " + u + " unit. final dice: " + att_dice +
                    ". remaining such level units: " + units.get(att).get(u));
        break;
      }
    }

    if (att_dice > def_dice) {
      out.println("attacker wins with lvl " + atk_combat + " unit, over defender's lvl " +
                  def_combat + " unit.");
      if (loseFight(def, def_combat, units, ids)) {
        return def;
      }
    }
    else {
      out.println("defender wins with lvl " + def_combat + " unit, over attacker's lvl " +
                  atk_combat + " unit.");
      if (loseFight(att, atk_combat, units, ids)) {
        return att;
      }
    }
    return -1;
  }

  /* resolves food cost.
   */
  private void moveOut(ArrayList<Request> requests) {
    for (Request r : requests) {
      out.println("Moving " + TerritoryWP.genCount(r.units) + " Units from " +
                  r.getSourceId() + " to " + r.getDestinationId() + " each costs " +
                  map.distances[r.getSourceId()][r.getDestinationId()] +
                  " units of food.");
      int cost = map.distances[r.getSourceId()][r.getDestinationId()] *
                 TerritoryWP.genCount(r.units);
      int food = map.food_accus.get(r.getIssuerId()) - cost;
      map.food_accus.put(r.getIssuerId(), food);
      Territory t = map.territories.get(r.getSourceId());
      for (int i = 0; i < 7; i++) {
        Integer sourcex = t.units.get(i);
        sourcex -= r.units.get(i);
        t.units.set(i, sourcex);
      }
    }
  }

  /* resolves all attack requests targeting the given territory.
   * @param t is the target territory.
   */
  private void combat(Territory t, ArrayList<Request> requests) {
    // Territory t = map.territories.get(territoryID);
    ArrayList<Integer> defender_unit = t.units;
    PlayerInfo defender_id = t.getPlayerInfo();
    ArrayList<ArrayList<Integer> > units = new ArrayList<>();

    units.add(defender_unit);
    ArrayList<PlayerInfo> ids = new ArrayList<PlayerInfo>();
    ids.add(defender_id);
    for (Request r : requests) {
      if (ids.contains(r.getIssuerId())) {
        int ididx = ids.indexOf(r.getIssuerId());
        ArrayList<Integer> u = units.get(ididx);

        units.set(ididx, merge(u, r.units));
      }
      else {
        units.add(r.units);
        ids.add(r.getIssuerId());
      }
    }
    out.println("===Battle of " + t.getName() + "===");
    out.println("Defender: " + ids.get(0).player_name + ", with " +
                TerritoryWP.genCount(units.get(0)) + " units.\n"
                + "Attacker(s): ");
    for (int k = 1; k < ids.size(); k++) {
      out.println(ids.get(k).player_name + ", with " +
                  TerritoryWP.genCount(units.get(k)) + " units.");
    }
    out.println("Battle Start.\n===============================");
    if (TerritoryWP.genCount(defender_unit) == 0) {
      units.remove(0);
      ids.remove(0);
      out.println("Defender abandons post.");
    }
    int i = 0;

    while (units.size() > 1) {
      int j = i + 1;
      if (j == units.size()) {
        j = 0;
      }
      out.println("Skirmish breaks out between " + ids.get(i).player_name + " and " +
                  ids.get(j).player_name);
      // Original defender is always defender. When original defender is not involved, player i is the defender and j is the attacker.

      if (ids.get(j).equals(defender_id)) {
        out.println("Original Defender is always the defender.");
        if (fight(i, j, units, ids) != -1) {
          i++;
          if (i >= units.size()) {
            i = 0;
          }
        }
        else {
          fight(j, i, units, ids);
        }
      }
      else {
        if (fight(j, i, units, ids) != -1) {
          i++;
          if (i >= units.size()) {
            i = 0;
          }
          continue;
        }
        else {
          fight(i, j, units, ids);
        }
      }

      i++;
      if (i >= units.size()) {
        i = 0;
      }
    }

    String is_defending;
    if (ids.get(0).equals(defender_id)) {
      is_defending = "holding their position in the territory.";
    }
    else {
      is_defending = "assuming control of the territory.";
    }
    out.println(ids.get(0).player_name + " won the Battle of " + t.getName() + ", " +
                is_defending);

    t.update(ids.get(0), TerritoryWP.genCount(units.get(0)), units.get(0));
    return;
  }

  /* resolves all requests.
   */
  @Override
  public void resolve(ArrayList<Request> requests) {
    HashSet<Territory> quietTerritories = new HashSet<>();
    for (Territory t : map.territories) {
      ArrayList<Request> t_res = select(t.getId(), requests);
      if (t_res.size() == 0) {
        out.println("All quiet on the " + t.getName() + " front.");
        quietTerritories.add(t);
      }
    }

    moveOut(requests);

    for (Territory t : map.territories) {
      if (!quietTerritories.contains(t)) {
        ArrayList<Request> t_res = select(t.getId(), requests);
        combat(t, t_res);
      }
    }
  }
}
