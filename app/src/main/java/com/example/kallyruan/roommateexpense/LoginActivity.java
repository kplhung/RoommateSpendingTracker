package com.example.kallyruan.roommateexpense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = findViewById(R.id.emailField);
        login.setTextColor(Color.BLACK);

        final EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.BLACK);

        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = "'" + login.getText().toString() + "'";
                String password = pw.getText().toString();

                DBQueries db = DBQueries.getInstance();
                int rs = db.login(email, password);

                Handler h = new Handler();
                // case 1: username does not match an existing one
                if (rs == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User does not exist; redirecting you to sign up page!",
                            Toast.LENGTH_SHORT);
                    // redirect to sign up page
                    toast.show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),
                                    SignupActivity.class));
                        }
                    }, 500);
                } else if (rs == 1) {
                    // case 2: password is incorrect
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Password is incorrect.", Toast.LENGTH_SHORT);
                    // redirect to sign up page
                    toast.show();
                }
                else {
                    // case 3: username and password are correct; bring to menu
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    i.putExtra("username", email);
                    startActivity(i);
                }
            }
        });
    }
}
