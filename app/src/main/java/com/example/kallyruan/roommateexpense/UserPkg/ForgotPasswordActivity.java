package com.example.kallyruan.roommateexpense.UserPkg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.EmailPkg.GMailSender;
import com.example.kallyruan.roommateexpense.R;

/**
 * Allows user to enter email address where a password reset code will be sent
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // user enters their email here
        final EditText enteredEmail = findViewById(R.id.emailAddressField);

        // button that user presses to send reset code
        Button sendCodeButton = findViewById(R.id.sendCodeButton);

        sendCodeButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQueries dbq = DBQueries.getInstance();
                String emailAddress = enteredEmail.getText().toString();

                /* check to see if email address is associated with a user
                   sends email if email exists in DB */
                if (dbq.userExists(emailAddress)) {
                    String resetCode = dbq.forgotPassword(emailAddress);

                    try {
                        GMailSender sender = new GMailSender(
                                "roommatespendingtracker@gmail.com",
                                "cis350s18");
                        sender.sendMail("Reset Password", "Here is the code to reset " +
                                        "your password: " + resetCode,
                                "roommatespendingtracker@gmail.com", emailAddress);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }

                // show message
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Thanks! If this email is associated with an account, then we've " +
                                "sent you an email with a reset code to set a new password." +
                                "Go to your inbox to get it! Re-directing you to set a new " +
                                "password.", Toast.LENGTH_SHORT);
                toast.show();

                // send user to page where they can type in their reset code and reset their pw
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {startActivity(new Intent(getApplicationContext(),
                            ResetPasswordActivity.class));}
                }, 500);
            }
        }));
    }
}