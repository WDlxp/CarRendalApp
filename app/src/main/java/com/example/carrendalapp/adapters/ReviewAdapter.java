package com.example.carrendalapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.carrendalapp.R;
import com.example.carrendalapp.entity.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    TextView tv_biaoti1,tv_name,tv_chepai1,tv_chepaihao1,tv_chexing1,tv_chexinghao1,tv_freetime1,tv_time;
    ImageView imgv_chezhu;
    Button btn;

    public ReviewAdapter(Context context, int resource, List<Review> objects) {
        super(context,resource,objects);

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getItem是从集合类中获取实体的类
        final Review review= getItem(position);
        convertView=LayoutInflater.from(getContext()).inflate(R.layout.activity_review_adapter,null);
        tv_biaoti1=convertView.findViewById(R.id.tv_biaoti1);
        tv_name=(TextView) convertView.findViewById(R.id.tv_name);
        tv_chepai1=(TextView) convertView.findViewById(R.id.tv_chepai1);
        tv_chepaihao1=(TextView) convertView.findViewById(R.id.tv_chepaihao1);
        tv_chexing1=(TextView) convertView.findViewById(R.id.tv_chexing1);
        tv_chexinghao1=(TextView) convertView.findViewById(R.id.tv_chexinghao1);
        tv_freetime1=(TextView) convertView.findViewById(R.id.tv_freetime1);
        tv_time=(TextView) convertView.findViewById(R.id.tv_time);
        imgv_chezhu=(ImageView) convertView.findViewById(R.id.imgv_chezhu);
        btn=(Button) convertView.findViewById(R.id.btn_shenhe);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //创建一个对话框
                final AlertDialog alertDialog = builder.create();
                //设置对话框内容
                builder.setTitle("审核操作")
                        //设置内容
                        .setMessage("确定要审核该内容吗？")
                        //设置确定按钮
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //审核并移除数据适配器中的数据

                                remove(review);
                                //通知数据适配器更新视图
                                notifyDataSetChanged();
                            }
                        })
                        //设置取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //隐藏弹窗
                                alertDialog.dismiss();
                            }
                        }).show();
                //同时对话框也可以加载布局文件
//                builder.setView()
            }

            private Context getContext() {
                return null;
            }

        });

        tv_name.setText(review.getName());
        tv_chepaihao1.setText(review.getChepaihao());
        tv_chexinghao1.setText(review.getChexing());
        tv_time.setText(review.getTime());

        return convertView;

    }

}
