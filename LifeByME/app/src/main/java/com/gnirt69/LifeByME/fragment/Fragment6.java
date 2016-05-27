package com.gnirt69.LifeByME.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnirt69.LifeByME.R;

public class Fragment6 extends Fragment {
    public Fragment6() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment6, container, false);
        setRetainInstance(true);
        return rootView;
    }
}