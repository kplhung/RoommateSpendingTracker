package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.MenuActivity;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.User;

/**
 * Created by Lily on 2/22/2018.
 */

public class JoinGroupActivity extends Activity{
    private User userInstance = User.getInstance(LoginActivity.email);
    private DBQueries dbInstance = DBQueries.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    /**
     * Allows user to join group by entering a code
     * Uses DBQueries instance to add the user to the group
     * @param view
     */
    public void joinGroup(View view){
        String code = ((EditText) findViewById(R.id.enterCodeField)).getText().toString();
        if (code != null){
            Group newGroup = new Group(dbInstance.getGroupForCode(code), userInstance);
            userInstance.addGroup(newGroup);
            int i = dbInstance.addUserToGroup(LoginActivity.email, code);
            if (i == 0) {
                //wrong code
                Toast.makeText(getApplicationContext(), "Incorrect Code",
                        Toast.LENGTH_LONG).show();
            } else if (i == 1) {
                //code had expired
                Toast.makeText(getApplicationContext(), "Code Already Expired",
                        Toast.LENGTH_LONG).show();
            } else {
                //success
                Toast.makeText(getApplicationContext(), "Success!",
                        Toast.LENGTH_LONG).show();
            }
            seeGroups();
        }
    }

    /**
     * After joining a group, user should be directly led to see a list of groups he/she is in
     */
    private void seeGroups(){
        Intent i = new Intent(this,GroupListActivity.class);
        startActivityForResult(i,1);
        finish();
    }

    /**
     * Navigates user back to main menu
     * @param view
     */
    public void backToMenu(View view){
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }
}
