package com.gnirt69.slidingmenuexample;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Martin on 2016-04-26.
 */
public class ForgotPassword extends ActionBarActivity implements OnTalkToDBFinish {


    EditText email;
    String email_string;

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
        }
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public void onTaskFailed() {

    }
}
