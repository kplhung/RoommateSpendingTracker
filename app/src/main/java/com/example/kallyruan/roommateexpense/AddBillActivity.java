package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Lily on 3/3/2018.
 */

public class AddBillActivity extends Activity{
    private int reminder_timeframe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ArrayList<User> roommates = getRoommates();
        RoommateAdapter roommate_adapter = new RoommateAdapter(this, roommates);
        Log.i("listview", ""+(listView == null));
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

    }

    public ArrayList<User> getRoommates(){
        ArrayList<User> roommates = new ArrayList<User>();
        //before the db is set up, we will use hard-coded data
        User user1 = new User("name1");
        User user2 = new User("name2");
        roommates.add(user1);
        roommates.add(user2);
        return roommates;
    }
}
