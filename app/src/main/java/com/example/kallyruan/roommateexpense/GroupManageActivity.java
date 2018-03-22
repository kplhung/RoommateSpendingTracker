package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kallyruan on 9/3/18.
 */

public class GroupManageActivity extends Activity{

    ArrayAdapter<String> adapter_name;
    GroupAdapter adapter;
    int action_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_group);
        showGroupList();

        ListView view_name = (ListView) findViewById(R.id.listView_manage);
        view_name.setItemsCanFocus(false);
        view_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                action_index=position;
                System.out.println("position "+action_index+" was clicked");
                showActionOption();
            }
        });

    }

    public void showGroupList() {
        ListView listView = (ListView) findViewById(R.id.listView_manage);
        ArrayList<Group> list = User.getInstance(LoginActivity.email).getGroups();
        GroupAdapter adapter = new GroupAdapter(this, list);
        listView.setAdapter(adapter);
    }

    public void showActionOption(){
        Button action1=(Button)findViewById(R.id.exitGroup);
        action1.setVisibility(View.VISIBLE);
        Button action2=(Button)findViewById(R.id.deleteGroup);
        action2.setVisibility(View.VISIBLE);
        Button action3=(Button)findViewById(R.id.cancelGroup);
        action3.setVisibility(View.VISIBLE);
    }

    public void exitGroup(View view){
        String userEmail=LoginActivity.email;
        User present = User.getInstance(userEmail);
        System.out.println("now try to get the "+action_index+"th group.");
        Group selectedGroup=present.getNthGroup(action_index);
        String groupCode = selectedGroup.getCode();
        System.out.println(groupCode);

        try {
            DBQueries db = DBQueries.getInstance();
            Boolean result = db.leaveGroup(userEmail, groupCode);
            if (!result) {
                System.out.println("Leave group action failed");
            }else{
                System.out.println("successfully exist group.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        //String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();
        //System.out.println(group);
        /*
        Boolean result = db.leaveGroup(userEmail,group);
        if (!result){
            System.out.println("Leave group action failed");
        }
*/
        //Intent i = new Intent(this,GroupManageActivity.class);
        //startActivityForResult(i,1);
    }

    public void deleteGroup(View view){
        //have not gotten the SQL function yet

        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    public void cancel(View view){
        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    public void backToExistingGroupList(View view){
        Intent i = new Intent(this,GroupListAcitivity.class);
        startActivityForResult(i,1);
    }
}
