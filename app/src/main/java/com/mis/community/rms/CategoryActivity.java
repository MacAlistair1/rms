package com.mis.community.rms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mis.community.rms.Common.CommonPos;
import com.mis.community.rms.Task.MyTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CategoryActivity extends Activity {

    public static ListView cateList;
    public static ProgressDialog progressDialog;
    Context context;
    private String qt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        context = this;


        OneTime();

        cateList = findViewById(R.id.cateList);

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


        new MyTask(getApplicationContext()).execute();

        cateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checkConnection();


                CommonPos.setPos(position);




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

    public void onBackPressed(){
        startActivity(new Intent(this, OrdersActivity.class));
        finish();
    }

    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected() == true){
            startActivity(new Intent(CategoryActivity.this, MenuActivity.class));
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

                    CategoryActivity.this.finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void OneTime(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        boolean first = sharedPreferences.getBoolean("done", true);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (first){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(context);
            final  View view = inflater.inflate(R.layout.layout_onetime, null);

            builder.setView(view);
            builder.setCancelable(false);

            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText tableSymbol;

                    tableSymbol = view.findViewById(R.id.tableSymbol);

                    qt = tableSymbol.getText().toString();

                    if (qt.isEmpty() || qt.equals("")){
                        Toast.makeText(getApplicationContext(), "Symbol cannot be null.", Toast.LENGTH_LONG).show();
                    }else {
                        editor.putString("tableSymbol", qt);
                        editor.putBoolean("done", false);
                        editor.apply();
                    }

                }
            });
            AlertDialog ouster = builder.create();
            ouster.show();
        }

    }

}
