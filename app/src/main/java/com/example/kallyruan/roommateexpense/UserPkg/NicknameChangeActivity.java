package com.example.kallyruan.roommateexpense.UserPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;

public class NicknameChangeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);
    }

    public void confirmChangeNickname(View view){
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirm action " +
                "dialog").setIcon(R.mipmap.usericon_2)
                .setNegativeButton("Cancel", null).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do confirmed change nickname action
                                changeNickname();
                            }
                        }).setMessage("Are you sure to change your nickname?").create();
        dialog.show();
    }

    public void changeNickname(){
        //get user inputs
        EditText name = findViewById(R.id.name);
        String nickname = name.getText().toString();
        String userEmail = LoginActivity.email;
        //connec to database
        DBQueries db = DBQueries.getInstance();
        boolean result = db.setNickname(userEmail , nickname);
        Handler h = new Handler();
        //check whether DBQueries successful
        if(result){
            // case 1: successful sign up
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Success! Redirecting to Profile page" , Toast.LENGTH_LONG);
            toast.show();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {startActivity(new Intent(getApplicationContext(),
                        ProfileActivity.class));}
            }, 500);
        }else{
            // case 2: email address is already associated with existing account
            Toast toast = Toast.makeText(getApplicationContext(),
                    "There is something wrong;" +
                            " please try again.", Toast.LENGTH_LONG);
            toast.show();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {startActivity(new Intent(getApplicationContext(),
                        NicknameChangeActivity.class));}
            }, 500);
        }
    }
}
