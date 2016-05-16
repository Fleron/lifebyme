package com.gnirt69.slidingmenuexample.fragment;

/**
 * SEARCH MEMBER
 * Created by Martin on 2016-05-12.
 */

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    String toUser;
    String fromUser;
    EditText user_to_add;
    View rootView;
    LinearLayout.LayoutParams params;

    public Fragment11() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_members, container, false);
        setRetainInstance(true);

        groupID = ((MainActivity) getActivity()).getGID();
        user_to_add = (EditText) rootView.findViewById(R.id.search_user);
        getUsername();
        runDBtaskGetGroupMembers(9);
        add_member = (Button)rootView.findViewById(R.id.add_member);
        add_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toUser = user_to_add.getText().toString();
                System.out.println("F: "+fromUser);
                System.out.println("T: "+toUser);
                System.out.println("Group ID: "+groupID);
                runDBtaskSendRequest(10);
            }
        });

        return rootView;
    }

    public void getUsername(){
        fromUser = ((MainActivity)getActivity()).getUser();
    }


    private void runDBtaskSendRequest(int request){

        task = new talkToDBTask(this);
        requestType = request;
        task.setUsername(fromUser);
        task.setToUser(toUser);
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }
    private void runDBtaskGetGroupMembers(int request){
        task = new talkToDBTask(this);
        requestType = request;
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }

    void generateTable(){
        System.out.println("members:");
        System.out.println(Arrays.toString(groupMembers));
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.members_layout);
        for (int i = 0; i < groupMembers.length; i++) {
            Button btn = new Button(getActivity());
            btn.setText(groupMembers[i]);
            btn.setBackgroundResource(R.drawable.mybutton);
            btn.setTextSize(20);
            btn.setId(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "Function not available yet..", Toast.LENGTH_SHORT).show();

                }
            });
            btn.setTextColor(Color.parseColor("#157065"));
            btn.setTypeface(null, Typeface.BOLD);
            params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.setMargins(0,5,0,0);

            ll.addView(btn,params);
        }
    }



    @Override
    public void onTaskCompleted() {
        groupMembers = task.getGroupMembers();
        generateTable();
    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}