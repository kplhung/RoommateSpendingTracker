package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* KH: This is the welcome page, where the user is able to press the 'Enter' button to get
 * to the login page.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        final Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to login page upon pressing the [Enter] button
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
