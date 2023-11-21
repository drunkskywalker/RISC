package edu.duke.ece651.team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.function.Function;

import edu.duke.ece651.team12.client.*;
import edu.duke.ece651.team12.shared.*;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.ResearchDuplicationChecker;
import edu.duke.ece651.team12.shared.ResearchRequest;
import edu.duke.ece651.team12.shared.RuleChecker;
import edu.duke.ece651.team12.util.Tools;

public class GameResearchActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private Button btn_research_info;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_research);
        dialog = new Dialog(this);

        initView();
    }


    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        btn_research_info = findViewById(R.id.btn_research_info);
        btn_research_info.setOnClickListener(this);
    }

    private void handleResearchRequest() {
        try{
            Request r = new ResearchRequest(ClientApp.player_info);
            RuleChecker res_rule_checker = new ResearchDuplicationChecker();
            res_rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Research success", "You made research successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Research Error", e.getMessage(), f);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_options:
                user_drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_research_info:
                handleResearchRequest();
        }

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
                Intent intent = new Intent(this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(this, GameOperationActivity.class);
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
                break;
            case R.id.item_switch:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameSwitchActivity.class));
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