package com.jjycoin.ui.SellHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjycoin.R;

import java.util.List;

public class SellHistoryAdapter extends RecyclerView.Adapter<SellHistoryAdapter.ViewHolder> {

    private List<SellHistoryModel> sellHistoryModels;

    public SellHistoryAdapter(List<SellHistoryModel> sellHistoryModels) {
        this.sellHistoryModels = sellHistoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_history_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellHistoryModel sellHistoryModel = sellHistoryModels.get(position);

        holder.AmountRecivedUpi.setText(sellHistoryModel.getCustomerUPI());

        holder.coinValueThenTextView.setText(sellHistoryModel.getValueThen());
        holder.RecivedAmount.setText(sellHistoryModel.getRecivedAmount());
        holder.sellTimeTextView.setText(sellHistoryModel.getSellTime());
        holder.sellDateTextView.setText(sellHistoryModel.getSellDate());
        holder.coinsTextView.setText(sellHistoryModel.getCoins());
        holder.Status.setText(sellHistoryModel.getStatus());
    }

    @Override
    public int getItemCount() {
        return sellHistoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coinValueThenTextView;
        TextView RecivedAmount;
        TextView sellTimeTextView;
        TextView sellDateTextView;
        TextView coinsTextView;
        TextView Status;
        TextView AmountRecivedUpi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinValueThenTextView = itemView.findViewById(R.id.CoinValueThenSell);
            RecivedAmount = itemView.findViewById(R.id.RecivedAmount);
            sellTimeTextView = itemView.findViewById(R.id.SellTime);
            sellDateTextView = itemView.findViewById(R.id.SellDate);
            coinsTextView = itemView.findViewById(R.id.SoldCoins);
            Status = itemView.findViewById(R.id.Status);
            AmountRecivedUpi = itemView.findViewById(R.id.AmountRecivedUpi);
        }
    }
}



