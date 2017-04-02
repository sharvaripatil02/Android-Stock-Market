package com.example.patil.stockmarket;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by patil on 3/3/2017.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView StockSymbolText;
    public TextView CompanyNameText;
    public TextView Arrow;
    public TextView PriceText;
    public TextView Changed_diff;

    public MyViewHolder(View view) {
        super(view);
        StockSymbolText = (TextView) view.findViewById(R.id.StockSymbolText);
        CompanyNameText = (TextView) view.findViewById(R.id.CompanyNameText);
        PriceText = (TextView) view.findViewById(R.id.PriceText);
        Arrow = (TextView) view.findViewById(R.id.arrow);
        Changed_diff = (TextView) view.findViewById(R.id.Changed_diff);
    }


}
