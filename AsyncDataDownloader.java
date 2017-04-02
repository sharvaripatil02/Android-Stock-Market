package com.example.patil.stockmarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by patil on 3/9/2017.
 */

public class AsyncDataDownloader extends AsyncTask<String, Integer, String> {


    private static final String TAG = "AsyncNameLoaderTask";

    private MainActivity mainActivity;
    private String urlToUse = "http://finance.google.com/finance/info?client=ig&q=";
    private String companyFullName = null;
    HashMap<String, String> slist = new HashMap<String, String>();

    public AsyncDataDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG, "param[0]: " + params[0]);

        urlToUse = urlToUse + params[0];
        String par = params[0];
        System.out.print(par);
        String[] pluscompanyName = params[1].split("-");
        companyFullName = pluscompanyName[1];

        Log.d(TAG, "doInBackground AsyncDataDownloader:urlToUse " + urlToUse);

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

            System.out.print(result.toString());
            Log.d(TAG, "doInBackground: " + result.toString());

        } catch (Exception e) {
            Log.e(TAG, "Exception in doInBackground: ", e);
            return null;
        }

        return result.toString();

    }


    private void parseJSON(String s) {
        try {

            JSONArray jobMain = new JSONArray(s);
            JSONObject jStock = null;
            jStock = jobMain.getJSONObject(0);

            Stock stockobj = new Stock();
            stockobj.setStockSymbol(jStock.getString("t"));
            stockobj.setPrice(Double.parseDouble(jStock.getString("l")));
            stockobj.setCompanyName(companyFullName);
            stockobj.setPriceChanged(Double.parseDouble(jStock.getString("c")));
            stockobj.setChanged_diff(Double.parseDouble(jStock.getString("cp")));

            slist.put(stockobj.getStockSymbol(), stockobj.getStockSymbol() + "-" + stockobj.getCompanyName());
            System.out.print("stockobject" + stockobj);
            mainActivity.stockadd(stockobj);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(String str) {

        if (!str.isEmpty()) {
            str = str.replace("//", "");
            System.out.print("" + str.toString());
            parseJSON(str);
        }

    }
}

