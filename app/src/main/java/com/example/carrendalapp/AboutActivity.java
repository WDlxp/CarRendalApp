package com.example.carrendalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author WD
 */
public class AboutActivity extends AppCompatActivity {

    private TextView tvTel;
    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("关于我们");
        actionBarAndStatusBarUtil.showBackButton();

        tvTel = findViewById(R.id.tv_tel);

        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri telUri = Uri.parse("tel:18959223364");
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                startActivity(intent);
            }
        });
    }
}
