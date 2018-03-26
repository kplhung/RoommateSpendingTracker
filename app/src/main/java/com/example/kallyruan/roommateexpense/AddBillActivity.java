package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by Lily on 3/3/2018.
 */

public class AddBillActivity extends Activity{
    private int reminder_timeframe;
    private DBQueries instance = DBQueries.getInstance();
    private String group_id;
    private RoommateAdapter roommate_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        this.group_id = i.getStringExtra("group_id");
        setContentView(R.layout.activity_add_bill);
        Spinner spinner = (Spinner) findViewById(R.id.timeframe_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeframe_adapter = ArrayAdapter.createFromResource(this,
                R.array.bill_date_timeframes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        timeframe_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(timeframe_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                reminder_timeframe = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ListView listView = (ListView) findViewById(R.id.split_bill_list);
        ArrayList<String> roommates_Ids = instance.groupMembers(group_id);
        ArrayList<User> roommates = new ArrayList<User> ();
        for (String user: roommates_Ids){
            roommates.add(new User(user));
        }
        this.roommate_adapter = new RoommateAdapter(this, roommates);
        listView.setAdapter(roommate_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int pos = position + 1;
                Toast.makeText(AddBillActivity.this, Integer.toString(pos) + " Clicked", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void saveBill(View view){
        String label = ((TextView) findViewById(R.id.label_input)).getText().toString();
        String desc = ((TextView) findViewById(R.id.description_input)).getText().toString();
        String due = ((TextView) findViewById(R.id.duedate_input)).getText().toString();

        ListView listview = (ListView) findViewById(R.id.split_bill_list);

        for (int i = 0; i < listview.getChildCount(); i++){
            View row = listview.getChildAt(i);
            String name = ((TextView) row.findViewById(R.id.roommate_name)).getText().toString();
            double amt = Double.parseDouble(((TextView)row.findViewById(R.id.pay_amt)).getText().toString());
            try {
                instance.addBill(name, this.group_id, label, amt, due, desc);
            }catch (IllegalArgumentException e){
                //toast the error message;
                Context context = getApplicationContext();
                CharSequence text = e.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        Intent i = new Intent(this,BillListActivity.class);
        i.putExtra("group_id", group_id);
        startActivityForResult(i,1);
        finish();
    }
}
