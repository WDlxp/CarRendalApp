package com.example.carrendalapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.carrendalapp.R;
import com.example.carrendalapp.adapters.HomeCarAdapter;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.Car;

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
 * A simple {@link Fragment} subclass.
 *
 * @author WD
 */
public class HomePageFragment extends Fragment {
    private ListView lvHomePage;
    private SwipeRefreshLayout srlHomePage;
    /**
     * 存储汽车类数组
     */
    private List<Car> carList = new ArrayList<>();

    /**
     * 主页数据的适配器
     */
    private HomeCarAdapter homeCarAdapter;


    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        lvHomePage = view.findViewById(R.id.lv_first);
        srlHomePage = view.findViewById(R.id.srl_home_page);

        //设置刷新控件的外观
        srlHomePage.setColorSchemeResources(android.R.color.holo_blue_bright);
        //刷新监听
        srlHomePage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new HomeQueryCarDataTask().execute();
            }
        });

        //添加头部的图片布局
        View imageHead=LayoutInflater.from(getContext()).inflate(R.layout.head_car_layout,null);
        //给ListView添加HeaderView
        lvHomePage.addHeaderView(imageHead);
        showFirstCar();
        return view;
    }

    private void showFirstCar() {
        //适配器
        homeCarAdapter = new HomeCarAdapter(
                getContext(),
                carList);
        //设置适配器
        lvHomePage.setAdapter(homeCarAdapter);
        //后台获取数据
        new HomeQueryCarDataTask().execute();
    }

    /**
     * 首页数据查询任务
     */
    private class HomeQueryCarDataTask extends AsyncTask<Void, Void, List<Car>> {

        @Override
        protected List<Car> doInBackground(Void... voids) {
            List<Car> list = null;
            try {
                URL url = new URL(UrlAddress.HOME_QUERY_CAR_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //获取输入流结合缓冲区
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                if (line != null) {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(line);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject carObject = data.getJSONObject(i);
                        list.add(new Car(
                                carObject.getString("account"),
                                carObject.getString("name"),
                                carObject.getString("carNumber"),
                                carObject.getString("carBand"),
                                carObject.getString("image"),
                                carObject.getString("freeTime"),
                                0,
                                0
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
            homeCarAdapter.updateListData(cars);
            //取消刷新
            srlHomePage.setRefreshing(false);
        }
    }
}
