package com.example.carrendalapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.carrendalapp.R;
import com.example.carrendalapp.entity.AppointOrder;
import com.example.carrendalapp.entity.Order;
import com.example.carrendalapp.tasks.CancelAppointTask;
import com.example.carrendalapp.tasks.DownloadImageTask;
import com.example.carrendalapp.views.CircleImageView;

import java.util.List;

/**
 * @author WD
 */
public class AppointOrderAdapter extends BaseAdapter {

    private CircleImageView circleImageView;
    private TextView tvName, tvCarNumber, tvCarBrand, tvFreeTime, tvAppointTime, tvAppointTel;
    private Button btnCancelAppoint;

    private Context mContext;
    private List<AppointOrder> appointOrderList;
    private int flag = 0;

    public AppointOrderAdapter(Context mContext, List<AppointOrder> appointOrderList, int flag) {
        this.mContext = mContext;
        this.appointOrderList = appointOrderList;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return appointOrderList.size();
    }

    @Override
    public Object getItem(int i) {
        return appointOrderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //获取当前项
        AppointOrder appointOrder = appointOrderList.get(i);
        final Order order = appointOrder.getOrder();
        //获取布局
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_myappoint, null);

        findViews(view);

        if (!"null".equals(appointOrder.getImage()) && appointOrder.getImage() != null) {
            new DownloadImageTask(circleImageView).execute(appointOrder.getImage());
        }
        tvName.setText(appointOrder.getName());
        tvCarNumber.setText("车牌" + order.getCarNumber());
        tvCarBrand.setText("车型：" + appointOrder.getCarBrand());
        tvFreeTime.setText("空闲时间：" + appointOrder.getFreeTime());
        tvAppointTime.setText("预约时间：" + order.getStartDate() + " " + order.getStartTime() + "~" + order.getFinishDate() + " " + order.getFinishTime());
        tvAppointTel.setText("Tel:" + appointOrder.getTel());
        //根据状态设计不同的显示
        if (order.getState() == 1) {
            btnCancelAppoint.setText("取消");
        } else {
            btnCancelAppoint.setEnabled(false);
            btnCancelAppoint.setBackgroundResource(R.drawable.btn_finish_order_bg);
            btnCancelAppoint.setText("已结束");
        }

        btnCancelAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancelAppointTask().execute(order.getAccount(), order.getCarNumber());
                //如果是全部页点击取消
                if (flag == 0) {
                    //设置为取消状态
                    appointOrderList.get(i).getOrder().setState(0);
                    notifyDataSetChanged();
                } else if (flag == 1) {
                    //移除数据元更新数据
                    appointOrderList.remove(i);
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    private void findViews(View view) {
        //显示出底部的预约时间和空闲时间
        LinearLayout ll = view.findViewById(R.id.ll);
        ll.setVisibility(View.VISIBLE);

        View vDivider = view.findViewById(R.id.v_divider);
        vDivider.setVisibility(View.VISIBLE);

        circleImageView = view.findViewById(R.id.iv_car);
        tvName = view.findViewById(R.id.tv_name);
        tvCarNumber = view.findViewById(R.id.tv_car_number);
        tvCarBrand = view.findViewById(R.id.tv_car_brand);
        tvFreeTime = view.findViewById(R.id.tv_free_time);
        btnCancelAppoint = view.findViewById(R.id.btn_appoint);

        tvAppointTime = view.findViewById(R.id.tv_appoint_time);
        tvAppointTel = view.findViewById(R.id.tv_appoint_tel);
    }

    public void updateAppointOrderData(List<AppointOrder> newAppointOrderList) {
        appointOrderList = newAppointOrderList;
        notifyDataSetChanged();
    }
}
