package controllers;

import android.util.Log;

import com.example.todoapp.TaskListActivity;

import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Task;

public class TaskDB {

    public static ArrayList<Task> loadTasks(String email) {
        Connection conn = DBConnection.createConnection();
        if (conn == null) return null;

        String sqlSentence = "SELECT * FROM kraTASKS WHERE email = ?";
        PreparedStatement preparedStatement = null;

        ArrayList<Task> tasks = null;
        try {
            preparedStatement = conn.prepareStatement(sqlSentence);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            Task task;
            tasks = new ArrayList<>();
            while(rs.next()){
                int id = (Integer) rs.getObject("idTask");
                String name = (String) rs.getObject("name");
                java.util.Date date = new java.util.Date(((java.sql.Date) rs.getObject("date")).getTime());
                String priorityS = (String) rs.getObject("priority");
                Task.Priority priority = Task.Priority.LOW;
                switch(priorityS.toUpperCase()){
                    case "H":
                        priority = Task.Priority.HIGH;
                        break;
                    case "M":
                        priority = Task.Priority.MED;
                        break;
                }
                boolean isChecked = (Boolean) rs.getObject("isChecked");
                task = new Task(id, name, date, priority, isChecked);
                tasks.add(task);
            }
        } catch (SQLException e) {
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
        return tasks;
    }

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

    public static boolean checkTask(Task task, boolean isChecked){
        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "UPDATE kraTASKS SET isChecked = ? WHERE idTask = ?";
        PreparedStatement sentence = null;
        try {
            sentence = conn.prepareStatement(sqlSentence);
            sentence.setInt(1, isChecked ? 1 : 0);
            sentence.setBigDecimal(2, new BigDecimal(task.getId()));
            int result = sentence.executeUpdate();
            Log.i("MyApp", "Result: " + result);
            return result > 0;
        } catch (SQLException e) {
            Log.i("MyApp", "Error checking task: " + e.toString());
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

}
