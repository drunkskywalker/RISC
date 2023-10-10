/*
package edu.duke.ece651.team12.server;

import edu.duke.ece651.team12.shared.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  private int port;
  private int num_player;
  private Game game;
  private BufferedReader bfr;
  private PrintStream out;
  private ServerSocket server_socket;

  private ArrayList<Socket> scs = new ArrayList<>();
  private ArrayList<ObjectOutputStream> write_streams = new ArrayList<>();
  private ArrayList<ObjectInputStream> read_streams = new ArrayList<>();
  private String map_name;

  public Server(int port,
                int num_player,
                int init_unit,
                BufferedReader bfr,
                PrintStream out,
                String map_name,
                int num_territories) {
    this.port = port;
    this.num_player = num_player;
    this.game = new Game(num_player, bfr, out, map_name, init_unit, num_territories);
    this.map_name = map_name;
    this.bfr = bfr;
    this.out = out;
  }

  private void SocketError(int i) throws IOException {
    scs.get(i).close();
    scs.remove(i);
    read_streams.remove(i);
    write_streams.remove(i);
    num_player--;
    out.println("A player has disconnected from the game.");
  }

  public void init() throws IOException, ClassNotFoundException {
    server_socket = new ServerSocket(port);
    ArrayList<String> ss = new ArrayList<String>();
    for (int i = 0; i < num_player; i++) {
      Socket cs = server_socket.accept();
      scs.add(cs);
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;

      OutputStream os = cs.getOutputStream();
      oos = new ObjectOutputStream(os);
      write_streams.add(oos);

      InputStream is = cs.getInputStream();
      ois = new ObjectInputStream(is);
      read_streams.add(ois);
      oos.writeObject(Integer.valueOf(i));
      oos.flush();
      oos.writeObject(Integer.valueOf(num_player));
      oos.flush();
      String name = (String)ois.readObject();
      ss.add(name);
    }

    ArrayList<PlayerInfo> pis = game.initPlayerInfos(ss);
    ArrayList<InitResponse> irs = game.initMap();
    for (int i = 0; i < num_player; i++) {
      write_streams.get(i).writeObject(pis.get(i));
      write_streams.get(i).flush();
      write_streams.get(i).writeObject(pis);
      write_streams.get(i).flush();
      write_streams.get(i).writeObject(irs);
      write_streams.get(i).flush();
      write_streams.get(i).writeObject(map_name);
      write_streams.get(i).flush();
      write_streams.get(i).writeObject(game.getInitUnit());
      write_streams.get(i).flush();
    }
    ArrayList<TurnResponse> t0 = new ArrayList<>();
    for (int i = 0; i < num_player; i++) {
      ArrayList<TurnResponse> t =
          (ArrayList<TurnResponse>)read_streams.get(i).readObject();
      t0.addAll(t);
    }
    game.initUnit(t0);
    for (int i = 0; i < num_player; i++) {
      write_streams.get(i).writeObject(t0);
      write_streams.get(i).flush();
    }
  }

  public void play() throws IOException, ClassNotFoundException {
    while (!game.checkEnd()) {
      ArrayList<Request> requests = new ArrayList<Request>();
      ArrayList<PlayerInfo> pis = new ArrayList<>();
      for (int i = 0; i < num_player; i++) {
        try {
          ArrayList<Request> r = (ArrayList<Request>)read_streams.get(i).readObject();
          requests.addAll(r);
          for (int j = 0; j < r.size(); j++) {
            pis.add(game.players.get(i));
          }
        }
        catch (SocketException e) {
          if (!game.checkLose(i)) {
            return;
          }
          SocketError(i);
        }
        catch (EOFException e) {
          if (!game.checkLose(i)) {
            return;
          }
          SocketError(i);
        }
      }
      ArrayList<TurnResponse> t = game.turn(requests, pis);
      for (int i = 0; i < num_player; i++) {
        try {
          write_streams.get(i).writeObject(t);
          write_streams.get(i).flush();
        }
        catch (SocketException e) {
          SocketError(i);
        }
        catch (EOFException e) {
          SocketError(i);
        }
      }
    }
  }

  public void end() throws IOException {
    for (Socket c : scs) {
      c.close();
    }
    server_socket.close();
  }
}
*/
