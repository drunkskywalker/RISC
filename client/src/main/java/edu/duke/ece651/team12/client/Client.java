package edu.duke.ece651.team12.client;

import edu.duke.ece651.team12.shared.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  private String address;
  private int port;
  private Socket sock;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private BufferedReader bfr;
  private PrintStream out;
  private Player player;

  public Client(String address, int port, BufferedReader bfr, PrintStream out) {
    this.address = address;
    this.port = port;
    this.bfr = bfr;
    this.out = out;
  }

  public String getName(int id, int tot) throws IOException {
    out.println("Please enter your name: ");
    String name = bfr.readLine();
    String num;
    if (id == 0) {
      num = "1st";
    }
    else if (id == 1) {
      num = "2nd";
    }
    else if (id == 2) {
      num = "3rd";
    }
    else {
      num = (id + 1) + "st";
    }
    out.println(name + ", welcome. You are the " + num + " player out of " + tot +
                " total players.");
    return name;
  }

  public void init() throws IOException, ClassNotFoundException {
    sock = new Socket(address, port);
    out.println("Successfully connected to " + address + " on port " + port);
    InputStream is = sock.getInputStream();
    ois = new ObjectInputStream(is);
    OutputStream os = sock.getOutputStream();
    oos = new ObjectOutputStream(os);
    Integer i = (Integer)ois.readObject();
    Integer tot = (Integer)ois.readObject();
    String name = getName(i, tot);
    oos.writeObject(name);
    oos.flush();
    PlayerInfo me = (PlayerInfo)ois.readObject();
    ArrayList<PlayerInfo> pis = (ArrayList<PlayerInfo>)ois.readObject();
    pis.remove(me.player_id);
    ArrayList<InitResponse> irs = (ArrayList<InitResponse>)ois.readObject();
    String map_name = (String)ois.readObject();
    PlayerMap map = new PlayerMap(map_name);
    TextMapDisplayer tmd = new TextMapDisplayer(map, me);
    player = new Player(me, pis, map, tmd, bfr, out);
    player.initializeMap(irs);
    Integer num_units = (Integer)ois.readObject();
    ArrayList<TurnResponse> tr = player.initUnit(num_units);
    oos.writeObject(tr);
    oos.flush();
    tr = (ArrayList<TurnResponse>)ois.readObject();
    player.map.update(tr);
  }

  public void play() throws IOException, ClassNotFoundException {
    while (player.checkEnd() == null) {
      out.println("Turn begins:");
      ArrayList<Request> requests = player.turn();
      oos.writeObject(requests);
      ArrayList<TurnResponse> trs = (ArrayList<TurnResponse>)ois.readObject();
      player.endTurn(trs);
    }
  }

  public void end() throws IOException {
    if (sock != null) {
      sock.close();
    }
  }
}
