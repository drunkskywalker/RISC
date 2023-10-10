package edu.duke.ece651.team12.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team12.shared.Color;
import edu.duke.ece651.team12.shared.InitResponse;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.TurnResponse;

public class V3ClientTest {
  @Test
  public void test_() {
    try {
      // server
      new Thread(() -> {
        try {
          ServerSocket server = new ServerSocket(4444);
          System.out.println(server);
          System.out.println("server runnning;");
          // accept client
          Socket client_socket = server.accept();
          System.out.println(client_socket);
          System.out.println("client connected;");
          OutputStream os_client = client_socket.getOutputStream();
          ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
          oos_client.flush();
          InputStream is_client = client_socket.getInputStream();
          ObjectInputStream ois_client = new ObjectInputStream(is_client);
          // registration
          int reg = (Integer) ois_client.readObject();
          String username = (String) ois_client.readObject();
          String password = (String) ois_client.readObject();
          String confirmation = (String) ois_client.readObject();
          assertEquals(reg, 1);
          assertEquals(username, "Bob");
          assertEquals(password, "password");
          assertEquals(confirmation, "password");
          oos_client.writeObject(0);
          oos_client.writeObject("Successfully registered");
          oos_client.flush();
          // login
          int login = (Integer) ois_client.readObject();
          username = (String) ois_client.readObject();
          password = (String) ois_client.readObject();
          assertEquals(login, -1);
          assertEquals(username, "Bob");
          assertEquals(password, "password");
          oos_client.writeObject(0);
          oos_client.writeObject("Successfully logged in");
          // new game
          int new_game = (Integer) ois_client.readObject();
          int num_players = (Integer) ois_client.readObject();
          assertEquals(new_game, -1);
          assertEquals(num_players, 2);
          oos_client.writeObject(1);
          oos_client.writeObject("Successfully created a new game");
          // join game
          int game_id = (Integer) ois_client.readObject();
          assertEquals(game_id, 1);
          oos_client.writeObject(0);
          oos_client.writeObject("Successfully joined the game");
          // initGame 
          oos_client.writeObject(1);
          oos_client.writeObject(2);
          String game_name = (String) ois_client.readObject();
          assertEquals(game_name, "Bob's Game");
          PlayerInfo p1 = new PlayerInfo(1, "Bob", new Color(255, 0, 0));
          PlayerInfo p2 = new PlayerInfo(2, "Alice", new Color(0, 255, 0));
          ArrayList<PlayerInfo> players = new ArrayList<>();
          players.add(p1);
          players.add(p2);
          oos_client.writeObject(p1);
          oos_client.writeObject(players);
          oos_client.writeObject("Bob's Game");
          ArrayList<InitResponse> init_responses = new ArrayList<InitResponse>();
          ArrayList<Integer> t1 = new ArrayList<Integer>();
          t1.add(1);
          init_responses.add(new InitResponse(0, p1, 6, "t0", t1));
          ArrayList<Integer> t2 = new ArrayList<Integer>();
          t2.add(0);
          init_responses.add(new InitResponse(1, p2, 6, "t1", t2));
          oos_client.writeObject(init_responses);
          // play
          ArrayList<TurnResponse> starting_response = new ArrayList<TurnResponse>();
          starting_response.add(new TurnResponse(0, p1, 6));
          starting_response.add(new TurnResponse(1, p2, 6));
          oos_client.writeObject(starting_response);
          int turn = (Integer) ois_client.readObject();
          assertEquals(turn, 0);
          ArrayList<Request> reqs = (ArrayList<Request>)ois_client.readObject();
          assertEquals(reqs.size(), 1);
          assertEquals(reqs.get(0).getIssuerId(), 0);
          assertEquals(reqs.get(0).getNumUnit(), 3);
          assertEquals(reqs.get(0).getSourceId(), 0);
          assertEquals(reqs.get(0).getDestinationId(), 1);
          ArrayList<TurnResponse> turn_responses = new ArrayList<TurnResponse>();
          turn_responses.add(new TurnResponse(0, p1, 3)); // 6 - 3
          turn_responses.add(new TurnResponse(1, p1, 1)); // lucky
          oos_client.writeObject(turn_responses);
          // close
          oos_client.close();
          ois_client.close();
          client_socket.close();
          server.close();
        }
        catch (Exception e) {
        }
      }).start();
      
      // client
      BufferedReader bfr = new BufferedReader(new StringReader("A\n0\n1\n3\nC\n"));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos, true);
      V3Client client = new V3Client("0.0.0.0", 4444, bfr, out);
      client.connect();
      // registration
      SimpleEntry<Integer,String> se = client.Register("Bob", "password", "password");
      assertEquals(se.getKey().intValue(), 0);
      assertEquals(se.getValue(), "Successfully registered");
      // login
      se = client.Login("Bob", "password");
      assertEquals(se.getKey().intValue(), 0);
      assertEquals(se.getValue(), "Successfully logged in");
      // new game
      se = client.newGame(2);
      assertEquals(se.getKey().intValue(), 1);
      assertEquals(se.getValue(), "Successfully created a new game");
      // join game
      se = client.joinGame(1);
      assertEquals(se.getKey().intValue(), 0);
      assertEquals(se.getValue(), "Successfully joined the game");
      // initGame
      client.initGame("Bob's Game");
      // play
      client.Play();
      // close
      client.close();
    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
