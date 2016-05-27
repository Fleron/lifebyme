package com.gnirt69.LifeByME.fragment;/**
 * Created by Martin 2016-05-23.
 */

import android.app.Fragment;
import android.content.Context;
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

public class Fragment15 extends Fragment implements OnTalkToDBFinish {
    Button change_pwd;
    String username;
    String current_password;
    String new_password;
    String confirm_password;
    private int request;
    int requestType;
    EditText c_password;
    EditText n_password;
    EditText con_password;
    talkToDBTask task;
    Context context;
    View rootView;

    public Fragment15(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.change_password, container, false);
        context = rootView.getContext();
        c_password = (EditText) rootView.findViewById(R.id.current_password);
        n_password = (EditText) rootView.findViewById(R.id.new_password);
        con_password = (EditText) rootView.findViewById(R.id.confirm_password);

        getUsername();

        change_pwd = (Button) rootView.findViewById(R.id.change_pwd);

        change_pwd.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                current_password = c_password.getText().toString();
                new_password = n_password.getText().toString();
                confirm_password = con_password.getText().toString();
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                View focusView = null;

                if(new_password.equals(confirm_password)){
                    if(new_password.length() > 4){
                        System.out.println("WORKS");
                        runDBtaskChangePwd(18);
                        }
                    else{
                        n_password.setError(getString(R.string.error_invalid_password));
                        focusView = n_password;
                        focusView.requestFocus();
                        n_password.setText("");
                        con_password.setText("");
                    }
                    }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();
                    n_password.setText("");
                    con_password.setText("");

                }
            }
        });


        return rootView;
    }

    private void runDBtaskChangePwd(int request){

        task = new talkToDBTask(this,context);
        requestType = request;
        task.setUsername(username);
        task.setCurrentPwd(current_password);
        task.setNewPwd(new_password);
        task.setRequestType(requestType);
        task.execute();
    }

    public void getUsername(){
        username = ((MainActivity)getActivity()).getUser();
    }


    @Override
    public void onTaskCompleted(int request) {
        Toast.makeText(getActivity().getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).replaceFragment(4);
    }

    @Override
    public void onTaskFailed(int responseCode) {

    }
}