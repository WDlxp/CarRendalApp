package com.example.carrendalapp.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carrendalapp.R;
import com.example.carrendalapp.adapters.AppointAdapter;
import com.example.carrendalapp.entity.CarOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author WD
 */
public class AllFragment extends Fragment {
    private ListView lv_alllist;
    private List<CarOrder> carList = new ArrayList<>();//存储汽车类数组

    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        lv_alllist = view.findViewById(R.id.lv_alllist);
        showCar();//相当于查询
        // Inflate the layout for this fragment
        return view;
    }

    public void showCar() {//将后台数据库表中的相关信息反馈到前端展示
        String name ="name";//获取车主姓名
        String carnumber = "carNumber";//获取车牌号
        String carbrand = "carBrand";//获取车辆型号
        String freetime = "freeTime";//获取车辆空闲时间

        String startdate = "startDate";//获取预约开始日期
        String starttime = "startTime";//获取预约开始时间点
        String finishdate = "finishDate";//获取预约结束日期
        String finishtime = "finishTime";//获取预约结束时间点
        String potel = "tel";//获取电话号码

        String potime = startdate + "-" + starttime + " " + finishdate + "-" + finishtime;//根据预约时间信息得到预约时间段
        CarOrder car = new CarOrder(name, carnumber, carbrand, freetime, potime, potel, 3, 3);//将信息存进实体类
        carList.add(car);//将实体类信息存进数组
        AppointAdapter appointAdapter = new AppointAdapter(//适配器
                getContext(),
                R.layout.layout_myappoint,
                carList);
        lv_alllist.setAdapter(appointAdapter);//将信息放入lv_list显示出来
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                URL url = null;//填写接口ip地址
//                try {
//                    url = new URL("http://27.154.144.77:8080/0722Task/ShoeServlet");//后台搭建后修改
//
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    InputStream inputStream = connection.getInputStream();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//                    String line = br.readLine();
//                    StringBuffer sb = new StringBuffer();
//
//                    while (line != null) {
//                        JSONObject object = new JSONObject(line);
//                        JSONArray array = object.getJSONArray("orderlist");
//
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object1 = array.getJSONObject(i);
//                            String name = object1.getString("name");//获取车主姓名
//                            String carnumber = object1.getString("carNumber");//获取车牌号
//                            String carbrand = object1.getString("carBrand");//获取车辆型号
//                            String freetime = object1.getString("freeTime");//获取车辆空闲时间
//
//                            String startdate = object1.getString("startDate");//获取预约开始日期
//                            String starttime = object1.getString("startTime");//获取预约开始时间点
//                            String finishdate = object1.getString("finishDate");//获取预约结束日期
//                            String finishtime = object1.getString("finishTime");//获取预约结束时间点
//                            String potel = object1.getString("tel");//获取电话号码
//
//                            String potime = startdate + "-" + starttime + " " + finishdate + "-" + finishtime;//根据预约时间信息得到预约时间段
//                            Car car = new Car(name, carnumber, carbrand, freetime, potime, potel, 3, 3);//将信息存进实体类
//                            carList.add(car);//将实体类信息存进数组
//                        }
//
//                        line = br.readLine();
//
//                    }
//                    Message message = new Message();
//                    message.what = 1;
//                    message.obj = carList;
//                    mhandler.sendMessage(message);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                HttpURLConnection connection = null;
//            }
//        }.start();
    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                AppointAdapter appointAdapter = new AppointAdapter(//适配器
                        getContext(),
                        R.layout.layout_myappoint,
                        carList);
                lv_alllist.setAdapter(appointAdapter);//将信息放入lv_list显示出来
            }
        }
    };

}
