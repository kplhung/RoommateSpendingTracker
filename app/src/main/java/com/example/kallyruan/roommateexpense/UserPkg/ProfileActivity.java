package com.example.kallyruan.roommateexpense.UserPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.kallyruan.roommateexpense.R;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);
    }

    /**
     * Re-routes user to activity that allows user to change their password
     * @param view
     */
    public void changePassword(View view) {
        Intent i = new Intent(this, PasswordChangeActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Re-routes user to activity that allows user to change their icon
     * @param view
     */
    public void changeIcon(View view) {
        Intent i = new Intent(this, IconChangeActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Re-routes user to activity that allows user to change their nickname
     * @param view
     */
    public void changeNickname(View view) {
        Intent i = new Intent(this, NicknameChangeActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Re-routes user to activity that allows user to change their email
     * @param view
     */
    public void changeEmail(View view) {
        Intent i = new Intent(this, EmailChangeActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Re-routes user to activity that allows user to delete account
     * @param view
     */
    public void deleteAccount(View view) {
        //pop up a window to let user confirm delete account action


    }





}
