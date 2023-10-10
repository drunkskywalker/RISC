package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class UpgradeRequest extends Request {
  public UpgradeRequest(PlayerInfo issuer_id, int source_id, ArrayList<Integer> units) {
    super(issuer_id, 0, source_id, 0);
    this.units = units;
  }
}
