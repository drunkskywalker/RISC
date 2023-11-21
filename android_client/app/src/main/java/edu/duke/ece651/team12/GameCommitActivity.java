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
import edu.duke.ece651.team12.shared.Player;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.Spy;
import edu.duke.ece651.team12.shared.Territory;
import edu.duke.ece651.team12.util.Tools;

public class GameCommitActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private Context context;
    private Button btn_commit_info;
    private Tools tools = new Tools();
    private Dialog dialog;
    private boolean enabler = false;
    public static final String TAG = "GameCommitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_commit);
        context = this;
        dialog = new Dialog(this);

        initView();
    }

    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        btn_commit_info = findViewById(R.id.btn_commit_info);
        btn_commit_info.setOnClickListener(this);
    }

    // if you got here with commit, do the following:
    // ClientApp.client.requ(requests);
    // go to waiting activity, do the following:
    // ClientMap.update(ClientApp.client.resp());
    //if map.checkEnd() is true: go to game end => then game selection activity.
    //if not, go to game mainpage. empty the requests arraylist by creating a new arraylist.
    // if map.checkLose(playerInfo) is true: don't let let user do operation. directly call ClientApp.client.requ(requests); and go to waiting activity.
    @Override
    public void onClick(View view) {
        if(enabler) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            tools.setInfoDialog(dialog, "Commit Error", "You have committed in the current round, please wait.", f);
            return;
        }

        enabler = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Tools.checkLose(ClientApp.player_info)) {
                        ClientApp.requests = new ArrayList<>();
                    }
                    ClientApp.client.requ(ClientApp.requests);

                    runOnUiThread(() -> {
                        tools.setWaitingDialog(dialog, "Commit Info", "Waiting for other players to commit...");
                    });

                    ClientApp.map.update(ClientApp.client.resp());
                    ClientApp.map.updateVision(ClientApp.player_info);
                    for (Territory t: ClientApp.map.territories) {
                        for (Spy s : t.spies) {
                            s.hasMoved =false;
                        }
                    }
                    ClientApp.requests = new ArrayList<>();
                    PlayerInfo result = Tools.checkEnd();
                    if (result != null) {
                        if(result.equals(ClientApp.player_info)) {
                            runOnUiThread(() -> {
                                Button btn_dialog_ok = dialog.findViewById(R.id.btn_dialog_ok);
                                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(context, GameStartActivity.class));
                                    }
                                });
                                Tools.setWinDialog(dialog);
                            });
                        }
                        else {
                            runOnUiThread(() -> {
                                Tools.setLoseDialog(dialog, "The winner is " + result.player_name);
                            });
                        }
                    }
                    else {
                        ClientApp.requests = new ArrayList<>();
                        runOnUiThread(() -> {
                            Function<Dialog, Void> f = (dialog) -> {
                                startActivity(new Intent(context, GameHomepageActivity.class));
                                dialog.dismiss();
                                return null;
                            };
                            tools.setInfoDialog(dialog, "Commit Info", "The game continues.\nClick OK to play one more round.", f);
                        });
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };
                        tools.setInfoDialog(dialog, "Commit Error", e.getMessage(), f);
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.item_home:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameHomepageActivity.class));
                break;
            case R.id.item_attack:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(GameCommitActivity.this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(GameCommitActivity.this, GameOperationActivity.class);
                bundle = new Bundle();
                bundle.putString("option", "Move");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_research:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameResearchActivity.class));
                break;
            case R.id.item_upgrade:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameUpgradeActivity.class));
                break;
            case R.id.item_switch:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameSwitchActivity.class));
                break;
            case R.id.item_commit:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_fog_of_war:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameFogWarActivity.class));
                break;
            case R.id.item_railway:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameRailwayActivity.class));
                break;
        }

        return false;
    }
}