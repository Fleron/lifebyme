package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnirt69.slidingmenuexample.R;

public class Fragment5 extends Fragment {
    public Fragment5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment5, container, false);
        return rootView;
    }
}