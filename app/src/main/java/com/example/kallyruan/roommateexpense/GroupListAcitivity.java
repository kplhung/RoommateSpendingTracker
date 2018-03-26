package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.database.SQLException;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by kallyruan on 13/2/18.
 */

public class GroupListAcitivity extends Activity{

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Integer> adapter_id;
    private int action_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_group);
        showGroupList();

        ListView view_name = (ListView) findViewById(R.id.listView_id);
        view_name.setItemsCanFocus(false);
        view_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                action_index=position;
                loadBillList(view);
            }
        });

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
        String userEmail = LoginActivity.email;
        String group_id = User.getInstance(userEmail).getNthGroup(
                action_index).getCode();
        Log.i("group_id gouplist", group_id);
        i.putExtra("group_id", group_id);
        startActivity(i);
    }

}
