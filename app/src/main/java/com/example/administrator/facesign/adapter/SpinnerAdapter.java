package com.example.administrator.facesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.facesign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class SpinnerAdapter extends BaseAdapter {

    private List<String> mList = new ArrayList<String>();
    private Context mContext;

    public SpinnerAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(android.R.layout.simple_spinner_item, null);
        if(convertView!=null) {
            TextView week_num=(TextView)convertView.findViewById(R.id.week_num);
            week_num.setText(mList.get(i));
        }
        return convertView;

    }
}
