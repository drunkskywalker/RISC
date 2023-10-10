package edu.duke.ece651.team12.shared;

import java.util.ArrayList;
public abstract class Territory implements Cloneable {
  protected final int id;
  protected final String name;
  protected final ArrayList<Integer> neighbors;
  protected int num_unit;
  protected PlayerInfo player_info;
  public ArrayList<Integer> units = new ArrayList<>();
  protected int prod_food = 0;
  protected int prod_tech = 0;
  public ArrayList<Spy> spies = new ArrayList<>();
  public int clockRounds = 0;
  public int ScorchedEarthRounds = 0;
  public int[] distmodifiers = new int[24];

  public Object clone() throws CloneNotSupportedException {
    Territory t = (Territory)super.clone();
    return t;
  }

  public void setUnits(ArrayList<Integer> u) {
    this.units = new ArrayList<>();
    for (Integer x : u) {
      units.add(x);
    }
  }

  public Territory(int id,
                   String name,
                   int num_unit,
                   ArrayList<Integer> neighbors,
                   PlayerInfo player_info) {
    this.id = id;
    this.name = name;
    this.num_unit = num_unit;
    this.neighbors = neighbors;
    this.player_info = player_info;
  }

  public int getId() { return this.id; }
  public int getNumUnit() { return this.num_unit; }
  public String getName() { return this.name; }
  //  public ArrayList<Integer> getNeighbors() { return this.neighbors; }

  public boolean isAdjacentTo(Territory target) {
    return this.neighbors.contains(target.getId());
  }

  public boolean isAdjacentTo(int id) { return this.neighbors.contains(id); }

  public PlayerInfo getPlayerInfo() { return this.player_info; }
  public void setPlayerInfo(PlayerInfo new_info) { this.player_info = new_info; }
  public void setNumUnit(int new_num_unit) { this.num_unit = new_num_unit; }

  @Override
  public String toString() {
    String res = "";
    res += "ID: ";
    res += id;
    res += ". Name: ";
    res += name;
    res += ". Units: ";
    res += num_unit;
    res += ". Owner: ";
    res += player_info.player_name;
    return res;
  }

  protected String neighbor() {
    String res = "Neighbors: ";
    for (Integer i : this.neighbors) {
      res += i;
      res += " ";
    }
    return res;
  }

  protected String printSpies() {
    String res = "Spys:\n";
    for (Spy s : this.spies) {
      res += "a spy owned by: ";
      res += s.owner.player_name + ",";
      if (s.hasMoved) {
        res += " not";
      }
      res += " ready to move;";
      res += "\n";
    }
    return res;
  }
  public String displayMyTerritory() {
    return this.toString() + "\n" + neighbor() + printSpies();
  }
  public String displayEnemyTerritory() { return this.toString(); }

  public void update(PlayerInfo winner, int num_units, ArrayList<Integer> units) {
    this.player_info = winner;
    this.num_unit = num_units;
    setUnits(units);
  }

  public boolean belongsTo(PlayerInfo p) { return getPlayerInfo().equals(p); }

  public void modify_dist(int target, int amount) { distmodifiers[target] = amount; }
}
