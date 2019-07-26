package com.example.carrendalapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReleaseActivity extends AppCompatActivity {

    TextView tvTitle, tvCarNumber, tvCarBand, tvStartDate, tvStartTime, tvFinishDate, tvFinishTime;
    EditText edtCarNumber, edtCarBand;
    Button btn;
    DatePicker dpStartDate, dpFinishDate;
    TimePicker tpStartTime, tpFinishTime;

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil=new ActionBarAndStatusBarUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("我的预约");
        actionBarAndStatusBarUtil.showBackButton();

        findViews();


        //设置为24小时显示格式
        tpStartTime.setIs24HourView(true);
        //设置为24小时显示格式
        tpFinishTime.setIs24HourView(true);


        btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                final String operate = "change";
                final String carNumber = edtCarNumber.getText().toString();
                final String carBand = edtCarBand.getText().toString();
                final String startDate = dpStartDate.getYear() + "." + dpStartDate.getMonth() + "." + dpStartDate.getDayOfMonth() + ".";
                final String startTime = tpStartTime.getHour() + ":" + tpStartTime.getMinute();
                final String finishDate = dpFinishDate.getYear() + "." + dpStartDate.getMonth() + "." + dpStartDate.getDayOfMonth() + ".";
                final String finishTime = tpFinishTime.getHour() + ":" + tpStartTime.getMinute();


                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        URL url = null;
                        try {
                            url = new URL("车牌=" + carNumber + "&车型=" + carBand + "&开始日期=" + startDate + "&开始时间=" + startTime + "&结束日期=" + finishDate + "&结束时间=" + finishTime + operate);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = connection.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            String line = br.readLine();
                            JSONObject object = new JSONObject(line);
                            int i = object.getInt("i");
                            if (i > 0) {
                                Log.i("insert result", "将数据添加到后台成功");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


    }

    private void findViews() {
        tvTitle = findViewById(R.id.tv_biaoti);
        tvCarNumber = findViewById(R.id.tv_chepai);
        tvCarBand = findViewById(R.id.tv_chexing);
        tvStartDate = findViewById(R.id.tv_startdate);
        tvStartTime = findViewById(R.id.tv_starttime);
        tvFinishDate = findViewById(R.id.tv_finishdate);
        tvFinishTime = findViewById(R.id.tv_finishtime);
        edtCarNumber = findViewById(R.id.edt_chepai);
        edtCarBand = findViewById(R.id.edt_chexing);
        dpStartDate = findViewById(R.id.dp_startdate);
        dpFinishDate = findViewById(R.id.dp_finishdate);
        tpStartTime = findViewById(R.id.tp_starttime);
        tpFinishTime = findViewById(R.id.tp_finishtime);
        btn = findViewById(R.id.btn_img);
    }
}
