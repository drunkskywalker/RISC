/**************************

Concerning vision update: on the start of each turn the vision is updated adn will remain that way until next turn.
Spy move orders will take effect next turn in order not to intefere with hasVision().
If a player has vision on a territory, its info is retreived directly. 
If a player does not have vision current;y on a territory but previously had, its info is retreived in old_info with territory id as key.
If a player has never seen a territory before, it only gets the id. 

Concerning gameplay: newly added methods gen*order*. notice that the gen*order*s are still wraped in try-catch blocks. to get details, let them throw.
Spy train, railway sabotage, scorched earth, cloak should resemble (somehow) unit upgrade. notice that railway sabotage is only valid on enemy territories.
railway construct should resemble unit move. spy move should resemble attack.

Remember to add rulecheckers to the global params, and the end turn method calls to update vision and distances.

/**************************/

package edu.duke.ece651.team12.shared;
import java.util.*;
public abstract class Map {
  public int[][] distances = V2MapFactory.genDistance();
  public HashMap<PlayerInfo, Integer> food_accus = new HashMap<>();
  public HashMap<PlayerInfo, Integer> tech_accus = new HashMap<>();
  public HashMap<PlayerInfo, Integer> tech_lvls = new HashMap<>();
  public HashMap<PlayerInfo, Boolean> research_barrier = new HashMap<>();
  public ArrayList<Territory> territories;
  public HashMap<Integer, Territory> old_info = new HashMap<>();
  private ResponseHandler response_handler;

  public final String name;

  public Map(String name) {
    this.territories = new ArrayList<Territory>();
    this.name = name;
    this.response_handler = new ResponseHandler();
  }

  public void init(InitResponse ir) {
    Territory t = response_handler.handleInitResponse(ir);
    territories.add(t);
    food_accus.put(ir.getPlayerInfo(), 0);
    tech_accus.put(ir.getPlayerInfo(), 0);
    tech_lvls.put(ir.getPlayerInfo(), 0);
    research_barrier.put(ir.getPlayerInfo(), false);
  }

  public void updateVision(PlayerInfo p) {
    for (Territory t : territories) {
      if (hasVision(t, p)) {
        try {
          old_info.put(t.getId(), (Territory)t.clone());
        }
        catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void update(ArrayList<TurnResponse> tr) {
    for (Territory t : territories) {
      for (TurnResponse r : tr) {
        if (r.getTerritoryId() == t.id) {
          t.distmodifiers = r.modifiers;

          response_handler.handleTurnResponse(t, r);
          response_handler.verifyStorage(this, r, r.getPlayerInfo());
          t.update(r.getPlayerInfo(), r.getNumUnit(), r.units);
          t.spies = (ArrayList<Spy>)r.spies.clone();
          t.clockRounds = r.cloak_rounds;
          t.ScorchedEarthRounds = r.scr_rounds;
          t.distmodifiers = r.modifiers;
          if (t.ScorchedEarthRounds > 0) {
            for (Integer n : t.neighbors) {
              this.distances[t.id][n] = 5;
              this.distances[n][t.id] = 5;
            }
          }
        }
      }
    }
  }

  public int getFoodProduct(PlayerInfo p) {
    int ans = 0;
    for (Territory t : territories) {
      if (t.getPlayerInfo().equals(p) && t.ScorchedEarthRounds == 0) {
        ans += t.prod_food;
      }
    }
    return ans;
  }

  public int getTechProduct(PlayerInfo p) {
    int ans = 0;
    for (Territory t : territories) {
      if (t.getPlayerInfo().equals(p) && t.ScorchedEarthRounds == 0) {
        ans += t.prod_tech;
      }
    }
    return ans;
  }

  public int[][] shortestDistance(PlayerInfo p) {
    int[][] ans = new int[24][24];

    for (int i = 0; i < 24; i++) {
      for (int j = 0; j < 24; j++) {
        if (!territories.get(i).belongsTo(p) || !territories.get(j).belongsTo(p)) {
          ans[i][j] = Integer.MAX_VALUE;
        }
        else if (territories.get(i).distmodifiers[j] == 1) {
          ans[i][j] = 0;
        }
        else {
          ans[i][j] = distances[i][j];
        }
      }
    }
    for (int i = 0; i < 24; i++) {
      for (int j = 0; j < 24; j++) {
        for (int k = 0; k < 24; k++) {
          if (ans[i][k] != Integer.MAX_VALUE && ans[k][j] != Integer.MAX_VALUE &&
              ans[i][j] > ans[i][k] + ans[k][j]) {
            int newdist = ans[i][k] + ans[k][j];
            ans[i][j] = newdist;
          }
        }
      }
    }
    return ans;
  }

  public void updateProduction(PlayerInfo p) {
    if (food_accus.get(p) != null) {
      Integer n = food_accus.get(p) + getFoodProduct(p);
      food_accus.put(p, n);
    }
    if (tech_accus.get(p) != null) {
      Integer n = tech_accus.get(p) + getTechProduct(p);
      tech_accus.put(p, n);
    }
  }

  /* returns if a user has visiion on the territory
   */
  public boolean hasVision(Territory t, PlayerInfo p) {
    if (t.belongsTo(p)) {
      return true;
    }
    for (Spy s : t.spies) {
      if (s.owner.equals(p)) {
        return true;
      }
    }
    if (t.clockRounds > 0) {
      return false;
    }
    for (Territory ts : this.territories) {
      if (ts.isAdjacentTo(t) && ts.belongsTo(p)) {
        return true;
      }
    }
    return false;
  }

  /* returns a list of spies belonging to a user
   */
  public ArrayList<Spy> getSpies(PlayerInfo p) {
    ArrayList<Spy> spies = new ArrayList<>();
    for (Territory t : territories) {
      for (Spy s : t.spies) {
        if (s.owner.equals(p)) {
          spies.add(s);
        }
      }
    }
    return spies;
  }
}
