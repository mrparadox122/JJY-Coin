package com.jjycoin.ui.home;

import static android.content.ContentValues.TAG;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.jjycoin.Variables;
import com.jjycoin.databinding.FragmentHomeBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        UserDetailsApi();
        return root;
    }

    public void SetViews() {
        requireActivity().runOnUiThread(() -> {
            try {
                binding.CurrentValue.setText(Variables.CoinValue);
                binding.AvalableCoins.setText(Variables.coins);
                binding.ReffralBouns.setText(Variables.referralBonus);
                binding.reffralcodeHome.setText(Variables.referralCode);
                binding.reffralcodeHome.setSelected(true);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void UserDetailsApi() {
        Executor executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            String[] field = new String[2];
            field[0] = "User_ID";
            field[1] = "apiKey";
            String[] data = new String[2];
            data[0] = Variables.userId;
            data[1] = Variables.APiKey;
            PutData putData = new PutData("https://jjyenterprises.in/_API/User_Details.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
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
                        Variables.Name = jsonObject.getJSONObject("data").getString("Name");
                        Variables.coins = jsonObject.getJSONObject("data").getString("Coins");
                        Variables.CoinValue = CoinData.getString("Coin_Value");
                        SetViews();
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}