package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.database.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by kallyruan on 13/2/18.
 */

public class GroupListAcitivity extends Activity{

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Integer> adapter_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_group);
        showGroupList();
    }

    public void showGroupList() {
        ListView listView = (ListView) findViewById(R.id.listView_id);
        ArrayList<Group> list = User.getInstance(LoginActivity.email).getGroups();
        GroupAdapter adapter = new GroupAdapter(this, list);
        listView.setAdapter(adapter);
    }

    public void backToMenu(View view){
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }

    public void manageGroupAction(View view){
        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
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
