package edu.duke.ece651.team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.util.Tools;

public class GameEntryActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Button btn_simple_user_options;
    private NavigationView simple_options_nav_view;
    private DrawerLayout simple_user_drawer_layout;
    private TextView tv_user_title;
    private Button btn_game_logout;
    private Button btn_game_entry;
    private Context context;
    private Dialog dialog;
    public static final String TAG = "GameEntryActivity";
    private Tools tools = new Tools();
    private boolean enabler = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_entry);
        context = this;
        dialog = new Dialog(this);

        initView();
    }

    private void initView() {
        btn_simple_user_options = findViewById(R.id.btn_simple_user_options);
        btn_simple_user_options.setOnClickListener(this);
        simple_user_drawer_layout = findViewById(R.id.simple_user_drawer_layout);
        simple_options_nav_view = findViewById(R.id.simple_options_nav_view);
        simple_options_nav_view.setNavigationItemSelectedListener(this);
        tv_user_title = findViewById(R.id.tv_user_title);
        tv_user_title.setText("New Game Entry");
        btn_game_logout = findViewById(R.id.btn_game_logout);
        btn_game_logout.setOnClickListener(this);
        btn_game_entry = findViewById(R.id.btn_game_entry);
        btn_game_entry.setOnClickListener(this);
    }

    private void logoutGame() {
        Intent intent = tools.handleLogout(context);
        startActivity(intent);
        finish();
    }

    private void initializeResources() {
        if(enabler) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            new Tools().setInfoDialog(dialog, "Game Entry Error", "You have entered this game, please wait.", f);
            return;
        }

        enabler = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientApp.client.requ(ClientApp.requests);
                    runOnUiThread(() -> {
                                tools.setWaitingDialog(dialog, "Loading game...", "Waiting for other players to enter the game");
                    });
                    ClientApp.map.update(ClientApp.client.resp());
                    ClientApp.requests = new ArrayList<>();
                    startActivity(new Intent(context, GameHomepageActivity.class));
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };

                        tools.setInfoDialog(dialog, "Game Entry Error", e.getMessage(), f);
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_simple_user_options:
                simple_user_drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_game_logout:
                logoutGame();
                break;
            case R.id.btn_game_entry:
                initializeResources();
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                tools.closeDrawerLayout(simple_user_drawer_layout);
                break;
            case R.id.item_logout:
                logoutGame();
        }

        return false;
    }
}