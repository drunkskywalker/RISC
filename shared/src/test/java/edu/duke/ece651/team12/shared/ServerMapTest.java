package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import org.junit.jupiter.api.Test;

public class ServerMapTest {
  @Test
  public void test() {
    ServerMap map = new ServerMap("A");
    map.initialize();
    map.initialize(new BufferedReader(new InputStreamReader(System.in)));

    map.generateResonse();
  }
}
