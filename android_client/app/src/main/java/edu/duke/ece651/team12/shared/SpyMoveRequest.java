package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class SpyMoveRequest extends Request {
  private static final long serialVersionUID = 30;

  public SpyMoveRequest(PlayerInfo issuer_id, int source_id, int destination_id) {
    super(issuer_id, 1, source_id, destination_id);
  }
}
