package com.jjycoin.ui.SellHistory;

public class SellHistoryModel {
    private String sellTime;
    private String sellDate;
    private String coins;
    private String RecivedAmount;
    private String valueThen;

    public String getStatus() {
        return Status;
    }

    public SellHistoryModel(String sellTime, String sellDate, String coins, String RecivedAmount, String valueThen, String status) {
        this.sellTime = sellTime;
        this.sellDate = sellDate;
        this.coins = coins;
        this.RecivedAmount = RecivedAmount;
        this.valueThen = valueThen;
        Status = status;
    }

    private String Status;

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
