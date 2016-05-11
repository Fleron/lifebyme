package com.gnirt69.slidingmenuexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gnirt69.slidingmenuexample.adapter.SlidingMenuAdapter;
import com.gnirt69.slidingmenuexample.fragment.Fragment1;
import com.gnirt69.slidingmenuexample.fragment.Fragment2;
import com.gnirt69.slidingmenuexample.fragment.Fragment3;
import com.gnirt69.slidingmenuexample.fragment.Fragment4;
import com.gnirt69.slidingmenuexample.fragment.Fragment5;
import com.gnirt69.slidingmenuexample.fragment.Fragment6;
import com.gnirt69.slidingmenuexample.fragment.Fragment7;
import com.gnirt69.slidingmenuexample.fragment.Fragment8;
import com.gnirt69.slidingmenuexample.fragment.Fragment9;
import com.gnirt69.slidingmenuexample.model.ItemSlideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NgocTri on 10/18/2015.
 */
public class MainActivity extends ActionBarActivity implements SensorEventListener{
    public static String steps_taken;
    receiver receiver;
    private TextView textView;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    public static final String RECEIVE_STEPS = "receive_steps";
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        receiver = new receiver();
        IntentFilter intentFilter = new IntentFilter(RECEIVE_STEPS);

        Intent intent2 = new Intent(MainActivity.this,stepCounterService.class);
        startService(intent2);
        bManager.registerReceiver(receiver,intentFilter);

        //init stepCounter
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        //Init component
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.home3, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.arrow2, "Statistics"));
        listSliding.add(new ItemSlideMenu(R.drawable.calendar, "Calendar"));
        listSliding.add(new ItemSlideMenu(R.drawable.social2, "Social"));
        listSliding.add(new ItemSlideMenu(R.drawable.settings2, "Settings"));
        listSliding.add(new ItemSlideMenu(R.drawable.aboutus2, "About"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        System.out.println(username);
        System.out.println(password);
        System.out.println("from main");

        //textView.findViewById(R.id.textViewStep);
        //textView = (TextView)findViewById(R.id.textViewStep);
        //Display icon to open/ close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set title
        setTitle(listSliding.get(0).getTitle());
        //item selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);

        //Display fragment 1 when start
        replaceFragment(0);
        //Hanlde on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set title
                setTitle(listSliding.get(position).getTitle());
                //item selected
                listViewSliding.setItemChecked(position, true);
                //Replace fragment
                replaceFragment(position);
                //Close menu
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    // Handle "Back"-button press
    @Override
    public void onBackPressed () {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            System.out.println("count=0");
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    //Create method replace fragment

    public void replaceFragment(int pos) {
        Fragment fragment = null;

        String fragmenttag = "fragment";
        switch (pos) {
            case 0:
                fragment = new Fragment1();
                fragmenttag += "1";
                break;
            case 1:
                fragment = new Fragment2();
                fragmenttag += "2";
                break;
            case 2:
                fragment = new Fragment3();
                fragmenttag += "3";
                break;
            case 3:
                fragment = new Fragment4();
                fragmenttag += "4";
                break;
            case 4:
                fragment = new Fragment5();
                fragmenttag += "5";
                break;
            case 5:
                fragment = new Fragment6();
                fragmenttag += "6";
                break;
            case 6:
                fragment = new Fragment7();
                fragmenttag += "7";
                break;

            case 7:
                fragment = new Fragment8();
                fragmenttag += "8";
                break;

            case 8:
                fragment = new Fragment9();
                fragmenttag += "9";
                break;

            default:
                fragment = new Fragment1();
                fragmenttag += "1";
                break;
        }

        if(null!=fragment) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            System.out.println(fragmenttag);
            transaction.replace(R.id.main_content, fragment,fragmenttag);
            transaction.addToBackStack(null);
            transaction.commit();
            listViewSliding.setItemChecked(pos, true);
            if(pos < 6) {
                setTitle(listSliding.get(pos).getTitle());
            }
        }
    }

    public String getUser(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        //System.out.println("gick in i sensor");
        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            //System.out.println("Step Counter Detected : " + value);
            //Fragment fragment = getFragmentManager().findFragmentByTag("fragment4");
            //TextView view = (TextView)fragment.getView().findViewById(R.id.textViewStep);
            //view.setText("Step Counter Detected : " + value);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
           // System.out.println("Step Detector Detected : " + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    protected void onStop() throws IllegalArgumentException {
      //  unregisterReceiver(receiver);
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }



    private class receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("received");
            if(action.equalsIgnoreCase(RECEIVE_STEPS)){
                int int_value = intent.getIntExtra("step",0);
                steps_taken = int_value +"";
                Fragment fragment = getFragmentManager().findFragmentByTag("fragment3");
                try {
                    //TextView view = (TextView) fragment.getView().findViewById(R.id.textViewStep);
                    //view.setText("Step Counter Detected : " + steps_taken);
                }
                catch(NullPointerException e){
                    e.getLocalizedMessage();
                }
            }

        }
    }
}
