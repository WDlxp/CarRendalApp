package com.example.carrendalapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.adapters.CheckAdapter;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.Car;
import com.example.carrendalapp.entity.CarOrder;
import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.fragments.MemberFragment;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

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

/**
 * @author H
 */
public class UserCheckActivity extends AppCompatActivity {

    private ListView lv_checklist;
    //存储汽车类数组
    private List<CarOrder> carList = new ArrayList<>();
    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();
    //适配器
    private CheckAdapter checkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());

        //取出SharedPreferences
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int manager = sp.getInt("manager", 1);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("查看进度");
        setContentView(R.layout.activity_user_check);
        lv_checklist = findViewById(R.id.lv_checklist);


        //如果是管理员切换标题
        if (manager == 0) {
            actionBarAndStatusBarUtil.setTitle("管理员审核");
        }
        actionBarAndStatusBarUtil.showBackButton();


        //管理员查询所有未审核车辆信息
        if (manager == 0) {
            //适配器
            checkAdapter = new CheckAdapter(
                    UserCheckActivity.this,
                    R.layout.layout_usercheck,
                    carList
            );
            //设置适配器
            lv_checklist.setAdapter(checkAdapter);
            //后台获取数据
            new QueryAllCar().execute();
        }

        //用户查询该账户用户发布的车辆信息
        else if (manager == 1) {
            //适配器
            checkAdapter = new CheckAdapter(
                    UserCheckActivity.this,
                    R.layout.layout_usercheck,
                    carList
            );
            //设置适配器
           lv_checklist.setAdapter(checkAdapter);
            //后台获取数据
            new QueryAccountCar().execute();
        }
    }

    //设置刷新菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    //刷新
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int manager = sp.getInt("manager", 1);
        if (itemId == R.id.refresh) {
            //管理员刷新
            if (manager == 0) {
                checkAdapter = new CheckAdapter(
                        UserCheckActivity.this,
                        R.layout.layout_usercheck,
                        carList
                );
                //设置适配器
                lv_checklist.setAdapter(checkAdapter);
                new QueryAllCar().execute();
                Toast.makeText(UserCheckActivity.this, "用户数据刷新成功！", Toast.LENGTH_SHORT).show();
            }

            //用户刷新
            else {
                checkAdapter = new CheckAdapter(
                        UserCheckActivity.this,
                        R.layout.layout_usercheck,
                        carList
                );
                //设置适配器
                lv_checklist.setAdapter(checkAdapter);
                new QueryAccountCar().execute();
                Toast.makeText(UserCheckActivity.this, "审核数据刷新成功！", Toast.LENGTH_SHORT).show();
            }

        }else if (itemId==android.R.id.home){
            finish();
        }
        return true;
    }


    /**
     * 查询所有未审核的车辆信息
     */
    private class QueryAllCar extends AsyncTask<Void, Void, List<Car>> {

        @Override
        protected List<Car> doInBackground(Void... voids) {
            List<Car> list = null;
            try {
                URL url = new URL(UrlAddress.QUERY_CARCHECK_URL + "?operation=checkquery&checkState=" + 1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //获取输入流结合缓冲区
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                if (line != null) {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(line);
                    JSONArray data = jsonObject.getJSONArray("managercheck");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject carObject = data.getJSONObject(i);
                        list.add(new Car(
                                carObject.getString("name"),
                                carObject.getString("account"),
                                carObject.getString("carNumber"),
                                carObject.getString("carBand"),
                                carObject.getString("image"),
                                carObject.getString("freeTime"),
                                carObject.getInt("state"),
                                1
                        ));
                    }
                }
                bufferedReader.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Car> cars) {
            checkAdapter = new CheckAdapter(
                    UserCheckActivity.this,
                    R.layout.layout_usercheck,
                    cars
            );
            super.onPostExecute(cars);
            //将信息放入list显示出来
            lv_checklist.setAdapter(checkAdapter);
        }
    }


    /**
     * 用户个人数据查询任务
     */

    private class QueryAccountCar extends AsyncTask<Void, Void, List<Car>> {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String account = sp.getString("account", null);

        @Override
        protected List<Car> doInBackground(Void... voids) {
            List<Car> list = null;

            try {
                URL url = new URL(UrlAddress.QUERY_CARCHECK_URL + "?operation=usercheckquery&account=" + account);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //获取输入流结合缓冲区
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();

                if (line != null) {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(line);

                    JSONArray data = jsonObject.getJSONArray("usercheck");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject carObject = data.getJSONObject(i);
                        list.add(new Car(
                                carObject.getString("name"),
                                account,
                                carObject.getString("carNumber"),
                                carObject.getString("carBand"),
                                carObject.getString("image"),
                                carObject.getString("freeTime"),
                                carObject.getInt("state"),
                                carObject.getInt("checkState")
                        ));
                    }
                }
                bufferedReader.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Car> cars) {
            super.onPostExecute(cars);
            checkAdapter = new CheckAdapter(
                    UserCheckActivity.this,
                    R.layout.layout_usercheck,
                    cars
            );

            //将信息放入list显示出来
            lv_checklist.setAdapter(checkAdapter);

        }
    }
}
