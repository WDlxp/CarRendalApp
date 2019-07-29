package com.example.carrendalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.adapters.CheckAdapter;
import com.example.carrendalapp.entity.CarOrder;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class UserCheckActivity extends AppCompatActivity {

    private ListView lv_checklist;
    //存储汽车类数组
    private List<CarOrder> carList = new ArrayList<>();

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取出SharedPreferences
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int manager = sp.getInt("manager", 1);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("查看进度");
        //如果是管理员切换标题
        if (manager == 0) {
            actionBarAndStatusBarUtil.setTitle("管理员审核");
        }
        actionBarAndStatusBarUtil.showBackButton();

        setContentView(R.layout.activity_user_check);
        lv_checklist = findViewById(R.id.lv_checklist);
        showCarcheck();//相当于查询
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            showCarcheck();
            Toast.makeText(UserCheckActivity.this, "刷新成功！", Toast.LENGTH_SHORT).show();
        } else if (itemId == android.R.id.home) {
            //点击返回按钮时关闭当前页面
            finish();
        }
        return true;
    }


    public void showCarcheck() {//将后台数据库表中的相关信息反馈到前端展示
        String name = "11";
        String carnumber = "1111111";
        String carbrand = "ww12245";
        String freetime = "2019-07-20-8:00 2019-07-30-9:00 2019-07-20-8:00 2019-07-30-9:00 2019-07-20-8:00 2019-07-30-9:00";
        String potel = "11111111111";
        int check = 1;
        CarOrder car = new CarOrder(name, carnumber, carbrand, freetime, null, potel, check, 4);//将信息存进实体类
        carList.add(car);//将实体类信息存进数组
        CheckAdapter checkAdapter = new CheckAdapter(//适配器
                UserCheckActivity.this,
                R.layout.layout_usercheck,
                carList);
        lv_checklist.setAdapter(checkAdapter);//将信息放入lv_list显示出来
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
//                        JSONArray array = object.getJSONArray("carlist");
//
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object1 = array.getJSONObject(i);
//                            String name = object1.getString("name");//获取车主姓名
//                            String carnumber = object1.getString("carNumber");//获取车牌号
//                            String carbrand = object1.getString("carBrand");//获取车辆型号
//                            String freetime = object1.getString("freeTime");//获取车辆空闲时间
//                            String potel = object1.getString("tel");//获取车主电话号码
//                            int check = object1.getInt("check");//获取车辆审核情况
//
//                            Car car = new Car(name, carnumber, carbrand, freetime, null, potel, check, 4);//将信息存进实体类
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
                CheckAdapter checkAdapter = new CheckAdapter(//适配器
                        UserCheckActivity.this,
                        R.layout.layout_usercheck,
                        carList);
                lv_checklist.setAdapter(checkAdapter);//将信息放入lv_list显示出来
            }
        }
    };


}
