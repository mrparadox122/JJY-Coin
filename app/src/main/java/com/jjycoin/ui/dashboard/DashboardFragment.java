package com.jjycoin.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjycoin.BuyCoin;
import com.jjycoin.R;
import com.jjycoin.ReceiveCoin;
import com.jjycoin.SellCoins;
import com.jjycoin.SendCoins;
import com.jjycoin.Variables;
import com.jjycoin.databinding.FragmentDashboardBinding;
import com.jjycoin.ui.BuyHistory.Buy_Histroy;
import com.jjycoin.ui.SellHistory.Sell_History;

public class DashboardFragment extends Fragment {

    @Nullable
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SetClicks(root);
        return root;
    }

    public void SetClicks(@NonNull View root)
    {
        RelativeLayout recivecoins = root.findViewById(R.id.recivecoinRelativeLayout);
        recivecoins.setOnClickListener(view -> {
            startActivity(new Intent(root.getContext(), ReceiveCoin.class));
        });
        RelativeLayout sendcoins = root.findViewById(R.id.sendcoinRelativeLayout);
        sendcoins.setOnClickListener(view -> {
            startActivity(new Intent(root.getContext(), SendCoins.class));
        });
        RelativeLayout buycoins = root.findViewById(R.id.buycoinRelativeLayout);
        buycoins.setOnClickListener(view -> {
            startActivity(new Intent(root.getContext(), BuyCoin.class));
        });
        RelativeLayout sellcoins = root.findViewById(R.id.sellcoinRelativeLayout);
        sellcoins.setOnClickListener(view -> {
            startActivity(new Intent(root.getContext(), SellCoins.class));
        });
        RelativeLayout SellHistory = root.findViewById(R.id.sellhistory);
        SellHistory.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), Sell_History.class));
        });

        binding.BuyHistoryRelativelayout.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(),Buy_Histroy.class));
        });
        binding.CurrentValueCoinPage.setText(Variables.CoinValue);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}