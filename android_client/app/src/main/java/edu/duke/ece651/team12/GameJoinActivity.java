package edu.duke.ece651.team12;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.AbstractMap;
import java.util.function.Function;

import edu.duke.ece651.team12.adapter.GameAdapter;
import edu.duke.ece651.team12.bean.GameList;
import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.client.V3Client;
import edu.duke.ece651.team12.util.Tools;

public class GameJoinActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ListView lv_available_games;
    private GameList game_list;
    private V3Client client;
    private Context context;
    private Dialog dialog;
    private Tools tools = new Tools();
    public static final String TAG = "GameJoinActivity";
    private Button btn_simple_user_options;
    private NavigationView simple_options_nav_view;
    private DrawerLayout simple_user_drawer_layout;
    private TextView tv_user_title;
    private Button btn_view_games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_join);
        dialog = new Dialog(this);

        context = this;
        initView();
    }

    private void initView() {
        client = ClientApp.client;
        lv_available_games = findViewById(R.id.lv_available_games);
        btn_simple_user_options = findViewById(R.id.btn_simple_user_options);
        btn_simple_user_options.setOnClickListener(this);
        simple_user_drawer_layout = findViewById(R.id.simple_user_drawer_layout);
        simple_options_nav_view = findViewById(R.id.simple_options_nav_view);
        simple_options_nav_view.setNavigationItemSelectedListener(this);
        tv_user_title = findViewById(R.id.tv_user_title);
        tv_user_title.setText("Join Game");

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    game_list = new GameList(client.availableGames());
                    GameAdapter mAdapter = new GameAdapter( GameJoinActivity.this, game_list);
                    runOnUiThread(() -> {
                        lv_available_games.setAdapter(mAdapter);
                        lv_available_games.setOnItemClickListener(GameJoinActivity.this);
                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };
                        tools.setInfoDialog(dialog, "Join Error", e.getMessage(), f);
                    });
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String game_id = game_list.getGameItem(position).getGameSeqNumber(); // game_seq_number is game_id
        Log.d(TAG, "Game ID Clicked: " + game_id);


        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    try {
                        AbstractMap.SimpleEntry<Integer, String> result = client.joinGame(Integer.parseInt(game_id));
                        int response_num = result.getKey();
                        if(response_num != -1) {
                            break;
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        e.printStackTrace();

                        runOnUiThread(() -> {
                            Function<Dialog, Void> f = (dialog) -> {
                                dialog.dismiss();
                                return null;
                            };
                            tools.setInfoDialog(dialog, "Join Error", e.getMessage(), f);
                        });
                    }
                }
                try{
                    int init_res = client.initGame();
                    // switch to different games based on stated in V3Client
                    //res >=0: to initunit activity; res ==-2: to game operation activity.
                    //initUnit activiety: send client a ArrayList<TurnResponse> stating each owned territory's initial unit. Then to gameoperation activity
                    // operation activity: play game
                    if(init_res >= 0) {
                        // switch to unit initialization screen
                        // territory arraylist in map. iterate through the list, find
                        // territory whose playerinfo equals player_info. ask for player to
                        // place maximum 20 units onto those territories.
                        // Generate as many TurnResponses as Territories owned by the player.
                        // use client to send back to the server. switch to game operation activity.
                        startActivity(new Intent(context, GameUnitInitActivity.class));
                    }
                    else if (init_res == -2) {
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try{
                                    ClientApp.client.restoreMap();
                                    startActivity(new Intent(context, GameHomepageActivity.class));
                                }catch(Exception e){
                                    Log.d(TAG, e.getMessage());
                                    e.printStackTrace();

                                    runOnUiThread(() -> {
                                        Function<Dialog, Void> f = (dialog) -> {
                                            dialog.dismiss();
                                            return null;
                                        };
                                        tools.setInfoDialog(dialog, "Join Error", e.getMessage(), f);
                                    });
                                }
                            }
                        }).start();

                    }

                }catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };
                        tools.setInfoDialog(dialog, "Join Error", e.getMessage(), f);
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_simple_user_options) {
            simple_user_drawer_layout.openDrawer(GravityCompat.START);
            return;
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
                intent.setClass(GameJoinActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
        return false;
    }
}