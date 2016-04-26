package com.gnirt69.slidingmenuexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.LoginActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;

/**
 * Created by Martin on 2016-04-07.
 */
public class CreateUser extends Activity implements OnTalkToDBFinish {
    String email;
    String username;
    String password;
    String confirm_password;
    int requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();
                String result = data.getStringExtra("result");
                System.out.println(result);
                Intent r = new Intent(CreateUser.this, LoginActivity.class);
                startActivity(r);
            }
            if(resultCode == Activity.RESULT_CANCELED){
                onUserAlreadyExists();
            }
        }
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                onUserAlreadyExists();
            }
            if(resultCode == Activity.RESULT_CANCELED){
                attemptAddUser();
            }
        }
    }*/

    public void onButtonClick(View V){
        if (V.getId() == R.id.button3){
            getFieldValues();
            hideSoftKeyboard(this);

            if (password.length()>4){
                if(passwordValid()){
                    runDBtaskAddUser(1,false);
                }
                else{
                    clearFields();
                    Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                clearFields();
                Toast.makeText(getApplicationContext(), "Password must be larger than four characters", Toast.LENGTH_SHORT).show();

            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void getFieldValues(){
        email = ((EditText) findViewById(R.id.email)).getText().toString();
        username = ((EditText) findViewById(R.id.username)).getText().toString();
        password = ((EditText) findViewById(R.id.password)).getText().toString();
        confirm_password = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
    }
    public void clearFields(){
        EditText editTextPassword = ((EditText) findViewById(R.id.password));
        EditText editTextconfirmedpassword = ((EditText) findViewById(R.id.confirm_password));
        editTextPassword.setText("");
        editTextconfirmedpassword.setText("");
    }
/*    public void attemptAddUser(){

        Intent tent = new Intent(CreateUser.this, TalkToDBActivity.class);
        tent.putExtra("username",username);
        tent.putExtra("password",password);
        tent.putExtra("email",email);
        int requestCode = 2;
        tent.putExtra("requestCode", requestCode);
        startActivityForResult(tent, 2);

    }*/
    public void checkUserExists(int request){
        talkToDBTask task = new talkToDBTask(this);
        task.setUsername(username);
        task.setPwd(password);
        requestType = request;
        task.setRequestType(requestType);
        task.execute();
    }

    private void onUserAlreadyExists(){
        EditText editTextPassword = ((EditText) findViewById(R.id.password));
        EditText editTextUsername = ((EditText) findViewById(R.id.username));
        EditText editTextconfirmedpassword = ((EditText) findViewById(R.id.confirm_password));
        Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT).show();
        editTextPassword.setText("");
        editTextUsername.setText("");
        editTextconfirmedpassword.setText("");
    }
    private boolean passwordValid(){
        if(password.equals(confirm_password)) {
            return true;
        }
        return false;
    }
    private void runDBtaskAddUser(int request,boolean addEmail){
        talkToDBTask task = new talkToDBTask(this);
        requestType = request;
        task.setUsername(username);
        task.setPwd(password);
        if(addEmail){
            task.setEmail(email);
        }
        task.setRequestType(requestType);
        task.execute();
    }
    private void userWasAdded(){

        Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();
        Intent r = new Intent(CreateUser.this, LoginActivity.class);
        startActivity(r);
    }
    @Override
    public void onTaskCompleted() {
        if(requestType == 1){
            onUserAlreadyExists();

        }
        if(requestType == 2){
            userWasAdded();

        }
    }

    @Override
    public void onTaskFailed() {
        if(requestType == 1){
            runDBtaskAddUser(2,true);
        }
        if(requestType == 2){
            System.out.println("error");
        }
    }
}
