package controllers;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //For correction: Replace with your database's IP.
    public static final String HOST = "192.168.1.71";
    private static final String DATABASE_NAME = "productosdb";
    private static final String PORT = "3306";
    private static final String USER = "yosoyroot";
    private static final String PASSWORD = "MANAGER";
    private static final String DATABASE_URL = "jdbc:mysql://"+ HOST + ":" + PORT + "/" + DATABASE_NAME;

    //-----------------------------------------------------------

    public static Connection createConnection(){
        try {
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean closeConnection(Connection conn){
        try {
            if(conn == null) return false;
            conn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //-----------------------------------------------------------


}
