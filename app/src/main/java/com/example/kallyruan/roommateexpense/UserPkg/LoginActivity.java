package com.example.kallyruan.roommateexpense.UserPkg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.EmailPkg.GMailSender;
import com.example.kallyruan.roommateexpense.MenuActivity;
import com.example.kallyruan.roommateexpense.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for user login (user enters email and pw); also allows user to begin process for
 * resetting password (forgot password)
 */
public class LoginActivity extends AppCompatActivity {
    // email of user, stored statically
    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // field where user inputs their email to login
        final EditText login = findViewById(R.id.emailField);
        login.setTextColor(Color.BLACK);

        // field where user inputs their password for authentication
        final EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.BLACK);

        // buttons to login or to state that password is forgotten
        final Button loginButton = findViewById(R.id.loginButton);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        // keep track of number of times wrong password has been put in for same email
        final Map<String, Integer> numErrors = new HashMap<String, Integer>();

        // handles logging into the app
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = login.getText().toString();
                String password = pw.getText().toString();

                DBQueries db = DBQueries.getInstance();
                int rs = db.login(email, password);

                Handler h = new Handler();

                // case 1: username does not match an existing one
                if (rs == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User does not exist; redirecting you to sign up page!",
                            Toast.LENGTH_SHORT);
                    toast.show();

                    // redirect to sign up page
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),
                                    SignupActivity.class));
                        }
                    }, 500);
                } else if (rs == 1) {
                    // case 2: password is incorrect
                    Toast toast;

                    // update number of errors
                    if (!numErrors.containsKey(email)) {
                        // this is user's first mistake
                        numErrors.put(email, 1);
                    } else {
                        int tmp = numErrors.get(email);

                        // force user to reset password
                        if (tmp >= 2) {
                            DBQueries dbq = DBQueries.getInstance();
                            String emailAddress = email;

                            if (dbq.userExists(email)) {
                                String resetCode = dbq.forgotPassword(email);

                                //lock the user out
                                dbq.resetPassword(email, resetCode);

                                // send reset code
                                try {
                                    GMailSender sender = new GMailSender(
                                            "roommatespendingtracker@gmail.com",
                                            "cis350s18");
                                    sender.sendMail("Reset Password", "Here is the " +
                                                    "code to reset your password: " + resetCode,
                                            "roommatespendingtracker@gmail.com", emailAddress);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),"Error",
                                            Toast.LENGTH_LONG).show();

                                }
                            }

                            // inform user that they have to reset pw
                            toast = Toast.makeText(getApplicationContext(),
                                    "Too many incorrect password attempts. Reset code send to " +
                                    "email", Toast.LENGTH_LONG);
                            toast.show();

                            // redirect to reset password
                            Handler resetHandler = new Handler();
                            resetHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {startActivity(new Intent(getApplicationContext(),
                                        ResetPasswordActivity.class));}
                            }, 500);
                        }
                        numErrors.put(email, tmp + 1);
                    }

                    toast = Toast.makeText(getApplicationContext(),
                            "Password is incorrect.", Toast.LENGTH_SHORT);
                    // redirect to sign up page
                    toast.show();
                } else {
                    // case 3: username and password are correct; bring to menu
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    i.putExtra("username", email);
                    startActivity(i);
                }
            }
        });

        // routes user to activity to start process of resetting password
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }
}