package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        ListView gridview = (ListView) findViewById(R.id.bills_list);
        ArrayList<Bill> bills = getBills();
        gridview.setAdapter(new BillAdapter(this, bills));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(BillListActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Bill> getBills(){
        ArrayList<Bill> allBills = new ArrayList<Bill>();
        //before the db is set up, we will use hard-coded data
        Bill bill1 = new Bill("Rent", "2400", "2018/03/01");
        Bill bill2 = new Bill("Electricity", "50", "2018/03/01");
        allBills.add(bill1);
        allBills.add(bill2);
        return allBills;
    }
}
