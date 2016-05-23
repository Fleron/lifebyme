package com.gnirt69.slidingmenuexample.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.gnirt69.slidingmenuexample.R;
import com.gnirt69.slidingmenuexample.fragment.Fragment13;

import java.util.ArrayList;

/**
 * Created by fleron on 2016-05-20.
 */
public class ArrayButtonAdapter extends ArrayAdapter<Button> {
    ArrayList<Button> list;
    Fragment13 frag;
    ViewGroup.LayoutParams params;
    public ArrayButtonAdapter(Context context, ArrayList<Button> objects, Fragment13 frag) {
        super(context,0, objects);
        this.list = objects;
        this.frag = frag;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Button btn = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.button_share,parent,false);
        }

        Button btnTest = (Button) convertView.findViewWithTag("buttonShare");
        //params = parent.getLayoutParams();
        //params.height = list.size()*150;
        //parent.setLayoutParams(params);
        //System.out.println(parent.getId()+" has size: "+ params.height);
        btnTest.setText(list.get(position).getText());
        btnTest.setId(list.get(position).getId());
        btnTest.setTextColor(Color.parseColor("#157065"));
        btnTest.setTypeface(null, Typeface.BOLD);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getTag(R.id.TAG_ID)== null){
                    System.out.println(v.getId());

                }else if(list.get(position).getTag(R.id.TAG_ID).equals("false")){
                    System.out.println(list.get(position).getTag(R.id.TAG_ID));
                    list.get(position).setTag(R.id.TAG_ID,"true");
                    list.get(position).setText(list.get(position).getTag(R.id.TAG_NAME)+" (click to unshare)");
                    frag.setListShare(list.get(position));
                    frag.removeListUnshare(list.get(position));

                }else if(list.get(position).getTag(R.id.TAG_ID).equals("true")){
                    System.out.println(list.get(position).getTag(R.id.TAG_ID));
                    list.get(position).setTag(R.id.TAG_ID,"false");
                    list.get(position).setText(list.get(position).getTag(R.id.TAG_NAME)+" (click to share)");
                    frag.setListUnshare(list.get(position));
                    frag.removeListShare(list.get(position));

                }
                frag.changeListSize();


            }
        });
        return convertView;
    }
}
