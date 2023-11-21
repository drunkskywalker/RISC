package edu.duke.ece651.team12.bean;

import android.widget.Button;

public class OperationTerritoryInfo {
    private boolean isStart;
    private int territory_id;
    private Button btn_territory;

    public OperationTerritoryInfo(boolean isStart, int territory_id, Button btn_territory) {
        this.isStart = isStart;
        this.territory_id = territory_id;
        this.btn_territory = btn_territory;
    }

    public boolean isStart() {
        return isStart;
    }

    public int getTerritoryID() {
        return territory_id;
    }

    public Button getBtnTerritory() {
        return btn_territory;
    }
}
