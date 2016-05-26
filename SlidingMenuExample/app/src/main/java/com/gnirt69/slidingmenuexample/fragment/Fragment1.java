package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin on 4/22/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String defaultSeperator;

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
    EditText numberEdit;
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

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Book.otf");
        button.setTypeface(typeface);

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

    public void runDBtaskDeleteActivity(int request, String varID){
        task = new talkToDBTask(this,context);
        task.setVarIDToDelete(varID);
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
                final RelativeLayout rl = new RelativeLayout(getActivity());
                rl.setId(Integer.parseInt(j));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                row.setBackgroundResource(R.drawable.tableborder);

                rl.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("Delete activity")
                                .setMessage("Are you sure you want to delete this activity?")
                                .setCancelable(true)
                                .setNegativeButton("no",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                // no action
                                            }
                                        })
                                .setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                                String varID = String.valueOf(rl.getId());
                                                runDBtaskDeleteActivity(20,varID);
                                            }
                                        });


                        builder.create().show();



                        return false;
                    }
                });

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

                    View view = inflater.inflate(R.layout.switch_button,rl,false);
                    //View view = inflater.inflate(R.layout.checkbox,rl,false);
                    //mCheckBox = (CheckBox)view.findViewById(R.id.checkbox_frag1);
                    mySwitch = (Switch) view.findViewById(R.id.switch1);

                    Tswich = false;
                    valueMap.put(j,"0");
                    mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    rl.addView(mySwitch);
                     table.addView(row, i);

                }else if(variableTypes[i].contains("amount")){
                    // System.out.println(k+" key amount");
                    //valueMap.put(k,numberEdit.getText().toString());
                    View view = inflater.inflate(R.layout.free_input_frag1,rl,false);
                    numberEdit = (EditText) view.findViewById(R.id.editNumber);
                    valueMap.put(j,"0");
                    numberEdit.addTextChangedListener(new TextWatcher() {
                        String k = j;
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println(k+": key to numberEdit");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            System.out.println(s.toString());
                            valueMap.put(k,s.toString());
                        }
                    });
                    rl.addView(numberEdit);
                    table.addView(row, i);
                }
                else if (variableTypes[i].contains("scale")|| variableTypes[i].contains("hours")) {

                    View view = inflater.inflate(R.layout.numberpicker_frag1,rl,false);
                    np = (NumberPicker) view.findViewById(R.id.numberPicker2);
                    /*if(variableTypes[i].contains("amount")){
                        //np.setDisplayedValues(cs);
                        np.setMaxValue(100);
                        np.setMinValue(0);
                        np.setValue(8);
                        valueMap.put(j,"8");
                    }
                    */if(variableTypes[i].contains("hours")){
                        np.setMaxValue(24);
                        np.setMinValue(0);
                        np.setValue(8);
                        valueMap.put(j,"8");
                    }
                    else if(variableTypes[i].contains("scale")){
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
                            if (list != null ) {
                                hoursSleep = list[newVal];
                                valueMap.put(k, hoursSleep);
                                System.out.println("nyckel: " + k);
                            }
                            else{
                                hoursSleep = String.valueOf(newVal);
                                valueMap.put(k, hoursSleep);
                                System.out.println("nyckel: " + k);
                            }
                        }
                    });

                    rl.addView(np);
                    table.addView(row, i);
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
        }else if(request == 20){
            Toast.makeText(getActivity().getApplicationContext(), "Variables removed!", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).replaceFragment(0);

        }
    }
    @Override
    public void onTaskFailed(int responseCode) {
        if(getActivity() != null){
            switch (responseCode){
                case 32:
                    Log.i("lifebyme","DBTask Cancelled");
                    break;
                case 31:
                    if(getActivity()!= null){
                        Toast.makeText(getActivity().getApplicationContext(), "No Connection!", Toast.LENGTH_SHORT).show();
                    }Log.i("lifebyme","responseCode: "+ String.valueOf(responseCode)+ ": onTaskFailed Daily Activity");
                    break;
                case 0:
                    if(getActivity()!= null){
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }Log.i("lifebyme","responseCode: "+ String.valueOf(responseCode)+ ": onTaskFailed Daily Activity");
                    break;

            }

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
