package edu.duke.ece651.team12.shared;

import java.io.Serializable;

public class Spy implements Serializable {
  private static final long serialVersionUID = 1112;

  public PlayerInfo owner;
  public boolean hasMoved = false;

  public Spy(PlayerInfo owner) { this.owner = owner; }
}
