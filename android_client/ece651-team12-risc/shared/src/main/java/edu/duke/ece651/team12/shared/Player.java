
package edu.duke.ece651.team12.shared;
import java.io.*;
import java.util.ArrayList;

public class Player {
  public Map map;
  public TextMapDisplayer map_displayer;
  public PlayerInfo player_info;
  public BufferedReader input_source;
  public PrintStream out;
  public ArrayList<PlayerInfo> enemies;
  private RuleChecker atk_rule_checker;
  private RuleChecker mv_rule_checker;
  private RuleChecker res_rule_checker;
  private RuleChecker upg_rule_checker;
  private RuleChecker sp_tr_rule_checker;
  private RuleChecker sp_mv_rule_checker;
  private RuleChecker cl_rule_checker;

  private RuleChecker rrcc = new RailwayConstructOwnershipChecker();

  private RuleChecker rscc = new RailwaySabotageOwnershipChecker();
  private RuleChecker sec = new ScorchedEarthOwnershipChecker();
  private int match_id;
  private int status = 0;

  public Player(PlayerInfo player_info,
                ArrayList<PlayerInfo> enemies,
                Map map,
                TextMapDisplayer map_displayer,
                BufferedReader input_source,
                PrintStream out,
                int match_id) {
    this.player_info = player_info;
    this.enemies = enemies;
    this.map = map;
    this.map_displayer = map_displayer;
    this.input_source = input_source;
    this.out = out;
    this.mv_rule_checker = new MoveOwnershipChecker();
    this.atk_rule_checker = new AttackOwnershipChecker();
    this.res_rule_checker = new ResearchDuplicationChecker();
    this.upg_rule_checker = new UpgradeOwnershipChecker();
    this.sp_tr_rule_checker = new SpyTrainOwnershipChecker();
    this.sp_mv_rule_checker = new SpyMoveOwnershipChecker();
    this.cl_rule_checker = new CloakOwnershipChecker();
    this.match_id = match_id;
  }

  public Player(PlayerInfo player_info,
                ArrayList<PlayerInfo> enemies,
                Map map,
                TextMapDisplayer map_displayer,
                BufferedReader input_source,
                PrintStream out) {
    this(player_info, enemies, map, map_displayer, input_source, out, 0);
  }

