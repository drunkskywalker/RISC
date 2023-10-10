package edu.duke.ece651.team12.server;
/*
import edu.duke.ece651.team12.shared.Game;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class V2Server {
  private int port;
  private ArrayList<String> gameFileNames = new ArrayList<>();
  private ArrayList<Thread> gameThreads = new ArrayList<>();
  private ArrayList<Integer> availableSeats = new ArrayList<>();
  private ArrayList<String> userNames = new ArrayList<>();
  private HashMap<String, String> passwords = new HashMap<>();
  private ArrayList<ArrayList<Integer> > userJoinedGames = new ArrayList<>();
  private ServerSocketChannel server_socket;
  private Selector selector;
  private ArrayList<Socket> clients = new ArrayList<>();
  private ArrayList<ObjectOutputStream> outputs = new ArrayList<>();
  private ArrayList<ObjectInputStream> inputs = new ArrayList<>();

  // inter-thread communication
  private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

  private class RegLogin implements Runnable {
    private Socket client;
    public RegLogin(Socket client) { this.client = client; }
    @Override
    public void run() {
      try {
        handle_connection(client);
      }
      catch (Exception e) {
        System.out.println(e.getStackTrace());
      }
    }
  }

  // 0: idle, 1: gaming. Idle and Gaming users have different sendable objects
  private ArrayList<Integer> userStatus = new ArrayList<>();

  public V2Server(int port) { this.port = port; }

  public void init() throws IOException {
    server_socket = ServerSocketChannel.open();
    server_socket.bind(new InetSocketAddress(port));
    server_socket.configureBlocking(false);
    selector = Selector.open();
    server_socket.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void main() throws IOException, ClassNotFoundException {
    while (true) {
      int cns = selector.select();
      if (cns == 0) {
        continue;
      }
      Iterable<SelectionKey> keys = selector.selectedKeys();
      for (SelectionKey k : keys) {
        if (k.isAcceptable()) {
          SocketChannel client = ((ServerSocketChannel)k.channel()).accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
          Socket c_socket = client.socket();
          System.out.println("transferring register / login to a new thread");
          Thread t = new Thread(new RegLogin(c_socket));
          t.start();
        }
        else if (k.isReadable()) {
          SocketChannel client_channel = (SocketChannel)k.channel();
          Socket client = client_channel.socket();
          int uid = clients.indexOf(client);
          int u_status = userStatus.get(uid);
          Integer game_id = (Integer)inputs.get(uid).readObject();
          if (game_id == -1) {
            Integer num_player = (Integer)inputs.get(uid).readObject();
            game_id = gameThreads.size();
          }
        }
      }
    }
  }

  private void handle_connection(Socket client)
      throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
    int uid;
    while (true) {
      Integer typ = (Integer)ois.readObject();
      // register
      System.out.println(typ);
      if (typ == 1) {
        String uname = (String)ois.readObject();
        String password = (String)ois.readObject();
        String confirm = (String)ois.readObject();
        if (userNames.indexOf(uname) != -1) {
          // username has been registered.
          oos.writeObject(new String(
              "Error: this username has been registered. Try login if you own this username, or try another username."));
        }
        else if (!password.equals(confirm)) {
          oos.writeObject(
              new String("Error: passwords are not the same. Please check again."));
        }
        else {
          oos.writeObject(new String("Successfully registered as " + uname + "."));
          userNames.add(uname);
          passwords.put(uname, password);
          clients.add(client);
          inputs.add(ois);
          outputs.add(oos);
          ArrayList<Integer> games = new ArrayList<>();
          userJoinedGames.add(games);
          userStatus.add(0);
          uid = userNames.size() - 1;
          break;
        }
      }
      else if (typ == -1) {
        String name = (String)ois.readObject();
        String password = (String)ois.readObject();
        if (userNames.indexOf(name) == -1) {
          oos.writeObject(
              "Error: this username does not exist. Register if you want to use this username, or check your spelling.");
        }
        else if (!passwords.get(name).equals(password)) {
          oos.writeObject("Error: wrong password. Check your spelling.");
        }
        else {
          oos.writeObject("Welcome back, " + name + ".");
          uid = userNames.indexOf(name);
          clients.set(uid, client);
          inputs.set(uid, ois);
          outputs.set(uid, oos);
          userStatus.set(uid, 0);
          break;
        }
      }
    }

    // out the while loop.
    ArrayList<Integer> games = userJoinedGames.get(uid);

    // resume all games the player is in and set user's status to playing.
    for (Integer i : games) {
      userStatus.set(uid, 1);
      String gnames = gameFileNames.get(i);
      Thread game = gameThreads.get(i);

      //TODO: resume game
    }
    ArrayList<Integer> free_games = new ArrayList<>();
    for (Integer i : availableSeats) {
      if (i > 0) {
        free_games.add(i);
      }
    }
    oos.writeObject(games);
    oos.writeObject(free_games);
  }

  private class RunGame implements Runnable {
    public final int gid;
    public Game game;
    public ArrayList<Integer> users;

    public RunGame(int gid, Game game, ArrayList<Integer> users) {
      this.gid = gid;
      this.game = game;
      this.users = users;
    }

    @Override
    public void run() {}
  }
}
*/
