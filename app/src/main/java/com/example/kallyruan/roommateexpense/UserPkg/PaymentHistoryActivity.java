package com.example.kallyruan.roommateexpense.UserPkg;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;

import java.sql.ResultSet;
import java.sql.SQLException;

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
            // TODO: query failed
        } else {
            try {
                while (rs.next()) {
                    // TODO: show each of these bills
                }
            } catch(SQLException e) {
                // TODO: handle exception
            }
        }
    }
}
