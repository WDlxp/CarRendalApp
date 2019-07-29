package com.example.carrendalapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.carrendalapp.entity.Car;
import com.example.carrendalapp.entity.CarOrder;
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
import java.util.List;

/**
 * @author WD
 */
public class HomeCarAdapter extends BaseAdapter implements View.OnClickListener {
    private CircleImageView circleImageView;
    private TextView tvName, tvCarNumber, tvCarBrand, tvFreeTime;
    private Button btnAppoint;

    private Context mContext;
    private List<Car> carList;
    private Car currentCar;

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
        currentCar = carList.get(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_myappoint, null);
        //findViewById
        findViews(view);
        btnAppoint.setOnClickListener(this);
        //初始化视图
        initViews(currentCar);
        return view;
    }

    private void initViews(Car car) {
        if (!car.getImage().equals("null") && car.getImage() != null) {
            new DownloadImageTask(circleImageView).execute(car.getImage());
        }
        tvName.setText(car.getName());
        tvCarNumber.setText(car.getCarNumber());
        tvCarBrand.setText(car.getCarBand());
        tvFreeTime.setText(car.getFreeTime());
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_appoint) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View dialogView = inflater.inflate(R.layout.layout_dialog, null);
            final DatePicker dpStart = dialogView.findViewById(R.id.dp_start);
            final DatePicker dpFinish = dialogView.findViewById(R.id.dp_finish);
            final TimePicker tpStart = dialogView.findViewById(R.id.tp_start);
            final TimePicker tpFinish = dialogView.findViewById(R.id.tp_finish);
            final EditText edTel = dialogView.findViewById(R.id.edt_dtel);

            //设置开始时间为24小时显示格式
            tpStart.setIs24HourView(true);
            //设置结束时间为24小时显示格式
            tpFinish.setIs24HourView(true);
            final Button btnQuit = dialogView.findViewById(R.id.btn_quit);
            final Button btnNow = dialogView.findViewById(R.id.btn_now);

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
                    str = new String[]{"2019-07-27-03:00", "2019-07-28-08:00"};//测试
                    ArrayList<String> free = new ArrayList();

                    for (int e = 0; e < str.length; e++) {
                        free.add(str[e]);
                    }


                    for (int a = 0; a < free.size(); a++) {
                        Log.d("fffff", "空闲时间为：" + free.get(a));
                    }

                    final String startdate = dpStart.getYear() + "-" + ChangeFormat(dpStart.getMonth() + 1) + "-" + ChangeFormat(dpStart.getDayOfMonth());
                    final String starttime = ChangeFormat(tpStart.getHour()) + ":" + ChangeFormat(tpStart.getMinute());
                    final String finishdate = dpFinish.getYear() + "-" + ChangeFormat(dpFinish.getMonth() + 1) + "-" + ChangeFormat(dpFinish.getDayOfMonth());
                    final String finishtime = ChangeFormat(tpFinish.getHour()) + ":" + ChangeFormat(tpFinish.getMinute());
                    final String tel = edTel.getText() + "";


                    final String appointstart = startdate + "-" + starttime;
                    final String appointfinish = finishdate + "-" + finishtime;

                    String appointime = startdate + " " + starttime + "-" + finishdate + " " + finishtime;
                    final String[] freenow = {""};

                    Log.d("fffff", "预约开始时间：" + appointstart);
                    Log.d("fffff", "预约结束时间：" + appointfinish);
                    Log.d("fffff", "电话号码：" + tel);


                    boolean flagstart = false;
                    boolean flagfinish = false;
                    int postart = -1;
                    int pofinish = -1;
                    int countstart = -1;
                    int countfinish = -1;

                    Log.d("fffff", "开始判断时间：" + countstart + "结束判断时间：" + countfinish);
                    for (int f = 0; f < appointstart.length(); f++) {//遍历预约时间，保证预约时间合理
                        if (appointstart.charAt(f) > appointfinish.charAt(f)) {
                            postart = -2;
                            pofinish = -2;
                            break;
                        } else if (appointstart.charAt(f) < appointfinish.charAt(f)) {//
                            continue;
                        } else if (appointstart.charAt(f) == appointfinish.charAt(f)) {
                            continue;
                        } else if (f == appointstart.length() - 1) {
                            postart = -1;
                            pofinish = -1;
                            break;
                        }
                    }

                    for (int a = 0; a < free.size(); a++) {//遍历空闲时间，找到当前预约开始时间所处位置2a
                        Log.d("fffff", "a=：" + a);
                        if (flagstart == true) {
                            Log.d("fffff", "开始为：" + countstart);
                            break;
                        } else if (free.get(2 * a) != null) {//这些都是开始时间
                            for (int b = 0; b < appointstart.length(); b++) {

                                //一旦先遍历到 预约开始时间 有一位大于 某空闲开始时间，则预约开始时间有效，否则继续判断
                                if (appointstart.charAt(b) > free.get(2 * a).charAt(b)) {
                                    countstart = 2 * a;
                                    Log.d("fffff", "start成功赋值2a：" + countstart);
                                    flagstart = true;
                                    break;
                                }

                                //如果预约开始时间小于某空闲开始时间，则将 countstart = 0，后续提示预约时间超出空闲时间
                                else if (appointstart.charAt(b) < free.get(2 * a).charAt(b)) {
                                    countstart = -1;
                                    Log.d("fffff", "start成功赋值-1：" + countstart);
                                    flagstart = true;
                                    break;
                                }

                                //如果预约开始时间等于某空闲开始时间，则继续判断下一位
                                else if (appointstart.charAt(b) == free.get(2 * a).charAt(b)) {
                                    Log.d("fffff", "start=：" + 1);
                                    continue;
                                }

                                //如果预约开始时间最后全部等于某空闲开始时间，则预约时间有效
                                else if (b == appointstart.length() - 1) {
                                    countstart = 2 * a;
                                    Log.d("fffff", "start成功赋值==2a：" + countstart);
                                    flagstart = true;
                                    break;
                                }

                            }
                        } else if (free.get(2 * a) == null) {
                            countstart = -1;
                            break;
                        }
                    }

                    for (int a = 0; a < free.size(); a++) {//遍历空闲时间，找到当前预约结束时间所处位置
                        Log.d("fffff", "a=：" + a);
                        if (flagfinish == true) {
                            Log.d("fffff", "结束为：" + countstart);
                            break;
                        } else if (free.get(2 * a + 1) != null) {//这些都是结束时间
                            for (int c = 0; c < appointfinish.length(); c++) {

                                //一旦先遍历到 预约结束时间 有一位小于 某空闲结束时间，则预约时间结束有效，否则继续判断
                                if (appointfinish.charAt(c) < free.get(2 * a + 1).charAt(c)) {
                                    countfinish = 2 * a + 1;
                                    Log.d("fffff", "finish成功赋值2a+1：" + countfinish);
                                    flagfinish = true;
                                    break;
                                }

                                //如果预约结束时间大于某空闲结束时间，则将 countfinish = 0，后续提示预约时间超出空闲时间
                                else if (appointfinish.charAt(c) > free.get(2 * a + 1).charAt(c)) {
                                    countfinish = -1;
                                    Log.d("fffff", "finish成功赋值-1：" + countfinish);
                                    flagfinish = true;
                                    break;
                                }

                                //如果预约结束时间等于某空闲结束时间，则继续判断下一位
                                else if (appointfinish.charAt(c) == free.get(2 * a + 1).charAt(c)) {
                                    Log.d("fffff", "finish=：" + 1);
                                    continue;
                                }
                                //如果预约开始时间最后全部等于某空闲开始时间，则预约时间有效
                                else if (c == appointfinish.length() - 1) {
                                    countfinish = 2 * a + 1;
                                    Log.d("fffff", "成功赋值2a+1=：" + countfinish);
                                    flagfinish = true;
                                    break;
                                }
                            }
                        } else if (free.get(2 * a + 1) == null) {
                            countfinish = -1;
                            break;
                        }
                    }

                    if (edTel.getText().length() != 11) {
                        Toast.makeText(mContext, "您的电话号码不存在，请重新填写！", Toast.LENGTH_SHORT).show();
                        Log.d("fffff", "电话号码长度为：" + edTel.getText().length() + "");
                        alertDialog.dismiss();
                    } else {
                        if ((countstart == countfinish - 1) && (countstart != -1) &&
                                (countfinish != -1) && (postart != -2) && (pofinish != -2)) {

                            Log.d("fffff", "他们是：" + countstart + " " + countfinish);
                            free.add(countstart + 1, appointstart);//在2a后面插入预约开始时间
                            free.add(countfinish, appointfinish);//在2a+1前面插入预约结束时间

                            for (int d = 0; d < free.size(); d++) { //计算现在的空闲时间
                                freenow[0] += free.get(d) + " ";
                                Log.d("fffff", "目前空闲时间：" + freenow[0]);
                            }
                            Log.d("Appoint", tel);
                            Log.d("Appoint", appointime);
//                            carappoint.setTel(tel);//将预约数据存入实体类
//                            carappoint.setPointTime(appointime);
                            final String finalTel = tel;
                            new Thread() {//将数据存进数据库
                                @Override
                                public void run() {
                                    super.run();

                                    URL url = null;
                                    try {
                                        url = new URL("http://27.154.144.77:8080/0722Task/NewsServlet1?tel="
                                                + finalTel + "&startDate=" + startdate + "&starttime=" + starttime
                                                + "&finishdate=" + finishdate + "&finishtime=" + finishtime + "&status=" + 2 + "&freetime=" + freenow[0]);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        //Log.d("URL", url.toString());
                                        InputStream inputStream = connection.getInputStream();
                                        //Log.d("URL", "2");
                                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                                        //Log.d("URL", "3");
                                        String line = br.readLine();
                                        //Log.d("URL", "4");
                                        JSONObject object = new JSONObject(line);
                                        int i = object.getInt("i");
                                        if (i > 0) {
                                            Log.d("URL", i + "将数据添加到后台数据库成功");
                                        }

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }.start();

                            notifyDataSetChanged(); //通知数据适配器更新视图

                            Intent intent = new Intent(mContext, MyAppointmentActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "预约成功！", Toast.LENGTH_SHORT).show();
                            //(countstart != countfinish - 1) || (countstart == -1) || (countfinish == -1)|| (postart == -2)|| (pofinish == -2)
                        } else {
                            Toast.makeText(mContext, "您的预约时间可能不合理" + "\n" + "或者超出车主空闲时间" + "\n" + "请重新预约哦！", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                }
            });
        }
    }

    public String ChangeFormat(int time) {
        String result = time + "";
        if (time < 10) {
            result = "0" + time;
        }
        return result;
    }
}
