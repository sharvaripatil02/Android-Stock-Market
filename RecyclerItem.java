package com.example.patil.stockmarket;

/**
 * Created by patil on 3/3/2017.
 */
public class RecyclerItem {

    String Symbol;
    String CompanyName;
    Double LastTradePrice;
    Double ChangedPriceAmt;
    Double ChangedPricePercent;

    public RecyclerItem(String symbol, String companyName) {
        Symbol = symbol;
        CompanyName = companyName;
        //LastTradePrice = lastTradePrice;
        //ChangedPriceAmt = changedPriceAmt;
        //ChangedPricePercent = changedPricePercent;
    }

    public RecyclerItem() {
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public Double getLastTradePrice() {
        return LastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        LastTradePrice = lastTradePrice;
    }

    public Double getChangedPriceAmt() {
        return ChangedPriceAmt;
    }

    public void setChangedPriceAmt(Double changedPriceAmt) {
        ChangedPriceAmt = changedPriceAmt;
    }

    public Double getChangedPricePercent() {
        return ChangedPricePercent;
    }

    public void setChangedPricePercent(Double changedPricePercent) {
        ChangedPricePercent = changedPricePercent;
    }
}
