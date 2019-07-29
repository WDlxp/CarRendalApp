package com.example.carrendalapp.tasks;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.utils.ImageUtil;
import com.example.carrendalapp.views.CircleImageView;

/**
 * 下载头像的Task
 *
 * @author WD
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    /**
     * 需要设置下载下来图片的控件
     */
    @SuppressLint("StaticFieldLeak")
    private CircleImageView civProfile;

    public DownloadImageTask(CircleImageView civProfile) {
        this.civProfile = civProfile;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        //判断参数不为空
        if (strings[0] != null) {
            //通过BASE_URL+图片名获取图片的完整路径
            String imagePath = UrlAddress.BASE_URL + strings[0];
            //返回下载的结果
            return ImageUtil.downloadImg(imagePath);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //接收下载结果不为空则设置头像
        if (bitmap != null) {
            civProfile.setImageBitmap(bitmap);
        } else {
//                Toast.makeText(AccountActivity.this, "头像下载失败", Toast.LENGTH_SHORT).show();
        }
    }
}