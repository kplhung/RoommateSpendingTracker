package com.example.kallyruan.roommateexpense.BillPkg;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * POJO that keeps track of individual Bill information
 * Created by Lily on 2/17/2018.
 */

public class Bill {
    private String name, amount, dueDate, billID, desc;
    private ArrayList<String []> other_payers;

    public Bill(String name, String amount, String dueDate, String billID, String desc) {
        this(name, amount, dueDate, billID, desc, null);
    }
    public Bill(String name, String amount, String dueDate, String billID, String desc, ArrayList<String []> other_payers) {
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.billID = billID;
        this.desc = desc;
        this.other_payers = other_payers;
    }

    /**
     * Updates due date for a given bill
     * @param newDueDate to replace old due date
     */
    public void updateDueDate(String newDueDate){
        this.dueDate = newDueDate;
    }

    /*
     * Getter methods for this POJO
     */
    public String getName(){
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getAmount(){
        return amount;
    }

    public String getDueDate(){
        return dueDate;
    }

    public String getBillID() {
        return this.billID;
    }

    public ArrayList<String []> getOtherPayers() {
        return other_payers;
    }
}
