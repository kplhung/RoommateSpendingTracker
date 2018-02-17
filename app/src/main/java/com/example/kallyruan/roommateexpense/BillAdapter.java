package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<Bill> bills;
    private TextView billName;
    private TextView billAmt;
    private TextView billDate;
    public BillAdapter(Activity activity, ArrayList<Bill> bills){
        this.mActivity = activity;
        this.bills = bills;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = mActivity.getLayoutInflater();

        if(view == null){

            view=inflater.inflate(R.layout.bills_list_row, null);

            billName=(TextView) view.findViewById(R.id.bill_name);
            billAmt=(TextView) view.findViewById(R.id.bill_amount);
            billDate=(TextView) view.findViewById(R.id.bill_date);

        }

        Bill bill = bills.get(position);
        billName.setText(bill.getName());
        billAmt.setText(bill.getAmount());
        billDate.setText(bill.getDueDate());
        return view;
    }
}
