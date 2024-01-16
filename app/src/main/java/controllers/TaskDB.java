package controllers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import models.Task;

public class TaskDB {

    //-------------------------------------------------------------------------------

    public static boolean saveTask(Task task, String email){
        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "INSERT INTO kraTASKS(idTask,email,name,date,priority,isChecked) values(?,?,?,?,?,?)";
        PreparedStatement sentence = null;
        try {
            sentence = conn.prepareStatement(sqlSentence);
            sentence.setBigDecimal(1, null);
            sentence.setString(2, email);
            sentence.setString(3, task.getName());
            sentence.setDate(4, new Date(task.getDate().getTime()));
            sentence.setString(5, "" + task.getPriority().toString().charAt(0));
            sentence.setInt(6, task.isChecked() ? 1 : 0);
            int result = sentence.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        } finally{
            try {
                sentence.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    //-------------------------------------------------------------------------------

    public static boolean deleteTask(Task task){
        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "DELETE FROM kraTASKS WHERE idTask = ?";
        PreparedStatement sentence = null;
        try {
            sentence = conn.prepareStatement(sqlSentence);
            sentence.setBigDecimal(1, new BigDecimal(task.getId()));
            int result = sentence.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        } finally{
            try {
                sentence.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------

}
