package com.mis.community.rms.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "foods.db";
    public static final String TB_NAME = "orders";

    String create_orders = "create table IF NOT EXISTS " + TB_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "product_id text," +
            "product_name text," +
            "quantity text," +
            "price text)";

    SQLiteDatabase database;
    Context context;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_orders);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addToOrder(String productId, String productName, String price, String quantity) {
        String query = "insert into " + TB_NAME + "(product_id, product_name, price, quantity) values " +
               "('" + productId + "','" + productName + "','" + price + "','" + quantity + "')";

        database.execSQL(query);
    }

    public Cursor FetchRecord(){
        Cursor c = null;

        String Query = "select * from " + TB_NAME;

        try {
            c = database.rawQuery(Query, null);
        }catch (SQLiteException e){
            Log.w("DB Error", e.getMessage());
        }

        return c;
    }

    public void cleanOrder(String ids, SQLiteDatabase database) {

     String selection = "product_id LIKE ?";
     String[] select_args = {ids};
     database.delete(TB_NAME, selection, select_args);

    }

    public void delData(String id, SQLiteDatabase db){
        String query = "DELETE FROM "+TB_NAME+ " WHERE product_id="+ id;
        db.execSQL(query);
    }


}
