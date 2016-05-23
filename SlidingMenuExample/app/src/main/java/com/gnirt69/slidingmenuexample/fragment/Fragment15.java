package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-05-23.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.R;

public class Fragment15 extends Fragment {
    Button change_pwd;
    ;


    public Fragment15(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.change_password, container, false);

        change_pwd = (Button) rootView.findViewById(R.id.change_pwd);

        change_pwd.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                System.out.println("CHANGE!!");
            }
        });


        return rootView;
    }
}