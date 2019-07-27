package com.example.carrendalapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.Car;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author WD
 */
public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtCarNumber, edtCarBand;
    private DatePicker dpStartDate, dpFinishDate;
    private TimePicker tpStartTime, tpFinishTime;
    private ImageView ivCar;
    private Button btnSubmit;

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    private String imagePath = null;
    private String imageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        //初始化状态栏与标题栏
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("发布租车信息");
        actionBarAndStatusBarUtil.showBackButton();

        //绑定视图
        findViews();

        //设置为24小时显示格式
        tpStartTime.setIs24HourView(true);
        //设置为24小时显示格式
        tpFinishTime.setIs24HourView(true);

        //设置点击事件
        setListeners();
    }

    private void setListeners() {
        ivCar.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void findViews() {
        edtCarNumber = findViewById(R.id.edt_car_number);
        edtCarBand = findViewById(R.id.edt_car_band);

        dpStartDate = findViewById(R.id.dp_start_date);
        dpFinishDate = findViewById(R.id.dp_finish_date);

        tpStartTime = findViewById(R.id.tp_start_time);
        tpFinishTime = findViewById(R.id.tp_finishtime);

        ivCar = findViewById(R.id.iv_car);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_car) {
            //设置隐式的Intent访问系统相册并选取一张图片
            //调用相册
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } else if (view.getId() == R.id.btn_submit) {
            //获取对应的信息
            String carNumber = edtCarNumber.getText().toString();
            String carBand = edtCarBand.getText().toString();
            if (carNumber.isEmpty() || carNumber.length() != 7) {
                Toast.makeText(ReleaseActivity.this, "车牌不正确", Toast.LENGTH_SHORT).show();
                edtCarNumber.requestFocus();
            } else if (carBand.isEmpty()) {
                Toast.makeText(ReleaseActivity.this, "车型不能为空", Toast.LENGTH_SHORT).show();
                edtCarBand.requestFocus();
            } else {
                //取出SharedPreferences
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String account = sp.getString("account", null);
                //获取空闲时间段
                StringBuilder timeStringBuilder = new StringBuilder();
                timeStringBuilder.append(dpStartDate.getYear()).append("-").append(zeroPadding(dpStartDate.getMonth())).append("-").append(zeroPadding(dpStartDate.getDayOfMonth())).append("-").append(zeroPadding(tpStartTime.getHour()) + ":" + zeroPadding(tpStartTime.getMinute())).append(" ").append(dpFinishDate.getYear() + "-" + zeroPadding(dpStartDate.getMonth()) + "-" + zeroPadding(dpStartDate.getDayOfMonth()) + "-").append(zeroPadding(tpFinishTime.getHour()) + ":" + zeroPadding(tpStartTime.getMinute()));
                Log.d("InsertTime", timeStringBuilder.toString());
                Car car = new Car(account, carNumber, carBand, imageName, timeStringBuilder.toString(), 0, 1);
                //启动发布
                new ReleaseCarInfoTask().execute(car);
            }
        }
    }

    private String zeroPadding(int time) {
        if (time < 10) {
            return 0 + "" + time;
        }
        return time + "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //获取图片路径
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                if (selectedImage != null) {
                    cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                        imagePath = cursor.getString(columnIndex);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivCar.setImageBitmap(bitmap);
                        cursor.close();
                    }
                }
                if (imagePath != null) {
                    new UploadImageTask().execute(imagePath);
                }
            }
        }
    }

    /**
     * 上传图片的Task
     */
    private class UploadImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String imagePath = strings[0];
            //这里的path就是那个地址的全局变量
            File file = new File(imagePath);
            String requestUrl = UrlAddress.UPLOAD_IMAGE_URL;
            //                        String imagePath = URL.BASE_URL + result;
//                        Bitmap bitmap=ImageUtil.downloadImg(imagePath);
            return ImageUtil.uploadFile(file, requestUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(ReleaseActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ReleaseActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                imageName = s;
            }
            super.onPostExecute(s);
        }
    }

    /**
     * 发布车辆的信息表
     */
    private class ReleaseCarInfoTask extends AsyncTask<Car, Void, Integer> {

        @Override
        protected Integer doInBackground(Car... cars) {
            Car car = cars[0];
            URL url = null;
            try {
                url = new URL(UrlAddress.INSERT_CAR_URL + "?operation=insert&account=" + car.getAccount() + "&carNumber=" + car.getCarNumber() + "&carBand=" + car.getCarBand() + "&image=" + imageName + "&freeTime=" + car.getFreeTime() + "&state=" + car.getState() + "&checkState=" + car.getCheck());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = br.readLine();
                JSONObject object = new JSONObject(line);
                return object.getInt("result");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Toast.makeText(ReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ReleaseActivity.this, "发布失败请再尝试一次", Toast.LENGTH_LONG).show();
            }
        }
    }
}
