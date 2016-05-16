package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin on 4/22/2016.
 */

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;
import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;

import java.util.Arrays;

public class Fragment1 extends Fragment implements OnTalkToDBFinish{
    ViewGroup rootView;
    ViewGroup rootView2;
    talkToDBTask task;
    String username ="";
    String password ="";
    CharSequence[] cs = new CharSequence[]{
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
            "17","18","19","20","21","22","23","24"
    };
    NumberPicker np;
    HorizontalNumberPicker np2;
    TableLayout table;
    String [] variableNames;
    String [] variableTypes;
    String [] variableIDS;

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
        rootView = (ViewGroup) inflater.inflate(R.layout.add_values, container,false);
        add_variable = (Button)rootView.findViewById(R.id.add_button2);
        skip_day = (Button)rootView.findViewById(R.id.skip_button2);
        username = ((MainActivity)getActivity()).getUser();
        password = ((MainActivity)getActivity()).getPassword();
        //View view2 =(View)inflater.inflate(R.layout.horizontal_number_picker,rootView,true);
        //np2 = (HorizontalNumberPicker) view2.findViewById(R.id.numberPicker);
        runDBtask(null,null,10);





        /*np = (HorizontalNumberPicker) rootView2.findViewById(R.id.numberPicker);

        np.setMinValue(0);
        np.setMaxValue(24);
        np.getButtonMinusView().setTextColor(Color.argb(255,255,255,255));
        np.getButtonMinusView().setScaleY(1.5f);
        np.getButtonMinusView().setScaleX(2);


        np.getButtonPlusView().setTextColor(Color.argb(255,255,255,255));
        np.getTextValueView().setTextColor(Color.argb(255,255,255,255));
        //np.setWrapSelectorWheel(false);*/
        button = (Button) rootView.findViewById(R.id.submit_button2);
        /*
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
        */
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



/*


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
        */
        return rootView;
    }
    public void runDBtask(String[]values, String[] keys,int request){
        task = new talkToDBTask(this);
        task.setUsername(username);
        task.setPwd(password);
        if(values != null && keys != null){
            task.setKeys(keys);
            task.setValues(values);
        }
        task.setRequestType(request);
        task.execute();
    }
    void populateTable(){

        table = (TableLayout)rootView.findViewById(R.id.table_layout);


        for(int i = 0; i<variableNames.length; i ++){
            TableRow row = new TableRow(getActivity());
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);
            TextView text = new TextView(getActivity());
            text.setWidth(400);
            text.setPadding(20,5,5,5);
            text.setGravity(Gravity.CENTER);
            text.setText(variableNames[i]);
            System.out.println("hej");
            if (i == 1){
                radioGroup = new RadioGroup(getActivity());
                radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                RadioButton btn1 = new RadioButton(getActivity());
                btn1.setId(R.id.btn1);
                RadioButton btn2 = new RadioButton(getActivity());
                btn2.setId(R.id.btn2);
                RadioButton btn3 = new RadioButton(getActivity());
                btn3.setId(R.id.btn3);
                radioGroup.addView(btn1);
                radioGroup.addView(btn2);
                radioGroup.addView(btn3);
                radioGroup.setGravity(Gravity.CENTER);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId) {
                            case R.id.btn1:
                                mood = 5;
                                System.out.println(mood);
                                // mood = good
                                break;
                            case R.id.btn2:
                                mood =0;
                                System.out.println(mood);
                                // mood = ok
                                break;
                            case R.id.btn3:
                                mood = -5;
                                System.out.println(mood);
                                // mood = not good
                                break;
                        }
                    }
                });
                row.addView(text);
                //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                row.addView(radioGroup);
                table.addView(row,i);

            }else if(i==2){
                mySwitch = new Switch(getActivity());
                mySwitch.setChecked(false);
                Tswich = false;
                mySwitch.setPadding(0,0,250,0);
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
                row.addView(text);
                //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                row.addView(mySwitch);
                table.addView(row,i);
                //mySwitch.setChecked(false);
                //Tswich = false;
            }else if(i==3){

                np = new NumberPicker(getActivity());
                np.setMaxValue(24);
                np.setMinValue(0);
                np.setX(2.0f);
                np.setValue(8);
                np.setClickable(false);
                np.setScaleX(0.8f);
                np.setScaleY(0.8f);
                np.setMinimumWidth(3);

                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        hoursSleep = newVal;
                        System.out.println(hoursSleep);
                    }
                });

                /*
                np2.getButtonMinusView().setTextColor(Color.argb(255,255,255,255));
                np2.getButtonMinusView().setScaleY(1.5f);
                np2.getButtonMinusView().setScaleX(2);
                np2.getButtonPlusView().setTextColor(Color.argb(255,255,255,255));
                np2.getTextValueView().setTextColor(Color.argb(255,255,255,255));

                np2.setListener(new HorizontalNumberPickerListener() {
                    @Override
                    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int value) {
                        System.out.println(value);
                    }
                });
                */
                /*
                np.setListener(new HorizontalNumberPickerListener() {
                    @Override
                    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int value) {
                        hoursSleep = value;
                        System.out.println(value);
                    }
                });*/
                row.addView(text);
                //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                row.addView(np);
                table.addView(row,i);
            }
            else {
                Button btn = new Button(getActivity());
                btn.setText("waddup");
                row.addView(text);
                //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                row.addView(btn);

                table.addView(row, i);
            }
        }
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
        variableIDS = task.getVariablesID();
        variableNames = task.getVariablesNames();
        variableTypes = task.getVariablesTypes();
        populateTable();
        Toast.makeText(getActivity().getApplicationContext(), "Variables added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskFailed() {
        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
    }
}
