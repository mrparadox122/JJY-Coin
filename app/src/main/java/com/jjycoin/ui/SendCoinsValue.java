package com.jjycoin.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.jjycoin.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONObject;

public class SendCoinsValue extends AppCompatActivity {

    AppCompatButton backButton , Send_Coins_Value_Send_Button;
    AppCompatEditText Send_Coins_Value_EditText;
    public static String account_Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coins_value);
        getSupportActionBar().hide();
        backButton = findViewById(R.id.BackButton);
        Send_Coins_Value_EditText = findViewById(R.id.Send_Coins_Value_EditText);
        Send_Coins_Value_Send_Button = findViewById(R.id.Send_Coins_Value_Send_Button);
        backButton.setOnClickListener(v -> {
            super.onBackPressed();
        });
        Send_Coins_Value_Send_Button.setOnClickListener(v -> {
            //Start ProgressBar first (Set visibility VISIBLE)
            if (!Send_Coins_Value_EditText.getText().equals("0") || !Send_Coins_Value_EditText.getText().toString().isEmpty() || Send_Coins_Value_EditText.getText().toString().length() <= 0)
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[3];
                        field[0] = "apiKey";
                        field[1] = "accountNumber";
                        field[2] = "coinsToAdd";
                        //Creating array for data
                        String[] data = new String[3];
                        data[0] = "s4fRbJ5w6Klm2Nc3";
                        data[1] = account_Number;
                        data[2] = Send_Coins_Value_EditText.getText().toString();
                        PutData putData = new PutData("http://jjysmartbusiness.com/_API/Send_Coins.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                //End ProgressBar (Set visibility to GONE)
                                Log.i("PutData", result);
                                try {
                                    JSONObject jsonResponse = new JSONObject(result);
                                    String status = jsonResponse.getString("status");
                                    // Show a toast message based on the status
                                    if (status.equals("success")) {
                                        Toast.makeText(SendCoinsValue.this, "Coins added successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        String message = jsonResponse.getString("message");
                                        Toast.makeText(SendCoinsValue.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(SendCoinsValue.this, "Error", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }

        });
    }
}