package com.mis.community.rms.Task;

import android.content.Context;
import android.os.AsyncTask;

import com.mis.community.rms.Common.CommonPos;
import com.mis.community.rms.MenuActivity;
import com.mis.community.rms.R;
import com.mis.community.rms.adapters.MenuItemsAdapter;
import com.mis.community.rms.model.MenuItemsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MenuTask extends AsyncTask<Void, Void, List<MenuItemsModel>> {

    String line = "";
    Context context;

    public MenuTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<MenuItemsModel> doInBackground(Void... voids) {

        BufferedReader reader = null;
        BufferedWriter writer = null;
        URL url1 = null;

        try {

            String value = String.valueOf(CommonPos.getPos() + 1);

            url1 = new URL("http://192.168.43.251/rms/allitems.php");
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream outputStream = connection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("cate_id","UTF-8")+"="+URLEncoder.encode(value,"UTF-8")+"&"+URLEncoder.encode("item_id","UTF-8")+"="+URLEncoder.encode("null","UTF-8");
            writer.write(post_data);
            writer.flush();
            writer.close();
            outputStream.close();


            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String jsonData = buffer.toString();


            JSONArray array = new JSONArray(jsonData);

            List<MenuItemsModel> menuItemsModelsList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);


                    MenuItemsModel menuItemsModel = new MenuItemsModel();
                    menuItemsModel.setId(jsonObject.getInt("items_id"));
                    menuItemsModel.setName(jsonObject.getString("item_name"));
                    menuItemsModel.setImage(jsonObject.getString("item_image"));
                    menuItemsModel.setPrice(jsonObject.getString("item_price"));

                    menuItemsModelsList.add(menuItemsModel);


            }





            return menuItemsModelsList;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(List<MenuItemsModel> result) {
        super.onPostExecute(result);
        MenuActivity.progressDialog.dismiss();
        if (result != null){
            MenuItemsAdapter adapter = new MenuItemsAdapter(context, R.layout.menu_items, result);
            MenuActivity.menuList.setAdapter(adapter);
        }


    }


}
