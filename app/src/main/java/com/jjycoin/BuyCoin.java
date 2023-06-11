package com.jjycoin;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjycoin.Apis.BuyCoinApi;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;


public class BuyCoin extends AppCompatActivity implements BuyCoinApi.BuyCoinApiListner {

    private static final int UPI_PAYMENT_REQUEST_CODE = 2316516;
    AppCompatButton sellcoin, BuyNow;
    TextView YouGetValue;
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

        double coinValue = Double.parseDouble(Variables.CoinValue); // Fixed coin value of 100

        // Calculate the value based on the coin value and pay amount
        double youGetValue = Double.parseDouble("1.00") / coinValue;

        // Set the calculated value as the text of YouGetValue
        YouGetValue.setText(String.valueOf(youGetValue));
        sellcoin.setOnClickListener(view -> {
            startActivity(new Intent(BuyCoin.this, SellCoins.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
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
                makePayment(YouPayEditText.getText().toString(), "905222.06@cmsidfc", "JJY ENTERPRISES");
            } else {
                YouPayEditText.setError("Enter Value");
            }
        });

    }
    // START PAYMENT INITIALIZATION
    public void makePayment(String amount, String upi, String name) {
        String transactionId = String.valueOf(Long.parseLong(generateTransactionId())+98);
        // Create a new Intent to start the UPI payment activity.
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getUPIString(upi, name, transactionId, amount)));
        // Start the activity.
        startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE);
    }

    private String generateTransactionId() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp/2);
    }

    private String getUPIString(String payeeAddress, String payeeName, String trxnID,
                                String payeeAmount) {
        String trxnRefId = generateTransactionId();
        String UPI = "upi://pay?pn=" + payeeName + "&am=" + payeeAmount
                + "&pa=" + payeeAddress + "&tid=" + trxnID + "&tr=" + trxnRefId;
        return UPI.replace(" ", "+");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== RESULT_CANCELED)
        {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            PopupDialog.getInstance(this)
                    .setStyle(Styles.ALERT)
                    .setHeading("CANCELED")
                    .setDescription("You've Canceled"+
                            " The Payment.")
                    .setCancelable(false)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                        }
                    });
            return;
        }
        if (requestCode == UPI_PAYMENT_REQUEST_CODE && data != null) {
            // Get the extras from the intent
            Bundle extras = data.getExtras();
            if (extras != null)
            {
                // Convert extras to a string
                String extrasString = extras.getString("response");
                // Split the extrasString based on the delimiter '&'
                String[] keyValuePairs = extrasString.split("&");

                // Create variables to store the individual values
                String txnId = "";
                String txnRef = "";
                String status = "";
                String responseCode = "";

                // Iterate through the keyValuePairs array
                for (String pair : keyValuePairs) {
                    // Split each key-value pair based on the delimiter '='
                    String[] entry = pair.split("=");

                    if (entry.length == 2) {
                        String key = entry[0];
                        String value = entry[1];
                        // Assign the values to the corresponding variables
                        switch (key) {
                            case "txnId":
                                txnId = value;
                                break;
                            case "txnRef":
                                txnRef = value;
                                break;
                            case "Status":
                                status = value;
                                break;
                            case "responseCode":
                                responseCode = value;
                                break;
                        }
                    }
                }
                // Log the individual values
                Log.d("txnId", txnId);
                Log.d("txnRef", txnRef);
                Log.d("Status", status);
                Log.d("responseCode", responseCode);
                Toast.makeText(this, status, Toast.LENGTH_LONG).show();
                if (status.matches("SUCCESS") ||status.matches("Success") || status.equalsIgnoreCase("SUCCESS"))
                {
                    BuyCoinApi buyCoinApi = new BuyCoinApi(txnId,txnRef,status,responseCode,YouGetValue.getText().toString(),YouPayEditText.getText().toString(),Variables.CoinValue,this);
                    buyCoinApi.CallAPi();
                }
                else if (status.matches("FAILURE"))
                {
                    Toast.makeText(this, "Purchase Failed", Toast.LENGTH_SHORT).show();
                    PopupDialog.getInstance(this)
                            .setStyle(Styles.FAILED)
                            .setHeading("Uh-Oh")
                            .setDescription("Unexpected error occurred."+
                                    " Payment Failed.")
                            .setCancelable(false)
                            .showDialog(new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                }
                            });
                }
            }
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void OnComplete() {
        runOnUiThread( () -> {
            Toast.makeText(BuyCoin.this, "Purchase Successfully", Toast.LENGTH_SHORT).show();
            PopupDialog.getInstance(this)
                    .setStyle(Styles.SUCCESS)
                    .setHeading("Well Done")
                    .setDescription("You have successfully"+
                            " Purchased Coins")
                    .setCancelable(false)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                        }
                    });
        } );
    }

    @Override
    public void OnError(String error) {
        runOnUiThread( () -> {
            PopupDialog.getInstance(this)
                    .setStyle(Styles.FAILED)
                    .setHeading("Uh-Oh")
                    .setDescription(error)
                    .setCancelable(false)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                        }
                    });
        } );
    }
}
