package com.example.carrendalapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自定义圆形的ImageView
 * @author WD
 */
public class CircleImageView extends AppCompatImageView {
    private float width;
    private float height;
    private float radius;
    private Paint paint;
    private Matrix matrix;

    public CircleImageView(Context context) {
        super(context);
        initProperties();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperties();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperties();
    }

    /**
     * 初始化属性
     */
    private void initProperties() {
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.darker_gray));
        //抗锯齿
        paint.setAntiAlias(true);
        //初始化缩放矩阵
        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //计算radius
        radius = Math.min(width, height) / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            super.draw(canvas);
        } else if (drawable instanceof BitmapDrawable) {
            //将着色器设置给画笔
            paint.setShader(initBitmapShader((BitmapDrawable) drawable));
            //使用画笔在画布上画圆
            canvas.drawCircle(width / 2, height / 2, radius, paint);
        }
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private BitmapShader initBitmapShader(BitmapDrawable drawable) {
        Bitmap bitmap = drawable.getBitmap();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = Math.max(width / bitmap.getWidth(), height / bitmap.getHeight());
        //将图片宽高等比例缩放，避免拉伸
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        return bitmapShader;
    }
}
