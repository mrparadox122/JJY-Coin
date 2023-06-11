package com.jjycoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjycoin.Apis.SellCoinApi;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class SellCoins extends AppCompatActivity implements SellCoinApi.SellCoinApiListner {

    AppCompatButton buycoin , SellcoinButton;
    EditText YouSellCoins;
    TextView YouGetSellValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_coins);
        getSupportActionBar().hide();
        buycoin = findViewById(R.id.buycoin);
        SellcoinButton = findViewById(R.id.SellcoinButton);
        YouSellCoins = findViewById(R.id.YouSellCoin);
        YouGetSellValue = findViewById(R.id.YouGetSellValue);
        buycoin.setOnClickListener(view -> {
            startActivity(new Intent(SellCoins.this,BuyCoin.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        SellLogic();
        SellcoinButton.setOnClickListener(v -> {
            if (!YouSellCoins.getText().toString().matches("") || YouSellCoins.getText().toString().length() > 1)
            {
                if (Float.parseFloat(YouGetSellValue.getText().toString()) >= 1)
                {
                    // Create an AlertDialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellCoins.this);
                    builder.setTitle("Enter UPI ID To Receive Amount");

                    // Create an EditText programmatically
                    final EditText editText = new EditText(SellCoins.this);
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    builder.setView(editText);

                    // Set the positive button (Submit)
                    builder.setPositiveButton("Submit", null); // Set null initially

                    // Create and show the dialog
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    // Override the positive button's onClick listener after showing the dialog
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the text from the EditText
                            String inputText = editText.getText().toString().trim();

                            // Check if the input text is not empty
                            if (!inputText.isEmpty()) {
                                SellCoinApi sellCoinApi = new SellCoinApi("pennding", YouSellCoins.getText().toString(),
                                        YouGetSellValue.getText().toString(), Variables.CoinValue, inputText, SellCoins.this);
                                sellCoinApi.CallAPi();

                                // Dismiss the dialog manually if the input is valid
                                dialog.dismiss();
                            } else {
                                // Show an error message or handle empty input
                                editText.setError("Enter UPI ID");
                            }
                        }
                    });

                    // Set the negative button (Cancel)
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle cancel button click
                            // ...
                        }
                    });
                }
                else
                {
                    PopupDialog.getInstance(this)
                            .setStyle(Styles.ALERT)
                            .setHeading("Uh-Oh")
                            .setDescription("Minumum 100rs Worth Of Coin Can Be Sold")
                            .setCancelable(false)
                            .showDialog(new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);

                                }
                            });
                }

            }
            else
            {
                YouSellCoins.setError("Enter Value");
            }
        });
    }

    public void SellLogic()
    {
        YouSellCoins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String pay = YouSellCoins.getText().toString();
                    double coinValue = Double.parseDouble(Variables.CoinValue); // Fixed coin value of 100

                    // Calculate the value based on the coin value and pay amount
                    double youGetValue = Double.parseDouble(pay) * coinValue;

                    // Set the calculated value as the text of YouGetValue
                    YouGetSellValue.setText(String.valueOf(youGetValue));
                }catch (Exception e)
                {
                    e.printStackTrace();
                    YouGetSellValue.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void OnComplete() {
        runOnUiThread( () -> {
            PopupDialog.getInstance(this)
                    .setStyle(Styles.ALERT)
                    .setHeading("Thanks")
                    .setDescription("Your Sell Request is under Process")
                    .setCancelable(false)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                            startActivity(new Intent(SellCoins.this,HomeScreen.class));
                            finish();
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