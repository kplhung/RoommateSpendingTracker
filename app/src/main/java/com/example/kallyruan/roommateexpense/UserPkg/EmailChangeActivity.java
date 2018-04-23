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

public class EmailChangeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        showUserInfo();
    }

    /**
     * This aims to let user confirm the action of changing log-in email
     * @param view
     */
    public void confirmChangeEmail(View view){
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirm action " +
                "dialog").setIcon(R.mipmap.usericon_2)
                .setNegativeButton("Cancel", null).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do confirmed change email action
                                changeEmail();
                            }
                        }).setMessage("Are you sure to change the account email?").create();
        dialog.show();

    }

    /**
     *  This aims to firstly check the old email account exists and password correct and then
     *  replace the account address with the new email address
     */
    public void changeEmail(){
        Toast toast;
        boolean result=false;
        //get user inputs
        EditText email = findViewById(R.id.old_email);
        String old_email = email.getText().toString();
        EditText password = findViewById(R.id.password);
        String account_password = password.getText().toString();
        EditText email_new = findViewById(R.id.new_email);
        String new_email = email_new.getText().toString();

        //check whether email and password are correct
        DBQueries db = DBQueries.getInstance();
        int rs = db.login(old_email, account_password);
        Handler h = new Handler();

        //only update the account email when current email and password are correct
        if(rs != 2){
            toast = Toast.makeText(getApplicationContext(),
                    "Email or password incorrect!; Please try again!", Toast.LENGTH_SHORT);
        }else{
            result = db.changeEmail(old_email, new_email);
            //check changeEmail successful from DBQueries
            if(result) {
                toast = Toast.makeText(getApplicationContext(),
                        "Successfully changed the account email!; Please login again!",
                        Toast.LENGTH_SHORT);
            }else{
                toast = Toast.makeText(getApplicationContext(),
                        "Something wrong!; Please try again!", Toast.LENGTH_SHORT);
            }
        }
        //show toast and redirect to responding page
        toast.show();
        if(result){
            // redirect to sign up page
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }}, 500);
        }else{
            // redirect to email change page
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), EmailChangeActivity.class));
                }}, 500);
        }
    }

    /**
     * This aims to back the Profile page
     * @param view
     */
    public void cancel(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Shows user icon and nickname
     **/
    public void showUserInfo(){
        DBQueries instance = DBQueries.getInstance();
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
            //default image
            iconIndex = -1;
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
