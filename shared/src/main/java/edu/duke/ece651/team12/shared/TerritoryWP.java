package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class TerritoryWP extends Territory {
  // WP stands for with productivity

  @Override
  public int getNumUnit() {
    int res = 0;
    for (Integer i : units) {
      res += i;
    }
    return res;
  }

  private static ArrayList<String> genUnitName() {
    ArrayList<String> res = new ArrayList<>();

    res.add("Shirriff");
    res.add("Gondorian Militia");
    res.add("Rohirrim");
    res.add("Dwarvish Warrior");
    res.add("Elvish Archer");
    res.add("Oliphaunt Rider");
    res.add("Balrog");

    return res;
  }

  public static ArrayList<Integer> genUnit(int num_unit) {
    ArrayList<Integer> res = new ArrayList<Integer>();
    res.add(num_unit);
    for (int i = 0; i < 6; i++) {
      res.add(0);
    }
    return res;
  }

  public static int genCount(ArrayList<Integer> units) {
    int ans = 0;
    for (Integer i : units) {
      ans += i;
    }
    return ans;
  }

  public TerritoryWP(int id,
                     String name,
                     int unit,
                     ArrayList<Integer> neighbors,
                     PlayerInfo player_info,
                     int prod_food,
                     int prod_tech) {
    super(id, name, 0, neighbors, player_info);

    this.prod_food = prod_food;
    this.prod_tech = prod_tech;
    this.units.add(unit);
    for (int i = 0; i < 6; i++) {
      this.units.add(0);
    }
  }

  @Override
  public String toString() {
    String res = "";
    ArrayList<String> names = genUnitName();
    res += id + ". " + name + ". Onwer: " + player_info.player_name + "\n";
    res += "Food production: " + prod_food + "\nTech production: " + prod_tech + "\n";
    for (int i = 0; i < 7; i++) {
      res += names.get(i) + ": " + units.get(i) + "\n";
    }
    res += "total: " + genCount(units) + " units";

    return res;
  }

  @Override
  public String displayMyTerritory() {
    String res = this.toString() + "\n" + neighbor() + "\n" + printSpies() +
                 "Cloaking turns: " + this.clockRounds;
    if (hasRailroad()) {
      res += "\nRailways to: ";
      for (int i = 0; i < 24; i++) {
        if (distmodifiers[i] == 1) {
          res += i + ", ";
        }
      }
      res += "\n";
    }
    if (this.ScorchedEarthRounds > 0) {
      res += "\n**************Scorched Earth**************\n";
    }
    return res;
  }

  private boolean hasRailroad() {
    for (int i = 0; i < 24; i++) {
      if (this.distmodifiers[i] == 1) {
        return true;
      }
    }
    return false;
  }
  @Override
  public String displayEnemyTerritory() {
    return this.toString() + "\n" + printSpies();
  }
}
