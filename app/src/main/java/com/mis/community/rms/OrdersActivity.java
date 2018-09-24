package com.mis.community.rms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.community.rms.Common.OrderCommon;
import com.mis.community.rms.Database.DBHelper;
import com.mis.community.rms.Task.SendTask;
import com.mis.community.rms.adapters.SQLiteListAdapter;

import java.util.ArrayList;

public class OrdersActivity extends Activity {

    Button orderBtn;
    Context context;
    ListView orderlist;
    TextView txtTotalPrice;

    DBHelper dbHelper;
    SQLiteDatabase SQLITEDATABASE;
    Cursor cursor;
    SQLiteListAdapter ListAdapter;
    int prices;
    ArrayList<Integer> product_id = new ArrayList<Integer>();
    ArrayList<String> product_name = new ArrayList<String>();
    ArrayList<String> product_price = new ArrayList<String>();
    ArrayList<String> product_quantity = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        context = this;




        orderlist = findViewById(R.id.orderlist);
        txtTotalPrice = findViewById(R.id.totalPrice);
        orderBtn = findViewById(R.id.orderbtn);

        dbHelper = new DBHelper(this);


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure you want to confirm this order?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Order Confirmed!", Toast.LENGTH_SHORT).show();
                        sendOrdersToMysql();
                        DeleteOrders();
                        finish();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                        DeleteOrders();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }

        });

        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    deleteOneItem(position+1);
            }
        });


    }



    @Override
    protected void onResume() {
        ShowSQLiteDBdata();
        super.onResume();
    }

    private void ShowSQLiteDBdata() {
        SQLITEDATABASE = dbHelper.getWritableDatabase();
        cursor = SQLITEDATABASE.rawQuery("SELECT * FROM orders", null);
        product_id.clear();
        product_name.clear();
        product_price.clear();
        product_quantity.clear();
        if (cursor.moveToFirst()) {
            do {

                product_id.add(cursor.getInt(cursor.getColumnIndex("product_id")));
                product_name.add(cursor.getString(cursor.getColumnIndex("product_name")));
                product_price.add(cursor.getString(cursor.getColumnIndex("price")));
                product_quantity.add(cursor.getString(cursor.getColumnIndex("quantity")));
            } while (cursor.moveToNext());
        }
        ListAdapter = new SQLiteListAdapter(OrdersActivity.this,
                product_id,
                product_name,
                product_price,
                product_quantity
        );
        orderlist.setAdapter(ListAdapter);
        cursor.close();

        for (int i = 0; i < product_id.size(); i++) {
            prices = prices + (Integer.parseInt(product_price.get(i)) * Integer.parseInt(product_quantity.get(i)));

        }
        txtTotalPrice.setText("Rs." + prices);

    }

    private void DeleteOrders() {
        dbHelper = new DBHelper(context);
        SQLITEDATABASE = dbHelper.getReadableDatabase();
        for (int i = 0; i < product_id.size(); i++) {
            int did = Integer.parseInt(String.valueOf(product_id.get(i)));
            dbHelper.cleanOrder(String.valueOf(did), SQLITEDATABASE);
        }

        Toast.makeText(getBaseContext(), "Order Confirmed", Toast.LENGTH_SHORT);
        Intent goto1 = new Intent(this, CategoryActivity.class);
        startActivity(goto1);
        finish();


    }
    private void deleteOneItem(int position) {

        int id = OrderCommon.getProduct_id();
        String name  = OrderCommon.getProduct_name();
        dbHelper = new DBHelper(context);
        SQLITEDATABASE = dbHelper.getReadableDatabase();
        dbHelper.delData(String.valueOf(position), SQLITEDATABASE);
        Intent refresh = new Intent(this, OrdersActivity.class);
        startActivity(refresh);
        finish();

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert!");
        builder.setMessage("Choose One:");
        builder.setCancelable(false);
        builder.setPositiveButton("Go To Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                checkConnection();


            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected() == true){
            startActivity(new Intent(OrdersActivity.this, CategoryActivity.class));
            finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Network Error");
            builder.setMessage("Error! No Internet Connection. Please ensure that your internet connection is working and retry.");
            builder.setCancelable(false);
            builder.setPositiveButton("Open Connection", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent settings = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                    startActivity(settings);
                }
            });
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    OrdersActivity.this.finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void sendOrdersToMysql(){
        dbHelper = new DBHelper(context);
        SQLITEDATABASE = dbHelper.getReadableDatabase();
        SharedPreferences preferences = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
        String tableName = preferences.getString("tableSymbol", "");
        for (int i = 0; i < product_id.size(); i++) {
            String item_name = String.valueOf(product_name.get(i));
            String quantity = String.valueOf(product_quantity.get(i));
            String price = String.valueOf(product_price.get(i));
            if (tableName != null){

                new SendTask().execute(tableName, item_name, quantity, price);
            }
        }
    }


}
