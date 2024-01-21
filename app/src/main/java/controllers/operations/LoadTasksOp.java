package controllers.operations;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import controllers.TaskDB;
import models.Task;

public class LoadTasksOp implements Callable<ArrayList<Task>> {

    private String email;

    public LoadTasksOp(String email){
        this.email = email;
    }

    @Override
    public ArrayList<Task> call() throws Exception {
        return TaskDB.loadTasks(email);
    }
}
