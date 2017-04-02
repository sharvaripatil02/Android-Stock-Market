package com.example.patil.stockmarket;

import java.io.Serializable;

/**
 * Created by patil on 3/5/2017.
 */

public class Stock implements Serializable, Comparable {
    private String StockSymbol;
    private String CompanyName;
    private Double Price;
    private Double PriceChanged;
    private Double Changed_diff;


    public String getStockSymbol() {
        return StockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.StockSymbol = stockSymbol;
    }


    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        this.CompanyName = companyName;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        this.Price = price;
    }

    public Double getPriceChanged() {
        return PriceChanged;
    }

    public void setPriceChanged(Double priceChanged) {
        this.PriceChanged = priceChanged;
    }

    public Double getChanged_diff() {
        return Changed_diff;
    }

    public void setChanged_diff(Double Changed_diff) {
        this.Changed_diff = Changed_diff;
    }


    @Override
    public int compareTo(Object o) {
        return this.StockSymbol.compareTo(((Stock) o).StockSymbol);
    }
}
