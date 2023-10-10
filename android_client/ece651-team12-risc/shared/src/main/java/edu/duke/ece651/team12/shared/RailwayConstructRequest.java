package edu.duke.ece651.team12.shared;

public class RailwayConstructRequest extends Request {
  private static final long serialVersionUID = 19202;
  public RailwayConstructRequest(PlayerInfo issuer_id,
                                 int source_id,
                                 int destination_id) {
    super(issuer_id, 0, source_id, destination_id);
  }
}
