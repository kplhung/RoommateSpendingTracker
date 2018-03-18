package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

import org.w3c.dom.Text;

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

        //get groupName
        EditText name= (EditText) findViewById(R.id.groupName);
        String groupName= name.getText().toString();

        //Check whether have invitee email
        EditText invitee= (EditText) findViewById(R.id.invitee1);
        checkEmail(invitee);
        EditText invitee2= (EditText) findViewById(R.id.invitee2);
        checkEmail(invitee2);
        EditText invitee3= (EditText) findViewById(R.id.invitee3);
        checkEmail(invitee3);

        sendinvitation(inviteeEmail);


        String user_id=LoginActivity.email;
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
