package com.example.carrendalapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * @author Administrator
 * @date 19-7-4
 */
public class ImageUtil {

    private static final String TAG = "uploadFile";
    //超时时间
    private static final int TIME_OUT = 1000 * 10;
    //设置编码
    private static final String CHARSET = "utf-8";

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param requestURL 请求的rul
     * @return 返回响应的内容
     */
    public static String uploadFile(File file, String requestURL) {
        String result = null;
        //边界标识   随机生成
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINE_END = "\r\n";
        //内容类型
        String CONTENT_TYPE = "multipart/form-data";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            //允许输入流
            conn.setDoInput(true);
            //允许输出流
            conn.setDoOutput(true);
            //不允许使用缓存
            conn.setUseCaches(false);
            //请求方式
            conn.setRequestMethod("POST");
            //设置编码
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if (file != null) {
                /*
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"").append(file.getName()).append("\"").append(LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET).append(LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(endData);
                dos.flush();
                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    System.out.println(result);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存文件到指定路径
     */
    public static void saveMyBitmap(Context context, Bitmap bitmap, String fileName) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/pic/";
        File appDir = new File(sdCardDir);
        //不存在
        if (!appDir.exists()) {
            appDir.mkdir();
        }
//        String fileName = "a.jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("SaveImage", "Successful");
    }

    /**
     * 将bitmap转换为字节
     *
     * @param bitmap Bitmap的图片
     * @return 返回字节数组
     */
    private byte[] bitmap2byte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * 从网络上下载图片资源
     * //不要在这里开线程容易出现返回值错误
     *
     * @param imgPath 资源链接
     * @return Bitmap
     */
    public static Bitmap downloadImg(final String imgPath) {
        Bitmap bmp = null;
        try {
            URL imgUrl = imgUrl = new URL(imgPath);
            //打开连接
            URLConnection con = imgUrl.openConnection();
            InputStream in = con.getInputStream();
            bmp = BitmapFactory.decodeStream(in);
            if (bmp == null) {
                Log.d("Image", "获取为空");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
