package com.jjycoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
                SellCoinApi sellCoinApi = new SellCoinApi("pennding" ,YouSellCoins.getText().toString(),YouGetSellValue.getText().toString(),Variables.CoinValue,this );
                sellCoinApi.CallAPi();
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