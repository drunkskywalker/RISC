package edu.duke.ece651.team12.bean;

public class GameItem {
    private String game_seq_number;
    private String num_player;

    public GameItem(String game_seq_number, String num_player) {
        this.game_seq_number = game_seq_number;
        this.num_player = num_player;
    }

    public String getGameSeqNumber() {
        return game_seq_number;
    }

    public void setGameSeqNumber(String game_seq_number) {
        this.game_seq_number = game_seq_number;
    }

    public String getNumPlayer() {
        return num_player;
    }

    public void setNumPlayer(String num_player) {
        this.num_player = num_player;
    }
}
