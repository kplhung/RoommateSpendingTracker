package com.example.kallyruan.roommateexpense;

import android.app.Activity;
import android.os.Bundle;

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
}
