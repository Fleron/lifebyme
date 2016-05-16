package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

public class Fragment8 extends Fragment implements OnTalkToDBFinish {
    Button button;
    ViewGroup rootView;
    EditText groupName;
    String gname;
    String username;
    int requestType;
    Context context;

    public Fragment8() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_group, container, false);
        context = rootView.getContext();
        setRetainInstance(true);
        username = ((MainActivity)getActivity()).getUser();
        groupName = (EditText) rootView.findViewById(R.id.add_group);
        button = (Button) rootView.findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFieldValues();
                gname = groupName.getText().toString();
                runDBtaskCreateGroup(6);
            }
        });




        return rootView;
    }


    private void runDBtaskCreateGroup(int request){

        talkToDBTask task = new talkToDBTask(this,context);
        requestType = request;
        task.setGname(gname);
        task.setUsername(username);
        task.setRequestType(requestType);
        task.execute();
    }

    private void groupWasAdded(){
        Toast.makeText(getActivity().getApplicationContext(), "Group created!", Toast.LENGTH_SHORT).show();
        //Send user to Social, add Toast.
    }
    @Override
    public void onTaskCompleted(int request) {
        groupWasAdded();
    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}