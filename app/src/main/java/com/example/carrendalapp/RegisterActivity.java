package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrendalapp.config.URL;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;

import java.io.File;

/**
 * 注册页面
 *
 * @author WD
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivProfile;
    private TextView tvToLogin;
    private Handler mHandler = new ImageHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("注册页");
        findViews();
        setListeners();
    }

    private void setListeners() {
        ivProfile.setOnClickListener(this);
        tvToLogin.setOnClickListener(this);
    }

    private void findViews() {
        ivProfile = findViewById(R.id.iv_profile);
        tvToLogin = findViewById(R.id.tv_to_login);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            //获取图片路径
            if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                final String imagePath = cursor.getString(columnIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ivProfile.setImageBitmap(bitmap);
                cursor.close();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //这里的path就是那个地址的全局变量
                        File file = new File(imagePath);
                        String RequestURL = URL.UPLOAD_IMAGE_URL;
                        String result = ImageUtil.uploadFile(file, RequestURL);
//                        String imagePath = URL.MAIN_URL + result;
//                        Bitmap bitmap=ImageUtil.downloadImg(imagePath);
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                        Log.d("UploadImage", result);
                    }
                }.start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_profile:
                //设置隐式的Intent访问系统相册并选取一张图片
                //调用相册
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_to_login:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
        }
    }

    private class ImageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(RegisterActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
////                String imagePath = URL.MAIN_URL + msg.obj;
//                Bitmap bitmap= (Bitmap) msg.obj;
////                Log.d("ImagePath",imagePath);
//                if (bitmap!=null){
//                    ivProfile.setImageBitmap(bitmap);
//                    Log.d("Image","图片不为空");
//                }else {
//                    Log.d("Image","图片为空");
//                }
            }
        }
    }

}
