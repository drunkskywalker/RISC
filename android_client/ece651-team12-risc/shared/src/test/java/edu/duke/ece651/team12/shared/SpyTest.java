package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SpyTest {
  @Test
  public void test() {
    Spy s = new Spy(new PlayerInfo(1));

    assertTrue(!s.hasMoved);
  }
}
