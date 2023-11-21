package edu.duke.ece651.team12.shared;
import java.io.*;
import java.util.*;
public class ServerMap extends Map {
  public ServerMap(String name) { super(name); }

  /** initializes the Map using a buffered reader.
   * @param bfr is the buffered reader we want to construct our map with.
   */
  public void initialize(BufferedReader bfr) {}
  // Placeholder
  public void initialize() {}

  @Override
  public String toString() {
    String re = "";
    for (Territory t : territories) {
      re += t.toString();
    }
    return re;
  }

  public ArrayList<TurnResponse> generateResonse() {
    ArrayList<TurnResponse> trs = new ArrayList<TurnResponse>();
    for (Territory t : territories) {
      trs.add(new TurnResponse(t.id,
                               t.player_info,
                               (ArrayList<Integer>)t.units.clone(),
                               food_accus.get(t.player_info),
                               tech_accus.get(t.player_info),
                               tech_lvls.get(t.player_info),
                               (ArrayList<Spy>)t.spies.clone(),
                               t.clockRounds,
                               t.ScorchedEarthRounds,
                               (int[])t.distmodifiers.clone()));
    }

    return trs;
  }
}
