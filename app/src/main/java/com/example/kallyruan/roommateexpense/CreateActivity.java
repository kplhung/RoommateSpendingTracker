package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kallyruan on 16/2/18.
 */

public class CreateActivity extends Activity {

    final String PROMOTEDINFO ="Type invitee email address";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
    }

    //send invitation and undate recent group list
    public void createGroup(View view){
        String groupName;
        ArrayList<String> inviteeEmail=new ArrayList<>();
        int memberNumber=0;
        String email;

        EditText name= (EditText) findViewById(R.id.groupName);
        groupName= name.getText().toString();

        // the following codes are a bit too repetitive but i haven't figured out how to improve it...
        EditText invitee1= (EditText) findViewById(R.id.invitee1);
        email=invitee1.getText().toString();
        if (!email.equals(PROMOTEDINFO)){
            inviteeEmail.add(email);
            memberNumber+=1;
        }

        EditText invitee2= (EditText) findViewById(R.id.invitee2);
        email=invitee2.getText().toString();
        if (!email.equals(PROMOTEDINFO)){
            inviteeEmail.add(email);
            memberNumber+=1;
        }

        EditText invitee3= (EditText) findViewById(R.id.invitee3);
        email=invitee3.getText().toString();
        if (!email.equals(PROMOTEDINFO)){
            inviteeEmail.add(email);
            memberNumber+=1;
        }
        System.out.println("number = "+memberNumber);
        sendinvitation(inviteeEmail);

        Integer groupId = GroupListAcitivity.newGroupId();
        System.out.println("newGroupID = "+groupId);
        GroupListAcitivity.idList.add(groupId);
        GroupListAcitivity.nameList.add(groupName);
        GroupListAcitivity.participationList.add(memberNumber);

        TextView message = (TextView) findViewById(R.id.message);
        message.setVisibility(View.VISIBLE);
    }

    //send invitation email to given invitee email addresses
    public void  sendinvitation(ArrayList<String> inviteeEmail){

    }

    public void backToMenu(View view) {
        Intent i = new Intent(this,MenuActivity.class);
        startActivityForResult(i,1);
    }
}
