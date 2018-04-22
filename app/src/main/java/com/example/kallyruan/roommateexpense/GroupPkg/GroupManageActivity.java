package com.example.kallyruan.roommateexpense.GroupPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.BillPkg.Bill;
import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.EmailPkg.GMailSender;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class allows the user to manage their groups: delete group, exit group, or remind group
 * members of upcoming bills
 * Created by kallyruan on 9/3/18.
 */

public class GroupManageActivity extends Activity{
    ArrayAdapter<String> adapter_name;
    GroupAdapter adapter;
    int action_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_group);
        showGroupList();

        ListView view_name = (ListView) findViewById(R.id.listView_manage);
        view_name.setItemsCanFocus(false);
        view_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                action_index = position;
                System.out.println("position "+action_index+" was clicked");
                showActionOption();
            }
        });

    }

    /**
     * Shows list of groups that user is part of
     */
    public void showGroupList() {
        ListView listView = (ListView) findViewById(R.id.listView_manage);
        ArrayList<Group> list = User.getInstance(LoginActivity.email).getGroups();
        GroupAdapter adapter = new GroupAdapter(this, list);
        listView.setAdapter(adapter);
    }

    /**
     * Shows list of possible actions user can take w.r.t. a group
     */
    public void showActionOption(){
        Button exitGroup = (Button) findViewById(R.id.exitGroup);
        exitGroup.setVisibility(View.VISIBLE);

        Button deleteGroup = (Button)findViewById(R.id.deleteGroup);
        deleteGroup.setVisibility(View.VISIBLE);

        Button cancelGroup = (Button)findViewById(R.id.cancelGroup);
        cancelGroup.setVisibility(View.VISIBLE);

        Button emailGroup = (Button) findViewById(R.id.emailGroup);
        emailGroup.setVisibility(View.VISIBLE);
    }

    /**
     * Exits user from group - disassociates group from user in the DB.
     * If user is last member of group, group is deleted.
     */
    public void exitGroup(){
        DBQueries db = DBQueries.getInstance();
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();
        Boolean result = db.leaveGroup(userEmail, group);

        if (!result){
            System.out.println("Leave group action failed");
        }

        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    /**
     * Deletes group from all group members' info; group record deleted
     */
    public void deleteGroup(){
        DBQueries db = DBQueries.getInstance();
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();
        boolean result = db.deleteGroup(group);

        // check whether action was successful
        if (!result) {
            System.out.println("Delete group action failed");
        }

        // refreshes page
        Intent i = new Intent(this, GroupManageActivity.class);
        startActivityForResult(i, 1);
    }

    /*
     ** Emails all members of a group with a reminder to pay. Opens up GMail app with members'
     * email addresses and body message already populated
     */
    public void emailGroup() {
        String userEmail = LoginActivity.email;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        DBQueries dbq = DBQueries.getInstance();

        // get list of group members
        ArrayList<String> members = dbq.getGroupMembers(User.getInstance(userEmail).getNthGroup(
                action_index).getCode());
        String[] emailAddresses = new String[members.size()];
        members.toArray(emailAddresses);
        i.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
        i.putExtra(Intent.EXTRA_SUBJECT, "Reminder to pay bills");
        i.putExtra(Intent.EXTRA_TEXT, "Hello! Please remember to pay your bills!");

        // send email
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) { }
    }

    /**
     * Pops up dialog to make user confirm group deletion
     * @param view
     */
    public void confirmDeleteAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirmation")
                .setIcon(R.mipmap.usericon_2).setNegativeButton("Cancel", null).
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do confirmed delete whole group action
                        sendMembersBills();
                        deleteGroup();
                    }
                }).setMessage("Do you want to delete this group for all members?").create();
        dialog.show();
    }

    /*
     ** Pops up dialog to let user confirm exit group action
     */
    public void confirmExitAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Confirmation")
                .setIcon(R.mipmap.usericon_2).setNegativeButton("Cancel", null)
                .setPositiveButton("Exit",
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do confirmed exit group action
                        sendSingleUserBills();
                        exitGroup();
                    }
                }).setMessage("Do you to exit this group?").create();
        dialog.show();
    }

    /**
     * Refreshes page
     * @param view
     */
    public void cancel(View view){
        Intent i = new Intent(this,GroupManageActivity.class);
        startActivityForResult(i,1);
    }

    /**
     * Navigates user back to page listing their groups
     * @param view
     */
    public void backToExistingGroupList(View view){
        Intent i = new Intent(this,GroupListActivity.class);
        startActivityForResult(i,1);
    }

    /**
     * Send members of group email regarding unpaid bills
     */
    public void sendMembersBills(){
        //get selected group code
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();

        // get email addresses of all members in this group
        DBQueries instance = DBQueries.getInstance();
        ArrayList<String> members = instance.getGroupMembers(group);

        //get group unpaid bills
        String content = emailContent(group);

        //send unpaid bills info to all group members' email boxes
        for(int i = 0 ; i < members.size() ; i ++ ){
            try {
                GMailSender sender = new GMailSender(
                        "roommatespendingtracker@gmail.com",
                        "cis350s18");
                sender.sendMail("Attention: Unpaid bills", "Here are the bills you " +
                                "need to pay\n\n " + content,
                        "roommatespendingtracker@gmail.com", members.get(i));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Sends group member who is leaving unpaid bill info
     */
    public void sendSingleUserBills(){
        // get selected group code
        String userEmail = LoginActivity.email;
        String group = User.getInstance(userEmail).getNthGroup(action_index).getCode();

        // get group unpaid bills
        String content = emailContent(group);

        // send to leaving users
        try {
            GMailSender sender = new GMailSender(
                    "roommatespendingtracker@gmail.com",
                    "cis350s18");
            sender.sendMail("Attention: Unpaid bills", "Here are the bills you need " +
                            "to pay\n\n " + content,
                    "roommatespendingtracker@gmail.com", LoginActivity.email);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gets all bill info for a given group
     * @param groupcode
     * @return bill info as a string
     */
    public String emailContent(String groupcode){
        // load all unpaid bills info as email content
        String content="";
        ArrayList<Bill> billList=getBillsByDate(groupcode);
        for(int i = 0 ; i < billList.size() ; i ++){
            content = content + "Unpaid Bill " + i + ":  " +billList.get(i).getName() +
                    "  Bill amount: $" + billList.get(i).getAmount() + "  Due date: " +
                    billList.get(i).getDueDate() + "  Bill Description: " +
                    billList.get(i).getDesc() + "\n" ;
        }
        return content;
    }

    /**
     * Helper method: array of bills sorted by due date chronologically
     * Assumes there are no bills in the DB from the past, as those should either be deleted after
     * payment is confirmed
     * @param groupcode
     * @return ArrayList of bills for group
     */
    private ArrayList<Bill> getBillsByDate(String groupcode){
        DBQueries instance = DBQueries.getInstance();
        ArrayList<Bill> allBills = new ArrayList<Bill>();
        ResultSet bill_rs = instance.userBillsInGroup(LoginActivity.email, groupcode);

        try {
            while (bill_rs.next()) {
                String name = bill_rs.getString("bill_name");
                String amt = bill_rs.getString("amount");
                String due_date = bill_rs.getString("due_date");
                String id = bill_rs.getString("bill_id");
                String desc = bill_rs.getString("description");
                Bill bill = new Bill(name, amt, due_date, id, desc);
                allBills.add(bill);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        Collections.sort(allBills, new Comparator<Bill>() {
            @Override
            public int compare(Bill bill1, Bill bill2) {
                return bill1.getDueDate().compareTo(bill2.getDueDate());
            }
        });
        return allBills;
    }
}