package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kallyruan on 13/2/18.
 */

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void myGroup(View view){
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent j = new Intent(this, GroupListAcitivity.class);
        j.putExtra("username", username);
        startActivityForResult(j,1);
    }
    
    public void createGroup(View view){
        Intent i = new Intent(this,CreateActivity.class);
        startActivityForResult(i,1);
    }

    public void joinGroup(View view){
        Intent i = new Intent(this,JoinGroupActivity.class);
        startActivityForResult(i, 1);
    }
}
