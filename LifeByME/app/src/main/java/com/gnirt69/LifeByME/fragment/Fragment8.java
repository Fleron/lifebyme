package com.gnirt69.LifeByME.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gnirt69.LifeByME.MainActivity;
import com.gnirt69.LifeByME.OnTalkToDBFinish;
import com.gnirt69.LifeByME.R;
import com.gnirt69.LifeByME.talkToDBTask;

public class Fragment8 extends Fragment implements OnTalkToDBFinish {
    Button button;
    ViewGroup rootView;
    EditText groupName;
    String gname;
    String username;
    int requestType;
    Context context;
    talkToDBTask task;

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
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                groupName.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "Group created!", Toast.LENGTH_SHORT).show();
                runDBtaskCreateGroup(6);
                ((MainActivity) getActivity()).replaceFragment(3);
            }
        });




        return rootView;
    }


    private void runDBtaskCreateGroup(int request){

        task = new talkToDBTask(this,context);
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
    public void onTaskFailed(int responseCode) {
        System.out.println("error error error");
    }
    @Override
    public void onPause() {
        if(task!= null &&task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        super.onPause();
    }
}