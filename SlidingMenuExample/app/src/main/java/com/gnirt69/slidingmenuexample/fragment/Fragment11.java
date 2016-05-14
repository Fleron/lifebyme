package com.gnirt69.slidingmenuexample.fragment;

/**
 * SEARCH MEMBER
 * Created by Martin on 2016-05-12.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

import java.util.Arrays;

public class Fragment11 extends Fragment implements OnTalkToDBFinish {
    Button add_member;
    talkToDBTask task;
    int requestType;
    String groupID;
    String [] groupMembers;

    public Fragment11() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_members, container, false);
        setRetainInstance(true);

        groupID = ((MainActivity) getActivity()).getGID();
        runDBtaskGetGroupMembers(9);
        add_member = (Button)rootView.findViewById(R.id.add_member);
        add_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(9);
            }
        });

        return rootView;
    }

    private void runDBtaskGetGroupMembers(int request){

        task = new talkToDBTask(this);
        requestType = request;
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted() {
        groupMembers = task.getGroupMembers();
        System.out.println(Arrays.toString(groupMembers));
        System.out.println("grupp medlemmar");
    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}