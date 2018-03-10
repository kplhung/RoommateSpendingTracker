package com.example.kallyruan.roommateexpense;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by evephelps on 3/9/18.
 */

public class DBQueries {
    private static DBQueries ourInstance = new DBQueries();
    private final Connection con;

    public static DBQueries getInstance() {
        if (ourInstance == null) {
            ourInstance = new DBQueries();
        }
        return ourInstance;
    }

    private DBQueries() {
        con = DBConnect.getConnection();
    }

    /**
     * Handles login to the app
     * @param email of the user
     * @param password of the user
     * @return 0 if the email doesn't exist in the database, 1 if the password was incorrect,
     * 2 if login was successful
     */
    int login(String email, String password) {
        boolean exists = userExists(email);

        if (exists) {
            String query = "SELECT password FROM Users WHERE user_id = " + email;
            ResultSet rs = null;
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);
                rs.next();
                String realPassword = rs.getString("password");

                if (realPassword.equals(password)) {
                    return 2;
                }
                else {
                    return 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Checks whether the user exists in the database
     * @param email of the user
     * @return true if exists, false otherwise
     */
    boolean userExists(String email) {
        String query = "SELECT * FROM Users WHERE user_id = " + email;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if (!rs.next()) {
                return false;
            }
            return true;

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Signs up the user to the app
     * @param email of the user
     * @param password of the user
     * @return true if sign up was successful, false if the user already has an account
     */
    boolean signUp(String email, String password) {
        boolean newUser = !userExists(email);

        if (newUser) {
            String query = "INSERT INTO Users VALUES(\"" + email + "\",\"" + password + "\")";
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                stmt.executeUpdate(query);

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Finds all the groups that a user is in
     * @param email of the user
     * @return the group_ids of the groups, in a ResultSet
     */
    ResultSet userGroups(String email) {
        String query = "SELECT group_id FROM UserGroups WHERE user_id = " + email;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds all information on all the bills assigned to the user
     * @param email of the user
     * @return a ResultSet including the bill_id, bill_name, amount, due_date, and description
     * for each of the user's bills
     */
    ResultSet userBills(String email) {
        String query = "SELECT bill_id, bill_name, amount, due_date, description FROM Bills " +
                "WHERE user_id = " + email;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds all the information on all the bills assigned to the user in a given group
     * @param email of the user
     * @param group_id of the group
     * @return a ResultSet including the bill_id, bill_name, amount, due_date, and description
     * for each of the user's bills
     */
    ResultSet userBillsInGroup(String email, String group_id) {
        String query = "SELECT bill_id, bill_name, amount, due_date, description FROM (Bills AS " +
                "B INNER JOIN (SELECT bill_id FROM GroupBills WHERE group_id = " + group_id + ") " +
                "AS G ON B.bill_id = G.bill_id) WHERE user_id = " + email;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
