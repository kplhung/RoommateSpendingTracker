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
        setContentView(R.layout.landing_page);
    }

    public void myGroup(View view){
        Intent i = new Intent(this,GroupListAcitivity.class);
        startActivityForResult(i,1);
    }
}
