package com.example.kallyruan.roommateexpense.BillPkg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;
import com.example.kallyruan.roommateexpense.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Lily on 2/17/2018.
 */

public class BillAdapter extends BaseExpandableListAdapter {
    private Activity mActivity;
    private DBQueries instance;
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
        this.instance = DBQueries.getInstance();
    }

//    /**
//     * @return number of bills
//     */
//    @Override
//    public int getCount() {
//        return bills.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View createdView;

        if(view == null){
            createdView = inflater.inflate(R.layout.bill_list_child, null);
        } else {
            createdView = view;
        }

        final Bill bill = bills.get(groupPosition);
        TextView bill_payer = (TextView) createdView.findViewById(R.id.bill_list_payer_name);
        TextView bill_payer_amt = (TextView) createdView.findViewById(R.id.bill_list_payer_amt);

        String[] payer = bill.getOtherPayers().get(childPosition);
        bill_payer.setText(payer[0]);
        bill_payer_amt.setText(payer[1]);

        return createdView;
    }

    @Override
    public int getGroupCount() {
        return bills.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return instance.getGroupMembers(instance.getGroupIdForBill(bills.get(i).getBillID())).size();
    }

    @Override
    public Object getGroup(int i) {
        return bills.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return instance.getGroupMembers(bills.get(i).getBillID()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View createdView;

        if(view == null){
            createdView = inflater.inflate(R.layout.bills_list_group, null);
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
                DBQueries dbq = DBQueries.getInstance();
                dbq.deleteBill(LoginActivity.email, groupID, bill.getBillID());
            }
        });
        return createdView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}