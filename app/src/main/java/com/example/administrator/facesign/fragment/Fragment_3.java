package com.example.administrator.facesign.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.facesign.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_3 extends Fragment {

    private TextView tv_title;

    private View view;
    public Fragment_3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_3, container, false);
        // Inflate the layout for this fragment
        initView();
        return view;
    }

    private void initView(){
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("考勤");
    }

}
