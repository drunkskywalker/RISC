package edu.duke.ece651.team12.shared;

import java.util.ArrayList;

public class AttackRequest extends Request {
  private static final long serialVersionUID = 400;
  public AttackRequest(PlayerInfo issuer_id,
                       int num_unit,
                       int source_id,
                       int destination_id,
                       ArrayList<Integer> units) {
    super(issuer_id, num_unit, source_id, destination_id);
    this.units = units;
  }

  public AttackRequest(PlayerInfo issuer_id,
                       int num_unit,
                       int source_id,
                       int destination_id) {
    this(issuer_id, num_unit, source_id, destination_id, TerritoryWP.genUnit(num_unit));
  }

  public AttackRequest(PlayerInfo issuer_id,
                       int source_id,
                       int destination_id,
                       ArrayList<Integer> units) {
    this(issuer_id, TerritoryWP.genCount(units), source_id, destination_id, units);
  }
}
