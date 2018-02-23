package com.example.kallyruan.roommateexpense; /**
 * Created by Kelly on 2/22/2018.
 */
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnect {

    static Connection getConnection() {
        Connection con = null;
        try {
            String hostname = "cis350project.crthvh2wxus6.us-east-2.rds.amazonaws.com";
            Class.forName("com.mysql.jdbc.Driver");
            String port = "3306";
            String dbName = "CIS350APP";
            String userName = "roommatespending";
            String password = "tracker350";
            String connectString = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName +
                    "?user=" + userName + "&password=" + password;
            con = DriverManager.getConnection(connectString);

        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
}
