package com.mis.community.rms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.mis.community.rms.Common.CommonPos;
import com.mis.community.rms.Task.MenuTask;
import com.mis.community.rms.Task.MyTask;
import com.mis.community.rms.Task.OrderTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MenuActivity extends Activity {

    public static GridView menuList;
    public static ProgressDialog progressDialog;
    Context ctx;
    String id, name, price, quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ctx = this;


        menuList = findViewById(R.id.menuList);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.......");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new MenuTask(ctx).execute();


        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkConnection();
                CommonPos.setItemPos(position);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.refresh) {
            new MyTask(getApplicationContext()).execute();
        }
        return true;
    }

    public void quantityAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(ctx);
        final  View view = inflater.inflate(R.layout.layout_custom_quantity, null);

        builder.setView(view);
        builder.setCancelable(false);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText quantity;

                quantity = view.findViewById(R.id.quantity);

                String qt = quantity.getText().toString();

                if (qt.isEmpty() || qt.equals("") || qt.equals("0")){
                    Toast.makeText(getApplicationContext(), "Quantity is Null.", Toast.LENGTH_LONG).show();
                }else {

                    CommonPos.setCount(Integer.parseInt(qt));
                    new OrderTask(getApplicationContext()).execute();
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ouster = builder.create();
        ouster.show();


    }

    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected() == true){

            quantityAlert();

            Toast.makeText(ctx, "Added to Order List", Toast.LENGTH_SHORT).show();

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
                    MenuActivity.this.finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
