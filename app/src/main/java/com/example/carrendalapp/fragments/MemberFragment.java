package com.example.carrendalapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.carrendalapp.AboutActivity;
import com.example.carrendalapp.AccountActivity;
import com.example.carrendalapp.MyAppointmentActivity;
import com.example.carrendalapp.R;
import com.example.carrendalapp.ReleaseActivity;
import com.example.carrendalapp.ReviewActivity;
import com.example.carrendalapp.entity.Review;
import com.example.carrendalapp.views.CircleImageView;

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

        RelativeLayout rlAppointment = view.findViewById(R.id.rl_appointment);
        RelativeLayout rlAboutUs = view.findViewById(R.id.rl_about_us);
        RelativeLayout rlReleaseNews = view.findViewById(R.id.rl_release_news);
        RelativeLayout rlCheck = view.findViewById(R.id.rl_check);

        circleImageView.setOnClickListener(this);
        rlAppointment.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
        rlReleaseNews.setOnClickListener(this);
        rlCheck.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.civ_profile:
                intent=new Intent(getContext(), AccountActivity.class);
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
                intent = new Intent(getContext(), ReviewActivity.class);
                break;
            default:
        }
        startActivity(intent);
    }
}
