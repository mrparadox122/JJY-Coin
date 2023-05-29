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

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class BuyCoin extends AppCompatActivity {

    private static final int UPI_PAYMENT = 122;
    private static final int UPI_PAYMENT_REQUEST_CODE = 2316516;
    AppCompatButton sellcoin, BuyNow;
    TextView YouGetValue;
    EditText YouPayEditText;
    private EasyUpiPayment easyUpiPayment;

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
            startActivity(new Intent(BuyCoin.this, SellCoins.class));
        });

        YouPayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmpty(YouPayEditText) && !YouPayEditText.getText().toString().isEmpty()) {
                    try {
                        String pay = YouPayEditText.getText().toString();
                        double coinValue = Double.parseDouble(Variables.CoinValue); // Fixed coin value of 100

                        // Calculate the value based on the coin value and pay amount
                        double youGetValue = Double.parseDouble(pay) / coinValue;

                        // Set the calculated value as the text of YouGetValue
                        YouGetValue.setText(String.valueOf(youGetValue));

                    } catch (Exception e) {
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
            if (!isEmpty(YouPayEditText) && !YouPayEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, YouGetValue.getText().toString(), Toast.LENGTH_SHORT).show();
                makePayment(String.valueOf(Double.parseDouble(YouPayEditText.getText().toString())), "7095966526@kotak", "JJYCoin", "", "12156489456");
            } else {
                YouPayEditText.setError("Enter Value");
            }
        });

    }

    // START PAYMENT INITIALIZATION
    // START PAYMENT INITIALIZATION
    public void makePayment(String amount, String upi, String name, String desc, String transactionId) {
        // Create a new Intent to start the UPI payment activity.
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("upi://pay?pa=" + upi + "&pn=" + name + "&am=" + amount + "&cu=INR" + "&tn=" + transactionId + "&tr=" + transactionId + "&desc=" + desc));
        // Start the activity.
        startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE, paymentStatusBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            String paymentStatus = "";
            if (resultCode == RESULT_OK) {
                paymentStatus = "success";
            } else if (resultCode == RESULT_CANCELED) {
                paymentStatus = "failure";
            } else {
                paymentStatus = "pending";
            }

            if (onPaymentStatus != null) {
                onPaymentStatus.run(paymentStatus);
            }
        }
    }

    private final RunnableWithParam<String> onPaymentStatus = paymentStatus -> {
        // Handle payment status logic here
        // Retrieve the status message from the Bundle based on the payment status
        String statusMessage = paymentStatusBundle().getString(paymentStatus);
        Toast.makeText(BuyCoin.this, statusMessage, Toast.LENGTH_SHORT).show();
    };
    public interface RunnableWithParam<T> {
        void run(T param);
    }

    private Bundle paymentStatusBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("success", "Payment successful");
        bundle.putString("failure", "Payment failed");
        bundle.putString("pending", "Payment pending");
        return bundle;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onBackPressed() {

    }
}
