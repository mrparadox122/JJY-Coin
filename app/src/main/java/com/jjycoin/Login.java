package com.jjycoin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.timonknispel.ktloadingbutton.KTLoadingButton;

public class Login extends AppCompatActivity {

    EditText Username , Password;
    TextView CreateAccount;
    KTLoadingButton LoginButton;
    CheckBox RememberMe;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Username = findViewById(R.id.UsernameEditText);
        Password = findViewById(R.id.PasswordEditText);
        CreateAccount = findViewById(R.id.CreateAccountLoginText);
        LoginButton = findViewById(R.id.LoginButton);
        RememberMe = findViewById(R.id.RememberMe);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            Username.setText(savedUsername);
            Password.setText(savedPassword);
            RememberMe.setChecked(true);
            // Automatically hit the API
            LoginButton.startLoading();
            Executor executors = Executors.newSingleThreadExecutor();
            executors.execute(new LoginApi());
        }

        LoginButton.setOnClickListener(view -> {
            //Start ProgressBar first (Set visibility VISIBLE)
            if (!Username.getText().toString().isEmpty() && !Password.getText().toString().isEmpty())
            {
                LoginButton.startLoading();
                Executor executors = Executors.newSingleThreadExecutor();
                executors.execute(new LoginApi());
            }
            else if(Username.getText().toString().trim().isEmpty()) {
                Username.setError("Username is required");
                Username.requestFocus();
                LoginButton.doResult(false, ktLoadingButton -> null);
            }
            else {
                Password.setError("Username is required");
                Password.requestFocus();
                LoginButton.doResult(false, ktLoadingButton -> null);
            }

        });
        CreateAccount.setOnClickListener(view -> {
            startActivity(new Intent(Login.this,Create_an_account.class));
        });
    }

    private class LoginApi implements Runnable {
        @Override
        public void run() {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[3];
            field[0] = "username";
            field[1] = "password";
            field[2] = "apiKey";
            //Creating array for data
            String[] data = new String[3];
            data[0] = Username.getText().toString();
            data[1] = Password.getText().toString();
            data[2] = Variables.APiKey;
            PutData putData = new PutData("https://jjyenterprises.in/_API/Login.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("PutData", result);
                    runOnUiThread( () -> {
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            if (status.equals("error")) {
                                String message = json.getString("message");
                                Log.e("MainActivity", "Error: " + message);
                                Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                                LoginButton.doResult(false, ktLoadingButton -> null);
                                return;
                            }
                            if (status.equals("success"))
                            {
                                if (RememberMe.isChecked()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", data[0]);
                                    editor.putString("password", data[1]);
                                    editor.apply();
                                }
                                LoginButton.doResult(true, ktLoadingButton -> null);
                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,HomeScreen.class));

                            }
                            JSONObject dataApi = json.getJSONObject("data");
                            String dataStatus = dataApi.getString("status");
                            int userId = dataApi.getInt("User_ID");
                            Variables.userId = String.valueOf(userId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } );
                }
                else {
                    runOnUiThread( () -> {
                        LoginButton.doResult(false, ktLoadingButton -> null);
                    } );

                }
            }
        }
    }

}