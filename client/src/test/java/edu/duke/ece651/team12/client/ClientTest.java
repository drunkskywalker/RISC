package edu.duke.ece651.team12.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class ClientTest {
  @Disabled
  @Test
  public void test() throws Exception {
    ByteArrayInputStream in =
        new ByteArrayInputStream("Gandalf\n1\nC\nC\nC\n".getBytes());

    InputStreamReader is = new InputStreamReader(in);
    BufferedReader isr = new BufferedReader(is);
    /*
    Client c1 = new Client("0", 4444, 1, isr, System.out);
    c1.phase1connect();
    Client c2 = new Client("0", 4444, 1, isr, System.out);
    c2.phase1connect();
    Client c3 = new Client("0", 4444, 1, isr, System.out);
    c3.phase1connect();
    //assertTrue(false);
    */
    Client c = new Client("ddd", 0, isr, System.out);
    c.getName(0, 4);
    c.getName(1, 4);
    c.getName(2, 4);
    c.getName(3, 4);
  }
}
