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
    @SuppressLint("StaticFieldLeak")
    private CircleImageView civProfile;

    public DownloadImageTask(CircleImageView civProfile) {
        this.civProfile = civProfile;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        if (strings[0] != null) {
            String imagePath = UrlAddress.BASE_URL + strings[0];
            return ImageUtil.downloadImg(imagePath);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //下载不为空则设置头像
        if (bitmap != null) {
            civProfile.setImageBitmap(bitmap);
        } else {
//                Toast.makeText(AccountActivity.this, "头像下载失败", Toast.LENGTH_SHORT).show();
        }
    }
}