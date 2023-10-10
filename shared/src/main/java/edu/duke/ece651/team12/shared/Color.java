package edu.duke.ece651.team12.shared;

import java.io.Serializable;

public class Color implements Serializable {
  public final int R;
  public final int G;
  public final int B;
  private static final long serialVersionUID = 18;
  public Color(int R, int G, int B) {
    this.R = R;
    this.G = G;
    this.B = B;
  }
  public Color() {
    R = 0;
    G = 0;
    B = 0;
  }

  @Override
  public boolean equals(Object rhs) {
    if (rhs != null && rhs.getClass().equals(this.getClass())) {
      Color rhsC = (Color)rhs;
      return (this.R == rhsC.R && this.G == rhsC.G && this.B == rhsC.B);
    }
    return false;
  }

  @Override
  public String toString() {
    if (R == 255 && R + G + B == 255) {
      return "Red";
    }
    else if (G == 255 && R + G + B == 255) {
      return "Green";
    }
    else if (B == 255 && R + G + B == 255) {
      return "Blue";
    }

    else if (R == 255 && G == 255 && B == 0) {
      return "Yellow";
    }
    else if (R == 255 && B == 255 && G == 255) {
      return "White(debug only)";
    }
    String res = "#";
    if (R < 16) {
      res += "0";
    }
    res += Integer.toHexString(R);
    if (G < 16) {
      res += "0";
    }
    res += Integer.toHexString(G);
    if (B < 16) {
      res += "0";
    }
    res += Integer.toHexString(B);
    return res;
  }
}
