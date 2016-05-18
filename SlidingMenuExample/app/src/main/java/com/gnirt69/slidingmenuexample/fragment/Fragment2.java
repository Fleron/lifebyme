package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by NgocTri on 10/18/2015.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.talkToDBTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Fragment2 extends Fragment implements OnTalkToDBFinish {
    String[] keys;
    private static final int MAX_DATA_POINTS = 100000;
    ArrayAdapter<String> adapter;
    List<String> list =new ArrayList<>();
    private static int x_max = 30;
    String[] values;
    String user = "";
    String pwd = "";
    talkToDBTask task;
    ArrayList<Double> mood;
    ArrayList<Double> workout;
    ArrayList<Double> sleep;
    ListView view;
    JSONObject dataObject;
    Context context;
    String[] items;
    GraphView graph;
    View rootView;
    public Fragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        context = rootView.getContext();
        setRetainInstance(true);
        view = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,list);
        view.setAdapter(adapter);
        mood = new ArrayList<>();
        workout = new ArrayList<>();
        sleep = new ArrayList<>();
        getDBvalues(4);




        return rootView;
    }
    private void setList(String item,String message) {
        list.add(message+item);
        adapter.notifyDataSetChanged();
    }
    private void getDBvalues(int request){
        task = new talkToDBTask(this,context);
        user = ((MainActivity)getActivity()).getUser();
        task.setUsername(user);
        pwd = ((MainActivity)getActivity()).getPassword();
        task.setPwd(pwd);
        task.setRequestType(request);
        task.execute();
    }
    private LineGraphSeries<DataPoint> createDataLine(ArrayList<Double> valueList){
        LineGraphSeries<DataPoint> returnDataSeries = new LineGraphSeries<>();
        if(values != null) {
            for (int i = 0; i < values.length; i++) {
                Double tempValue = Double.valueOf(values[i]);
                valueList.add(tempValue);
                returnDataSeries.appendData(new DataPoint(i, tempValue),false,MAX_DATA_POINTS);

            }
        }
        return returnDataSeries;
    }
    private LineGraphSeries<DataPoint> receivedDataLine(int key,ArrayList<Double> valueList){
        LineGraphSeries<DataPoint> returnDataSeries = new LineGraphSeries<>();
        int j = 0;
        if(keys != null) {
            for (int i = 0; i < keys.length; i++) {
                int tempKey = Integer.parseInt(keys[i]);
                Double tempValue = Double.valueOf(values[i]);
                if (tempKey == key) {
                    valueList.add(tempValue);
                    returnDataSeries.appendData(new DataPoint(j, tempValue), false, 100);
                    j++;
                }
            }
        }
        return returnDataSeries;
    }
    private void runGraph(View rootView){
        //Här nedanför hämtas värden från föregående aktivitet
        //Intent i = getIntent();

        //keys = i.getStringArrayExtra("keys");
        //values = i.getStringArrayExtra("values");


        //Här skapar vi våran graf, lämpligt nog döpt till "graph"
        graph = (GraphView) rootView.findViewById(R.id.graph);
        setupSingleGraph(graph);
        setupDataToGraph();
        /*
        //PointsGraphSeries<DataPoint> seriesPoints = receivedDataPoints(1);
        LineGraphSeries<DataPoint> serieslineSleep = receivedDataLine(1,sleep);
        LineGraphSeries<DataPoint> serieslineworkout = receivedDataLine(2,workout);
        LineGraphSeries<DataPoint> serieslineMood = receivedDataLine(3,mood);
        setList(checkCorrelation(sleep,mood),"correlation between sleep and mood: ");
        setList(makeString(getMean(sleep)),"Average sleep: ");
        setList(makeString((getMax(sleep))),"Max sleep: ");
        setList(makeString(getMin(sleep)),"Min sleep: ");
        setList(checkCorrelation(sleep,workout),"correlation between sleep and workout: ");
        setList(checkCorrelation(workout,mood),"correlation between workout and mood: ");
        setList(makeString((getMean(mood))),"Average mood: ");

        serieslineMood.setColor(Color.parseColor("#CC5920"));
        serieslineSleep.setColor(Color.BLUE);


        //Stil på grafen, alltså själva grafen och inte linjerna

        serieslineSleep.setTitle("Sleep(hours)");
        serieslineMood.setTitle("Mood");

        serieslineMood.setDrawBackground(true);
        serieslineMood.setBackgroundColor(Color.argb(50,204,255,204));
        serieslineSleep.setDrawBackground(true);
        serieslineSleep.setBackgroundColor(Color.argb(70,255,255,255));


        graph.addSeries(serieslineSleep);
        graph.getSecondScale().addSeries(serieslineMood);
        */
    }
    private void valuesForSingleGraph(String key){
        try {
            JSONArray jsonValues = dataObject.getJSONArray(key);
            values = new String[jsonValues.length()];
            for(int i = 0; i < jsonValues.length(); i++){
                values[i] = jsonValues.getString(i);
            }
            System.out.println(Arrays.toString(values));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setupDataToGraph(){
        if(dataObject != null){
            Iterator<String> keys = dataObject.keys();
            String key = keys.next();

            valuesForSingleGraph(key);

            ArrayList<Double> temp= new ArrayList<>();
            LineGraphSeries<DataPoint> serieslineTemp =createDataLine(temp);
            
            setList(makeString(getMean(temp)),"Average "+key+": ");
            setList(makeString((getMax(temp))),"Max "+key+": ");
            setList(makeString(getMin(temp)),"Min "+key+": ");
            serieslineTemp.setColor(Color.BLUE);
            serieslineTemp.setTitle(key);
            serieslineTemp.setDrawBackground(true);
            serieslineTemp.setBackgroundColor(Color.argb(50,204,255,204));
            graph.addSeries(serieslineTemp);

        }
    }
    private void setupSomethingToGraph() {
        if(dataObject != null){
            Iterator<String> keys = dataObject.keys();
            while(keys.hasNext()){
                String key = keys.next();
                //paintSingleGraph(key);
            }
        }
    }
    private String makeString(double x){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String item = df.format(x);
        return item;
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
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);


        String item = df.format(corr*100)+"%";

        return item;
    }
    private double getMean(ArrayList<Double> x){
        Double[] xs = x.toArray(new Double[x.size()]);
        double [] xh = toPrimitive(xs);
        return StatUtils.mean(xh);
    }
    private double getMax(ArrayList<Double> x){
        Double[] xs = x.toArray(new Double[x.size()]);
        double [] xh = toPrimitive(xs);
        return StatUtils.max(xh);
    }
    private double getMin(ArrayList<Double> x){
        Double[] xs = x.toArray(new Double[x.size()]);
        double [] xh = toPrimitive(xs);
        return StatUtils.min(xh);
    }
    private PointsGraphSeries<DataPoint> receivedDataPoints(int key){
        PointsGraphSeries<DataPoint> returnDataSeries = new PointsGraphSeries<>();
        int j = 0;
        for(int k = 0; k < keys.length; k++){
            int tempKey = Integer.parseInt(keys[k]);
            int tempValue = Integer.parseInt(values[k]);
            if(tempKey == key){

                returnDataSeries.appendData(new DataPoint(j,tempValue),false,100);

                j++;
            }
        }
        return returnDataSeries;
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
    private void setupSingleGraph(GraphView graph){
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("x-axel");

        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMaxX(x_max);
        graph.getViewport().setMinX(0);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#4DFFFFFF"));


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.setTitle("Activity");
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
    }
    private void setupGraph(GraphView graph){
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("x-axel");

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(17);
        graph.getViewport().setMinX(0);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#4DFFFFFF"));
        graph.getSecondScale().setMinY(-5);
        graph.getSecondScale().setMaxY(5);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.setTitle("Activity");
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.parseColor("#CC5920"));
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
    }
    @Override
    public void onTaskCompleted(int request) {
        /*keys = task.getKeys();
        values = task.getValues();
        */
        this.dataObject = task.getDataObject();
        runGraph(rootView);
    }
    @Override
    public void onTaskFailed() {
        System.out.println("fail");
    }
}
