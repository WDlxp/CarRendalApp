package com.example.carrendalapp.utils;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.example.carrendalapp.R;

/**
 * @author WD
 */
public class ActionBarAndStatusBarUtil {
    private static TextView tvTitle;
    private static ActionBar myActionBar;
    public static void initActionBarAndStatusBar(Window window, ActionBar actionBar) {
//        ActionBar actionBar = getSupportActionBar();
        //使用自定义的标题栏使得标题栏字体居中
        if (actionBar != null) {
            myActionBar=actionBar;
            //设置自定义的ActionBar显示方式
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //设置自定义ActionBar的布局
            actionBar.setCustomView(R.layout.title_layout);
            //设置阴影大小
            actionBar.setElevation(0f);
            //绑定Title
            tvTitle = actionBar.getCustomView().findViewById(R.id.display_title);
            //展示自定义ActionBar
            actionBar.setDisplayShowCustomEnabled(true);
        }
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void setTitle(String title) {
        tvTitle.setText(title);
    }

    public static void showBackButton(){
        myActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
