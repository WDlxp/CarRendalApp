package com.example.carrendalapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.carrendalapp.MyAppointmentActivity;
import com.example.carrendalapp.R;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.Car;
import com.example.carrendalapp.entity.CarOrder;
import com.example.carrendalapp.entity.Order;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author WD
 */
public class HomeCarAdapter extends BaseAdapter {
    private CircleImageView circleImageView;
    private TextView tvName, tvCarNumber, tvCarBrand, tvFreeTime;
    private Button btnAppoint;

    private Context mContext;
    private List<Car> carList;
    private final static String TAG = "Appoint";

    public HomeCarAdapter(Context mContext, List<Car> carList) {
        this.mContext = mContext;
        this.carList = carList;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int i) {
        return carList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //获取当前项的数据
        final Car currentCar = carList.get(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_myappoint, null);
        //findViewById
        findViews(view);
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取布局
                final LayoutInflater inflater = LayoutInflater.from(mContext);
                //获取自定义的View
                View dialogView = inflater.inflate(R.layout.layout_dialog, null);
                //绑定控件
                final DatePicker dpStart = dialogView.findViewById(R.id.dp_start);
                final DatePicker dpFinish = dialogView.findViewById(R.id.dp_finish);
                final TimePicker tpStart = dialogView.findViewById(R.id.tp_start);
                final TimePicker tpFinish = dialogView.findViewById(R.id.tp_finish);
                final EditText edTel = dialogView.findViewById(R.id.edt_dtel);

                final Button btnQuit = dialogView.findViewById(R.id.btn_quit);
                final Button btnNow = dialogView.findViewById(R.id.btn_now);

                //设置开始时间为24小时显示格式
                tpStart.setIs24HourView(true);
                //设置结束时间为24小时显示格式
                tpFinish.setIs24HourView(true);

                //创建AlertDialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog alertDialog = builder
                        .setView(dialogView)
                        .create();//创建一个对话框
                //设置透明背景显示圆角
                Window window = alertDialog.getWindow();
                assert window != null;
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //展示弹窗
                alertDialog.show();

                //取消按钮直接关闭对话框
                btnQuit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                //确认按钮进行预约
                btnNow.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        String[] str = currentCar.getFreeTime().split(" ");//一个空格分隔每一次空闲时间
//                    str = new String[]{"2019-07-27-03:00", "2019-07-28-08:00"};//测试
                        List<String> free = new ArrayList<>();
                        //将时间段加入ArrayList中
                        Collections.addAll(free, str);

                        for (int a = 0; a < free.size(); a++) {
                            Log.d(TAG, "空闲时间为：" + free.get(a));
                        }

                        //获取开始和结束的日期、时间
                        final String startDate = dpStart.getYear() + "-" + changeFormat(dpStart.getMonth() + 1) + "-" + changeFormat(dpStart.getDayOfMonth());
                        final String startTime = changeFormat(tpStart.getHour()) + ":" + changeFormat(tpStart.getMinute());
                        final String finishDate = dpFinish.getYear() + "-" + changeFormat(dpFinish.getMonth() + 1) + "-" + changeFormat(dpFinish.getDayOfMonth());
                        final String finishTime = changeFormat(tpFinish.getHour()) + ":" + changeFormat(tpFinish.getMinute());
                        final String tel = edTel.getText().toString();

                        final String appointStart = startDate + "-" + startTime;
                        final String appointFinish = finishDate + "-" + finishTime;

                        String appointTime = startDate + " " + startTime + "-" + finishDate + " " + finishTime;
                        final String[] freeNow = {""};

                        Log.d(TAG, "预约开始时间：" + appointStart);
                        Log.d(TAG, "预约结束时间：" + appointFinish);
                        Log.d(TAG, "电话号码：" + tel);


                        boolean flagStart = false;
                        boolean flagFinish = false;
                        int poStart = -1;
                        int poFinish = -1;
                        int countStart = -1;
                        int countFinish = -1;

                        Log.d(TAG, "开始判断时间：" + countStart + "结束判断时间：" + countFinish);
                        //遍历预约时间，保证预约时间合理
                        for (int f = 0; f < appointStart.length(); f++) {
                            if (appointStart.charAt(f) > appointFinish.charAt(f)) {
                                poStart = -2;
                                poFinish = -2;
                                break;
                            } else if (appointStart.charAt(f) < appointFinish.charAt(f) || appointStart.charAt(f) == appointFinish.charAt(f)) {
                            } else if (f == appointStart.length() - 1) {
                                break;
                            }
                        }

                        for (int a = 0; a < free.size(); a++) {//遍历空闲时间，找到当前预约开始时间所处位置2a
                            Log.d(TAG, "a=：" + a);
                            if (flagStart) {
                                Log.d(TAG, "开始为：" + countStart);
                                break;
                            } else if (free.get(2 * a) != null) {//这些都是开始时间
                                for (int b = 0; b < appointStart.length(); b++) {
                                    //一旦先遍历到 预约开始时间 有一位大于 某空闲开始时间，则预约开始时间有效，否则继续判断
                                    if (appointStart.charAt(b) > free.get(2 * a).charAt(b)) {
                                        countStart = 2 * a;
                                        Log.d(TAG, "start成功赋值2a：" + countStart);
                                        flagStart = true;
                                        break;
                                    }
                                    //如果预约开始时间小于某空闲开始时间，则将 countstart = 0，后续提示预约时间超出空闲时间
                                    else if (appointStart.charAt(b) < free.get(2 * a).charAt(b)) {
                                        Log.d(TAG, "start成功赋值-1：" + countStart);
                                        flagStart = true;
                                        break;
                                    }

                                    //如果预约开始时间等于某空闲开始时间，则继续判断下一位
                                    else if (appointStart.charAt(b) == free.get(2 * a).charAt(b)) {
                                        Log.d(TAG, "start=：" + 1);
                                    }

                                    //如果预约开始时间最后全部等于某空闲开始时间，则预约时间有效
                                    else if (b == appointStart.length() - 1) {
                                        countStart = 2 * a;
                                        Log.d(TAG, "start成功赋值==2a：" + countStart);
                                        flagStart = true;
                                        break;
                                    }

                                }
                            } else if (free.get(2 * a) == null) {
                                break;
                            }
                        }

                        for (int a = 0; a < free.size(); a++) {//遍历空闲时间，找到当前预约结束时间所处位置
                            Log.d(TAG, "a=：" + a);
                            if (flagFinish) {
                                Log.d(TAG, "结束为：" + countStart);
                                break;
                            } else if (free.get(2 * a + 1) != null) {//这些都是结束时间
                                for (int c = 0; c < appointFinish.length(); c++) {
                                    //一旦先遍历到 预约结束时间 有一位小于 某空闲结束时间，则预约时间结束有效，否则继续判断
                                    if (appointFinish.charAt(c) < free.get(2 * a + 1).charAt(c)) {
                                        countFinish = 2 * a + 1;
                                        Log.d(TAG, "finish成功赋值2a+1：" + countFinish);
                                        flagFinish = true;
                                        break;
                                    }
                                    //如果预约结束时间大于某空闲结束时间，则将 countfinish = 0，后续提示预约时间超出空闲时间
                                    else if (appointFinish.charAt(c) > free.get(2 * a + 1).charAt(c)) {
                                        Log.d(TAG, "finish成功赋值-1：" + countFinish);
                                        flagFinish = true;
                                        break;
                                    }

                                    //如果预约结束时间等于某空闲结束时间，则继续判断下一位
                                    else if (appointFinish.charAt(c) == free.get(2 * a + 1).charAt(c)) {
                                        Log.d(TAG, "finish=：" + 1);
                                    }
                                    //如果预约开始时间最后全部等于某空闲开始时间，则预约时间有效
                                    else if (c == appointFinish.length() - 1) {
                                        countFinish = 2 * a + 1;
                                        Log.d(TAG, "成功赋值2a+1=：" + countFinish);
                                        flagFinish = true;
                                        break;
                                    }
                                }
                            } else if (free.get(2 * a + 1) == null) {
                                break;
                            }
                        }

                        if (edTel.getText().length() != 11) {
                            Toast.makeText(mContext, "您的电话号码不存在，请重新填写！", Toast.LENGTH_SHORT).show();
                            edTel.requestFocus();
                            Log.d(TAG, "电话号码长度为：" + edTel.getText().length() + "");
                        } else {
                            if (countStart == countFinish - 1 && countStart != -1 && countFinish != -1 && poStart != -2) {

                                Log.d(TAG, TAG + countStart + " " + countFinish);
                                free.add(countStart + 1, appointStart);//在2a后面插入预约开始时间
                                free.add(countFinish, appointFinish);//在2a+1前面插入预约结束时间

                                for (int d = 0; d < free.size(); d++) { //计算现在的空闲时间
                                    freeNow[0] += free.get(d) + " ";
                                    Log.d(TAG, "目前空闲时间：" + freeNow[0]);
                                }
                                Log.d("Appoint", tel);
                                Log.d("Appoint", appointTime);

                                final String finalTel = tel;

                                //获取账号数据
                                SharedPreferences sp = mContext.getSharedPreferences("data", MODE_PRIVATE);
                                String account = sp.getString("account", null);
                                Order carOrder = new Order(account, currentCar.getCarNumber(), startDate, startTime, finishDate, finishTime, 1);
                                new InsertOrderTask().execute(carOrder);
                                new UpdateFreeTimeTask().execute(currentCar.getCarNumber(), freeNow[0]);
//                            Intent intent = new Intent(mContext, MyAppointmentActivity.class);
//                            mContext.startActivity(intent);
                                //弹窗消失
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(mContext, "您的预约时间可能不合理" + "\n" + "或者超出车主空闲时间" + "\n" + "请重新预约哦！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        //初始化视图
        initViews(currentCar);
        return view;
    }

    private void initViews(Car car) {
        if (!"null".equals(car.getImage()) && car.getImage() != null) {
            new DownloadImageTask(circleImageView).execute(car.getImage());
        }
        tvName.setText(car.getName());
        tvCarNumber.setText("车牌：" + car.getCarNumber());
        tvCarBrand.setText("车型：" + car.getCarBand());
        tvFreeTime.setText("空闲时间：" + car.getFreeTime());
    }

    private void findViews(View view) {
        circleImageView = view.findViewById(R.id.iv_car);
        tvName = view.findViewById(R.id.tv_name);
        tvCarNumber = view.findViewById(R.id.tv_car_number);
        tvCarBrand = view.findViewById(R.id.tv_car_brand);
        tvFreeTime = view.findViewById(R.id.tv_free_time);
        btnAppoint = view.findViewById(R.id.btn_appoint);

        btnAppoint = view.findViewById(R.id.btn_appoint);
    }

    public void updateListData(List<Car> cars) {
        carList = cars;
        notifyDataSetChanged();
    }

    /**
     * 修改格式将，例如7-->07
     *
     * @param time 时间
     * @return 修改后的时间
     */
    private String changeFormat(int time) {
        String result = time + "";
        if (time < 10) {
            result = "0" + time;
        }
        return result;
    }

    /**
     * 上传订单的任务
     */
    private class InsertOrderTask extends AsyncTask<Order, Void, Integer> {

        @Override
        protected Integer doInBackground(Order... orders) {
            Order order = orders[0];
            URL url = null;
            try {
                url = new URL(UrlAddress.INSERT_ORDER_URL + "?operation=insert&account="
                        + order.getAccount() + "&carNumber=" + order.getCarNumber() + "&startDate=" + order.getStartDate() + "&startTime=" + order.getStartTime()
                        + "&finishDate=" + order.getFinishDate() + "&finishTime=" + order.getFinishTime());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = br.readLine();
                JSONObject object = new JSONObject(line);
                return object.getInt("i");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Toast.makeText(mContext, "预定成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "预定失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新空闲时间
     */
    private class UpdateFreeTimeTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String carNumber = strings[0];
            String newFreeTime = strings[1];
            URL url = null;
            try {
                url = new URL(UrlAddress.UPDATE_FREE_TIME_URL + "?operation=updateFreeTime&carNumber="
                        + carNumber + "&newFreeTime=" + newFreeTime);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = br.readLine();
                JSONObject object = new JSONObject(line);
                return object.getInt("i");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Toast.makeText(mContext, "空闲时间更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "空闲时间更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
