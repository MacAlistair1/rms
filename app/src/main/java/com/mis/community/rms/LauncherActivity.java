package com.mis.community.rms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class LauncherActivity extends Activity {

    ImageView imageView;
    Button btnStart;

    Animation animation;
    AnimationDrawable drawable;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        context = this;

        imageView = findViewById(R.id.imageView);
        btnStart = findViewById(R.id.btnStart);

        animation = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        btnStart.setAnimation(animation);

        imageView.setBackgroundResource(R.drawable.framechange);
        drawable = (AnimationDrawable) imageView.getBackground();
        drawable.start();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();

                if (info != null && info.isConnected() == true){
                    startActivity(new Intent(LauncherActivity.this, CategoryActivity.class));
                    finish();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            LauncherActivity.this.finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });


    }

}
