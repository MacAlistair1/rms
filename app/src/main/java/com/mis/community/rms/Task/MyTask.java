package com.mis.community.rms.Task;

import android.content.Context;
import android.os.AsyncTask;

import com.mis.community.rms.CategoryActivity;
import com.mis.community.rms.R;
import com.mis.community.rms.adapters.CategoryAdapter;
import com.mis.community.rms.model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyTask extends AsyncTask<Void, Void, List<CategoryModel>> {

    String line = "";
    Context context;


    public MyTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<CategoryModel> doInBackground(Void... voids) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;


        try {

            URL url = new URL("http://192.168.43.251/rms/category.php");

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String jsonData = buffer.toString();


            JSONArray array = new JSONArray(jsonData);

            List<CategoryModel> categoryModelList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(jsonObject.getInt("cate_id"));
                categoryModel.setName(jsonObject.getString("cate_name"));
                categoryModel.setImage(jsonObject.getString("cate_image"));


                categoryModelList.add(categoryModel);
            }

            return categoryModelList;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<CategoryModel> result) {
        super.onPostExecute(result);
        CategoryActivity.progressDialog.dismiss();
        CategoryAdapter adapter = new CategoryAdapter(context, R.layout.rows, result);
        CategoryActivity.cateList.setAdapter(adapter);


    }


}
