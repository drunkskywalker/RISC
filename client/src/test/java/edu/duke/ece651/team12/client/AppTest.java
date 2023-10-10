package edu.duke.ece651.team12.client;

import edu.duke.ece651.team12.shared.BasicMapFactory;
import edu.duke.ece651.team12.shared.Color;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.TurnResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AppTest {
  private class mockServer implements Runnable {
    @Override
    public void run() {
      try {
        ServerSocket s = new ServerSocket(4444);
        Socket cs = s.accept();
        System.out.println("we are here!");
        OutputStream os = cs.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        InputStream is = cs.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        System.out.println("we are here!");
        oos.writeObject(Integer.valueOf(0));
        oos.flush();
        System.out.println("we are here!");
        oos.writeObject(Integer.valueOf(1));
        oos.flush();
        System.out.println("we are here!");
        String name = (String)ois.readObject();
        PlayerInfo p = new PlayerInfo(0, name, new Color(255, 0, 0));
        PlayerInfo p2 = new PlayerInfo(1, "GGGG", new Color(0, 255, 0));
        oos.writeObject(p);
        oos.flush();
        ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
        pis.add(p);
        pis.add(p2);
        oos.writeObject(pis);
        oos.flush();
        BasicMapFactory bmf = new BasicMapFactory(pis, 2, 6);
        oos.writeObject(bmf.createInitResponses(6));
        oos.flush();
        System.out.println("we are here!");
        oos.writeObject(new String("ME"));
        oos.flush();
        oos.writeObject(Integer.valueOf(1));
        oos.flush();
        ArrayList<TurnResponse> trs = (ArrayList<TurnResponse>)ois.readObject();
        ArrayList<TurnResponse> ttr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
          ttr.add(new TurnResponse(i, p, 0));
        }
        ttr.add(new TurnResponse(5, p2, 0));
        oos.writeObject(ttr);
        ArrayList<Request> rqs = (ArrayList<Request>)ois.readObject();

        ttr = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
          ttr.add(new TurnResponse(i, p, 12));
        }

        oos.writeObject(ttr);
        cs = s.accept();
        os = cs.getOutputStream();
        oos = new ObjectOutputStream(os);

        is = cs.getInputStream();
        ois = new ObjectInputStream(is);
        System.out.println("we are here!");
        oos.writeObject(Integer.valueOf(0));
        oos.flush();
        cs.close();
        s.close();
        System.out.println("bye");
        return;
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
  @Disabled
  @Test
  public void test() throws Exception {
    Thread t1 = new Thread(new mockServer());
    t1.start();

    ByteArrayInputStream in =
        new ByteArrayInputStream("Gandalf\n1\nC\nC\nC\n".getBytes());
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;

    System.setIn(in);
    System.setOut(System.out);
    String a[] = new String[1];
    a[0] = "0";
    App.main(a);
    String b[] = new String[2];
    b[0] = "0";
    b[1] = "d";

    App.main(b);
    String args[] = new String[2];
    args[0] = "0";
    args[1] = "4444";
    App.main(args);
    App.main(args);
    App.main(args);
  }
}
