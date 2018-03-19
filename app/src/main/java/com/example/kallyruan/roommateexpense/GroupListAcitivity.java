package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kallyruan on 13/2/18.
 */

public class GroupListAcitivity extends Activity{

    // hard-code data
    // this following idList is just made up for temporary testing, will be update after database all set up
    public static ArrayList<Integer>  idList= new ArrayList<Integer>(Arrays.asList(10001,10002));
    public static ArrayList<String> nameList= new ArrayList<String>(Arrays.asList("RodinCollege 1010","RodinCollege 1011"));
    public static ArrayList<Integer> participationList=new ArrayList<Integer>(Arrays.asList(4,3));
    public static ArrayList<String> alertList=new ArrayList<String>(Arrays.asList("","DUE"));

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Integer> adapter_id;
    ArrayAdapter<String> adapter_name;
    ArrayAdapter<Integer> adapter_participation;
    ArrayAdapter<String> adapter_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_group);
        showGroupList();
    }

    public void showGroupList() {
    // this part should connect with database and show all groups user had

        //Fill the list view of id
        ListView view_id = (ListView) findViewById(R.id.listView_id);
        adapter_id=new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1,
                idList);
        view_id.setAdapter(adapter_id);

        //Fill the list view of name
        ListView view_name = (ListView) findViewById(R.id.listView_name);
        adapter_name=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                nameList);
        view_name.setAdapter(adapter_name);

        //Fill the list view of participation
        ListView view_participation = (ListView) findViewById(R.id.listView_participation);
        adapter_participation=new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1,
                participationList);
        view_participation.setAdapter(adapter_participation);

        //Fill the list view of alert
        ListView view_alert = (ListView) findViewById(R.id.listView_alert);
        adapter_alert=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                alertList);
        view_alert.setAdapter(adapter_alert);
    }

    public void backToMenu(View view){
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }

    public void manageGroupAction(View view){
        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    // give next new group id to calling method
    public static Integer newGroupId(){
        int index = idList.size()-1;
        int last = idList.get(index);
        return (Integer) (last+1) ;
    }


    //load bill list
    public void loadBillList(View view){
        Intent i = new Intent(this, BillListActivity.class);
        //temporarily show info for group 1
        Intent j = getIntent();
        i.putExtra("group_id", idList.get(0) + "");
        i.putExtra("username", j.getStringExtra("username"));
        startActivity(i);
    }
}
