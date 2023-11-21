package edu.duke.ece651.team12;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import edu.duke.ece651.team12.adapter.GridViewAdapter;
import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Territory;
import edu.duke.ece651.team12.shared.TurnResponse;
import edu.duke.ece651.team12.util.Tools;

public class GameUnitInitActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private GridView gridview_unit_init;
    private Button btn_game_new;
    private LinkedHashMap<Integer, Integer> territory_unit_map;
    private NavigationView simple_options_nav_view;
    private DrawerLayout simple_user_drawer_layout;
    private Context context;
    private Dialog dialog;
    public static final String TAG = "GameUnitInitActivity";
    private boolean enabler = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_unit_init);
        dialog = new Dialog(this);
        context = this;
        showLeftDrawerInfo();

        initView();
    }

    private void initView() {
        PlayerInfo playerInfo = ClientApp.player_info;
        System.out.println(playerInfo.player_name);
        PlayerMap map = ClientApp.map;
        System.out.println(map.name);
        ArrayList<Territory> territories = map.territories;
        System.out.println("UnitInit========================");
        System.out.println("territories: " + territories.size());
        territory_unit_map = new LinkedHashMap<>();
        List<String> myTerritories = new ArrayList<>();
        for (Territory t: territories) {
            if (t.getPlayerInfo().equals(playerInfo)){
                myTerritories.add(t.getId()+"");
                territory_unit_map.put(t.getId(), 0);
            }
        }
        System.out.println("UnitInit========================");
        System.out.println(myTerritories.size());
        System.out.println(myTerritories.get(0));
        gridview_unit_init = findViewById(R.id.gridview_unit_init);
        gridview_unit_init.setAdapter(new GridViewAdapter(myTerritories,GameUnitInitActivity.this));
        btn_game_new = findViewById(R.id.btn_game_new);
        btn_game_new.setOnClickListener(this);
        simple_user_drawer_layout = findViewById(R.id.simple_user_drawer_layout);
        simple_options_nav_view = findViewById(R.id.simple_options_nav_view);
        simple_options_nav_view.setNavigationItemSelectedListener(this);
    }

    private void showLeftDrawerInfo() {
        Function<Dialog, Void> f = (dialog) -> {
            dialog.dismiss();
            return null;
        };

        new Tools().setInfoDialog(dialog, "Game Tip", "You can swipe from the left side of the screen to open the hidden menu.", f);
    }

    private void clearEditTexts() {
        final int size = gridview_unit_init.getChildCount();

        for(int i = 0; i < size; i++) {
            EditText et_territory_unit = (EditText)gridview_unit_init.getChildAt(i);
            et_territory_unit.getText().clear();
        }
    }


    // Create territory arraylist in map. iterate through the list, find
    // territory whose playerinfo equals player_info. ask for player to
    // place maximum 20 units onto those territories.
    // Generate as many TurnResponse s as Territories owned by the player.
    // use client to send back to the server. switch to game operation activity.
    @Override
    public void onClick(View view) {
        if(enabler) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            new Tools().setInfoDialog(dialog, "Unit Init Error", "You have initialized units, please wait.", f);
            return;
        }

        final int size = gridview_unit_init.getChildCount();
        int sum = 0;
        int total_units = 20;
        Iterator iter = territory_unit_map.entrySet().iterator();

        for(int i = 0; i < size; i++) {
            EditText et_territory_unit = (EditText)gridview_unit_init.getChildAt(i);

            if(et_territory_unit.length() == 0) {
                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                new Tools().setInfoDialog(dialog, "Units Init Error", "Please fill in all the fields.", f);
                return;
            }

            try{
                int curr_unit = Integer.parseInt(et_territory_unit.getText().toString());
                sum += curr_unit;
                Map.Entry entry = (Map.Entry) iter.next();
                entry.setValue(curr_unit);
                if(curr_unit < 0) {
                    Function<Dialog, Void> f = (dialog) -> {
                        clearEditTexts();
                        dialog.dismiss();
                        return null;
                    };
                    new Tools().setInfoDialog(dialog, "Units Init Error", "Units must be positive.", f);
                    return;
                }
            }
            catch (Exception e) {
                Function<Dialog, Void> f = (dialog) -> {
                    clearEditTexts();
                    dialog.dismiss();
                    return null;
                };
                new Tools().setInfoDialog(dialog, "Units Init Error", e.getMessage(), f);
                return;
            }
        }
        Log.d(TAG,"Total units placed: " + sum+ ", should be " + total_units);

        if(sum == total_units) {
            enabler = true;
            ArrayList<TurnResponse> trs = new ArrayList<TurnResponse>();
            for (Integer i: territory_unit_map.keySet()) {
                trs.add(new TurnResponse(i, ClientApp.player_info, territory_unit_map.get(i)));
            }
            Log.d(TAG, "turn responses collected.");

            new Tools().setWaitingDialog(dialog, "Receiving data...", "Other players are entering territory information.");

            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        ClientApp.client.initMap(trs);
                        ClientApp.client.restoreMap();
                        startActivity(new Intent(context, GameEntryActivity.class));
                    }catch(Exception e){
                        Log.d(TAG, e.getMessage());
                        e.printStackTrace();

                        runOnUiThread(() -> {
                            Function<Dialog, Void> f = (dialog) -> {
                                dialog.dismiss();
                                clearEditTexts();
                                return null;
                            };
                            new Tools().setInfoDialog(dialog, "Units Init Error", e.getMessage(), f);
                        });
                    }
                }
            }).start();
        }
        else {
            Log.d(TAG, "Units are not 20 in total");

            runOnUiThread(() -> {
                Function<Dialog, Void> f = (dialog) -> {
                    clearEditTexts();
                    dialog.dismiss();
                    return null;
                };
                new Tools().setInfoDialog(dialog, "Units Number Error", "The units you placed are not 20 in total (・ω<)", f);
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                simple_user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameStartActivity.class));
                break;
            case R.id.item_logout:
                Intent intent = new Intent();
                intent.setClass(GameUnitInitActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
        return false;
    }
}