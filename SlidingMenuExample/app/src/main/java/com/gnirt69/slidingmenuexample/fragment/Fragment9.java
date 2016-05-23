package com.gnirt69.slidingmenuexample.fragment;

/**
 * SELECTED_GROUP
 * Created by lumph on 2016-05-10.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

public class Fragment9 extends Fragment implements OnTalkToDBFinish {
    Button members;
    Button edit_var;
    String groupName;
    String groupID;
    TextView title;
    talkToDBTask task;
    Context context;
    View rootView;
    int requestType;
    LinearLayout.LayoutParams params;
    String [] groupVarName;
    String [] groupVarID;
    String [] varOwner;

    public Fragment9() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.selected_group, container, false);
        setRetainInstance(true);
        groupName = ((MainActivity) getActivity()).getGname();
        groupID = ((MainActivity)getActivity()).getGID();
        title = (TextView) rootView.findViewById(R.id.groupName);
        title.setText(groupName);
        title.setAllCaps(true);

        members = (Button)rootView.findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(10);
            }
        });
        edit_var = (Button)rootView.findViewById(R.id.edit_var);
        edit_var.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(12);            }
        });

        runDBtaskGetGroupVar(16);

        return rootView;
    }


    private void runDBtaskGetGroupVar(int request){

        task = new talkToDBTask(this,context);
        requestType = request;
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }

    void generateTable(){
        if(rootView != null) {
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.variable_layout);
        for (int i = 0; i < groupVarName.length; i++) {
            Button btn = new Button(getActivity());
            btn.setText(groupVarName[i]+" ("+varOwner[i]+")");
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
    }


    @Override
    public void onTaskCompleted(int request) {
        groupVarName = task.getGroupVarNames();
        groupVarID = task.getGroupVarIDs();
        varOwner = task.getVarOwner();

        generateTable();
    }

    @Override
    public void onTaskFailed() {
        System.out.println("ERROR ERROR ERRRRRROR...");
    }
}
