package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.*;
import org.junit.jupiter.api.Test;

public class RequestTest {
  @Test
  public void test() {
    PlayerInfo p1 = new PlayerInfo(0, "Pass", new Color(0, 0, 0));
    Request r = new AttackRequest(p1, 2, 3, 4);
    assertEquals(r.getIssuerId(), p1);
    assertEquals(r.getNumUnit(), 2);
    assertEquals(r.getSourceId(), 3);
    assertEquals(r.getDestinationId(), 4);
    r = new MoveRequest(p1, 2, 3, 4);
    assertEquals(r.getIssuerId(), p1);
    assertEquals(r.getNumUnit(), 2);
    assertEquals(r.getSourceId(), 3);
    assertEquals(r.getDestinationId(), 4);
  }

  /**
  @Test
  public void test_transmission() throws Exception {
    Socket sock = new Socket("0", 4444);
    InputStream inputStream = sock.getInputStream();
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    Request re = (Request)objectInputStream.readObject();
    assertEquals(re.getIssuerId(), 0);
    sock.close();
  }
  */
}
