package edu.duke.ece651.team12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.AbstractMap;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.client.V3Client;
import edu.duke.ece651.team12.util.Tools;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_login_username;
    private EditText et_login_password;
    private Button btn_login_submit;
    private Button btn_back_to_register;
    private V3Client client;
    private Context context;
    public static final String TAG = "LoginActivity";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        initView();
    }

    private void initView() {
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login_submit = findViewById(R.id.btn_login_submit);
        btn_back_to_register = findViewById(R.id.btn_back_to_register);
        btn_login_submit.setOnClickListener(this);
        btn_back_to_register.setOnClickListener(this);
        dialog = new Dialog(this);

        client = ClientApp.client;
    }

    private void clearEditTexts() {
        et_login_username.getText().clear();
        et_login_password.getText().clear();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_register) {
            startActivity(new Intent(context, RegisterActivity.class));
            return;
        }

        Log.d(TAG, "submit button");
        String str_login_username = et_login_username.getText().toString();
        String str_login_password = et_login_password.getText().toString();

        new Thread(new Runnable(){
            @Override
            public void run() {
                AbstractMap.SimpleEntry<Integer, String> loginInfo = null;
                Log.d(TAG, "Input username, password: " + str_login_username + "," + str_login_password);

                try {
                    loginInfo = client.Login(str_login_username, str_login_password);
                    int login_key = loginInfo.getKey();
                    String login_desc = loginInfo.getValue();

                    Log.d(TAG, "Login Key: " + login_key + ", " + "Login Desc: " + login_desc);

                    if(login_key >= 0) {
                        Log.d(TAG, "Login success");

                        runOnUiThread(() -> {
                            Toast.makeText(context, login_desc, Toast.LENGTH_SHORT).show();
                        });
                        startActivity(new Intent(context, GameStartActivity.class));
                    }
                    else {
                        runOnUiThread(() -> {
                            String dialog_body = "";
                            if(login_key == -1) {
                                dialog_body = "Sorry, this username does not exist ( Ĭ ^ Ĭ )";
                            }
                            if(login_key == -2) {
                                dialog_body = "Sorry, the password is not correct ￣へ￣";
                            }

                            Function<Dialog, Void> f = (dialog) -> {
                                clearEditTexts();
                                dialog.dismiss();
                                return null;
                            };
                            new Tools().setInfoDialog(dialog, "Login Error", dialog_body, f);
                        });
                    }
                } catch (Exception e) {
                    Log.d(TAG,e.getMessage());
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Function<Dialog, Void> f = (dialog) -> {
                            clearEditTexts();
                            dialog.dismiss();
                            return null;
                        };
                        new Tools().setInfoDialog(dialog, "Login Error", e.getMessage(), f);
                    });
                }
            }
        }).start();

    }
}
