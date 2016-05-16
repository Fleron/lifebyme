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
import android.widget.EditText;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;

public class Fragment10 extends Fragment {
    Button search_user;
    EditText user_to_add;
    int requestType;
    String receiverUser;
    String senderUser;
    String groupID;

    public Fragment10() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_member, container, false);
        search_user = (Button)rootView.findViewById(R.id.button3);
        user_to_add = (EditText) rootView.findViewById(R.id.search_user);

        setRetainInstance(true);
        search_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                receiverUser = user_to_add.getText().toString();
                groupID = ((MainActivity)getActivity()).getGID();
                getUsername();
                
                //runDBtaskAddUserToGroup();
                System.out.println("S = "+senderUser);
                System.out.println("R = "+receiverUser);
                System.out.println("Group ID = "+groupID);
            }
        });
        return rootView;

    }

    public void getUsername(){
        senderUser = ((MainActivity)getActivity()).getUser();
    }

/*    private void runDBtaskSendUserRequest(int request){

        talkToDBTask task = new talkToDBTask(this);
        requestType = request;
        task.setUsername(user);
        task.set
        task.setRequestType(requestType);
        task.execute();
    }

    private void userWasAdded(){
        Toast.makeText(getActivity().getApplicationContext(), "User added to group!", Toast.LENGTH_SHORT).show();
        //Send user to Social, add Toast.
    }
    @Override
    public void onTaskCompleted() {
        userWasAdded();
    }

    @Override
    public void onTaskFailed() {
        System.out.println("error error error");
    }
}*/

}