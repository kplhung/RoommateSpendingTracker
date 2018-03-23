package com.example.kallyruan.roommateexpense;

/**
 * Created by Lily on 2/17/2018.
 */

public class Bill {
    private String name, amount, dueDate, billID;
    public Bill(String name, String amount, String dueDate, String billID){
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.billID = billID;
    }
    public void updateDueDate(String newDueDate){
        this.dueDate = newDueDate;
    }

    public String getName(){
        return name;
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
}
