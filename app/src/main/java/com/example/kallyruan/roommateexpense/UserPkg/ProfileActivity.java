package com.example.kallyruan.roommateexpense.UserPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.MainActivity;
import com.example.kallyruan.roommateexpense.MenuActivity;
import com.example.kallyruan.roommateexpense.R;

public class ProfileActivity extends Activity {
    private DBQueries instance = DBQueries.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);
        showUserInfo();
        //set up clickListener for signOut
        android.support.v7.widget.AppCompatImageView view = findViewById(R.id.signOutButton);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signOut();
            }
        });
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
     * Re-routes user to activity that allows user to check all their payment history
     * @param view
     */
    public void paymentHistory(View view) {
        Intent i = new Intent(this, PaymentHistoryActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Pops up dialog to make user confirm account deletion
     * @param view
     */
    public void confirmDeleteAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirm action dialog")
                .setIcon(R.mipmap.usericon_2).setNegativeButton("Cancel", null).
                        setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do confirmed delete account action
                                deleteAccount();
                            }
                        }).setMessage("Are you sure you want to delete this account permanently?").create();
        dialog.show();
    }

    /**
     * Delete account from Database
     */
    public void deleteAccount() {
        String userEmail = LoginActivity.email;
        DBQueries instance = DBQueries.getInstance();
        boolean result = instance.deleteAccount(userEmail);
        Handler h = new Handler();
        if(result){
            // case 1: successful sign up
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Success! Redirecting to Login page" , Toast.LENGTH_LONG);
            toast.show();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {startActivity(new Intent(getApplicationContext(),
                        LoginActivity.class));}
            }, 500);
        }else{
            // case 2: email address is already associated with existing account
            Toast toast = Toast.makeText(getApplicationContext(),
                    "There is something wrong;" +
                            " please try again.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Re-routes user to menu activity
     * @param view
     */
    public void backToMenu(View view){
        Intent i = new Intent(this, MenuActivity.class);
        startActivityForResult(i, 1);
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

    public void signOut(){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 1);
    }


}
