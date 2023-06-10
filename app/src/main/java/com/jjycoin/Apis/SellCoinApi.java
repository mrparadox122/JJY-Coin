package com.jjycoin.Apis;

import android.util.Log;

import com.jjycoin.Variables;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SellCoinApi {

    private SellCoinApiListner mlistner;
    String status;
    String coins;
    String receivedAmount;
    String valueThen;

    public SellCoinApi(String status, String coins, String receivedAmount, String valueThen , SellCoinApiListner mlistner)
    {
        this.mlistner = mlistner;
        this.status = status;
        this.coins = coins;
        this.receivedAmount = receivedAmount;
        this.valueThen = valueThen;
    }

    public void CallAPi()
    {
        Executor executor = Executors.newCachedThreadPool();
        executor.execute(new HitApi());
    }

    public class HitApi implements Runnable
    {

        @Override
        public void run() {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[6];
            field[0] = "apiKey";
            field[1] = "userId";
            field[2] = "coins";
            field[3] = "receivedAmount";
            field[4] = "valueThen";
            field[5] = "status";
            //Creating array for data
            String[] data = new String[6];
            data[0] = Variables.APiKey;
            data[1] = Variables.userId;
            data[2] = coins;
            data[3] = receivedAmount;
            data[4] = valueThen;
            data[5] = status;
            PutData putData = new PutData("https://jjyenterprises.in/_API/Sell_Coins.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("PutData", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("status").equals("success"))
                        {
                            mlistner.OnComplete();
                        }
                        else
                            mlistner.OnError(jsonObject.getString("message"));
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            //End Write and Read data with URL
        }
    }
    public interface SellCoinApiListner
    {
        void OnComplete();
        void OnError(String error);
    }
}
