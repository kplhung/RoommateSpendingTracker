package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.EmailPkg.GMailSender;
import com.example.kallyruan.roommateexpense.MenuActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kallyruan on 16/2/18.
 */

public class CreateActivity extends Activity {

    final String PROMOTEDINFO ="Invitee email address";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
    }

    /*
    ** this function is to get new group and invitee to create a new group in DB
    */
    public void createGroup(View view){
        //get groupName
        EditText name = (EditText) findViewById(R.id.groupName);
        String groupName = name.getText().toString();

        ArrayList<String> invitees = new ArrayList<String>();
        EditText invitee1 = (EditText) findViewById(R.id.invitee1);
        invitees = checkEmail(invitees, invitee1);
        EditText invitee2 = (EditText) findViewById(R.id.invitee2);
        invitees = checkEmail(invitees, invitee2);
        EditText invitee3 = (EditText) findViewById(R.id.invitee3);
        invitees = checkEmail(invitees, invitee3);

        String user_id = LoginActivity.email;
        DBQueries db = DBQueries.getInstance();

        //add group information to database, if success then show message
        String group_id = db.createGroup(user_id, groupName);

        if(group_id != null){
            //send invite emails & codes
            sendInviteEmails(invitees, user_id, group_id, groupName);
            //show successful created message
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.VISIBLE);
        } else {
            System.out.println("something wrong with add group to db");
        }
    }

    public void sendInviteEmails(ArrayList<String> list, String user, String group_id, String name) {
        DBQueries db = DBQueries.getInstance();
        for (int i = 0; i < list.size(); i++) {
            String invitee = list.get(i);
            String code = "";
            if (invitee != null && !invitee.equals(PROMOTEDINFO)) {
                code = db.getInviteCode(group_id);
            }
            try {
                GMailSender sender = new GMailSender(
                        "roommatespendingtracker@gmail.com",
                        "cis350s18");
                sender.sendMail("Group Invite", "RoommateSpendingTracker user " + user
                                + " invited you to join group " + name + "! Use this code to join: "
                                + code + ". It will expire in one week.",
                                "roommatespendingtracker@gmail.com", invitee);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    /*
    ** This method is to back to menu page
    */
    public void backToMenu(View view) {
        Intent i = new Intent(this , MenuActivity.class);
        startActivityForResult(i , 1);
    }
    
    /*
    ** This method is to check whether user input email address
    */
    public ArrayList<String> checkEmail(ArrayList<String> list, EditText invitee){
        String email = invitee.getText().toString();
        if (!email.equals("Invitee email address") ){
            list.add(email);
        }
        return list;
    }
}
