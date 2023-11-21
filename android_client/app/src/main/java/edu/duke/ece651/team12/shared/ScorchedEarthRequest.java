package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class ScorchedEarthRequest extends Request {
  private static final long serialVersionUID = 31;

  public ScorchedEarthRequest(PlayerInfo issuer_id, int source_id) {
    super(issuer_id, 1, source_id, -1);
  }
}
