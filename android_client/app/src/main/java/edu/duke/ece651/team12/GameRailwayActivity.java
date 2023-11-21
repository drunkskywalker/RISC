package edu.duke.ece651.team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class GameRailwayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout user_drawer_layout;
    private NavigationView options_nav_view;
    private TextView tv_create_a_railway;
    private TextView tv_sabotage_a_railway;
    private TextView tv_scorch_earth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_railway);

        initView();
    }

    private void initView() {
        user_drawer_layout = findViewById(R.id.user_drawer_layout);
        options_nav_view = findViewById(R.id.options_nav_view);
        options_nav_view.setNavigationItemSelectedListener(this);
        tv_create_a_railway = findViewById(R.id.tv_create_a_railway);
        tv_sabotage_a_railway = findViewById(R.id.tv_sabotage_a_railway);
        tv_scorch_earth = findViewById(R.id.tv_scorch_earth);
        tv_create_a_railway.setOnClickListener(this);
        tv_sabotage_a_railway.setOnClickListener(this);
        tv_scorch_earth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String fog_railway_option = "";

        switch (view.getId()) {
            case R.id.tv_create_a_railway:
                fog_railway_option = "create_a_railway";
                break;
            case R.id.tv_sabotage_a_railway:
                fog_railway_option = "sabotage_a_railway";
                break;
            case R.id.tv_scorch_earth:
                fog_railway_option = "scorch_earth";
                break;
        }

        Intent intent = new Intent(this, GameFogRailwayOperationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fog_railway_option", fog_railway_option);
        intent.putExtras(bundle);
        startActivity(intent);
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
                break;
        }

        return false;
    }

}