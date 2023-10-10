package edu.duke.ece651.team12.shared;

public class ResponseHandler {
  public ResponseHandler() {}
  public Territory handleInitResponse(InitResponse response) {
    return new TerritoryWP(response.getTerritoryId(),
                           response.getName(),

                           response.getNumUnit(),
                           response.getNeighbors(),
                           response.getPlayerInfo(),
                           response.food_prod,
                           response.tech_prod);
  }
  /*
  public void handleTurnResponse(TerritoryWP t, TurnResponseWP response) {
    t.setPlayerInfo(response.player_info);

    t.setUnits(response.getUnits());
    t.setNumUnit(response.getNumUnit());
    }*/

  public void verifyStorage(Map map, TurnResponse response, PlayerInfo p) {
    map.food_accus.put(p, response.food_accu);
    map.tech_accus.put(p, response.tech_accu);
    map.tech_lvls.put(p, response.tech_lvl);
    map.research_barrier.put(p, false);
  }
  public void handleTurnResponse(Territory t, TurnResponse response) {
    t.setPlayerInfo(response.player_info);
    t.setNumUnit(response.num_unit);
    t.setUnits(response.units);
  }
}
