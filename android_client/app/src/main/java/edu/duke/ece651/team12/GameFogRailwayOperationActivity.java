package edu.duke.ece651.team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.function.Function;

import edu.duke.ece651.team12.bean.OperationTerritoryInfo;
import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.CloakOwnershipChecker;
import edu.duke.ece651.team12.shared.CloakRequest;
import edu.duke.ece651.team12.shared.RailwayConstructOwnershipChecker;
import edu.duke.ece651.team12.shared.RailwayConstructRequest;
import edu.duke.ece651.team12.shared.RailwaySabotageOwnershipChecker;
import edu.duke.ece651.team12.shared.RailwaySabotageRequest;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.ResearchDuplicationChecker;
import edu.duke.ece651.team12.shared.ResearchRequest;
import edu.duke.ece651.team12.shared.RuleChecker;
import edu.duke.ece651.team12.shared.ScorchedEarthOwnershipChecker;
import edu.duke.ece651.team12.shared.ScorchedEarthRequest;
import edu.duke.ece651.team12.shared.SpyCreateRequest;
import edu.duke.ece651.team12.shared.SpyMoveOwnershipChecker;
import edu.duke.ece651.team12.shared.SpyMoveRequest;
import edu.duke.ece651.team12.shared.SpyTrainOwnershipChecker;
import edu.duke.ece651.team12.util.Tools;

