package com.gnirt69.LifeByME.fragment;/**
 * Created by Martin 2016-04-20.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.gnirt69.LifeByME.MainActivity;
import com.gnirt69.LifeByME.R;

import java.util.Calendar;

public class Fragment3 extends Fragment {

    public Fragment3(){}

    CalendarView calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment3, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        calendar=(CalendarView) getActivity().findViewById(R.id.calendarView1);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                System.out.println(year+"-"+month+"-"+dayOfMonth);
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.YEAR, year);
                tempCalendar.set(Calendar.MONTH, month);
                tempCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long milliTime = tempCalendar.getTimeInMillis();
                if(getActivity() != null){
                    ((MainActivity)getActivity()).setCalendarTime(milliTime);
                    calendar.setDate(milliTime,true,true);
                    ((MainActivity) getActivity()).replaceFragment(0);
                }




            }
        });

        //initializes the calendarview
        initializeCalendar(calendar);

    }

    public void initializeCalendar(CalendarView calendar) {
        // TODO Auto-generated method stub


        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);
        //calendar.setFocusedMonthDateColor(Color.argb(255,255,255,255));
        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar

        calendar.setFirstDayOfWeek(2);
        if(getActivity() != null){
            long milliTime = ((MainActivity)getActivity()).getCalendarTime();
            if(milliTime != 0){
                calendar.setDate(milliTime,true,true);
            }
        }



        /**
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                curDate =String.valueOf(d);
            }
        });


        calendar.setOnDateChangeListener(new OnDateChangeListener() {


            //show the selected date as a toast

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Toast.makeText(getActivity().getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

            }

        })

        ;
         **/

    }
}