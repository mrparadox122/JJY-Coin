package com.jjycoin.ui.BuyHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;

import com.jjycoin.Apis.BuyHistoryApi;
import com.jjycoin.R;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;

public class Buy_Histroy extends AppCompatActivity implements BuyHistoryApi.BuyHistoryApiListner {

    RecyclerView BuyHistoryRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_histroy);
        getSupportActionBar().hide();
        BuyHistoryRecyclerView = findViewById(R.id.BuyHistoryRecyclerView);
        BuyHistoryApi buyHistoryApi = new BuyHistoryApi(this);
        buyHistoryApi.CallAPi();
    }

    @Override
    public void OnComplete(ArrayList<BuyHistoryModel> buyHistoryModels) {
        runOnUiThread( () ->
        {
            BuyHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            BuyHistoryRecyclerView.setAdapter(new BuyHistoryAdapter(buyHistoryModels));
        });

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