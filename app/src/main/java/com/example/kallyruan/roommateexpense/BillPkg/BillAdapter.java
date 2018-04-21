package com.example.kallyruan.roommateexpense.BillPkg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;

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
    private String groupID, userName;

    public BillAdapter(Activity activity, ArrayList<Bill> bills, String groupID, String userName){
        this.mActivity = activity;
        this.bills = bills;
        this.groupID = groupID;
        this.userName = userName;
    }

    /**
     * @return number of bills
     */
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

    /**
     * @param position index of group for which view is desired
     * @param view
     * @param viewGroup
     * @return View for a group
     */
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
                String billID = bill.getBillID();
                String userID = LoginActivity.email;

                v.setVisibility(View.GONE);
                createdView.setVisibility(View.GONE);
                DBQueries dbq = DBQueries.getInstance();
                dbq.deleteBill(userID, groupID, billID);
        }
        });
        return createdView;
    }
}