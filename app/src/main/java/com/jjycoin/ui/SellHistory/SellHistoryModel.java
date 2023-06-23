package com.jjycoin.ui.SellHistory;

public class SellHistoryModel {
    private final String sellTime;
    private final String sellDate;
    private final String coins;
    private final String RecivedAmount;
    private final String valueThen;

    public String getCustomerUPI() {
        return CustomerUPI;
    }

    private final String CustomerUPI;

    public String getStatus() {
        return Status;
    }

    public SellHistoryModel(String CustomerUPI, String sellTime, String sellDate, String coins, String RecivedAmount, String valueThen, String status) {
        this.CustomerUPI = CustomerUPI;
        this.sellTime = sellTime;
        this.sellDate = sellDate;
        this.coins = coins;
        this.RecivedAmount = RecivedAmount;
        this.valueThen = valueThen;
        Status = status;
    }

    private final String Status;

    public String getSellTime() {
        return sellTime;
    }

    public String getSellDate() {
        return sellDate;
    }

    public String getCoins() {
        return coins;
    }

    public String getRecivedAmount() {
        return RecivedAmount;
    }

    public String getValueThen() {
        return valueThen;
    }
}
