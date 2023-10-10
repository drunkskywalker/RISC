package edu.duke.ece651.team12.shared;

import java.util.*;
/*
public class MoveAdjacencyChecker implements RuleChecker {
  private RuleChecker next;

  public MoveAdjacencyChecker() { this.next = new BlankChecker(); }

  // find the territories that a player owns
  private HashSet<Territory> findOwnTerritories(Map map, PlayerInfo player_info) {
    HashSet<Territory> t_list = new HashSet<>();
    for (Territory t : map.territories) {
      if (t.player_info.equals(player_info)) {
        t_list.add(t);
      }
    }
    return t_list;
  }

  // find Territory based on ID
  private HashMap<Integer, Territory> findTerritoryfromID(Map map) {
    HashMap<Integer, Territory> kv = new HashMap<>();
    for (Territory t : map.territories) {
      kv.put(t.id, t);
    }
    return kv;
  }

  // Use BFS to traverse the territories to check if there is a path composed of current player's own territories
  private boolean checkReachable(Territory startTerritory,
                                 Territory destinationTerritory,
                                 HashSet<Territory> myTerritories,
                                 HashMap<Integer, Territory> kv) {
    Queue<Territory> queue = new ArrayDeque<>();
    HashSet<Territory> visited = new HashSet<>();

    // initialize queue and visited
    queue.offer(startTerritory);
    visited.add(startTerritory);

    // access the queue
    // pop one Territory every time entering the loop
    while (!queue.isEmpty()) {
      Territory currTerritory = queue.poll();
      // expand the neighboring own territories
      ArrayList<Integer> neighbors = currTerritory.getNeighbors();
      for (int id : neighbors) {
        if (id == destinationTerritory.getId()) {
          return true;
        }
        Territory t = kv.get(id);
        if (visited.contains(t) || !myTerritories.contains(t)) {
          continue;
        }
        visited.add(t);
        queue.offer(t);
      }
    }
    return false;
  }

  @Override
  public boolean checkRule(Request request, Map map, PlayerInfo player_info) {
    // find Territory based on ID
    HashMap<Integer, Territory> kv = findTerritoryfromID(map);
    HashSet<Territory> myTerritories = findOwnTerritories(map, player_info);

    Territory t1 = kv.get(request.getSourceId());
    Territory t2 = kv.get(request.getDestinationId());
    /*
        if(t1 == null || t2 == null) {
            throw new IllegalArgumentException(
                    "Move Invalid Instruction: Territory does not exist.");
        }

        if(!myTerritories.contains(t1)) {
            throw new IllegalArgumentException(
                    "Move Invalid Instruction: This starting territory belongs to another player.");
        }

        if(!myTerritories.contains(t2)) {
            throw new IllegalArgumentException(
                    "Move Invalid Instruction: This destination territory belongs to another player.");
        }

// check if the starting territory can form a path to the destination territory
// through current player's own territories

if (checkReachable(t1, t2, myTerritories, kv)) {
  return next.checkRule(request, map, player_info);
}
else {
  throw new IllegalArgumentException(
      "Move Invalid Instruction: The starting territory cannnot find a path to reach the destination territory.");
}
}
}
*/
