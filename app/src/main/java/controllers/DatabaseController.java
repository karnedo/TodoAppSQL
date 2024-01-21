package controllers;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.todoapp.TaskListActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controllers.operations.CheckTaskOp;
import controllers.operations.DeleteTaskOp;
import controllers.operations.GetPictureOp;
import controllers.operations.InsertTaskOp;
import controllers.operations.LoadTasksOp;
import controllers.operations.LogInOp;
import controllers.operations.SetPictureOp;
import controllers.operations.SignUpOp;
import models.Task;

public class DatabaseController {

    private static final int TIMEOUT = 5000;

    public static ArrayList<Task> loadTasks(String email, TaskListActivity taskListActivity){
        FutureTask t = new FutureTask(new LoadTasksOp(email));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);

        ArrayList<Task> tasks = null;
        try {
            tasks = (ArrayList<Task>) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
            return tasks;
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return tasks;
        }
    }

    public static boolean saveTask(Task task, String email) {
        FutureTask t = new FutureTask(new InsertTaskOp(task, email));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return success;
        }
    }

    public static boolean deleteTask(Task task){
        FutureTask t = new FutureTask(new DeleteTaskOp(task));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return success;
        }
    }

    public static boolean checkTask(Task task, boolean isChecked){
        FutureTask t = new FutureTask(new CheckTaskOp(task, isChecked));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
        } finally {
            es.shutdown();
            return success;
        }
    }

    public static boolean logIn(String email, String password){
        FutureTask t = new FutureTask(new LogInOp(email, password));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return success;
        }
    }

    public static boolean signUp(String email, String password){
        FutureTask t = new FutureTask(new SignUpOp(email, password));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return success;
        }
    }

    public static Bitmap getProfilePicture(String email){
        FutureTask t = new FutureTask(new GetPictureOp(email));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        Bitmap picture = null;
        try {
            picture = (Bitmap) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return picture;
        }
    }

    public static boolean setProfilePicture(String email, Bitmap picture){
        FutureTask t = new FutureTask(new SetPictureOp(email, picture));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            return success;
        }
    }

}
