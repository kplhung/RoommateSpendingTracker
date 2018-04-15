package com.example.kallyruan.roommateexpense.UserPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;

public class IconChangeActivity extends AppCompatActivity {
    DBQueries instance = DBQueries.getInstance();
    int user_icon = -1; //default image
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_icon);
        showIconImage();
        showrecentIcon();
    }

    /**
     * Shows user icon in grid view
     */
    public void showIconImage(){
        GridView view = (GridView) findViewById(R.id.user_icons);
        view.setAdapter(new ImageAdapter(this));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int location, long l) {
                user_icon = location;
                showNewIcon(user_icon);
            }
        });

    }

    /**
     * Shows the recent user icon
     */
    public void showrecentIcon(){
        // get icon and set to corresponding imageView
        String icon = instance.getIcon(LoginActivity.email);
        int iconIndex;
        try {
            iconIndex = Integer.parseInt(icon);
        } catch (Exception e) {
            iconIndex = -1;
        }
        ImageView image = findViewById(R.id.old_iconImage);
        switch(iconIndex) {
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

    /**
     * Shows the latest icon user clicked on
     */
    public void showNewIcon(int index){
        ImageView image = findViewById(R.id.new_iconImage);
        switch(index) {
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

    /**
     * Set the new picked icon as the new user icon in database
     * @param View
     */
    public void changeUserIcon(View view){
        String userEmail = LoginActivity.email;
        boolean result = instance.setIcon(userEmail, Integer.toString(user_icon));
        Handler h = new Handler();
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
                        IconChangeActivity.class));}
            }, 500);
        }
    }

    public void back(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, 1);
    }

}
