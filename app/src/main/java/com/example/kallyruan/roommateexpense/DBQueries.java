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
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

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
     * PARAMETER CHECKING
     */

    /**
     * Throws an IllegalArgumentException if the email is null
     * @param email
     */
    void nullEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Cannot have an empty user id");
        }
    }

    /**
     * Throws an IllegalArgumentException if the password is null
     * @param password
     */
    void nullPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Cannot have an empty password");
        }
    }

    /**
     * Throws an IllegalArgumentException if the group_id is null
     * @param group_id
     */
    void nullGroup(String group_id) {
        if (group_id == null) {
            throw new IllegalArgumentException("Cannot have an empty group id");
        }
    }

    /**
     * Throws an IllegalArgumentException if the date is null, isn't the right length, or isn't
     * correctly formatted
     * @param date
     */
    void validDueDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Must enter a non-empty due date");
        }

        if (date.length() != 9 && date.length() != 10) {
            throw new IllegalArgumentException("Due date must be 9 or 10 characters long");
        }

        if (date.charAt(4) != '-' || date.charAt(6) != '-') {
            throw new IllegalArgumentException("Must enter a correctly formatted due date");
        }
    }

    /**
     * Throws an IllegalArgumentException if the group name is null or >20 characters
     * @param name
     */
    void nullGroupName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Chosen group name cannot be empty");
        }

        if (name.length() > 20) {
            throw new IllegalArgumentException("The group name must be max 20 characters");
        }
    }

    /**
     * Used for generating both group_id's and bill_id's
     * Generates a random 20 character sequence based on the English alphabet and integers 0-9
     * @return the random 20 character string
     */
    String generateId() {
        StringBuilder testId = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * alphabet.length());
            testId.append(alphabet.charAt(index));
        }

        return testId.toString();
    }

    /**
     * USER QUERIES
     */

    /**
     * Handles login to the app
     * @param email of the user
     * @param password of the user
     * @return 0 if the email doesn't exist in the database, 1 if the password was incorrect,
     * 2 if login was successful
     */
    int login(String email, String password) {
        nullEmail(email);
        nullPassword(password);

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
                    return 2; //successful login
                }
                else {
                    return 1; //incorrect password
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0; //user doesn't exist in the database
    }

    /**
     * Checks whether the user exists in the database
     * @param email of the user
     * @return true if exists, false otherwise
     */
    boolean userExists(String email) {
        nullEmail(email);

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
        nullEmail(email);
        nullPassword(password);

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
        nullEmail(email);

        //DOESN'T CHECK IF THE USER EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

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
        nullEmail(email);

        //DOESN'T CHECK IF THE USER EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

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
        nullEmail(email);
        nullGroup(group_id);

        //DOESN'T CHECK IF THE USER OR GROUP EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

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

    /**
     * BILL QUERIES
     */

    /**
     * Checks whether the bill_id is already in use by another bill
     * @param bill_id unique identifier for the bill
     * @return true if the bill id is already being used, false otherwise
     */
    boolean billExists(String bill_id) {
        String query = "SELECT * FROM Bills WHERE bill_id = " + bill_id;
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
     * Creates a bill based on the inputted information
     * @param user
     * @param group
     * @param name
     * @param amt
     * @param due
     * @param desc
     * @return if the bill addition was successful
     */
    boolean addBill(String user, String group, String name, double amt, String due, String desc) {
        nullEmail(user);
        nullGroup(group);
        validDueDate(due);

        //DOESN'T CHECK IF THE USER OR GROUP EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

        String newBillId = generateId();
        while (billExists(newBillId)) {
            newBillId = generateId();
        }

        String bills = "INSERT INTO Bills VALUES (" + newBillId + ", " + name + ", " + user +
                ", " + amt + ", " + due + ", " + desc + ")";
        String groupBills = "INSERT INTO GroupBills VALUES(" + group + ", " + newBillId + ")";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            stmt.executeUpdate(groupBills);

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    boolean deleteBill(String user_id, String group_id, String bill_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        //DOESN'T CHECK IF THE USER/GROUP/BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON
        //IMPLEMENTATION

        String groupBills = "DELETE FROM GroupBills WHERE group_id=" + group_id + " AND bill_id=" +
                bill_id;
        String bills = "DELETE FROM Bills WHERE bill_id = " + bill_id + " AND user_id = " + user_id;
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(groupBills);
            stmt.executeUpdate(bills);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    boolean changeDueDate(String bill_id, String due_date) {
        validDueDate(due_date);

        //DOESN'T CHECK IF THE BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String bills = "UPDATE Bills SET due_date = " + due_date + " WHERE bill_id = " + bill_id;
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    boolean changeAmount(String bill_id, double amt) {
        //DOESN'T CHECK IF THE BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String bills = "UPDATE Bills SET amount = " + amt + " WHERE bill_id = " + bill_id;
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * GROUP QUERIES
     */

    /**
     * Checks whether that group already exists in the database
     * @param group_id
     * @return if the group_id is taken
     */
    boolean groupExists(String group_id) {
        String query = "SELECT * FROM Groups WHERE group_id = '" + group_id + "'";
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
     * Returns the group name (in a ResultSet), given a group_id
     * @param group_id
     * @return group_name
     */
    String groupName(String group_id) {
        String query = "SELECT * FROM Groups WHERE group_id = '" + group_id + "'";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getString("group_name");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Updates the database with the new group, given a user_id of the creator and the
     * user-specified name of the group
     * @param user_id
     * @param group_name
     * @return if the update was successful
     */
    boolean createGroup(String user_id, String group_name) {
        nullEmail(user_id);
        nullGroupName(group_name);

        String group_id = generateId();
        while (groupExists(group_id)) {
            group_id = generateId();
        }

        String groups = "INSERT INTO Groups VALUES ('" + group_id + "', '" + group_name + "')";
        String userGroups = "INSERT INTO UserGroups VALUES (" + user_id + ", '" + group_id + "')";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(groups);
            stmt.executeUpdate(userGroups);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Adds the specified user to the specified group
     * @param user_id
     * @param group_id
     * @return 0 if the addition failed, 1 if the user doesn't exist in the database, 2 if the
     * addition was successful
     */
    int addUserToGroup(String user_id, String group_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        if (userExists(user_id)) {
            String userGroups = "INSERT INTO UserGroups VALUES (" + user_id + ", " + group_id + ")";
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                stmt.executeUpdate(userGroups);
                return 2; //addition successful
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

        else {
            return 1; //user doesn't exist, invite the user to the app
        }

        return 0; //addition failed
    }

    /**
     * Removes the user from the group and updates all tables accordingly
     * @param user_id
     * @param group_id
     * @return if the removal was successful
     */
    boolean leaveGroup(String user_id, String group_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        String bill_id = null;

        String groupBills = "DELETE FROM GroupBills WHERE group_id = " + group_id + " AND bill_id" +
                " = " + bill_id;
        String bills = "DELETE FROM Bills WHERE bill_id = " + bill_id + " AND user_id = " + user_id;
        String userGroups = "DELETE FROM UserGroups WHERE user_id = " + user_id + " AND group_id " +
                "= " + group_id;
        Statement stmt = null;
        ResultSet usersBills = userBillsInGroup(user_id, group_id);
        try {
            while (usersBills.next()) {
                bill_id = usersBills.getString("bill_id");
                stmt.executeUpdate(groupBills);
                stmt.executeUpdate(bills);
            }
            stmt.executeUpdate(userGroups);

            deleteGroup(group_id);

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if the group should be deleted (participation 0) and deletes accordingly
     * @param group_id
     * @return if the delete was successful
     */
    boolean deleteGroup(String group_id) {
        nullGroup(group_id);

        if (groupParticipation(group_id) == 0) {
            String groups = "DELETE FROM Groups WHERE group_id = " + group_id;
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                stmt.executeUpdate(groups);
                return true;
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Determines how many users are in a given group
     * @param group_id
     * @return the number of users in the group
     */
    int groupParticipation(String group_id) {
        nullGroup(group_id);

        String query = "SELECT COUNT(DISTINCT user_id) FROM UserGroups WHERE group_id = " +
                group_id;
        Statement stmt = null;
        ResultSet rs = null;
        int count = -1;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            count = rs.getInt(0);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

}
