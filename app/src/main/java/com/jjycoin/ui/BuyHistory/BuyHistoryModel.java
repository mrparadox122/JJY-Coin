package com.jjycoin.ui.BuyHistory;

public class BuyHistoryModel {
    private String buyTime;
    private String buyDate;
    private String coins;
    private String paidAmount;
    private String valueThen;

    public String getBuyTime() {
        return buyTime;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public String getCoins() {
        return coins;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public String getValueThen() {
        return valueThen;
    }

    public BuyHistoryModel(String buyTime, String buyDate, String coins, String paidAmount, String valueThen) {
        this.buyTime = buyTime;
        this.buyDate = buyDate;
        this.coins = coins;
        this.paidAmount = paidAmount;
        this.valueThen = valueThen;
    }
}
