package edu.duke.ece651.team12.server;

import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.TurnResponse;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class V3ServerTest {
  @Timeout(value = 200, unit = TimeUnit.SECONDS)
  @Test
  public void test() throws Exception {
    try {
      new Thread(() -> {
        try {
          String args[] = new String[0];
          App app = new App();
          app.main(args);
        }
        catch (Exception e) {
        }
      }).start();

      Thread.sleep(10);
      Socket client = new Socket("0.0.0.0", 4444);

      System.out.println(client);
      System.out.println("connected;");
      OutputStream os = client.getOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.flush();

      InputStream is = client.getInputStream();
      ObjectInputStream ois = new ObjectInputStream(is);
      oos.writeObject(1);
      oos.writeObject("D");

      oos.writeObject("S");
      oos.writeObject("S");
      Integer x = (Integer)ois.readObject();
      String res = (String)ois.readObject();
      System.out.println(res + x);
      HashMap<Integer, String> hsm = (HashMap<Integer, String>)ois.readObject();
      oos.writeObject(-1);
      oos.writeObject(2);
      //      oos.writeObject(20);

      Socket c2 = new Socket("0.0.0.0", 4444);

      System.out.println(c2);
      System.out.println("connected;");
      OutputStream os2 = c2.getOutputStream();
      ObjectOutputStream oos2 = new ObjectOutputStream(os2);
      oos2.flush();

      InputStream is2 = c2.getInputStream();
      ObjectInputStream ois2 = new ObjectInputStream(is2);
      oos2.writeObject(1);
      oos2.writeObject("D");

      oos2.writeObject("S");
      oos2.writeObject("S");

      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);

      oos2.writeObject(1);
      oos2.writeObject("D");

      oos2.writeObject("S");
      oos2.writeObject("R");

      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);
      oos2.writeObject(-1);
      oos2.writeObject("A");

      oos2.writeObject("S");

      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);
      oos2.writeObject(1);
      oos2.writeObject("E");

      oos2.writeObject("S");
      oos2.writeObject("S");

      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);

      c2.close();
      c2 = new Socket("0.0.0.0", 4444);

      System.out.println(c2);
      System.out.println("connected;");
      os2 = c2.getOutputStream();
      oos2 = new ObjectOutputStream(os2);
      oos2.flush();

      is2 = c2.getInputStream();
      ois2 = new ObjectInputStream(is2);
      oos2.writeObject(1);
      oos2.writeObject("gdgf");

      oos2.writeObject("E");
      oos2.writeObject("B");
      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();

      oos2.writeObject(-1);
      oos2.writeObject("E");

      oos2.writeObject("B");
      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();

      oos2.writeObject(-1);
      oos2.writeObject("E");

      oos2.writeObject("S");

      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();

      HashMap<Integer, String> hs = (HashMap<Integer, String>)ois2.readObject();

      oos2.writeObject(9);
      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);

      hs = (HashMap<Integer, String>)ois2.readObject();

      oos2.writeObject(0);
      x = (Integer)ois2.readObject();
      res = (String)ois2.readObject();
      System.out.println(res + x);

      Object t = ois.readObject();
      t = ois.readObject();
      t = ois2.readObject();
      t = ois2.readObject();

      //oos.writeObject(0);
      oos.writeObject("a");

      //oos2.writeObject(1);
      oos2.writeObject("b");
      //hs = (HashMap<Integer, String>)ois2.readObject();
      //oos.writeObject(0);

      ois.readObject();
      ois.readObject();

      ois.readObject();

      ois.readObject();

      ois.readObject();
      ois2.readObject();
      ois2.readObject();

      ois2.readObject();

      ois2.readObject();

      ois2.readObject();

      oos.writeObject(new ArrayList<TurnResponse>());
      oos2.writeObject(new ArrayList<TurnResponse>());
      ois.readObject();
      ois2.readObject();
      oos.writeObject(0);
      oos.writeObject(new ArrayList<Request>());
      oos2.writeObject(0);
      oos2.writeObject(new ArrayList<Request>());
      ois.readObject();
      ois2.readObject();
      oos.writeObject(-2);
      //oos.writeObject(new ArrayList<Request>());
      oos2.writeObject(0);
      oos2.writeObject(new ArrayList<Request>());
    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
