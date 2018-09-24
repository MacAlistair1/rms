package com.mis.community.rms.Task;

import android.content.Context;
import android.os.AsyncTask;

import com.mis.community.rms.Common.CommonPos;
import com.mis.community.rms.Common.OrderCommon;
import com.mis.community.rms.Database.DBHelper;
import com.mis.community.rms.model.OrderModel;

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
import java.util.List;

public class OrderTask extends AsyncTask<Void, Void, List<OrderModel>> {

    String line = "";
    Context context;
    private String id, name, price, quantity;

    public OrderTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<OrderModel> doInBackground(Void... voids) {

        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;


        try {

            String catePos = String.valueOf(CommonPos.getPos() + 1);
            String itemPos = String.valueOf(CommonPos.getItemPos() + 1);
            String count = String.valueOf(CommonPos.getCount());


            url = new URL("http://192.168.43.251/rms/allitems.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream outputStream = connection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("cate_id", "UTF-8") + "=" + URLEncoder.encode(catePos, "UTF-8") + "&" + URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(itemPos, "UTF-8");
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


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                OrderCommon.setProduct_id(Integer.parseInt(itemPos));
                OrderCommon.setProduct_name(jsonObject.getString("item_name"));
                OrderCommon.setPrice(jsonObject.getString("item_price"));
                OrderCommon.setQuantity(String.valueOf(count));

            }

            id = String.valueOf(OrderCommon.getProduct_id());
            name = OrderCommon.getProduct_name();
            price = OrderCommon.getPrice();
            quantity = OrderCommon.getQuantity();

            if (String.valueOf(id) != null && name != null && price != null && quantity != null) {
                new DBHelper(context).addToOrder(String.valueOf(id), name, price, quantity);
            }


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
    protected void onPostExecute(List<OrderModel> result) {
        super.onPostExecute(result);

    }
}
