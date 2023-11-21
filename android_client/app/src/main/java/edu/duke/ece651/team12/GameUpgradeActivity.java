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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.PlayerMap;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.ResearchDuplicationChecker;
import edu.duke.ece651.team12.shared.RuleChecker;
import edu.duke.ece651.team12.shared.Territory;
import edu.duke.ece651.team12.shared.UpgradeCostChecker;
import edu.duke.ece651.team12.shared.UpgradeOwnershipChecker;
import edu.duke.ece651.team12.shared.UpgradeRequest;
import edu.duke.ece651.team12.util.Tools;

public class GameUpgradeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private Button btn_upgrade_info;
    public static final String TAG = "GameUpgradeActivity";
    private Tools tools = new Tools();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_upgrade);
        dialog = new Dialog(this);

        initView();
    }

    
    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        btn_upgrade_info = findViewById(R.id.btn_upgrade_info);
        btn_upgrade_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_user_options) {
            user_drawer_layout.openDrawer(GravityCompat.START);
            return;
        }

        EditText item_upgrade_0 = findViewById(R.id.item_upgrade_0);
        EditText item_upgrade_1 = findViewById(R.id.item_upgrade_1);
        EditText item_upgrade_2 = findViewById(R.id.item_upgrade_2);
        EditText item_upgrade_3 = findViewById(R.id.item_upgrade_3);
        EditText item_upgrade_4 = findViewById(R.id.item_upgrade_4);
        EditText item_upgrade_5 = findViewById(R.id.item_upgrade_5);
        EditText et_territory_id_upgrade = findViewById(R.id.et_territory_id_upgrade);
        List<EditText> items_upgrade = new ArrayList<>();
        items_upgrade.add(item_upgrade_0);
        items_upgrade.add(item_upgrade_1);
        items_upgrade.add(item_upgrade_2);
        items_upgrade.add(item_upgrade_3);
        items_upgrade.add(item_upgrade_4);
        items_upgrade.add(item_upgrade_5);

        ArrayList<Integer> lv_upgrade_list = tools.getUnitsList(items_upgrade, dialog, "Upgrade Error");

        if(lv_upgrade_list == null) {
            return;
        }

        if(et_territory_id_upgrade.length() == 0) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            tools.setInfoDialog(dialog, "Upgrade Error", "Please enter the territory ID", f);
            return;
        }

        int upgrade_territory_id = Integer.parseInt(et_territory_id_upgrade.getText().toString());

        try {
            Request r = Tools.genUpgrade(upgrade_territory_id - 1, lv_upgrade_list);

            if(Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do not win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);
                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                tools.setInfoDialog(dialog, "Upgrade Info", "You made an upgrade successfully.", f);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();

            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            tools.setInfoDialog(dialog, "Upgrade Error", e.getMessage(), f);
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
                Intent intent = new Intent(GameUpgradeActivity.this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(GameUpgradeActivity.this, GameOperationActivity.class);
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