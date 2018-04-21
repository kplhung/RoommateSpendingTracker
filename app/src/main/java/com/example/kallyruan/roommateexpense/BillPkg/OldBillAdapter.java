package com.example.kallyruan.roommateexpense.BillPkg;

import com.example.kallyruan.roommateexpense.DB.DBQueries;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.R;

import java.util.ArrayList;

/**
 * Created by Kelly on 4/20/2018.
 */
public class OldBillAdapter extends BaseAdapter {
    private Activity mActivity;
    private DBQueries instance;
    private ArrayList<OldBill> oldBills;
    private TextView groupName;
    private TextView billName;
    private TextView billAmt;
    private TextView billDatePaid;
    private TextView billDescription;

    public OldBillAdapter(Activity activity, ArrayList<OldBill> oldBills){
        this.mActivity = activity;
        this.oldBills = oldBills;
        this.instance = DBQueries.getInstance();
    }

    /**
     * @return number of bills for group
     */
    @Override
    public int getCount() {
        return oldBills.size();
    }

    /*
     * Getter methods
     */
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
            createdView = inflater.inflate(R.layout.old_bills_list_row, null);
            groupName = (TextView) createdView.findViewById(R.id.group_name);
            billName = (TextView) createdView.findViewById(R.id.bill_name);
            billAmt = (TextView) createdView.findViewById(R.id.bill_amount);
            billDatePaid = (TextView) createdView.findViewById(R.id.date_paid);
            billDescription = (TextView) createdView.findViewById(R.id.bill_description);
        } else {
            createdView = view;
        }

        final OldBill bill = oldBills.get(position);
        groupName.setText(bill.getGroupName());
        billName.setText(bill.getName());
        billAmt.setText(bill.getAmount());
        billDatePaid.setText(bill.getDatePaid());
        billDescription.setText(bill.getDesc());

        return createdView;
    }
}

