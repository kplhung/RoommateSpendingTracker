package com.example.kallyruan.roommateexpense.DB;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Contains functions for all database queries supported in the app
 * Created by evephelps on 3/9/18.
 */

public class DBQueries {
    private static DBQueries ourInstance = new DBQueries();
    private final Connection con;
    private final String alphabet = DBConstants.code_alphabet;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DBConstants.date_format);

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
     *
     * @param email
     */
    public void nullEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Cannot have an empty user id");
        }
    }

    /**
     * Throws an IllegalArgumentException if the password is null
     *
     * @param p
     */
    public void nullPassword(String p) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot have an empty password");
        }
    }

    /**
     * Throws an IllegalArgumentException if the group_id is null
     *
     * @param group_id
     */
    public void nullGroup(String group_id) {
        if (group_id == null) {
            throw new IllegalArgumentException("Cannot have an empty group id");
        }
    }

    /**
     * Throws an IllegalArgumentException if the date is null, isn't the right length, or isn't
     * correctly formatted
     *
     * @param date
     */
    public void validDueDate(String date) {
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
     *
     * @param name
     */
    public void nullGroupName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Chosen group name cannot be empty");
        }

        if (name.length() > 20) {
            throw new IllegalArgumentException("The group name must be max 20 characters");
        }
    }

    /**
     * Used for generating both group_id's and bill_id's
     * Generates a random "length" character sequence based on the English alphabet and integers 0-9
     *
     * @return the random "length" character string
     */
    public String generateCode(int length) {
        StringBuilder testId = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
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
     *
     * @param email    of the user
     * @param password of the user
     * @return 0 if the email doesn't exist in the database, 1 if the password was incorrect,
     * 2 if login was successful
     */
    public int login(String email, String password) {
        nullEmail(email);
        nullPassword(password);

        boolean exists = userExists(email);

        if (exists) {
            String query = "SELECT password FROM Users WHERE user_id = ?";
            ResultSet rs = null;
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                rs.next();
                String realPassword = rs.getString("password");

                if (realPassword.equals(password)) {
                    return 2; //successful login
                } else {
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
     *
     * @param email of the user
     * @return true if exists, false otherwise
     */
    public boolean userExists(String email) {
        nullEmail(email);

        String query = "SELECT * FROM Users WHERE user_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if given password matches user's password
     * @param email       of user
     * @param oldPassword of user
     * @return true if correct password given, false otherwise
     */
    public boolean enteredCorrectPassword(String email, String oldPassword) {
        /* email is associated with a given user, based on implementation; i.e.,
           user is guaranteed to exist */
        nullEmail(email);
        nullPassword(oldPassword);

        String query = "SELECT password FROM Users WHERE user_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            rs.next();

            if (oldPassword.equals(rs.getString("password"))) {
                Log.v("Correct password", oldPassword); //TODO: remove this
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Signs up the user to the app
     *
     * @param email    of the user
     * @param password of the user
     * @return true if sign up was successful, false if the user already has an account
     */
    public boolean signUp(String email, String password) {
        nullEmail(email);
        nullPassword(password);

        boolean newUser = !userExists(email);

        if (newUser) {
            String query = "INSERT INTO Users VALUES(\"" + email + "\",\"" + password + "\",\"" +
                    "\",\"" + "\",\"" + "\",\"" + "\")";
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
     * Handles changing a user's email address
     * @param oldEmail
     * @param newEmail
     * @return whether the udpate was successful
     */
    public boolean changeEmail(String oldEmail, String newEmail) {
        nullEmail(oldEmail);
        nullEmail(newEmail);

        if (userExists(oldEmail)) {
            //collect old information
            String oldInfo = "SELECT * FROM Users WHERE user_id = ?";
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = con.prepareStatement(oldInfo);
                stmt.setString(1, oldEmail);
                rs = stmt.executeQuery();
                rs.next();
                String pw = rs.getString("password");
                String nickname = rs.getString("nickname");
                String icon_id = rs.getString("icon_id");
                String reset_code = rs.getString("reset_code");
                String expiry_date = rs.getString("expiry_date");

                //add the new email as a new user with all the old information
                String addNewEmail = "INSERT INTO Users VALUES(\"" + newEmail + "\",\"" + pw +
                        "\",\"" + nickname + "\",\"" + icon_id + "\",\"" + reset_code + "\",\"" +
                        expiry_date + "\")";
                stmt.executeUpdate(addNewEmail);

                //update the bills
                String bills = "UPDATE Bills SET user_id = ? WHERE user_id = ?";
                PreparedStatement pstmt = con.prepareStatement(bills);
                pstmt.setString(1, newEmail);
                pstmt.setString(2, oldEmail);
                pstmt.executeUpdate();

                //update the groups
                String groups = "UPDATE UserGroups SET user_id = ? WHERE user_id = ?";
                pstmt = con.prepareStatement(groups);
                pstmt.setString(1, newEmail);
                pstmt.setString(2, oldEmail);
                pstmt.executeUpdate();

                //update the paid bills
                String oldBills = "UPDATE OldUserBills SET user_id = ? WHERE user_id = ?";
                pstmt = con.prepareStatement(oldBills);
                pstmt.setString(1, newEmail);
                pstmt.setString(2, oldEmail);
                pstmt.executeUpdate();

                //remove the old email from Users
                String remove = "DELETE FROM Users WHERE user_id = ?";
                pstmt = con.prepareStatement(remove);
                pstmt.setString(1, oldEmail);
                stmt.executeUpdate();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Stores a reset code for the user in the database, as well as the day that the code
     * was created.
     *
     * @param email the email of the user who forgot his/her password
     * @return the 6-char code needed to reset the user's password, null if the user doesn't
     * exist or the database otherwise fails to set a code
     */
    public String forgotPassword(String email) {
        nullEmail(email);

        if (userExists(email)) {
            String code = generateCode(6);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 7); //one week from today
            String expiry = dateFormat.format(c.getTime());

            String query = "UPDATE Users SET reset_code = ?, expiry_date = ? WHERE user_id = ?";
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, code);
                stmt.setString(2, expiry);
                stmt.setString(3, email);
                stmt.executeUpdate();

                return code;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Determine if the user should be able to update his/her password
     *
     * @param email the user
     * @param code  the 6 character code required to allow password update
     * @return 2 if the update is allowed, 1 if the code already expired, 0 if if the
     * code is incorrect or the queries failed
     */
    public int allowReset(String email, String code) {
        nullEmail(email);

        if (userExists(email)) {
            String reset_code = "SELECT reset_code FROM Users WHERE user_id = ?";
            String expiry_date = "SELECT expiry_date FROM Users WHERE user_id = ?";
            ResultSet rs = null;
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement(reset_code);
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                rs.next();
                String storedCode = rs.getString("reset_code");

                stmt = con.prepareStatement(expiry_date);
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                rs.next();
                String date = rs.getString("expiry_date");
                Date expiry = dateFormat.parse(date);
                Date today = new Date();

                if (code != null && code.equals(storedCode)) {
                    //code hasn't expired yet, and it's the right code
                    if (today.before(expiry)) {
                        //edit the expiry date so the code is no longer valid
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, -1); //set it to yesterday
                        String newExpiry = dateFormat.format(c.getTime());

                        String query = "UPDATE Users SET expiry_date = ? WHERE user_id = ?";
                        PreparedStatement update = null;
                        update = con.prepareStatement(query);
                        update.setString(1, newExpiry);
                        update.setString(2, email);
                        update.executeUpdate();

                        return 2;
                    }
                    //code is correct but it already expired
                    else {
                        return 1;
                    }
                }
                //code is incorrect
                else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Handles resetting the user's password with the new one
     * @param user     the user
     * @param password the new password
     * @return whether the update was successful
     */
    public boolean resetPassword(String user, String password) {
        nullEmail(user);
        nullPassword(password);

        if (userExists(user)) {
            String query = "UPDATE Users SET password = ? WHERE user_id = ?";
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, password);
                stmt.setString(2, user);
                stmt.executeUpdate();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Handles setting the user's nickname
     * @param user     the user
     * @param nickname the desired nickname
     * @return whether the update was successful
     */
    public boolean setNickname(String user, String nickname) {
        nullEmail(user);

        if (userExists(user)) {
            String query = "UPDATE Users SET nickname = ? WHERE user_id = ?";
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, nickname);
                stmt.setString(2, user);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Handles getting the user's nickname
     * @param user the user
     * @return the desired nickname, null if it hasn't been set
     */
    public String getNickname(String user) {
        nullEmail(user);

        String nickname = null;

        if (userExists(user)) {
            String query = "SELECT nickname FROM Users WHERE user_id = ?";
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, user);
                rs = stmt.executeQuery();
                rs.next();

                nickname = rs.getString("nickname");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nickname;
    }

    /**
     * Handles setting the user's icon
     * @param user the user
     * @param icon the desired icon
     * @return whether the update was successful
     */
    public boolean setIcon(String user, String icon) {
        nullEmail(user);

        if (userExists(user)) {
            String query = "UPDATE Users SET icon_id = ? WHERE user_id = ?";
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, icon);
                stmt.setString(2, user);
                stmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Handles getting the user's icon
     * @param user the user
     * @return the desired icon, null if it hasn't been set
     */
    public String getIcon(String user) {
        nullEmail(user);

        String icon = null;

        if (userExists(user)) {
            String query = "SELECT icon_id FROM Users WHERE user_id = ?";
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = con.prepareStatement(query);
                stmt.setString(1, user);
                rs = stmt.executeQuery();
                rs.next();

                icon = rs.getString("icon_id");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return icon;
    }

    /**
     * Handles safe deletion of a user's account from the database
     * @param email
     * @return whether the deletion was successful
     */
    public boolean deleteAccount(String email) {
        nullEmail(email);

        String users = "DELETE FROM Users WHERE user_id = ?";
        PreparedStatement stmt = null;

        String group_id = null;

        ResultSet usersGroups = userGroups(email);
        ResultSet oldBills = getOldBills(email);
        try {
            //delete all bills and groups -- all bills must be assigned an associated group
            while (usersGroups != null && usersGroups.next()) {
                group_id = usersGroups.getString("group_id");
                leaveGroup(email, group_id);
            }

            //delete all paid bills from the database
            while (oldBills != null && oldBills.next()) {
                String bill_id = oldBills.getString("bill_id");
                String query = "DELETE FROM OldBills WHERE bill_id = ?";
                stmt = con.prepareStatement(query);
                stmt.setString(1, bill_id);
                stmt.executeUpdate();
            }
            //remove from OldUserBills
            String query = "DELETE FROM OldUserBills WHERE user_id = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.executeUpdate();

            //remove the user from Users
            stmt = con.prepareStatement(users);
            stmt.setString(1, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * BILL QUERIES
     */

    /**
     * Checks whether the bill_id is already in use by another bill
     *
     * @param bill_id unique identifier for the bill
     * @return true if the bill id is already being used, false otherwise
     */
    public boolean billExists(String bill_id) {
        String query = "SELECT * FROM Bills WHERE bill_id = '" + bill_id + "'";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if (!rs.next()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Finds all information on all the bills assigned to the user
     *
     * @param email of the user
     * @return a ResultSet including the bill_id, bill_name, amount, due_date, and description
     * for each of the user's bills
     */
    public ResultSet userBills(String email) {
        nullEmail(email);

        //DOESN'T CHECK IF THE USER EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String query = "SELECT bill_id, bill_name, amount, due_date, description FROM Bills " +
                "WHERE user_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds all the information on all the bills assigned to the user in a given group
     *
     * @param email    of the user
     * @param group_id of the group
     * @return a ResultSet including the bill_id, bill_name, amount, due_date, and description
     * for each of the user's bills
     */
    public ResultSet userBillsInGroup(String email, String group_id) {
        nullEmail(email);
        nullGroup(group_id);

        //DOESN'T CHECK IF THE USER OR GROUP EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

        String query = "SELECT B.bill_id, B.bill_name, B.amount, B.due_date, B.description FROM (Bills AS " +
                "B INNER JOIN (SELECT bill_id FROM GroupBills WHERE group_id = ?) " +
                "AS G ON B.bill_id = G.bill_id) WHERE user_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, group_id);
            stmt.setString(2, email);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a bill based on the inputted information
     *
     * @param user
     * @param group_id
     * @param name
     * @param amt
     * @param date
     * @param desc
     * @return if the bill addition was successful
     */
    public boolean addBill(String user, String group_id, String name, double amt, String date, String desc) {
        nullEmail(user);
        nullGroup(group_id);
        validDueDate(date);

        //DOESN'T CHECK IF THE USER OR GROUP EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

        String newBillId = generateCode(10);
        while (billExists(newBillId)) {
            newBillId = generateCode(10);
        }
        String bills = "INSERT INTO Bills VALUES (\"" + newBillId + "\", \"" + name + "\", \"" +
                user + "\", \"" + amt + "\", \"" + date + "\", \"" + desc + "\")";
        String groupBills = "INSERT INTO GroupBills VALUES(\"" + group_id + "\", \"" + newBillId +
                "\")";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            stmt.executeUpdate(groupBills);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes the specified bill from the database, and adds it to the user's paid bills list
     * @param user_id
     * @param group_id
     * @param bill_id
     * @return whether the delete was successful
     */
    public boolean deleteBill(String user_id, String group_id, String bill_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        //DOESN'T CHECK IF THE USER/GROUP/BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON
        //IMPLEMENTATION

        String billInfo = "SELECT * FROM Bills WHERE bill_id = ?";
        String groupBills = "DELETE FROM GroupBills WHERE group_id = ? AND bill_id = ?";
        String bills = "DELETE FROM Bills WHERE bill_id = ? AND user_id = ?";
        PreparedStatement stmt = null;

        try {
            // add the bill to the user's old bills
            String group_name = getGroupName(group_id);
            stmt = con.prepareStatement(billInfo);
            stmt.setString(1, bill_id);
            ResultSet info = stmt.executeQuery();
            String description = "";
            double amount = 0.0;
            String billName = "";

            //there's a bill matching that id
            if (info.next()) {
                billName = info.getString("bill_name");
                description = info.getString("description");
                amount = info.getDouble("amount");
            }
            Log.v("tried to do this", "" + addOldBill(bill_id, billName, user_id,
                    group_name, amount, description));

            stmt = con.prepareStatement(groupBills);
            stmt.setString(1, group_id);
            stmt.setString(2, bill_id);
            stmt.executeUpdate();

            stmt = con.prepareStatement(bills);
            stmt.setString(1, bill_id);
            stmt.setString(2, user_id);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the bill's due date in the database
     *
     * @param bill_id
     * @param due_date
     * @return whether the update was successful
     */
    public boolean changeDueDate(String bill_id, String due_date) {
        validDueDate(due_date);

        //DOESN'T CHECK IF THE BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String bills = "UPDATE Bills SET due_date = '" + due_date + "' WHERE bill_id = '" + bill_id + "'";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the bill's amount in the database
     *
     * @param bill_id
     * @param amt
     * @return whether the update was successful
     */
    public boolean changeAmount(String bill_id, double amt) {
        //DOESN'T CHECK IF THE BILL EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String bills = "UPDATE Bills SET amount = " + amt + " WHERE bill_id = '" + bill_id + "'";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bills);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Handles finding the group_id associated with a given bill_id
     *
     * @param bill_id
     * @return the associated group_id, null if it doesn't exist
     */
    public String getGroupIdForBill(String bill_id) {
        if (billExists(bill_id)) {
            try {
                String group = "SELECT * FROM GroupBills WHERE bill_id = '" + bill_id + "'";
                ResultSet rs = null;
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(group);

                if (rs.next()) {
                    return rs.getString("group_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * OLD BILL QUERIES
     */

    /**
     * Checks whether that bill_id is being used in OldBills
     * @param bill_id the bill id
     * @return true if the bill_id is in use, false otherwise
     */
    boolean oldBillExists(String bill_id) {
        String query = "SELECT * FROM OldBills WHERE bill_id = '" + bill_id + "'";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if (!rs.next()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns all the old bills associated with the given user
     * @param user_id the user id
     * @return a ResultSet of the old bills, null if the query fails
     */
    public ResultSet getOldBills(String user_id) {
        nullEmail(user_id);

        //DOESN'T CHECK IF THE USER EXISTS -- CAN ADD THIS, BUT SHOULD BE GUARANTEED
        //BASED ON IMPLEMENTATION

        String query = "SELECT B.bill_id, B.bill_name, B.group_name, B.amount, B.description, " +
                "B.date_paid FROM (OldBills AS B INNER JOIN (SELECT bill_id FROM OldUserBills WHERE " +
                "user_id = ?) AS O ON B.bill_id = O.bill_id)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, user_id);
            rs = stmt.executeQuery();
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
}

    /**
     * Handles inserting an old bill into the table of paid bills
     * @param bill_id
     * @param user_id
     * @param group_name
     * @param amount
     * @param description
     * @return whether the insertion was successful
     */
    boolean addOldBill(String bill_id, String bill_name, String user_id, String group_name, double amount,
                       String description){
        //the bill_id must be unique
        while (oldBillExists(bill_id)) {
            bill_id = generateCode(10);
        }
        Calendar c = Calendar.getInstance();
        String date = dateFormat.format(c.getTime()); //the bill was paid today

        String oldBills = "INSERT INTO OldBills VALUES (\"" + bill_id + "\", \"" + bill_name +
                "\", \"" + group_name + "\", \"" + amount + "\", \"" + description + "\", \"" +
                date + "\")";
        String oldUserBills = "INSERT INTO OldUserBills VALUES (\"" + user_id + "\", \"" +
                bill_id + "\")";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(oldBills);
            stmt.executeUpdate(oldUserBills);

            return true;
        } catch (SQLException e) {
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
    public boolean groupExists(String group_id) {
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
     * Finds all the groups that a user is in
     * @param email of the user
     * @return the group_ids of the groups, in a ResultSet
     */
    public ResultSet userGroups(String email) {
        nullEmail(email);

        //DOESN'T CHECK IF THE USER EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String query = "SELECT group_id FROM UserGroups WHERE user_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks whether that group invite code is already being used
     * @param code
     * @return if the code is in use
     */
    public boolean codeInUse(String code) {
        String query = "SELECT * FROM GroupCodes WHERE invite_code = '" + code + "'";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if (!rs.next()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Generates, stores, and returns the invite code for joining the group.
     * @param group_id
     * @return
     */
    public String getInviteCode(String group_id) {
        nullGroup(group_id);

        if (groupExists(group_id)) {
            String code = generateCode(6);
            while (codeInUse(code)) {
                code = generateCode(6);
            }
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 7); //one week from today
            String expiry = dateFormat.format(c.getTime());

            String query = "INSERT INTO GroupCodes VALUES(\"" + code + "\",\"" + expiry + "\",\""
                    + group_id + "\")";
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                stmt.executeUpdate(query);
                return code;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Returns the group_id associated with the invite code, null if it doesn't exist
     * @param code
     * @return the associated group_id
     */
    public String getGroupForCode(String code) {
        if (codeInUse(code)) {
            String query = "SELECT group_id FROM GroupCodes WHERE invite_code = '" + code + "'";
            Statement stmt = null;
            ResultSet rs = null;

            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);

                if (!rs.next()) {
                    return rs.getString("group_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Returns a list of the email addresses of members in the group
     * @param group_id
     * @return list of members
     */
    public ArrayList<String> getGroupMembers(String group_id) {
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
    public String getGroupName(String group_id) {
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
    public ResultSet groupBills(String group_id) {
        nullGroup(group_id);

        //DOESN'T CHECK IF THE GROUP EXISTS, BUT SHOULD BE GUARANTEED BASED ON IMPLEMENTATION

        String query = "SELECT B.bill_id, B.bill_name, B.amount, B.due_date, B.description FROM " +
                "(Bills AS B INNER JOIN (SELECT bill_id FROM GroupBills WHERE group_id = ?) " +
                "AS G ON B.bill_id = G.bill_id)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, group_id);
            rs = stmt.executeQuery();
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
     * @return the group_id if successful, null otherwise
     */
    public String createGroup(String user_id, String group_name) {
        nullEmail(user_id);
        nullGroupName(group_name);

        String group_id = generateCode(10);
        while (groupExists(group_id)) {
            group_id = generateCode(10);
        }

        String groups = "INSERT INTO Groups VALUES (\"" + group_id + "\", \"" + group_name + "\")";
        String userGroups = "INSERT INTO UserGroups VALUES (\"" + user_id + "\", \"" + group_id + "\")";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(groups);
            stmt.executeUpdate(userGroups);

            return group_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds the specified user to the specified group
     * @param user_id
     * @param code
     * @return 0 if the code was wrong, 1 if the code had expired, 2 if the user doesn't exist in
     * the database, 3 if the addition was successful
     */
    public int addUserToGroup(String user_id, String code) {
        nullEmail(user_id);

        if (userExists(user_id)) {
            String query = "SELECT * FROM GroupCodes WHERE invite_code = '" + code + "'";
            ResultSet rs = null;
            Statement stmt = null;

            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);

                if (!rs.next()) {
                    return 0; //code doesn't exist in the database
                }
                String group_id = rs.getString("group_id");
                String date = rs.getString("expiry_date");
                Date expiry = dateFormat.parse(date);
                Date today = new Date();

                if (today.before(expiry)) { //code exists and hasn't expired
                    //remove the code from GroupCodes now that it's been used
                    String remove = "DELETE FROM GroupCodes WHERE invite_code = ?";
                    PreparedStatement s = null;
                    s = con.prepareStatement(remove);
                    s.setString(1, code);
                    s.executeUpdate();

                    //add the user to the group
                    String userGroups = "INSERT INTO UserGroups VALUES (\"" + user_id + "\", \""
                            + group_id + "\")";
                    Statement add = null;
                    add = con.createStatement();
                    add.executeUpdate(userGroups);
                    return 3;
                }
                //code exists but already expired
                else {
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //user doesn't exist in the database
        else {
            return 2;
        }
        return 0; //failed otherwise
    }

    /**
     * Removes the user from the group and updates all tables accordingly
     * @param user_id
     * @param group_id
     * @return if the removal was successful
     */
    public boolean leaveGroup(String user_id, String group_id) {
        nullEmail(user_id);
        nullGroup(group_id);

        String bill_id = null;

        String groupBills = "DELETE FROM GroupBills WHERE group_id = ? AND bill_id = ?";
        String bills = "DELETE FROM Bills WHERE bill_id = ? AND user_id = ?";
        String userGroups = "DELETE FROM UserGroups WHERE user_id = ? AND group_id = ?";
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
                s.setString(2, user_id);
                s.executeUpdate();
            }
            s = con.prepareStatement(userGroups);
            s.setString(1, user_id);
            s.setString(2, group_id);
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
    public boolean deleteGroup(String group_id) {
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
    public int groupParticipation(String group_id) {
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
