package edu.duke.ece651.team12.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameList {
    private List<GameItem> list = new ArrayList<GameItem>();

    public GameList(HashMap<Integer, String> availableGames) {
        for(Object key : availableGames.keySet()) {
            list.add(new GameItem(key.toString(), availableGames.get(key)));
        }
    }

    public List<GameItem> geGametList() {
        return list;
    }

    public int geGametListCount() {
        return list.size();
    }

    public GameItem getGameItem(int i) {
        return list.get(i);
    }
}
