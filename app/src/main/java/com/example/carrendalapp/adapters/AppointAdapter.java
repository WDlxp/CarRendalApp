package com.example.carrendalapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.carrendalapp.MyAppointmentActivity;
import com.example.carrendalapp.R;
import com.example.carrendalapp.entity.CarOrder;

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
public class AppointAdapter extends ArrayAdapter {
    public AppointAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }


    private String changeFormat(int time) {
        String result = time + "";
        if (time < 10) {
            result = "0" + time;
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        final CarOrder carappoint = (CarOrder) getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_myappoint, null);
        ImageView img = convertView.findViewById(R.id.iv_car);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_carnumber = convertView.findViewById(R.id.tv_car_number);
        TextView tv_carbrand = convertView.findViewById(R.id.tv_car_brand);
        TextView tv_freetime = convertView.findViewById(R.id.tv_free_time);
        TextView tv_potime = convertView.findViewById(R.id.tv_potime);
        TextView tv_potel = convertView.findViewById(R.id.tv_potel);
        Button btn_status = convertView.findViewById(R.id.btn_appoint);

        carappoint.setStatus(3);//测试

        if (carappoint.getStatus() == 0) {
            btn_status.setText("已结束");

        } else if (carappoint.getStatus() == 1) {
            btn_status.setText("取消");

            btn_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //等后台搭建再写取消按钮
                }
            });

        } else if (carappoint.getStatus() == 2) {
            btn_status.setText("预约中");

        } else if (carappoint.getStatus() == 3) {
            btn_status.setText("预约");

//            final View appoinview = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog, null);

            btn_status.setOnClickListener(new View.OnClickListener() {//点击预约按钮进行预约
                @Override
                public void onClick(View view) {//创建对话框 进行预约时间和电话选择
                    final LayoutInflater inflater = LayoutInflater.from(getContext());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    view = inflater.inflate(R.layout.layout_dialog, null);
                    final DatePicker dp_start = view.findViewById(R.id.dp_start);
                    final DatePicker dp_finish = view.findViewById(R.id.dp_finish);
                    final TimePicker tp_start = view.findViewById(R.id.tp_start);
                    final TimePicker tp_finish = view.findViewById(R.id.tp_finish);
                    final EditText edt_dtel = view.findViewById(R.id.edt_dtel);

                    tp_start.setIs24HourView(true);//设置开始时间为24小时显示格式
                    tp_finish.setIs24HourView(true);//设置结束时间为24小时显示格式
                    final Button btn_quit = view.findViewById(R.id.btn_quit);
                    final Button btn_now = view.findViewById(R.id.btn_now);
                    final AlertDialog alertDialog = builder
                            .setView(view)
                            .create();//创建一个对话框
                    alertDialog.show();

                    btn_quit.setOnClickListener(new View.OnClickListener() {//取消按钮直接关闭对话框
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });


                    btn_now.setOnClickListener(new View.OnClickListener() {//确认按钮进行预约
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view) {
                            carappoint.setStatus(1);//此时从预约->取消
                            String[] str = carappoint.getFreeTime().split(" ");//一个空格分隔每一次空闲时间
                            str = new String[]{"2019-07-27-03:00", "2019-07-28-08:00"};//测试
                            ArrayList<String> free = new ArrayList();

                            for (int e = 0; e < str.length; e++) {
                                free.add(str[e]);
                            }


                            for (int a = 0; a < free.size(); a++) {
                                Log.d("fffff", "空闲时间为：" + free.get(a));
                            }

                            final String startdate = dp_start.getYear() + "-" + changeFormat(dp_start.getMonth() + 1) + "-" + changeFormat(dp_start.getDayOfMonth());
                            final String starttime = changeFormat(tp_start.getHour()) + ":" + changeFormat(tp_start.getMinute());
                            final String finishdate = dp_finish.getYear() + "-" + changeFormat(dp_finish.getMonth() + 1) + "-" + changeFormat(dp_finish.getDayOfMonth());
                            final String finishtime = changeFormat(tp_finish.getHour()) + ":" + changeFormat(tp_finish.getMinute());
                            final String tel = edt_dtel.getText() + "";


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

                            if (edt_dtel.getText().length() != 11) {
                                Toast.makeText(getContext(), "您的电话号码不存在，请重新填写！", Toast.LENGTH_SHORT).show();
                                Log.d("fffff", "电话号码长度为：" + edt_dtel.getText().length() + "");
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
                                    carappoint.setTel(tel);//将预约数据存入实体类
                                    carappoint.setPointTime(appointime);
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

                                    Intent intent = new Intent(getContext(), MyAppointmentActivity.class);
                                    getContext().startActivity(intent);
                                    Toast.makeText(getContext(), "预约成功！", Toast.LENGTH_SHORT).show();
                                } else {//(countstart != countfinish - 1) || (countstart == -1) || (countfinish == -1)|| (postart == -2)|| (pofinish == -2)
                                    Toast.makeText(getContext(), "您的预约时间可能不合理" + "\n" + "或者超出车主空闲时间" + "\n" + "请重新预约哦！", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        }
                    });

                }
            });
        }

        tv_name.setText(carappoint.getName());
        tv_carnumber.setText("车牌:" + carappoint.getCarNumber());
        tv_carbrand.setText("车型:" + carappoint.getCarBand());
        tv_freetime.setText("空闲时间:" + "\n" + carappoint.getFreeTime());
        LinearLayout linearLayout = convertView.findViewById(R.id.ll);
        if (carappoint.getStatus() != 3) {
            linearLayout.setVisibility(View.VISIBLE);
            tv_potime.setText("预约时间:" + carappoint.getPointTime());
            tv_potel.setText("电话号码:" + carappoint.getTel());
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        return convertView;
    }
}
