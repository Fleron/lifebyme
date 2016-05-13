package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by NgocTri on 10/18/2015.
 */

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

public class Fragment4 extends Fragment implements OnTalkToDBFinish {

    private View rootView;
    Button button;
    Button select_group;
    String username;
    int requestType;
    talkToDBTask task;
    private String[] gnames;
    private String[] gids;
    LinearLayout.LayoutParams params;


    public Fragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment4, container, false);
        //changeText(MainActivity.steps_taken);
        setRetainInstance(true);

        getUsername();
        runDBtaskGetGroups(8);

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

    void generateGroupButton(){
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.groups_layout);
        for (int i = 0; i < gnames.length; i++) {
            Button btn = new Button(getActivity());
            btn.setText(gnames[i]);
            btn.setBackgroundResource(R.drawable.mybutton);
            btn.setTextSize(20);
            btn.setId(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setGname(gnames[v.getId()]);
                    ((MainActivity) getActivity()).setGID(gids[v.getId()]);
                    ((MainActivity) getActivity()).replaceFragment(8);
                    System.out.println(gnames[v.getId()]);
                    System.out.println(gids[v.getId()]);
                }
            });
            btn.setTextColor(Color.parseColor("#157065"));
            btn.setTypeface(null, Typeface.BOLD);
            params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.setMargins(0,5,0,0);

            ll.addView(btn,params);
        }



    }




    public void getUsername(){
        username = ((MainActivity)getActivity()).getUser();
    }

    private void runDBtaskGetGroups(int request){

        task = new talkToDBTask(this);
        requestType = request;
        task.setUsername(username);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted() {
        gids = task.getGroupIDs();
        gnames = task.getGroupNames();
        generateGroupButton();
    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}
