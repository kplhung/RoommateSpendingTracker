package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<Bill> bills;
    private TextView billName;
    private TextView billAmt;
    private TextView billDate;
    private Button billConfirmButton;
    private String groupID;
    private String username;

    public BillAdapter(Activity activity, ArrayList<Bill> bills, String groupID, String username){
        this.mActivity = activity;
        this.bills = bills;
        this.groupID = groupID;
        this.username = username;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, final View view, ViewGroup viewGroup) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View createdView;

        if(view == null){

            createdView = inflater.inflate(R.layout.bills_list_row, null);

            billName = (TextView) createdView.findViewById(R.id.bill_name);
            billAmt = (TextView) createdView.findViewById(R.id.bill_amount);
            billDate = (TextView) createdView.findViewById(R.id.bill_date);
            billConfirmButton = (Button) createdView.findViewById(R.id.bill_confirm);
        } else {
            createdView = view;
        }

        final Bill bill = bills.get(position);
        billName.setText(bill.getName());
        billAmt.setText(bill.getAmount());
        billDate.setText(bill.getDueDate());

        billConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                createdView.setVisibility(View.GONE);
                // TODO: (Kelly) add logic to remove Bill from DB
                DBQueries db = DBQueries.getInstance();
                // TODO: bill ID info; fix dummy variable
                String billID = null;

                boolean success = db.deleteBill(username, groupID, billID);
            }
        });
        return createdView;
    }
    public void changeBill(View view){
    }
    public void changeBill(View view){
    }
}
