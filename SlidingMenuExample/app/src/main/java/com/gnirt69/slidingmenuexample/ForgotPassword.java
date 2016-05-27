package com.gnirt69.slidingmenuexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

    }

    public void onButtonClick(View V){
        if (V.getId() == R.id.reset_password){
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(this.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            email = (EditText) findViewById(R.id.resetPassword);
            email_string = email.getText().toString();
            System.out.println("Reset password, email: "+email_string);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
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
            Toast.makeText(getApplicationContext(), "An email has been sent to "+email_string, Toast.LENGTH_SHORT).show();
            Intent r = new Intent(ForgotPassword.this, LoginActivity.class);
            startActivity(r);

        }
        else{
            Toast.makeText(getApplicationContext(), email_string+" is not in our database.", Toast.LENGTH_SHORT).show();

        }

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
