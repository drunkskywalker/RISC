package edu.duke.ece651.team12.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BasicMapFactory implements AbstractMapFactory {
  private ArrayList<PlayerInfo> players;
  private ArrayList<String> territory_names;
  private int num_players;
  private int num_territories;

  public BasicMapFactory(ArrayList<PlayerInfo> players,
                         ArrayList<String> territory_names,
                         int num_players,
                         int num_territories) {
    this.players = players;
    this.territory_names = territory_names;
    this.num_players = num_players;
    this.num_territories = num_territories;
  };

  private static ArrayList<String> genPresetMiddleEarth(int size) {
    if (size == 6) {
      return genSmallPresetMiddleEarth();
    }
    else {  // if (size == 24) {
      return genLargePresetMiddleEarth();
    }

    // return null;
  }

  private static ArrayList<String> genLargePresetMiddleEarth() {
    ArrayList<String> ans = new ArrayList<String>();

    ans.add("The Shire");
    ans.add("Bree");
    ans.add("Rivendell");
    ans.add("Weather Hills");
    ans.add("Lake Evendim");
    ans.add("Forodwaith");
    ans.add("Grey Mountains");
    ans.add("Iron Hills");
    ans.add("Erebor");
    ans.add("Lorien");
    ans.add("Misty Mountains");
    ans.add("Mirkwood");
    ans.add("Moria");
    ans.add("Edoras");
    ans.add("Helm's Deep");
    ans.add("Fangorn Forest");
    ans.add("Dunland");
    ans.add("Anorien");
    ans.add("Lebennin");
    ans.add("Minas Tirith");
    ans.add("Minas Morgul");
    ans.add("Mount Doom");
    ans.add("Barad Dur");
    ans.add("Mountains of Shadow");
    return ans;
  }

  private static ArrayList<String> genSmallPresetMiddleEarth() {
    ArrayList<String> ans = new ArrayList<String>();
    ans.add("Shire");
    ans.add("Rivendell");
    ans.add("Lorien");
    ans.add("Misty Mountains");
    ans.add("Mirkwood");
    ans.add("Mount Doom");
    return ans;
  }

  public BasicMapFactory(ArrayList<PlayerInfo> players,
                         int num_players,
                         int num_territories) {
    this(players, genPresetMiddleEarth(num_territories), num_players, num_territories);
  }

  private int[][] genAdjacency(int size) {
    if (size == 6) {
      return genSmallPresetAdjacency();
    }

    else {  // if (size == 24) {
      return genRegularAdjacency();
    }
    // return null;
  }

  private int[][] genSmallPresetAdjacency() {
    String[] territories = {
        "Shire", "Rivendell", "Lorien", "Misty Mountains", "Mirkwood", "Mount Doom"};
    HashMap<String, Integer> t = new HashMap<>();
    int num_territories = territories.length;
    for (int i = 0; i < num_territories; i++) {
      t.put(territories[i], i);
    }
    int[][] adjacency = new int[num_territories][num_territories];
    adjacency[t.get("Shire")][t.get("Rivendell")] = 1;
    adjacency[t.get("Shire")][t.get("Mount Doom")] = 1;
    adjacency[t.get("Rivendell")][t.get("Shire")] = 1;
    adjacency[t.get("Rivendell")][t.get("Mount Doom")] = 1;
    adjacency[t.get("Rivendell")][t.get("Lorien")] = 1;
    adjacency[t.get("Rivendell")][t.get("Misty Mountains")] = 1;
    adjacency[t.get("Rivendell")][t.get("Mirkwood")] = 1;
    adjacency[t.get("Lorien")][t.get("Rivendell")] = 1;
    adjacency[t.get("Lorien")][t.get("Misty Mountains")] = 1;
    adjacency[t.get("Misty Mountains")][t.get("Rivendell")] = 1;
    adjacency[t.get("Misty Mountains")][t.get("Lorien")] = 1;
    adjacency[t.get("Misty Mountains")][t.get("Mirkwood")] = 1;
    adjacency[t.get("Mirkwood")][t.get("Rivendell")] = 1;
    adjacency[t.get("Mirkwood")][t.get("Misty Mountains")] = 1;
    adjacency[t.get("Mount Doom")][t.get("Shire")] = 1;
    adjacency[t.get("Mount Doom")][t.get("Rivendell")] = 1;

    return adjacency;
  }

  private int[][] genRegularAdjacency() {
    Random rand = new Random(42);
    double connection_probability = 0.4;
    int num_territories = 24;
    int[][] adjacency = new int[num_territories][num_territories];
    for (int i = 0; i < num_territories; i++) {
      for (int j = 0; j < num_territories; j++) {
        int isAdjacent = 0;
        if (i > j) {
          isAdjacent = adjacency[j][i];
        }
        if (i < j) {
          if (i == (j - 1)) {
            isAdjacent = 1;
          }
          else {
            isAdjacent = rand.nextFloat() < connection_probability ? 1 : 0;
          }
        }
        adjacency[i][j] = isAdjacent;
      }
    }
    return adjacency;
  }

  /*
  private boolean isValid() {
    if (num_territories % num_players != 0) {
      return false;
    }
    if (num_territories > territory_names.size()) {
      return false;
    }
    return true;
  }
  */
  /*
  public ArrayList<InitResponse> createInitResponses(ArrayList<PlayerInfo> players,
                                                     ArrayList<String> territory_names) {
    return createInitResponses(players, territory_names, 24);
  }
  */

  // overload function
  public ArrayList<InitResponse> createInitResponses() { return createInitResponses(24); }

  public ArrayList<InitResponse> createInitResponses(int size) {
    return createInitResponses(size, true);
  }

  public ArrayList<InitResponse> createInitResponses(int size, boolean isCustomUnit) {
    //   if (!isValid()) {
    // return null;
    // }
    int[][] adj = genAdjacency(size);
    // int[][] adj = genAdjacency(24);
    int num_players = players.size();
    int num_territories_per_player = num_territories / num_players;
    ArrayList<InitResponse> res = new ArrayList<InitResponse>();

    for (int i = 0; i < num_territories; i++) {
      ArrayList<Integer> neighbors = new ArrayList<Integer>();
      for (int j = 0; j < num_territories; j++) {
        if (adj[i][j] == 1) {
          neighbors.add(j);
        }
      }

      InitResponse ir = null;
      if (isCustomUnit) {
        ir = new InitResponse(i,
                              players.get(i / num_territories_per_player),
                              0,
                              territory_names.get(i),
                              neighbors);
      }
      else {
        ir = new InitResponse(i,
                              players.get(i / num_territories_per_player),
                              5,
                              territory_names.get(i),
                              neighbors);
      }
      res.add(ir);
    }
    return res;
  }
}
