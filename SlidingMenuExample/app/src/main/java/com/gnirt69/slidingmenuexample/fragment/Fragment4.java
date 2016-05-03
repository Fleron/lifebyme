package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by NgocTri on 10/18/2015.
 */

import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment4 extends Fragment {

    private TextView textView;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    private View rootView;
    Button button;

    public Fragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment4, container, false);
        //changeText(MainActivity.steps_taken);
        setRetainInstance(true);

        button = (Button) rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(7);
                System.out.println("create group");
            }
        });

        return rootView;
    }
    public void changeText(String result){
        //this textview should be bound in the fragment onCreate as a member variable
        //TextView text =(TextView)rootView.findViewById(R.id.textViewStep);
        //text.setText(result);
    }
}
