
package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
public class InitResponseTest {
  @Test
  public void test() {
    ResponseHandler rh = new ResponseHandler();

    InitResponse ir =
        new InitResponse(0,
                         new PlayerInfo(0, "Gandalf", new Color(255, 255, 255)),
                         12,
                         "Gondor",
                         new ArrayList<Integer>());

    Territory tr = rh.handleInitResponse(ir);
    assertEquals(tr.getId(), 0);
    assertEquals(tr.getName(), "Gondor");
    //assertEquals(tr.toString(), "0. Gondor. Units: 12\n====================\n");
  }
  /*
  @Test
  public void test_transmission() throws IOException, ClassNotFoundException {
    Socket sock = new Socket("0", 4444);
    InputStream inputStream = sock.getInputStream();
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    Integer req = (Integer)objectInputStream.readObject();
    InitResponse re = (InitResponse)objectInputStream.readObject();
    ArrayList<Integer> arr = new ArrayList<Integer>();
    arr.add(1);

    arr.add(2);
    assertEquals(re.getNeighbors(), arr);
    sock.close();
    PlayerMap map = new PlayerMap("Middle Earth");
    map.init(re);
    System.out.println(map.toString());
    //    assertEquals(false, true);
  }
  */
}
