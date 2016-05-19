package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.LoginActivity;
import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment5 extends Fragment {
    Button log_out_button;

    public Fragment5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment5, container, false);
        setRetainInstance(true);
        log_out_button = (Button) rootView.findViewById(R.id.log_out);
        log_out_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager(); // or 'getSupportFragmentManager();'
                int count = fm.getBackStackEntryCount();
                for(int i = 0; i < count - 1; ++i) {
                    fm.popBackStack();
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                ((MainActivity) getActivity()).logOut();
            }
        });

        return rootView;
    }
}