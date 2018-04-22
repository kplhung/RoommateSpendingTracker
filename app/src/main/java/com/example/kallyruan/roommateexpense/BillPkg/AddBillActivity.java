package com.example.kallyruan.roommateexpense.BillPkg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.RoommateAdapter;
import com.example.kallyruan.roommateexpense.UserPkg.User;

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
        final Intent i = getIntent();
        this.group_id = i.getStringExtra("group_id");
        setContentView(R.layout.activity_add_bill);

        Button saveButton = (Button) findViewById(R.id.save_bill_btn);

        // load bills
        loadAddBillItems();
        if (i.getStringExtra("bill_code") != null){
            Log.i("new bill", "false");
            loadExistingBill(i);
        } else {
            Log.i("new bill", "true");
            loadRoommateList();
        }

        // bills saved upon user clicking
        saveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (i.getStringExtra("bill_code") != null) {
                    saveExistingBill(v, i);
                } else {
                    saveNewBill(v);
                }
            }
        });
    }

    /**
     * Loads options user must select to add bill
     */
    public void loadAddBillItems(){
        Spinner spinner = (Spinner) findViewById(R.id.timeframe_spinner);

        // create ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeframe_adapter = ArrayAdapter.createFromResource(this,
                R.array.bill_date_timeframes, android.R.layout.simple_spinner_item);

        // specify layout to use when list of choices appears
        timeframe_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply the adapter to spinner
        spinner.setAdapter(timeframe_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                reminder_timeframe = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    /**
     * Loads roommates that are part of a given group so that user can allot dollar amounts
     */
    public void loadRoommateList(){
        ListView listView = (ListView) findViewById(R.id.split_bill_list);
        ArrayList<String> roommates_Ids = instance.getGroupMembers(group_id);
        ArrayList<User> roommates = new ArrayList<User> ();
        for (String user: roommates_Ids){
            roommates.add(new User(user));
        }

        this.roommate_adapter = new RoommateAdapter(this, roommates);
        listView.setAdapter(roommate_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    /**
     * Loads existing bills for a given user for display
     * @param i intent
     */
    public void loadExistingBill(Intent i){
        // set bill names
        EditText bill_name = (EditText) findViewById(R.id.label_input);
        bill_name.setText(i.getStringExtra("bill_name"));
        bill_name.setEnabled(false);

        // set bill descriptions
        EditText bill_desc = (EditText) findViewById(R.id.description_input);
        bill_desc.setText(i.getStringExtra("bill_desc"));
        bill_desc.setEnabled(false);

        // set bill dates
        ((EditText) findViewById(R.id.duedate_input)).setText(i.getStringExtra("bill_date"));
        ListView listView = (ListView) findViewById(R.id.split_bill_list);

        ArrayList<String> roommates_Ids = instance.getGroupMembers(group_id);
        ArrayList<User> roommates = new ArrayList<User> ();
        roommates.add(new User(LoginActivity.email));

        this.roommate_adapter = new RoommateAdapter(this, roommates);
        listView.setAdapter(roommate_adapter);
    }

    /**
     * Handles saving bill that user is trying to add (propagating to database)
     * @param view
     */
    public void saveNewBill(View view){
        String label = ((TextView) findViewById(R.id.label_input)).getText().toString();
        String desc = ((TextView) findViewById(R.id.description_input)).getText().toString();
        String due = ((TextView) findViewById(R.id.duedate_input)).getText().toString();

        // check if user missed a field when creating a new bill
        if (label == "" || desc == "" || due == ""){
            Context context = getApplicationContext();
            CharSequence text = "Missing a field";

            // alert user
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            ListView listview = (ListView) findViewById(R.id.split_bill_list);

            for (int i = 0; i < listview.getChildCount(); i++) {
                View row = listview.getChildAt(i);
                String name = ((TextView) row.findViewById(R.id.roommate_name)).getText().toString();
                try {
                    double amt = Double.parseDouble(((TextView)
                            row.findViewById(R.id.pay_amt)).getText().toString());
                    instance.addBill(name, this.group_id, label, amt, due, desc);
                    Intent intent = new Intent(this, BillListActivity.class);
                    intent.putExtra("group_id", group_id);
                    startActivityForResult(intent, 1);
                    finish();
                } catch (NumberFormatException e) {
                    // user entered invalid owed amount
                    Context context = getApplicationContext();
                    CharSequence text = "Enter a valid number for amount required to pay";

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (IllegalArgumentException e) {
                    // other error: show user this message
                    Context context = getApplicationContext();
                    CharSequence text = e.getMessage();

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        }
    }

    /**
     * Saves changes to existing bill, propagating to database
     * @param view
     * @param i
     */
    public void saveExistingBill(View view, Intent i){
        String newDueDate = ((EditText) findViewById(R.id.duedate_input)).getText().toString();
        try {
            // set new amount
            Double newAmt = Double.parseDouble(((EditText)
                    findViewById(R.id.pay_amt)).getText().toString());
            if (!i.getStringExtra("bill_date").equals(newDueDate)) {
                instance.changeDueDate(i.getStringExtra("bill_code"), newDueDate);
            } else if (!i.getStringExtra("bill_amt").equals(newAmt)) {
                instance.changeAmount(i.getStringExtra("bill_code"), newAmt);
            }

            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra("group_id", group_id);
            startActivityForResult(intent, 1);
            finish();
        } catch (NumberFormatException e) {
            // user entered invalid owed amount
            Context context = getApplicationContext();
            CharSequence text = "Enter a valid number for amount required to pay";

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    /**
     * Cancels add bill and navigates back to list of bills within a group for a user
     * @param view
     */
    public void cancelAddBill(View view){
        Intent i = new Intent(this, BillListActivity.class);
        i.putExtra("group_id", group_id);
        startActivityForResult(i,1);
    }
}