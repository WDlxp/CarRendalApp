package com.example.carrendalapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrendalapp.R;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.Car;
import com.example.carrendalapp.tasks.DownloadImageTask;
import com.example.carrendalapp.views.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CheckAdapter extends ArrayAdapter {


    public CheckAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Car car = (Car) getItem(position);

       // Log.d("fffff",car.getAccount());

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_usercheck, null);
        CircleImageView img = convertView.findViewById(R.id.img_cportrait);
        TextView tv_cname = convertView.findViewById(R.id.tv_cname);
        TextView tv_ccarnumber = convertView.findViewById(R.id.tv_ccarnumber);
        TextView tv_ccarbrand = convertView.findViewById(R.id.tv_ccarbrand);
        TextView tv_cfreetime = convertView.findViewById(R.id.tv_cfreetime);
        final TextView tv_usercheck = convertView.findViewById(R.id.tv_usercheck);


        //如果图片不为空的时候下载头像
        //获取头像
        if (!"null".equals(car.getImage()) && car.getImage() != null) {
            new DownloadImageTask(img).execute(car.getImage());
        }

        //用户信息展示
        tv_cname.setText(car.getName());
        tv_ccarnumber.setText("车牌：" + car.getCarNumber());
        tv_ccarbrand.setText("车型：" + car.getCarBand());
        tv_cfreetime.setText("空闲时间：" + "\n" + car.getFreeTime());

        //获取SharedPreferences
        SharedPreferences sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
        //获取账号的是否管理员
        int manager = sp.getInt("manager", 1);

        //管理员
        if (manager == 0) {
            //管理员审核
            tv_usercheck.setText("审核");
            //点击审核
            tv_usercheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("fffff",car.getCarNumber());
                    final String carnumbercheck = car.getCarNumber();
                    car.setCheck(0);
                    new Checkcar().execute(carnumbercheck);
                    tv_usercheck.setText("已审核");

                    //提示审核通过
                    Toast.makeText(getContext(), "审核成功！", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //用户查看审核情况
        else if (manager == 1) {
            //1表示还未审核
            if (car.getCheck() == 1) {
                tv_usercheck.setText("待审核");
            }
            //0表示审核已通过
            else if (car.getCheck() == 0) {
                tv_usercheck.setText("审核通过");
            }
        }
        return convertView;
    }


    /**
     * 更新后台对应account的checkState为0
     */

    private class Checkcar extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String carnumber = strings[0];
            URL url = null;
            try {
                url = new URL(UrlAddress.Update_CHECK_URL + "?operation=check&carNumber=" + carnumber);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                return object.getInt("checkclick");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}
