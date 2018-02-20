package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final Button signupButton = findViewById(R.id.signUpButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: finish these cases
                // case 1: email address is already associated with existing account
                // case 2: successful sign up
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
