package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin on 4/22/2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;
import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment1 extends Fragment implements OnTalkToDBFinish{
    ViewGroup rootView;
    talkToDBTask task;
    String username ="";
    String password ="";

    HorizontalNumberPicker np;

    int hoursSleep;
    int mood;
    int work;
    private Switch mySwitch;
    boolean Tswich;
    Button button;
    Button add_variable;
    Button skip_day;
    RadioGroup radioGroup;
    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, null);
        add_variable = (Button)rootView.findViewById(R.id.add_button);
        skip_day = (Button)rootView.findViewById(R.id.skip_button);
        username = ((MainActivity)getActivity()).getUser();
        password = ((MainActivity)getActivity()).getPassword();
        np = (HorizontalNumberPicker) rootView.findViewById(R.id.numberPicker);


        np.setMinValue(0);
        np.setMaxValue(24);
        //np.setWrapSelectorWheel(false);
        button = (Button) rootView.findViewById(R.id.button);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.radioButton:
                        mood = 5;
                        // mood = good
                        break;
                    case R.id.radioButton2:
                        mood =0;
                        // mood = ok
                        break;
                    case R.id.radioButton3:
                        mood = -5;
                        // mood = not good
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Timmars sömn: " + hoursSleep);
                System.out.println("Tränat: " + Tswich);
                System.out.println("Humör: " + mood);
                System.out.println(Integer.toString(hoursSleep));

                String[] values = {Integer.toString(hoursSleep), Integer.toString(work), Integer.toString(mood)};
                String[] keys = {"1", "2", "3"};
                System.out.println(Arrays.toString(values));
                System.out.println(keys);
                System.out.println(username);
                System.out.println(password);
                runDBtask(values, keys, 3);
            }
        });

        add_variable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(6);
                System.out.println("add variable");
            }
        });

        skip_day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Day skipped!", Toast.LENGTH_SHORT).show();
                System.out.println("skip day");
            }
        });



/*        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
>>>>>>> e262376344f7839c10d7adb7e3601b6680b802cf

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hoursSleep = newVal;
            }
        });
        */

        mySwitch = (Switch) rootView.findViewById(R.id.switch1);
        mySwitch.setChecked(false);
        Tswich = false;
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Tswich == false){
                    Tswich = true;
                    work = 1;
                }
                else{
                    Tswich = false;
                    work = 0;
                }
            }
        });
        mySwitch.setChecked(false);
        Tswich = false;
        return rootView;
    }
    public void runDBtask(String[]values, String[] keys,int request){
        task = new talkToDBTask(this);
        task.setUsername(username);
        task.setPwd(password);
        task.setKeys(keys);
        task.setValues(values);
        task.setRequestType(request);
        task.execute();
    }
    void setMessage(String msg){
        TextView txt=(TextView) rootView.findViewById(R.id.textView);
        txt.setText(msg);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    //Mood: good
                    mood = 5;
                break;
            case R.id.radioButton2:
                if (checked)
                    // Mood: OK
                    mood = 0;
                break;
            case R.id.radioButton3:
                if (checked)
                    // Mood: Bad
                    mood = -5;
                break;
        }
    }
    public void onSwitch(View v){


        if(Tswich == false){
            Tswich = true;
            work = 1;
        }
        else{
            Tswich = false;
            work = 0;
        }
    }
    @Override
    public void onTaskCompleted() {
        Toast.makeText(getActivity().getApplicationContext(), "Variables added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskFailed() {
        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
    }
}
