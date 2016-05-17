package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin on 4/22/2016.
 */

import android.app.Fragment;
import android.content.Context;
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

import com.wefika.horizontalpicker.HorizontalPicker;


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
    //HorizontalNumberPicker np2;
    TableLayout table;
    HorizontalPicker np2;
    LayoutInflater inflater;
    String [] variableNames;
    String [] variableTypes;
    String [] variableIDS;
    Context context;

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
        this.inflater = inflater;
        rootView = (ViewGroup) inflater.inflate(R.layout.add_values, container,false);
        LinearLayout view = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.test,null);
        np2 = (HorizontalPicker) view.findViewById(R.id.numberPicker);

        context = rootView.getContext();

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

        if (variableNames != null) {
            for (int i = 0; i < variableNames.length; i++) {
                TableRow row = new TableRow(getActivity());
                RelativeLayout rl = new RelativeLayout(getActivity());
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                row.setBackgroundResource(R.drawable.tableborder);
                rl.setBackgroundResource(R.drawable.tableborder);
                //row.setLayoutParams(params);
                //row.setBackgroundColor(Color.BLACK);
                row.addView(rl, params);
                TextView text = new TextView(getActivity());

                text.setText(variableNames[i]);
                text.setGravity(Gravity.CENTER_VERTICAL);
                text.setId(i);
                lp.setMargins(5, 5,5,5);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                text.setLayoutParams(lp);
                rl.addView(text);

                if (variableTypes[i].contains("TEST2")) {
                    //RelativeLayout rl2 = new RelativeLayout(getActivity());
                    //row.addView(rl2, params);
                    View view = inflater.inflate(R.layout.radiogroup,null);
                    radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.radioButton:
                                    mood = 5;
                                    System.out.println(mood);
                                    // mood = good
                                    break;
                                case R.id.radioButton2:
                                    mood = 0;
                                    System.out.println(mood);
                                    // mood = ok
                                    break;
                                case R.id.radioButton3:
                                    mood = -5;
                                    System.out.println(mood);
                                    // mood = not good
                                    break;
                            }
                        }
                    });
                    //lp.setMargins(50, 5, 150, 5);
                    //lp.addRule(RelativeLayout.RIGHT_OF,i);
                    //radioGroup.setLayoutParams(lp);
                    rl.addView(radioGroup);

                    //row.addView(text);
                    //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                    table.addView(row, i);

                } else if (variableTypes[i].contains("binary")) {
                    //RelativeLayout rl2 = new RelativeLayout(getActivity());
                    //row.addView(rl2, params);
                    View view = inflater.inflate(R.layout.switch_button,null);
                    mySwitch = (Switch) view.findViewById(R.id.switch1);

                    Tswich = false;
                    //mySwitch.setPadding(0,0,250,0);
                    mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (Tswich == false) {
                                Tswich = true;
                                work = 1;
                            } else {
                                Tswich = false;
                                work = 0;
                            }
                        }
                    });
                    //lp.setMargins(150, 100, 150, 5);
                    //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                   // mySwitch.setLayoutParams(lp);
                    rl.addView(mySwitch);
                    //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                    table.addView(row, i);

                } else if (variableTypes[i].contains("TEST3") || variableTypes[i].contains("TEST")) {
                    //RelativeLayout rl2 = new RelativeLayout(getActivity());
                    //row.addView(rl2, params);
                    View view = inflater.inflate(R.layout.numberpicker_frag1,null);
                    np = (NumberPicker) view.findViewById(R.id.numberPicker2);

                    np.setMaxValue(24);
                    np.setMinValue(0);
                    np.setValue(8);
                    np.setClickable(false);
                    
                    np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            hoursSleep = newVal;
                            System.out.println(hoursSleep);
                        }
                    });
                    //lp.setMargins(200, 5, 200, 5);
                    //lp.addRule(RelativeLayout.RIGHT_OF,i);
                    //np.setLayoutParams(lp);
                    rl.addView(np);

                    //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                    table.addView(row, i);


                } else if (i == 3 && np2 != null) {
                    np2.setValues(cs);

                    np2.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
                        @Override
                        public void onItemSelected(int index) {
                            hoursSleep = index + 1;
                            System.out.println(hoursSleep);
                        }
                    });
                    row.addView(text);
                    //row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
                    row.addView(np2);
                    table.addView(row, i);
                } else {


                }
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
