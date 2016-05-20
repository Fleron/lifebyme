package com.gnirt69.slidingmenuexample.fragment;/**
 * Created by Martin 2016-05-19.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.OnTalkToDBFinish;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.model.ArrayButtonAdapter;
import com.gnirt69.slidingmenuexample.model.ButtonAdapter;
import com.gnirt69.slidingmenuexample.talkToDBTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment13 extends Fragment implements OnTalkToDBFinish{


    Context context;
    View rootView;
    String username;
    String password;
    String GID;
    talkToDBTask task;
    String [] sharedVarName;
    String [] sharedVarID;
    String [] notSharedName;
    String [] notSharedID;
    ArrayList<Button> btnListShared =  new ArrayList<>();
    ArrayList<Button> btnListUnshared =  new ArrayList<>();
    LinearLayout.LayoutParams params;
    LinearLayout ll;
    LinearLayout ll2;
    int requestType;
    ArrayAdapter<Button> adapterShare;
    ArrayAdapter<Button> adapterUnshare;
    ArrayButtonAdapter btnAdapterShare;
    ArrayButtonAdapter btnAdapterUnshare;
    List<Button> listShare = new ArrayList<>();
    List<Button> listUnshare = new ArrayList<>();

    public Fragment13() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_var, container, false);
        setRetainInstance(true);
        context = rootView.getContext();
        getUsername();
        getPassword();

        ListView view = (ListView) rootView.findViewById(R.id.shared_var_list);
        //adapterShare = new ArrayAdapter<Button>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listShare);
        btnAdapterShare = new ArrayButtonAdapter(getActivity().getApplicationContext(),btnListShared,this);
        view.setAdapter(btnAdapterShare);

        ListView view2 = (ListView) rootView.findViewById(R.id.unshared_var_list);
        btnAdapterUnshare = new ArrayButtonAdapter(getActivity().getApplicationContext(),btnListUnshared,this);
        //adapterUnshare = new ArrayAdapter<Button>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listUnshare);
        view2.setAdapter(btnAdapterUnshare);

        getGID();
        runDBtaskGetVar(15);

        return rootView;
    }

    public void getUsername(){
        username = ((MainActivity)getActivity()).getUser();
    }

    public void getPassword(){
        password = ((MainActivity)getActivity()).getPassword();
    }

    public void getGID(){
        GID = ((MainActivity)getActivity()).getGID();
    }


    void populateSharedVar(){
        ll = (LinearLayout) rootView.findViewById(R.id.shared_var);
        System.out.println("in i populate share var");

        if(sharedVarName != null && sharedVarName.length > 0){
            System.out.println(sharedVarName.length);
            for (int i = 0; i < sharedVarName.length; i++) {
                Button btn = new Button(getActivity());
                btn.setText(sharedVarName[i]+" (click to unshare)");
                btn.setBackgroundResource(R.drawable.mybutton);
                btn.setTextSize(20);
                btn.setId(i);
                btn.setTag(R.id.TAG_ID,"true");
                btn.setTag(R.id.TAG_NAME,sharedVarName[i]);
                btn.setTextColor(Color.parseColor("#157065"));
                btn.setTypeface(null, Typeface.BOLD);
                btnListShared.add(btn);
                btnAdapterShare.notifyDataSetChanged();
                params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                params.setMargins(0,5,0,0);

                //ll.addView(btn,params);
            }

        }



    }

    void populateNotSharedVar(){
        System.out.println("in i populate unshare var");
        ll2 = (LinearLayout) rootView.findViewById(R.id.not_shared_var);
        if(notSharedName != null && notSharedName.length > 0){
            System.out.println(Arrays.toString(notSharedName));
            for (int i = 0; i < notSharedName.length; i++) {
                Button btn = new Button(getActivity());
                btn.setText(notSharedName[i]+" (click to share)");
                btn.setBackgroundResource(R.drawable.mybutton);
                btn.setTextSize(20);
                btn.setId(i);
                btn.setTag(R.id.TAG_ID,"false");
                btn.setTag(R.id.TAG_NAME,notSharedName[i]);

                btnListUnshared.add(btn);
                btnAdapterUnshare.notifyDataSetChanged();

            }
        }
    }

    private void shareVar (int bid){
        System.out.println("in i share var");
        ll = (LinearLayout) rootView.findViewById(R.id.shared_var);
        Button btn2 = new Button(getActivity());
        btn2.setText(notSharedName[bid]+" (click to unshare)");
        btn2.setBackgroundResource(R.drawable.mybutton);
        btn2.setTextSize(20);
        btn2.setId(bid);
        setListShare(btn2);
        btn2.setTag("true");
        btn2.setTextColor(Color.parseColor("#157065"));
        btn2.setTypeface(null, Typeface.BOLD);

        params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.setMargins(0,5,0,0);
        setListShare(btn2);
        //ll.addView(btn2,params);
    }

    public void setListShare(Button button){
        System.out.println("settling list share with button: "+button.getId());
        btnListShared.add(button);
        btnAdapterShare.notifyDataSetChanged();
    }
    public void setListUnshare(Button button){
        System.out.println("settling list unshare with button: "+button.getId());
        btnListUnshared.add(button);
        btnAdapterUnshare.notifyDataSetChanged();
    }

    public void removeListShare(Button button){
        System.out.println("removing list share with button: "+button.getId());
        btnListShared.remove(button);
        btnAdapterShare.notifyDataSetChanged();
    }
    public void removeListUnshare(Button button){
        System.out.println("removing list ushare with button: "+button.getId());
        btnListUnshared.remove(button);
        btnAdapterUnshare.notifyDataSetChanged();
    }

    private void unshareVar (int buttonID){
        System.out.println("in i unshare var");
        ll2 = (LinearLayout) rootView.findViewById(R.id.not_shared_var);
        Button btn3 = new Button(getActivity());
        btn3.setText(sharedVarName[buttonID]+" (click to share)");
        btn3.setBackgroundResource(R.drawable.mybutton);
        btn3.setTextSize(20);
        btn3.setId(buttonID);
        btn3.setTextColor(Color.parseColor("#157065"));
        btn3.setTypeface(null, Typeface.BOLD);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ID: "+v.getId());
               // shareVar(v.getId());
                //ll2.removeView(v);
            }
        });
        params = (LinearLayout.LayoutParams) ll2.getLayoutParams();
        params.setMargins(0,5,0,0);
       // ll2.addView(btn3,params);
    }

    private void runDBtaskGetVar(int request){

        task = new talkToDBTask(this, context);
        requestType = request;
        task.setUsername(username);
        task.setPwd(password);
        task.setGroupID(GID);
        task.setRequestType(requestType);
        task.execute();
    }

    @Override
    public void onTaskCompleted(int request) {
        sharedVarName = task.getSharedVarName();
        sharedVarID = task.getSharedVarID();
        notSharedName = task.getNotSharedVarName();
        notSharedID = task.getNotSharedVarID();
        populateSharedVar();
        populateNotSharedVar();
    }
    @Override
    public void onTaskFailed() {

    }
}

