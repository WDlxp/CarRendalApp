package com.example.carrendalapp.fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.carrendalapp.BaseLazyLoadFragment;
import com.example.carrendalapp.R;
import com.example.carrendalapp.adapters.AppointOrderAdapter;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.AppointOrder;
import com.example.carrendalapp.entity.Order;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author WD
 */
public class FinishedFragment extends BaseLazyLoadFragment {
    private ListView lvAllList;
    //存储汽车类数组
    private List<AppointOrder> carList = new ArrayList<>();

    private AppointOrderAdapter appointOrderAdapter;

    public FinishedFragment() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_all;
    }

    @Override
    protected void lazyLoad() {
        lvAllList = getView().findViewById(R.id.lv_all_list);
        showCar();//相当于查询
    }

    public void showCar() {//将后台数据库表中的相关信息反馈到前端展示
        //适配器
        appointOrderAdapter = new AppointOrderAdapter(getContext(), carList, 2);
        //将信息放入lv_list显示出来
        lvAllList.setAdapter(appointOrderAdapter);

        //取出SharedPreferences
        SharedPreferences sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
        String account = sp.getString("account", null);
        new QueryOrderByStateTask().execute(account);
    }


    /**
     * 查询预约中的订单信息
     */
    private class QueryOrderByStateTask extends AsyncTask<String, Void, List<AppointOrder>> {

        @Override
        protected List<AppointOrder> doInBackground(String... strings) {
            String account = strings[0];
            List<AppointOrder> list = null;
            try {
                URL url = new URL(UrlAddress.QUERY_ORDER_URL + "?operation=queryByState&account=" + account + "&state=0");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //获取输入流结合缓冲区
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                if (line != null) {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(line);
                    Log.d("TAG1", line);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject appointOrderObject = data.getJSONObject(i);
                        Order order = new Order(
                                appointOrderObject.getString("account"),
                                appointOrderObject.getString("carNumber"),
                                appointOrderObject.getString("startDate"),
                                appointOrderObject.getString("startTime"),
                                appointOrderObject.getString("finishDate"),
                                appointOrderObject.getString("finishTime"),
                                appointOrderObject.getInt("state")
                        );
                        AppointOrder appointOrder = new AppointOrder(
                                appointOrderObject.getString("name"),
                                appointOrderObject.getString("tel"),
                                appointOrderObject.getString("carBrand"),
                                appointOrderObject.getString("image"),
                                appointOrderObject.getString("freeTime"),
                                order
                        );
                        list.add(appointOrder);
                    }
                }
                bufferedReader.close();
                inputStream.close();
                return list;
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
        protected void onPostExecute(List<AppointOrder> appointOrders) {
            super.onPostExecute(appointOrders);
            if (appointOrders != null) {
                carList = appointOrders;
                appointOrderAdapter.updateAppointOrderData(carList);
            }
        }
    }
}
