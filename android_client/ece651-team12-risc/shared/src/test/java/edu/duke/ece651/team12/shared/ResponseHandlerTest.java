package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ResponseHandlerTest {
  @Test
  public void test() {
    ResponseHandler rh = new ResponseHandler();

    InitResponseWP ir = new InitResponseWP(0,
                                           new PlayerInfo(0, "", new Color(0, 0, 0)),
                                           12,
                                           "Gondor",
                                           new ArrayList<Integer>(),
                                           2,
                                           2);
    Territory tr = rh.handleInitResponse(ir);
    assertEquals(tr.getId(), 0);
    assertEquals(tr.getName(), "Gondor");
    //   assertEquals(tr.toString(), "0. Gondor. Units: 12\n====================\n");
    // assertEquals(ir.toString(), rh.generateMessage(tr, ir));
    PlayerInfo pi2 = new PlayerInfo(1, "df", new Color(1, 1, 1));
    TurnResponse tu = new TurnResponse(1, pi2, 0);
    rh.handleTurnResponse(tr, tu);
    assertEquals(tr.getPlayerInfo(), pi2);
  }
}
