package com.example.kallyruan.roommateexpense.UserPkg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.BillPkg.Bill;
import com.example.kallyruan.roommateexpense.BillPkg.OldBillAdapter;
import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.BillPkg.OldBill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.example.kallyruan.roommateexpense.R;

/**
 * Shows user their payment history.
 * @author Kelly
 */
public class PaymentHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        // get user's old bills in a result set
        DBQueries dbq = DBQueries.getInstance();
        ResultSet rs = dbq.getOldBills(LoginActivity.email);
        if (rs == null) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "You have not paid any bills!",
                    Toast.LENGTH_SHORT);
            t.show();
        } else {
            displayPaymentHistory();
        }
    }

    /**
     * Helper: getOldBills
     */
    public ArrayList<OldBill> getOldBills(ResultSet rs) {
        ArrayList<OldBill> oldBills = new ArrayList<OldBill>();
        try {
            while (rs.next()) {
                String billName = rs.getString("bill_name");
                String groupName = rs.getString("group_name");
                String amount = rs.getString("amount");
                String desc = rs.getString("description");
                String datePaid = rs.getString("date_paid");
                String billID = rs.getString("bill_id");
                OldBill bill = new OldBill(groupName, billName, amount, datePaid, billID,
                        desc);
                oldBills.add(bill);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return oldBills;
    }

    /**
     * Displays past bills in a list
     */
    public void displayPaymentHistory(){
        ListView listView = findViewById(R.id.payment_history_list);
        DBQueries dbq = DBQueries.getInstance();

        // get old bills from DB
        ResultSet rs =  dbq.getOldBills(LoginActivity.email);
        ArrayList<OldBill> oldBills = getOldBills(rs);

        OldBillAdapter adapter = new OldBillAdapter(this, oldBills);
        listView.setAdapter(adapter);
    }
}
