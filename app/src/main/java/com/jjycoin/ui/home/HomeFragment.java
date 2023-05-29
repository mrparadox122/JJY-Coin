package com.jjycoin.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjycoin.Variables;
import com.jjycoin.databinding.FragmentHomeBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        UserDetailsApi();


        return root;
    }

    public void SetViews()
    {
        binding.CurrentValue.setText(Variables.CoinValue);
        binding.AvalableCoins.setText(Variables.coins);
    }

    public void UserDetailsApi()
    {
        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "User_ID";
                field[1] = "apiKey";
                //Creating array for data
                String[] data = new String[2];
                data[0] = Variables.userId;
                data[1] = "s4fRbJ5w6Klm2Nc3";
                PutData putData = new PutData("https://jjyenterprises.in/_API/User_Details.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject CoinData = jsonObject.getJSONObject("coins");
                            Variables.userId = jsonObject.getJSONObject("data").getString("User_ID");
                            Variables.phoneNumber = jsonObject.getJSONObject("data").getString("PhoneNumber");
                            Variables.accountNumber = jsonObject.getJSONObject("data").getString("AccountNumber");
                            Variables.dateOfBirth = jsonObject.getJSONObject("data").getString("DateOfBirth");
                            Variables.referralCode = jsonObject.getJSONObject("data").getString("ReffralCode");
                            Variables.referralBonus = jsonObject.getJSONObject("data").getString("ReffralBouns");
                            Variables.profilePic = jsonObject.getJSONObject("data").getString("ProfilePic");
                            Variables.coins = jsonObject.getJSONObject("data").getString("Coins");
                            Variables.CoinValue = CoinData.getString("Coin_Value");
                            SetViews();
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString() );
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}