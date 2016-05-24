package com.gnirt69.slidingmenuexample;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Martin on 2016-04-07.
 */
public class CreateUser extends Activity implements OnTalkToDBFinish {
    String email;
    String username;
    String password;
    String confirm_password;
    int requestType;
    private AccountManager mAccountManager;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        mAccountManager = AccountManager.get(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
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
                if(passwordValid()) {
                    if (checkBox.isChecked()) {
                        runDBtaskAddUser(1, false);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You have to accept the terms and conditions..", Toast.LENGTH_SHORT).show();
                    }
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
        if(V.getId() == R.id.textView5){
                new AlertDialog.Builder(CreateUser.this)
                        .setTitle("Disclaimer")
                        .setMessage("When using the LifeByMe app please note that correlation is not the same thing as causation, i e having two parameters that follow similar patterns does not necessarily mean that these two parameters affect each other. The LifeByMe app will only show mathematical correlation and does not take into account whether parameters are otherwise related or not.")
                        .setCancelable(true)
                        .create().show();
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
        talkToDBTask task = new talkToDBTask(this,this);
        task.setUsername(username);
        task.setPwd(password);
        requestType = request;
        task.setRequestType(requestType);
        task.execute();
    }
/*
    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");
                    Log.d("udinic", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }
*/


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
        talkToDBTask task = new talkToDBTask(this,this);
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
    public void onTaskCompleted(int request) {
        if(requestType == 1){
            onUserAlreadyExists();

        }
        if(requestType == 2){
            userWasAdded();

        }
    }

    @Override
    public void onTaskFailed(int responseCode) {
        if(requestType == 1){
            runDBtaskAddUser(2,true);
        }
        if(requestType == 2){
            System.out.println("error");
        }
    }
}
