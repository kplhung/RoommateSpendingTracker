package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Lily on 3/5/2018.
 */

public class RoommateAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<User> roommates;
    private TextView roommate_name;
    public RoommateAdapter(Activity activity, ArrayList<User> roommates){
        this.mActivity = activity;
        this.roommates = roommates;
    }
    @Override
    public int getCount() {
        return roommates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        if(view == null){
            view=inflater.inflate(R.layout.split_bill_row, null);
            this.roommate_name=(TextView) view.findViewById(R.id.roommate_name);
        }

        User roommate = roommates.get(i);
        this.roommate_name.setText(roommate.getName());
        return view;
    }
}
