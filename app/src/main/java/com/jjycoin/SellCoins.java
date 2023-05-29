package com.jjycoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class SellCoins extends AppCompatActivity {

    AppCompatButton buycoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_coins);
        getSupportActionBar().hide();
        buycoin = findViewById(R.id.buycoin);
        buycoin.setOnClickListener(view -> {
            startActivity(new Intent(SellCoins.this,BuyCoin.class));
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
}