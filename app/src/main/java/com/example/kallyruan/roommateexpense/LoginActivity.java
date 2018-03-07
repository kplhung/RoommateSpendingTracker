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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = findViewById(R.id.emailField);
        login.setTextColor(Color.BLACK);

        final EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.BLACK);

        final Button loginButton = findViewById(R.id.loginButton);

        // establish db connection
        final Connection con = DBConnect.getConnection();

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = "'" + login.getText().toString() + "'";
                String password = pw.getText().toString();

                String query = "SELECT password FROM Users WHERE user_id = " + email;
                ResultSet rs = null;
                Statement stmt = null;

                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    String realPassword = "";
                    Handler h = new Handler();
                    // case 1: username does not match an existing one
                    if (!rs.next()) {
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
                    } else {
                        realPassword = rs.getString("password");
                        // case 2: password is incorrect
                        if (!realPassword.equals(password)) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password is incorrect.", Toast.LENGTH_SHORT);
                            // redirect to sign up page
                            toast.show();
                        } else {
                            // case 3: username and password are correct; bring to menu
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
