package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.ImageView;

import com.example.kallyruan.roommateexpense.BillPkg.BillListActivity;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.MenuActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.User;

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
        ImageView icon=findViewById(R.id.user_icon);
        //hard-code here to show one icon
        icon.setImageResource(R.mipmap.usericon_2);
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

    /**
     * Loads the list of bills of the user when a group is clicked
     * @param view
     */
    public void loadBillList(View view){
        Intent i = new Intent(this, BillListActivity.class);
        String userEmail = LoginActivity.email;
        String group_id = User.getInstance(userEmail).getNthGroup(
                action_index).getCode();
        i.putExtra("group_id", group_id);
        startActivity(i);
    }

}
