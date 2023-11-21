package edu.duke.ece651.team12;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.client.V3Client;
import edu.duke.ece651.team12.util.Tools;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_login;
    private Button btn_register;
    public static final String TAG = "MainActivity";
    private Dialog dialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        context = this;
        dialog = new Dialog(this);
    }


    private void initializeGame() {
        V3Client client = ClientApp.client;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Log.d(TAG, "connecting to server");
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();

                    Function<Dialog, Void> f = (dialog) -> {dialog.dismiss();
                        return null;
                    };
                    runOnUiThread(() -> {
                        new Tools().setInfoDialog(dialog, "Connection Error","Cannot initialize the game...", f);
                    });
                }
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        V3Client client = ClientApp.client;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Log.d(TAG, "connecting to server");
                    Function<Dialog, Void> f = (dialog) -> {dialog.dismiss();
                        return null;
                    };
                    runOnUiThread(() -> {
                        new Tools().setInfoDialog(dialog, "Connection...","Please wait", f);
                    });
                    client.connect();
                    switch (view.getId()) {
                        case R.id.btn_login:
                            startActivity(new Intent(context, LoginActivity.class));
                            break;
                        case R.id.btn_register:
                            startActivity(new Intent(context, RegisterActivity.class));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Function<Dialog, Void> f = (dialog) -> {dialog.dismiss();
                        return null;
                    };
                    runOnUiThread(() -> {
                        new Tools().setInfoDialog(dialog, "Connection Error","Cannot initialize the game...", f);
                    });
                }
            }
        }).start();
    }
}
