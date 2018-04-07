package com.example.kallyruan.roommateexpense.DB; /**
 * Created by Kelly on 2/22/2018.
 * This class returns a connection object used to connect to a MySQL database.
 */

import java.sql.*;
import android.content.res.Resources;

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
            String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" +
                    userName + "&password=" + password;
            con = DriverManager.getConnection(url);

        } catch (SQLException err) {
            System.out.println(err.getMessage());
            err.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
}
