package com.gnirt69.slidingmenuexample.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.MainActivity;
import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.fragment.Fragment12;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fleron on 2016-05-20.
 */
public class ButtonAdapter extends BaseAdapter {
    List<String> ButtonText;
    ArrayList<String> name;

    Context context;
    Fragment12 frag;
    ButtonAdapter adapter;
    private static LayoutInflater inflater;

   public ButtonAdapter(MainActivity mainActivity, List<String> names, Fragment12 frag){
       ButtonText = names;
       adapter = this;
       name = new ArrayList<>();
       this.frag = frag;
       context = mainActivity;
       inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }
    public void setName(String name){
        this.name.add(name);
    }
    public String getName(int position){
        return this.name.get(position);
    }
    @Override
    public int getCount() {
        return ButtonText.size();
    }

    @Override
    public Object getItem(int position) {
        return ButtonText.get(position);
    }
    public void clear(){
        ButtonText.clear();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.button_share,parent,false);
        }
        Button btn = (Button) convertView.findViewWithTag("buttonShare");
        btn.setText(ButtonText.get(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.onButtonClick(adapter,position);
            }
        });
        return convertView;
    }
}
