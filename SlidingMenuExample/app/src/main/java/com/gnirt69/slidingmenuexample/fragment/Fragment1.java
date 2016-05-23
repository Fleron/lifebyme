package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin on 4/22/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;
import android.graphics.Typeface;
import android.content.Context;

public class Fragment1 extends Fragment implements OnTalkToDBFinish{
    ViewGroup rootView;
    ViewGroup rootView2;
    talkToDBTask task;
    String username ="";
    String password ="";
    String[] cs = new String[]{
            "24","23","22","21","20","19","18","17","16","15","14","13","12","11","10","9","8",
            "7","6","5","4","3","2","1","0"
    };
    String[] nums =new String[] {
            "3","2","1","0","-1","-2","-3"
    };
    NumberPicker np;

    TableLayout table;
    LayoutInflater inflater;
    String [] variableNames;
    String [] variableTypes;
    String [] variableIDS;
    Context context;
    String[] values;
    Map<String,String> valueMap;
    String[] keys;
    String hoursSleep;
    String mood;
    String work;
    String j;
    private Switch mySwitch;
    private CheckBox mCheckBox;
    boolean Tswich;
    Button button;
    ImageButton add_variable;
    Button skip_day;
    RadioGroup radioGroup;
    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        this.inflater = inflater;
        rootView = (ViewGroup) inflater.inflate(R.layout.add_values, container,false);
        //LinearLayout view = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.test,null);


        context = rootView.getContext();

        add_variable = (ImageButton)rootView.findViewById(R.id.add_button2);
        //skip_day = (Button)rootView.findViewById(R.id.skip_button2);
        username = ((MainActivity)getActivity()).getUser();
        password = ((MainActivity)getActivity()).getPassword();

        runDBtask(null,null,10);

        button = (Button) rootView.findViewById(R.id.submit_button2);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/CircularStd-Book.otf");
        button.setTypeface(font);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Timmars sömn: " + hoursSleep);
                System.out.println("Tränat: " + Tswich);
                System.out.println("Humör: " + mood);

                int i = 0;
                for(String key : valueMap.keySet()){
                    values[i] = valueMap.get(key);
                    keys[i] = key;
                    i++;
                }
                System.out.println(Arrays.asList(valueMap));
                System.out.println(Arrays.toString(values));
                System.out.println(Arrays.toString(keys));
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

/*        skip_day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Day skipped!", Toast.LENGTH_SHORT).show();
                System.out.println("skip day");
            }
        });*/

        return rootView;
    }
    public void runDBtask(String[]values, String[] keys,int request){
        task = new talkToDBTask(this,context);
        task.setUsername(username);
        task.setPwd(password);
        if(values != null && keys != null){
            task.setKeys(keys);
            task.setValues(values);
        }
        task.setRequestType(request);
        task.execute();
    }
    void populateTable() {

        table = (TableLayout) rootView.findViewById(R.id.table_layout);

        valueMap = new HashMap<>();
        if (variableNames != null) {
            values = new String[variableIDS.length];
            keys = new String[variableIDS.length];
            for (int i = 0; i < variableNames.length; i++) {
                j = variableIDS[i];
                System.out.println(j);
                TableRow row = new TableRow(getActivity());
                RelativeLayout rl = new RelativeLayout(getActivity());
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                row.setBackgroundResource(R.drawable.tableborder);

                row.addView(rl, params);

                View view2 = inflater.inflate(R.layout.text_frag1,rl,false);
                TextView text = (TextView)view2.findViewById(R.id.textView_frag1);

                text.setText(variableNames[i]);

                text.setId(i);
                lp.setMargins(5, 5,5,5);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                rl.addView(text);
                //används inte
                if (variableTypes[i].contains("shit")) {
                    View view = inflater.inflate(R.layout.radiogroup,rl,false);
                    radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
                    valueMap.put(j,"0");
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        String k = j;

                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.radioButton:
                                    mood =""+ 5;
                                    valueMap.put(k,mood);
                                    System.out.println("nyckel: "+k);
                                    // mood = good
                                    break;
                                case R.id.radioButton2:
                                    mood =""+ 0;
                                    valueMap.put(k,mood);
                                    System.out.println("nyckel: "+k);
                                    // mood = ok
                                    break;
                                case R.id.radioButton3:
                                    mood =""+ -5;
                                    valueMap.put(k,mood);
                                    System.out.println("nyckel: "+k);
                                    // mood = not good
                                    break;
                            }
                        }
                    });

                    rl.addView(radioGroup);

                    table.addView(row, i);

                } else if (variableTypes[i].contains("binary")) {

                    //View view = inflater.inflate(R.layout.switch_button,rl,false);
                    View view = inflater.inflate(R.layout.checkbox,rl,false);
                    mCheckBox = (CheckBox)view.findViewById(R.id.checkbox_frag1);
                    //mySwitch = (Switch) view.findViewById(R.id.switch1);

                    Tswich = false;
                    valueMap.put(j,"0");
                    mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        String k = j;
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                valueMap.put(k,""+1);
                                System.out.println("nyckel: "+k);
                            }else{
                                valueMap.put(k,""+0);
                                System.out.println("nyckel: "+k);
                            }
                        }
                    });
                    /*
                    mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        String k = j;
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (Tswich == false) {
                                Tswich = true;
                                work =""+ 1;
                                valueMap.put(k,work);
                            } else {
                                Tswich = false;
                                work =""+ 0;
                                valueMap.put(k,work);
                            }
                        }
                    });
                    */
                    rl.addView(mCheckBox);

                     table.addView(row, i);

                } else if (variableTypes[i].contains("amount")|| variableTypes[i].contains("scale")) {

                    View view = inflater.inflate(R.layout.numberpicker_frag1,rl,false);
                    np = (NumberPicker) view.findViewById(R.id.numberPicker2);
                    if(variableTypes[i].contains("amount")){
                        np.setDisplayedValues(cs);
                        np.setMaxValue(cs.length-1);
                        np.setMinValue(0);
                        np.setValue(8);
                        valueMap.put(j,"8");
                    }else{
                        np.setDisplayedValues(nums);
                        np.setMinValue(0);
                        np.setMaxValue(nums.length-1);
                        np.setValue(nums.length/2);

                        valueMap.put(j,"0");
                    }
                    np.setWrapSelectorWheel(false);
                    np.setClickable(false);
                    
                    np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        String k = j;
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            String[] list = picker.getDisplayedValues();
                            hoursSleep = list[newVal];
                            valueMap.put(k,hoursSleep);
                            System.out.println("nyckel: "+k);
                        }
                    });

                    rl.addView(np);
                    table.addView(row, i);



                } else {


                }
            }
        }
    }
    @Override
    public void onTaskCompleted(int request) {
        variableIDS = task.getVariablesID();
        variableNames = task.getVariablesNames();
        variableTypes = task.getVariablesTypes();
        populateTable();
        if(request == 1){
            Toast.makeText(getActivity().getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
        }else if(request == 3){
            Toast.makeText(getActivity().getApplicationContext(), "Variables added!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskFailed() {
        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
    }
}
