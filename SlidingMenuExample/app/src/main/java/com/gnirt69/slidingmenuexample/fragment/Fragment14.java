package com.gnirt69.slidingmenuexample.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;


public class Fragment14 extends Fragment {
    GraphView graph;
    View rootView;
    ListView view;
    TextView textView;
    final static int COLOR_RGB_FIRSTLINE_PRIMARY = Color.rgb(198,148,87);
    final static int COLOR_RGB_FIRSTLINE_SECONDARY= Color.rgb(161, 121, 71);
    final static int COLOR_RGB_SECONDLINE_PRIMARY = Color.rgb(182,80,104);
    final static int BACKGROUNDCOLOR_ARGB_FIRSTLINE = Color.argb(50, 227, 181, 123);
    final static int BACKGROUNDCOLOR_ARGB_SECONDLINE = Color.argb(50, 209, 113, 136);

    private static final int MAX_DATA_POINTS = 100000;
    List<String> list =new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<Double> valueList1;
    String valueList1Name;
    ArrayList<Double> valueList2;
    String valueList2Name;
    Date[] dateList1;
    Date[] dateList2;

    public Fragment14() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_14, container, false);
        view = (ListView) rootView.findViewById(R.id.listView2);
        textView = (TextView) rootView.findViewById(R.id.textview_frag14);
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,list);
        view.setAdapter(adapter);
        if(getActivity() != null){
            valueList1Name = ((MainActivity)getActivity()).getFirstValueListName();
            valueList2Name = ((MainActivity)getActivity()).getSecondValueListName();
            dateList1 = ((MainActivity)getActivity()).getFirstValueDates();
            dateList2 = ((MainActivity)getActivity()).getSecondValueDates();
            valueList1 = ((MainActivity)getActivity()).getFirstValueList();
            valueList2 = ((MainActivity)getActivity()).getSecondValueList();
            runGraph();
        }
        return rootView;
    }
    private LineGraphSeries<DataPoint> createDataLine(ArrayList<Double> valueList,Date[] dateList){
        LineGraphSeries<DataPoint> returnDataSeries = new LineGraphSeries<>();
        if(valueList != null && dateList != null) {
            for (int i = 0; i < valueList.size(); i++) {
                Double tempValue = valueList.get(i);
                returnDataSeries.appendData(new DataPoint(dateList[i], tempValue),false,MAX_DATA_POINTS);
            }
        }
        return returnDataSeries;
    }
    private void setupDoubleLinesToGraph(){

        if(valueList1 != null && valueList2 != null) {

            LineGraphSeries<DataPoint> serieslineTemp = createDataLine(valueList1,dateList1);
            LineGraphSeries<DataPoint> serieslineTemp2 = createDataLine(valueList2,dateList2);
            adapter.clear();
            adapter.notifyDataSetChanged();
            //getCorrelationList(values);
            trimListForCorrelation(valueList1,valueList2,dateList1,dateList2);
            setList(checkCorrelation(valueList1, valueList2), " Correlation: ");
            serieslineTemp.setTitle(valueList1Name);
            serieslineTemp.setDrawBackground(true);

            serieslineTemp2.setTitle(valueList2Name);
            serieslineTemp2.setDrawBackground(true);
            serieslineTemp.setColor(COLOR_RGB_FIRSTLINE_PRIMARY);
            serieslineTemp2.setColor(COLOR_RGB_SECONDLINE_PRIMARY);
            Paint paint = new Paint();
            paint.setColor(COLOR_RGB_FIRSTLINE_PRIMARY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            serieslineTemp.setBackgroundColor(BACKGROUNDCOLOR_ARGB_FIRSTLINE);
            serieslineTemp.setCustomPaint(paint);
            Paint paint2 = new Paint();
            paint2.setColor(COLOR_RGB_SECONDLINE_PRIMARY);
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(8);
            serieslineTemp2.setCustomPaint(paint2);
            serieslineTemp2.setBackgroundColor(BACKGROUNDCOLOR_ARGB_SECONDLINE);
            //graph.setTitle("Correlation between: " + valueList1Name + " and " + valueList2Name);
            textView.setText("Correlation between: " + valueList1Name + " and " + valueList2Name);


            if(dateList1.length >= dateList2.length) {
                graph.getViewport().setMinX(dateList1[0].getTime());
                if(dateList1.length < 11){
                    graph.getViewport().setMaxX(dateList1[dateList1.length-1].getTime());
                    Log.d("graphxMax","datelist1 longest. length < 11. maxX set to: "+dateList1[dateList1.length-1].getTime());

                }else{
                    graph.getViewport().setMaxX(dateList1[10].getTime());
                    Log.d("graphxMax","datelist1 longest. length >= 11 maxX set to: "+dateList1[10].getTime());
                }
                graph.addSeries(serieslineTemp);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getSecondScale().setMaxY(serieslineTemp2.getHighestValueY()+5);
                graph.getSecondScale().setMinY(serieslineTemp2.getLowestValueY()-5);
                graph.getSecondScale().addSeries(serieslineTemp2);
            }else{
                graph.getViewport().setMinX(dateList2[0].getTime());
                if(dateList2.length < 11){
                    graph.getViewport().setMaxX(dateList2[dateList2.length-1].getTime());
                    Log.d("graphxMax","datelist2 longest. length < 11. maxX set to: "+dateList2[dateList2.length-1].getTime());
                }else{
                    graph.getViewport().setMaxX(dateList2[10].getTime());
                    Log.d("graphxMax","datelist longest. length >= 11 maxX set to: "+dateList2[10].getTime());
                }
                graph.addSeries(serieslineTemp2);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getSecondScale().setMaxY(serieslineTemp.getHighestValueY()+5);
                graph.getSecondScale().setMinY(serieslineTemp.getLowestValueY()-5);
                graph.getSecondScale().addSeries(serieslineTemp);
                graph.getGridLabelRenderer().setVerticalLabelsColor(COLOR_RGB_SECONDLINE_PRIMARY);
                graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(COLOR_RGB_FIRSTLINE_PRIMARY);
            }

        }
    }
    private void trimListForCorrelation(ArrayList<Double> list1,ArrayList<Double> list2,Date[] dateList1, Date[] dateList2){
        if(list1 != null && list2 != null && dateList1 != null && dateList2 != null){
            int i = 0;
            while(list1.size() != list2.size()){
                if(list1.size() > list2.size()) {
                    if (!Arrays.asList(dateList2).contains(dateList1[i])) {
                        if(i< list1.size()){
                            list1.remove(i);
                        }
                    }else{
                        i++;
                    }
                }else{
                    if (!Arrays.asList(dateList1).contains(dateList2[i])) {
                        if(i< list2.size()){
                            list2.remove(i);
                        }
                    }else{
                        i++;
                    }
                }
                System.out.println(i);
            }
        }
    }
    private void setList(String item,String message) {
        list.add(message+item);
        adapter.notifyDataSetChanged();
    }
    private void runGraph(){

        //Här skapar vi våran graf, lämpligt nog döpt till "graph"
        graph = (GraphView) rootView.findViewById(R.id.graph2);
        //setupDoubleLinesToGraph();

        if(valueList1 != null && valueList2 != null){
            setupDoubleGraph();
            setupDoubleLinesToGraph();

        }else{
            System.out.println("shit");
        }

    }
    private String checkCorrelation(ArrayList<Double> list1, ArrayList<Double> list2) {
        double corr = 0;
        if (list1 != null && list2 != null){
            while(!(list1.size() == list2.size())){
                if(list1.size()> list2.size()){
                    list1.remove(list1.size()-1);
                }else list2.remove(list2.size()-1);
            }
            Double[] xs = list1.toArray(new Double[list1.size()]);
            Double[] ys = list2.toArray(new Double[list2.size()]);
            double[] x = toPrimitive(xs);
            double[] y = toPrimitive(ys);
            corr = new KendallsCorrelation().correlation(x,y);
        }
        String item = makeString(corr);
        return item;
    }
    private String makeString(double x){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String item = df.format(x*100)+"%";
        return item;
    }
    public static double[] toPrimitive(Double[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return null;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].doubleValue();
        }
        return result;
    }
    private void setupDoubleGraph() {
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#4DFFFFFF"));


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(Color.argb(50,255,255,255));
        graph.getLegendRenderer().setTextColor(Color.rgb(48,110, 85));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(15);
        graph.getGridLabelRenderer().setVerticalLabelsColor(COLOR_RGB_FIRSTLINE_SECONDARY);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(COLOR_RGB_SECONDLINE_PRIMARY);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.rgb(255,255,255));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.rgb(255,255,255));
        graph.getGridLabelRenderer();
    }

}
