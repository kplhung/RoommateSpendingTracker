package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillListActivity extends Activity {
    private String username, group_id;
    private DBQueries instance = DBQueries.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        this.username = i.getStringExtra("username");
        this.group_id = i.getStringExtra("group_id");
        setContentView(R.layout.activity_bill_list);
        ListView listView = (ListView) findViewById(R.id.bills_list);
        ArrayList<Bill> bills = getBills();
        BillAdapter adapter = new BillAdapter(this, bills, this.group_id, this.username);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos = position + 1;
                Toast.makeText(BillListActivity.this, Integer.toString(pos)+" Clicked",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    public ArrayList<Bill> getBills(){
        ResultSet bill_rs = instance.userBillsInGroup(username, group_id);
        ArrayList<Bill> allBills = new ArrayList<Bill>();
        //before the db is set up, we will use hard-coded data
        Bill bill1 = new Bill("Rent", "2400", "2018/03/01");
        Bill bill2 = new Bill("Electricity", "50", "2018/03/01");
        allBills.add(bill1);
        allBills.add(bill2);
//        try {
//            while (bill_rs.next()) {
//                String name = bill_rs.getString("bill_name");
//                String amt = bill_rs.getString("amount");
//                String due_date = bill_rs.getString("due_date");
//                Bill bill = new Bill(name, amt, due_date);
//                allBills.add(bill);
//            }
//        } catch(SQLException e){
//        }
        return allBills;
    }

    public void addBill(View view){
        Intent i = new Intent(this, AddBillActivity.class);
        startActivityForResult(i,1);
        i.putExtra("username", this.username);
        i.putExtra("group_id", this.group_id);
    }
}
