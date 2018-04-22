package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.BillPkg.Bill;
import com.example.kallyruan.roommateexpense.BillPkg.BillListActivity;
import com.example.kallyruan.roommateexpense.BillPkg.UpcomingBillAdapter;
import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.GroupPkg.CreateActivity;
import com.example.kallyruan.roommateexpense.GroupPkg.GroupListAcitivity;
import com.example.kallyruan.roommateexpense.GroupPkg.JoinGroupActivity;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.UserPkg.PasswordChangeActivity;
import com.example.kallyruan.roommateexpense.UserPkg.ProfileActivity;

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kallyruan on 13/2/18.
 */

public class MenuActivity extends Activity {
    private DBQueries instance = DBQueries.getInstance();
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        showUserInfo();
        displayUpcomingBills();
        android.support.v7.widget.AppCompatImageView view = findViewById(R.id.signOutButton);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signOut();
            }
        });
        this.mActivity = this;
    }

    /**
     * Re-routes user to see their list of groups
     * @param view
     */
    public void myGroup(View view){
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent j = new Intent(this, GroupListAcitivity.class);
        j.putExtra("username", username);
        startActivityForResult(j,1);
    }

    /**
     * Allows user to create group; re-routes user to this activity
     * @param view
     */
    public void createGroup(View view){
        Intent i = new Intent(this,CreateActivity.class);
        startActivityForResult(i,1);
    }

    /**
     * Re-routes user to activity that allows user to join a group by entering a code
     * @param view
     */
    public void joinGroup(View view){
        Intent i = new Intent(this,JoinGroupActivity.class);
        startActivityForResult(i, 1);
    }


    /**
     *
     */
    public void manageProfile(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Displays upcoming bills in a list, showing bill's name, amount due for user, due date,
     * and name of group the bill is from
     */
    public void displayUpcomingBills(){
        ListView listView = findViewById(R.id.upcoming_bills_list);
        final ArrayList<Bill> bills = getBillsByDate();
        UpcomingBillAdapter adapter = new UpcomingBillAdapter(this, bills);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos = position + 1;
                Intent i = new Intent(mActivity, BillListActivity.class);
                i.putExtra("group_id", instance.getGroupIdForBill(bills.get(position).getBillID()));
                startActivity(i);
            }

        });
    }

    /**
     * Gets array of bills, sorted chronologically by date
     *
     * Note that this method assumes that there are no bills in the DB from the past, as those
     * would have been deleted after payment confirmation
     * @return ArrayList of bills
     */
    private ArrayList<Bill> getBillsByDate(){
        ArrayList<Bill> allBills = new ArrayList<Bill>();
        ResultSet bill_rs = instance.userBills(LoginActivity.email);

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
        } catch(SQLException e) {
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

    /**
     * Shows user icon and nickname
     **/
    public void showUserInfo(){
        // get nickname, email and set to TextView content
        String nickname = instance.getNickname(LoginActivity.email);
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