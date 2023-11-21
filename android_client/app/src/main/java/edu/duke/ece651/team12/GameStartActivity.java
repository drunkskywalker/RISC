package edu.duke.ece651.team12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import edu.duke.ece651.team12.util.Tools;

public class GameStartActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Button btn_game_new;
    private Button btn_game_join;
    private Button btn_simple_user_options;
    private NavigationView simple_options_nav_view;
    private DrawerLayout simple_user_drawer_layout;
    private TextView tv_user_title;
    private Button btn_game_logout;
    private Context context;
    private Tools tools = new Tools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        tv_user_title = findViewById(R.id.tv_user_title);
        tv_user_title.setText("Begin Your Adventure");
        context = this;

        initView();
    }

    private void initView() {
        btn_game_new = findViewById(R.id.btn_game_new);
        btn_game_new.setOnClickListener(this);
        btn_game_join = findViewById(R.id.btn_game_join);
        btn_game_join.setOnClickListener(this);
        btn_simple_user_options = findViewById(R.id.btn_simple_user_options);
        btn_simple_user_options.setOnClickListener(this);
        simple_user_drawer_layout = findViewById(R.id.simple_user_drawer_layout);
        simple_options_nav_view = findViewById(R.id.simple_options_nav_view);
        simple_options_nav_view.setNavigationItemSelectedListener(this);
        btn_game_logout = findViewById(R.id.btn_game_logout);
        btn_game_logout.setOnClickListener(this);
    }

    private void logoutGame() {
        Intent intent = tools.handleLogout(context);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_game_new:
                startActivity(new Intent(this, GameNewActivity.class));
                break;
            case R.id.btn_game_join:
                startActivity(new Intent(this, GameJoinActivity.class));
                break;
            case R.id.btn_simple_user_options:
                simple_user_drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_game_logout:
                logoutGame();
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
