package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText login = findViewById(R.id.emailField);
        login.setTextColor(Color.LTGRAY);
        login.setText("jonsnow@winterfell.com");

        EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.LTGRAY);
        pw.setText("password");

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check to see if login and password combo is valid
                // TODO: finish these cases
                // case 1: username does not match an existing one
                // case 2: password isn't correct
                // case 3: username and password correct -> go to landing page
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
    }
}
