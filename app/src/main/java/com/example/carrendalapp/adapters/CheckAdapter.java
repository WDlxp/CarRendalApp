package com.example.carrendalapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carrendalapp.R;
import com.example.carrendalapp.entity.CarOrder;

import java.util.List;

public class CheckAdapter extends ArrayAdapter {
    public CheckAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CarOrder car = (CarOrder) getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_usercheck, null);
        ImageView img = convertView.findViewById(R.id.img_cportrait);
        TextView tv_cname = convertView.findViewById(R.id.tv_cname);
        TextView tv_ccarnumber = convertView.findViewById(R.id.tv_ccarnumber);
        TextView tv_ccarbrand = convertView.findViewById(R.id.tv_ccarbrand);
        TextView tv_cfreetime = convertView.findViewById(R.id.tv_cfreetime);
        TextView tv_usercheck = convertView.findViewById(R.id.tv_usercheck);


        //img
        tv_cname.setText(car.getName());
        tv_ccarnumber.setText("车牌：" + car.getCarNumber());
        tv_ccarbrand.setText("车型：" + car.getCarBand());
        tv_cfreetime.setText("空闲时间：" +"\n"+ car.getFreeTime());

        // tv_usercheck.setText(car.getCheck());

        if (car.getCheck() == 1) {
            tv_usercheck.setText("待审核");
        } else if (car.getCheck() == 0) {
            tv_usercheck.setText("审核通过");
        }


        return convertView;
    }

}
