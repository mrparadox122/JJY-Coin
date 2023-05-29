package com.jjycoin;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BuyCoin extends AppCompatActivity {

    private static final int UPI_PAYMENT = 122;
    AppCompatButton sellcoin , BuyNow;
    TextView  YouGetValue;
    EditText YouPayEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coin);
        getSupportActionBar().hide();
        sellcoin = findViewById(R.id.sellcoin);
        BuyNow = findViewById(R.id.BuyNow);
        YouPayEditText = findViewById(R.id.YouPayEditText);
        YouGetValue = findViewById(R.id.YouGetValue);
        sellcoin.setOnClickListener(view -> {
            startActivity(new Intent(BuyCoin.this,SellCoins.class));
        });

            YouPayEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!isEmpty(YouPayEditText) && !YouPayEditText .getText().toString().isEmpty())
                    {
                        try {
                            String pay = YouPayEditText.getText().toString();
                            double coinValue = Double.parseDouble(Variables.CoinValue); // Fixed coin value of 100

                            // Calculate the value based on the coin value and pay amount
                            double youGetValue = Double.parseDouble(pay) / coinValue;

                            // Set the calculated value as the text of YouGetValue
                            YouGetValue.setText(String.valueOf(youGetValue));

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            YouGetValue.setText("0");
                        }

                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        BuyNow.setOnClickListener(v -> {
            if (!isEmpty(YouPayEditText) && !YouPayEditText.getText().toString().isEmpty())
            {
                Toast.makeText(this, YouGetValue.getText().toString(), Toast.LENGTH_SHORT).show();
                payUsingUpi(String.valueOf(Double.parseDouble(YouPayEditText.getText().toString())),"7095966526@kotak","JJY Coin");
            }
            else
            {
                YouPayEditText.setError("Enter Value");
            }
        });

    }


    public void payUsingUpi(String amount, String upiId, String name) {

        // Create a URI for the UPI payment intent.
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("am", amount)
                .build();

        // Create an intent for the UPI payment.
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // Show a dialog to the user to choose an UPI app.
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // Check if the intent resolves to an activity.
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT) {
            if (true) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable()) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (String s : response) {
                String[] equalStr = s.split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("status")) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("approvalrefno") || equalStr[0].toLowerCase().equals("txnref")) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}