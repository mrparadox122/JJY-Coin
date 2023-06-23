package com.jjycoin.Apis;

import android.util.Log;

import com.jjycoin.Variables;
import com.jjycoin.ui.BuyHistory.BuyHistoryModel;
import com.jjycoin.ui.SellHistory.SellHistoryModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SellHistoryApi {
    private final SellHistoryApiListner mlistner;

    public SellHistoryApi(SellHistoryApiListner mlistner)
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
            PutData putData = new PutData("https://jjyenterprises.in/_API/User_SellHistory.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("PutData", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        jsonObject.getString("status").equals("success");
                        if (jsonObject.getString("status").equals("success")) {
                            ArrayList<SellHistoryModel> sellHistoryModels = new ArrayList<>();

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject buyObject = dataArray.getJSONObject(i);

                                // Fetch the values from the JSON object and create a BuyHistoryModel object
                                String CustomerUPI = buyObject.getString("Customer_UPI");
                                String sellTime = buyObject.getString("Sell_Time");
                                String sellDate = buyObject.getString("Sell_Date");
                                String coins = buyObject.getString("Coins");
                                String RecivedAmount = buyObject.getString("Recived_Amount");
                                String valueThen = buyObject.getString("Value_Then");
                                String Status = buyObject.getString("Status");

                                SellHistoryModel sellHistoryModel = new SellHistoryModel(CustomerUPI ,sellTime, sellDate, coins, RecivedAmount, valueThen,Status);

                                // Add the BuyHistoryModel object to the list
                                sellHistoryModels.add(sellHistoryModel);
                            }

                            // Call the listener with the list of BuyHistoryModel objects
                            mlistner.OnComplete(sellHistoryModels);
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
    public interface SellHistoryApiListner
    {
        void OnComplete(ArrayList<SellHistoryModel> sellHistoryModels);
        void OnError(String error);
    }
}
