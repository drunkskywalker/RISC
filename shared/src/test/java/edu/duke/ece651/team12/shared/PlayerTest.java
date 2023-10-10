package edu.duke.ece651.team12.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class PlayerTest {
  //  @Disabled
  @Test
  public void test() throws IOException {
    PlayerInfo p1 = new PlayerInfo(0, "Gandalf", new Color(255, 0, 0));
    PlayerInfo p2 = new PlayerInfo(1, "Master Yoda", new Color(0, 255, 0));
    PlayerInfo p3 = new PlayerInfo(2, "Kaiser Wilhelm II", new Color(0, 0, 255));
    ArrayList<PlayerInfo> enemies = new ArrayList<PlayerInfo>();

    enemies.add(p2);
    enemies.add(p3);
    Map map = new PlayerMap("Middle Earth");
    ArrayList<Integer> i0 = new ArrayList<Integer>();
    i0.add(1);
    i0.add(2);
    ArrayList<Integer> i1 = new ArrayList<Integer>();
    i1.add(0);
    i1.add(2);
    ArrayList<Integer> i2 = new ArrayList<Integer>();
    i2.add(0);
    i2.add(1);
    map.init(new InitResponse(0, p1, 12, "Gondor", i0));
    map.init(new InitResponse(1, p2, 12, "Dagobah", i1));
    map.init(new InitResponse(2, p3, 12, "Wilhelmshaven", i2));
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);

    StringReader sr = new StringReader(
        "Gandalf\nST\n1\nST\n3\na\n0\n1\n1\n0\n0\n0\n0\n0\n0\n0\nb\nc\nd\nA\n0\n1\n1\nR\nC\nD\nU\n0\n1\n0\n0\n0\n0\n0\nC\n");
    BufferedReader isr = new BufferedReader(sr);
    Player p = new Player(p1, enemies, map, tmd, isr, System.out);
    p.turn();
    ArrayList<TurnResponse> tr1 = new ArrayList<TurnResponse>();
    tr1.add(new TurnResponse(0, p2, 12));
    tr1.add(new TurnResponse(1, p1, 12));
    tr1.add(new TurnResponse(2, p2, 12));
    assertTrue(!p.endTurn(tr1));

    tr1.remove(0);
    tr1.add(new TurnResponse(0, p1, 12));

    p.endTurn(tr1);
    p.turn();
    tr1.add(new TurnResponse(2, p1, 12));
    p.endTurn(tr1);

    assertTrue(p.checkLose(p2));
    assertTrue(!p.checkLose(p1));
    //    assertTrue(false);
  }
  //@Disabled
  @Test
  public void test_full() throws IOException {
    PlayerInfo p1 = new PlayerInfo(0, "Gandalf", new Color(255, 0, 0));
    PlayerInfo p2 = new PlayerInfo(1, "Master Yoda", new Color(0, 255, 0));
    ArrayList<PlayerInfo> enemies = new ArrayList<PlayerInfo>();
    ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();

    enemies.add(p2);
    players.add(p1);
    players.add(p2);
    Map map = new PlayerMap("Middle Earth");
    BasicMapFactory bmf = new BasicMapFactory(players, 2, 6);

    StringReader sr = new StringReader(
        "a\n12\n10\nD\nM\n0\n1\n2\nM\n0\n1\n20\nD\nA\n7\n0\n0\nA\n1\n3\n1\nA\n1\n4\n1\nA\n1\n5\n1\nC\n");
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);

    BufferedReader isr = new BufferedReader(sr);
    Player p = new Player(p1, enemies, map, tmd, isr, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses(6);
    p.initializeMap(irs);
    p.initUnit(10);
    ArrayList<TurnResponse> trs = new ArrayList<>();
    trs.add(new TurnResponse(1, p1, 100));
    map.update(trs);
    p.turn();
    //assertTrue(false);
    trs.add(new TurnResponse(3, p1, 1));
    trs.add(new TurnResponse(4, p1, 1));
    trs.add(new TurnResponse(5, p1, 1));
    p.endTurn(trs);
    //  assertTrue(false);
  }
  //@Disabled
  @Test
  public void test_full2() throws IOException {
    PlayerInfo p1 = new PlayerInfo(0, "Gandalf", new Color(255, 0, 0));
    PlayerInfo p2 = new PlayerInfo(1, "Master Yoda", new Color(0, 255, 0));
    ArrayList<PlayerInfo> enemies = new ArrayList<PlayerInfo>();
    ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();

    enemies.add(p2);
    players.add(p1);
    players.add(p2);
    Map map = new PlayerMap("Middle Earth");
    BasicMapFactory bmf = new BasicMapFactory(players, 2, 6);

    StringReader sr = new StringReader(
        "a\n12\n0\n1\n0\n0\n0\n0\n0\nD\nM\n20\n23432\n0\nM\n0\n1\n2\nM\n0\n1\n20\nD\nA\n7\n0\n0\nA\n1\n3\n1\nA\n1\n4\n1\nA\n1\n5\n1\nM\n1\n0\n1\nC\nC\n");
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);

    BufferedReader isr = new BufferedReader(sr);
    Player p = new Player(p1, enemies, map, tmd, isr, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses(6);
    p.initializeMap(irs);
    p.initUnit(10);
    ArrayList<TurnResponse> trs = new ArrayList<>();
    trs.add(new TurnResponse(1, p1, 100));
    map.update(trs);
    p.turn();
    trs.add(new TurnResponse(3, p1, 1));
    trs.add(new TurnResponse(4, p1, 1));
    trs.add(new TurnResponse(5, p1, 1));
    p.endTurn(trs);
    //   assertTrue(false);
  }
  @Test
  public void test_lose() throws IOException {
    PlayerInfo p1 = new PlayerInfo(0, "Gandalf", new Color(255, 0, 0));
    PlayerInfo p2 = new PlayerInfo(1, "Master Yoda", new Color(0, 255, 0));
    ArrayList<PlayerInfo> enemies = new ArrayList<PlayerInfo>();
    ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();

    enemies.add(p2);
    players.add(p1);
    players.add(p2);
    Map map = new PlayerMap("Middle Earth");
    BasicMapFactory bmf = new BasicMapFactory(players, 2, 6);

    StringReader sr = new StringReader(
        "ST\n1\nST\n3\na\n12\n10\nD\nM\n0\n1\n2\nM\n0\n1\n20\nD\nA\n7\n0\n0\nA\n1\n3\n1\n0\n0\n0\n0\n0\n0\n0\nA\n1\n4\n1\nA\n1\n5\n1\n0\n0\n0\n0\n0\n0\n0\nR\nST\n4\nST\n2\nC\n");
    TextMapDisplayer tmd = new TextMapDisplayer(map, p1);

    BufferedReader isr = new BufferedReader(sr);
    Player p = new Player(p2, enemies, map, tmd, isr, System.out);
    ArrayList<InitResponse> irs = bmf.createInitResponses(6);

    p.initializeMap(irs);
    ArrayList<TurnResponse> trs = new ArrayList<>();
    trs.add(new TurnResponse(1, p1, TerritoryWP.genUnit(100), 1000, 1000, 5));
    map.update(trs);
    //assertTrue(false);
    trs.add(new TurnResponse(3, p1, 1));
    trs.add(new TurnResponse(4, p1, 1));
    trs.add(new TurnResponse(5, p1, 1));

    p.endTurn(trs);
    p.turn();
  }
}
