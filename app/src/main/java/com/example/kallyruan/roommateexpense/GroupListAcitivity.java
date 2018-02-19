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
    // this following idList is just made up for temporary testing
    public static ArrayList<Integer>  idList= new ArrayList<Integer>(Arrays.asList(10001,10002));
    public static ArrayList<String> nameList= new ArrayList<String>(Arrays.asList("RodinCollege 1010","RodinCollege 1011"));
    public static ArrayList<Integer> participationList=new ArrayList<Integer>(Arrays.asList(4,3));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_group);
        showGroupList();
    }

    public void showGroupList() {
        //the following code is competitive... though i don't know how to avoid findViewById for every time....
        if(idList.size()>=1) {
            TextView row2Column1 = (TextView) findViewById(R.id.row2Column1);
            row2Column1.setText(Integer.toString(idList.get(0)));
            TextView row2Column2 = (TextView) findViewById(R.id.row2Column2);
            row2Column2.setText(nameList.get(0));
            TextView row2Column3 = (TextView) findViewById(R.id.row2Column3);
            row2Column3.setText(Integer.toString(participationList.get(0)));
        }
        if(idList.size()>=2) {
            TextView row3Column1 = (TextView) findViewById(R.id.row3Column1);
            row3Column1.setText(Integer.toString(idList.get(1)));
            TextView row3Column2 = (TextView) findViewById(R.id.row3Column2);
            row3Column2.setText(nameList.get(1));
            TextView row3Column3 = (TextView) findViewById(R.id.row3Column3);
            row3Column3.setText(Integer.toString(participationList.get(1)));
        }

        if(idList.size()>=3) {
            TextView row4Column1 = (TextView) findViewById(R.id.row4Column1);
            row4Column1.setText(Integer.toString(idList.get(2)));
            TextView row4Column2 = (TextView) findViewById(R.id.row4Column2);
            row4Column2.setText(nameList.get(2));
            TextView row4Column3 = (TextView) findViewById(R.id.row4Column3);
            row4Column3.setText(Integer.toString(participationList.get(2)));
        }

    }

    public static Integer newGroupId(){
        int index = idList.size()-1;
        int last = idList.get(index);
        return (Integer) (last+1) ;
    }
}
