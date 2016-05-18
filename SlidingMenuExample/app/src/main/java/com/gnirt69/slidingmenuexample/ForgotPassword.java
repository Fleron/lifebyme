package com.gnirt69.slidingmenuexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Martin on 2016-04-26.
 */
public class ForgotPassword extends ActionBarActivity implements OnTalkToDBFinish {


    EditText email;
    String email_string;
    String sendStatus;
    int requestType;
    talkToDBTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public void onButtonClick(View V){
        if (V.getId() == R.id.reset_password){
            email = (EditText) findViewById(R.id.resetPassword);
            email_string = email.getText().toString();
            System.out.println("Reset password, email: "+email_string);
            runDBtaskResetPassword(14);
        }
    }

    private void runDBtaskResetPassword(int request){
        task = new talkToDBTask(this, this);
        requestType = request;
        task.setEmail(email_string);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted(int request) {
        sendStatus = task.getSendStatus();
        if(sendStatus.equals("SENDEMAIL_SUCCESS")){

        }
        else{

        }

    }

    @Override
    public void onTaskFailed() {

    }
}
