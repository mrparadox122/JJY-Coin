package com.jjycoin.Apis;

import android.util.Log;

import com.jjycoin.Variables;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BuyCoinApi {

    private BuyCoinApiListner mlistner;
    String txnId;
    String txnRef;
    String status;
    String responseCode;
    String coins;
    String paidAmount;
    String valueThen;

    public BuyCoinApi( String txnId, String txnRef, String status, String responseCode, String coins, String paidAmount, String valueThen , BuyCoinApiListner mlistner)
    {
        this.mlistner = mlistner;
        this.txnId = txnId;
        this.txnRef = txnRef;
        this.status = status;
        this.responseCode = responseCode;
        this.coins = coins;
        this.paidAmount = paidAmount;
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
            String[] field = new String[9];
            field[0] = "apiKey";
            field[1] = "userId";
            field[2] = "coins";
            field[3] = "paidAmount";
            field[4] = "valueThen";
            field[5] = "txnId";
            field[6] = "txnRef";
            field[7] = "txnStatus";
            field[8] = "txnAppResponseCode";
            //Creating array for data
            String[] data = new String[9];
            data[0] = Variables.APiKey;
            data[1] = Variables.userId;
            data[2] = coins;
            data[3] = paidAmount;
            data[4] = valueThen;
            data[5] = txnId;
            data[6] = txnRef;
            data[7] = status;
            data[8] = responseCode;
            PutData putData = new PutData("https://jjyenterprises.in/_API/Buy_Coins.php", "POST", field, data);
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
    public interface BuyCoinApiListner
    {
        void OnComplete();
        void OnError(String error);
    }
}
