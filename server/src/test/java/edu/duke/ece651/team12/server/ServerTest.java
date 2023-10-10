package edu.duke.ece651.team12.server;

// import static org.junit.jupiter.api.Assertions.*;
import edu.duke.ece651.team12.shared.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class ServerTest {
  /*
  @Test
  public void test_connection() throws IOException {
    ServerSocket ss = new ServerSocket(4444);
    Socket cs = ss.accept();
    Request re = new Request(0, 0, 0, 0);
    ArrayList<Integer> arr = new ArrayList<Integer>();
    arr.add(1);
    arr.add(2);
    InitResponse ir =
        new InitResponse(0, new PlayerInfo(0, "", new Color(0, 0, 0)), 1223, "Bree", arr);

    OutputStream outputStream = cs.getOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(new Integer(1));
    objectOutputStream.flush();
    objectOutputStream.writeObject(ir);
    objectOutputStream.flush();

    objectOutputStream.close();
    ss.close();
  }
  */
  /*
  @Disabled
  @Test
  public void test() throws IOException, ClassNotFoundException {
    InputStreamReader is = new InputStreamReader(System.in);
    BufferedReader bfr = new BufferedReader(is);
    Server server = new Server(4444, 3, "Middle Earth", bfr, System.out);
    server.phase1createSocket();
  }
  */
}
