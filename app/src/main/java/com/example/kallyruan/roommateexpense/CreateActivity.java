package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;

import java.util.ArrayList;

/**
 * Created by kallyruan on 16/2/18.
 */

public class CreateActivity extends Activity {

    final String PROMOTEDINFO ="Type invitee email address";
    ArrayList<String> inviteeEmail=new ArrayList<>();
    int new_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
    }

    //send invitation and undate recent group list
    public void createGroup(View view){

        String groupName;
        ArrayList<String> inviteeEmail = new ArrayList<>();
        int memberNumber = 0;
        String email;
        //get groupName
        EditText name = (EditText) findViewById(R.id.groupName);
        groupName= name.getText().toString();

        // the following codes are a bit too repetitive but i haven't figured out how to improve it...
        EditText invitee1 = (EditText) findViewById(R.id.invitee1);
        email = invitee1.getText().toString();
        if (!email.equals(PROMOTEDINFO)){
            inviteeEmail.add(email);
            memberNumber += 1;
        }

        //Check whether have invitee email
        EditText invitee= (EditText) findViewById(R.id.invitee1);
        checkEmail(invitee);
        EditText invitee2= (EditText) findViewById(R.id.invitee2);
        checkEmail(invitee2);
        EditText invitee3= (EditText) findViewById(R.id.invitee3);
        checkEmail(invitee3);

        sendinvitation(inviteeEmail);


        String user_id = LoginActivity.email;
        DBQueries db = DBQueries.getInstance();

        //add group information to database, if success then show message
        boolean result=db.createGroup(user_id,groupName);
        if(result==true){
            //show successful created message
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.VISIBLE);
        }else{
            System.out.println("something wrong with add group to db");
        }

    }

    //send invitation email to given invitee email addresses
    public void  sendinvitation(ArrayList<String> inviteeEmail){

    }

    public void backToMenu(View view) {
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }

    public void checkEmail(EditText invitee){
        String email=invitee.getText().toString();
        if (!email.equals(PROMOTEDINFO)){
            inviteeEmail.add(email);
        }
    }
}
