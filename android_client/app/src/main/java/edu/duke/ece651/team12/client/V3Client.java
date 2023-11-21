package edu.duke.ece651.team12.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team12.shared.*;
import edu.duke.ece651.team12.shared.InitResponse;
import edu.duke.ece651.team12.shared.Player;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.TurnResponse;

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
    String uname;


    public V3Client(String host, int port, BufferedReader bfr, PrintStream out) {
        this.host = host;
        this.port = port;
        this.bfr = bfr;
        this.out = out;
    }

    /* Connect to server.
     */
    public void connect() throws IOException {
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
        if (uid >=0) {uname = username;}
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
        if (uid >=0) {uname = username;}
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


    public ArrayList<InitResponse> JoinNewGame(int ind) throws IOException, ClassNotFoundException {
        Integer tot = (Integer)ois.readObject();
        System.out.println(ind + " out of " + tot);
        oos.writeObject(uname);

        ClientApp.player_info = (PlayerInfo)ois.readObject();
        System.out.println("JoinNewGame===================");
        System.out.println(ClientApp.player_info.player_name);

        ClientApp.enemies = (ArrayList<PlayerInfo>)ois.readObject();
        for (int i = 0; i < ClientApp.enemies.size(); i++) {
            if (ClientApp.enemies.get(i).player_id ==  ClientApp.player_info.player_id) {
                ClientApp.enemies.remove(i);
                break;
            }
        }
        System.out.println("I'm here");
        String mapName = (String)ois.readObject();
        ArrayList<InitResponse> irs = (ArrayList<InitResponse>)ois.readObject();
        Integer numUnit = (Integer)ois.readObject();
        return irs;
        //Move to next activity

        //ArrayList<TurnResponse> trs = player.initUnit(numUnit);
        //initMap(trs);
        //System.out.println("Game Initialized.");
    }

    public ArrayList<InitResponse> joinedGameBefore(int ind) throws IOException, ClassNotFoundException {
        System.out.println("This looks familiar.");
        Integer gid = (Integer)ois.readObject();
        ind = (Integer)ois.readObject();

        ClientApp.player_info = (PlayerInfo)ois.readObject();
        ClientApp.enemies = (ArrayList<PlayerInfo>)ois.readObject();
        for (int i = 0; i < ClientApp.enemies.size(); i++) {
            if (ClientApp.enemies.get(i).player_id == ClientApp.player_info.player_id) {
                ClientApp.enemies.remove(i);
                break;
            }
        }
        String mapName = (String)ois.readObject();
        System.out.println("I'm back");
        ArrayList<InitResponse> irs = (ArrayList<InitResponse>)ois.readObject();
        return irs;
    }

    public int initGame() throws IOException, ClassNotFoundException {
        System.out.println("Initializing Game");
        Integer ind = (Integer)ois.readObject();
        ArrayList<InitResponse> irs = null;
        if (ind >= 0) {
            //new Game
            irs = JoinNewGame(ind);
//TODO: switch to unit initialization screen
            //TODO: territory arraylist in map. iterate through the list, find
            // territory whose playerinfo equals player_info. ask for player to
            // place maximum 20 units onto those territories.
            // Generate as many TurnResponse s as Territories owned by the player.
            // use client to send back to the server. switch to game operation activity.

        }
        // joined this game before
        else if (ind == -2) {
            irs = joinedGameBefore(ind);
            // switch directly to game operation activity
        }
        else {return -1;}
        ClientApp.map = new PlayerMap("Middle Earth");
        for (InitResponse i: irs) {
            ClientApp.map.init(i);
        }
        System.out.println(ClientApp.player_info.player_name);
        System.out.println("InitGame is returning");
        return ind;
    }


    /*send initial unit placements to the server during game initialization,*/
    public void initMap(ArrayList<TurnResponse> trs)
            throws IOException, ClassNotFoundException {
        oos.writeObject(trs);
    }


    /* receive and update the map based on the server's responses during the game.*/
    public void restoreMap() throws IOException, ClassNotFoundException {
        System.out.println("Starting to restore map.");
        ArrayList<TurnResponse> deployment =
                (ArrayList<TurnResponse>)((ArrayList<TurnResponse>)ois.readObject()).clone();
        System.out.println("Receiving deployment: containing " + deployment.size() +
                " TurnResponses.");
        for (TurnResponse re : deployment) {
            System.out.println(re.getPlayerInfo());
        }
        ClientApp.map.update((ArrayList<TurnResponse>)deployment.clone());
        System.out.println("Map Restored.");
        //System.out.println(player.map_displayer.display());
    }

    public void switchGame() throws IOException, ClassNotFoundException{
        oos.writeObject(-2);
        oos.writeObject(uid);
        return;
    }

    public void requ(ArrayList<Request> requests) throws IOException, ClassNotFoundException {
        oos.writeObject(uid);
        oos.writeObject(requests);

    }

    public ArrayList<TurnResponse> resp() throws IOException, ClassNotFoundException  {
        ArrayList<TurnResponse> trs = (ArrayList<TurnResponse>)ois.readObject();

        if (trs == null) {
            System.out.println("Server dropped connection.");

        }

        return trs;

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

