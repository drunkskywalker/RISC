package edu.duke.ece651.team12.server;

import edu.duke.ece651.team12.shared.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class V3Server {
  private int port;
  private ServerSocket server_socket;
  private ArrayList<String> userNames = new ArrayList<>();
  private HashMap<String, String> passwords = new HashMap<>();
  private ArrayList<ArrayList<Integer> > userJoinedGames = new ArrayList<>();
  private ArrayList<Socket> clients = new ArrayList<>();
  private ArrayList<ObjectOutputStream> outputs = new ArrayList<>();
  private ArrayList<ObjectInputStream> inputs = new ArrayList<>();
  private ArrayList<Thread> gameThreads = new ArrayList<>();
  private ArrayList<RunnableGame> games = new ArrayList<>();
  static Lock lock = new ReentrantLock();
  private MessageDigest md;

  public V3Server(int port) {
    this.port = port;

    try {
      md = MessageDigest.getInstance("SHA-256");
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void init() throws IOException { server_socket = new ServerSocket(port); }

  /*starts the server to listen for incoming connections. When a client connects, it creates a new InitGame instance with the client's socket, wraps it in a new thread, and starts the thread.*/
  public void serve() throws IOException {
    System.out.println("Listening on " + server_socket.toString() + " port " + port +
                       "...");

    while (true) {
      System.out.println("Accepting connection...");
      Socket client = server_socket.accept();
      Thread t_reg = new Thread(new InitGame(client));
      t_reg.start();
    }
  }

  // Intermediate object between a game and a player.
  // When user inputs -1, returns subsequent transmission to the InitGame thread.
  // Use a new thread to wrap around the Transmiter object each time a player starts/joins/resumes/switches a game.
  private class Transmiter implements Runnable {
    int uid;
    int gid;
    //Socket client;
    RunnableGame game;
    LinkedBlockingQueue<Object> inqueue;
    LinkedBlockingQueue<Object> outqueue;
    Lock trlock;

    public Transmiter(int uid,
                      int gid,
                      RunnableGame game,
                      LinkedBlockingQueue<Object> inqueue,
                      LinkedBlockingQueue<Object> outqueue) {
      this.uid = uid;
      this.gid = gid;
      this.game = game;
      this.inqueue = inqueue;
      this.outqueue = outqueue;
      this.trlock = game.gamelock;
    }

    public Transmiter(int uid, RunnableGame game) {
      this(uid, game.gid, game, game.outqueue, game.inqueue);
    }

    /*initializes the game for a player, exchanging data like player names, player information, map name, initial unit count, and other initialization data between the server and the client.*/

    private void init_game(ObjectInputStream ois, ObjectOutputStream oos)
        throws IOException, ClassNotFoundException, InterruptedException {
      System.out.print("T[" + uid + "]");
      System.out.println("Initializing game");
      while (inqueue.isEmpty()) {
      }
      trlock.lock();
      // send/recv initialization stuff. the game does not start until game.state flip to 1.
      Integer ind = (Integer)inqueue.take();
      Integer tot = (Integer)inqueue.take();

      trlock.unlock();
      System.out.print("T[" + uid + "]");
      System.out.println("Received user joining data");

      oos.writeObject(ind);
      oos.writeObject(tot);

      System.out.print("T[" + uid + "]");
      System.out.println("Reading Name");

      String name = (String)ois.readObject();

      System.out.print("T[" + uid + "]");
      System.out.println("Picked name " + name);

      trlock.lock();
      outqueue.add(ind);
      outqueue.add(name);
      trlock.unlock();

      System.out.print("T[" + uid + "]");
      System.out.println("Name written to queue. waiting for game to receive.");

      while (inqueue.isEmpty() || inqueue.peek() instanceof Integer) {
        System.out.print("T[" + uid + "]");
        System.out.println("Waiting...");
      }

      System.out.print("T[" + uid + "]");
      System.out.println("Reading PlayerInfo, Enemies, MapName, InitUnitCount");

      trlock.lock();

      /*
      while () {
        System.out.println("why is this a integer: " + inqueue.peek());
      }
      */

      ArrayList<PlayerInfo> pis = (ArrayList<PlayerInfo>)inqueue.take();
      String mapName = (String)inqueue.take();
      Integer initUnit = (Integer)inqueue.take();
      ArrayList<InitResponse> irs = (ArrayList<InitResponse>)inqueue.take();

      trlock.unlock();
      oos.writeObject(pis.get(ind));
      System.out.println("T[" + uid + "]Got " + pis.get(ind).toString());
      oos.writeObject(pis);
      oos.writeObject(mapName);
      oos.writeObject(irs);
      oos.writeObject(initUnit);
      ArrayList<TurnResponse> unitPlacement = (ArrayList<TurnResponse>)ois.readObject();
      trlock.lock();
      outqueue.add(unitPlacement);
      trlock.unlock();
      System.out.print("T[" + uid + "]");
      System.out.println("Wrote initial unit placement.");
    }

    /*the main loop that handles player actions and game state updates. It starts by initializing the game if the game state is 0, and then proceeds to handle game states and communication with the player.*/
    @Override
    public void run() {
      try {
        ObjectOutputStream oos;
        ObjectInputStream ois;
        ois = inputs.get(uid);

        oos = outputs.get(uid);

        // init game
        if (game.state == 0) {
          init_game(ois, oos);
        }
        else {
          System.out.print("T[" + uid + "]");
          System.out.println("Game state is now 1.");

          oos.writeObject(-2);
          trlock.lock();
          //Integer ind = (Integer)inqueue.take();
          ArrayList<PlayerInfo> PIS = (ArrayList<PlayerInfo>)inqueue.take();
          String mname = (String)inqueue.take();
          ArrayList<InitResponse> irs = (ArrayList<InitResponse>)inqueue.take();
          trlock.unlock();
          oos.writeObject(gid);
          PlayerInfo x = null;
          for (PlayerInfo y : PIS) {
            if (y.player_name.equals(userNames.get(uid))) {
              x = y;
            }
          }
          oos.writeObject(x.player_id);
          oos.writeObject(x);
          oos.writeObject(PIS);
          oos.writeObject(mname);
          oos.writeObject(irs);
          System.out.print("T[" + uid + "]");
          System.out.println("Resuming game " + gid + ".");
        }
        // wait for the game to start
        while (this.game.state != 1) {
          System.out.print("T[" + uid + "]");
          System.out.println("waiting for state...");
        }

        while (inqueue.isEmpty()) {
        }

        //TODO: still check this
        //  while ((Integer)inqueue.peek() != gid) {
        //  continue;
        // }

        trlock.lock();
        Integer x = (Integer)inqueue.take();
        if (x < 0) {
          // TODO: DOSOMETHING
        }

        ArrayList<TurnResponse> unitPlacement = (ArrayList<TurnResponse>)inqueue.take();
        System.out.print("T[" + uid + "]");
        System.out.println("Read saved unit placement.");

        trlock.unlock();

        oos.writeObject(unitPlacement);

        System.out.println("T[" + uid + "]Game Start/Resume.");

        /*The method handles various scenarios based on the game status received from the client. If the status is -1, it indicates the player wants to quit. If the status is -2, it indicates the player wants to switch games. Otherwise, it processes the client's requests and sends the appropriate responses.*/
        while (this.game.state == 1) {
          // if status is normal, it should be the player's uid.
          Integer status = (Integer)ois.readObject();
          System.out.println("T[" + uid + "]Receiving Game status " + status);

          if (status == -1) {
            //quit.
            System.out.println("T[" + uid + "]Indicated by player to quit.");

            /*this class uses the trlock (game lock) to ensure that only one thread can access the shared game resources at a time, avoiding concurrency issues.*/

            trlock.lock();

            outqueue.add(Integer.valueOf(-1));
            outqueue.add(Integer.valueOf(uid));
            trlock.unlock();
            break;
          }
          else if (status == -2) {
            Integer dropped = (Integer)ois.readObject();
            System.out.println("T[" + uid +
                               "]Player Switches. Returning control to InitGame Thread.");
            return;
          }
          else {
            //normal.
            try {
              System.out.println("T[" + uid + "]Normal function.");

              ArrayList<Request> reqs = (ArrayList<Request>)ois.readObject();
              System.out.println("T[" + uid + "]Received Requests.");

              trlock.lock();
              outqueue.add(Integer.valueOf(uid));
              outqueue.add(reqs);
              System.out.println("T[" + uid + "]Sent Requests.");

              trlock.unlock();
              System.out.println("T[" + uid + "]Waiting for responses.");

              while (inqueue.isEmpty()) {
              }
              System.out.println("T[" + uid +
                                 "]Responses available: size: " + inqueue.size());
            }
            catch (Exception e) {
              System.out.println("Dumbass: " + e.getMessage());
            }

            trlock.lock();
            Integer gg = (Integer)inqueue.take();
            ArrayList<Response> reps = (ArrayList<Response>)inqueue.take();
            trlock.unlock();
            oos.writeObject(reps);
            System.out.println("T[" + uid + "]Received responses.");
          }
        }

        // exception. player drops. send to server to indicate error.
      }
      catch (Exception e) {
        trlock.lock();
        outqueue.add(-1);
        outqueue.add(uid);
        trlock.unlock();
        System.out.print("T[" + uid + "]Experienced ");
        System.out.println(e.getMessage());
        e.printStackTrace();
        return;
      }
    }
  }

  public class InitGame implements Runnable {
    Socket client;
    int uid = -1;
    public InitGame(Socket c) { client = c; }
    @Override
    public void run() {
      System.out.println("Starting InitGame thread");
      try {
        uid = handle_connection(client);
        ObjectInputStream ois = inputs.get(uid);
        ObjectOutputStream oos = outputs.get(uid);

        while (true) {
          // Send the user available games. Receive the choice: >=0: joins the game. -1: creates a new game.
          HashMap<Integer, String> availableGames = new HashMap<>();
          for (RunnableGame rg : games) {
            // has empty seat, or was a member of this game
            if (rg.joinable() || (rg.uids.contains(uid) && rg.state != -1)) {
              availableGames.put(rg.gid, rg.describe());
            }
          }
          System.out.println("I[" + uid +
                             "]Current available games: " + availableGames.size());
          oos.writeObject(availableGames);
          System.out.println("I[" + uid + "]Player choosing join or start");
          Integer choice = (Integer)ois.readObject();
          System.out.println("I[" + uid + "]Player chose " + choice);

          // starting new game
          if (choice == -1) {
            System.out.println("I[" + uid + "]Starting new game");
            Integer pn = (Integer)ois.readObject();

            lock.lock();
            int gid = games.size();
            lock.unlock();

            RunnableGame newGame = new RunnableGame(gid, uid, pn);
            System.out.println("G[" + gid + "]New Game " + newGame.describe() +
                               " created");
            games.add(newGame);
            oos.writeObject(gid);
            oos.writeObject(uid + ": Starting game " + newGame.describe());
            newGame.state = 0;

            Transmiter trans = new Transmiter(uid, newGame);
            Thread transmitThread = new Thread(trans);
            System.out.println("I[" + uid +
                               "]Transitioning socket control to transmiter for player " +
                               uid);
            transmitThread.start();
            System.out.println("I[" + uid +
                               "]InitGame thread should not write to oos now");
            transmitThread.join();
            System.out.println(
                "I[" + uid + "]Receiving back socket control from trasmiter for player " +
                uid);
          }

          // joining existing game
          else if (games.size() > choice && ((games.get(choice).state == 0) ||
                                             (games.get(choice).uids.contains(uid)))) {
            System.out.println("I[" + uid + "]Joining game " + choice);
            lock.lock();
            RunnableGame game = games.get(choice);
            lock.unlock();

            System.out.println("I[" + uid + "]Joining game " + game.describe());
            game.join(uid);
            oos.writeObject(game.gid);
            oos.writeObject("Joined game " + game.describe());
            Transmiter trans = new Transmiter(uid, game);
            Thread transmitThread = new Thread(trans);

            System.out.println("I[" + uid +
                               "]Transitioning socket control to transmiter for player " +
                               uid);
            transmitThread.start();

            System.out.println("I[" + uid +
                               "]InitGame thread should not write to oos now");
            transmitThread.join();
            System.out.println(
                "I[" + uid + "]Receiving back socket control from trasmiter for player " +
                uid);
          }
          else {
            System.out.println("That's not a valid game.");
            oos.writeObject(-1);
            oos.writeObject("That's not a valid game.");
          }
        }
      }
      catch (Exception e) {
        System.out.print("I[" + uid + "]Experiencing ");
        System.out.println(e.getMessage());
        //e.printStackTrace();
        return;
      }
    }
  }

  public class RunnableGame implements Runnable {
    public int gid;
    public ArrayList<Integer> uids = new ArrayList<>();
    // public ArrayList<Boolean> online = new ArrayList<>();
    public int max_player;
    public LinkedBlockingQueue<Object> inqueue = new LinkedBlockingQueue<>();
    public LinkedBlockingQueue<Object> outqueue = new LinkedBlockingQueue<>();

    public Game g;
    public Lock gamelock;

    // 0: initing; 1: gaming; 2: interrupted (in the middle of a game);  -1: finished
    public int state;

    public RunnableGame(int gid, int uid, int pn) {
      this.gid = gid;
      uids.add(uid);
      max_player = pn;
      this.gamelock = new ReentrantLock();
    }

    /*This method allows a player to join the game. If the player is already in the game, their status is updated to "online". If the player is not in the game, they are added to the uids list and their status is set to "online". If the game is already in progress (state == 1), the necessary information is sent to the new player to allow them to resume the game. If the number of players in the game has reached the maximum (as determined by joinable), a new Thread object is created and started to run the game.*/
    public void join(int uid) {
      System.out.println("G[" + uid + "]joins game No. " + gid);
      lock.lock();
      if (uids.contains(uid)) {
        //  online.set(uids.indexOf(uid), true);
      }
      else {
        uids.add(uid);
        //  online.add(true);
      }
      lock.unlock();
      System.out.println("G[" + gid + "]Accepted user " + uid);

      //switching back to game.
      if (this.state == 1) {
        System.out.println("G[" + gid + "]Player " + uid + " Switching back to game.");

        this.gamelock.lock();
        System.out.println("G[" + gid + "]Writing save game.");
        ArrayList<InitResponse> irs = this.g.resumeMap();
        ArrayList<TurnResponse> trs = this.g.turnResponse();
        // player number
        //this.outqueue.add(uids.indexOf(uid));
        // player infos
        this.outqueue.add(g.players);
        // map name
        this.outqueue.add("Middle Earth");
        this.outqueue.add((ArrayList<InitResponse>)irs.clone());
        this.outqueue.add(gid);
        this.outqueue.add((ArrayList<TurnResponse>)trs.clone());
        this.gamelock.unlock();
        System.out.println("G[" + gid + "]Rewrote necessary info.");
        return;
      }
      boolean ready = !joinable();

      if (ready) {
        Thread gameThread = new Thread(this);
        System.out.println("G[" + gid + "]Starting game thread");
        gameThread.start();
        gameThreads.add(gameThread);
        // begin the game
      }
      else {
        System.out.print("G[" + gid + "]");
        System.out.println("Waiting for more to join");
      }
    }

    /*This method checks if a player can join the game by checking if the number of players in the game is less than the maximum allowed and if the game is not finished (state != -1)*/
    public boolean joinable() {
      if (uids.size() < max_player) {
        System.out.println("G[" + gid + "]Not fulled players");
      }
      else {
        System.out.println("G[" + gid + "]Full players");
      }
      return ((uids.size() < max_player)) && (state != -1);
    }

    /*This method returns a description of the game, including the game ID, maximum number of players, and game state.*/
    public String describe() {
      String res = "Game No. " + gid;
      res += "; Max Players: " + max_player;
      res += "; game state: " + state;
      return res;
    }
    /* This method initializes the game and distributes necessary information to the players.*/
    public void init_game() throws InterruptedException {
      System.out.println(gid + ": Initializing Game.");
      ArrayList<String> names = new ArrayList<>();
      for (int i = 0; i < max_player; i++) {
        names.add(null);
      }
      System.out.println(gid + ": Distributing basic info.");
      gamelock.lock();

      for (int i = 0; i < max_player; i++) {
        outqueue.add(i);
        outqueue.add(max_player);
      }
      gamelock.unlock();

      System.out.println(
          "G[" + gid +
          "]player no, total players distributed. Waiting for player to receive. Waiting for names.");
      while (inqueue.size() != max_player * 2) {
      }
      System.out.println("G[" + gid + "]queue fully populated. begin reading.");

      for (int count = 0; count < max_player; count++) {
        System.out.println("G[" + gid + "]attempting to read from inqueue.");

        //    if (queue.size() % 2 != 0) {
        //  continue;
        // }

        gamelock.lock();

        Integer n = (Integer)inqueue.take();
        String name = (String)inqueue.take();
        names.set(n, name);
        System.out.println("G[" + gid + "]Received " + name + " as " + n + "'s name.");

        gamelock.unlock();
      }
      gamelock.lock();

      ArrayList<PlayerInfo> pis = g.initPlayerInfos(names);
      ArrayList<InitResponse> irs = g.initMap();
      gamelock.unlock();
      for (PlayerInfo x : pis) {
        System.out.println(x.toString());
      }
      System.out.println("G[" + gid + "]Initialzied PI, map. distributing to players.");

      outqueue.clear();
      for (int i = 0; i < max_player; i++) {
        gamelock.lock();

        outqueue.add(pis);
        outqueue.add("Middle Earth");

        outqueue.add(g.getInitUnit());
        outqueue.add(irs);

        System.out.println("G[" + gid + "]distributed uid " + uids.get(i) + "'s info.");
        System.out.println("G[" + gid + "] outqueue size: " + outqueue.size());
        gamelock.unlock();
      }
      System.out.println("G[" + gid + "] outqueue size: " + outqueue.size());
      System.out.println("G[" + gid + "]Read unit placement.");

      ArrayList<TurnResponse> trs = new ArrayList<>();
      System.out.println("G[" + gid + "] outqueue size: " + outqueue.size());

      while (!outqueue.isEmpty()) {
      }

      System.out.println("G[" + gid + "] outqueue size: " + outqueue.size());
      while (inqueue.size() != max_player) {
      }
      System.out.println("G[" + gid + "] queue fully populated. begin reading.");

      for (int count = 0; count < max_player; count++) {
        gamelock.lock();
        ArrayList<TurnResponse> tr = (ArrayList<TurnResponse>)inqueue.poll();

        gamelock.unlock();

        System.out.println("G[" + gid + "]Received unit placement.");
        trs.addAll(tr);
      }

      System.out.println("G[" + gid + "]resolve initial placement.");

      g.initUnit(trs);
      System.out.println("G[" + gid + "]finished initial placement.");

      //      gamelock.lock();
      //      for (int i = 0; i < max_player; i++) {
      //       outqueue.add(gid);
      //  outqueue.add(trs);
      // }
      //gamelock.unlock();
      //
    }

    /*
a. Locks the game state to prevent concurrent modification.
b. Distributes the initial game state to all players.
c. Enters the main game loop, which continues until the game ends.
d. Within the loop, it waits for and processes player requests, such as quitting the game, switching out of the game, or sending game-related requests.
e. Resolves the game state after processing all player requests.
f. Sends updated game state (TurnResponse) to all players.
g. Unlocks the game state once all updates are sent.
h. When the game ends, it sets the state to -1 and prints a message.
    */
    public void play() throws ClassNotFoundException, InterruptedException, Exception {
      // first restore the game.
      gamelock.lock();

      for (int i = 0; i < max_player; i++) {
        outqueue.add(gid);
        ArrayList<TurnResponse> trs = g.turnResponse();

        outqueue.add(trs);
      }
      gamelock.unlock();
      this.state = 1;
      System.out.println("G[" + gid + "]Distributed Initial Placement.");
      System.out.println("G[" + gid + "]Game Start.");
      while (!g.checkEnd()) {
        System.out.println("G[" + gid + "]Waiting for requests.");
        int count = 0;
        ArrayList<Request> re = new ArrayList<>();
        ArrayList<PlayerInfo> pis = new ArrayList<>();
        while (inqueue.isEmpty()) {
        }
        while (count != max_player) {
          while (inqueue.isEmpty()) {
          }

          //TODO: differentiate switch and drop.
          if ((Integer)inqueue.peek() == -1) {
            gamelock.lock();

            Integer errcode = (Integer)inqueue.take();
            Integer dropped = (Integer)inqueue.take();
            gamelock.unlock();
            System.out.println("G[" + gid + "]Player " + dropped + " quits.");

            if (g.checkLose(uids.indexOf(dropped))) {
              //game continues. the dropped player has already lost.
              gamelock.lock();
              max_player--;
              int ind = uids.indexOf(dropped);
              uids.remove(ind);
              //online.remove(ind);
              gamelock.unlock();
            }
            else {
              //TODO: game needs to pause. lost a player.

              throw(new Exception("Player Dropped"));
            }
          }

          // Switch.
          // Does nothing.
          else if ((Integer)inqueue.peek() == -2) {
            gamelock.lock();
            Integer errcode = (Integer)inqueue.take();
            Integer dropped = (Integer)inqueue.take();
            gamelock.unlock();
            System.out.println("G[" + gid + "]player " + dropped +
                               " switches out of the game.");
          }
          else {
            gamelock.lock();
            Integer u = (Integer)inqueue.take();
            System.out.println("G[" + gid + "]Receive request from player " + u + ".");
            ArrayList<Request> reqs = (ArrayList<Request>)inqueue.take();
            for (int j = 0; j < reqs.size(); j++) {
              pis.add(reqs.get(0).getIssuerId());
            }
            gamelock.unlock();
            re.addAll(reqs);
            count++;
            System.out.println("G[" + gid + "]Received Request from " + count +
                               " players.");
          }
        }
        System.out.println("G[" + gid + "]Received Request from " + count +
                           " players, resolving.");

        ArrayList<TurnResponse> trs = g.turn(re, pis);
        System.out.println("G[" + gid + "]Copies of turn response needed: " + max_player);

        gamelock.lock();
        for (int i = 0; i < max_player; i++) {
          outqueue.add(gid);
          outqueue.add((ArrayList<TurnResponse>)trs.clone());

          System.out.println("G[" + gid + "]Sending back response " + i);
        }

        System.out.println("G[" + gid + "]Queued responses: " + outqueue.size());

        gamelock.unlock();
      }

      state = -1;
      System.out.println("\n=================\nGame No. " + gid +
                         " has finished.\n=================\n");
    }

    /*
a. The run() method is the main entry point for the game server thread.
b. It checks the current game state and either initializes a new game or continues an existing one by calling the play() method.
c. If an exception occurs, it sets the state to -1, clears the input queue, and sends an error response to players.
    */
    @Override
    public void run() {
      try {
        // directly continue game
        if (state == 2) {
          state = 1;
          play();
        }

        // first initialization
        else if (state == 0) {
          g = new Game(max_player, null, System.out, "Middle Earth", 20, 24);
          init_game();
          state = 1;
          play();
        }

        //
        else if (state == 1) {
          play();
        }

        // terminated.
        // never should need this.
        else if (state == -1) {
          System.out.println("You are not supposed to be here.");
        }
      }
      catch (Exception e) {
        if (state == 1) {
          this.state = -1;
          System.out.println("Experienced " + e.getMessage() + ".");
          inqueue.clear();

          //TODO: notify other users?
          for (int i = 0; i < max_player; i++) {
            outqueue.add(-1);

            System.out.println("G[" + gid + "]Sending back response " + i);
          }
        }
      }
    }
  }

  /*
a. This method hashes a given password using the MessageDigest instance 'md'.
b. Converts the hashed bytes into a hexadecimal string and returns it.
  */
  private String hashPassword(String pswd) {
    byte[] hashedbts = md.digest(pswd.getBytes());
    StringBuilder hash_b = new StringBuilder();
    for (byte b : hashedbts) {
      hash_b.append(String.format("%02x", b));
    }
    String hash = hash_b.toString();

    return hash;
  }
  // register/login the user, saves IO streams. returns the uid of the user.

  /*
a. This method handles user registration and login.
b. It first establishes a connection with the client and sets up input and output streams.
c. Waits for a client request (register or login) and processes it accordingly.
d. For registration, it checks if the username is already registered or if the provided passwords match. If successful, it registers the user and adds the necessary details to data structures.
e. For login, it verifies the username and password. If successful, it updates the client, input, and output data structures for the user.
f. Returns the user ID of the logged-in or registered user.
  */
  private int handle_connection(Socket client)
      throws IOException, ClassNotFoundException {
    System.out.println(client);
    InputStream is = client.getInputStream();
    ObjectInputStream ois = new ObjectInputStream(is);
    OutputStream os = client.getOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(os);
    oos.flush();
    int uid = -1;
    System.out.println("Connection established.");
    while (true) {
      System.out.println("Waiting for login/register");
      Integer typ = (Integer)ois.readObject();

      // register
      if (typ == 1) {
        String uname = (String)ois.readObject();
        String password = (String)ois.readObject();
        String confirm = (String)ois.readObject();
        if (userNames.indexOf(uname) != -1) {
          // username has been registered.
          oos.writeObject(-1);
          oos.writeObject(new String(
              "Error: this username has been registered. Try login if you own this username, or try another username."));
          System.out.println("Error registering");
        }
        else if (!password.equals(confirm)) {
          oos.writeObject(-2);
          oos.writeObject(
              new String("Error: passwords are not the same. Please check again."));
          System.out.println("Error registering");
        }
        else {
          lock.lock();
          userNames.add(uname);
          uid = userNames.size() - 1;
          oos.writeObject(uid);
          oos.writeObject(new String("Successfully registered as " + uname + "."));
          String hash = hashPassword(password);
          passwords.put(uname, hash);
          clients.add(client);
          inputs.add(ois);
          outputs.add(oos);
          ArrayList<Integer> games = new ArrayList<>();
          userJoinedGames.add(games);
          lock.unlock();

          return uid;
        }
      }

      //login
      else if (typ == -1) {
        String name = (String)ois.readObject();
        String password = (String)ois.readObject();
        if (userNames.indexOf(name) == -1) {
          oos.writeObject(-1);
          oos.writeObject(
              "Error: this username does not exist. Register if you want to use this username, or check your spelling.");
          System.out.println("Error logging in");
        }
        else if (!passwords.get(name).equals(hashPassword(password))) {
          oos.writeObject(-2);
          oos.writeObject("Error: wrong password. Check your spelling.");
          System.out.println("Error logging in");
        }
        else {
          lock.lock();

          oos.writeObject(userNames.indexOf(name));
          oos.writeObject("Welcome back, " + name + ".");
          uid = userNames.indexOf(name);
          clients.set(uid, client);
          inputs.set(uid, ois);
          outputs.set(uid, oos);
          lock.unlock();

          return uid;
        }
      }
    }
  }
}
