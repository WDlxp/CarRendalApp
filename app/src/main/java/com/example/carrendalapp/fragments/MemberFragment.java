package com.example.carrendalapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carrendalapp.MyAppointmentActivity;
import com.example.carrendalapp.R;

/**
 * A simple {@link Fragment} subclass.
 * @author WD
 */
public class MemberFragment extends Fragment {


    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_member, container, false);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MyAppointmentActivity.class);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

}
