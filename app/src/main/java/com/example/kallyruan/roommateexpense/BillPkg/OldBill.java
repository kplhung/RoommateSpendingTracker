package com.example.kallyruan.roommateexpense.BillPkg;

public class OldBill extends Bill {
    private String groupName;
    public OldBill(String groupName, String name, String amount, String dueDate, String billID,
                   String desc) {
        super(name, amount, dueDate, billID, desc);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDatePaid() {
        return super.getDueDate();
    }
}
