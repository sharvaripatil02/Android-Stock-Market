package com.example.patil.stockmarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION;

/**
 * Created by patil on 3/3/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";

    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase dbobject;
    public static DBHandler dbHandler;

    private static final String DATABASE_NAME = "StockMarketDB";
    private static final String SYMBOL_NAME = "Symbol";
    private static final String COMPANY_NAME = "CompanyName";
    private static final String TABLE_NAME = "StockMarketTable";


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL_NAME + " TEXT not null unique," +
                    COMPANY_NAME + " TEXT not null)";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbobject = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler:DONE");
    }

    //IF DB DOESN'T EXIST
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: Making New DB");
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


    public ArrayList<Stock> loadStockDB() {

        Log.d(TAG, "loadStock: LOADING Stock DATA FROM DB");
        ArrayList<Stock> slist = new ArrayList<>();

        Cursor cursor = dbobject.query(
                TABLE_NAME,  // The table to query
                new String[]{SYMBOL_NAME, COMPANY_NAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String c = cursor.getString(1);
                Stock st = new Stock();
                st.setStockSymbol(symbol);
                st.setCompanyName(c);
                slist.add(st);

                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadStock: Loaded STOCK DATA FROM DB");

        return slist;
    }


    public void addStock(Stock stockObj) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL_NAME, stockObj.getStockSymbol());
        values.put(COMPANY_NAME, stockObj.getCompanyName());

        deleteStock(stockObj.getStockSymbol());

        long key = dbobject.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addStock: " + key);
    }


    public void deleteStock(String symbol) {
        Log.d(TAG, "deletStock: " + symbol);
        int cnt = dbobject.delete(TABLE_NAME, SYMBOL_NAME + " = ?", new String[]{symbol});
        Log.d(TAG, "deleteStock: " + cnt);
    }


    public static DBHandler getInstance(Context context) {
        if (dbHandler == null)
            dbHandler = new DBHandler(context);
        return dbHandler;
    }

    public void DBset() {
        dbobject = getWritableDatabase();
    }

    public boolean fetchStock(String symbolName) {
        Cursor cursor = dbobject.rawQuery("select * from " + TABLE_NAME + " where " + SYMBOL_NAME + " = '" + symbolName + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
