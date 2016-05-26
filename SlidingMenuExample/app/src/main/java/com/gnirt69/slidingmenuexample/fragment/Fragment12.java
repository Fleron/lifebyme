
package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by NgocTri on 10/18/2015.
 */

        import android.app.Fragment;
        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.CornerPathEffect;
        import android.graphics.LinearGradient;
        import android.graphics.Paint;
        import android.graphics.Shader;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.gnirt69.slidingmenuexample.MainActivity;
        import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
        import com.gnirt69.slidingmenuexample.R;
        import com.gnirt69.slidingmenuexample.model.ButtonAdapter;
        import com.gnirt69.slidingmenuexample.talkToDBTask;
        import com.jjoe64.graphview.GraphView;
        import com.jjoe64.graphview.GridLabelRenderer;
        import com.jjoe64.graphview.LegendRenderer;
        import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
        import com.jjoe64.graphview.series.DataPoint;
        import com.jjoe64.graphview.series.DataPointInterface;
        import com.jjoe64.graphview.series.LineGraphSeries;
        import com.jjoe64.graphview.series.OnDataPointTapListener;
        import com.jjoe64.graphview.series.PointsGraphSeries;
        import com.jjoe64.graphview.series.Series;

        import org.apache.commons.math3.stat.StatUtils;
        import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
        import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.lang.reflect.Array;
        import java.math.RoundingMode;
        import java.text.DecimalFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.Iterator;
        import java.util.List;

public class Fragment12 extends Fragment implements OnTalkToDBFinish {
    String[] keys,values,items;
    private static final int MAX_DATA_POINTS = 100000;
    ButtonAdapter adapter;
    String firstVariable;
    List<String> list =new ArrayList<>();
    private static int x_max = 30;
    String user = "";
    String pwd = "";
    talkToDBTask task;
    ArrayList<Double> mood,workout,sleep;
    ListView view;
    TextView textView;
    TextView textViewMaxMinAvg;
    JSONObject dataObjectForeign;
    JSONObject dataObjectForeignDates;
    JSONObject dataObject;
    JSONObject dataObjectDates;
    Context context;
    GraphView graph;
    View rootView;
    public Fragment12() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        context = rootView.getContext();
        setRetainInstance(true);
        view = (ListView) rootView.findViewById(R.id.listView);
        textView = (TextView) rootView.findViewById(R.id.textview_frag12);
        textViewMaxMinAvg = (TextView) rootView.findViewById(R.id.textViewStatistics);

        adapter = new ButtonAdapter(((MainActivity)getActivity()),list,this);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = parent.getItemAtPosition(position);
                String objString = obj.toString();
                int end = objString.indexOf(":");
                String k = objString.substring(0,end);

