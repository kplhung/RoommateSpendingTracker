package com.example.kallyruan.roommateexpense.UserPkg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // user presses this to reset password
        final EditText emailField = findViewById(R.id.emailField);
        final EditText resetCodeField = findViewById(R.id.resetCodeField);
        final EditText newPasswordField = findViewById(R.id.newPasswordField);
        Button resetPassword = findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQueries dbq = DBQueries.getInstance();
                String email = emailField.getText().toString();
                String resetCode = resetCodeField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                int resetPermission = dbq.allowReset(email, resetCode);
                Toast toast;

                // user should be given permission to update their password
                if (resetPermission == 2) {
                    dbq.resetPassword(email, newPassword);
                    toast = Toast.makeText(getApplicationContext(),
                            "Success! Re-directing you to login page.", Toast.LENGTH_SHORT);
                    toast.show();

                    // redirect to login page
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));}
                    }, 500);

                } else if (resetPermission == 1) {
                    toast = Toast.makeText(getApplicationContext(),
                            "This code has already expired.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "The entered reset code is incorrect.", Toast.LENGTH_LONG);
                }
            }
        }));









    }

}
