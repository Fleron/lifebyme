package com.gnirt69.slidingmenuexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivityHomemade extends ActionBarActivity implements OnTalkToDBFinish{
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_activity_homemade);
    }

    public void onButtonClick(View V){

        if (V.getId() == R.id.login) {
            EditText editTextPassword = ((EditText) findViewById(R.id.editText1));
            EditText editTextUsername = ((EditText) findViewById(R.id.editText2));
            username = editTextPassword.getText().toString();
            password = editTextUsername.getText().toString();
            String email ="";

            System.out.println(username);
            System.out.println(password);

            runDBtask();
        }
        else if (V.getId() == R.id.button2){
            //Intent r = new Intent(LoginActivityHomemade.this, CreateUserActivity.class);
            //startActivity(r);
        }
    }
    private void runDBtask(){
        talkToDBTask task = new talkToDBTask(this,this);
        task.setUsername(username);
        task.setPwd(password);
        task.setRequestType(1);
        task.execute();
    }
    private void setText(boolean success){
        if(success){
            Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
        }
        else{
            EditText editTextPassword = ((EditText) findViewById(R.id.editText1));
            EditText editTextUsername = ((EditText) findViewById(R.id.editText2));
            editTextPassword.setText("");
            editTextUsername.setText("");
            Toast.makeText(getApplicationContext(), "Not a valid user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskCompleted(int request) {
        Intent r = new Intent(LoginActivityHomemade.this, MainActivity.class);
        r.putExtra("username", username);
        r.putExtra("password", password);
        System.out.println(username);
        System.out.println(password);
        System.out.println("from login");
        startActivity(r);
    }

    @Override
    public void onTaskFailed() {
        System.out.println("fail");
    }
}
