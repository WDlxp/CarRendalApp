package com.example.carrendalapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.carrendalapp.R;

/**
 * A simple {@link Fragment} subclass.
 * @author WD
 */
public class HomePageFragment extends Fragment {


    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home_page, container, false);
        EditText etName=view.findViewById(R.id.et_name);
        etName.setText("你好");
        return view;
    }
}
