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
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;

/**
 * This activity is where (an already logged in) user changes their password upon re-confirming
 * their old password
 */
public class PasswordChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        // user enters their old password into this EditText
        final EditText oldPasswordField = (EditText) findViewById(R.id.old_password_field);

        // user enters new password into this EditText
        final EditText newPasswordField = (EditText) findViewById(R.id.new_password_field);

        Button changePWButton = (Button) findViewById(R.id.change_password);

        // attempt to change password
        changePWButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // username and confirmed old password
                String username = LoginActivity.email;
                String oldPassword = oldPasswordField.getText().toString();

                DBQueries dbq = DBQueries.getInstance();

                // check if user entered correct old password
                if (dbq.enteredCorrectPassword(username, oldPassword)) {
                    // correct old password: allow reset
                    dbq.resetPassword(username, newPasswordField.getText().toString());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Success! Please login with your new password.", Toast.LENGTH_LONG);
                    toast.show();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));}
                    }, 500);
                } else {
                    // entered incorrect password; show Toast indicating this
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The password you entered was incorrect.", Toast.LENGTH_SHORT);
                    // redirect to sign up page
                    toast.show();
                }
            }
        });
    }
}
