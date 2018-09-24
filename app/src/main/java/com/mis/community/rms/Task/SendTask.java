package com.mis.community.rms.Task;

import android.os.AsyncTask;
import android.util.Log;

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

public class SendTask extends AsyncTask<String, Void, Void> {

    URL orders = null;
    BufferedWriter writer = null;
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    String line = "";

    @Override
    protected Void doInBackground(String... params) {

        try {
            orders = new URL("http://192.168.43.251/rms/orders.php");
            connection = (HttpURLConnection) orders.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestMethod("GET");
            OutputStream stream = connection.getOutputStream();

            writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

            String tablename = params[0];
            String item_name = params[1];
            String quantity = params[2];
            String price = params[3];

            if (tablename != null && item_name != null && quantity != null && price != null){

                String post_data = URLEncoder.encode("tablename","UTF-8")+"="+URLEncoder.encode(tablename,"UTF-8")+"&"+URLEncoder.encode("item_name","UTF-8")+"="+URLEncoder.encode(item_name,"UTF-8")+"&"+URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(quantity,"UTF-8")+"&"+URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8");
                writer.write(post_data);
                writer.flush();
                writer.close();
                stream.close();
            }
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String data = buffer.toString();

            Log.e("Error:", data);




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
