package com.jjycoin.Apis;

import android.util.Log;

import com.jjycoin.Variables;
import com.jjycoin.ui.BuyHistory.BuyHistoryModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BuyHistoryApi {
    private final BuyHistoryApi.BuyHistoryApiListner mlistner;

    public BuyHistoryApi(BuyHistoryApi.BuyHistoryApiListner mlistner)
    {
        this.mlistner = mlistner;
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
            String[] field = new String[2];
            field[0] = "apiKey";
            field[1] = "User_ID";
            //Creating array for data
            String[] data = new String[2];
            data[0] = Variables.APiKey;
            data[1] = Variables.userId;
            PutData putData = new PutData("https://jjyenterprises.in/_API/User_BuyHistory.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("PutData", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        jsonObject.getString("status").equals("success");
                        if (jsonObject.getString("status").equals("success")) {
                            ArrayList<BuyHistoryModel> buyHistoryModels = new ArrayList<>();

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject buyObject = dataArray.getJSONObject(i);

                                // Fetch the values from the JSON object and create a BuyHistoryModel object
                                String buyTime = buyObject.getString("Buy_Time");
                                String buyDate = buyObject.getString("Buy_Date");
                                String coins = buyObject.getString("Coins");
                                String paidAmount = buyObject.getString("Paid_Amount");
                                String valueThen = buyObject.getString("Value_Then");

                                BuyHistoryModel buyHistoryModel = new BuyHistoryModel(buyTime, buyDate, coins, paidAmount, valueThen);

                                // Add the BuyHistoryModel object to the list
                                buyHistoryModels.add(buyHistoryModel);
                            }

                            // Call the listener with the list of BuyHistoryModel objects
                            mlistner.OnComplete(buyHistoryModels);
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
    public interface BuyHistoryApiListner
    {
        void OnComplete(ArrayList<BuyHistoryModel> buyHistoryModels);
        void OnError(String error);
    }
}
