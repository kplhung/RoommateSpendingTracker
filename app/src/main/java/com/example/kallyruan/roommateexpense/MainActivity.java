package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kallyruan on 13/2/18.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //for temporary use, click welcome text to switch to landing page.
    public void nextPage(View view){
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }
}
