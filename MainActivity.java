package com.example.patil.stockmarket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, DialogInterface.OnClickListener {


    private TextView textViewsymbol;
    private EditText et;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    boolean check = false;
    private List<Stock> listItems = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private DBHandler dbhandler;
    private static String url1 = "http://www.marketwatch.com/investing/stock/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkConnectivity()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Data cannot be loaded");
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        } else {

            adapter = new MyAdapter(listItems, this);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            DBHandler.getInstance(this).DBset();
            swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);

            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!checkConnectivity()) {
                        NoConnection();
                    } else {
                        loadData();
                        swiper.setRefreshing(false);
                    }
                }
            });
            loadData();
        }
    }

    private void loadData() {
        ArrayList<Stock> slist = DBHandler.getInstance(this).loadStockDB();
        listItems.clear();
        Log.d("loadStocks", "size is : " + slist.size());
        if (slist.size() != 0) {
            for (int k = 0; k < slist.size(); k++) {
                String symbol = slist.get(k).getStockSymbol();
                String plusCompnayName = slist.get(k).getStockSymbol() + "-" + slist.get(k).getCompanyName();
                callAsyncDataDownloader(symbol, plusCompnayName);
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addStock:
                Toast.makeText(this, "add Stock", Toast.LENGTH_SHORT).show();
                if (!checkConnectivity()) {
                    NoConnection();
                } else {
                    onClickaddStock();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void NoConnection()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot Be Added Without A Network Connection");
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    private boolean checkConnectivity() {
        ConnectivityManager cmanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo n = cmanager.getActiveNetworkInfo();
        if (n != null && n.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public void onClickaddStock() {
        et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        new AlertDialog.Builder(this).setTitle("Add Stock").setView(et).setMessage("Enter Stock Name:").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (checkConnectivity()) {
                    check = true;
                    String input_string = et.getText().toString();
                    AsyncNameDownloader al = new AsyncNameDownloader(MainActivity.this);
                    al.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, input_string);
                } else {
                    NoConnection();
                }
            }
        }).setNegativeButton("Cancel", null).show();
         }


    @Override
    public boolean onLongClick(View view) {

        final int pos = recyclerView.getChildLayoutPosition(view);
        Stock s = listItems.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBHandler.getInstance(MainActivity.this).deleteStock(listItems.get(pos).getStockSymbol());
                listItems.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock " + listItems.get(pos).getStockSymbol() + "?");
        AlertDialog dialog = builder.create();
        dialog.show();


        return true;
    }


    public void callAsyncDataDownloader(String stockchoice, String pluscompanyName) {
        Log.d("MainActivity", "callAsyncDataDownloader: " + stockchoice);
        Log.d("MainActivity", "callAsyncDataDownloader: " + pluscompanyName);
        AsyncDataDownloader ad = new AsyncDataDownloader(MainActivity.this);
        ad.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, stockchoice, pluscompanyName);
    }


    public void stockadd(Stock stockobj) {

        if (check) {
            if (DBHandler.getInstance(this).fetchStock(stockobj.getStockSymbol())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Duplicate Stock");
                builder.setMessage("Stock Symbol " + stockobj.getStockSymbol() + " is already displayed");
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                listItems.add(0, stockobj);
                DBHandler.getInstance(this).addStock(stockobj);
                Collections.sort(listItems);
                adapter.notifyDataSetChanged();
            }
            check = false;
        } else {
            listItems.add(0, stockobj);
            DBHandler.getInstance(this).addStock(stockobj);
            Collections.sort(listItems);
            Log.d("MainAct","stockadd"+listItems.toString());
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {

        final int pos = recyclerView.getChildLayoutPosition(view);
        Stock s = listItems.get(pos);
        String key = listItems.get(pos).getStockSymbol();
        Toast.makeText(this, "Stock Details", Toast.LENGTH_SHORT).show();
        String url = url1+key;
        Uri uriUrl = Uri.parse(url);
        Log.d("Mactivity","URL selected"+uriUrl.toString());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}


