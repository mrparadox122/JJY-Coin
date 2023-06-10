package com.jjycoin.ui.SellHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;

import com.jjycoin.Apis.SellHistoryApi;
import com.jjycoin.R;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;

public class Sell_History extends AppCompatActivity implements SellHistoryApi.SellHistoryApiListner {

    RecyclerView SellcoinRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);
        getSupportActionBar().hide();
        SellcoinRecyclerView = findViewById(R.id.SellcoinRecyclerView);
        SellHistoryApi sellHistoryApi = new SellHistoryApi(this);
        sellHistoryApi.CallAPi();
    }

    @Override
    public void OnComplete(ArrayList<SellHistoryModel> sellHistoryModels) {
        runOnUiThread( () -> {
            SellcoinRecyclerView.setLayoutManager(new LinearLayoutManager(Sell_History.this));
            SellcoinRecyclerView.setAdapter(new SellHistoryAdapter(sellHistoryModels));
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