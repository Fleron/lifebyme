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

import com.gnirt69.slidingmenuexample.R;

public class Fragment10 extends Fragment {
    Button search_user;
    EditText user_to_add;
    int requestType;
    String user;

    public Fragment10() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_user, container, false);
        search_user = (Button)rootView.findViewById(R.id.button3);
        user_to_add = (EditText) rootView.findViewById(R.id.search_user);

        setRetainInstance(true);
        search_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                user = user_to_add.getText().toString();
                //runDBtaskAddUserToGroup();
                System.out.println(user);
            }
        });
        return rootView;

    }

/*    private void runDBtaskAddUserToGroup(int request){

        talkToDBTask task = new talkToDBTask(this);
        requestType = request;
        task.setUsername(user);
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