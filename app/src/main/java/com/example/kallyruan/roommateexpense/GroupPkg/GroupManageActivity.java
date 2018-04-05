package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.User;

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
        Button exitGroup = (Button) findViewById(R.id.exitGroup);
        exitGroup.setVisibility(View.VISIBLE);

        Button deleteGroup = (Button)findViewById(R.id.deleteGroup);
        deleteGroup.setVisibility(View.VISIBLE);

        Button cancelGroup = (Button)findViewById(R.id.cancelGroup);
        cancelGroup.setVisibility(View.VISIBLE);

        Button emailGroup = (Button) findViewById(R.id.emailGroup);
        emailGroup.setVisibility(View.VISIBLE);
    }

    //this function is to exist group and delete this group information from this user DB.
    //if this user is the last one in the group, then delete group record
    public void exitGroup(){
        DBQueries db = DBQueries.getInstance();
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();
        Boolean result = db.leaveGroup(userEmail, group);

        if (!result){
            System.out.println("Leave group action failed");
        }

        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    //this function is to delete group information from all members' DB and the group record as well
    public void deleteGroup(){
        DBQueries db = DBQueries.getInstance();
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();
        boolean result = db.deleteGroup(group);
        //check whether action successful
        if (!result) {
            System.out.println("Delete group action failed");
        }
        Intent i = new Intent(this, GroupManageActivity.class);
        startActivityForResult(i, 1);
    }

    // this emails users in the group with a reminder to pay
    public void emailGroup() {
        String userEmail = LoginActivity.email;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        DBQueries dbq = DBQueries.getInstance();
        ArrayList<String> members = dbq.getGroupMembers(User.getInstance(userEmail).getNthGroup(
                action_index).getCode());
        String[] emailAddresses = new String[members.size()];
        members.toArray(emailAddresses);
        i.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
        i.putExtra(Intent.EXTRA_SUBJECT, "Reminder to pay bills");
        i.putExtra(Intent.EXTRA_TEXT, "Hello! Please remember to pay your bills!");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Log.v("unsuccessful", "bad");
            // Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    //confirm user delete group action
    public void confirmDeleteAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirm action dialog").setIcon(R.mipmap.usericon_2)
                .setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do confirmed action
                        deleteGroup();
                    }
                }).setMessage("Are you sure to delete this group from all members?").create();
        dialog.show();
    }

    //confirm user exit group action
    public void confirmExitAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirm action dialog").setIcon(R.mipmap.usericon_2)
                .setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do confirmed action
                        exitGroup();
                    }
                }).setMessage("Are you sure to exit this group ?").create();
        dialog.show();
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
