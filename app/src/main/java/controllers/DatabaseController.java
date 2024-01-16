package controllers;

import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controllers.operations.DeleteTaskOp;
import controllers.operations.InsertTaskOp;
import models.Task;

public class DatabaseController {

    private static final int TIMEOUT = 5000;
    public static boolean saveTask(Task task, String email) {
        FutureTask t = new FutureTask(new InsertTaskOp(task, email));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
        }
        return success;
    }

    public static boolean deleteTask(Task task){
        FutureTask t = new FutureTask(new DeleteTaskOp(task));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean success = false;
        try {
            success = (boolean) t.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
        }
        return success;
    }

}
