package edu.duke.ece651.team12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.AbstractMap;
import java.util.function.Function;

import edu.duke.ece651.team12.client.ClientApp;
import edu.duke.ece651.team12.client.V3Client;
import edu.duke.ece651.team12.util.Tools;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_register_username;
    private EditText et_register_password;
    private EditText et_register_confirm_password;
    private Button btn_register_submit;
    private Button btn_back_to_login;
    private V3Client client;
    private Context context;
    public static final String TAG = "RegisterActivity";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        initView();
    }

    private void initView() {
        et_register_username = findViewById(R.id.et_register_username);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_confirm_password = findViewById(R.id.et_register_confirm_password);
        btn_register_submit = findViewById(R.id.btn_register_submit);
        btn_back_to_login = findViewById(R.id.btn_back_to_login);
        btn_register_submit.setOnClickListener(this);
        btn_back_to_login.setOnClickListener(this);
        dialog = new Dialog(this);

        client = ClientApp.client;
    }

    private void clearEditTexts() {
        et_register_username.getText().clear();
        et_register_password.getText().clear();
        et_register_confirm_password.getText().clear();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_login) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        Log.d(TAG, "submit button");
        String str_register_username = et_register_username.getText().toString();
        String str_register_password = et_register_password.getText().toString();
        String str_register_confirm_password = et_register_confirm_password.getText().toString();

        Log.d(TAG, "Input username, password, confirmPassword: " + str_register_username + "," + str_register_password + "," + str_register_confirm_password);

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AbstractMap.SimpleEntry<Integer, String> registerInfo = client.Register(str_register_username, str_register_password, str_register_confirm_password);
                    int register_key = registerInfo.getKey();
                    String register_desc = registerInfo.getValue();

                    Log.d(TAG, "Register Key: " + register_key + ", " + "Register Desc: " + register_desc);

                    if(register_key >= 0) {
                        Log.d(TAG, "Register success");

                        runOnUiThread(() -> {
                            Toast.makeText(context, register_desc, Toast.LENGTH_SHORT).show();
                        });
                        startActivity(new Intent(context, GameStartActivity.class));
                    }
                    else {
                        runOnUiThread(() -> {
                            String dialog_body = "";
                            if(register_key == -1) {
                                dialog_body = "Sorry, this username has been used ( Ĭ ^ Ĭ )";
                            }
                            if(register_key == -2) {
                                dialog_body = "Sorry, the two passwords do not match d(´ω｀*)";
                            }

                            Function<Dialog, Void> f = (dialog) -> {
                                clearEditTexts();
                                dialog.dismiss();
                                return null;
                            };
                            new Tools().setInfoDialog(dialog, "Register Error", dialog_body, f);
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
                        new Tools().setInfoDialog(dialog, "Register Error", e.getMessage(), f);
                    });
                }
            }
        }).start();

    }
}
