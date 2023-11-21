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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.AbstractMap;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.client.V3Client;
import edu.duke.ece651.team12.shared.*;
import edu.duke.ece651.team12.util.Tools;

public class GameNewActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private EditText et_num_player;
    private Button btn_game_create;
    private Context context;
    private Button btn_simple_user_options;
    private NavigationView simple_options_nav_view;
    private DrawerLayout simple_user_drawer_layout;
    private TextView tv_user_title;
    public static final String TAG = "GameNewActivity";
    private Dialog dialog;
    private boolean enabler = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_new);
        context = this;
        dialog = new Dialog(this);

        initView();
    }

    private void initView() {
        et_num_player = findViewById(R.id.et_num_player);
        btn_game_create = findViewById(R.id.btn_game_create);
        btn_game_create.setOnClickListener(this);
        btn_simple_user_options = findViewById(R.id.btn_simple_user_options);
        btn_simple_user_options.setOnClickListener(this);
        simple_user_drawer_layout = findViewById(R.id.simple_user_drawer_layout);
        simple_options_nav_view = findViewById(R.id.simple_options_nav_view);
        simple_options_nav_view.setNavigationItemSelectedListener(this);
        tv_user_title = findViewById(R.id.tv_user_title);
        tv_user_title.setText("Start New Game");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_simple_user_options) {
            simple_user_drawer_layout.openDrawer(GravityCompat.START);
            return;
        }

        if(enabler) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            new Tools().setInfoDialog(dialog, "New Game Error", "You have created this game, please wait.", f);
            return;
        }

        try{
            if(et_num_player.length() == 0) {
                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                new Tools().setInfoDialog(dialog, "New Game Error", "Please fill in the field", f);
                return;
            }

            int num_player = Integer.parseInt(et_num_player.getText().toString());

            if(num_player < 2) {
                String dialog_body = "The number should be at least 2.";

                Function<Dialog, Void> f = (dialog) -> {
                    et_num_player.getText().clear();
                    dialog.dismiss();
                    return null;
                };
                new Tools().setInfoDialog(dialog, "New Game Error", dialog_body, f);
                return;
            }

            V3Client client = ClientApp.client;
            enabler = true;

            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        client.availableGames();
                        AbstractMap.SimpleEntry<Integer, String> newGame = client.newGame(num_player);
                        int newGame_key = newGame.getKey();
                        String newGame_desc = newGame.getValue();

                        Log.d(TAG, "New Key: " + newGame_key + ", " + "New Desc: " + newGame_desc);

                        runOnUiThread(() -> {
                            new Tools().setWaitingDialog(dialog, "Initializing...", "Other players are joining the game.");
                        });

                        int init_res = client.initGame();

                        startActivity(new Intent(context, GameUnitInitActivity.class));
                        runOnUiThread(() -> {
                            Toast.makeText(context, "New game has been created", Toast.LENGTH_LONG).show();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();

                        runOnUiThread(() -> {
                            Function<Dialog, Void> f = (dialog) -> {
                                dialog.dismiss();
                                return null;
                            };
                            new Tools().setInfoDialog(dialog, "Initialization Error", e.getMessage(), f);
                        });
                    }
                }
            }).start();
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                et_num_player.getText().clear();
                dialog.dismiss();
                return null;
            };
            new Tools().setInfoDialog(dialog, "New Game Error", e.getMessage(), f);
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
                intent.setClass(GameNewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
        return false;
    }
}