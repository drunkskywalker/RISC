package edu.duke.ece651.team12;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.Color;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.Territory;
import edu.duke.ece651.team12.util.Tools;

public class GameHomepageActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private Button btn_user_options;
    private Dialog dialog;
    public static final String TAG = "GameHomepageActivity";
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
    private Button btn_territory_inquiry;
    private Context context;
    private TextView level_0_unit;
    private TextView level_1_unit;
    private TextView level_2_unit;
    private TextView level_3_unit;
    private TextView level_4_unit;
    private TextView level_5_unit;
    private TextView level_6_unit;
    private int selected_territory_id = -1;
    private int food_production_perturn = 0;
    private int tech_production_perturn = 0;
    private int food_accumulation = 0;
    private int tech_accumulation = 0;
    private int research_level = 0;
    private int num_of_spies = 0;
    private int cloaking_rounds = 0;
    private int scorch_rounds = 0;
    private String territory_name = "☆☆☆";
    private String owner_name = "☆☆☆";
    private Button btn_selected_territory = null;
    private Button btn_train_1_2;
    private Button btn_train_1_3;
    private Button btn_train_2_3;
    private Button btn_train_2_4;
    private Button btn_train_3_4;
    private Button btn_train_3_7;
    private Button btn_train_4_5;
    private Button btn_train_4_6;
    private Button btn_train_4_10;
    private Button btn_train_5_6;
    private Button btn_train_5_9;
    private Button btn_train_7_8;
    private Button btn_train_8_17;
    private Button btn_train_9_10;
    private Button btn_train_9_11;
    private Button btn_train_10_15;
    private Button btn_train_10_16;
    private Button btn_train_10_17;
    private Button btn_train_11_12;
    private Button btn_train_11_13;
    private Button btn_train_11_15;
    private Button btn_train_12_13;
    private Button btn_train_13_14;
    private Button btn_train_13_20;
    private Button btn_train_14_20;
    private Button btn_train_15_16;
    private Button btn_train_16_18;
    private Button btn_train_17_19;
    private Button btn_train_18_19;
    private Button btn_train_19_21;
    private Button btn_train_20_22;
    private Button btn_train_21_22;
    private Button btn_train_21_24;
    private Button btn_train_22_23;
    private Button btn_train_23_24;
    List<Button> btn_train_list = new ArrayList<>();
    List<Button> selected_train_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_homepage);
        context = this;

        initView();
    }


    private void initView() {
        btn_user_options = findViewById(R.id.btn_user_options);
        btn_user_options.setOnClickListener(this);
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        dialog = new Dialog(this);
        Function<Dialog, Void> f = (dialog) -> {
            dialog.dismiss();
            return null;
        };
        new Tools().setInfoDialog(dialog, "Territory Inquiry",
                "You can touch the flag in any territory to inquire about information", f);
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
        btn_territory_inquiry = findViewById(R.id.btn_territory_inquiry);
        btn_territory_inquiry.setOnClickListener(this);

        level_0_unit = findViewById(R.id.level_0_unit);
        level_1_unit = findViewById(R.id.level_1_unit);
        level_2_unit = findViewById(R.id.level_2_unit);
        level_3_unit = findViewById(R.id.level_3_unit);
        level_4_unit = findViewById(R.id.level_4_unit);
        level_5_unit = findViewById(R.id.level_5_unit);
        level_6_unit = findViewById(R.id.level_6_unit);

        btn_train_1_2 = findViewById(R.id.btn_train_1_2);
        btn_train_1_3 = findViewById(R.id.btn_train_1_3);
        btn_train_2_3 = findViewById(R.id.btn_train_2_3);
        btn_train_2_4 = findViewById(R.id.btn_train_2_4);
        btn_train_3_4 = findViewById(R.id.btn_train_3_4);
        btn_train_3_7 = findViewById(R.id.btn_train_3_7);
        btn_train_4_5 = findViewById(R.id.btn_train_4_5);
        btn_train_4_6 = findViewById(R.id.btn_train_4_6);
        btn_train_4_10 = findViewById(R.id.btn_train_4_10);
        btn_train_5_6 = findViewById(R.id.btn_train_5_6);
        btn_train_5_9 = findViewById(R.id.btn_train_5_9);
        btn_train_7_8 = findViewById(R.id.btn_train_7_8);
        btn_train_8_17 = findViewById(R.id.btn_train_8_17);
        btn_train_9_10 = findViewById(R.id.btn_train_9_10);
        btn_train_9_11 = findViewById(R.id.btn_train_9_11);
        btn_train_10_15 = findViewById(R.id.btn_train_10_15);
        btn_train_10_16 = findViewById(R.id.btn_train_10_16);
        btn_train_10_17 = findViewById(R.id.btn_train_10_17);
        btn_train_11_12 = findViewById(R.id.btn_train_11_12);
        btn_train_11_13 = findViewById(R.id.btn_train_11_13);
        btn_train_11_15 = findViewById(R.id.btn_train_11_15);
        btn_train_12_13 = findViewById(R.id.btn_train_12_13);
        btn_train_13_14 = findViewById(R.id.btn_train_13_14);
        btn_train_13_20 = findViewById(R.id.btn_train_13_20);
        btn_train_14_20 = findViewById(R.id.btn_train_14_20);
        btn_train_15_16 = findViewById(R.id.btn_train_15_16);
        btn_train_16_18 = findViewById(R.id.btn_train_16_18);
        btn_train_17_19 = findViewById(R.id.btn_train_17_19);
        btn_train_18_19 = findViewById(R.id.btn_train_18_19);
        btn_train_19_21 = findViewById(R.id.btn_train_19_21);
        btn_train_20_22 = findViewById(R.id.btn_train_20_22);
        btn_train_21_22 = findViewById(R.id.btn_train_21_22);
        btn_train_21_24 = findViewById(R.id.btn_train_21_24);
        btn_train_22_23 = findViewById(R.id.btn_train_22_23);
        btn_train_23_24 = findViewById(R.id.btn_train_23_24);

        btn_train_list.add(btn_train_1_2);
        btn_train_list.add(btn_train_1_3);
        btn_train_list.add(btn_train_2_3);
        btn_train_list.add(btn_train_2_4);
        btn_train_list.add(btn_train_3_4);
        btn_train_list.add(btn_train_3_7);
        btn_train_list.add(btn_train_4_5);
        btn_train_list.add(btn_train_4_6);
        btn_train_list.add(btn_train_4_10);
        btn_train_list.add(btn_train_5_6);
        btn_train_list.add(btn_train_5_9);
        btn_train_list.add(btn_train_7_8);
        btn_train_list.add(btn_train_8_17);
        btn_train_list.add(btn_train_9_10);
        btn_train_list.add(btn_train_9_11);
        btn_train_list.add(btn_train_10_15);
        btn_train_list.add(btn_train_10_16);
        btn_train_list.add(btn_train_10_17);
        btn_train_list.add(btn_train_11_12);
        btn_train_list.add(btn_train_11_13);
        btn_train_list.add(btn_train_11_15);
        btn_train_list.add(btn_train_12_13);
        btn_train_list.add(btn_train_13_14);
        btn_train_list.add(btn_train_13_20);
        btn_train_list.add(btn_train_14_20);
        btn_train_list.add(btn_train_15_16);
        btn_train_list.add(btn_train_16_18);
        btn_train_list.add(btn_train_17_19);
        btn_train_list.add(btn_train_18_19);
        btn_train_list.add(btn_train_19_21);
        btn_train_list.add(btn_train_20_22);
        btn_train_list.add(btn_train_21_22);
        btn_train_list.add(btn_train_21_24);
        btn_train_list.add(btn_train_22_23);
        btn_train_list.add(btn_train_23_24);
    }

    private void resetUnitsInfo() {
        level_0_unit.setText("☆");
        level_1_unit.setText("☆");
        level_2_unit.setText("☆");
        level_3_unit.setText("☆");
        level_4_unit.setText("☆");
        level_5_unit.setText("☆");
        level_6_unit.setText("☆");
    }

    private Button findViewByTerritoryInfo(int territory_id_1, int territory_id_2) {
        String id_info = "";
        if(territory_id_1 < territory_id_2) {
            id_info = territory_id_1 + "_" + territory_id_2;
        }
        else {
            id_info = territory_id_2 + "_" + territory_id_1;
        }

        for(Button btn : btn_train_list) {
            if(btn.getText().equals(id_info)) {
                return btn;
            }
        }

        return null;
    }

    private void resetColor() {
        if(btn_selected_territory != null) {
            Tools.setFlagColor(btn_selected_territory, R.color.black, context);

            for(Button btn : selected_train_list) {
                btn.setVisibility(View.INVISIBLE);
            }
            selected_train_list = new ArrayList<>();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_user_options) {
            user_drawer_layout.openDrawer(GravityCompat.START);
            return;
        }

        if(view.getId() == R.id.btn_territory_inquiry) {
            View popUpView = LayoutInflater.from(context).inflate(R.layout.popup_dialog, null, false);
            if(selected_territory_id != -1) {
                Territory t = ClientApp.map.territories.get(selected_territory_id-1);

                if(ClientApp.map.hasVision(t, ClientApp.player_info)) {
                    if(ClientApp.map.hasVision(t, ClientApp.player_info)) {
                        // keep the info in t
                    }
                    else if(ClientApp.map.old_info.containsKey(t.getId())) {
                        t = ClientApp.map.old_info.get(t.getId());
                    }
                    else {
                        t = null;
                    }

                    TextView tv_territory_id = popUpView.findViewById(R.id.tv_territory_id);
                    TextView tv_territory_name = popUpView.findViewById(R.id.tv_territory_name);
                    TextView tv_territory_owner = popUpView.findViewById(R.id.tv_territory_owner);
                    TextView tv_territory_food_each = popUpView.findViewById(R.id.tv_territory_food_each);
                    TextView tv_territory_food_total = popUpView.findViewById(R.id.tv_territory_food_total);
                    TextView tv_territory_tech_each = popUpView.findViewById(R.id.tv_territory_tech_each);
                    TextView tv_territory_tech_total = popUpView.findViewById(R.id.tv_territory_tech_total);
                    TextView tv_territory_tech_level = popUpView.findViewById(R.id.tv_territory_tech_level);
                    TextView tv_territory_spy = popUpView.findViewById(R.id.tv_territory_spy);
                    TextView tv_territory_cloaking_rounds = popUpView.findViewById(R.id.tv_territory_cloaking_rounds);
                    TextView tv_territory_scorch_rounds = popUpView.findViewById(R.id.tv_territory_scorch_rounds);

                    tv_territory_id.setText("Territory " + Integer.toString(selected_territory_id));
                    if(t != null) {
                        tv_territory_name.setText(territory_name);
                        tv_territory_owner.setText(owner_name);
                        tv_territory_food_each.setText(food_production_perturn + "");
                        tv_territory_food_total.setText(food_accumulation + "");
                        tv_territory_tech_each.setText(tech_production_perturn + "");
                        tv_territory_tech_total.setText(tech_accumulation + "");
                        tv_territory_tech_level.setText(research_level + "");
                        tv_territory_spy.setText(num_of_spies + "");
                        tv_territory_cloaking_rounds.setText(cloaking_rounds + " rounds");
                        tv_territory_scorch_rounds.setText(scorch_rounds + " rounds");
                    }
                    else {
                        tv_territory_name.setText("☆☆☆");
                        tv_territory_owner.setText("☆☆☆");
                        tv_territory_food_each.setText("☆☆☆");
                        tv_territory_food_total.setText("☆☆☆");
                        tv_territory_tech_each.setText("☆☆☆");
                        tv_territory_tech_total.setText("☆☆☆");
                        tv_territory_tech_level.setText("☆☆☆");
                        tv_territory_spy.setText("☆☆☆");
                        tv_territory_cloaking_rounds.setText("☆☆☆");
                        tv_territory_scorch_rounds.setText("☆☆☆");
                    }
                }
            }

            //create a PopupWindow，params are loaded View, width, height
            PopupWindow popUpWindow = new PopupWindow(popUpView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popUpWindow.setOutsideTouchable(true);
            popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

            popUpWindow.showAtLocation(popUpView, Gravity.LEFT, 0, 0);
            return;
        }

        /** else, press one of the territory id buttons
         *
         */
        int territory_number = Integer.parseInt(((Button)view).getText().toString());
        selected_territory_id = territory_number;
        Log.d(TAG, "territory number: " + territory_number);

        if(btn_selected_territory != null) {
            resetColor();
        }

        Button btn_territory_id = findViewById(view.getId());
        btn_selected_territory = btn_territory_id;
        Tools.setFlagColor(btn_territory_id, R.color.purple_200, context);

        /** New feature: fog of war
         *
         */
        //To see these value of different players, input a different PlayerInfo object, available at ClientApp.enemies or territory.getPlayerInfo()
        Territory t = ClientApp.map.territories.get(territory_number-1);

        if(ClientApp.map.hasVision(t, ClientApp.player_info)) {
            // keep the info in t
        }
        else if(ClientApp.map.old_info.containsKey(t.getId())) {
            t = ClientApp.map.old_info.get(t.getId());
        }
        else {
            t = null;
        }

        if(t != null) {
            // display territory info
            territory_name = t.getName();
            PlayerInfo owner = t.getPlayerInfo();
            owner_name = owner.player_name;
            Color color = owner.color;
            ArrayList<Integer> units = t.units;

            food_production_perturn = ClientApp.map.getFoodProduct(ClientApp.player_info);
            tech_production_perturn = ClientApp.map.getTechProduct(ClientApp.player_info);
            food_accumulation = ClientApp.map.food_accus.get(ClientApp.player_info);
            tech_accumulation = ClientApp.map.tech_accus.get(ClientApp.player_info);
            research_level = ClientApp.map.tech_lvls.get(ClientApp.player_info);
            num_of_spies = Tools.getNumOfSpies(t, ClientApp.player_info);
            cloaking_rounds = t.clockRounds;
            scorch_rounds = t.ScorchedEarthRounds;

            level_0_unit.setText(units.get(0) + "");
            level_1_unit.setText(units.get(1) + "");
            level_2_unit.setText(units.get(2) + "");
            level_3_unit.setText(units.get(3) + "");
            level_4_unit.setText(units.get(4) + "");
            level_5_unit.setText(units.get(5) + "");
            level_6_unit.setText(units.get(6) + "");

            // display railway info
            for(int i = 0; i < t.distmodifiers.length; i++) {
                if(t.distmodifiers[i] == 1) {
                    int territory_id_1 = t.getId() + 1;
                    int territory_id_2 = i + 1;
                    Button btn = findViewByTerritoryInfo(territory_id_1, territory_id_2);
                    btn.setVisibility(View.VISIBLE);
                    selected_train_list.add(btn);
                }
            }
        }
        else {
            resetUnitsInfo();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(btn_selected_territory != null) {
            resetColor();
        }

        switch (item.getItemId()) {
            case R.id.item_home:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_attack:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(GameHomepageActivity.this, GameOperationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("option", "Attack");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_move:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                intent = new Intent(GameHomepageActivity.this, GameOperationActivity.class);
                bundle = new Bundle();
                bundle.putString("option", "Move");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_commit:
                user_drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, GameCommitActivity.class));
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
