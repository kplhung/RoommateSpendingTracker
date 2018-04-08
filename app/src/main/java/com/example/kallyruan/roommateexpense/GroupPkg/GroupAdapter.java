package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.GroupPkg.Group;
import com.example.kallyruan.roommateexpense.R;

import java.util.ArrayList;

/**
 * Adapter for Group class
 * Created by kallyruan on 11/3/18.
 */

public class GroupAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<Group> groups;
    private Button delete;
    private TextView groupName;
    private TextView participation;
    private TextView alert;

    public GroupAdapter(Activity activity, ArrayList<Group> groups){
        this.mActivity = activity;
        this.groups = groups;
    }

    @Override
    public int getCount() {
        return groups.size();
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
    public View getView(int position, final View view, ViewGroup viewGroup) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View createdView;

        if(view == null){
            createdView = inflater.inflate(R.layout.group_list_row, null);
            delete = (Button) createdView.findViewById(R.id.Manage);
            groupName = (TextView) createdView.findViewById(R.id.groupName);
            participation = (TextView) createdView.findViewById(R.id.groupParticipation);
            alert = (TextView) createdView.findViewById(R.id.alert);
        } else {
            createdView = view;
        }

        final Group group = groups.get(position);
        groupName.setText(group.getName());
        participation.setText(Integer.toString(group.getParticipation()));
        alert.setText(group.getAlert());

        return createdView;
    }
}