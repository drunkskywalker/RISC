package edu.duke.ece651.team12.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import edu.duke.ece651.team12.GameStartActivity;
import edu.duke.ece651.team12.LoginActivity;
import edu.duke.ece651.team12.MainActivity;
import edu.duke.ece651.team12.R;
import edu.duke.ece651.team12.RegisterActivity;
import edu.duke.ece651.team12.bean.OperationTerritoryInfo;
import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.shared.AttackOwnershipChecker;
import edu.duke.ece651.team12.shared.AttackRequest;
import edu.duke.ece651.team12.shared.MoveOwnershipChecker;
import edu.duke.ece651.team12.shared.MoveRequest;
import edu.duke.ece651.team12.shared.PlayerInfo;
import edu.duke.ece651.team12.shared.Request;
import edu.duke.ece651.team12.shared.RuleChecker;
import edu.duke.ece651.team12.shared.Spy;
import edu.duke.ece651.team12.shared.Territory;
import edu.duke.ece651.team12.shared.UpgradeCostChecker;
import edu.duke.ece651.team12.shared.UpgradeOwnershipChecker;
import edu.duke.ece651.team12.shared.UpgradeRequest;

public class Tools {
    public static void setInfoDialog(Dialog dialog, String dialog_title, String dialog_body, Function<Dialog, Void> f) {
        dialog.setContentView(R.layout.info_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_dialog_title = dialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(dialog_title);
        TextView tv_dialog_body = dialog.findViewById(R.id.tv_dialog_body);
        tv_dialog_body.setText(dialog_body);
        Button btn_dialog_ok = dialog.findViewById(R.id.btn_dialog_ok);
        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f.apply(dialog);
            }
        });
        dialog.show();
    }

    public static void setWaitingDialog(Dialog dialog, String dialog_title, String dialog_body) {
        dialog.setContentView(R.layout.waiting_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_dialog_title = dialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(dialog_title);
        TextView tv_dialog_body = dialog.findViewById(R.id.tv_dialog_body);
        tv_dialog_body.setText(dialog_body);
        //dialog.setCancelable(false);
        dialog.show();
    }

    public static void setWinDialog(Dialog dialog) {
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static void setLoseDialog(Dialog dialog, String message) {
        dialog.setContentView(R.layout.lose_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_dialog_body_message = dialog.findViewById(R.id.tv_dialog_body_message);
        tv_dialog_body_message.setText(message);
        dialog.show();
    }

    public Intent handleLogout(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // it can close all the activities except the final activity
        return intent;
    }

    public void closeDrawerLayout(DrawerLayout drawer_layout) {
        drawer_layout.closeDrawer(GravityCompat.START);
    }

    public static boolean checkLose(PlayerInfo player_info) {
        for (Territory t : ClientApp.map.territories) {
            if (t.getPlayerInfo().equals(player_info)) {
                return false;
            }
        }
        return true;
    }

    public static PlayerInfo checkEnd () {
        PlayerInfo p = ClientApp.map.territories.get(0).getPlayerInfo();
        for (Territory t : ClientApp.map.territories) {
            if (!p.equals(t.getPlayerInfo())) {
                return null;
            }
        }
        return p;
    }

    public static Request genUpgrade(int source_num, ArrayList<Integer> uns) {
        Request r = new UpgradeRequest(ClientApp.player_info, source_num, uns);
        RuleChecker upg_rule_checker = new UpgradeOwnershipChecker();
        upg_rule_checker.checkRule(r, ClientApp.map, ClientApp.player_info);
        Territory t = ClientApp.map.territories.get(source_num);

        for (int i = 0; i < 6; i++) {
            int x = r.getUnits().get(i);
            int p = t.units.get(i);
            int n = t.units.get(i + 1);
            t.units.set(i, p - x);
            t.units.set(i + 1, n + x);
        }

        int[] costs = UpgradeCostChecker.costs();
        int cost = 0;
        for (int i = 0; i < 6; i++) {
            cost += r.getUnits().get(i) * costs[i];
        }
        int stock = ClientApp.map.tech_accus.get(ClientApp.player_info);
        ClientApp.map.tech_accus.put(ClientApp.player_info, stock - cost);
        return r;
    }

    public static Request genMove(int source_num, int target_num, ArrayList<Integer> units) {
        PlayerInfo player_info = ClientApp.player_info;
        RuleChecker mv_rule_checker = new MoveOwnershipChecker();

        Request r = new MoveRequest(player_info, source_num, target_num, units);
        mv_rule_checker.checkRule(r, ClientApp.map, player_info);

        for (int i = 0; i < 7; i++) {
            Integer sourcex = ClientApp.map.territories.get(source_num).units.get(i);
            sourcex -= units.get(i);
            Integer targetx = ClientApp.map.territories.get(target_num).units.get(i);
            targetx += units.get(i);
            ClientApp.map.territories.get(source_num).units.set(i, sourcex);
            ClientApp.map.territories.get(target_num).units.set(i, targetx);
            int food_acc = ClientApp.map.food_accus.get(player_info);
            int distance = ClientApp.map.shortestDistance(player_info)[source_num][target_num];
            food_acc -= distance * units.get(i);
            ClientApp.map.food_accus.put(player_info, food_acc);
        }
        return r;
    }

    public static Request genAttack(int source_num, int target_num, ArrayList<Integer> units) {
        PlayerInfo player_info = ClientApp.player_info;
        RuleChecker atk_rule_checker = new AttackOwnershipChecker();

        Request r = new AttackRequest(player_info, source_num, target_num, units);
        atk_rule_checker.checkRule(r, ClientApp.map, player_info);

        for (int i = 0; i < 7; i++) {
            Integer sourcex = ClientApp.map.territories.get(source_num).units.get(i);
            sourcex -= units.get(i);
            ClientApp.map.territories.get(source_num).units.set(i, sourcex);
            int food_acc = ClientApp.map.food_accus.get(player_info);
            int distance = ClientApp.map.distances[source_num][target_num];
            food_acc -= distance * units.get(i);
            ClientApp.map.food_accus.put(player_info, food_acc);
        }
        return r;
    }

    public ArrayList<Integer> getUnitsList(List<EditText> items_option, Dialog dialog, String dialog_title) {
        ArrayList<Integer> units_list = new ArrayList<Integer>();

        for(int i = 0; i < items_option.size(); i++) {
            if(items_option.get(i).length() == 0) {
                Function<Dialog, Void> f = (dialog1) -> {
                    dialog1.dismiss();
                    return null;
                };
                setInfoDialog(dialog, dialog_title, "Please fill in all the fields.", f);
                return null;
            }
            units_list.add(Integer.parseInt(items_option.get(i).getText().toString()));
        }

        return units_list;
    }

    public static int getNumOfSpies(Territory t, PlayerInfo playerInfo) {
        int num = 0;
        for(Spy spy : t.spies) {
            if(spy.owner.equals(playerInfo)) {
                num++;
            }
        }

        return num;
    }

    public static void setFlagColor(Button btn_territory_id, int color_id, Context context) {
        Drawable d = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_flag_svgrepo_com, null);
        d = DrawableCompat.wrap(d);
        DrawableCompat.setTint(d, context.getResources().getColor(color_id));
        btn_territory_id.setBackground(d);
    }

    public static OperationTerritoryInfo handleTerritorySelect(int start_territory_id, int dest_territory_id, Button btn_start_territory, Button btn_dest_territory, Button btn_territory_id, Context context, Dialog dialog, String operation) {
        OperationTerritoryInfo op_territory_info = null;

        if(start_territory_id == -1 && dest_territory_id == -1) {
            start_territory_id = Integer.parseInt(btn_territory_id.getText().toString());
            Tools.setFlagColor(btn_territory_id, R.color.real_red, context);
            btn_start_territory = btn_territory_id;
            op_territory_info = new OperationTerritoryInfo(true, start_territory_id, btn_start_territory);
        }
        else if (dest_territory_id == -1) {
            dest_territory_id = Integer.parseInt(btn_territory_id.getText().toString());
            Tools.setFlagColor(btn_territory_id, R.color.real_blue, context);
            btn_dest_territory = btn_territory_id;
            op_territory_info = new OperationTerritoryInfo(false, dest_territory_id, btn_dest_territory);
        }
        else {
            Function<Dialog, Void> f = (dialog1) -> {
                dialog1.dismiss();
                return null;
            };
            Tools.setInfoDialog(dialog, operation + " Info", "You have selected start and destination. If you want to reset, click the RESET button and reselect.", f);
        }

        return op_territory_info;
    }

    public static void hasSelectedStartDest(int start_territory_id, int dest_territory_id, Dialog dialog, String operation) {
        if(start_territory_id == -1 || dest_territory_id == -1) {
            Function<Dialog, Void> f = (dialog1) -> {
                dialog1.dismiss();
                return null;
            };
            setInfoDialog(dialog, operation + " Error", "Please select start and destination territory by clicking the flags.", f);
        }
    }
}
