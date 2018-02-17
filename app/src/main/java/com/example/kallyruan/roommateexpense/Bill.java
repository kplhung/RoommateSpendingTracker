package com.example.kallyruan.roommateexpense;

/**
 * Created by Lily on 2/17/2018.
 */

public class Bill {
    private String name, amount, dueDate;
    public Bill(String name, String amount, String dueDate){
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
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
}
