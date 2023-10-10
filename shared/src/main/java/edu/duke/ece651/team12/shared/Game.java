package edu.duke.ece651.team12.shared;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class Game {
  private final int num_player;
  private BufferedReader input_source;
  private PrintStream out;
  public ArrayList<PlayerInfo> players;
  private static ArrayList<Color> colors;
  private ServerMap map;
  private AttackRequestResolver arr;
  private MoveRequestResolver mrr;
  private UpgradeRequestResolver urr;
  private SpyTrainRequestResolver scrr;
  private SpyMoveRequestResolver smrr;
  private CloakRequestResolver crr;
  private RailwayConstructRequestResolver rcrr;
  private RailwaySabotageRequestResolver rsrr;
  private ScorchedEarthRequestResolver serr;
  private BasicMapFactory bmf;
  private int init_unit;
  private int num_territory;
  private RuleChecker moc = new MoveOwnershipChecker();
  private RuleChecker aoc = new AttackOwnershipChecker();
  private RuleChecker rrc = new ResearchDuplicationChecker();
  private RuleChecker uoc = new UpgradeOwnershipChecker();
  private RuleChecker scc = new SpyTrainOwnershipChecker();
  private RuleChecker smc = new SpyMoveOwnershipChecker();
  private RuleChecker crc = new CloakOwnershipChecker();
  private RuleChecker rrcc = new RailwayConstructOwnershipChecker();
  private RuleChecker rscc = new RailwaySabotageOwnershipChecker();
  private RuleChecker sec = new ScorchedEarthOwnershipChecker();
  public Game(int num_player,
              BufferedReader bfr,
              PrintStream out,
              String map_name,
              int init_unit,
              int num_territory) {
    this.num_player = num_player;
    this.input_source = bfr;
    this.out = out;
    this.players = new ArrayList<>();
    this.init_unit = init_unit;

    this.num_territory = num_territory;
    colors = new ArrayList<Color>();
    colors.add(new Color(255, 0, 0));
    colors.add(new Color(0, 255, 0));
    colors.add(new Color(0, 0, 255));
    colors.add(new Color(255, 255, 0));
    map = new ServerMap(map_name);
    mrr = new MoveRequestResolver(map, out);
    arr = new AttackRequestResolver(map, 20, out);
    urr = new UpgradeRequestResolver(map, out);
    scrr = new SpyTrainRequestResolver(map, out);
    smrr = new SpyMoveRequestResolver(map, out);
    crr = new CloakRequestResolver(map, out);
    rcrr = new RailwayConstructRequestResolver(map, out);
    rsrr = new RailwaySabotageRequestResolver(map, out);
    serr = new ScorchedEarthRequestResolver(map, out);

    out.println("=================\nInitiating RISC. World name: " + map_name +
                ", number of participants: " + num_player + ".\n=================");
  }
  public Game(int num_player,
              BufferedReader bfr,
              PrintStream out,
              String map_name,
              int init_unit) {
    this(num_player, bfr, out, map_name, init_unit, 6);
  }

  /* returns how many units a player gets when game starts.
   */
  public int getInitUnit() { return this.init_unit; }

  /* @return true if the game has ended.
   */
  public boolean checkEnd() {
    PlayerInfo p = map.territories.get(0).getPlayerInfo();
    for (Territory t : map.territories) {
      if (p.equals(t.getPlayerInfo())) {
        continue;
      }
      else {
        return false;
      }
    }
    /*
    out.println("=================\nThe fate of " + map.name + " in the hands of " +
                p.player_name + ". Game over.\n=================");
    */
    return true;
  }

  /* generates and returns PlayerInfo with given list of player names.
   */
  public ArrayList<PlayerInfo> initPlayerInfos(ArrayList<String> names) {
    for (int i = 0; i < num_player; i++) {
      PlayerInfo pi = new PlayerInfo(i, names.get(i), colors.get(i));
      players.add(pi);
    }
    return players;
  }

  /* initializes map and returns InitResponses.
   */
  public ArrayList<InitResponse> initMap() {
    /*
    bmf = new BasicMapFactory(players, num_player, num_territory);
    ArrayList<InitResponse> res = bmf.createInitResponses(num_territory);
    // bmf = new BasicMapFactory(players, num_player, 24);
    // ArrayList<InitResponse> res = bmf.createInitResponses();
    
    for (InitResponse r : res) {
      map.init(r);
    }
    return res;
    */
    V2MapFactory v2mf = new V2MapFactory(players);
    ArrayList<InitResponse> res = v2mf.createInitResponses();
    for (InitResponse r : res) {
      map.init(r);
    }
    for (Territory t : map.territories) {
      //  out.println(t instanceof TerritoryWP);
      //  out.println(t.units.size());
    }
    return res;
  }

  /* used to resume the player to saved status. creates territories. need to give turnresponses afterwards.
   */
  public ArrayList<InitResponse> resumeMap() {
    ArrayList<InitResponse> irs = new ArrayList<>();
    for (Territory t : map.territories) {
      irs.add(new InitResponseWP(t.id,
                                 t.player_info,
                                 t.getNumUnit(),
                                 t.getName(),
                                 t.neighbors,
                                 t.prod_food,
                                 t.prod_tech));
    }
    return irs;
  }

  /* updates the map with initial unit placement.
   */
  public void initUnit(ArrayList<TurnResponse> turn0) { map.update(turn0); }

  /*
  private ArrayList<Request> getMoveRequests(ArrayList<Request> reqs) {
    ArrayList<Request> mv = new ArrayList<Request>();
    for (Request r : reqs) {
      if (r instanceof MoveRequest) {
        mv.add(r);
      }
    }
    return mv;
    }*/

  // probably need refractoring

  /* selects Attack Requests from list of requests and resolve them centrally.
   */
  private ArrayList<Request> getAttackRequests(ArrayList<Request> reqs) {
    ArrayList<Request> atk = new ArrayList<Request>();
    for (Request r : reqs) {
      if (r instanceof AttackRequest) {
        atk.add(r);
      }
    }
    return atk;
  }

  private ArrayList<Request> getSERequests(ArrayList<Request> reqs) {
    ArrayList<Request> atk = new ArrayList<Request>();
    for (Request r : reqs) {
      if (r instanceof ScorchedEarthRequest) {
        atk.add(r);
      }
    }
    return atk;
  }

  private ArrayList<Request> getRSRequests(ArrayList<Request> reqs) {
    ArrayList<Request> atk = new ArrayList<Request>();
    for (Request r : reqs) {
      if (r instanceof RailwaySabotageRequest) {
        atk.add(r);
      }
    }
    return atk;
  }

  /* checks all requests if they are valid.
   */
  private boolean checkRule(Request req, PlayerInfo p) {
    if (req.equals(null)) {
      return false;
    }
    try {
      if (req instanceof MoveRequest) {
        moc.checkRule(req, map, p);
      }
      else if (req instanceof AttackRequest) {
        aoc.checkRule(req, map, p);
      }
      else if (req instanceof ResearchRequest) {
        rrc.checkRule(req, map, p);
      }
      else if (req instanceof UpgradeRequest) {
        uoc.checkRule(req, map, p);
      }
      else if (req instanceof SpyCreateRequest) {
        scc.checkRule(req, map, p);
      }
      else if (req instanceof SpyMoveRequest) {
        smc.checkRule(req, map, p);
      }
      else if (req instanceof CloakRequest) {
        crc.checkRule(req, map, p);
      }
      else if (req instanceof ScorchedEarthRequest) {
        sec.checkRule(req, map, p);
      }
      else if (req instanceof RailwayConstructRequest) {
        rrcc.checkRule(req, map, p);
      }
      else if (req instanceof RailwaySabotageRequest) {
        rscc.checkRule(req, map, p);
      }
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  /*  Processes a turn. generates Turn response.
   */

  public ArrayList<TurnResponse> turn(ArrayList<Request> reqs, ArrayList<PlayerInfo> ps) {
    for (int i = 0; i < reqs.size(); i++) {
      checkRule(reqs.get(i), ps.get(i));
      if (reqs.get(i) instanceof NukeRequest) {
        for (Territory t : map.territories) {
          t.setPlayerInfo(reqs.get(i).getIssuerId());
        }
      }
      if (reqs.get(i) instanceof MoveRequest) {
        ArrayList<Request> mv = new ArrayList<>();
        mv.add(reqs.get(i));
        mrr.resolve(mv);
      }
      if (reqs.get(i) instanceof UpgradeRequest) {
        ArrayList<Request> up = new ArrayList<>();
        up.add(reqs.get(i));
        urr.resolve(up);
      }
      if (reqs.get(i) instanceof SpyCreateRequest) {
        ArrayList<Request> spc = new ArrayList<>();
        spc.add(reqs.get(i));
        scrr.resolve(spc);
      }
      if (reqs.get(i) instanceof SpyMoveRequest) {
        ArrayList<Request> smc = new ArrayList<>();
        smc.add(reqs.get(i));
        smrr.resolve(smc);
      }
      if (reqs.get(i) instanceof CloakRequest) {
        ArrayList<Request> clcs = new ArrayList<>();
        clcs.add(reqs.get(i));
        crr.resolve(clcs);
      }
      if (reqs.get(i) instanceof RailwayConstructRequest) {
        ArrayList<Request> rrcs = new ArrayList<>();
        rrcs.add(reqs.get(i));

        rcrr.resolve(reqs);
      }
    }

    //out.println("Number of Move Orders: " + mv.size());

    ArrayList<Request> rss = getRSRequests(reqs);
    rsrr.resolve(rss);

    ArrayList<Request> atk = getAttackRequests(reqs);
    arr.resolve(atk);

    ArrayList<Request> ses = getSERequests(reqs);
    serr.resolve(ses);

    //out.println("Number of Attack Orders: " + mv.size());
    for (Territory t : map.territories) {
      if (t.ScorchedEarthRounds == 0) {
        Integer x = t.units.get(0);

        t.units.set(0, x + 1);
      }
      for (Spy s : t.spies) {
        s.hasMoved = false;
        out.println(s.owner.player_name + "\'s spy in " + t.name +
                    " has fully rested, and ready to move.");
      }
      if (t.clockRounds > 0) {
        t.clockRounds--;
        out.println("Territory " + t.name + "'s cloak will last for another " +
                    t.clockRounds + " round(s).");
      }

      if (t.ScorchedEarthRounds > 0) {
        for (Territory d : map.territories) {
          if (t.isAdjacentTo(d)) {
            t.modify_dist(d.id, 0);
            d.modify_dist(t.id, 0);
          }
          for (Integer n : t.neighbors) {
            map.distances[t.id][n] = 5;
            map.distances[n][t.id] = 5;
          }
        }
        t.ScorchedEarthRounds--;
        out.println("Territory " + t.name +
                    "'s scorched earth status will last for another " +
                    t.ScorchedEarthRounds + " round(s).");
      }
    }
    for (PlayerInfo p : players) {
      map.updateProduction(p);
      if (map.research_barrier.get(p)) {
        int lvl = map.tech_lvls.get(p) + 1;
        map.tech_lvls.put(p, lvl);
        out.println("Increasing " + p.player_name + "'s tech lvl");
        map.research_barrier.put(p, false);
      }
    }
    return map.generateResonse();
  }

  /* gets turn response without updating units.
   */
  public ArrayList<TurnResponse> turnResponse() {
    return (ArrayList<TurnResponse>)map.generateResonse().clone();
  }

  /* checks if a player with given id loses.
   * @param i is the player id.
   * @returns true if player loses.
   */
  public boolean checkLose(int i) {
    PlayerInfo p = players.get(i);
    for (Territory t : map.territories) {
      if (t.getPlayerInfo().equals(p)) {
        return false;
      }
    }
    return true;
  }
}
