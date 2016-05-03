package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.R;

public class Fragment8 extends Fragment {
    Button button;
    public Fragment8() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_group, container, false);
        setRetainInstance(true);
        button = (Button) rootView.findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Group created!", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }
}