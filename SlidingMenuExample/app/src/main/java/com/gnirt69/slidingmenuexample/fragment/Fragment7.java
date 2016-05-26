package com.gnirt69.slidingmenuexample.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

/**
 * Created by fleron on 2016-05-03.
 */
public class Fragment7 extends Fragment implements OnTalkToDBFinish{
    Button button;
    EditText text;
    talkToDBTask task;
    RadioGroup radioGroup;
    String scale;
    String username ="";
    String password ="";
    String variableName ="";
    Context context;
    public Fragment7() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_variable, container, false);
        context = rootView.getContext();
        setRetainInstance(true);
        button = (Button) rootView.findViewById(R.id.button3);
        text = (EditText) rootView.findViewById(R.id.add_variable);
        username = ((MainActivity)getActivity()).getUser();
        password = ((MainActivity)getActivity()).getPassword();
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.radioButton:
                        scale = "amount";
                        // amount
                        break;
                    case R.id.radioButton2:
                        scale ="binary";
                        // binary
                        break;
                    case R.id.radioButton3:
                        scale = "scale";
                        // mood
                        break;
                    case R.id.radioButton4:
                        scale = "hours";
                        // hours
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                variableName = text.getText().toString();
                runDBtask(5);
                ((MainActivity) getActivity()).replaceFragment(0);
            }
        });

        return rootView;
    }
    public void runDBtask(int request){
        task = new talkToDBTask(this,context);
        task.setUsername(username);
        task.setPwd(password);
        task.setVariableName(variableName);
        task.setVariableType(scale);
        task.setRequestType(request);
        task.execute();
    }

    @Override
    public void onTaskCompleted(int request) {
        Toast.makeText(getActivity().getApplicationContext(), "Variable added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskFailed(int responseCode) {
        if(getActivity() != null){
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPause() {
        if(task!= null &&task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        super.onPause();
    }
}
