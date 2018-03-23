package com.example.kallyruan.roommateexpense;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;


/**
 * Contains functions for all database queries supported in the app
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
     * @param p
     */
    void nullPassword(String p) {
        if (p == null) {
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

        if (date.length() != 10) {
            throw new IllegalArgumentException("Due date must be 10 characters long");
        }

        if (date.charAt(4) != '-' || date.charAt(7) != '-') {
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

        String query = "SELECT B.bill_id, B.bill_name, B.amount, B.due_date, B.description FROM (Bills AS " +
                "B INNER JOIN (SELECT bill_id FROM GroupBills WHERE group_id = '" + group_id + "') " +
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
     * Handles safe deletion of a user's account from the database
     * @param email
     * @return whether the deletion was successful
     */
    boolean deleteAccount(String email) {
        nullEmail(email);

        String users = "DELETE FROM Users WHERE user_id = " + email;
        Statement stmt = null;

        String group_id = null;

        ResultSet usersGroups = userGroups(email);
        try {
            //delete all bills and groups -- all bills must be assigned an associated group
            while (usersGroups != null && usersGroups.next()) {
                group_id = usersGroups.getString("group_id");
                leaveGroup(email, group_id);
            }

            //remove the user from Users
            stmt = con.createStatement();
            stmt.executeUpdate(users);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
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
     * @param group_id
     * @param name
     * @param amt
     * @param date
     * @param desc
     * @return if the bill addition was successful
     */
    boolean addBill(String user, String group_id, String name, double amt, String date, String desc) {
        nullEmail(user);
        nullGroup(group_id);
        validDueDate(date);

        //DOESN'T CHECK IF THE USER OR GROUP EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

        String newBillId = generateId();
//        while (billExists(newBillId)) {
//            newBillId = generateId();
//        }

        String bills = "INSERT INTO Bills VALUES ('" + newBillId + "', '" + name + "', " + user +
                ", " + amt + ", '" + date + "', '" + desc + "')";
        String groupBills = "INSERT INTO GroupBills VALUES('" + group_id + "', '" + newBillId + "')";
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

    /**
     * Deletes the specified bill from the database
     * @param user_id
     * @param group_id
     * @param bill_id
     * @return whether the delete was successful
     */
    boolean deleteBill(String user_id, String group_id, String bill_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        //DOESN'T CHECK IF THE USER/GROUP/BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON
        //IMPLEMENTATION

        String groupBills = "DELETE FROM GroupBills WHERE group_id = ? AND bill_id = ?";
        String bills = "DELETE FROM Bills WHERE bill_id = ? AND user_id = " + user_id;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(groupBills);
            stmt.setString(1, group_id);
            stmt.setString(2, bill_id);
            stmt.executeUpdate();

            stmt = con.prepareStatement(bills);
            stmt.setString(1, bill_id);
            stmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the bill's due date in the database
     * @param bill_id
     * @param due_date
     * @return whether the update was successful
     */
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

    /**
     * Updates the bill's amount in the database
     * @param bill_id
     * @param amt
     * @return whether the update was successful
     */
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
     * Returns a list of the email addresses of members in the group
     * @param group_id
     * @return list of members
     */
    ArrayList<String> groupMembers(String group_id) {
        ArrayList<String> members = new ArrayList<String>();
        String query = "SELECT user_id FROM UserGroups WHERE group_id = '" + group_id + "'";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                members.add(rs.getString("user_id"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return members;
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
     * Finds all information on all the bills assigned to the group
     * @param group_id of the group
     * @return a ResultSet including the bill_id, bill_name, amount, due_date, and description
     * for each of the group's bills
     */
    ResultSet groupBills(String group_id) {
        nullGroup(group_id);

        //DOESN'T CHECK IF THE GROUP EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String query = "SELECT bill_id, bill_name, amount, due_date, description FROM Bills " +
                "WHERE group_id = '" + group_id + "'";
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
            String userGroups = "INSERT INTO UserGroups VALUES (" + user_id + ", '" + group_id + "')";
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

        String groupBills = "DELETE FROM GroupBills WHERE group_id = ? AND bill_id = ?";
        String bills = "DELETE FROM Bills WHERE bill_id = ? AND user_id = " + user_id;
        String userGroups = "DELETE FROM UserGroups WHERE user_id = " + user_id + " AND group_id = ?";
        PreparedStatement s = null;

        ResultSet usersBills = userBillsInGroup(user_id, group_id);
        try {
            while (usersBills != null && usersBills.next()) {
                bill_id = usersBills.getString("bill_id");

                s = con.prepareStatement(groupBills);
                s.setString(1, group_id);
                s.setString(2, bill_id);
                s.executeUpdate();

                s = con.prepareStatement(bills);
                s.setString(1, bill_id);
                s.executeUpdate();
            }
            s = con.prepareStatement(userGroups);
            s.setString(1, group_id);
            s.executeUpdate();

            if (groupParticipation(group_id) == 0)
                deleteGroup(group_id);

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a group and all of its associated bill information for all users
     * @param group_id of the group
     * @return whether the delete was successful
     */
    boolean deleteGroup(String group_id) {
        nullGroup(group_id);

        String groups = "DELETE FROM Groups WHERE group_id = ?";
        String userGroups = "DELETE FROM UserGroups WHERE group_id = ?";
        String groupBills = "DELETE FROM GroupBills WHERE group_id = ?";
        String bills = "DELETE FROM Bills WHERE bill_id = ?";
        PreparedStatement s = null;

        String bill_id = null;

        ResultSet groupsBills = groupBills(group_id);
        try {
            s = con.prepareStatement(userGroups);
            s.setString(1, group_id);
            s.executeUpdate();

            s = con.prepareStatement(groupBills);
            s.setString(1, group_id);
            s.executeUpdate();

            s = con.prepareStatement(groups);
            s.setString(1, group_id);
            s.executeUpdate();

            while (groupsBills != null && groupsBills.next()) {
                bill_id = groupsBills.getString("bill_id");
                s = con.prepareStatement(bills);
                s.setString(1, bill_id);
                s.executeUpdate();
            }

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
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

        String query = "SELECT COUNT(DISTINCT user_id) FROM UserGroups WHERE group_id = '" +
                group_id + "'";
        Statement stmt = null;
        ResultSet rs = null;
        int count = -1;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next())
                count = rs.getInt(1);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

}
