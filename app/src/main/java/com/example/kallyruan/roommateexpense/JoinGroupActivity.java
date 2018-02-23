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
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //should get username from db, or pass it from Intent
        this.user = new User("User");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    public void joinGroup(View view){
        String code = ((EditText) findViewById(R.id.enterCodeField)).getText().toString();
        //check if code is in the database; for now, it will always be true if something is entered
        if (code != null){
            Group newGroup = getGroup(code);
            this.user.addGroup(newGroup);
            seeGroups();
        }
    }
    private Group getGroup(String code){
        Group group = new Group(code, user);
        //should fetch group from db
        return group;
    }
    private void seeGroups(){
        Intent i = new Intent(this,GroupListAcitivity.class);
        startActivityForResult(i,1);
        finish();
    }

}
