package com.gnirt69.slidingmenuexample.fragment;

/**
 * SELECTED_GROUP
 * Created by lumph on 2016-05-10.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment9 extends Fragment {
    Button add_member;

    public Fragment9() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.selected_group, container, false);
        add_member = (Button)rootView.findViewById(R.id.add_member);
        setRetainInstance(true);
        add_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(9);
            }
        });
        return rootView;
    }


}
