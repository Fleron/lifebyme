package com.gnirt69.slidingmenuexample.fragment;

/**
 * Created by NgocTri on 10/18/2015.
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

import java.util.Arrays;

public class Fragment4 extends Fragment implements OnTalkToDBFinish {

    private View rootView;
    Button button;
    String username;
    int requestType;
    String GID;
    String requestAnswer;
    talkToDBTask task;
    String requestID;
    private String[] gnames;
    private String[] gids;
    private String[] gnames_request;
    private String[] gids_request;
    private String[] fromUsers;
    String  requestCallback;
    Context context;
    LinearLayout.LayoutParams params;
    LinearLayout ll;
    LinearLayout ll2;


    public Fragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment4, container, false);
        context = rootView.getContext();
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
        ll = (LinearLayout) rootView.findViewById(R.id.groups_layout);
        System.out.println("Gnames:");
        System.out.println(Arrays.toString(gnames));
        if(gnames != null && gnames.length > 0){
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
               //btn.setTypeface(null, Typeface.BOLD);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Medium.otf");
                btn.setTypeface(typeface);
                params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                params.setMargins(0,5,0,0);

                ll.addView(btn,params);
            }
        }


    }

    void generateRequestTable(){
        ll2 = (LinearLayout) rootView.findViewById(R.id.requests);
        if (gnames_request != null && gnames_request.length > 0){
            for (int i = 0; i < gnames_request.length; i++) {
                RelativeLayout ll3 = new RelativeLayout(context);

                ImageButton accept = new ImageButton(getActivity());
                ImageButton deny = new ImageButton(getActivity());
                accept.setId(i);
                deny.setId(1000+i);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("ACCEPT");
                        System.out.println(gnames_request[v.getId()]);
                        requestAnswer = "TRUE";
                        GID = gids_request[v.getId()];
                        runDBtaskAnswerRequest(12);

                    }
                });

                deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("DENY");
                        System.out.println(gnames_request[v.getId()-1000]);
                        requestAnswer = "FALSE";
                        GID = gids_request[v.getId()-1000];
                        runDBtaskAnswerRequest(12);

                    }
                });

                accept.setBackgroundResource(R.drawable.checked);
                deny.setBackgroundResource(R.drawable.cancel);

                ll3.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ll3.setBackgroundResource(R.drawable.mybutton);

                TextView request = new TextView(context);
                request.setText(gnames_request[i]+" ("+fromUsers[i]+")");
                request.setTextSize(20);
                request.setTextColor(Color.parseColor("#167165"));
                request.setAllCaps(true);
                request.setTypeface(null, Typeface.BOLD);
                RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(20, 20, 0, 20); // llp.setMargins(left, top, right, bottom);
                request.setLayoutParams(llp);

                LinearLayout.LayoutParams llpp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llpp.setMargins(0, 5, 0, 0); // llp.setMargins(left, top, right, bottom);
                ll3.setLayoutParams(llpp);

                // Cancel-button
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params1.addRule(RelativeLayout.CENTER_VERTICAL);
                params1.setMargins(0,0,50,0);
                params1.width = 55;
                params1.height = 55;
                deny.setLayoutParams(params1);

                // Accept-button
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params2.addRule(RelativeLayout.LEFT_OF,1000+i);
                params2.addRule(RelativeLayout.CENTER_VERTICAL);
                params2.setMargins(0,0,70,0);
                params2.width = 70;
                params2.height = 70;
                accept.setLayoutParams(params2);

                ll3.addView(request);
                ll3.addView(accept);
                ll3.addView(deny);
                ll2.addView(ll3);
            }
        }

    }

    public void getUsername(){
        username = ((MainActivity)getActivity()).getUser();
    }

    private void runDBtaskGetGroups(int request){

        task = new talkToDBTask(this,context);
        requestType = request;
        task.setUsername(username);
        task.setRequestType(requestType);
        task.execute();
    }

    private void runDBtaskAnswerRequest(int request){
        System.out.println("group id: "+GID);
        task = new talkToDBTask(this,context);
        requestType = request;
        task.setUsername(username);
        task.setGroupID(GID);
        task.setRequestAnswer(requestAnswer);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted(int request) {
        if (request == 8) {
            // Groups
            gids = task.getGroupIDs();
            gnames = task.getGroupNames();

            //Requests
            gnames_request = task.getGroupNamesRequest();
            gids_request = task.getGroupIDsRequest();
            fromUsers = task.getRequestSender();

            generateGroupButton();
            generateRequestTable();
        }
        else if (request == 12){
            requestCallback = task.getRequestCallback();
            System.out.println("request response: "+requestCallback);
            System.out.println("group ID: "+GID);
            ll.removeAllViews();
            ll2.removeAllViews();

            runDBtaskGetGroups(8);




        }
    }

    @Override
    public void onTaskFailed(int responseCode) {
        System.out.println("error error error");
    }
}
