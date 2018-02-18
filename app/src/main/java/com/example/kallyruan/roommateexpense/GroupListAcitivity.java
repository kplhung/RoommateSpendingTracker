package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kallyruan on 13/2/18.
 */

public class GroupListAcitivity extends Activity{
    List<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_group);

        // this following idList is just made up for temporary testing
        idList= Arrays.asList(10001,10002);

    }
    public void loadBillList(View view){
        Intent i = new Intent(this,BillListActivity.class);
        //temporarily show info for group 1
        i.putExtra("group_id", idList.get(0) + "");
        startActivity(i);
    }
}
