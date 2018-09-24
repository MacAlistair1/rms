package com.mis.community.rms;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.mis.community.rms.Interface.OnGetResponseListener;
import com.mis.community.rms.Task.GetResponseTask;
import com.mis.community.rms.adapters.MenuPagerAdapter;
import com.mis.community.rms.model.Menu;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    MenuPagerAdapter menuPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private List<Menu> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager =  findViewById(R.id.container);

        menuList = new ArrayList<>();

        final TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        new GetResponseTask(onGetResponseListener).execute("http://192.168.43.251/rms/category.php");
    }

    private OnGetResponseListener onGetResponseListener = new OnGetResponseListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onFinish(String response) {
            Log.e(MainActivity.class.getSimpleName(), "Response is: " + response);
            try{
                JSONArray jsonArrayCategory = new JSONArray(response);
                for(int i=0; i < jsonArrayCategory.length(); i++){
                    JSONObject object = jsonArrayCategory.getJSONObject(i);

                    Menu menu = new Menu();
                    menu.setId(object.getInt("cate_id"));
                    menu.setTitle(object.getString("cate_name"));

                    menuList.add(menu);


                    MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), menuList);

                    mViewPager = findViewById(R.id.container);
                    mViewPager.setAdapter(menuPagerAdapter);

                    TabLayout tabLayout = findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
