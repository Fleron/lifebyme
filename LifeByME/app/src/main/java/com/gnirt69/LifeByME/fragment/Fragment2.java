package com.gnirt69.LifeByME.fragment;/**
 * Created by Martin 2016-05-19.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gnirt69.LifeByME.MainActivity;
import com.gnirt69.LifeByME.OnTalkToDBFinish;
import com.gnirt69.LifeByME.R;
import com.gnirt69.LifeByME.talkToDBTask;

public class Fragment2 extends Fragment implements OnTalkToDBFinish{

    int requestType;
    String username;
    String password;
    Context context;
    talkToDBTask task;
    String [] variableNames;
    String [] variableTypes;
    String [] variableIDS;
    LinearLayout.LayoutParams params;
    LinearLayout ll;
    View rootView;

    public Fragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.statistics, container, false);
        setRetainInstance(true);
        context = rootView.getContext();
        getUsername();
        getPassword();
        runDBtaskGetGroups(10);


        return rootView;
    }

    public void getUsername(){
        username = ((MainActivity)getActivity()).getUser();
    }

    public void getPassword(){
        password = ((MainActivity)getActivity()).getPassword();
    }

    void populateTable(){
        if(rootView != null) {
            ll = (LinearLayout) rootView.findViewById(R.id.groups_layout);
            if (variableNames != null && variableNames.length > 0) {
                for (int i = 0; i < variableNames.length; i++) {
                    Button btn = new Button(getActivity());
                    btn.setText(variableNames[i]);
                    btn.setBackgroundResource(R.drawable.mybutton);
                    btn.setTextSize(20);
                    btn.setId(i);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(variableNames[v.getId()]);
                            ((MainActivity) getActivity()).setVID(variableNames[v.getId()]);
                            ((MainActivity) getActivity()).replaceFragment(11);
                        }
                    });
                    btn.setTextColor(Color.parseColor("#157065"));
                    //btn.setTypeface(null, Typeface.BOLD);
                    Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Medium.otf");
                    btn.setTypeface(typeface);
                    params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                    params.setMargins(0, 5, 0, 0);

                    ll.addView(btn, params);
                }
            }
        }



    }

    private void runDBtaskGetGroups(int request){

        task = new talkToDBTask(this, context);
        requestType = request;
        task.setUsername(username);
        task.setPwd(password);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted(int request) {
        variableIDS = task.getVariablesID();
        variableNames = task.getVariablesNames();
        variableTypes = task.getVariablesTypes();
        populateTable();
    }
    @Override
    public void onTaskFailed(int responseCode) {

    }
    @Override
    public void onPause() {
        if(task!= null &&task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        super.onPause();
    }
}

