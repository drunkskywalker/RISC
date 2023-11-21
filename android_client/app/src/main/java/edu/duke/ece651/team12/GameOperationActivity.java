package edu.duke.ece651.team12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import edu.duke.ece651.team12.bean.OperationTerritoryInfo;
import edu.duke.ece651.team12.client.*;
import edu.duke.ece651.team12.shared.*;
import edu.duke.ece651.team12.util.Tools;

public class GameOperationActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private String operation;
    private Button btn_attack_move_info;
    private Button btn_territory_1;
    private Button btn_territory_2;
    private Button btn_territory_3;
    private Button btn_territory_4;
    private Button btn_territory_5;
    private Button btn_territory_6;
    private Button btn_territory_7;
    private Button btn_territory_8;
    private Button btn_territory_9;
    private Button btn_territory_10;
    private Button btn_territory_11;
    private Button btn_territory_12;
    private Button btn_territory_13;
    private Button btn_territory_14;
    private Button btn_territory_15;
    private Button btn_territory_16;
    private Button btn_territory_17;
    private Button btn_territory_18;
    private Button btn_territory_19;
    private Button btn_territory_20;
    private Button btn_territory_21;
    private Button btn_territory_22;
    private Button btn_territory_23;
    private Button btn_territory_24;
    private Button btn_reset;
    private int start_territory_id = -1;
    private int dest_territory_id = -1;
    private Button btn_start_territory = null;
    private Button btn_dest_territory = null;
    private Tools tools = new Tools();
    private Dialog dialog;
    public static final String TAG = "GameOperationActivity";
    private Context context;
    private EditText item_operation_0;
    private EditText item_operation_1;
    private EditText item_operation_2;
    private EditText item_operation_3;
    private EditText item_operation_4;
    private EditText item_operation_5;
    private EditText item_operation_6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_operation);
        dialog = new Dialog(this);
        context = this;

        initView();
    }


    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        btn_attack_move_info = findViewById(R.id.btn_attack_move_info);
        btn_attack_move_info.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        operation = bundle.getString("option");

        btn_territory_1 = findViewById(R.id.btn_territory_1);
        btn_territory_1.setOnClickListener(this);
        btn_territory_2 = findViewById(R.id.btn_territory_2);
        btn_territory_2.setOnClickListener(this);
        btn_territory_3 = findViewById(R.id.btn_territory_3);
        btn_territory_3.setOnClickListener(this);
        btn_territory_4 = findViewById(R.id.btn_territory_4);
        btn_territory_4.setOnClickListener(this);
        btn_territory_5 = findViewById(R.id.btn_territory_5);
        btn_territory_5.setOnClickListener(this);
        btn_territory_6 = findViewById(R.id.btn_territory_6);
        btn_territory_6.setOnClickListener(this);
        btn_territory_7 = findViewById(R.id.btn_territory_7);
        btn_territory_7.setOnClickListener(this);
        btn_territory_8 = findViewById(R.id.btn_territory_8);
        btn_territory_8.setOnClickListener(this);
        btn_territory_9 = findViewById(R.id.btn_territory_9);
        btn_territory_9.setOnClickListener(this);
        btn_territory_10 = findViewById(R.id.btn_territory_10);
        btn_territory_10.setOnClickListener(this);
        btn_territory_11 = findViewById(R.id.btn_territory_11);
        btn_territory_11.setOnClickListener(this);
        btn_territory_12 = findViewById(R.id.btn_territory_12);
        btn_territory_12.setOnClickListener(this);
        btn_territory_13 = findViewById(R.id.btn_territory_13);
        btn_territory_13.setOnClickListener(this);
        btn_territory_14 = findViewById(R.id.btn_territory_14);
        btn_territory_14.setOnClickListener(this);
        btn_territory_15 = findViewById(R.id.btn_territory_15);
        btn_territory_15.setOnClickListener(this);
        btn_territory_16 = findViewById(R.id.btn_territory_16);
        btn_territory_16.setOnClickListener(this);
        btn_territory_17 = findViewById(R.id.btn_territory_17);
        btn_territory_17.setOnClickListener(this);
        btn_territory_18 = findViewById(R.id.btn_territory_18);
        btn_territory_18.setOnClickListener(this);
        btn_territory_19 = findViewById(R.id.btn_territory_19);
        btn_territory_19.setOnClickListener(this);
        btn_territory_20 = findViewById(R.id.btn_territory_20);
        btn_territory_20.setOnClickListener(this);
        btn_territory_21 = findViewById(R.id.btn_territory_21);
        btn_territory_21.setOnClickListener(this);
        btn_territory_22 = findViewById(R.id.btn_territory_22);
        btn_territory_22.setOnClickListener(this);
        btn_territory_23 = findViewById(R.id.btn_territory_23);
        btn_territory_23.setOnClickListener(this);
        btn_territory_24 = findViewById(R.id.btn_territory_24);
        btn_territory_24.setOnClickListener(this);
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        item_operation_0 = findViewById(R.id.item_operation_0);
        item_operation_1 = findViewById(R.id.item_operation_1);
        item_operation_2 = findViewById(R.id.item_operation_2);
        item_operation_3 = findViewById(R.id.item_operation_3);
        item_operation_4 = findViewById(R.id.item_operation_4);
        item_operation_5 = findViewById(R.id.item_operation_5);
        item_operation_6 = findViewById(R.id.item_operation_6);

        if(operation.equals("Attack")) {
            item_operation_0.setHint("attack");
            item_operation_1.setHint("attack");
            item_operation_2.setHint("attack");
            item_operation_3.setHint("attack");
            item_operation_4.setHint("attack");
            item_operation_5.setHint("attack");
            item_operation_6.setHint("attack");
        }
    }

    private void handleAttack(int source, int target, ArrayList<Integer> unit_operation_list) {
        Tools.setFlagColor(btn_start_territory, R.color.black, context);
        Tools.setFlagColor(btn_dest_territory, R.color.black, context);

        try {
            Request r = Tools.genAttack(source, target, unit_operation_list);
            if (Tools.checkLose(ClientApp.player_info)) {
                Tools.setLoseDialog(dialog, "You do not win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);
                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                tools.setInfoDialog(dialog, "Attack Info", "You made an attack successfully.", f);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();

            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            tools.setInfoDialog(dialog, "Attack Error", e.getMessage(), f);
        }

    }

    private void handleMove(int source, int target, ArrayList<Integer> unit_operation_list) {
        Tools.setFlagColor(btn_start_territory, R.color.black, context);
        Tools.setFlagColor(btn_dest_territory, R.color.black, context);

        try {
            Request r = Tools.genMove(source, target, unit_operation_list);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do not win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);
                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                tools.setInfoDialog(dialog, "Move Info", "You made a move successfully.", f);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();

            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            tools.setInfoDialog(dialog, "Move Error", e.getMessage(), f);
        }
    }

    private void handleOperationInfo() {
        Tools.hasSelectedStartDest(start_territory_id, dest_territory_id, dialog, operation);

        List<EditText> items_operation = new ArrayList<>();
        items_operation.add(item_operation_0);
        items_operation.add(item_operation_1);
        items_operation.add(item_operation_2);
        items_operation.add(item_operation_3);
        items_operation.add(item_operation_4);
        items_operation.add(item_operation_5);
        items_operation.add(item_operation_6);

        ArrayList<Integer> unit_operation_list = tools.getUnitsList(items_operation, dialog, operation + " Error");

        if(unit_operation_list == null) {
            return;
        }

        int source = start_territory_id - 1;
        int target = dest_territory_id - 1;

        switch(operation) {
            case "Attack":
                handleAttack(source, target, unit_operation_list);
                break;
            case "Move":
                handleMove(source,target, unit_operation_list);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_reset) {
            if(btn_start_territory != null && btn_dest_territory != null) {
                Tools.setFlagColor(btn_start_territory, R.color.black, context);
                Tools.setFlagColor(btn_dest_territory, R.color.black, context);
                btn_start_territory = null;
                btn_dest_territory = null;
                start_territory_id = -1;
                dest_territory_id = -1;
            }
            return;
        }

        if(view.getId() == R.id.btn_attack_move_info) {
            handleOperationInfo();
            return;
        }

        /** else, press one of the territory id buttons
         *
         */
        Button btn_territory_id = (Button)findViewById(view.getId());
        OperationTerritoryInfo op_territory_info = Tools.handleTerritorySelect(start_territory_id, dest_territory_id, btn_start_territory, btn_dest_territory,
                btn_territory_id, context, dialog, operation);

        if(op_territory_info == null) {
            return;
        }

        if(op_territory_info.isStart()) {
            start_territory_id = op_territory_info.getTerritoryID();
            btn_start_territory = op_territory_info.getBtnTerritory();
        }
        else {
            dest_territory_id = op_territory_info.getTerritoryID();
            btn_dest_territory = op_territory_info.getBtnTerritory();
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
                Intent intent = new Intent(GameOperationActivity.this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(GameOperationActivity.this, GameOperationActivity.class);
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
