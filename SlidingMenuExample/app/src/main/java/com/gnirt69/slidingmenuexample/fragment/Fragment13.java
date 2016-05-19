package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-05-19.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;

public class Fragment13 extends Fragment implements OnTalkToDBFinish{


    Context context;
    View rootView;

    public Fragment13() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.statistics, container, false);
        setRetainInstance(true);
        context = rootView.getContext();


        return rootView;
    }

    @Override
    public void onTaskCompleted(int request) {

    }

    @Override
    public void onTaskFailed() {

    }
}