  private int readFromInput() throws IOException {
    String inp = input_source.readLine();
    int num;
    try {
      num = Integer.parseInt(inp);
      return num;
    }
    catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid Instruction: please enter an integer.");
    }
  }

  public void initializeMap(ArrayList<InitResponse> responses) {
    for (InitResponse r : responses) {
      map.init(r);
    }
  }

  public ArrayList<TurnResponse> initUnit(int num_unit) throws IOException {
    int remain = num_unit;
    ArrayList<TurnResponse> tr = new ArrayList<>();
    for (Territory t : map.territories) {
      if (t.getPlayerInfo().equals(this.player_info)) {
        boolean valid = false;
        if (remain == 0) {
          out.println(
              "You have no more units to assign. Rest of the territories will 0 initial unit.");
          break;
        }
        out.println("Plase enter how many units you want to place in " + t.getName() +
                    ". Remaining units: " + remain + ";");
        while (!valid) {
          try {
            int num = readFromInput();
            if (num > remain) {
              out.println(
                  "You don't have that many units remaining. \nPlease try again.");
            }
            else {
              valid = true;
              remain -= num;
              tr.add(new TurnResponse(t.getId(), player_info, num));
            }
          }
          catch (IllegalArgumentException e) {
            out.println(e.getMessage());
            out.println("Please try again.");
          }
        }
      }
      else {
        continue;
      }
    }
    if (remain > 0) {
      out.println(
          "You haven't used all your units. The remaining units will be assigned to the last territory.");
      TurnResponse last = tr.get(tr.size() - 1);
      tr.remove(tr.size() - 1);
      tr.add(new TurnResponse(
          last.getTerritoryId(), player_info, last.getNumUnit() + remain));
    }
    out.println("You can still move the units around with no cost when the game starts.");
    return tr;
  }

  public boolean checkLose(PlayerInfo player_info) {
    for (Territory t : map.territories) {
      if (t.player_info.equals(player_info)) {
        return false;
      }
    }
    return true;
  }
  public PlayerInfo checkEnd() {
    PlayerInfo p = map.territories.get(0).player_info;
    for (Territory t : map.territories) {
      if (!p.equals(t.player_info)) {
        return null;
      }
    }
    return p;
  }

  public Request genTrainSpy(int source_num) {
    Request r = new SpyCreateRequest(player_info, source_num);

    // check rule
    try {
      sp_tr_rule_checker.checkRule(r, map, player_info);

      Integer unit = map.territories.get(source_num).units.get(0);
      unit--;
      map.territories.get(source_num).units.set(0, unit);
      int tech_acc = map.tech_accus.get(player_info);
      tech_acc -= 20;
      map.tech_accus.put(player_info, tech_acc);
      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genMoveSpy(int source_num, int target_num) {
    Request r = new SpyMoveRequest(player_info, source_num, target_num);

    // check rule;

    try {
      sp_mv_rule_checker.checkRule(r, map, player_info);
      Spy tomove = null;
      for (Spy s : map.territories.get(source_num).spies) {
        if (!s.hasMoved && s.owner.equals(player_info)) {
          tomove = s;
          tomove.hasMoved = true;
          break;
        }
      }
      map.territories.get(source_num).spies.remove(tomove);

      int food_acc = map.food_accus.get(player_info);
      food_acc -= map.distances[source_num][target_num];
      map.food_accus.put(player_info, food_acc);

      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genCloak(int source_num) {
    Request r = new CloakRequest(player_info, source_num);

    // check rule;
    try {
      cl_rule_checker.checkRule(r, map, player_info);

      int adjacents = 0;
      for (Territory t : map.territories) {
        if (t.isAdjacentTo(map.territories.get(source_num))) {
          adjacents++;
        }
      }

      int tech_acc = map.tech_accus.get(player_info);
      tech_acc -= 5 * adjacents;
      map.tech_accus.put(player_info, tech_acc);

      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genSE(int source_num) {
    try {
      Request r = new ScorchedEarthRequest(player_info, source_num);
      sec.checkRule(r, map, player_info);
      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genRC(int source_num, int dest_num) {
    try {
      Request r = new RailwayConstructRequest(player_info, source_num, dest_num);
      rrcc.checkRule(r, map, player_info);
      Integer food_accu = map.food_accus.get(player_info);
      Territory t0 = map.territories.get(r.getSourceId());
      Territory t1 = map.territories.get(r.getDestinationId());
      int cost = map.distances[t0.id][t1.id] * 5;
      food_accu -= cost;
      map.food_accus.put(player_info, food_accu);

      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genRS(int source_num) {
    try {
      Request r = new RailwaySabotageRequest(player_info, source_num);
      rscc.checkRule(r, map, player_info);

      Integer accu = map.tech_accus.get(player_info);
      int adjs = 0;
      Territory t0 = map.territories.get(r.getSourceId());

      for (Territory t : map.territories) {
        // is adjacent, does not partially belong to the issuer, and has railway
        if (t.isAdjacentTo(t0) && !t.belongsTo(player_info) &&
            t.distmodifiers[t0.id] == 1) {
          adjs += 5;
        }
      }
      accu -= adjs;
      map.tech_accus.put(player_info, accu);

      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      return null;
    }
  }

  public Request genMove(int source_num, int target_num, ArrayList<Integer> units) {
    try {
      Request r = new MoveRequest(player_info, source_num, target_num, units);
      mv_rule_checker.checkRule(r, map, player_info);

      for (int i = 0; i < 7; i++) {
        Integer sourcex = map.territories.get(source_num).units.get(i);
        sourcex -= units.get(i);
        Integer targetx = map.territories.get(target_num).units.get(i);
        targetx += units.get(i);
        map.territories.get(source_num).units.set(i, sourcex);
        map.territories.get(target_num).units.set(i, targetx);
        int food_acc = map.food_accus.get(player_info);
        int distance = map.shortestDistance(player_info)[source_num][target_num];
        food_acc -= distance * units.get(i);
        map.food_accus.put(player_info, food_acc);
      }
      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      out.println("Please try again.");
    }
    return null;
  }

  public Request genAttack(int source_num, int target_num, ArrayList<Integer> units) {
    try {
      Request r = new AttackRequest(player_info, source_num, target_num, units);
      atk_rule_checker.checkRule(r, map, player_info);

      for (int i = 0; i < 7; i++) {
        Integer sourcex = map.territories.get(source_num).units.get(i);
        sourcex -= units.get(i);
        map.territories.get(source_num).units.set(i, sourcex);
        int food_acc = map.food_accus.get(player_info);
        int distance = map.distances[source_num][target_num];
        food_acc -= distance * units.get(i);
        map.food_accus.put(player_info, food_acc);
      }
      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      out.println("Please try again.");
    }
    return null;
  }

  public Request genUpgrade(int source_num, ArrayList<Integer> units) {
    try {
      Request r = new UpgradeRequest(player_info, source_num, units);
      upg_rule_checker.checkRule(r, map, player_info);
      Territory t = map.territories.get(source_num);
      for (int i = 0; i < 6; i++) {
        int x = r.units.get(i);
        int p = t.units.get(i);
        int n = t.units.get(i + 1);
        t.units.set(i, p - x);
        t.units.set(i + 1, n + x);
      }
      int[] costs = UpgradeCostChecker.costs();
      int cost = 0;
      for (int i = 0; i < 6; i++) {
        cost += r.units.get(i) * costs[i];
      }
      int stock = map.tech_accus.get(player_info);
      map.tech_accus.put(player_info, stock - cost);
      return r;
    }
    catch (IllegalArgumentException e) {
      out.println(e.getMessage());
      out.println("Please try again.");

      return null;
    }
  }

  public ArrayList<Request> turn() throws IOException {
    ArrayList<Request> requests = new ArrayList<Request>();
    if (checkLose(this.player_info)) {
      out.println(
          "You have lost. You may now observe the conquests of other participants.");
      return requests;
    }
    out.println("Another day dawns upon the land of " + map.name + ". Parabellum!");
    out.println(map_displayer.display());
    while (true) {
      try {
        out.println(player_info.player_name + ", what do you want to do?");
        out.println(
            "Your options:\nM==>Move units from one territory to another territory. The route must be controled by you. The units will arrive this turn and ready to move again or attack.\nA==>Attack from a territory to an adjacent enemy territory. The units will not participate in the defense of the source territory.\nC==>Commit all your changes, end your turn.\nR==>Increase Tech Level.\nU==>Upgrade units.\nD==>Display the map again.\nS==>Switch to another game.\nST==>Train a spy.\nSM==>Move a spy.\nCL==>Cloak a territory.\nSE==>Perform Scorched Earth on a territory, significantly increase move cost forever, disable unit recruitment and production for 3 turns.\nRC==>Construct railways between 2 adjacent territories, significantly decrease domestic transport food cost. Notice that railways won't work when entering enemy territories.\nRS==>Sacrifice an already settled spy to sabotage enemy's railway network in a territory.");
        out.println("Your food storage: " + map.food_accus.get(player_info) +
                    ". Your food productivity: " + map.getFoodProduct(player_info) +
                    "\nYour tech storage: " + map.tech_accus.get(player_info) +
                    ". Your tech productivity: " + map.getTechProduct(player_info));
        String operation = input_source.readLine().toUpperCase();

        if (operation.equals("C")) {
          out.println(
              "You have commited your turn. please wait for other players and the server to respond.");
          this.status = 1;
          break;
        }

        else if (operation.equals("S")) {
          out.println("Switching.");
          this.status = -1;
          return null;
        }

        else if (operation.equals("U")) {
          out.println("Enter the territory ID where you want to upgrade your units.");
          int source_num = readFromInput();
          out.println(
              "Enter 6 integers, representing number of each levels unit upgrades.");
          ArrayList<Integer> units = new ArrayList<>();
          for (int i = 0; i < 7; i++) {
            out.println("Enter lvl " + i + " unit upgrade numbers");
            units.add(readFromInput());
          }
          Request r = genUpgrade(source_num, units);

          System.out.println(r);
          if (r != null) {
            out.println(
                "Your upgrade order has been confirmed. Your research will be complete next turn.");

            requests.add(r);
          }
        }
        else if (operation.equals("R")) {
          try {
            Request r = new ResearchRequest(player_info);
            if (res_rule_checker.checkRule(r, map, player_info)) {
              requests.add(r);
              out.println(
                  "Your research order has been confirmed. Your research will be complete next turn.");
            }
          }
          catch (IllegalArgumentException e) {
            out.println(e.getMessage());
            out.println("Please try again.");
          }
        }
        else if (operation.equals("M")) {
          //move
          out.println("Enter the territory ID where you want to move your units from.");
          int source_num = readFromInput();
          out.println("Enter the territory ID where you want to move your units to.");
          int target_num = readFromInput();
          ArrayList<Integer> units_to_move = new ArrayList<>();
          for (int i = 0; i < 7; i++) {
            out.println("Enter the number of lv " + i + " units you want to move.");
            int unit_num = readFromInput();
            units_to_move.add(unit_num);
          }

          Request r = genMove(source_num, target_num, units_to_move);
          if (r != null) {
            out.println(
                "Your move order has been confirmed. You can continue to issue orders.");

            requests.add(r);
          }
        }

        else if (operation.equals("A")) {
          //attack
          //placeholder: replace with real request

          out.println("Enter the territory ID where you want to initiate your attack.");
          int source_num = readFromInput();
          out.println("Enter the territory ID where you want to attack.");
          int target_num = readFromInput();
          ArrayList<Integer> units_to_attack = new ArrayList<>();
          for (int i = 0; i < 7; i++) {
            out.println("Enter the number of lv " + i +
                        " units you want to attack with.");
            int unit_num = readFromInput();
            units_to_attack.add(unit_num);
          }
          Request r = genAttack(source_num, target_num, units_to_attack);
          if (r != null) {
            requests.add(r);
            out.println(
                "Your attack order has been confirmed. You can continue to issue orders.");
          }
        }

        else if (operation.equals("D")) {
          out.println(map_displayer.display());
          out.println(map_displayer.adjacency());
          out.println(map_displayer.info());
        }
        else if (operation.equals("ST")) {
          out.println("Enter the territory ID where you want to train spy.");
          int source_num = readFromInput();
          Request r = genTrainSpy(source_num);
          if (r != null) {
            requests.add(r);
            out.println(
                "Your train spy order has been confirmed. The spy will be ready next turn.");
          }
        }

        else if (operation.equals("SM")) {
          out.println("Enter the territory ID where you want to move spy from.");
          int source_num = readFromInput();

          out.println("Enter the territory ID where you want to move spy to.");
          int destination_num = readFromInput();
          Request r = genMoveSpy(source_num, destination_num);
          if (r != null) {
            requests.add(r);
            out.println(
                "Your move spy order has been confirmed. The spy will be ready next turn.");
          }
        }
        else if (operation.equals("CL")) {
          out.println("Enter the territory ID where you want to cloak.");
          int source_num = readFromInput();
          Request r = genCloak(source_num);
          if (r != null) {
            requests.add(r);
            out.println(
                "Your cloak order has been confirmed. Your territory will be cloaked in next turn lasting 3 turns.");
          }
        }
        else if (operation.equals("SE")) {
          out.println("Enter the territory ID that you want to conduct scorched earth.");
          int s = readFromInput();
          Request r = genSE(s);
          if (r != null) {
            requests.add(r);
            out.println(
                "This territory will be scorched since next turn for 3 turns. Let us hope that it is worth it.");
          }
        }
        else if (operation.equals("RC")) {
          out.println("Enter the territory ID where you want to construct railway from.");
          int source_num = readFromInput();
          out.println("Enter the territory ID where you want to construct railway to.");
          int target_num = readFromInput();
          Request r = genRC(source_num, target_num);
          if (r != null) {
            out.println(
                "Your railway construct order has been confirmed. You can continue to issue orders.");

            requests.add(r);
          }
        }
        else if (operation.equals("RS")) {
          out.println("Enter the territory ID where you want to sabotage railways.");
          int s = readFromInput();
          Request r = genRS(s);
          if (r != null) {
            out.println(
                "Your spy will sabotage enemy's railway, but as a consequence will be revealed to be a spy.");
            requests.add(r);
          }
        }

        else {
          out.println(operation + " is not a valid operation. Please try again.");
        }
      }
      catch (Exception e) {
        out.println("Experiencing: ");
        out.println(e.getMessage());
        out.println("Please try again.");
      }
    }

    return requests;
  }

  public boolean endTurn(ArrayList<TurnResponse> responses) {
    this.status = 0;
    out.println(map_displayer.endTurnInfo(responses));
    map.update(responses);

    PlayerInfo win = checkEnd();
    if (win != null) {
      if (win.equals(player_info)) {
        out.println("Congratulations! You have gained control over " + map.name + ".");
      }
      else {
        out.println(win.player_name + " has gained control over " + map.name + ".");
      }
      return true;
    }
    map.updateVision(player_info);
    for (Territory t : map.territories) {
      for (Spy s : t.spies) {
        s.hasMoved = false;
      }
    }
    return false;
  }
}
