package com.example.administrator.facesign.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.AttentionInfo;
import com.example.administrator.facesign.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private Context mContext;
    private List<AttentionInfo> mAttentionList;

    public RecyclerAdapter(Context mContext, List<AttentionInfo> mAttentionList) {
        this.mContext = mContext;
        this.mAttentionList = mAttentionList;
    }

    public void refreshData(List<AttentionInfo> info){
        this.mAttentionList = info;
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        AttentionInfo item = mAttentionList.get(position);
       //周
        holder.tv_weekend.setText("第 "+item.getWeekend()+" 周");
        //课程
        holder.tv_course_name.setText(item.getCourseName());
        //星期几
        String day = "";
        switch (item.getDay()){
            case 1:
                day = "星期一";
                break;
            case 2:
                day = "星期二";
                break;
            case 3:
                day = "星期三";
                break;
            case 4:
                day = "星期四";
                break;
            case 5:
                day = "星期五";
                break;
            case 6:
                day = "星期六";
                break;
            case 7:
                day = "星期日";
                break;
        }
        holder.tv_day.setText(day);
        //节数
        holder.tv_section.setText(item.getStartSection()+" - "+item.getEndSection()+"节");
        //状态
        switch (item.getStatus()){
            case -1:
                holder.tv_status.setText("异常");
                holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.md_Red_A200));
                break;
            //正常
            case TimeUtil.STATUS_NORMAL:
                holder.tv_status.setText("正常");
                holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.md_Indigo_A200));

                break;
            //迟到
            case TimeUtil.STATUS_LATE:
                holder.tv_status.setText("迟到");
                holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.md_Orange_A200));

                break;
            //旷课
            case TimeUtil.STATUS_ABSENT:
                holder.tv_status.setText("旷课");
                holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.md_Red_A200));
                break;
            //早退
            case TimeUtil.STATUS_EARLY:
                holder.tv_status.setText("早退");
                holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.md_Pink_A200));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.mAttentionList == null? 0 : this.mAttentionList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_weekend;
        private TextView tv_course_name;
        private TextView tv_day;
        private TextView tv_section;
        private TextView tv_status;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_weekend = (TextView) itemView.findViewById(R.id.tv_weekend);
            tv_course_name = (TextView) itemView.findViewById(R.id.tv_course_name);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_section = (TextView) itemView.findViewById(R.id.tv_section);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);

        }
    }
}
