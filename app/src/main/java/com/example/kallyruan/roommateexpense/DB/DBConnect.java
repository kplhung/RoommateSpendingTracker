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
            String hostname = DBConstants.host_name;
            Class.forName(DBConstants.driver);
            String port = DBConstants.port;
            String dbName = DBConstants.db_name;
            String userName = DBConstants.db_user;
            String password = DBConstants.db_password;
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
