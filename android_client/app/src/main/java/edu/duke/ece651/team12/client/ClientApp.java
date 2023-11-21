package edu.duke.ece651.team12.client;

import android.app.Application;

import java.util.ArrayList;

import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Request;

public class ClientApp extends Application {
    public static V3Client client = new V3Client("vcm-32083.vm.duke.edu", 12345, null, System.out);
    public static PlayerMap map;
    public static PlayerInfo player_info;
    public static ArrayList<PlayerInfo> enemies;
    public static ArrayList<Request> requests = new ArrayList<>();
}
