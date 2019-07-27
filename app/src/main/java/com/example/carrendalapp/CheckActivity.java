package com.example.carrendalapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.adapters.ReviewAdapter;
import com.example.carrendalapp.entity.Review;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WD
 */
public class CheckActivity extends AppCompatActivity {


    private ListView lvReview;
    private List<Review> reviewList = new ArrayList<>();

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("管理员审核");
        actionBarAndStatusBarUtil.showBackButton();

//        lvReview = findViewById(R.id.lv_check);

        Review review = new Review("张三", "闽D12345", "奔驰", "2019.07.24.12：00-2019.08.20.12:00");
        Review review1 = new Review("李四", "闽D26541", "丰田", "2019.07.20.12：00-2019.08.22.12:00");
        Review review2 = new Review("王五", "闽D24632", "奥迪", "2019.07.18.12：00-2019.08.2.12:00");

        reviewList.add(review);
        reviewList.add(review1);
        reviewList.add(review2);

//        ReviewAdapter reviewAdapter = new ReviewAdapter(CheckActivity.this, R.layout.activity_review_adapter, reviewList);
//        lvReview.setAdapter(reviewAdapter);
    }
    //gradlew ProcessApplicationManifest --stacktrace
}
