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

import java.util.ArrayList;

/**
 * Created by kallyruan on 16/2/18.
 */

public class CreateActivity extends Activity {

    final String PROMOTEDINFO = "Type invitee email address";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
    }

    /**
     * Creates new group and sends invites
     */
    public void createGroup(View view){
        // get groupName
        String groupName = ((EditText) findViewById(R.id.enterNameField)).getText().toString();

        // get invitee email addresses (up to three)
        ArrayList<String> invitees = new ArrayList<String>();
        String invitee1 = ((EditText) findViewById(R.id.enterM1Field)).getText().toString();
        invitees = checkEmail(invitees, invitee1);
        String invitee2 = ((EditText) findViewById(R.id.enterM2Field)).getText().toString();
        invitees = checkEmail(invitees, invitee2);
        String invitee3 = ((EditText) findViewById(R.id.enterM3Field)).getText().toString();
        invitees = checkEmail(invitees, invitee3);

        String user_id = LoginActivity.email;

        DBQueries db = DBQueries.getInstance();

        //add group information to database
        String group_id = db.createGroup(user_id, groupName);

        if(group_id != null){
            //send invitees email & codes
            sendInviteEmails(invitees, user_id, group_id, groupName);
            //show success message
            Toast.makeText(getApplicationContext(), "Success!",
                    Toast.LENGTH_LONG).show();
            seeGroups();
        } else {
            System.out.println("Group not successfully created.");
        }
    }

    /**
     * After creating a group, user should be directly led to see a list of groups he/she is in
     */
    private void seeGroups(){
        Intent i = new Intent(this, GroupListAcitivity.class);
        startActivityForResult(i,1);
        finish();
    }

    /**
     * Sends invitation emails to join group to specified users
     * @param list of invitees
     * @param user who is sending invites
     * @param group_id ID of group that invitees are being invited to
     * @param name of group
     */
    public void sendInviteEmails(ArrayList<String> list, String user, String group_id, String name) {
        DBQueries db = DBQueries.getInstance();

        // loop over invitees to send emails
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
    ** Navigates user back to menu page
    */
    public void backToMenu(View view) {
        Intent i = new Intent(this , MenuActivity.class);
        startActivityForResult(i , 1);
    }

    /**
     * Checks if user inputted an email address
     * @param list of invitees
     * @param invitee to be invited
     * @return ArrayList of invitees
     */
    public ArrayList<String> checkEmail(ArrayList<String> list, String invitee){
        if (!invitee.equals("Invitee email address") ){
            list.add(invitee);
        }
        return list;
    }
}