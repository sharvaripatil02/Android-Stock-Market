package com.example.patil.stockmarket;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by patil on 3/3/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "MyAdapter";

    private MainActivity mainAct;
    private List<Stock> listItem = new ArrayList<>();


    public MyAdapter(List<Stock> list, MainActivity mainAct) {
        this.listItem = list;
        this.mainAct = mainAct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Stock item = listItem.get(position);
        holder.StockSymbolText.setText(item.getStockSymbol());
        holder.CompanyNameText.setText(item.getCompanyName());
        holder.PriceText.setText(Double.toString(item.getPrice()));
        holder.PriceText.setText(Double.toString(item.getPriceChanged()));
        holder.Changed_diff.setText((Double.toString(item.getChanged_diff()) + "(" + Double.toString(item.getChanged_diff()) + "%)"));
        Double k = item.getPriceChanged();
        if (k < 0) {
            holder.Arrow.setBackgroundResource(R.drawable.ic_red_arrow);
            holder.StockSymbolText.setTextColor(Color.parseColor("#F5140B"));
            holder.CompanyNameText.setTextColor(Color.parseColor("#F5140B"));
            holder.PriceText.setTextColor(Color.parseColor("#F5140B"));
            holder.Changed_diff.setTextColor(Color.parseColor("#F5140B"));
        }else
        if (k > 0) {
            holder.Arrow.setBackgroundResource(R.drawable.ic_green_arrow1);
            holder.StockSymbolText.setTextColor(Color.parseColor("#319f06"));
            holder.CompanyNameText.setTextColor(Color.parseColor("#319f06"));
            holder.PriceText.setTextColor(Color.parseColor("#319f06"));
            holder.Changed_diff.setTextColor(Color.parseColor("#319f06"));
        }
        //no change
        else
        {
            holder.Arrow.setBackgroundResource(R.drawable.ic_arrow_white);
            holder.StockSymbolText.setTextColor(Color.parseColor("#FFFFFF"));
            holder.CompanyNameText.setTextColor(Color.parseColor("#FFFFFF"));
            holder.PriceText.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Changed_diff.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        if(listItem == null)
        {
            Log.d("MyAdapter","getItemCount is  NULL");
            return 0;
        }
        return listItem.size();
    }
}


