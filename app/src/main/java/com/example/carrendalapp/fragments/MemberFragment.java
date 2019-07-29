package com.example.carrendalapp.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrendalapp.AboutActivity;
import com.example.carrendalapp.AccountActivity;
import com.example.carrendalapp.MainActivity;
import com.example.carrendalapp.MyAppointmentActivity;
import com.example.carrendalapp.R;
import com.example.carrendalapp.ReleaseActivity;
import com.example.carrendalapp.CheckActivity;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.tasks.DownloadImageTask;
import com.example.carrendalapp.utils.ImageUtil;
import com.example.carrendalapp.views.CircleImageView;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author WD
 */
public class MemberFragment extends Fragment implements View.OnClickListener {


    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        CircleImageView circleImageView = view.findViewById(R.id.civ_profile);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvCheckMessage = view.findViewById(R.id.tv_check_message);

        RelativeLayout rlAppointment = view.findViewById(R.id.rl_appointment);
        RelativeLayout rlAboutUs = view.findViewById(R.id.rl_about_us);
        RelativeLayout rlReleaseNews = view.findViewById(R.id.rl_release_news);
        RelativeLayout rlCheck = view.findViewById(R.id.rl_check);

        circleImageView.setOnClickListener(this);
        rlAppointment.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
        rlReleaseNews.setOnClickListener(this);
        rlCheck.setOnClickListener(this);

        //取出SharedPreferences
        SharedPreferences sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
        String imageName = sp.getString("imageName", null);
        String name = sp.getString("account", null);
        int manager = sp.getInt("manager", 1);
        //如果图片不为空的时候
        if (!imageName.equals("null") && imageName != null) {
            new DownloadImageTask(circleImageView).execute(imageName);
        }
        tvName.setText("账号：" + name);
        //根据是否为管理员显示不同的内容
        if (manager == 0) {
            tvCheckMessage.setText(R.string.check);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.civ_profile:
                intent = new Intent(getContext(), AccountActivity.class);
                break;
            case R.id.rl_appointment:
                intent = new Intent(getContext(), MyAppointmentActivity.class);
                break;
            case R.id.rl_about_us:
                intent = new Intent(getContext(), AboutActivity.class);
                break;
            case R.id.rl_release_news:
                intent = new Intent(getContext(), ReleaseActivity.class);
                break;
            case R.id.rl_check:
                intent = new Intent(getContext(), CheckActivity.class);
                break;
            default:
        }
        startActivity(intent);
    }
}
