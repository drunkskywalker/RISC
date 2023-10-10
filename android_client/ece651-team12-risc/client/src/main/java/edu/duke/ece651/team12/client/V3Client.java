package edu.duke.ece651.team12.client;
import edu.duke.ece651.team12.shared.InitResponse;
import edu.duke.ece651.team12.shared.Player;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.TextMapDisplayer;
import edu.duke.ece651.team12.shared.TurnResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

public class V3Client {
  
  String host;
  int port;
  ObjectInputStream ois;
  ObjectOutputStream oos;
  Socket sock;
  Player player;
  private BufferedReader bfr;
  private PrintStream out;
  int uid;

  
  public V3Client(String host, int port, BufferedReader bfr, PrintStream out) {
    this.host = host;
    this.port = port;
    this.bfr = bfr;
    this.out = out;
  }

  /* Connect to server.
   */
  public void connect() throws IOException, ClassNotFoundException {
    sock = new Socket(host, port);

    oos = new ObjectOutputStream(sock.getOutputStream());
    oos.flush();
    ois = new ObjectInputStream(sock.getInputStream());
    out.println("Connected to Server " + host + " on " + port);
  }

  /* Login the user with username and password.
   * @returns uid and message if success, returns -1 and error message if fail.
   */
  public SimpleEntry<Integer, String> Login(String username, String password)
      throws IOException, ClassNotFoundException {
    oos.writeObject(-1);
    oos.writeObject(username);
    oos.writeObject(password);
    uid = (Integer)ois.readObject();
    String message = (String)ois.readObject();
    return new SimpleEntry<Integer, String>(uid, message);
  }

  /* Register the user with username and password.
   * @returns uid and message if success, returns -1 and error message if fail.
   */
  public SimpleEntry<Integer, String> Register(String username,
                                               String password,
                                               String confirmation)
      throws IOException, ClassNotFoundException {
    oos.writeObject(1);
    oos.writeObject(username);
    oos.writeObject(password);
    oos.writeObject(confirmation);
    uid = (Integer)ois.readObject();
    String message = (String)ois.readObject();
    return new SimpleEntry<Integer, String>(uid, message);
  }

  public HashMap<Integer, String> availableGames()
      throws IOException, ClassNotFoundException {
    HashMap<Integer, String> gms = (HashMap<Integer, String>)ois.readObject();
    return gms;
  }
  /* requests to start a new game
   * @returns game id and message.
   */
  public SimpleEntry<Integer, String> newGame(int numPlayer)
      throws IOException, ClassNotFoundException {
    oos.writeObject(-1);
    oos.writeObject(numPlayer);
    Integer gid = (Integer)ois.readObject();
    String message = (String)ois.readObject();
    return new SimpleEntry<Integer, String>(gid, message);
  }

  /* requests to join a game
   * @returns game 
   */
  public SimpleEntry<Integer, String> joinGame(int gid)
      throws IOException, ClassNotFoundException {
    oos.writeObject(gid);
    Integer response = (Integer)ois.readObject();
    String message = (String)ois.readObject();
    return new SimpleEntry<Integer, String>(response, message);
  }

  public void initGame(String name) throws IOException, ClassNotFoundException {
    System.out.println("Initializing Game");
    Integer ind = (Integer)ois.readObject();
    if (ind >= 0) {
      Integer tot = (Integer)ois.readObject();
      System.out.println(ind + " out of " + tot);
      oos.writeObject(name);
      PlayerInfo p = (PlayerInfo)ois.readObject();
      ArrayList<PlayerInfo> pis = (ArrayList<PlayerInfo>)ois.readObject();
      for (int i = 0; i < pis.size(); i++) {
        if (pis.get(i).player_id == p.player_id) {
          pis.remove(i);
          break;
        }
      }
      System.out.println("I'm here");
      String mapName = (String)ois.readObject();
      ArrayList<InitResponse> irs = (ArrayList<InitResponse>)ois.readObject();
      Integer numUnit = (Integer)ois.readObject();
      PlayerMap map = new PlayerMap(mapName);
      TextMapDisplayer tmd = new TextMapDisplayer(map, p);
      player = new Player(p, pis, map, tmd, bfr, out);
      player.initializeMap(irs);
      ArrayList<TurnResponse> trs = player.initUnit(numUnit);
      initMap(trs);
      System.out.println("Game Initialized.");
    }
    // joined this game before
    else if (ind == -2) {
      System.out.println("This looks familiar.");
      Integer gid = (Integer)ois.readObject();
      ind = (Integer)ois.readObject();
      PlayerInfo p = (PlayerInfo)ois.readObject();
      ArrayList<PlayerInfo> pis = (ArrayList<PlayerInfo>)ois.readObject();
      for (int i = 0; i < pis.size(); i++) {
        if (pis.get(i).player_id == p.player_id) {
          pis.remove(i);
          break;
        }
      }
      String mapName = (String)ois.readObject();
      PlayerMap map = new PlayerMap(mapName);
      TextMapDisplayer tmd = new TextMapDisplayer(map, p);

      player = new Player(p, pis, map, tmd, bfr, out);
      System.out.println("I'm back");
      ArrayList<InitResponse> irs = (ArrayList<InitResponse>)ois.readObject();
      player.initializeMap(irs);
    }
  }

  
  /*send initial unit placements to the server during game initialization,*/
  private void initMap(ArrayList<TurnResponse> trs)
      throws IOException, ClassNotFoundException {
    oos.writeObject(trs);
  }


  /* receive and update the map based on the server's responses during the game.*/
  public void restoreMap() throws IOException, ClassNotFoundException {
    ArrayList<TurnResponse> deployment =
        (ArrayList<TurnResponse>)((ArrayList<TurnResponse>)ois.readObject()).clone();
    System.out.println("Receiving deployment: containing " + deployment.size() +
                       " TurnResponses.");
    for (TurnResponse re : deployment) {
      System.out.println(re.getPlayerInfo());
    }
    player.map.update((ArrayList<TurnResponse>)deployment.clone());
    System.out.println("Map Restored.");
    System.out.println(player.map_displayer.display());
  }

  
  /* asks for player turn input, send to server, get result, update status.
   * @returns if game ends or player switches game. notifies server if player switches.
   * @throws IOException if streams are closed. 
   */
  public void Play() throws IOException, ClassNotFoundException {
    restoreMap();
    try {
      while (player.checkEnd() == null) {
        out.println("Turn begins:");

        ArrayList<Request> requests = player.turn();
        if (requests == null) {
          System.out.println("Switching as user demand");
          oos.writeObject(-2);
          oos.writeObject(uid);
          return;
        }
        oos.writeObject(uid);
        oos.writeObject(requests);

        ArrayList<TurnResponse> trs = (ArrayList<TurnResponse>)ois.readObject();
        if (trs == null) {
          System.out.println("Server dropped connection.");
        }
        player.endTurn(trs);
      }
    }
    catch (Exception e) {
      try {
        System.out.println("What are you doing down there?");
        System.out.println(e.getMessage());
        e.printStackTrace();
        oos.writeObject(-1);
        oos.writeObject(uid);
        close();
      }
      catch (Exception ex) {
        ex.printStackTrace();
        close();
        throw new IOException("Connection dropped. Exiting");
      }
    }
  }

  /* close socket.
   */
  public void close() throws IOException {
    if (sock != null) {
      sock.close();
    }
  }
}
