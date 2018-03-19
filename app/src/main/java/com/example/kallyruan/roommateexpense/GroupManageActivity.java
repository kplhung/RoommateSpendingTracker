package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kallyruan on 9/3/18.
 */

public class GroupManageActivity extends Activity {

    ArrayAdapter<String> adapter_name;
    int action_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_group);

        //Fill the list view of name
        ListView view_name = (ListView) findViewById(R.id.listView_name);
        adapter_name=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                GroupListAcitivity.nameList);
        view_name.setAdapter(adapter_name);
        view_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                action_index=position;
                System.out.println("position "+action_index+" was clicked");
                showActionOption();
            }
        });
    }

    public void showActionOption(){
        Button action1=(Button)findViewById(R.id.exit);
        action1.setVisibility(View.VISIBLE);
        Button action2=(Button)findViewById(R.id.delete);
        action2.setVisibility(View.VISIBLE);
        Button action3=(Button)findViewById(R.id.cancel);
        action3.setVisibility(View.VISIBLE);
    }

    public void exitGroup(View view){
        //here should delete this group information in the database!!

        //for now, just delete from hard-code data
        GroupListAcitivity.idList.remove(action_index);
        GroupListAcitivity.nameList.remove(action_index);
        GroupListAcitivity.participationList.remove(action_index);
        GroupListAcitivity.alertList.remove(action_index);

        //here should check whether i am the last one to leave this group, if so, then should delete
        //this group information in the database!!


        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    public void deleteGroup(View view){
        //firstly, check whether have the permission to delete group,


        //if have the permission,
        //delete group information in all memebers' record in the database!!

        //for now, just delete from hard-code data
        GroupListAcitivity.idList.remove(action_index);
        GroupListAcitivity.nameList.remove(action_index);
        GroupListAcitivity.participationList.remove(action_index);
        GroupListAcitivity.alertList.remove(action_index);
        //here should also delete other user data in this group


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
