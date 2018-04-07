package com.example.kallyruan.roommateexpense.UserPkg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;

public class PasswordChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        // user enters their old password into this EditText
        final EditText oldPasswordField = (EditText) findViewById(R.id.old_password_field);

        // user enters new password into this EditText
        EditText newPasswordField = (EditText) findViewById(R.id.new_password_field);

        Button changePWButton = (Button) findViewById(R.id.change_password);

        // attempt to change password
        changePWButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = LoginActivity.email;
                String oldPassword = oldPasswordField.getText().toString();

                DBQueries dbq = DBQueries.getInstance();
                if (dbq.enteredCorrectPassword(username, oldPassword)) {
                    // TODO: entered correct password; reset password accordingly
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
