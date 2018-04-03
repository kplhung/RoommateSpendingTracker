package com.example.kallyruan.roommateexpense.BillPkg;

/**
 * Created by Lily on 3/31/2018.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.BillPkg.Bill;
import com.example.kallyruan.roommateexpense.R;

import java.util.ArrayList;

public class UpcomingBillAdapter extends BaseAdapter {
    private Activity mActivity;
    private DBQueries instance;
    private ArrayList<Bill> bills;
    private TextView billName;
    private TextView billAmt;
    private TextView billDate;
    private TextView billGroup;

    public UpcomingBillAdapter(Activity activity, ArrayList<Bill> bills){
        this.mActivity = activity;
        this.bills = bills;
        this.instance = DBQueries.getInstance();
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
            createdView = inflater.inflate(R.layout.upcoming_bills_list_row, null);
            billName = (TextView) createdView.findViewById(R.id.bill_name);
            billAmt = (TextView) createdView.findViewById(R.id.bill_amount);
            billDate = (TextView) createdView.findViewById(R.id.bill_date);
            billGroup = (TextView) createdView.findViewById(R.id.group);
        } else {
            createdView = view;
        }

        final Bill bill = bills.get(position);
//        String groupID = instance.groupName(instance.groupID(bill.getBillID()));
        billName.setText(bill.getName());
        billAmt.setText(bill.getAmount());
        billDate.setText(bill.getDueDate());
//        billGroup.setText(groupID);

        return createdView;
    }
}
