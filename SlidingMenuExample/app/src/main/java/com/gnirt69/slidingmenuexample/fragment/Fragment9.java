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
import android.widget.TextView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment9 extends Fragment {
    Button members;
    String groupName;
    String groupID;
    TextView title;

    public Fragment9() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.selected_group, container, false);
        setRetainInstance(true);
        groupName = ((MainActivity) getActivity()).getGname();
        groupID = ((MainActivity)getActivity()).getGID();
        title = (TextView) rootView.findViewById(R.id.groupName);
        title.setText(groupName);

        members = (Button)rootView.findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(10);
            }
        });
        return rootView;
    }


}
