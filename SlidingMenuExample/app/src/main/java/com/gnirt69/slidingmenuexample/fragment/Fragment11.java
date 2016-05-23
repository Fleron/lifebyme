package com.gnirt69.slidingmenuexample.fragment;

/**
 * SEARCH MEMBER
 * Created by Martin on 2016-05-12.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

public class Fragment11 extends Fragment implements OnTalkToDBFinish {
    Button add_member;
    Button leave_group;
    talkToDBTask task;
    int requestType;
    String groupID;
    String [] groupMembers;
    String  requestStatus;
    Context context;
    String toUser;
    String fromUser;
    String groupName;
    EditText user_to_add;
    View rootView;
    TextView title;
    LinearLayout.LayoutParams params;


    public Fragment11() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.group_members, container, false);
        context = rootView.getContext();
        setRetainInstance(true);

        groupName = ((MainActivity) getActivity()).getGname();
        groupID = ((MainActivity) getActivity()).getGID();
        title = (TextView) rootView.findViewById(R.id.groupName);
        title.setText(groupName);
        title.setAllCaps(true);

        user_to_add = (EditText) rootView.findViewById(R.id.search_user);
        getUsername();
        runDBtaskGetGroupMembers(9);
        leave_group = (Button)rootView.findViewById(R.id.leave_group);
        add_member = (Button)rootView.findViewById(R.id.add_member);
        add_member.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                toUser = user_to_add.getText().toString();
                if(!toUser.equals(fromUser)){
                    runDBtaskSendRequest(11);
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "You are already in this group...", Toast.LENGTH_SHORT).show();
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    user_to_add.setText("");/**/
                }
            }
        });

        leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("leave group");
                runDBtaskLeaveGroup(13);
            }
        });

        return rootView;
    }

    public void getUsername(){
        fromUser = ((MainActivity)getActivity()).getUser();
    }

    private void runDBtaskSendRequest(int request){

        task = new talkToDBTask(this,context);
        requestType = request;
        task.setUsername(fromUser);
        task.setToUser(toUser);
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }

    private void runDBtaskGetGroupMembers(int request){
        task = new talkToDBTask(this,context);
        requestType = request;
        task.setGroupID(groupID);
        task.setRequestType(requestType);
        task.execute();
    }

    private void runDBtaskLeaveGroup(int request){
        task = new talkToDBTask(this,context);
        requestType = request;
        task.setGroupID(groupID);
        task.setUsername(fromUser);
        task.setRequestType(requestType);
        task.execute();
    }


    void generateTable(){
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
            //btn.setTypeface(null, Typeface.BOLD);
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Medium.otf");
            btn.setTypeface(typeface);
            params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.setMargins(0,5,0,0);

            ll.addView(btn,params);
        }
    }

    @Override
    public void onTaskCompleted(int request) {

        if (request == 9){
            System.out.println(request);
            groupMembers = task.getGroupMembers();
            generateTable();
        }
        else if(request == 11){
            requestStatus = task.getRequestStatus();
            switch (requestStatus) {
                case "REQUESTSENT":
                    user_to_add.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "A request has been sent to "+toUser+".", Toast.LENGTH_SHORT).show();
                    break;
                case "REQUESTEXIST":
                    user_to_add.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "A request has already been sent to "+toUser+".", Toast.LENGTH_SHORT).show();
                    break;
                case "NOTFOUND":
                    user_to_add.setText("");
                    /**/Toast.makeText(getActivity().getApplicationContext(), "Username "+toUser+" does not exist.", Toast.LENGTH_SHORT).show();
                    break;
                case "MEMBEREXIST":
                    user_to_add.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), toUser+" is already in this group.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if(request == 13) {
            ((MainActivity) getActivity()).replaceFragment(3);
        }

    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}
