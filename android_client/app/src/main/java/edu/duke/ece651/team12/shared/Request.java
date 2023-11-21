package edu.duke.ece651.team12.shared;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Request implements Serializable {
  private final PlayerInfo issuer_id;
  private final int num_unit;
  protected ArrayList<Integer> units = new ArrayList<Integer>();
  private final int source_id;
  private final int destination_id;
  private static final long serialVersionUID = 12;
  public Request(PlayerInfo issuer_id, int num_unit, int source_id, int destination_id) {
    this.issuer_id = issuer_id;
    this.num_unit = num_unit;
    this.source_id = source_id;
    this.destination_id = destination_id;
  }
  public PlayerInfo getIssuerId() { return this.issuer_id; }
  public int getNumUnit() { return this.num_unit; }
  public int getSourceId() { return this.source_id; }
  public int getDestinationId() { return this.destination_id; }
  public ArrayList<Integer> getUnits() {
    return units;
  }
}