public class GameFogRailwayOperationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;

    private ImageView image_fog_railway_left;
    private TextView tv_fog_railway_title;
    private ImageView image_fog_railway_right;
    private TextView tv_fog_railway_body;
    private TextView tv_fog_railway_question;
    private Button btn_fog_railway_info;

    private String fog_railway_option;

    private Dialog dialog;
    public static final String TAG = "GameFogRailwayOperationActivity";
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
    private Context context;
    
    private int selected_territory_id = -1;
    private int start_territory_id = -1;
    private int dest_territory_id = -1;
    private Button btn_selected_territory = null;
    private Button btn_start_territory = null;
    private Button btn_dest_territory = null;
    private Button btn_fog_railway_reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fog_railway_operation);
        context = this;

        Bundle bundle = getIntent().getExtras();
        fog_railway_option = bundle.getString("fog_railway_option");

        initView();
    }

    private void resetTerritoryInfo() {
        if(btn_start_territory != null && btn_dest_territory != null) {
            Tools.setFlagColor(btn_start_territory, R.color.black, context);
            Tools.setFlagColor(btn_dest_territory, R.color.black, context);
            btn_start_territory = null;
            btn_dest_territory = null;
            start_territory_id = -1;
            dest_territory_id = -1;
        }
    }

    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        dialog = new Dialog(this);

        image_fog_railway_left = findViewById(R.id.image_fog_railway_left);
        tv_fog_railway_title = findViewById(R.id.tv_fog_railway_title);
        image_fog_railway_right =findViewById(R.id.image_fog_railway_right);
        tv_fog_railway_body = findViewById(R.id.tv_fog_railway_body);
        tv_fog_railway_question = findViewById(R.id.tv_fog_railway_question);

        if(fog_railway_option.equals("move_a_spy")) {
            image_fog_railway_left.setImageDrawable(getResources().getDrawable(R.drawable.ic_route_start_svgrepo_com));
            tv_fog_railway_title.setText("MOVE\nA\nSPY");
            image_fog_railway_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_route_start_svgrepo_com));
            tv_fog_railway_body.setText("Please click two flags, red as start, blue as destination");
            tv_fog_railway_question.setText("Are you sure to move a spy?");
            btn_fog_railway_reset = findViewById(R.id.btn_fog_railway_reset);
            btn_fog_railway_reset.setVisibility(View.VISIBLE);
            btn_fog_railway_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_start_territory != null && btn_dest_territory != null) {
                       resetTerritoryInfo();
                    }
                }
            });
        }
        else if(fog_railway_option.equals("cloaking")) {
            image_fog_railway_left.setImageDrawable(getResources().getDrawable(R.drawable.ic_mask_svgrepo_com));
            tv_fog_railway_title.setText("CLOAKING");
            image_fog_railway_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_mask_svgrepo_com));
            tv_fog_railway_question.setText("Are you sure to cloak?");
        }
        else if(fog_railway_option.equals("create_a_railway")) {
            image_fog_railway_left.setImageDrawable(getResources().getDrawable(R.drawable.ic_construction_crane_svgrepo_com));
            tv_fog_railway_title.setText("CREATE\nRAILWAY");
            tv_fog_railway_body.setText("Require Tech Level 2.Please click two flags, red as start, blue as destination");
            image_fog_railway_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_construction_crane_svgrepo_com));
            tv_fog_railway_question.setText("Are you sure to create a railway?");
            btn_fog_railway_reset = findViewById(R.id.btn_fog_railway_reset);
            btn_fog_railway_reset.setVisibility(View.VISIBLE);
            btn_fog_railway_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_start_territory != null && btn_dest_territory != null) {
                        resetTerritoryInfo();
                    }
                }
            });
        }
        else if(fog_railway_option.equals("sabotage_a_railway")) {
            image_fog_railway_left.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_entry_svgrepo_com));
            tv_fog_railway_title.setText("SABOTAGE\nRAILWAY");
            tv_fog_railway_body.setText("Require Tech Level 3. Please click on the flag to choose a territory.");
            image_fog_railway_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_entry_svgrepo_com));
            tv_fog_railway_question.setText("Are you sure to sabotage a railway?");
        }
        else if(fog_railway_option.equals("scorch_earth")) {
            image_fog_railway_left.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_svgrepo_com));
            tv_fog_railway_title.setText("SCORCH\nEARTH");
            tv_fog_railway_body.setText("It affects 5 rounds. Please click on the flag to choose a territory.");
            image_fog_railway_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_svgrepo_com));
            tv_fog_railway_question.setText("Are you sure to scorch earth?");
        }

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
        btn_fog_railway_info = findViewById(R.id.btn_fog_railway_info);
        btn_fog_railway_info.setOnClickListener(this);
    }

    private void handleTrainASpyRequest() {
        if(selected_territory_id == -1) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            Tools.setInfoDialog(dialog, "Train A Spy Error", "Please select a territory", f);
            return;
        }

        Tools.setFlagColor(btn_selected_territory, R.color.black, context);

        try{
            Request r = new SpyCreateRequest(ClientApp.player_info, selected_territory_id - 1);
            RuleChecker rule_checker = new SpyTrainOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Train spy success", "You trained a spy successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Train Spy Error", e.getMessage(), f);
        }
    }

    private void handleCloakingRequest() {
        if(selected_territory_id == -1) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            Tools.setInfoDialog(dialog, "Cloaking Error", "Please select a territory", f);
            return;
        }

        Tools.setFlagColor(btn_selected_territory, R.color.black, context);

        try{
            Request r = new CloakRequest(ClientApp.player_info, selected_territory_id - 1);
            RuleChecker rule_checker = new CloakOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Cloaking success", "You cloaked successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Cloaking Error", e.getMessage(), f);
        }
    }

    private void handleMoveASpyRequest() {
        Tools.hasSelectedStartDest(start_territory_id, dest_territory_id, dialog, "Move Spy");

        if(btn_start_territory != null) {
            Tools.setFlagColor(btn_start_territory, R.color.black, context);
        }

        if(btn_dest_territory != null) {
            Tools.setFlagColor(btn_dest_territory, R.color.black, context);
        }

        try{
            Request r = new SpyMoveRequest(ClientApp.player_info, start_territory_id - 1, dest_territory_id - 1);
            RuleChecker rule_checker = new SpyMoveOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Move spy success", "You moved a spy successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Move Spy Error", e.getMessage(), f);
        }
    }

    private void handleCreateRailwayRequest() {
        Tools.hasSelectedStartDest(start_territory_id, dest_territory_id, dialog, "Create Railway");

        if(btn_start_territory != null) {
            Tools.setFlagColor(btn_start_territory, R.color.black, context);
        }

        if(btn_dest_territory != null) {
            Tools.setFlagColor(btn_dest_territory, R.color.black, context);
        }

        try{
            Request r = new RailwayConstructRequest(ClientApp.player_info, start_territory_id - 1, dest_territory_id - 1);
            RuleChecker rule_checker = new RailwayConstructOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Create railway success", "You created a railway successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Create Railway Error", e.getMessage(), f);
        }
    }

    private void handleSabotageRailwayRequest() {
        if(selected_territory_id == -1) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            Tools.setInfoDialog(dialog, "Sabotage Railway Error", "Please select a territory", f);
            return;
        }

        Tools.setFlagColor(btn_selected_territory, R.color.black, context);

        try{
            Request r = new RailwaySabotageRequest(ClientApp.player_info, selected_territory_id - 1);
            RuleChecker rule_checker = new RailwaySabotageOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Sabotage railway success", "You sabotaged a railway successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Sabotage Railway Error", e.getMessage(), f);
        }
    }

    private void handleScorchEarthRequest() {
        if(selected_territory_id == -1) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };
            Tools.setInfoDialog(dialog, "Scorch Earth Error", "Please select a territory", f);
            return;
        }

        Tools.setFlagColor(btn_selected_territory, R.color.black, context);

        try{
            Request r = new ScorchedEarthRequest(ClientApp.player_info, selected_territory_id - 1);
            RuleChecker rule_checker = new ScorchedEarthOwnershipChecker();
            rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
            if (Tools.checkLose(ClientApp.player_info)){
                Tools.setLoseDialog(dialog, "You do no win.");
            }
            else if(r != null) {
                ClientApp.requests.add(r);

                Function<Dialog, Void> f = (dialog) -> {
                    dialog.dismiss();
                    return null;
                };
                Tools.setInfoDialog(dialog, "Scorch earth success", "You scorched earth successfully.", f);
            }
        }
        catch (Exception e) {
            Function<Dialog, Void> f = (dialog) -> {
                dialog.dismiss();
                return null;
            };

            Tools.setInfoDialog(dialog, "Scorch Earth Error", e.getMessage(), f);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_fog_railway_info) {
            switch(fog_railway_option) {
                case "train_a_spy":
                    handleTrainASpyRequest();
                    break;
                case "move_a_spy":
                    handleMoveASpyRequest();
                    break;
                case "cloaking":
                    handleCloakingRequest();
                    break;
                case "create_a_railway":
                    handleCreateRailwayRequest();
                    break;
                case "sabotage_a_railway":
                    handleSabotageRailwayRequest();
                    break;
                case "scorch_earth":
                    handleScorchEarthRequest();
                    break;

            }
            return;
        }

        /** else, press one of the territory id buttons
         *
         */
        if(fog_railway_option.equals("train_a_spy") || fog_railway_option.equals("cloaking") || fog_railway_option.equals("sabotage_a_railway") || fog_railway_option.equals("scorch_earth")) {
            if(selected_territory_id != -1) {
                Tools.setFlagColor(btn_selected_territory, R.color.black, context);
            }

            int territory_number = Integer.parseInt(((Button)view).getText().toString());
            selected_territory_id = territory_number;
            btn_selected_territory = (Button) view;
            Log.d(TAG, "territory number: " + territory_number);

            Button btn_territory_id = findViewById(view.getId());
            Tools.setFlagColor(btn_territory_id, R.color.purple_200, context);
        }
        else {
            Button btn_territory_id = (Button)findViewById(view.getId());
            String from_to_operation = "";
            if(fog_railway_option.equals("move_a_spy")) {
                from_to_operation = "Move Spy";
            }
            else {
                from_to_operation = "Create Railway";
            }

            OperationTerritoryInfo op_territory_info = Tools.handleTerritorySelect(start_territory_id, dest_territory_id, btn_start_territory, btn_dest_territory,
                    btn_territory_id, context, dialog, from_to_operation);

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(selected_territory_id != -1) {
            Tools.setFlagColor(btn_selected_territory, R.color.black, context);
        }



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
                startActivity(new Intent(this, GameResearchActivity.class));
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