package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText username = findViewById(R.id.emailField);
        username.setTextColor(Color.BLACK);

        final android.widget.EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.BLACK);

        final Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = pw.getText().toString();

                DBQueries db = DBQueries.getInstance();
                boolean success = db.signUp(email, password);

                Handler h = new Handler();
                if (success) {
                    // case 1: successful sign up
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Success! Redirecting to login page; please login with" +
                                    " your new account info.", Toast.LENGTH_LONG);
                    toast.show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));}
                    }, 500);
                } else {
                    // case 2: email address is already associated with existing account
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "There is already an account associated with this email address;" +
                                    " please use a different one.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
