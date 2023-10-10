package edu.duke.ece651.team12.shared;

import java.util.ArrayList;
import java.util.Random;

public class V2MapFactory implements AbstractMapFactory {
  private ArrayList<PlayerInfo> players;
  private int num_p;
  private int t_per_p;

  public V2MapFactory(ArrayList<PlayerInfo> players) {
    this.players = players;
    num_p = players.size();
    t_per_p = 24 / num_p;
  }
  public ArrayList<InitResponse> createInitResponses() {
    ArrayList<InitResponse> ins = new ArrayList<InitResponse>();

    for (int i = 0; i < 12; i++) {
      Random ran = new Random();
      int f = ran.nextInt(5);
      int t = ran.nextInt(5);
      int f_n = 5 - f;
      int t_n = 5 - t;
      ins.add(new InitResponseWP(2 * i,
                                 players.get(2 * i / (24 / num_p)),
                                 0,
                                 genNames().get(2 * i),
                                 genAdj(2 * i),
                                 f,
                                 t));
      ins.add(new InitResponseWP(2 * i + 1,
                                 players.get(2 * i / (24 / num_p)),
                                 0,
                                 genNames().get(2 * i + 1),
                                 genAdj(2 * i + 1),
                                 f_n,
                                 t_n));
    }
    return ins;
  }

  private static ArrayList<String> genNames() {
    ArrayList<String> res = new ArrayList<>();
    res.add("Lindon");
    res.add("Annuminas");
    res.add("The Shire");
    res.add("Amon Sul");
    res.add("Imladris");
    res.add("Angmar");
    res.add("Minhiriath");
    res.add("Enedwaith");
    res.add("Gundabad");
    res.add("Khazad-dûm");
    res.add("Gladden Fields");
    res.add("Mirkwood");
    res.add("Dol Guldur");
    res.add("Erebor");
    res.add("Lothlorien");
    res.add("Fangorn Forest");
    res.add("Dunland");
    res.add("Rohan");
    res.add("Gondor");
    res.add("Dagorlad");
    res.add("Ithilien");
    res.add("Barad-dur");
    res.add("Nurnen");
    res.add("Harad");
    return res;
  }

  public static ArrayList<Integer> genAdj(int i) {
    ArrayList<Integer> res = new ArrayList<>();
    i++;
    switch (i) {
      case 1:
        res.add(2);
        res.add(3);
        break;
      case 2:
        res.add(1);
        res.add(3);
        res.add(4);
        break;
      case 3:
        res.add(1);
        res.add(2);
        res.add(4);
        res.add(7);
        break;
      case 4:
        res.add(2);
        res.add(3);
        res.add(5);
        res.add(6);
        break;
      case 5:
        res.add(4);
        res.add(6);
        res.add(9);
        break;
      case 6:
        res.add(4);
        res.add(5);
        break;
      case 7:
        res.add(3);
        res.add(8);
        break;
      case 8:
        res.add(7);
        res.add(17);
        break;
      case 9:
        res.add(5);
        res.add(10);
        res.add(11);
        break;
      case 10:
        res.add(9);
        res.add(15);
        res.add(16);
        res.add(17);
        break;
      case 11:
        res.add(9);
        res.add(12);
        res.add(13);
        res.add(15);
        break;
      case 12:
        res.add(11);
        res.add(13);
        res.add(14);
        break;
      case 13:
        res.add(11);
        res.add(12);
        res.add(20);
        break;
      case 14:
        res.add(12);
        res.add(20);
        break;
      case 15:
        res.add(10);
        res.add(16);
        res.add(11);
        break;
      case 16:
        res.add(10);
        res.add(15);
        res.add(18);
        break;
      case 17:
        res.add(8);
        res.add(10);
        res.add(19);
        break;
      case 18:
        res.add(16);
        res.add(19);
        res.add(20);
        break;
      case 19:
        res.add(17);
        res.add(18);
        res.add(21);
        res.add(24);
        break;
      case 20:
        res.add(13);
        res.add(14);
        res.add(18);
        res.add(22);
        break;
      case 21:
        res.add(19);
        res.add(22);
        res.add(24);
        break;
      case 22:
        res.add(20);
        res.add(21);
        res.add(23);
        break;
      case 23:
        res.add(22);
        res.add(24);
        break;

      case 24:
        res.add(19);
        res.add(21);
        res.add(23);
        break;
    }
    ArrayList<Integer> ans = new ArrayList<Integer>();
    for (int k = 0; k < res.size(); k++) {
      Integer x = res.get(k);
      ans.add(x - 1);
    }
    return ans;
  }

  private static void fill(int[][] array, int i, int j, int d) {
    array[i - 1][j - 1] = d;
    array[j - 1][i - 1] = d;
  }
  public static int[][] genDistance() {
    int[][] res = new int[24][24];
    for (int i = 1; i < 25; i++) {
      for (int j = 1; j < 25; j++) {
        fill(res, i, j, Integer.MAX_VALUE);
      }
      fill(res, i, i, 0);
    }

    fill(res, 1, 2, 3);
    fill(res, 1, 3, 3);
    fill(res, 2, 3, 1);
    fill(res, 2, 4, 1);
    fill(res, 3, 4, 1);
    fill(res, 3, 7, 2);
    fill(res, 4, 5, 2);
    fill(res, 4, 6, 1);
    fill(res, 5, 6, 2);
    fill(res, 5, 9, 5);
    fill(res, 7, 8, 2);
    fill(res, 8, 17, 2);
    fill(res, 9, 10, 5);
    fill(res, 9, 11, 5);
    fill(res, 10, 15, 5);
    fill(res, 10, 16, 5);
    fill(res, 10, 17, 5);
    fill(res, 11, 12, 1);
    fill(res, 11, 13, 1);
    fill(res, 11, 15, 1);
    fill(res, 12, 13, 2);
    fill(res, 12, 14, 2);
    fill(res, 13, 20, 1);
    fill(res, 14, 20, 3);
    fill(res, 15, 16, 1);
    fill(res, 16, 18, 1);
    fill(res, 17, 19, 3);
    fill(res, 18, 19, 3);
    fill(res, 18, 20, 2);
    fill(res, 19, 21, 2);
    fill(res, 19, 24, 3);
    fill(res, 20, 22, 3);
    fill(res, 21, 22, 3);
    fill(res, 21, 24, 3);
    fill(res, 22, 23, 1);
    fill(res, 23, 24, 3);
    return res;
  }
}
