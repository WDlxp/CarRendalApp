package com.example.carrendalapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.carrendalapp.config.UrlAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author WD
 */
public class CancelAppointTask extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        String account = strings[0];
        String carNumber = strings[1];
        try {
            URL url = new URL(UrlAddress.CANCEL_ORDER_URL + "?operation=cancelOrder&account=" + account + "&carNumber=" + carNumber);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //获取输入流
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            JSONObject object = new JSONObject(line);
            //关闭
            inputStream.close();
            bufferedReader.close();
            return object.getInt("result");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (integer > 0) {
            Log.d("CancelOrder", "成功取消订单");
        } else {
            Log.d("CancelOrder", "成功取消订单");
        }
    }
}
