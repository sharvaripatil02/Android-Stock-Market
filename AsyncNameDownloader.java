package com.example.patil.stockmarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by patil on 3/5/2017.
 */


public class AsyncNameDownloader extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncNameLoaderTask";

    // private final String APIKEY = "82524f542314c1d07b9b2c71c7ddfe1992942d03";
    private MainActivity mainActivity = new MainActivity();
    private final String stockurl = "http://stocksearchapi.com/api/?api_key=82524f542314c1d07b9b2c71c7ddfe1992942d03&";
    private String inputsymboltext;
    HashMap<String, String> slist = new HashMap<String, String>();
    String stockchoice = null;

    public AsyncNameDownloader(MainActivity ma) {
        this.mainActivity = ma;
    }


    @Override
    protected String doInBackground(String... params) {

        Uri.Builder buildURL = Uri.parse(stockurl).buildUpon();
        buildURL.appendQueryParameter("search_text", params[0]);
        Log.d(TAG, "param[0]: " + params[0]);
        String urlToUse = buildURL.build().toString();
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }

            //System.out.print(result.toString());
            Log.d(TAG, "doInBackground: " + result.toString());

        } catch (FileNotFoundException f) {
            Log.e(TAG, "Exception in doInBackground:FileNotFoundException  ", f);

        } catch (Exception e) {
            Log.e(TAG, "Exception in doInBackground: ", e);
                   }

        Log.e(TAG, "InBackgroud: string builder "+result.toString());
        return result.toString();

    }


    private void parseJSON(String s) {

        try {
            JSONArray jobMain = new JSONArray(s);
            JSONObject jStock = null;

            int length = jobMain.length();
            Stock stockobj = new Stock();
            Log.e(TAG, "Length: jsonarray" +length);

            if (length==1) {
                jStock = jobMain.getJSONObject(0);
                stockchoice = jStock.getString("company_symbol");
                String companyName = jStock.getString("company_name");
                Log.d("NameDownloder", "beforecallAsyncDataDownloader: " + stockchoice);
                Log.d("NameDownloder", "beforecallAsyncDataDownloader: " + companyName);
                mainActivity.callAsyncDataDownloader(stockchoice, stockchoice + "-" + companyName);

            } else {
                for (int k = 0; k < length; k++) {
                    jStock = jobMain.getJSONObject(k);

                    stockobj.setStockSymbol(jStock.getString("company_symbol"));
                    stockobj.setCompanyName(jStock.getString("company_name"));
                    slist.put(stockobj.getStockSymbol(), stockobj.getStockSymbol() + "-" + stockobj.getCompanyName());
                }

                //ListJSON();
                showlist();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showlist() {

        int k = 0;
        final String[] str = new String[slist.size()];

        for (String arr : slist.values()) {
            str[k] = arr;
            k++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
        builder.setTitle("Stock Selection");
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] s = str[which].split("-");
                stockchoice = s[0];
                mainActivity.callAsyncDataDownloader(stockchoice, slist.get(stockchoice));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    protected void onPostExecute(String str) {
        Log.d(TAG, "onPostExecution: " + str);
        if (!str.isEmpty()) {
            parseJSON(str);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Symbol Not Found");
            builder.setMessage("Data for stock symbol");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}

