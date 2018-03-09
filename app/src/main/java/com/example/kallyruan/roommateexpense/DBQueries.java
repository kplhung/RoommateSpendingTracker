package com.example.kallyruan.roommateexpense;

/**
 * Created by evephelps on 3/9/18.
 */

public class DBQueries {
    private static final DBQueries ourInstance = new DBQueries();

    public static DBQueries getInstance() {
        return ourInstance;
    }

    private DBQueries() {
    }
}
