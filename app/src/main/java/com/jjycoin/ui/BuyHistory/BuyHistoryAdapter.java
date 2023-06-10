package com.jjycoin.ui.BuyHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjycoin.R;

import java.util.List;

public class BuyHistoryAdapter extends RecyclerView.Adapter<BuyHistoryAdapter.ViewHolder> {

    private List<BuyHistoryModel> buyHistoryList;

    public BuyHistoryAdapter(List<BuyHistoryModel> buyHistoryList) {
        this.buyHistoryList = buyHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_history_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyHistoryModel buyHistoryModel = buyHistoryList.get(position);

        holder.coinValueThenTextView.setText(buyHistoryModel.getValueThen());
        holder.paidAmountTextView.setText(buyHistoryModel.getPaidAmount());
        holder.buyTimeTextView.setText(buyHistoryModel.getBuyTime());
        holder.buyDateTextView.setText(buyHistoryModel.getBuyDate());
        holder.coinsTextView.setText(buyHistoryModel.getCoins());
    }

    @Override
    public int getItemCount() {
        return buyHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coinValueThenTextView;
        TextView paidAmountTextView;
        TextView buyTimeTextView;
        TextView buyDateTextView;
        TextView coinsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinValueThenTextView = itemView.findViewById(R.id.CoinValueThen);
            paidAmountTextView = itemView.findViewById(R.id.PaidAmount);
            buyTimeTextView = itemView.findViewById(R.id.BuyTime);
            buyDateTextView = itemView.findViewById(R.id.BuyDate);
            coinsTextView = itemView.findViewById(R.id.Coins);
        }
    }
}



