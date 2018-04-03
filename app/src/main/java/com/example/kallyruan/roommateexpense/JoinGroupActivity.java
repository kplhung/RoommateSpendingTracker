package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
     * Uses DBQueries instance to add the user to the group.
     * @param view
     */
    public void joinGroup(View view){
        String code = ((EditText) findViewById(R.id.enterCodeField)).getText().toString();
        if (code != null){
            Group newGroup = new Group(dbInstance.getCodeGroup(code), userInstance);
            userInstance.addGroup(newGroup);
            int i = dbInstance.addUserToGroup(LoginActivity.email, code);
            seeGroups();
        }
    }

    /**
     * After joining a group, user should be directly led to see a list of groups he/she is in
     */
    private void seeGroups(){
        Intent i = new Intent(this,GroupListAcitivity.class);
        startActivityForResult(i,1);
        finish();
    }

}
