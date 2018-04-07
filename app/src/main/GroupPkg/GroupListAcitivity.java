package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        showUserInfo();
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

    /**
     *this method is to get user icon and nickname from database and show it in the interface
     **/
    public void showUserInfo() {
        //problem with connecting db, hardcode now
        TextView userNickname = findViewById(R.id.user_nickname);
        userNickname.setText("Hard-code");
        ImageView image = findViewById(R.id.user_icon);
        image.setImageResource(R.mipmap.usericon_5);

        //get nickname and set to Textview content
        /*
        String nickname = instance.getNickname(LoginActivity.email);
        TextView userNickname = findViewById(R.id.nicknameField);
        userNickname.setText(nickname);


        //get icon and set to corresponding imageView

        String icon = instance.getIcon(LoginActivity.email);
        int iconIndex=Integer.parseInt(icon);
        ImageView image = findViewById(R.id.user_icon);
        switch(iconIndex){
            case 0:
                image.setImageResource(R.mipmap.usericon_8);
                break;
            case 1:
                image.setImageResource(R.mipmap.usericon_9);
                break;
            case 2:
                image.setImageResource(R.mipmap.usericon_3);
                break;
            case 3:
                image.setImageResource(R.mipmap.usericon_2);
                break;
            case 4:
                image.setImageResource(R.mipmap.usericon_7);
                break;
            case 5:
                image.setImageResource(R.mipmap.usericon_6);
                break;
            case 6:
                image.setImageResource(R.mipmap.usericon_4);
                break;
            case 7:
                image.setImageResource(R.mipmap.usericon_1);
                break;
            default:
                image.setImageResource(R.mipmap.usericon_5);
                break;
        }
        */
    }

}
