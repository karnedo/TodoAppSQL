package controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Task;

public class ProfileDB {

    public static Boolean logIn(String email, String password) {
        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "SELECT * FROM kraUSERS WHERE email = ? AND PASSWORD = ?";
        PreparedStatement sentence = null;
        boolean sucess = false;
        try {
            sentence = conn.prepareStatement(sqlSentence);
            sentence.setString(1, email);
            sentence.setString(2, password);
            int result = sentence.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            sucess = false;
        } finally{
            try {
                sentence.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException | NullPointerException e) {}
            return sucess;
        }
    }

    public static Boolean signUp(String email, String password) {
        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "INSERT INTO kraUSERS(email, password) VALUES(?,?)";
        PreparedStatement sentence = null;

        try {
            sentence = conn.prepareStatement(sqlSentence);
            sentence.setString(1, email);
            sentence.setString(2, password);
            int result = sentence.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            Log.i("MyApp", e.toString());
            return false;
        } finally{
            try {
                if(sentence != null) sentence.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException | NullPointerException e) {}
        }
    }

    public static Bitmap getPicture(String email){
        Connection conn = DBConnection.createConnection();
        if (conn == null) return null;

        String sqlSentence = "SELECT picture FROM kraUSERS WHERE email = ?";
        PreparedStatement preparedStatement = null;

        Bitmap picture = null;
        try {
            preparedStatement = conn.prepareStatement(sqlSentence);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            Blob blob = null;
            while(rs.next()){
                blob = rs.getBlob("picture");
            }
            picture = blobToBitmap(blob);
        } catch (SQLException e) {
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
        return picture;
    }
    public static Boolean setPicture(String email, Bitmap picture) {

        Connection conn = DBConnection.createConnection();
        if(conn == null) return false;
        String sqlSentence = "UPDATE kraUSERS SET picture = ? where email = ?";
        PreparedStatement sentence = null;
        byte[] pictureBytes = toByteArray(picture);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pictureBytes);
        try {

            sentence = conn.prepareStatement(sqlSentence);
            sentence.setBinaryStream(1, inputStream, pictureBytes.length);
            sentence.setString(2, email);
            int result = sentence.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            Log.i("MyApp", "Err: " + e);
            return false;
        } finally{
            try {
                if(sentence != null) sentence.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException | NullPointerException e) {}
        }

    }

    private static Bitmap blobToBitmap(Blob blob) {
        try {
            if (blob != null) {
                InputStream inputStream = blob.getBinaryStream();

                byte[] bytes = toByteArray(inputStream);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    private static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 25, outputStream);
        return outputStream.toByteArray();
    }
}
