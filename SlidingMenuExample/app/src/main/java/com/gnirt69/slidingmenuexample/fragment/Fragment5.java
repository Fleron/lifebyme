package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.LoginActivity;
import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment5 extends Fragment {
    Button log_out_button;
    Button change_pwd_button;
    Button reminders_button;

    public Fragment5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment5, container, false);
        setRetainInstance(true);
        log_out_button = (Button) rootView.findViewById(R.id.log_out);
        change_pwd_button = (Button) rootView.findViewById(R.id.change_password);
        reminders_button = (Button) rootView.findViewById(R.id.reminders);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Book.otf");
        log_out_button.setTypeface(font);
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Book.otf");
        change_pwd_button.setTypeface(font);
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Book.otf");
        reminders_button.setTypeface(font);


        log_out_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager(); // or 'getSupportFragmentManager();'
                int count = fm.getBackStackEntryCount();
                for(int i = 0; i < count - 1; ++i) {
                    fm.popBackStack();
                }
                ((MainActivity) getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        change_pwd_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(14);
            }
        });

        reminders_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Function not available yet..", Toast.LENGTH_SHORT).show();

            }
        });



        return rootView;
    }
}