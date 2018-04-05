package com.example.kallyruan.roommateexpense.BillPkg;

/**
 * Created by Lily on 2/17/2018.
 */

public class Bill {
    private String name, amount, dueDate, billID, desc;
    public Bill(String name, String amount, String dueDate, String billID, String desc){
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.billID = billID;
        this.desc = desc;
    }
    public void updateDueDate(String newDueDate){
        this.dueDate = newDueDate;
    }

    public String getName(){
        return name;
    }

    public String getDesc() { return desc; }

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
