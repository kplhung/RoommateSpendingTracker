package com.example.kallyruan.roommateexpense.BillPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillListActivity extends Activity {
    private String group_id;
    private int pos;
    private DBQueries instance = DBQueries.getInstance();
    private ArrayList<Bill> bills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        showUserInfo();
        
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        String userEmail = LoginActivity.email;
        ListView listView = (ListView) findViewById(R.id.bills_list);
        bills = getBills();
        BillAdapter adapter = new BillAdapter(this, bills, group_id, userEmail);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                pos = position + 1;
                Toast.makeText(BillListActivity.this, Integer.toString(pos)+" Clicked",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    public ArrayList<Bill> getBills(){
        ResultSet bill_rs = instance.userBillsInGroup(LoginActivity.email, group_id);
        ArrayList<Bill> allBills = new ArrayList<Bill>();
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
        return allBills;
    }

    public void addBill(View view){
        Intent i = new Intent(this, AddBillActivity.class);
        i.putExtra("group_id", this.group_id);
        startActivityForResult(i,1);
    }
    public void editBill(View view){
        Intent i = new Intent(this, AddBillActivity.class);
        i.putExtra("group_id", this.group_id);
        Bill bill = bills.get(pos);
        i.putExtra("bill_code", bill.getBillID());
        i.putExtra("bill_name", bill.getName());
        i.putExtra("bill_amt", bill.getAmount());
        i.putExtra("bill_date", bill.getDueDate());
        i.putExtra("bill_desc", bill.getDesc());
        startActivityForResult(i, 1);
    }

    /**
     * this method is to get user icon and nickname from database and show it in the interface
     **/
    public void showUserInfo(){
        //problem with connecting db, hardcode now
        TextView userNickname = findViewById(R.id.user_nickname);
        userNickname.setText("Hard-code");
        ImageView image = findViewById(R.id.user_icon);
        image.setImageResource(R.mipmap.usericon_5);

        //get nickname and set to Textview content
        /*
        String nickname = instance.getNickname(LoginActivity.email);
        TextView userNickname = findViewById(R.id.nicknameField);
        userNickname.setText(nickname);


        //get icon and set to corresponding imageView

        String icon = instance.getIcon(LoginActivity.email);
        int iconIndex=Integer.parseInt(icon);
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
        */
    }
}
