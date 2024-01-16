package controllers.operations;

import java.util.concurrent.Callable;

import controllers.TaskDB;
import models.Task;

public class InsertTaskOp implements Callable<Boolean> {

    private Task task;
    private String email;

    public InsertTaskOp(Task task, String email){
        this.task = task;
        this.email = email;
    }

    @Override
    public Boolean call(){
        return TaskDB.saveTask(task, email);
    }
}
