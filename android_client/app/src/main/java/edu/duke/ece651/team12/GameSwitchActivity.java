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

import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.ResearchRequest;
import edu.duke.ece651.team12.util.Tools;

public class GameSwitchActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private Context context;
    private Button btn_switch_info;
    private Dialog dialog;
    private Tools tools = new Tools();
    public static final String TAG = "GameSwitchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_switch);
        dialog = new Dialog(this);

        context = this;
        initView();
    }

    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        btn_switch_info = findViewById(R.id.btn_switch_info);
        btn_switch_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    ClientApp.client.switchGame();
                    startActivity(new Intent(context, GameStartActivity.class));
                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };
                        tools.setInfoDialog(dialog, "Switch Info", "You have switched from the previous game", f);
                    });
                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            dialog.dismiss();
                            return null;
                        };
                        tools.setInfoDialog(dialog, "Switch Error", e.getMessage(), f);
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameHomepageActivity.class));
                break;
            case R.id.item_attack:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(GameSwitchActivity.this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(GameSwitchActivity.this, GameOperationActivity.class);
                bundle = new Bundle();
                bundle.putString("option", "Move");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_upgrade:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameUpgradeActivity.class));
                break;
            case R.id.item_research:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameResearchActivity.class));
                break;
            case R.id.item_switch:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_commit:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameCommitActivity.class));
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