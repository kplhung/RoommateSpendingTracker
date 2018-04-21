package com.example.kallyruan.roommateexpense.UserPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;

public class NicknameChangeActivity extends Activity {
    private DBQueries instance = DBQueries.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);
        showUserInfo();
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

    public void cancel(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, 1);
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

    /**
     * Shows user icon and nickname
     **/
    public void showUserInfo(){
        // get nickname and set to TextView content
        String nickname = instance.getNickname(LoginActivity.email);
        TextView email = findViewById(R.id.user_email);
        email.setText(LoginActivity.email);
        TextView userNickname = findViewById(R.id.user_nickname);
        userNickname.setText(nickname);

        // get icon and set to corresponding imageView
        String icon = instance.getIcon(LoginActivity.email);
        int iconIndex;
        try {
            iconIndex = Integer.parseInt(icon);
        } catch (Exception e) {
            iconIndex = -1;
            System.out.println("No icon image recorded. Put default image instead.");
        }

        ImageView image = findViewById(R.id.user_icon);
        switch(iconIndex){
            case 0:
                image.setImageResource(R.mipmap.usericon_8);
                break;
            case 1:
                image.setImageResource(R.mipmap.usericon_9);
                break;
            case 2:
                image.setImageResource(R.mipmap.usericon_3);
                break;
            case 3:
                image.setImageResource(R.mipmap.usericon_2);
                break;
            case 4:
                image.setImageResource(R.mipmap.usericon_7);
                break;
            case 5:
                image.setImageResource(R.mipmap.usericon_6);
                break;
            case 6:
                image.setImageResource(R.mipmap.usericon_4);
                break;
            case 7:
                image.setImageResource(R.mipmap.usericon_1);
                break;
            default:
                image.setImageResource(R.mipmap.usericon_5);
                break;
        }
    }
}
