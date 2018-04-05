package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.BillPkg.Bill;
import com.example.kallyruan.roommateexpense.BillPkg.UpcomingBillAdapter;
import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.GroupPkg.GroupListAcitivity;
import com.example.kallyruan.roommateexpense.GroupPkg.JoinGroupActivity;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.UserPkg.PasswordChangeActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        displayUpcomingBills();
    }

    public void myGroup(View view){
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent j = new Intent(this, GroupListAcitivity.class);
        j.putExtra("username", username);
        startActivityForResult(j,1);
    }
    
    public void createGroup(View view){
        Intent i = new Intent(this,CreateActivity.class);
        startActivityForResult(i,1);
    }

    public void joinGroup(View view){
        Intent i = new Intent(this,JoinGroupActivity.class);
        startActivityForResult(i, 1);
    }

    public void changePassword(View view) {
        Intent i = new Intent(this, PasswordChangeActivity.class);
        startActivityForResult(i, 1);
    }

    /*
    displays the upcoming bills in a list, showing the bill's name, amount due for user, due date,
    and name of the group the bill is from
     */
    public void displayUpcomingBills(){
        ListView listView = findViewById(R.id.upcoming_bills_list);
        ArrayList<Bill> bills = getBillsByDate();
        UpcomingBillAdapter adapter = new UpcomingBillAdapter(this, bills);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos = position + 1;
                Toast.makeText(MenuActivity.this, Integer.toString(pos)+" Clicked",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    /*
        Helper function to get an array of bills sorted by due date (chronologically with the one
        closest to current date listed first)
        This function assumes that there are no bills in the database from the past, as those should
        either be deleted after payment is confirmed or a recurrent one in which case the next duedate
        is listed in the database.
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
