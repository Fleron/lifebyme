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

import java.util.ArrayList;

/**
 * Created by fleron on 2016-05-20.
 */
public class ButtonAdapter extends BaseAdapter {
    ArrayList<Button> btns;
    Context context;
    private static LayoutInflater inflater;

   public ButtonAdapter(MainActivity mainActivity,ArrayList<Button> btnList){
       btns = btnList;
       context = mainActivity;
       inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

   }

    @Override
    public int getCount() {
        return btns.size();
    }

    @Override
    public Object getItem(int position) {
        return btns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return btns.get(position).getId();
    }
    public class Holder{
        Button btn;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.button_share,parent,false);
        holder.btn = (Button) rowView.findViewWithTag("buttonShare");
        holder.btn = btns.get(position);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ID: "+v.getId() + " eller "+ btns.get(position));
            }
        });

        return rowView;
    }
}
