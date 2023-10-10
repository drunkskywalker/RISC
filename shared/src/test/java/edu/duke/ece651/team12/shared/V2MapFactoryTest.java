package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class V2MapFactoryTest {
  @Test
  public void test_() {
    for (int i = 0; i < 24; i++) {
      ArrayList<Integer> ax = V2MapFactory.genAdj(i);
      for (Integer x : ax) {
        ArrayList<Integer> ay = V2MapFactory.genAdj(x);
        if (ay.indexOf(i) == -1) {
          System.out.println("Error: " + x + " does not contain " + i);
        }
      }
    }

    int[][] arr = V2MapFactory.genDistance();
    for (int i = 0; i < 24; i++) {
      ArrayList<Integer> is = V2MapFactory.genAdj(i);
      for (Integer x : is) {
        if (arr[i][x] == Integer.MAX_VALUE) {
          System.out.println("Err: " + i + ", " + x);
        }
      }
      System.out.println(Arrays.toString(arr[i]));
      for (int j = 0; j < 24; j++) {
        if (arr[i][j] != arr[j][i]) {
          System.out.println("Err: " + i + ", " + j);
        }
      }
    }

    //assertTrue(false);
  }
}