                if(checkInVariables(k)){
                    graph.removeAllSeries();
                    System.out.println(k);
                    if(getActivity() != null){
                        ((MainActivity)getActivity()).setSecondValueListName(k);
                        ((MainActivity)getActivity()).setSecondValueList(createArrayList(k,dataObject));
                        ((MainActivity)getActivity()).setSecondValueDates(getDatesFromObject(k,dataObjectDates));
                        ((MainActivity) getActivity()).replaceFragment(13);
                    }
                }else{
                    ((MainActivity)getActivity()).setSecondValueListName(k);
                    ((MainActivity)getActivity()).setSecondValueList(createArrayList(k,dataObjectForeign));
                    ((MainActivity)getActivity()).setSecondValueDates(getDatesFromObject(k,dataObjectForeignDates));
                    ((MainActivity) getActivity()).replaceFragment(13);
                }

            }
        });
        mood = new ArrayList<>();
        workout = new ArrayList<>();
        sleep = new ArrayList<>();

        firstVariable = ((MainActivity)getActivity()).getVID();
        getDBvalues(4);

        return rootView;
    }
    public void onButtonClick(ButtonAdapter parent, int position){
        //String objString = obj.toString();
        //int end = objString.indexOf("");

        String k = (String) parent.getName(position);
        System.out.println(k+ " HEEEELLO");
        if(checkInVariables(k)){
            graph.removeAllSeries();
            System.out.println(k);
            if(getActivity() != null){
                ((MainActivity)getActivity()).setSecondValueListName(k);
                ((MainActivity)getActivity()).setSecondValueList(createArrayList(k,dataObject));
                ((MainActivity)getActivity()).setSecondValueDates(getDatesFromObject(k,dataObjectDates));
                ((MainActivity) getActivity()).replaceFragment(13);
            }
        }else{
            ((MainActivity)getActivity()).setSecondValueListName(k);
            ((MainActivity)getActivity()).setSecondValueList(createArrayList(k,dataObjectForeign));
            ((MainActivity)getActivity()).setSecondValueDates(getDatesFromObject(k,dataObjectForeignDates));
            ((MainActivity) getActivity()).replaceFragment(13);
        }

    }
    private boolean checkInVariablesForeign(String item, JSONObject object){
        Iterator<String> keys = object.keys();
        while(keys.hasNext()){
            if(keys.next().equals(item)){
                return true;
            }
        }
        return false;
    }
    private boolean checkInVariables(String item){
        Iterator<String> keys = dataObject.keys();
        while(keys.hasNext()){
            if(keys.next().equals(item)){
                return true;
            }
        }
        return false;
    }
    private ArrayList<Double> createArrayList(String key,JSONObject dataObject){
        String[] temp = getValuesFromObject(key,dataObject);
        ArrayList<Double> returnList = new ArrayList<>();
        if(temp != null) for (String aTemp : temp) returnList.add(Double.valueOf(aTemp));
        return returnList;
    }
    private void setList(String item,String message) {
        list.add(item + message);
        adapter.setName(item);
        adapter.notifyDataSetChanged();
    }
    private void getValueFromDBSingleVID(int request,String VID){
        task = new talkToDBTask(this,context);
        task.setVariableID(VID);
        task.setRequestType(request);
        task.execute();

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
    private LineGraphSeries<DataPoint> createDataLine(ArrayList<Double> valueList,String[] values,Date[] dates){
        System.out.println(dates.toString());
        LineGraphSeries<DataPoint> returnDataSeries = new LineGraphSeries<>();
        if(values != null) {
            for (int i = 0; i < values.length; i++) {
                Double tempValue = Double.valueOf(values[i]);
                valueList.add(tempValue);
                returnDataSeries.appendData(new DataPoint(dates[i], tempValue),false,MAX_DATA_POINTS);

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
    private void runGraph(View rootView,String firstKey){
        //Här skapar vi våran graf, lämpligt nog döpt till "graph"
        graph = (GraphView) rootView.findViewById(R.id.graph);
        setupSingleGraph(graph);
        System.out.println(firstKey);
        if(firstKey != null){
            //Iterator<String> keys = dataObject.keys();
            //String key = keys.next();
            if(!checkInVariables(firstKey) && dataObjectForeign != null){
                System.out.println("pass check in variables = false");
                Iterator<String> keys = dataObjectForeign.keys();
                String key = keys.next();
                setupDataToGraph(key,false);
            }else{
                if(dataObjectForeign!= null) {
                    System.out.println(dataObjectForeign.toString());
                    if (checkInVariablesForeign(firstKey, dataObjectForeign)){
                        Iterator<String> keys = dataObjectForeign.keys();
                        String key = keys.next();
                        setupDataToGraph(key, false);
                    }
                }else{
                    setupDataToGraph(firstKey,true);
                }
            }
        }else{
            Iterator<String> keys = dataObject.keys();
            String key = keys.next();
            setupDataToGraph(key,true);
        }

    }
    private String[] getValuesFromObject(String key,JSONObject dataObject){
        try {

            JSONArray jsonValues = dataObject.getJSONArray(key);
            String [] returnString = new String[jsonValues.length()];
            for(int i = 0; i < jsonValues.length(); i++){
                returnString[i] = jsonValues.getString(i);
            }

            return returnString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Date[] getDatesFromObject(String key,JSONObject dataObjectDates){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            JSONArray jsonValues = dataObjectDates.getJSONArray(key);
            Date[] returnDates = new Date[jsonValues.length()];
            for(int i = 0; i < jsonValues.length(); i++){
                returnDates[i] = formatter.parse(jsonValues.getString(i));
            }
            return returnDates;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String[] valuesForSingleGraph(String key,String[] values,JSONObject dataObject){
        try {
            System.out.println("in values for single graph: "+dataObject);
            JSONArray jsonValues = dataObject.getJSONArray(key);
            values = new String[jsonValues.length()];
            for(int i = 0; i < jsonValues.length(); i++){
                values[i] = jsonValues.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }return values;
    }
    private ArrayList<Double> makeArrayListFromStringList(String[] array){
        ArrayList<Double> valueList = new ArrayList<>();
        if(array != null) {
            for (int i = 0; i < array.length; i++) {
                Double tempValue = Double.valueOf(array[i]);
                valueList.add(tempValue);
            }
        }
        return valueList;
    }
    private void setupDataToGraph(String key,boolean inList){
        JSONObject dataObject;
        JSONObject dataObjectDates;
        if(this.dataObject != null && this.dataObjectDates != null){
            Date[] dateTemp;
            if(inList){
                dataObject = this.dataObject;
                dataObjectDates = this.dataObjectDates;
                values = valuesForSingleGraph(key,values,dataObject);
                dateTemp = getDatesFromObject(key,dataObjectDates);

            }else{
                dataObject = this.dataObjectForeign;
                dataObjectDates = this.dataObjectForeignDates;
                values = valuesForSingleGraph(key,values,dataObject);
                dateTemp = getDatesFromObject(key,dataObjectDates);
            }
            dataObject = this.dataObject;
            dataObjectDates = this.dataObjectDates;
            ArrayList<Double> temp= new ArrayList<>();
            final LineGraphSeries<DataPoint> serieslineTemp =createDataLine(temp,values,dateTemp);
            if(getActivity()!= null){
                ((MainActivity)getActivity()).setFirstValueList(temp);
                if(inList){
                    ((MainActivity)getActivity()).setFirstValueListName(key);
                }
                ((MainActivity)getActivity()).setFirstValueDates(dateTemp);
            }

            adapter.clear();
            adapter.notifyDataSetChanged();
            String tempText = "Max: "+makeString(getMax(temp))+ "      Min: "+makeString(getMin(temp)) + "      Average: "+ makeString(getMean(temp));
            textViewMaxMinAvg.setText(tempText);

            getCorrelationList(values,dateTemp,dataObject,dataObjectDates);

            serieslineTemp.setTitle(key);
            serieslineTemp.setDrawBackground(true);
            Paint paint = new Paint();
            paint.setColor(Color.argb(255,198,148,87));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            serieslineTemp.setBackgroundColor(Color.argb(50, 227, 181, 123));
            serieslineTemp.setCustomPaint(paint);

            //graph.setTitle(key);
            graph.getViewport().setMinX(dateTemp[0].getTime());
            if(dateTemp.length < 11){
                graph.getViewport().setMaxX(dateTemp[dateTemp.length-1].getTime());
            }else{
                graph.getViewport().setMaxX(dateTemp[10].getTime());
            }
            if(!inList){
                String title = ((MainActivity)getActivity()).getFirstValueListName();
                System.out.println("title is: "+title+" inList = false");
                textView.setText(title);
            }else{
                System.out.println("title is: "+key);
                textView.setText(key);
            }
            graph.getViewport().setXAxisBoundsManual(true);

            graph.addSeries(serieslineTemp);

        }

    }
    private void setupDoubleLinesToGraph(String key1,String key2){
//        String[] key1Values = getValuesFromObject(key1);
//        String[] key2Values = getValuesFromObject(key2);
//        if(key1Values != null && key2Values!= null) {
//            ArrayList<Double> temp1 = new ArrayList<>();
//            LineGraphSeries<DataPoint> serieslineTemp = createDataLine(temp1, key1Values);
//            ArrayList<Double> temp2 = new ArrayList<>();
//            LineGraphSeries<DataPoint> serieslineTemp2 = createDataLine(temp2, key2Values);
//            adapter.clear();
//            adapter.notifyDataSetChanged();
//            //getCorrelationList(values);
//
//            setList(checkCorrelation(temp1, temp2), " Correlation: ");
//            serieslineTemp.setTitle(key1);
//            serieslineTemp.setDrawBackground(true);
//
//            serieslineTemp2.setTitle(key2);
//            serieslineTemp2.setDrawBackground(true);
//            Paint paint = new Paint();
//            paint.setColor(Color.argb(255, 198, 148, 87));
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(8);
//            serieslineTemp.setBackgroundColor(Color.argb(50, 227, 181, 123));
//            serieslineTemp.setCustomPaint(paint);
//            Paint paint2 = new Paint();
//            paint2.setColor(Color.argb(255, 182, 80, 104));
//            paint2.setStyle(Paint.Style.STROKE);
//            paint2.setStrokeWidth(8);
//            serieslineTemp2.setCustomPaint(paint2);
//            serieslineTemp2.setBackgroundColor(Color.argb(50, 209, 113, 136));
//            graph.setTitle("Correlation between: " + key1 + " and " + key2);
//            graph.addSeries(serieslineTemp);
//            graph.getSecondScale().setMaxY(serieslineTemp2.getHighestValueY());
//            graph.getSecondScale().setMinY(serieslineTemp2.getLowestValueY());
//            graph.getSecondScale().addSeries(serieslineTemp2);
//        }
    }
    private void trimListForCorrelation(ArrayList<Double> list1,ArrayList<Double> list2,Date[] dateList1
            , Date[] dateList2,ArrayList<Double>templist1,ArrayList<Double>templist2 ){
        for (Double temp:list1){
            templist1.add(temp);
        }
        for (Double temp2:list2){
            templist2.add(temp2);
        }

        if(templist1 != null && templist2 != null && dateList1 != null && dateList2 != null){
            int i = 0;
            System.out.println("datelist1: " + dateList1.length);
            System.out.println("list1: " + list1.size());
            System.out.println("datelist2: " + dateList2.length);
            System.out.println("list2: " + list2.size());

            while(templist1.size() != templist2.size()){
                if(templist1.size() > templist2.size()) {
                    if (!Arrays.asList(dateList2).contains(dateList1[i])) {
                        if(i< templist1.size()){
                            templist1.remove(i);
                        }
                    }else{
                        i++;
                    }
                }else{
                    if (!Arrays.asList(dateList1).contains(dateList2[i])) {
                        if(i< templist2.size()){
                            templist2.remove(i);
                        }
                    }else{
                        i++;
                    }
                }
            }
        }
    }
    private void getCorrelationList(String[] checkList,Date[] dateCheck,JSONObject dataObject,JSONObject dataObjectDates){
        System.out.println("in i getCorrelation");
        if(dataObject != null) {
            Iterator<String> keys = dataObject.keys();
            ArrayList<Double> checkArray = makeArrayListFromStringList(checkList);

            while(keys.hasNext()) {
                String key = keys.next();
                String[] valueTemp = getValuesFromObject(key,dataObject);
                Date[] dateTemp = getDatesFromObject(key, dataObjectDates);

                if(!(Arrays.equals(valueTemp,checkList))){
                    ArrayList<Double> valueTempDouble = makeArrayListFromStringList(valueTemp);

                    ArrayList<Double>templist1 = new ArrayList<>(checkArray.size());
                    ArrayList<Double>templist2 = new ArrayList<>(valueTempDouble.size());

                    trimListForCorrelation(checkArray,valueTempDouble,dateCheck,dateTemp,templist1,templist2);
                    String corr = checkCorrelation(templist1,templist2);
                    setList(key," and "+firstVariable+": " + corr);

                }

            }
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
        if(list1.size() == list2.size()) {
            Double[] xs = list1.toArray(new Double[list1.size()]);
            Double[] ys = list2.toArray(new Double[list2.size()]);
            double[] x = toPrimitive(xs);
            double[] y = toPrimitive(ys);
            corr = new SpearmansCorrelation().correlation(x, y);
        }
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);


        String item = df.format(corr*100)+"%";

        return item;
    }
    private double getMean(ArrayList<Double> x){
        if(x.size() > 0) {
            Double[] xs = x.toArray(new Double[x.size()]);
            double[] xh = toPrimitive(xs);
            return StatUtils.mean(xh);
        }return 0;
    }
    private double getMax(ArrayList<Double> x){
        if(x.size() > 0) {
            Double[] xs = x.toArray(new Double[x.size()]);
            double[] xh = toPrimitive(xs);
            return StatUtils.max(xh);
        }return 0;
    }
    private double getMin(ArrayList<Double> x){
        if(x.size() > 0) {
            Double[] xs = x.toArray(new Double[x.size()]);
            double[] xh = toPrimitive(xs);
            return StatUtils.min(xh);
        }return 0;
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
        // graph.getGridLabelRenderer().setHorizontalAxisTitle("Day");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));


        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#4DFFFFFF"));


        graph.getLegendRenderer().setVisible(false);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.setTitleColor(Color.rgb(255,255,255));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.rgb(255,255,255));
        graph.getGridLabelRenderer().setGridColor(Color.rgb( 59,135,104));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.rgb(255,255,255));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.rgb(255,255,255));
    }
    private void setupDoubleGraph(GraphView graph){
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("x-axel");

        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMaxX(x_max);
        graph.getViewport().setMinX(0);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#4DFFFFFF"));


        graph.getLegendRenderer().setVisible(false);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.rgb(161,121, 71));
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.rgb(182,80,104));
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
        if(request == 4){
            this.dataObject = task.getDataObject();
            this.dataObjectDates = task.getDataObjectDates();

            if(checkInVariables(firstVariable)){
                runGraph(rootView,firstVariable);
            }else{
                getValueFromDBSingleVID(19,firstVariable);
            }
        }else if(request == 19){
            this.dataObjectForeign = task.getDataObject();
            System.out.println(this.dataObjectForeign);
            this.dataObjectForeignDates = task.getDataObjectDates();
            Iterator<String> keys = dataObjectForeign.keys();
            firstVariable = keys.next();
            runGraph(rootView,firstVariable);
        }

    }
    @Override
    public void onTaskFailed(int responseCode) {
        Log.d("lifebyme",this.getTag() +" onTaskFailed() ran.");
    }
    @Override
    public void onPause() {
        if(task!= null &&task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        super.onPause();
    }
}
